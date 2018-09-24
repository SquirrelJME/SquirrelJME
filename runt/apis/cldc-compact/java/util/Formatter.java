// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * This class implements C-style {@code printf()} formatted for text, it is
 * inspired by the C language but it does not match it exactly and this is
 * far more strict in what it requires.
 *
 * @since 2018/09/23
 */
@ImplementationNote("")
public final class Formatter
	implements Closeable, Flushable
{
	/** The appendable to write to. */
	private final Appendable _out;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The IOException if any was generated. */
	private IOException _ioe;
	
	/**
	 * Initializes a formatter which uses an internal {@link StringBuilder}.
	 *
	 * @since 2018/09/23
	 */
	public Formatter()
	{
		this._out = new StringBuilder();
	}
	
	/**
	 * Initializes the formatter writing to the given output.
	 *
	 * @param __a The output.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/23
	 */
	public Formatter(Appendable __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this._out = __a;
	}
	
	/**
	 * Initializes the formatter writing to the given output.
	 *
	 * @param __ps The output.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/23
	 */
	public Formatter(PrintStream __ps)
		throws NullPointerException
	{
		this((Appendable)__ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public void close()
	{
		// No effect?
		if (this._closed)
			return;
		
		// Set closed
		this._closed = true;
		
		// Can only be done if it is closeable
		Appendable out = this._out;
		if (out instanceof Closeable)
			try
			{
				((Closeable)out).close();
			}
			catch (IOException e)
			{
				this._ioe = e;
			}
	}
	
	/**
	 * Flushes the output formatter, if it is flushable.
	 *
	 * @throws IllegalStateException If this formatter was closed.
	 * @since 2018/09/23
	 */
	@Override
	public void flush()
		throws IllegalStateException
	{
		// {@squirreljme.error ZZ1k The formatter has been closed.}
		if (this._closed)
			throw new IllegalStateException("ZZ1k");
		
		// Can only be done if it is flushable
		Appendable out = this._out;
		if (out instanceof Flushable)
			try
			{
				((Flushable)out).flush();
			}
			catch (IOException e)
			{
				this._ioe = e;
			}
	}
	
	/**
	 * Prints formatted text to the output.
	 *
	 * @param __fmt The format specified.
	 * @param __args The input arguments.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the format specifiers are not
	 * valid.
	 * @throws IllegalStateException If this formatter has been closed.
	 * @throws NullPointerException If no format was specified.
	 * @since 2018/09/23
	 */
	public Formatter format(String __fmt, Object... __args)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__fmt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ1l This formatter has been closed.}
		if (this._closed)
			throw new IllegalStateException("ZZ1l");
		
		// Force this to exist
		if (__args == null)
			__args = new Object[0];
		
		if (true)
			throw new todo.TODO();
		
		return this;
	}
	
	/**
	 * Returns the last {@link IOException} that was thrown by the output.
	 *
	 * @return The last exception thrown or {@code null} if one was never
	 * thrown.
	 * @since 2018/09/23
	 */
	public IOException ioException()
	{
		return this._ioe;
	}
	
	/**
	 * Returns the output.
	 *
	 * @return The output.
	 * @throws IllegalStateException If this formatter was closed.
	 * @since 2018/09/23
	 */
	public Appendable out()
		throws IllegalStateException
	{
		// {@squirreljme.error ZZ1i The formatter has been closed.}
		if (this._closed)
			throw new IllegalStateException("ZZ1i");
		
		return this._out;
	}
	
	/**
	 * This performs {@code this.out().toString()} and returns that result, as
	 * such the value here might not match what was written into the string.
	 *
	 * @return The string from the output.
	 * @throws IllegalStateException If this formatter was closed.
	 * @since 2018/09/23
	 */
	@Override
	public String toString()
		throws IllegalStateException
	{
		// {@squirreljme.error ZZ1j The formatter has been closed.}
		if (this._closed)
			throw new IllegalStateException("ZZ1j");
		
		return this._out.toString();
	}
}

