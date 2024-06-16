// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import cc.squirreljme.plugin.multivm.VMHelpers;
import cc.squirreljme.plugin.util.ForwardInputToOutput;
import cc.squirreljme.plugin.util.ForwardStream;
import cc.squirreljme.plugin.util.PathUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		return PathUtils.findPathInstalled("cmake",
			"CMake");
	}
	
	/**
	 * Requests the version of CMake.
	 *
	 * @return The resultant CMake version or {@code null} if there is no
	 * CMake available or the version could not be parsed.
	 * @since 2024/04/01
	 */
	public static VersionNumber cmakeExeVersion()
	{
		// We need the CMake executable
		Path cmakeExe = CMakeUtils.cmakeExePath();
		if (cmakeExe == null)
			return null;
		
		String rawStr = null;
		try
		{
			rawStr = CMakeUtils.cmakeExecuteOutput(null,
				"version", "--version");
			
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
					
					// Are there digits in here?
					int firstDig = -1;
					for (char c = '0'; c <= '9'; c++)
					{
						int maybeDig = ln.indexOf(c);
						if (maybeDig >= 0)
							if (firstDig < 0 || maybeDig < firstDig)
								firstDig = maybeDig;
					}
					
					// Was a digit actually found?
					if (firstDig >= 0)
						return VersionNumber.parse(
							ln.substring(firstDig).trim());
				}
			}
			
			// Failed
			return null;
		}
		catch (IOException __e)
		{
			throw new RuntimeException(
				"Could not determine CMake version: " + rawStr, __e);
		}
	}
	
	/**
	 * Executes a CMake task.
	 *
	 * @param __workDir The working directory.
	 * @param __logger The logger to use.
	 * @param __logName The name of the log.
	 * @param __logDir The CMake build directory.
	 * @param __args CMake arguments.
	 * @return The CMake exit value.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/03/15
	 */
	public static int cmakeExecute(Path __workDir, Logger __logger,
		String __logName, Path __logDir, String... __args)
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
			return CMakeUtils.cmakeExecutePipe(__workDir, null,
				stdOut, stdErr, __logName, __args);
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
	 * @param __workDir The working directory.
	 * @param __buildType The build type used.
	 * @param __args CMake arguments.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/04/01
	 */
	public static String cmakeExecuteOutput(Path __workDir, String __buildType,
		String... __args)
		throws IOException
	{
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayOutputStream err = new ByteArrayOutputStream())
		{
			// Run command
			CMakeUtils.cmakeExecutePipe(__workDir, null, out, err,
				__buildType, __args);
			
			// Return output for later decoding
			return out.toString("utf-8") +
				System.getProperty("line.separator") +
				err.toString("utf-8");
		}
	}
	
	/**
	 * Executes a CMake task to the given pipe.
	 *
	 * @param __workDir The working directory.
	 * @param __in The standard input for the task.
	 * @param __out The standard output for the task.
	 * @param __err The standard error for the task.
	 * @param __buildType The build type used.
	 * @param __args CMake arguments.
	 * @return The CMake exit value.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/04/01
	 */
	public static int cmakeExecutePipe(Path __workDir, InputStream __in,
		OutputStream __out, OutputStream __err, String __buildType,
		String... __args)
		throws IOException
	{
		return CMakeUtils.cmakeExecutePipe(__workDir, true,
			__in, __out, __err,
			__buildType, __args);
	}
	
	/**
	 * Executes a CMake task to the given pipe.
	 *
	 * @param __workDir The working directory.
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
	public static int cmakeExecutePipe(Path __workDir, boolean __fail,
		InputStream __in,
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
		
		// Working directory, if specified
		if (__workDir != null)
			procBuilder.directory(__workDir.toFile());
		
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
					"CMake failed to %s: exit value %d",
						__buildType, proc.exitValue()));
			
			// Use the given exit value
			return proc.exitValue();
		}
		catch (InterruptedException|IllegalThreadStateException __e)
		{
			throw new RuntimeException("CMake timed out or stuck!", __e);
		}
		finally
		{
			// Destroy the task
			proc.destroy();
		}
	}
	
	/**
	 * Configures the CMake task.
	 *
	 * @param __task The task to configure.
	 * @throws IOException If it could not be configured.
	 * @since 2024/04/08
	 */
	public static void configure(CMakeBuildTask __task)
		throws IOException
	{
		Path cmakeBuild = __task.cmakeBuild;
		Path cmakeSource = __task.cmakeSource;
		
		// Old directory must be deleted as it might be very stale
		VMHelpers.deleteDirTree(__task, cmakeBuild);
		
		// Make sure the output build directory exists
		Files.createDirectories(cmakeBuild);
		
		// Debug version
		VersionNumber version = CMakeUtils.cmakeExeVersion();
		__task.getLogger().lifecycle("CMake version: " + version);
		
		// Force 32-bit compile?
		String genPlatform;
		if (OperatingSystem.current().isWindows() &&
			CMakeUtils.is32BitHost() && CMakeUtils.isX86Host())
			genPlatform = "-DCMAKE_GENERATOR_PLATFORM=Win32";
		else
			genPlatform = "-DXXIGNORETHISXX=1";
		
		// Configure CMake first before we continue with anything
		// Note that newer CMake has a better means of specifying the path
		if (version != null && version.compareTo(
			VersionNumber.parse("3.13")) >= 0)
			CMakeUtils.cmakeExecute(__task.cmakeBuild, __task.getLogger(),
				"configure",
				__task.getProject().getBuildDir().toPath(),
				"-DCMAKE_BUILD_TYPE=RelWithDebInfo",
				genPlatform,
				"-S", cmakeSource.toAbsolutePath().toString(),
				"-B", cmakeBuild.toAbsolutePath().toString());
		
		// Otherwise fallback to the old method which is a bit more tricky
		// as we need to set our working directory accordingly
		else
			CMakeUtils.cmakeExecute(__task.cmakeBuild, __task.getLogger(),
				"configure",
				__task.getProject().getBuildDir().toPath(),
				"-DCMAKE_BUILD_TYPE=RelWithDebInfo",
				genPlatform,
				cmakeSource.toAbsolutePath().toString());
	}
	
	/**
	 * Is configuration needed?
	 *
	 * @param __task The task to check.
	 * @return If reconfiguration is needed or not.
	 * @since 2024/04/08
	 */
	public static boolean configureNeeded(CMakeBuildTask __task)
	{
		Path cmakeBuild = __task.cmakeBuild;
		
		// Missing directories or no cache at all?
		if (!Files.isDirectory(cmakeBuild) ||
			!Files.exists(cmakeBuild.resolve("CMakeCache.txt")))
			return true;
		
		// Load in the CMake cache to check it
		try
		{
			// Load CMake cache
			Map<String, String> cmakeCache = CMakeUtils.loadCache(cmakeBuild);
			
			// Check the configuration directory
			String rawConfigDir = cmakeCache.get(
				"CMAKE_CACHEFILE_DIR:INTERNAL");
			
			// No configuration directory is known??
			if (rawConfigDir == null)
				return true;
			
			// Did the directory of the cache change? This can happen
			// under CI/CD where the build directory is different and
			// there is old data that is restored
			Path configDir = Paths.get(rawConfigDir).toAbsolutePath();
			if (!Files.isSameFile(configDir, cmakeBuild) ||
				!cmakeBuild.equals(configDir))
				return true;
		}
		catch (IOException __ignored)
		{
			// If this happens, just assume it needs to be done
			return true;
		}
		
		// Not needed
		return false;
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
	 * Is this a 32-bit host?
	 *
	 * @return If this is a 32-bit host.
	 * @since 2024/06/16
	 */
	public static boolean is32BitHost()
	{
		switch (System.getProperty("os.arch").toLowerCase())
		{
			case "x86":
			case "x86_32":
			case "x86-32":
			case "i386":
			case "i486":
			case "i586":
			case "i686":
			case "ia32":
			case "ppc":
			case "powerpc":
			case "arm32":
				return true;
		}
		
		// It is not or unknown
		return false;
	}
	
	/**
	 * Is this an x86 host?
	 *
	 * @return If this is a x86 host.
	 * @since 2024/06/16
	 */
	private static boolean isX86Host()
	{
		switch (System.getProperty("os.arch").toLowerCase())
		{
			case "x86":
			case "x86_32":
			case "x86-32":
			case "x86_64":
			case "x86-64":
			case "amd64":
			case "i386":
			case "i486":
			case "i586":
			case "i686":
			case "ia32":
				return true;
		}
		
		// It is not or unknown
		return false;
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
