// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.GradleLoggerOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.gradle.workers.WorkAction;

/**
 * This performs the actual work of running the VM tests.
 *
 * @since 2020/09/07
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class VMTestWorkAction
	implements WorkAction<VMTestParameters>
{
	/** Logger storage. */
	static final Map<String, __LogHolder__> _LOGGERS =
		new ConcurrentHashMap<>();
	
	/** The timeout for tests, in nanoseconds. */
	public static final long TEST_TIMEOUT =
		360_000_000_000L;
	
	/** Skip sequence special. */
	private static final byte[] _SKIP_SPECIAL =
		new byte[]{'%', '!', 'S', 'k', 'O', 'n', 'T', 'i', '!', '%'};
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/07
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	@Override
	public void execute()
	{
		// Determine the name of the test
		VMTestParameters parameters = this.getParameters();
		String testName = parameters.getTestName().get();
		
		// Get the log holder
		__LogHolder__ logHolder = VMTestWorkAction._LOGGERS.get(
			parameters.getUniqueId().get());
		Logger logger = logHolder.logger;
		
		// Threads for processing stream data
		Thread stdOutThread = null;
		Thread stdErrThread = null;
		
		// The current and total test IDs, used to measure progress
		int count = parameters.getCount().get();
		int total = parameters.getTotal().get();
		
		// If we are debugging, we do not want to kill the test by a timeout
		// if it takes forever because we might be very slow at debugging
		boolean isDebugging = VMTestTaskAction.isDebugging();
		
		// The process might not be able to execute
		Process process = null;
		try
		{
			// Note this is running
			logger.lifecycle(String.format("???? %s [%d/%d]",
				testName, count, total));
			
			// Clock the starting time
			long clockStart = System.currentTimeMillis();
			long nsStart = System.nanoTime();
			
			// Setup output handler
			ProcessBuilder processBuilder = new ProcessBuilder(
				parameters.getCommandLine().get().toArray(new String[0]));
			processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
			processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
			
			// Start the process with the command line that was pre-determined
			process = processBuilder.start();
			
			// Setup listening buffer threads
			VMTestOutputBuffer stdOut = new VMTestOutputBuffer(
				process.getInputStream(), new GradleLoggerOutputStream(logger
				, LogLevel.LIFECYCLE, count, total),
				false);
			VMTestOutputBuffer stdErr = new VMTestOutputBuffer(
				process.getErrorStream(), new GradleLoggerOutputStream(logger
				, LogLevel.ERROR, count, total), 
				true);
			
			// Setup threads for reading standard output and standard error
			stdOutThread = new Thread(stdOut, "stdOutReader");
			stdErrThread = new Thread(stdErr, "stdErrReader");
			
			// Start both threads so console lines can be read as they appear
			stdOutThread.start();
			stdErrThread.start();
			
			// Wait for the process to terminate, the exit code will contain
			// the result of the test (pass, skip, fail)
			int exitCode = -1;
			boolean timeOutHit = false;
			for (;;)
				try
				{
					// Has the test run expired? Only when not debugging
					if (!isDebugging)
					{
						long nsDur = System.nanoTime() - nsStart;
						if (nsDur >= VMTestWorkAction.TEST_TIMEOUT)
						{
							// Note it
							logger.error(String.format("TIME %s [%d/%d]",
								testName, count, total));
							
							// Set timeout as being hit, used for special
							// check
							timeOutHit = true;
							
							// The logic for interrupts is the same
							throw new InterruptedException("Test Timeout");
						}
					}
					
					// Wait for completion
					if (process.waitFor(3, TimeUnit.SECONDS))
					{
						exitCode = process.waitFor();
						break;
					}
				}
				catch (InterruptedException e)
				{
					// Add note that this happened
					logger.error(String.format("INTR %s", testName));
					
					// Stop the processes that are running
					process.destroy();
					stdOutThread.interrupt();
					stdErrThread.interrupt();
					
					// Stop running the loop
					break;
				}
			
			// Clock the ending time
			long nsDur = System.nanoTime() - nsStart;
			
			byte[] stdErrBytes = stdErr.getBytes(stdErrThread);
			if (timeOutHit && VMTestWorkAction.__findTimeoutSkip(stdErrBytes))
				exitCode = VMTestResult.SKIP.exitCode;
			
			// Note this has finished
			VMTestResult testResult = VMTestResult.valueOf(exitCode);
			logger.lifecycle(String.format("%4s %s [%d/%d]",
				testResult, testName, count, total));
			
			// Write the XML file
			try (PrintStream out = new PrintStream(Files.newOutputStream(
				parameters.getResultFile().get().getAsFile().toPath(),
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE)))
			{
				// Write the resultant XML, this will be read later for
				// detection purposes
				VMTestWorkAction.__writeXml(out, testName, testResult,
					parameters.getVmName().get(), clockStart, nsDur,
					stdOut.getBytes(stdOutThread),
					stdErrBytes);
				
				// Make sure everything is written
				out.flush();
			}
		}
		
		// Process failed to execute
		catch (IOException e)
		{
			throw new RuntimeException("I/O Exception in " + testName, e);
		}
		
		// Interrupt read/write threads
		finally
		{
			// If our test process is still alive, stop it
			if (process != null)
				if (process.isAlive())
					process.destroyForcibly();
			
			// Stop the standard output thread from running
			if (stdOutThread != null)
				stdOutThread.interrupt();
			
			// Stop the standard error thread from running
			if (stdErrThread != null)
				stdErrThread.interrupt();
		}
	}
	
	/**
	 * Finds the special timeout sequence if this has had a timeout, this is
	 * used to detect if a test should just skip rather than causing a fail.
	 * 
	 * @param __stdErrBytes The standard error bytes.
	 * @return If the sequence was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/07/18
	 */
	private static boolean __findTimeoutSkip(byte[] __stdErrBytes)
		throws NullPointerException
	{
		if (__stdErrBytes == null)
			throw new NullPointerException("NARG");
		
		// Skip special sequence
		byte[] skipSpecial = VMTestWorkAction._SKIP_SPECIAL;
		byte firstByte = skipSpecial[0];
		int skipLen = skipSpecial.length;
		
		// Find the sequence
		for (int i = 0, n = Math.max(0, __stdErrBytes.length - skipLen);
			i < n; i++)
		{
			// If the first byte is a match, then
			byte quick = __stdErrBytes[i];
			if (quick != firstByte)
				continue;
			
			// Check if the entire sequence matched
			for (int j = 0, q = i; j <= skipLen; j++, q++)
				if (j == skipLen)
					return true;
				else if (__stdErrBytes[q] != skipSpecial[j])
					break;
		}
		
		// Not found
		return false;
	}
	
	/**
	 * Writes the XML test result to the given output.
	 * 
	 * @param __out The stream to write the XML to.
	 * @param __testName The name of the test.
	 * @param __result The result of the test.
	 * @param __vmName The virtual machine name.
	 * @param __clockStart The starting time of the test.
	 * @param __nsDur The duration of the test in nanoseconds.
	 * @param __stdOut Standard output.
	 * @param __stdErr Standard error.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/07
	 */
	@SuppressWarnings("resource")
	private static void __writeXml(PrintStream __out, String __testName,
		VMTestResult __result, String __vmName, long __clockStart,
		long __nsDur, byte[] __stdOut, byte[] __stdErr)
		throws NullPointerException
	{
		if (__out == null || __testName == null || __result == null ||
			__stdOut == null || __stdErr == null || __vmName == null)
			throw new NullPointerException("NARG");
		
		// Write the XML header
		__out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		// Determine the counts for the test
		int numTests = 1;
		int numSkipped = (__result == VMTestResult.SKIP ? 1 : 0);
		int numFailed = (__result == VMTestResult.FAIL ? 1 : 0);
		
		// The current timestamp
		String nowTimestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
			LocalDateTime.ofInstant(Instant.ofEpochMilli(__clockStart),
				ZoneId.systemDefault()));
			
		// Duration in seconds
		double durationSeconds = __nsDur / 1_000_000_000D;
		
		// Open test suite
		__out.printf("<testsuite name=\"%s\" tests=\"%d\" " +
			"skipped=\"%d\" failures=\"%d\" errors=\"%d\" " +
			"timestamp=\"%s\" hostname=\"%s\" time=\"%.3f\" " +
			">",
			__testName, numTests, numSkipped, numFailed, numFailed,
			nowTimestamp, __vmName, durationSeconds);
		__out.println();
		
		// Begin properties
		__out.println("<properties>");
		
		// A special property is used for a quick search to determine if there
		// is a pass, skip, or fail as the test result needs to be read to
		// determine if the task is okay 
		__out.printf("<property name=\"squirreljme.test.result\" " +
			"value=\"%s:result:%s:\" />", VMTestTaskAction._SPECIAL_KEY,
			__result.name());
		__out.println();
		
		// Also
		__out.printf("<property name=\"squirreljme.test.nanoseconds\" " +
			"value=\"%s:nanoseconds:%s:\" />", VMTestTaskAction._SPECIAL_KEY,
			__nsDur);
		__out.println();
		
		// End properties
		__out.println("</properties>");
		
		// Begin test case
		__out.printf("<testcase name=\"%s\" classname=\"%s\" " +
			"time=\"%.3f\">",
			__testName, __testName, durationSeconds);
		__out.println();
		
		// Failed tests use this tag accordingly, despite there being a
		// failures indicator 
		if (__result == VMTestResult.FAIL)
		{
			__out.printf("<failure type=\"%s\">", __testName);
			VMTestWorkAction.__writeText(__out, __stdErr);
			__out.println("</failure>");
		}
		
		// Write both buffers
		VMTestWorkAction.__writeTextTag(__out, "system-out", __stdOut);
		VMTestWorkAction.__writeTextTag(__out, "system-err", __stdErr);
		
		// End test case
		__out.println("</testcase>");
		
		// Close test suite
		__out.println("</testsuite>");
	}
	
	/**
	 * Writes the given XML Text.
	 * 
	 * @param __out The target stream.
	 * @param __text The text to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/12
	 */
	private static void __writeText(PrintStream __out, byte[] __text)
		throws NullPointerException
	{
		if (__out == null || __text == null)
			throw new NullPointerException("NARG");
		
		__out.print("<![CDATA[");
		__out.print(new String(__text));
		__out.print("]]>");
	}
	
	/**
	 * Writes raw buffer text to the output.
	 * 
	 * @param __out The stream to write to.
	 * @param __key The tag key.
	 * @param __text The bytes for the key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/07
	 */
	@SuppressWarnings("resource")
	private static void __writeTextTag(PrintStream __out, String __key,
		byte[] __text)
		throws NullPointerException
	{
		if (__out == null || __key == null || __text == null)
			throw new NullPointerException("NARG");
		
		// Write tag into here
		__out.printf("<%s>", __key);
		VMTestWorkAction.__writeText(__out, __text);
		__out.printf("</%s>", __key);
		__out.println();
	}
}
