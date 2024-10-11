// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Access to the Fossil SCM command line interface.
 *
 * @since 2024/10/05
 */
public class FossilCommand
{
	/** The executable path. */
	protected final Path exe;
	
	/**
	 * Initializes the command executor.
	 *
	 * @param __exe The executable to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/05
	 */
	public FossilCommand(Path __exe)
		throws NullPointerException
	{
		if (__exe == null)
			throw new NullPointerException("NARG");
		
		this.exe = __exe;
	}
	
	/**
	 * Adds a file to the un-versioned space.
	 *
	 * @param __path The file to add.
	 * @param __target The target destination.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/05
	 */
	public void add(Path __path, String __target)
		throws IOException, NullPointerException
	{
		if (__path == null || __target == null)
			throw new NullPointerException("NARG");
		
		this.exec("uv", "add",
			__path.toAbsolutePath().toString(), "--as", __target);
	}
	
	/**
	 * Adds a file to the un-versioned space.
	 *
	 * @param __rawData The raw file data to add.
	 * @param __target The target destination.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/05
	 */
	public void add(byte[] __rawData, String __target)
		throws IOException, NullPointerException
	{
		if (__rawData == null || __target == null)
			throw new NullPointerException("NARG");
		
		Path temp = null;
		try
		{
			// Setup temporary file
			temp = Files.createTempFile("squirreljme", ".bin");
			
			// Write everything there
			Files.write(temp, __rawData,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE);
			
			// Store it
			this.add(temp.toAbsolutePath(), __target);
		}
		finally
		{
			if (temp != null)
				try
				{
					Files.delete(temp);
				}
				catch (IOException __ignored)
				{	
				}
		}
	}
	
	/**
	 * Executes the fossil command.
	 *
	 * @param __args The arguments to use.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/05
	 */
	public void exec(String... __args)
		throws IOException, NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Add in all arguments
		List<String> args = new ArrayList<>();
		args.add(this.exe.toAbsolutePath().toString());
		args.addAll(Arrays.asList(__args));
		
		// Setup process
		ProcessBuilder builder = new ProcessBuilder(args);
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		
		// Start and wait for it to complete
		Process process = builder.start();
		try
		{
			int exit = process.waitFor();
			if (exit != 0)
				throw new IOException("Exited with " + exit);
		}
		catch (InterruptedException __e)
		{
			throw new IOException("Interrupted", __e);
		}
	}
	
	/**
	 * Finds the Fossil command.
	 *
	 * @return The resultant command or {@code null} if there is none.
	 * @since 2024/10/05
	 */
	public static FossilCommand instance()
	{
		// Windows or not?
		String exeName;
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			exeName = "fossil.exe";
		else
			exeName = "fossil";
		
		// Use system PATH
		String paths = System.getenv("PATH");
		if (paths != null)
			for (String path : paths.split(Pattern.quote(File.pathSeparator)))
			{
				Path maybe = Paths.get(path, exeName);
				if (Files.exists(maybe) && Files.isExecutable(maybe))
					return new FossilCommand(maybe);
			}
		
		// Not found
		return null;
	}
}
