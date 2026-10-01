// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import java.nio.file.Path;

/**
 * Handles running CMake.
 *
 * @since 2025/10/22
 */
public class CMakeCommand
{
	/** The CMake executable. */
	protected final Path exe;
	
	/**
	 * Initializes the command executor.
	 *
	 * @param __exe The executable to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/10/22
	 */
	public CMakeCommand(Path __exe)
		throws NullPointerException
	{
		if (__exe == null)
			throw new NullPointerException("NARG");
		
		this.exe = __exe;
	}
	
	/**
	 * Finds the CMake command.
	 *
	 * @return The resultant command or {@code null} if there is none.
	 * @since 2025/10/22
	 */
	public static CMakeCommand instance()
	{
		Path path = Utils.findExecutable("cmake");
		if (path != null)
			return new CMakeCommand(path);
		return null;
	}
}
