// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This represents a single class file.
 *
 * @since 2017/09/26
 */
public final class ClassFile
{
	/**
	 * Initializes the class file.
	 *
	 * @since 2017/09/26
	 */
	ClassFile()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This parses the input stream as a class file and returns the
	 * representation of that class file.
	 *
	 * @param __is The input stream to source classes from.
	 * @return The decoded class file.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public static ClassFile decode(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		DataInputStream dis = new DataInputStream(__is);
		
		throw new todo.TODO();
	}
}

