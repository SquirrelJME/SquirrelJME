// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Handles finding and calling Install4J.
 *
 * @since 2025/05/23
 */
public class Install4JCommand
{
	/** The path to the executable. */
	protected final Path exe;
	
	/**
	 * Initializes the Install4J command.
	 *
	 * @param __exe The executable used.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/23
	 */
	public Install4JCommand(Path __exe)
		throws NullPointerException
	{
		if (__exe == null)
			throw new NullPointerException("NARG");
		
		this.exe = __exe;
	}
	
	/**
	 * Builds the Install4J install media.
	 *
	 * @param __project The project root.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/23
	 */
	public void media(Path __project)
		throws IOException, NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		ProcessBuilder builder = new ProcessBuilder(
			__project.toAbsolutePath().normalize().toString());
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		
		// Start and wait for it to complete
		Process process = builder.start();
		
		// Wait for completion, if successful then stop
		try
		{
			int exit = process.waitFor();
			if (exit == 0)
				return;
			
			throw new IOException(String.format(
				"Failed with exit code %d", exit));
		}
		catch (InterruptedException __e)
		{
			process.destroyForcibly();
			throw new IOException(__e.getMessage(), __e);
		}
	}
	
	/**
	 * Returns the instance of Install4J.
	 *
	 * @return The instance or {@code null} if not found.
	 * @since 2025/05/23
	 */
	public static Install4JCommand instance()
	{
		Path path = Utils.findExecutable("install4jc");
		if (path != null)
			return new Install4JCommand(path);
		return null;
	}
}
