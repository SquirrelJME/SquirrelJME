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

import java.io.IOException;
import java.io.OutputStream;

/**
 * This interface is used to allow classes utilizing the compiler to place
 * output files in a target JAR potentially. It is possible for multiple
 * outputs to be open at the same time, as such cases should be handled where
 * they must sequentially be output.
 *
 * All instances of this class must be thread safe.
 *
 * @since 2017/11/28
 */
public interface CompilerOutput
{
	/**
	 * Flushes the output.
	 *
	 * @throws CompilerException If there was an error flushing.
	 * @since 2017/11/28
	 */
	public abstract void flush()
		throws CompilerException;
	
	/**
	 * This is called when a file has been or is being compiled and it is
	 * required to output the file.
	 *
	 * @param __n The name of the file being written, this should match the
	 * same format that is used in ZIP files.
	 * @return An output stream which writes bytes to the output.
	 * @throws CompilerException If the output could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public abstract OutputStream output(String __n)
		throws CompilerException, NullPointerException;
}

