// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream which is able to be indented on each new line.
 *
 * This class cannot handle indentation characters which are beyond the
 * standard ASCII range.
 *
 * @since 2018/05/14
 */
public final class IndentedOutputStream
	extends OutputStream
{
	/** The output stream to write to. */
	protected final OutputStream out;
	
	/** The indentation character. */
	private char _char;
	
	/** The indentation level. */
	private int _level;
	
	/** On a new line? */
	private boolean _newline =
		true;
	
	/**
	 * Initializes the indented output stream using tab as the indentation
	 * character.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public IndentedOutputStream(OutputStream __out)
		throws NullPointerException
	{
		this(__out, '\t');
	}
	
	/**
	 * Initializes the indented output stream.
	 *
	 * @param __out The stream to write to.
	 * @param __c The character to indent with.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public IndentedOutputStream(OutputStream __out, char __c)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
		this._char = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/14
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.out.close();
	}
	
	/**
	 * Decrements the indentation level.
	 *
	 * @since 2018/05/14
	 */
	public final void decrement()
	{
		int level = this._level;
		if (level > 0)
			this._level = level - 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/14
	 */
	@Override
	public final void flush()
		throws IOException
	{
		this.out.flush();
	}
	
	/**
	 * Increments the indentation level.
	 *
	 * @since 2018/05/14
	 */
	public final void increment()
	{
		int level = this._level;
		if (level < Integer.MAX_VALUE)
			this._level = level + 1;
	}
	
	/**
	 * Sets the level of indentation.
	 *
	 * @param __i The indentation level.
	 * @throws IllegalArgumentException If the level is negative.
	 * @since 2018/05/14
	 */
	public final void setLevel(int __i)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BD0l Cannot set the indentation level to a
		// negative value.}
		if (__i < 0)
			throw new IllegalArgumentException("BD0l");
		
		this._level = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/14
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		OutputStream out = this.out;
		
		// New-line will set the newline flag
		int actb = (__b & 0xFF);
		if (actb == '\r' || actb == '\n')
			this._newline = true;
		
		// Every other character will indent, if on a newline
		else if (this._newline)
		{
			// Clear flag to not indent again
			this._newline = false;
			
			// Indent using the given character
			char indentchar = this._char;
			for (int i = 0, n = this._level; i < n; i++)
				out.write(indentchar);
		}
		
		// Forward always, but use the original input
		out.write(__b);
	}
}

