// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This interface is used to actually obtain the bytes that are within the
 * allocated address space of the system.
 *
 * All addresses are relative to the base of their associated region.
 *
 * @since 2016/08/07
 */
public interface AddressBusHandler
{
	/**
	 * Reads the byte at the given position.
	 *
	 * @param __p The position of the byte being read relative to the start
	 * of the associated handler range.
	 * @return The value at the given address.
	 * @since 2016/08/07
	 */
	public abstract byte read(long __p);
	
	/**
	 * Writes the byte at the given position.
	 *
	 * @param __p The position of the byte being written relative to the start
	 * of the associated handler range.
	 * @param __v The value to write at the given address.
	 * @since 2016/08/07
	 */
	public abstract void write(long __p, byte __v);
}

