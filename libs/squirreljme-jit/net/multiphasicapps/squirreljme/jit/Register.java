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
 * This interface is used to describe registers.
 *
 * Registers compare to their index first then with their {@link DataType}. If
 * two registers share the same index and {@link DataType} they are considered
 * equal to each other.
 *
 * @since 2017/02/13
 */
public interface Register
	extends Comparable<Register>
{
	/**
	 * Returns the index of the register.
	 *
	 * @return The register index.
	 * @since 2017/02/13
	 */
	public abstract int index();
	
	/**
	 * Returns the type of value the register stores.
	 *
	 * @return The data type of the register.
	 * @since 2017/02/13
	 */
	public abstract DataType type();
}

