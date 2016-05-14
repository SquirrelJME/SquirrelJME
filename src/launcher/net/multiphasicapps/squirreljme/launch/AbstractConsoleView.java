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
	
	/** The current attribute brush. */
	private volatile byte _attrbrush;
	
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
	 * This is a callback which is called when the size of the console has
	 * changed.
	 *
	 * @since 2016/05/14
	 */
	protected abstract void sizeChanged();
	
	/**
	 * Returns {@code true} if the terminal supports and can display a console
	 * with the given size.
	 *
	 * @param __c The number of columns.
	 * @param __r The number of rows.
	 * @return {@code true} if the size is supported.
	 * @since 2016/05/14
	 */
	public abstract boolean supportsSize(int __c, int __r);
	
	/**
	 * Returns the number of columns the console has available.
	 *
	 * @return The number of columns displayed.
	 * @since 2016/05/14
	 */
	public final int getColumns()
	{
		// Lock
		synchronized (lock)
		{
			return _cols;
		}
	}
	
	/**
	 * Returns the number of rows the console has available.
	 *
	 * @return The number of rows displayed.
	 * @since 2016/05/14
	 */
	public final int getRows()
	{
		// Lock
		synchronized (lock)
		{
			return _rows;
		}
	}
	
	/**
	 * Invoked {@code put(__c, __r, __cs, 0, __cs.length())}.
	 *
	 * @param __c The column to place at.
	 * @param __r The row to place at.
	 * @param __cs The sequence to place.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final void put(int __c, int __r, CharSequence __cs)
		throws NullPointerException
	{
		put(__c, __r, __cs, 0, __cs.length());
	}
	
	/**
	 * Puts the given character sequence onto the console surface.
	 *
	 * Note that if the length added to the offset exceeds the length of the
	 * character sequence then placement of characters stops. Also, if a
	 * sequence exceeds the columns in the terminal it will not wrap around.
	 *
	 * @param __c The column to place at.
	 * @param __r The row to place at.
	 * @param __cs The sequence to place.
	 * @param __o The offset to read characters from.
	 * @param __l The maximum number of characters to write.
	 * @throws IndexOutOfBoundsException If the offset or length are negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final void put(int __c, int __r, CharSequence __cs, int __o,
		int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__cs == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Nothing to place
		if (__l == 0 || __r < 0 || __c < (-__l))
			return;
		
		// Lock
		synchronized (lock)
		{
			// Get console details
			char[] chars = this._chars;
			byte[] attrs = this._attrs;
			int cells = this._cells;
			int cols = this._cols;
			int rows = this._rows;
			byte attrbrush = this._attrbrush;
			
			// Off the end of the console? Do nothing
			if (__c >= cols || __r >= rows)
				return;
			
			// Reading characters could run off the length since the sequence
			// may be modified during execution and
			try
			{
				for (int i = 0, cx = __c, dx = (__r * cols) + __c; i < __l;
					i++, cx++, dx++)
				{
					// If off the left end, ignore
					if (cx < 0)
						continue;
					
					// Cannot draw any more so stop
					else if (cx >= cols)
						break;
					
					// Place character
					chars[dx] = __cs.charAt(__o + i);
					attrs[dx] = attrbrush;
				}
			}
			
			// Character was out of bounds, so just ignore it
			catch (IndexOutOfBoundsException e)
			{
			}
		}
	}
	
	/**
	 * Return the raw character array.
	 *
	 * @return The raw character array.
	 * @since 2016/05/14
	 */
	protected final char[] rawChars()
	{
		// Lock
		synchronized (lock)
		{
			return _chars;
		}
	}
	
	/**
	 * Returns the raw attribute array.
	 *
	 * @return The raw attribute array.
	 * @since 2016/05/14
	 */
	protected final byte[] rawAttributes()
	{
		// Lock
		synchronized (lock)
		{
			return _attrs;
		}
	}
	
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
		
		// If the given size is not supported, then do nothing
		// However always support 1x1 character terminals
		if (__c > 1 && __r > 1 && !supportsSize(__c, __r))
			return false;
		
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
				
				// Report size change
				sizeChanged();
				
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

