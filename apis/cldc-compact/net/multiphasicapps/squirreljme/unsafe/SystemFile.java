// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

import java.io.IOException;

/**
 * This class provides access to the host filesystem if it is available.
 *
 * @see __Ext_SystemFile__
 * @since 2017/08/10
 */
public final class SystemFile
{
	/**
	 * Not used.
	 *
	 * @since 2017/08/10
	 */
	private SystemFile()
	{
	}
	
	/**
	 * Writes a single byte to standard error.
	 *
	 * @param __b The byte to write.
	 * @since 2016/08/07
	 */
	public static void stdErr(int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes multiple bytes to standard error.
	 *
	 * @param __b The bytes to write.
	 * @param __o The starting offset.
	 * @param __l The number of bytes to write.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @since 2016/08/07
	 */
	public static void stdErr(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes a single byte to standard output.
	 *
	 * @param __b The byte to write.
	 * @since 2016/08/07
	 */
	public static void stdOut(int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes multiple bytes to standard output.
	 *
	 * @param __b The bytes to write.
	 * @param __o The starting offset.
	 * @param __l The number of bytes to write.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @since 2016/08/07
	 */
	public static void stdOut(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
}

