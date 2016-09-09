// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal;

/**
 * This represents a single terminal screen which is used to store information
 * about what is displayed on the screen of the terminal.
 *
 * Note the fields here are public as they are intended to be used only by
 * specific implementations of the terminal.
 *
 * @since 2016/09/08
 */
public final class TerminalScreen
	implements TerminalColor
{
	/** The column count. */
	public final int columns;
	
	/** The row count. */
	public final int rows;
	
	/** The number of cells. */
	public final int cells;
	
	/** The characters in each cell. */
	public final char[] text;
	
	/** The cell attributes, the values here are bitfields. */
	public final short[] attr;
	
	/**
	 * Initializes the terminal screen.
	 *
	 * @param __c The number of columns to use.
	 * @param __r The number of rows to use.
	 * @throws IndexOutOfBoundsException If the number of columns or rows
	 * is zero or negative.
	 * @since 2016/09/08
	 */
	public TerminalScreen(int __c, int __r)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AD01 A terminal screen with zero or negative
		// number of columns or rows specified.}
		if (__c <= 0 || __r <= 0)
			throw new IndexOutOfBoundsException("AD01");
		
		// Set size
		this.columns = __c;
		this.rows = __r;
		
		// Determine cells, for data allocation
		int cells = __c * __r;
		this.cells = cells;
		
		// Setup arrays
		this.text = new char[cells];
		this.attr = new short[cells];
	}
}

