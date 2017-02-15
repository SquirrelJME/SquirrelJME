// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This class manages register and stack allocations and is used to determine
 * where and how to store variables for computational and storage use.
 *
 * Instances of this class would also be created when a call to a method is
 * required to be performed.
 *
 * @since 2017/02/11
 */
public abstract class CellAllocator
{
	/**
	 * Returns the base register.
	 *
	 * @return The base register.
	 * @since 2017/02/15
	 */
	public abstract Register baseRegister();
	
	/**
	 * Returns the stack register.
	 *
	 * @return The stack register.
	 * @since 2017/02/15
	 */
	public abstract Register stackRegister();
}

