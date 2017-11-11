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

/**
 * This contains high level memory methods which operate within the virtual
 * machine at a high level, rather than a low level.
 *
 * @since 2017/11/10
 */
public abstract class HighMemoryFunctions
{
	/**
	 * Specifies that the virtual machine should perform garbage collection.
	 *
	 * @since 2017/11/10
	 */
	public abstract void gc();
	
	
	/**
	 * This searches the the virtual machine executable memory space for
	 * strings which exist within the binary (which are always interned).
	 *
	 * @param __s The string to find the interned instance of.
	 * @return The interned string or {@code null} if it was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	public final String internString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

