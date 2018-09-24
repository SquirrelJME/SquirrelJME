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
 * Format specifiers start with the {@code '%'} character which defines a
 * sequence accordingly. The format specifiers are in the following format:
 * {@code %[argument_index$][flags][width][.precision]conversion}. Any fields
 * within brackets are optional. For any specifiers which do not conform to
 * arguments, their format is {@code %[flags][width]conversion}.
 *
 * {@code argument_index$} specifies which argument to take the value from,
 * this allows one to use an alternative order. If this is not specified then
 * the order is implied based on the argument order for any specifiers which
 * do not use an argument index. This means that:
 * {@code format("%7$d %d %6$d %d %% %d", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)} will
 * print {@code "7 1 6 2 % 3"}.
 *
 * {@code flags} specifies special flags which are used to modify how the
 * printing is done. The following flags are used:
 *
 * {@code width} is a positive decimal integer which specifies the minimum
 * amount of space the text will take up, it will be aligned accordingly
 * depending on the flags used.
 *
 * {@code precision} is a positive decimal integer which specified a limit
 * on the output, this depends on the format specifier.
 *
 * {@code conversion} is the symbol which determines how the input is to be
 * formatted.
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
		
		// Writing to the appendable may cause an exception to occur
		try
		{
			// Process input characters
			Appendable out = this._out;
			for (int i = 0, n = __fmt.length(); i < n; i++)
			{
				char c = __fmt.charAt(i);
				
				// Just a normal character
				if (c != '%')
				{
					out.append(c);
					continue;
				}
				
				// It is simpler to handle the parsing of the specifier in
				// another method due to loops and variables
				i = this.__parseSpecifier(out, i, __fmt, __args);
			}
		}
		
		// Catch any exception
		catch (IOException e)
		{
			this._ioe = e;
		}
		
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
	
	/**
	 * Parses the specifier in the input format.
	 *
	 * @param __out The output sink.
	 * @param __i The index of the specifier.
	 * @param __fmt The formatted text.
	 * @param __args The arguments.
	 * @return The index where the format specifier ends, this is so the
	 * calling loop can properly set its counter.
	 * @throws IllegalArgumentException If the format specifiers are not
	 * correct.
	 * @throws IOException If writing failed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/23
	 */
	private static int __parseSpecifier(Appendable __out, int __i,
		String __fmt, Object... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__out == null || __fmt == null)
			throw new NullPointerException("NARG");
				
		throw new todo.TODO();
	}
}

