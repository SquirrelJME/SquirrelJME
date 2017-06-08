// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class decodes the constant pool and provides generic access to the
 * contents of it.
 *
 * @since 2017/06/08
 */
public class Pool
{
	/**
	 * Parses and initializes the constant pool structures.
	 *
	 * @param __in The input class containing the constant pool to be read.
	 * @throws IOException On read errors.
	 * @throws JITException If the constant pool is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public Pool(DataInputStream __in)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Entries in the pool
		int count = __in.readUnsignedShort();
		
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the entry at the specified index.
	 *
	 * @param <C> The type of class to get.
	 * @param __cl The type of class to get.
	 * @param __i The index of the entry to get.
	 * @return The entry at the specified position as the given class.
	 * @throws JITException If the class type does not match or the pool index
	 * is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public <C> C get(Class<C> __cl, int __i)
		throws JITException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

