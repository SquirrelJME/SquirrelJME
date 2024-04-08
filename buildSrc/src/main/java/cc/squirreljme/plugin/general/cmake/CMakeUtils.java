// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import cc.squirreljme.plugin.util.ForwardInputToOutput;
import cc.squirreljme.plugin.util.ForwardStream;
import cc.squirreljme.plugin.util.PathUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.util.internal.VersionNumber;

/**
 * Utilities for CMake.
 *
 * @since 2024/03/15
 */
public final class CMakeUtils
{
	/**
	 * Not used.
	 *
	 * @since 2024/03/15
	 */
	private CMakeUtils()
	{
	}
	
	/**
	 * Returns the path where CMake is available.
	 *
	 * @return The CMake path.
	 * @since 2024/03/15
	 */
	public static Path cmakeExePath()
	{
		// Standard executable?
		Path cmakePath = PathUtils.findPath("cmake");
		
		// Windows executable?
		if (cmakePath == null)
			cmakePath = PathUtils.findPath("cmake.exe");
		
		// Standard installation on Windows?
		if (cmakePath == null &&
			OperatingSystem.current() == OperatingSystem.WINDOWS)
		{
			String programFiles = System.getenv("PROGRAMFILES");
			if (programFiles != null)
			{
				Path maybe = Paths.get(programFiles).resolve("CMake")
					.resolve("bin").resolve("cmake.exe");
				if (Files.exists(maybe))
					cmakePath = maybe;
			}
		}
		
		// Homebrew on macOS?
		if (cmakePath == null &&
			OperatingSystem.current() == OperatingSystem.MAC_OS)
		{
			Path maybe = Paths.get("/").resolve("opt")
				.resolve("homebrew").resolve("bin")
				.resolve("cmake");
			if (Files.exists(maybe))
				cmakePath = maybe;
		}
		
		return cmakePath;
	}
	
	/**
	 * Requests the version of CMake.
	 *
	 * @return The resultant CMake version or {@code null} if there is no
	 * CMake available.
	 * @since 2024/04/01
	 */
	public static VersionNumber cmakeExeVersion()
	{
		// We need the CMake executable
		Path cmakeExe = CMakeUtils.cmakeExePath();
		if (cmakeExe == null)
			return null;
		
		try
		{
			String rawStr = CMakeUtils.cmakeExecuteOutput("version",
				"--version");
			
			// Read in what looks like a version number
			try (BufferedReader buf = new BufferedReader(
				new StringReader(rawStr)))
			{
				for (;;)
				{
					String ln = buf.readLine();
					if (ln == null)
						break;
					
					// Remove any whitespace and make it lowercase so it is
					// easier to parse
					ln = ln.trim().toLowerCase(Locale.ROOT);
					
					// Is this the version string?
					if (ln.startsWith("cmake version"))
						return VersionNumber.parse(
							ln.substring("cmake version".length()).trim());
				}
			}
			
			// Failed
			throw new RuntimeException(
				"CMake executed but there was no version.");
		}
		catch (IOException __e)
		{
			throw new RuntimeException("Could not determine CMake version.",
				__e);
		}
	}
	
