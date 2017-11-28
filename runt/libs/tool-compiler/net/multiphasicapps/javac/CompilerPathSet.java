// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.Closeable;

/**
 * A path set represents the contents of a binary JAR file or a tree of
 * source code within a directory.
 *
 * Entries are named according to how they would appear in ZIP files with
 * no distinction of directories being made (so a file would be called for
 * example {@code META-INF/MANIFEST.MF} regardless of the host system).
 *
 * Iteration goes over the contents of the path set.
 *
 * All instances of this class must be thread safe.
 *
 * @since 2017/11/28
 */
public interface CompilerPathSet
	extends Closeable, Iterable<CompilerInput>
{
	/**
	 * {@inheritDoc}
	 * @throws CompilerException If closing the path set has failed.
	 * @since 2017/11/28
	 */
	@Override
	public abstract void close()
		throws CompilerException;
	
	/**
	 * Obtians the specified 
	 *
	 * @param __n The name of the file to open.
	 * @return The input for the given file.
	 * @throws CompilerException If some other error happened while trying to
	 * obtain the input.
	 * @throws NoSuchInputException If the input by the specified name does
	 * not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public abstract CompilerInput input(String __n)
		throws CompilerException, NoSuchInputException, NullPointerException;
}

