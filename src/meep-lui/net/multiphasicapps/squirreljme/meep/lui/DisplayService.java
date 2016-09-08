// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui;

/**
 * This is a service which implements the actual support for displays. This
 * base class is intended to make implementing LUI displays much easier by
 * wrapping a line based format with unspecified attributes.
 *
 * @since 2016/09/07
 */
public abstract class DisplayService
{
	/**
	 * Sets the size of the output display.
	 *
	 * @param __c The number of columns.
	 * @param __r The number of rows.
	 * @throws IllegalArgumentException If either is zero or negative.
	 * @since 2016/09/08
	 */
	protected final void setDisplaySize(int __c, int __r)
		throws IllegalArgumentException
	{
		// {@squirreljme.error DA02 The size of the output display has
		// a zero or negative number of rows or columns.}
		if (__c <= 0 || __r <= 0)
			throw new IllegalArgumentException("DA02");
		
		throw new Error("TODO");
	}
}

