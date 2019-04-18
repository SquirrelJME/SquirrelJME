// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This interface represents an instance of an object.
 *
 * @since 2019/01/10
 */
public interface Instance
	extends Value
{
	/**
	 * Counts the object up or down.
	 *
	 * @param __up Counting up.
	 * @return If the object was fully counted down, it may be garbage
	 * collected.
	 * @since 2019/04/18
	 */
	public abstract boolean count(boolean __up);
	
	/**
	 * Returns the current object count.
	 *
	 * @return The current count.
	 * @since 2019/04/18
	 */
	public abstract int currentCount();
}

