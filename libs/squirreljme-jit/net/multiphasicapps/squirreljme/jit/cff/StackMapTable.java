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
import java.io.IOException;

/**
 * This is the stack map table which is used for verification purposes.
 *
 * @since 2017/10/09
 */
public final class StackMapTable
{
	/**
	 * Decodes the stack map table.
	 *
	 * @param __p The constant pool.
	 * @param __m The method this code exists within.
	 * @param __new Should the new stack map table format be used?
	 * @param __in The data for the stack map table.
	 * @throws InvalidClassFormatException If the stack map table is not
	 * valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	public static StackMapTable decode(Pool __p, Method __m, boolean __new,
		DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__p == null || __m == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

