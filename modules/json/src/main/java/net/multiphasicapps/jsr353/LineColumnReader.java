// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This is similar to {@code LineNumberReader} except that it also supports
 * reading of column numbers. Note that column numbers are zero based.
 *
 * @since 2014/08/02
 */
public class LineColumnReader
	extends BufferedReader
{
	/** Default system wide tab character size. */
	public static final int DEFAULT =
		Integer.MIN_VALUE;
	
	/** Internal default tab size. */
	private static final int INTERNDEFTS =
		8;
	
	/** The current line. */
	protected int line;
	
	/** The current read column. */
	protected int column;
	
	/** The size of the tab character. */
	protected int tabsize;
	
	/** Marked column. */
	private int _mkdcol =
		-1;
	
	/**
	 * Initializes the line and column count reader with a default tab size.
	 *
	 * @param __r Input stream to read from.
	 * @since 2014/12/19
	 */
	public LineColumnReader(Reader __r)
	{
		this(__r, LineColumnReader.DEFAULT);
	}
	
	/**
	 * Initializes the line and column count reader.
	 *
	 * @param __r Input stream to read from.
	 * @param __ts Size of tabulation characters.
	 * @since 2014/08/02
	 */
	public LineColumnReader(Reader __r, int __ts)
	{
		super(__r);
		
		// Set tab
		this.setTabSize(__ts);
	}
	
	/**
	 * Initializes the line and column count reader.
	 *
	 * @param __r Input stream to read from.
	 * @param __ts Size of tabulation characters.
	 * @param __sz Size of the underlying buffer.
	 * @since 2014/08/02
	 */
	public LineColumnReader(Reader __r, int __ts, int __sz)
	{
		super(__r, __sz);
		
		// Set tab
		this.setTabSize(__ts);
	}
	
	/**
	 * Closes the number reader and the underlying input stream.
	 *
	 * @throws IOException If there was an error closing.
	 * @since 2014/08/06
	 */
	@Override
	public void close()
		throws IOException
	{
		synchronized(this.lock)
		{
			// Has been closed, ignore
			if (this.tabsize < 0)
				return;
			
			// Set closed and close under stream
			this.tabsize = -1;
			super.close();
		}
	}
	
	/**
	 * Returns the current column number, zero indexed.
	 *
	 * @return The current column number.
	 * @throws IllegalStateException If the stream has been closed.
	 * @since 2014/08/02
	 */
	public int getColumnNumber()
	{
		synchronized (this.lock)
		{
			// Closed
			if (this.tabsize < 0)
				throw new IllegalStateException("Stream has been closed.");
			
			return this.column;
		}
	}
	
	/**
	 * Returns the line number.
	 * 
	 * @return The line number.
	 * @since 2022/07/12
	 */
	public int getLineNumber()
	{
		synchronized (this.lock)
		{
			// Closed
			if (this.tabsize < 0)
				throw new IllegalStateException("CLOS");
			
			return this.line;
		}
	}
	
	/**
	 * Returns the current tab size.
	 *
	 * @return The current tab size.
	 * @throws IllegalStateException If the stream has been closed.
	 * @since 2014/08/02
	 */
	public int getTabSize()
	{
		synchronized (this.lock)
		{
			// Closed
			if (this.tabsize < 0)
				throw new IllegalStateException("Stream has been closed.");
			
			// Return it
			return this.tabsize;
		}
	}
	
	/**
	 * Marks the stream to that it may later be {@link #reset()}.
	 *
	 * @param __ral Read-ahead limit, number of characters to mark.
	 * @throws IOException If the stream could not be marked.
	 * @since 2014/09/01
	 */
	@Override
	public void mark(int __ral)
		throws IOException
	{
		// Cannot be negative
		if (__ral < 0)
			throw new IllegalArgumentException(String.format(
				"Negative read-ahead of %1$d.", __ral));
		
		// Locked
		synchronized (this.lock)
		{
			// Remember column
			this._mkdcol = this.column;
			
			// Mark super (does line number too)
			super.mark(__ral);
		}
	}
	
	/**
	 * Reads a bunch of characters from the input stream and determines the
	 * current column number from them.
	 *
	 * @param __b Buffer to read into.
	 * @param __o Offset into buffer.
	 * @param __l Length of read count.
	 * @return The number of bytes read.
	 * @throws IOException On any I/O error.
	 * @since 2014/08/02
	 */
	@Override
	public int read(char[] __b, int __o, int __l)
		throws IOException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("Null arguments");
		if (__o < 0 || __l < 0 || __o + __l > __b.length)
			throw new IllegalArgumentException("Invalid offset or length.");
		
		// Lock on object
		synchronized (this.lock)
		{
			// Cannot have been closed
			if (this.tabsize < 0)
				throw new IllegalStateException("Stream has been closed.");
			
			// Read into a new buffer.
			char[] nb = new char[__l];
			int rc = super.read(nb, 0, nb.length);
		
			// EOF or nothing read
			if (rc <= 0)
				return rc;
		
			// Go through characters and evaluate them, luckily \n are
			// compacted
			for (int i = 0; i < rc; i++)
			{
				char c = nb[i];
			
				// Tab
				if (c == '\t')
					this.column = (this.column -
						(this.column % this.tabsize)) + this.tabsize;
			
				// New line
				else if (c == '\n')
				{
					this.column = 0;
					this.line++;
				}
			
				// Other character
				else
					this.column++;
			}
		
			// Copy buffer to target
			for (int i = 0; i < rc; i++)
				__b[__o + i] = nb[i];
		
			// Return the read count
			return rc;
		}
	}
	
	/**
	 * Reads a character from the input stream and returns it.
	 *
	 * @return The read character or {@code -1} on EOF.
	 * @throws IOException On any I/O error.
	 * @since 2014/08/02
	 */
	@Override
	public int read()
		throws IOException
	{
		synchronized (this.lock)
		{
			char[] b = new char[1];
			if (this.read(b, 0, 1) == 1)
				return b[0];
			return -1;
		}
	}
	
	/**
	 * Resets the stream, back to the position it was at when the method
	 * {@link #mark(int)} was invoked.
	 *
	 * @throws IOException If the stream could not be reset.
	 * @since 2014/09/01
	 */
	@Override
	public void reset()
		throws IOException
	{
		// Locked
		synchronized (this.lock)
		{
			// Was never marked?
			if (this._mkdcol < 0)
				throw new IOException("Stream was never marked.");
			
			// Restore column value
			this.column = this._mkdcol;
			
			// Reset super
			super.reset();
		}
	}
	
	/**
	 * Sets a new column number.
	 *
	 * @param __cn New column number to set.
	 * @throws IllegalStateException If the stream has been closed.
	 * @throws IllegalArgumentException If the column number is negative.
	 * @since 2014/08/02
	 */
	public void setColumnNumber(int __cn)
	{
		// Cannot be negative
		if (__cn < 0)
			throw new IllegalArgumentException(String.format(
				"Negative column number %1$d.", __cn));
		
		// Locked
		synchronized (this.lock)
		{
			// Closed
			if (this.tabsize < 0)
				throw new IllegalStateException("Stream has been closed.");
			
			// Set
			this.column = __cn;
		}
	}
	
	/**
	 * Sets the tab size, if {@link #DEFAULT} is used then the system
	 * property "{@code tab.size}" is used if one is set, otherwise 8 is
	 * used. Does nothing if the stream is closed.
	 *
	 * @param __ts New tab size to set.
	 * @since 2014/08/02
	 */
	public void setTabSize(int __ts)
	{
		// Cannot be <= 0
		if (__ts <= 0 && __ts != LineColumnReader.DEFAULT)
			throw new IllegalArgumentException(String.format(
				"Zero or negative tab size (%1$d).", __ts));
		
		synchronized (this.lock)
		{
			// Has been closed
			if (this.tabsize < 0)
				return;
			
			// Normal
			if (__ts != LineColumnReader.DEFAULT)
				this.tabsize = __ts;
			
			// Use system property
			else
			{
				try
				{
					String swts = System.getProperty("tab.size");
					
					// If no property use default
					if (swts == null)
						throw new NumberFormatException();
				
					// As number
					this.tabsize = Integer.valueOf(swts);
				}
			
				// Not allowed; Not a number, or way out of range.
				catch (SecurityException|NumberFormatException e)
				{
					this.tabsize = LineColumnReader.INTERNDEFTS;
				}
			}
		}
	}
}

