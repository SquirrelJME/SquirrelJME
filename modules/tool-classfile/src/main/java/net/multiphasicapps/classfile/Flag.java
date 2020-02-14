// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This interface is used to flag the types used for flags.
 *
 * @since 2016/04/23
 */
public interface Flag
{
	/**
	 * Returns the bit mask of the given flag.
	 *
	 * @return The bit mask of the given flag.
	 * @since 2017/07/07
	 */
	public abstract int javaBitMask();
	
	/**
	 * Returns the flag ordinal.
	 *
	 * @return The ordinal of the flag.
	 * @since 2016/04/23
	 */
	public abstract int ordinal();
}

