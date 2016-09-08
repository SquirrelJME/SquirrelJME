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
 * DESCRIBE THIS.
 *
 * @since 2016/09/08
 */
public final class DisplayCells
{
	/** The character data. */
	public final char[] text;
	
	/** The attribute data. */
	public final byte[] attr;
	
	/** The number of cells. */
	public final int cells;
	
	/** The number of columns. */
	public final int cols;
	
	/** The number of rows. */
	public final int rows;
	
	/**
	 * Sets the size of the output display.
	 *
	 * @param __c The number of columns.
	 * @param __r The number of rows.
	 * @throws IndexOutOfBoundsException If either is zero or negative.
	 * @since 2016/09/08
	 */
	public DisplayCells(int __c, int __r)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error DA02 The size of the output display has
		// a zero or negative number of rows or columns.}
		if (__c <= 0 || __r <= 0)
			throw new IllegalArgumentException("DA02");
		
		// Set
		int cells = __c * __r;
		this.cells = cells;
		this.cols = __c;
		this.rows = __r;
		
		// Setup data arrays
		this.text = new char[cells];
		this.attr = new byte[cells];
	}
}

