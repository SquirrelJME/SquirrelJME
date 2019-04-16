// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents the minimized constant pool.
 *
 * @since 2019/04/16
 */
public final class MinimizedPool
{
	
	/**
	 * Decodes the minimized constant pool.
	 *
	 * @param __n Number of entries in the pool.
	 * @param __is The bytes to decode from.
	 * @return The resulting minimized pool.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public static final MinimizedPool decode(int __n, InputStream __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		DataInputStream dis = new DataInputStream(__is);
		
		// Debug
		todo.DEBUG.note("Decode %d (%d bytes)", __n, __is.available());
		
		throw new todo.TODO();
	}
}

