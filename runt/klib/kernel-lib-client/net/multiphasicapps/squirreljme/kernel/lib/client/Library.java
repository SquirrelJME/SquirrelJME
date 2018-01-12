// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib.client;

import java.io.InputStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTrustGroup;

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
	
	/** The index of the library. */
	protected final int index;
	
	/**
	 * Initializes the base library.
	 *
	 * @param __dx The index of the library.
	 * @throws IllegalArgumentException If the library index is negative.
	 * @since 2018/01/07
	 */
	protected Library(int __dx)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AV01 Cannot have a library index which is
		// negative.}
		if (__dx < 0)
			throw new IllegalArgumentException("AV01");
		
		this.index = __dx;
	}
	
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
	 * Returns the library index.
	 *
	 * @return The library index.
	 * @since 2018/01/07
	 */
	public final int index()
	{
		return this.index;
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
	 * Returns the trust group which this library is under.
	 *
	 * @return The trust group the library exists under.
	 * @since 2018/01/11
	 */
	public final SystemTrustGroup trustGroup()
	{
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

