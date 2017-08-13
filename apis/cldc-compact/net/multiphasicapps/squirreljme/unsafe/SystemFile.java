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
 * @see __Ext_systemfile__
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
	 * Returns the separator which is used for directories.
	 *
	 * @return The path separator for directories.
	 * @since 2017/08/13
	 */
	public static String directorySeparator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the line separator that is used on the system.
	 *
	 * @return The line separator that is used on the system.
	 * @since 2017/08/13
	 */
	public static String lineSeparator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the {@code PATH} variable separator that the system uses.
	 *
	 * @return The separator used for the {@code PATH} variable.
	 * @since 2017/08/13
	 */
	public static String pathSeparator()
	{
		throw new todo.TODO();
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
	
	/**
	 * Returns the directory where temporary files are located.
	 *
	 * @return The temporary directory.
	 * @since 2017/08/13
	 */
	public static String temporaryDirectory()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the home directory of the current user.
	 *
	 * @return The user's home directory.
	 * @since 2017/08/03
	 */
	public static String userHome()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the current working directory.
	 *
	 * @return The current working directory.
	 * @since 2017/08/13
	 */
	public static String workingDirectory()
	{
		throw new todo.TODO();
	}
}

