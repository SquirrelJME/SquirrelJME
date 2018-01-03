// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib;

import java.io.InputStream;

/**
 * This represents a single program which exists within the kernel and maps
 * with suites within the Java ME environment. Each program is identified by
 * an identifier which represents the program index. The index remains constant
 * for the same program (unless that program has been changed). The index is
 * used to refer to the program slot.
 *
 * @since 2017/12/11
 */
public abstract class Library
{
	/**
	 * Returns the value of the given control key.
	 *
	 * @param __k The control key.
	 * @return The value of the key or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public final String controlGet(String __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the value of the given control key.
	 *
	 * @param __k The control key.
	 * @param __v The new value to set, {@code null} clears it.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/01/02
	 */
	public final void controlSet(String __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads the specified resource from the given library.
	 *
	 * @param __n The name of the resource to load.
	 * @return The resource data or {@code null} if not found.
	 * @since 2018/01/02
	 */
	public final InputStream loadResource(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the library type.
	 *
	 * @return The library type.
	 * @since 2018/01/02
	 */
	public final int type()
	{
		throw new todo.TODO();
	}
}

