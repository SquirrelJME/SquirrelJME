// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import cc.squirreljme.plugin.util.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.gradle.internal.os.OperatingSystem;

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
		if (OperatingSystem.current() == OperatingSystem.WINDOWS)
		{
			String programFiles = System.getenv("PROGRAMFILES");
			if (programFiles != null)
			{
				Path maybe = Paths.get(programFiles).resolve("CMake")
					.resolve("bin").resolve("cmake.exe");
				if (Files.exists(maybe))
					return maybe;
			}
		}
		
		return cmakePath;
	}
	
	/**
	 * Executes a CMake task.
	 *
	 * @param __logger The logger to use.
	 * @param __logName The name of the log.
	 * @param __cmakeBuild The CMake build directory.
	 * @param __args CMake arguments.
	 * @throws IOException On read/write or execution errors.
	 * @since 2024/03/15
	 */
	public static void cmakeExecute(Logger __logger, String __logName,
		Path __cmakeBuild, String... __args)
		throws IOException
	{
		// Need CMake
		Path cmakePath = CMakeUtils.cmakeExePath();
		if (cmakePath == null)
			throw new RuntimeException("CMake not found.");
		
		// Determine run arguments
		List<String> args = new ArrayList<>();
		args.add(cmakePath.toAbsolutePath().toString());
		args.addAll(Arrays.asList(__args));
		
		// Set executable process
		ProcessBuilder procBuilder = new ProcessBuilder();
		procBuilder.command(args);
		
		// Output log files
		Path outLog = __cmakeBuild.resolve(__logName + ".out");
		Path errLog = __cmakeBuild.resolve(__logName + ".err");
		
		// Log the output somewhere
		procBuilder.redirectOutput(outLog.toFile());
		procBuilder.redirectError(errLog.toFile());
		
		// Start the process
		Process proc = procBuilder.start();
		
		// Wait for it to complete
		try
		{
			if (!proc.waitFor(5, TimeUnit.MINUTES) ||
				proc.exitValue() != 0)
				throw new RuntimeException(String.format(
					"CMake failed to %s...", __logName));
		}
		catch (InterruptedException __e)
		{
			throw new RuntimeException("CMake timed out!", __e);
		}
		finally
		{
			CMakeUtils.dumpLog(__logger,
				LogLevel.LIFECYCLE, outLog);
			CMakeUtils.dumpLog(__logger,
				LogLevel.ERROR, errLog);
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
}
