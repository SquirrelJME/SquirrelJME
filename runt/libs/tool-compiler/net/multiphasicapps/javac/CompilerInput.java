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

import java.io.InputStream;

/**
 * This is used as input for the compiler and is used for source code and/or
 * class binaries.
 *
 * All instances of this class must be thread safe.
 *
 * @since 2017/11/28
 */
public interface CompilerInput
{
	/**
	 * Returns the name of the input file.
	 *
	 * @return The input file name.
	 * @throws CompilerException If the name could not be obtained.
	 * @since 2017/11/28
	 */
	public abstract String name()
		throws CompilerException;
	
	/**
	 * Opens an input stream for this input.
	 *
	 * @return The stream for the given input.
	 * @throws CompilerException If the stream could not be opened for any
	 * reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public abstract InputStream open()
		throws CompilerException, NullPointerException;
}

