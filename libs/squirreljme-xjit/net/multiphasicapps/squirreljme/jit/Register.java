// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This interface is used to associate classes (in most cases an enum) to a
 * specific register.
 *
 * Registers are opaque in that they do not have a specified size or data type
 * due to the potential for architectures which have variable register sizes.
 *
 * All implementations of this class must support {@link #equals(Object)} and
 * {@link #hashCode()}.
 *
 * @since 2017/03/10
 */
public interface Register
{
	/**
	 * Returns the index of the register. Most architectures index their
	 * registers by the index of that register.
	 *
	 * @return The index of the register.
	 * @since 2017/04/01
	 */
	public abstract int index();
	
	/**
	 * Is this a floating point register?
	 *
	 * @return {@code true} if a floating point register.
	 * @since 2017/03/25
	 */
	public abstract boolean isFloat();
	
	/**
	 * Is this an integer register?
	 *
	 * @return {@code true} if an integer register.
	 * @since 2017/03/25
	 */
	public abstract boolean isInteger();
}

