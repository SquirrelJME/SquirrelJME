// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

/**
 * Accesses an interger array.
 *
 * @since 2018/02/21
 */
public interface IntegerArray
	extends Array
{
	/**
	 * Returns the integer at the given index.
	 *
	 * @param __i The index to get.
	 * @return The value.
	 * @throws ArrayIndexOutOfBoundsException If out of bounds.
	 * @since 2018/03/02
	 */
	public abstract int get(int __i)
		throws ArrayIndexOutOfBoundsException;
}

