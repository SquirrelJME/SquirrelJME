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
 *  - {@code '-'} -- Left justified
 *    (general, char, int, float, date).
 *  - {@code '#'} -- Conversion dependent alternate form
 *    (general, int (only: o, x, X), float).
 *  - {@code '+'} -- Include a sign always
 *    (int, float).
 *  - {@code ' '} -- Include leading space for positive values
 *    (int, float).
 *  - {@code '0'} -- Zero padding
 *    (int, float).
 *  - {@code ','} -- Use locale specific grouping separators
 *    (int (only: d), float).
 *  - {@code '('} -- Negative values are enclosed in parenthesis
 *    (int, float).
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
	/** The newline sequence. */
	private static final String _NEWLINE;
	
	/** The appendable to write to. */
	private final Appendable _out;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The IOException if any was generated. */
	private IOException _ioe;
	
	/**
	 * Cache the line separator which is derived from the system properties.
	 *
	 * @since 2018/10/10
	 */
	static
	{
		String nl;
		try
		{
			nl = System.getProperty("line.separator");
		}
		catch (SecurityException e)
		{
			nl = "\n";
		}
		
		_NEWLINE = nl;
	}
	
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
		
		// Global state needed for argument handling.
		__PrintFGlobal__ pg = new __PrintFGlobal__(__args);
		
		// Writing to the appendable may cause an exception to occur
		try
		{
			// Process input characters
			Appendable out = this._out;
			for (int i = 0, n = __fmt.length(), next = 0; i < n; i = next)
			{
				char c = __fmt.charAt(i);
				
				// Just a normal character
				if (c != '%')
				{
					out.append(c);
					
					// Just skip the single character
					next = i + 1;;
					continue;
				}
				
				// It is simpler to handle the parsing of the specifier in
				// another method due to loops and variables
				__PrintFState__ pf = new __PrintFState__(pg);
				next = this.__specifier(pf, i, __fmt);
				
				// Handle output of the specifier
				this.__output(out, pf);
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
	 * Outputs to the state to the appendable.
	 *
	 * @param __out The output appendable.
	 * @param __pf The standard state.
	 * @throws IllegalArgumentException If the format is not valid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/28
	 */
	private static void __output(Appendable __out, __PrintFState__ __pf)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__out == null || __pf == null)
			throw new NullPointerException("NARG");
			
		__PrintFConversion__ conv = __pf._conv;
		__PrintFCategory__ cat = conv.__category();
		
		// Depends on the conversion
		String append;
		switch (conv)
		{
				// Newline sequence
			case NEWLINE:
				append = Formatter._NEWLINE;
				break;
				
				// Single character
			case CHARACTER:
				Character cha = __pf.<Character>__argument(Character.class);
				append = (cha == null ? "null" : cha.toString());
				break;
			
				// Simple string conversion
			case STRING:
				append = __pf.<Object>__argument(Object.class, "null").
					toString();
				break;
			
				// {@squirreljme.error ZZ1z Unimplemented conversion.
				// (The conversion)}
			default:
				throw new todo.TODO("ZZ1z " + conv);
		}
		
		// Convert to uppercase
		if (__pf._upper)
			append = append.toUpperCase();
		
		// General items may be reduced by the precision, at most
		if (cat == __PrintFCategory__.GENERAL)
		{
			int precision = __pf._precision;
			if (precision >= 0)
				append = append.substring(0,
					Math.min(precision, append.length()));
		}
		
		// Width is either higher of the input width or the string length
		int strlen = append.length(),
			width = Math.max(strlen, __pf._width),
			padding = width - strlen;
		
		// Right justification spaces?
		boolean isleft = __pf.__isLeftJustified();
		if (!isleft)
			for (int i = 0; i < padding; i++)
				__out.append(' ');
		
		// Send the string in
		__out.append(append);
		
		// Left justification spaces?
		if (isleft)
			for (int i = 0; i < padding; i++)
				__out.append(' ');
	}
	
	/**
	 * Parses the specifier in the input format.
	 *
	 * This parses the following, just returning that information:
	 * {@code %[argument_index$][flags][width][.precision]conversion}.
	 *
	 * @param __pf The PrintF state.
	 * @param __base The base character index.
	 * @param __fmt The format specifiers.
	 * @return The index where the format specifier ends, this is so the
	 * calling loop can properly set its counter.
	 * @throws IllegalArgumentException If the format specifiers are not
	 * correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/23
	 */
	private static int __specifier(__PrintFState__ __pf, int __base,
		String __fmt)
		throws IllegalArgumentException, NullPointerException
	{
		if (__pf == null || __fmt == null)
			throw new NullPointerException("NARG");
		
		// Length of the format
		int fmtlen = __fmt.length();
		
		// The specifier could be too short! So handle these cases and make
		// sure the exception of the right type
		int at = __base + 1;
		try
		{
			char c = __fmt.charAt(at);
			
			// The first might be a number specifying the argument index to use
			// Note that zero is NOT included because it would be parsed as
			// a flag, additionally argument indexes are 1-based!
			if (c >= '1' && c <= '9')
			{
				// Read the entire number
				int base = at;
				for (;;)
				{
					c = __fmt.charAt(at);
					if (c >= '0' && c <= '9')
						at++;
					else
						break;
				}
				
				// Parse value
				int val = Integer.valueOf(__fmt.substring(base, at));
				
				// If there is a dollar sign, this is going to be the
				// argument index
				if (__fmt.charAt(at) == '$')
				{
					__pf.__setArgumentIndex(val);
					
					// Skip the dollar
					at++;
				}
				
				// Otherwise this is the width
				else
					__pf.__setWidth(val);
			}
			
			// Parse flags, but only if no width was specified
			if (!__pf.__hasWidth())
				for (c = __fmt.charAt(at); __pf.__setFlag(c); at++)
					;
			
			// Parse width
			c = __fmt.charAt(at);
			if (!__pf.__hasWidth() && c >= '1' && c <= '9')
			{
				// Read the entire number
				int base = at;
				for (;;)
				{
					c = __fmt.charAt(at);
					if (c >= '0' && c <= '9')
						at++;
					else
						break;
				}
				
				// Set width
				__pf.__setWidth(Integer.valueOf(__fmt.substring(base, at)));
			}
			
			// Parse precision
			c = __fmt.charAt(at);
			if (c == '.')
				throw new todo.TODO();
			
			// Parse the conversion
			c = __fmt.charAt(at);
			if (c == 't' || c == 'T')
			{
				__pf.__setConversion(c, __fmt.charAt(at + 1));
				at++; 
			}
			else
				__pf.__setConversion(c, -1);
			
			// Skip conversion character
			at++;
		}
		
		// {@squirreljme.error ZZ1m Could not parse the format specifier
		// properly. (The string with the specifier sequence)}
		catch (IndexOutOfBoundsException|NumberFormatException e)
		{
			throw new IllegalArgumentException("ZZ1m " +
				__fmt.substring(__base, Math.min(at, __fmt.length())), e);
		}
		
		// Return the new ending index, after everything was parsed
		return at;
	}
}

