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
 * @since 2016/09/08
 */
public final class TerminalScreen
	implements TerminalAttribute, TerminalColor
{
	/** The column count. */
	protected final int columns;
	
	/** The row count. */
	protected final int rows;
	
	/** The number of cells. */
	protected final int cells;
	
	/** The characters in each cell. */
	protected final char[] text;
	
	/** The cell attributes, the values here are bitfields. */
	protected final short[] attr;
	
	/** Notifier for terminal updates. */
	protected final TerminalUpdateNotifier notifier;
	
	/**
	 * Initializes the terminal screen.
	 *
	 * @param __n The notifier to be called when a change is detected on the
	 * screen contents. This parameter is optional.
	 * @param __c The number of columns to use.
	 * @param __r The number of rows to use.
	 * @throws IndexOutOfBoundsException If the number of columns or rows
	 * is zero or negative.
	 * @since 2016/09/08
	 */
	public TerminalScreen(TerminalUpdateNotifier __n, int __c, int __r)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AD01 A terminal screen with zero or negative
		// number of columns or rows specified.}
		if (__c <= 0 || __r <= 0)
			throw new IndexOutOfBoundsException("AD01");
		
		// Set
		this.notifier = __n;
		
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
	
	/**
	 * Returns the number of columns used.
	 *
	 * @return The column count.
	 * @since 2016/09/11
	 */
	public int columns()
	{
		return this.columns;
	}
	
	/**
	 * Returns the number of rows used.
	 *
	 * @return The row count.
	 * @since 2016/09/11
	 */
	public int rows()
	{
		return this.rows;
	}
}

