// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch;

/**
 * This provides an abstract implementation of a standard console interface
 * which is used for limited systems which only have a graphical text console
 * available for usage.
 *
 * The internal character buffers and such are managed by the console view,
 * the implementing interface just has to draw the console in most cases.
 *
 * @since 2016/05/14
 */
public abstract class AbstractConsoleView
{
	/** Console lock. */
	protected final Object lock =
		new Object();
	
	/** The console character data. */
	private volatile char[] _chars;
	
	/** The console attribute data. */
	private volatile byte[] _attrs;
	
	/** The total number of character cells. */
	private volatile int _cells;
	
	/** The number of columns. */
	private volatile int _cols;
	
	/** The number of rows. */
	private volatile int _rows;
	
	/**
	 * Initializes the base console view, which is initially sized to be a
	 * default sized character terminal.
	 *
	 * @since 2016/05/14
	 */
	public AbstractConsoleView()
	{
		// The console view might not be able to be initialized, so constantly
		// attempt to do so. The console data buffer allocation should be
		// immune to out of memory errors.
		int c = defaultColumns() + 1, r = defaultRows() + 1;
		boolean flag = false;
		do
		{
			// If the terminal is sized 1x1 then flag
			if (c == 1 && r == 1)
			{
				// {@squirreljme.error AY03 Could not allocate even a 1x1
				// terminal window.}
				if (flag)
					throw new RuntimeException("AY03");
				
				// Fail next time
				flag = true;
			}
			
			// Reduce size by one
			c = Math.max(1, c - 1);
			r = Math.max(1, r - 1);
		} while (!setSize(c, r));
	}
	
	/**
	 * The default number of columns to use.
	 *
	 * @return The default column count.
	 * @since 2016/05/14
	 */
	public abstract int defaultColumns();
	
	/**
	 * The default number of rows to use.
	 *
	 * @return The default row count.
	 * @since 2016/05/14
	 */
	public abstract int defaultRows();
	
	/**
	 * Displays the internal console buffer to the system's specific console
	 * interface.
	 *
	 * @since 2016/05/14
	 */
	public abstract void displayConsole();
	
	/**
	 * Sets the size of the console device.
	 *
	 * @param __c The number of columns to use.
	 * @param __r The number of rows to use.
	 * @return {@code true} if the new size took effect, otherwise
	 * {@code false} indicates that the console buffer could not be allocated
	 * perhaps due to being out of memory.
	 * @throws IllegalArgumentException If the number rows or columns are
	 * zero or negative.
	 * @since 2016/05/14
	 */
	public final boolean setSize(int __c, int __r)
	{
		// {@squirreljme.error AY01 Requested the the console view be set to
		// a zero or negative size. (The requested columns; The requested
		// rows)}
		if (__c <= 0 || __r <= 0)
			throw new IllegalArgumentException(String.format("AY01 %d %d",
				__c, __r));
		
		// Lock
		synchronized (lock)
		{
			// Get the old size
			int oc = _cols;
			int or = _rows;
			
			// If the same size, do nothing
			if (oc == __c && or == __r)
				return true;
			
			// It is possible that there might not be enough memory to allocate
			// the console. So instead of failing with an out of memory error,
			// the set size just fails.
			try
			{
				// Allocate
				int cells = __c * __r;
				char[] chars = new char[cells];
				byte[] attrs = new byte[cells];
		
				// Set new details
				this._chars = chars;
				this._attrs = attrs;
				this._cells = cells;
				this._cols = __c;
				this._rows = __r;
				
				// Was changed
				return true;
			}
			
			// Cannot fit the new console size into memory
			catch (OutOfMemoryError e)
			{
				return false;
			}
		}
	}
}