	/**
	 * Executes a CMake task.
	 *
	 * @param __logger The logger to use.
	 * @param __logName The name of the log.
	 * @param __logDir The CMake build directory.
	 * @param __args CMake arguments.
	 * @return The CMake exit value.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/03/15
	 */
	public static int cmakeExecute(Logger __logger, String __logName,
		Path __logDir, String... __args)
		throws IOException
	{
		if (__logDir == null)
			throw new NullPointerException("NARG");
		
		// Output log files
		Path outLog = __logDir.resolve(__logName + ".out");
		Path errLog = __logDir.resolve(__logName + ".err");
		
		// Make sure directories exist first
		Files.createDirectories(outLog.getParent());
		Files.createDirectories(errLog.getParent());
		
		// Run with log wrapping
		try (OutputStream stdOut = Files.newOutputStream(outLog,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
			 OutputStream stdErr = Files.newOutputStream(errLog,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING))
		{
			// Execute CMake
			return CMakeUtils.cmakeExecutePipe(null, stdOut, stdErr,
				__logName, __args);
		}
		
		// Dump logs to Gradle
		finally
		{
			CMakeUtils.dumpLog(__logger,
				LogLevel.LIFECYCLE, outLog);
			CMakeUtils.dumpLog(__logger,
				LogLevel.ERROR, errLog);
		}
	}
	
	/**
	 * Executes a CMake task and returns the output as a string.
	 *
	 * @param __buildType The build type used.
	 * @param __args CMake arguments.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/04/01
	 */
	public static String cmakeExecuteOutput(String __buildType,
		String... __args)
		throws IOException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Run command
			CMakeUtils.cmakeExecutePipe(null, baos, null,
				__buildType, __args);
			
			// Decode to output
			return baos.toString("utf-8");
		}
	}
	
	/**
	 * Executes a CMake task to the given pipe.
	 *
	 * @param __in The standard input for the task.
	 * @param __out The standard output for the task.
	 * @param __err The standard error for the task.
	 * @param __buildType The build type used.
	 * @param __args CMake arguments.
	 * @return The CMake exit value.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/04/01
	 */
	public static int cmakeExecutePipe(InputStream __in,
		OutputStream __out, OutputStream __err, String __buildType,
		String... __args)
		throws IOException
	{
		return CMakeUtils.cmakeExecutePipe(true, __in, __out, __err,
			__buildType, __args);
	}
	
	/**
	 * Executes a CMake task to the given pipe.
	 *
	 * @param __fail Emit failure if exit status is non-zero.
	 * @param __in The standard input for the task.
	 * @param __out The standard output for the task.
	 * @param __err The standard error for the task.
	 * @param __buildType The build type used.
	 * @param __args CMake arguments.
	 * @return The CMake exit value.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/04/01
	 */
	public static int cmakeExecutePipe(boolean __fail, InputStream __in,
		OutputStream __out, OutputStream __err, String __buildType,
		String... __args)
		throws IOException
	{
		// Need CMake
		Path cmakePath = CMakeUtils.cmakeExePath();
		if (cmakePath == null)
			throw new RuntimeException("CMake not found.");
		
		// Determine run arguments
		List<String> args = new ArrayList<>();
		args.add(cmakePath.toAbsolutePath().toString());
		if (__args != null && __args.length > 0)
			args.addAll(Arrays.asList(__args));
		
		// Set executable process
		ProcessBuilder procBuilder = new ProcessBuilder();
		procBuilder.command(args);
		
		// Log the output somewhere
		if (__in != null)
			procBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
		if (__out != null)
			procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
		if (__err != null)
			procBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
		
		// Start the process
		Process proc = procBuilder.start();
		
		// Wait for it to complete
		try (ForwardStream fwdOut = (__out == null ? null : 
				 new ForwardInputToOutput(proc.getInputStream(), __out));
			 ForwardStream fwdErr = (__err == null ? null : 
				 new ForwardInputToOutput(proc.getErrorStream(), __err)))
		{
			// Forward output
			if (__out != null)
				fwdOut.runThread("cmake-stdout");
			
			// Forward error
			if (__err != null)
				fwdErr.runThread("cmake-stderr");
			
			// Wait for completion, stop if it takes too long
			if (!proc.waitFor(15, TimeUnit.MINUTES) ||
				(__fail && proc.exitValue() != 0))
				throw new RuntimeException(String.format(
					"CMake failed to %s...", __buildType));
			
			// Use the given exit value
			return proc.exitValue();
		}
		catch (InterruptedException __e)
		{
			throw new RuntimeException("CMake timed out!", __e);
		}
		finally
		{
			// Destroy the task
			proc.destroy();
		}
	}
	
	/**
	 * Dumps log to the output. 
	 *
	 * @param __logger The logger to output to.
	 * @param __level The log level.
	 * @param __pathy The output path.
	 * @since 2024/03/15
	 */
	public static void dumpLog(Logger __logger, LogLevel __level, Path __pathy)
	{
		try
		{
			for (String ln : Files.readAllLines(__pathy))
				__logger.log(__level, ln);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the CMake cache from the given build.
	 *
	 * @param __logDir The build directory to use.
	 * @return The CMake cache.
	 * @throws IOException If it could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/01
	 */
	public static Map<String, String> loadCache(Path __logDir)
		throws IOException, NullPointerException
	{
		if (__logDir == null)
			throw new NullPointerException("NARG");
		
		// Load in lines accordingly
		Map<String, String> result = new LinkedHashMap<>();
		for (String line : Files.readAllLines(
			__logDir.resolve("CMakeCache.txt")))
		{
			// Comment?
			if (line.startsWith("//") || line.startsWith("#"))
				continue;
			
			// Find equal sign between key and value
			int eq = line.indexOf('=');
			if (eq < 0)
				continue;
			
			// Split in
			result.put(line.substring(0, eq).trim(),
				line.substring(eq + 1).trim());
		}
		
		// Return parsed properties
		return result;
	}
}
