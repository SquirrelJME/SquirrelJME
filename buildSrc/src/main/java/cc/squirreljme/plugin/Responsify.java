// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.process.ExecSpec;

/**
 * Generates a response file for Windows.
 *
 * @since 2025/02/14
 */
public final class Responsify
{
	/** Cached responsify check. */
	private static final Map<Path, Boolean> _CACHED =
		new TreeMap<>();
	
	/**
	 * Not used.
	 *
	 * @since 2025/02/14
	 */
	private Responsify()
	{
	}
	
	/**
	 * Adapts the given arguments to use response files.
	 *
	 * @param __args The arguments to adapt.
	 * @return The adapted arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/02/14
	 */
	public static List<String> adapt(Iterable<String> __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Convert arguments first
		List<String> args = new ArrayList<>();
		for (String arg : __args)
			args.add(arg);
		
		// If not running on Windows, we do not have to worry about creating
		// a mini file just to hold all the command line arguments, joy!
		// Only handle certain kinds of arguments
		String exe = (!args.isEmpty() ? args.get(0) : null);
		if (exe == null || !OperatingSystem.current().isWindows())
			return args;
		
		// Must be the Java executable
		if (!(exe.toLowerCase().endsWith("java.exe")))
			return args;
		
		// Eclipse Adoptium just randomly got rid of response file support, so
		// check if it is supported in the target Java
		if (!Responsify.__supported(Paths.get(exe).toAbsolutePath()
			.normalize()))
			return args;
		
		// Setup temporary file
		try
		{
			Path responseFile = Files.createTempFile("sjme", "r");
			Runtime.getRuntime().addShutdownHook(new Thread(() ->
				{
					try
					{
						Files.delete(responseFile);
					}
					catch (IOException ignored)
					{
					}
				}, "ResponseCleanup"));
			
			// Quote all arguments! If needed
			List<String> quoted = new ArrayList<>(args.size());
			for (int i = 1, n = args.size(); i < n; i++)
			{
				String arg = args.get(i);
				
				// Escape backslashes?
				if (arg.indexOf('\\') >= 0)
					arg = arg.replace("\\", "\\\\");
				
				// Quote if needed
				if (arg.indexOf(' ') >= 0)
					quoted.add("\"" + arg + "\"");
				else
					quoted.add(arg);
			}
			
			// Write the file
			Files.write(responseFile,
				quoted,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
			
			// Refer to this
			return Arrays.asList(exe,
				"@" + responseFile.toAbsolutePath());
		}
		
		// Fallback since the file failed to be created
		catch (IOException ignored)
		{
			return args;
		}
	}
	
	/**
	 * Adapts the arguments into the given target.
	 *
	 * @param __spec The target to place into.
	 * @param __args The arguments to adapt.
	 * @return The adapted target.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/02/14
	 */
	public static ExecSpec into(ExecSpec __spec,
		Iterable<String> __args)
		throws NullPointerException
	{
		if (__spec == null || __args == null)
			throw new NullPointerException("NARG");
		
		__spec.setCommandLine(Responsify.adapt(__args));
		return __spec;
	}
	
	/**
	 * Adapts the arguments into the given target.
	 *
	 * @param __pb The target to place into.
	 * @param __args The arguments to adapt.
	 * @return The adapted target.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/02/14
	 */
	public static ProcessBuilder into(ProcessBuilder __pb,
		Iterable<String> __args)
		throws NullPointerException
	{
		if (__pb == null || __args == null)
			throw new NullPointerException("NARG");
	
		__pb.command(Responsify.adapt(__args));
		return __pb;
	}
	
	/**
	 * Creates a process builder with the responsified arguments.
	 *
	 * @param __args The arguments used.
	 * @return The process builder to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/02/14
	 */
	public static ProcessBuilder of(String... __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		return Responsify.into(new ProcessBuilder(), Arrays.asList(__args));
	}
	
	/**
	 * Creates a process builder with the responsified arguments.
	 *
	 * @param __args The arguments used.
	 * @return The process builder to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/02/14
	 */
	@SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
	public static ProcessBuilder of(List<String> __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		return Responsify.into(new ProcessBuilder(), __args);
	}
	
	/**
	 * Checks if response files are supported because Eclipse Adoptium just
	 * decided to get rid of them.
	 *
	 * @param __exe The executable to check.
	 * @return If they are supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	private static boolean __supported(Path __exe)
		throws NullPointerException
	{
		if (__exe == null)
			throw new NullPointerException("NARG");
		
		synchronized (Responsify.class)
		{
			// Do we already know the result for the given executable?
			if (Responsify._CACHED.containsKey(__exe))
				return Responsify._CACHED.get(__exe);
		}
		
		// Need to make a fake response file
		Path tempFile = null;
		try
		{
			// Setup new temp file
			tempFile = Files.createTempFile("response", ".txt");
			
			// Just put "-help" in here
			Files.write(tempFile, Arrays.asList("-help"),
				StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE);
			
			// Try running the Java command
			Process process = new ProcessBuilder(__exe.toString(),
				"@" + tempFile.toAbsolutePath().normalize()).start();
			
			// Wait for this to finish
			try
			{
				// Wait for a result
				int result = process.waitFor();
				
				// If executing help was successful, then it is supported,
				// otherwise it is not. Cache for later so we do not have
				// to check this all the time
				boolean supported = (result == 0);
				synchronized (Responsify.class)
				{
					Responsify._CACHED.put(__exe, supported);
				}
				
				// Use the result
				return supported;
			}
			catch (InterruptedException __e)
			{
				// Was interrupted, so assume not supported as the user could
				// not wait for it
				return false;
			}
		}
		catch (IOException __failed)
		{
			// Assume it is not supported
			return false;
		}
		finally
		{
			// Cleanup
			try
			{
				if (tempFile != null)
					Files.deleteIfExists(tempFile);
			}
			catch (IOException ignored)
			{
			}
		}
	}
}
