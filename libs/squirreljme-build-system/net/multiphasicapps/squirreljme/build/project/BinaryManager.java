// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.project;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is used to manage binaries which are available for running
 * and/or compilation.
 *
 * @since 2017/10/31
 */
public final class BinaryManager
{
	/** The output directory where built binaries are to be placed. */
	protected final Path output;
	
	/** Projects which may exist that provide access to source code. */
	protected final SourceManager sources;
	
	/**
	 * Initializes the binary manager.
	 *
	 * @param __out The output directory, this directory is scanned for
	 * binaries as requested.
	 * @param __src The source code where projects may be sourced from, may
	 * be {@code null} if there is no source code.
	 * @throws NullPointerException If no output path was specified.
	 * @since 2017/10/31
	 */
	public BinaryManager(Path __out, SourceManager __src)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.output = __out;
		this.sources = __src;
	}
	
	/**
	 * Obtains the binary which uses the given source name.
	 *
	 * @param __n The name of the project to get.
	 * @return The binary for the given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public Binary get(SourceName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Creates a virtual binary which is sourced from the given JAR file and
	 * where it has no backing in source code.
	 *
	 * @param __p The path to JAR to be opened as a binary.
	 * @return The binary for the given path.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public Binary getVirtual(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

