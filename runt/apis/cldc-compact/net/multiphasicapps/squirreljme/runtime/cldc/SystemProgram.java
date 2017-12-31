// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

import java.io.InputStream;

/**
 * This interface is used to represent generic access to a program that exists
 * within the kernel.
 *
 * Since the kernel always uses tasks for permission checking, the instance
 * of this class should pass that information to the kernel as needed.
 *
 * Instances of this class will be used as keys so it must implement
 * {@link #equals(Object)} and {@link #hashCode()} to where even two
 * instances of this class which point to the same program refer to that
 * instance.
 *
 * @since 2017/12/10
 */
public interface SystemProgram
{
	/**
	 * Returns the value of a control key.
	 *
	 * @param __k The control key to read.
	 * @return The value of the key or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public abstract String controlGet(String __k)
		throws NullPointerException;
	
	/**
	 * Sets the specified control key to the given value.
	 *
	 * @param __k The key to set.
	 * @param __v The value to set, if {@code null} then it is cleared.
	 * @throws NullPointerException If no key was set.
	 * @since 2017/12/31
	 */
	public abstract void controlSet(String __k, String __v)
		throws NullPointerException;
	
	/**
	 * Returns the index of the program.
	 *
	 * @return The program index.
	 * @since 2017/12/27
	 */
	public abstract int index();
	
	/**
	 * Loads the given resource from the program.
	 *
	 * @param __n The resource to load.
	 * @return The input stream over the resource or {@code null} if it does
	 * not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public abstract InputStream loadResource(String __n)
		throws NullPointerException;
	
	/**
	 * Returns the program type.
	 *
	 * @return The program type.
	 * @since 2017/12/31
	 */
	public abstract int type();
}

