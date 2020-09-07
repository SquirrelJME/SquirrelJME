// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.gradle.workers.WorkAction;

/**
 * This performs the actual work of running the VM tests.
 *
 * @since 2020/09/07
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class VMTestWorkAction
	implements WorkAction<MultiVMTestParameters>
{
	/** The timeout for tests. */
	private static final long _TEST_TIMEOUT =
		120_000_000_000L;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/07
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	@Override
	public void execute()
	{
		MultiVMTestParameters parameters = this.getParameters();
		
		// The process might not be able to execute
		try
		{
			// Note this is running
			String testName = parameters.getTestName().get();
			System.err.printf("???? %s%n", testName);
			
			// Clock the starting time
			long clockStart = System.currentTimeMillis();
			long nsStart = System.nanoTime();
			
			// Start the process with the command line that was pre-determined
			Process process = new ProcessBuilder(parameters.getCommandLine()
				.get().toArray(new String[0])).start();
			
			// Wait for the process to terminate, the exit code will contain
			// the result of the test (pass, skip, fail)
			int exitCode = -1;
			for (;;)
				try
				{
					// Has the test run expired?
					long nsDur = System.nanoTime() - nsStart;
					if (nsDur >= VMTestWorkAction._TEST_TIMEOUT)
					{
						// Note it
						System.err.printf("TIME %s%n", testName);
						
						// Stop it now
						process.destroyForcibly();
						break;
					}
					
					// Wait for completion
					if (process.waitFor(10, TimeUnit.SECONDS))
					{
						exitCode = process.waitFor();
						break;
					}
				}
				catch (InterruptedException ignored)
				{
				}
			
			// Clock the ending time
			long nsDur = System.nanoTime() - nsStart;
			
			// Note this has finished
			VMTestResult testResult = VMTestResult.valueOf(exitCode);
			System.err.printf("%4s %s%n", testResult, testName);
			
			// Read all of standard error and output, these will be stored
			// in the log
			byte[] stdOut = MultiVMHelpers.readAll(process.getInputStream());
			byte[] stdErr = MultiVMHelpers.readAll(process.getErrorStream());
			
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
					parameters.getVmName().get(), clockStart, nsDur, stdOut,
					stdErr);
				
				// Make sure everything is written
				out.flush();
			}
		}
		
		// Process failed to execute
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
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
		
		// Determine the counts for the test
		int numTests = 1;
		int numSkipped = (__result == VMTestResult.SKIP ? 1 : 0);
		int numFailed = (__result == VMTestResult.FAIL ? 1 : 0);
		
		// The current timestamp
		String nowTimestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
			LocalDateTime.ofInstant(Instant.ofEpochMilli(__clockStart),
				ZoneId.systemDefault()));
		
		// Open test suite
		__out.printf("<testsuite name=\"%s\" tests=\"%d\" " +
			"skipped=\"%d\" failures=\"%d\" errors=\"%d\" " +
			"timestamp=\"%s\" hostname=\"%s\" time=\"%.3f\" " +
			">",
			__testName, numTests, numSkipped, numFailed, numFailed,
			nowTimestamp, __vmName, __nsDur / 1_000_000D);
		__out.println();
		
		// No properties are used
		__out.println("<properties />");
		
		// Output the single test case
		__out.printf("<testcase name=\"%s\" classname=\"%s\" " +
			"time=\"%.3f\" " +
			"/>",
			__testName, __testName, __nsDur / 1_000_000D);
		
		// Write both buffers
		VMTestWorkAction.__writeXmlText(__out, "system-out", __stdOut);
		VMTestWorkAction.__writeXmlText(__out, "system-out", __stdErr);
		
		// Close test suite
		__out.println("</testsuite>");
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
	private static void __writeXmlText(PrintStream __out, String __key,
		byte[] __text)
		throws NullPointerException
	{
		if (__out == null || __key == null || __text == null)
			throw new NullPointerException("NARG");
		
		// Start section
		__out.printf("<%s><![CDATA[", __key);
		
		// Write all the bytes
		__out.print(new String(__text));
		
		// End section
		__out.printf("]]></%s>", __key);
		__out.println();
	}
}
