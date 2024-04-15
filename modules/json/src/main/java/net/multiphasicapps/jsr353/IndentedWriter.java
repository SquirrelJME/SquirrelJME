// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.Writer;

/**
 * This writer appends indentation to the output on the start of each line so
 * that methods do not need to be duplicated to perform this action.
 *
 * This class assumes that lines end with '\n'.
 *
 * @see Writer
 * @since 2014/04/29
 */
public class IndentedWriter
	extends Writer
{
	/** The stream to write to. */
	protected final Writer out;
	
	/** Indentation count. */
	private int _ic;
	
	/** Padding character. */
	private char _pad;
	
	/** Set to true when indentation is about to be done. */
	private boolean _doident;
	
	/** Stream was closed. */
	private boolean _closed;
	
	/**
	 * Initializes a new indented writer with the default indentation of zero
	 * using tabs.
	 *
	 * @param __w The writer to wrap.
	 * @since 2014/04/29
	 */
	public IndentedWriter(Writer __w)
	{
		this(__w, '\t', 0);
	}
	
	/**
	 * Initializes a new indented writer with the specified indent character
	 * and the amount of times to indent.
	 *
	 * @param __w The writer to wrap.
	 * @param __pad Padding character.
	 * @param __cnt Initial indentation.
	 * @since 2014/04/29
	 */
	public IndentedWriter(Writer __w, char __pad, int __cnt)
		throws NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		this.out = __w;
		
		// Set
		this._ic = __cnt;
		this._pad = __pad;
		
		// May never drop below zero
		if (this._ic < 0)
			this._ic = 0;
		
		// Initially indent only if non-zero, so that the first line becomes
		// indented as expected.
		this._doident = this._ic > 0;
	}
	
	/**
	 * Closes the indented writer and the underlying output stream.
	 *
	 * @throws IOException If there was an error closing the stream.
	 * @since 2014/08/05
	 */
	@Override
	public void close()
		throws IOException
	{
		synchronized (this.lock)
		{
			// Ignore if already closed
			if (this._closed)
				return;
			
			// Set closed and close inner stream
			this._closed = true;
			this.out.close();
		}
	}
	
	/**
	 * Writes a single character to the output stream.
	 *
	 * @param __c The character to write.
	 * @since 2014/04/29
	 */
	@Override
	public void write(int __c)
		throws IOException
	{
		synchronized (this.lock)
		{
			// If indenting, perform indentation
			if (this._doident)
			{
				// Write the indentation level
				for (int i = 0; i < this._ic; i++)
					this.out.write(this._pad);
			
				// No longer indent
				this._doident = false;
			}
		
			// If newline, set indentation mark
			if (__c == '\n')
				this._doident = true;
		
			// Write character to wrapped stream
			this.out.write(__c);
		}
	}
	
	/**
	 * Writes to the indentation stream with the specified buffer, offset, and
	 * length.
	 *
	 * @param __cbuf Character buffer to write to.
	 * @param __off Offset into the buffer.
	 * @param __len Length of the amount of data to write.
	 * @since 2014/04/29
	 */
	@Override
	public void write(char[] __cbuf, int __off, int __len)
		throws IOException
	{
		synchronized (this.lock)
		{
			// Cannot be null
			if (__cbuf == null)
				throw new NullPointerException("No character buffer set.");
		
			// Cannot be out of bounds
			if (__off < 0 || __off > __cbuf.length)
				throw new IndexOutOfBoundsException(String.format(
					"Illegal offset %1$d.", __off));
		
			// Cap length to input
			if (__off + __len > __cbuf.length)
				__len = __cbuf.length - __off;
		
			// Do not bother if not writing anything
			if (__len <= 0)
				return;
		
			// Write single characters
			for (int i = 0; i < __len; i++)
				this.write(__cbuf[__off + i]);
		}
	}
	
	/**
	 * Writes to the indentation stream with the specified String, offset, and
	 * length.
	 *
	 * This must be implemented so that the current methods are called.
	 *
	 * @param __s Character buffer to write to.
	 * @param __off Offset into the buffer.
	 * @param __len Length of the amount of data to write.
	 * @since 2014/04/29
	 */
	@Override
	public void write(String __s, int __off, int __len)
		throws IOException
	{
		synchronized (this.lock)
		{
			this.write(__s.toCharArray(), __off, __len);
		}
	}
	
	@Override
	public void flush()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets a new indentation level. A zero indentation is not possible.
	 *
	 * @param __v New indentation level.
	 * @since 2014/04/29
	 */
	public void setIndent(int __v)
	{
		synchronized (this.lock)
		{
			this._ic = __v;
			if (this._ic < 0)
				this._ic = 0;
		}
	}
	
	/**
	 * Sets the new padding character.
	 *
	 * @param __v New padding character.
	 * @since 2014/04/29
	 */
	public void setPad(char __v)
	{
		synchronized (this.lock)
		{
			this._pad = __v;
		}
	}
	
	/**
	 * Returns the current indentation level.
	 *
	 * @return The indentation level.
	 * @since 2014/04/29
	 */
	public int getIndent()
	{
		synchronized (this.lock)
		{
			return this._ic;
		}
	}
	
	/**
	 * Returns the current padding character.
	 *
	 * @return The padding character.
	 * @since 2014/04/29
	 */
	public char getPad()
	{
		synchronized (this.lock)
		{
			return this._pad;
		}
	}
	
	/**
	 * Adds a relative amount of indentation and returns the old identation
	 * level. A zero indentation is not possible.
	 *
	 * @param __v The relative amount of indentation to add.
	 * @return The old indentation level.
	 * @since 2014/05/29
	 */
	public int addIndent(int __v)
	{
		synchronized (this.lock)
		{
			int old = this._ic;
			this.setIndent(this._ic + __v);
			return old;
		}
	}
}

