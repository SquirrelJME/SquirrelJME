// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

/**
 * Writes CSV data to an output file.
 *
 * @param <T> The type to write.
 * @since 2023/09/12
 */
public final class CsvWriter<T>
	implements Closeable
{
	/** The serializer used. */
	protected final CsvSerializer<T> serializer;
	
	/** The output target. */
	protected final Appendable out;
	
	/** The working buffer. */
	private final StringBuilder _buffer =
		new StringBuilder();
	
	/** The internal result. */
	private final CsvSerializerResult _result =
		new CsvSerializerResult();
	
	/** Were the headers written? */
	private volatile boolean _wroteHeaders;
	
	/**
	 * Initializes the CSV writer.
	 *
	 * @param __serializer The serializer for encoding data.
	 * @param __out The output source to write rows to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvWriter(CsvSerializer<T> __serializer, Appendable __out)
		throws NullPointerException
	{
		if (__serializer == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.serializer = __serializer;
		this.out = __out;
		
		// Always determine target headers to use
		__serializer.serializeHeaders(this._result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public void close()
		throws IOException
	{
		// Write the headers?
		IOException suppress = null;
		try
		{
			if (!this._wroteHeaders)
			{
				this.__writeRow(this._result._headers);
				this._wroteHeaders = true;
			}
		}
		catch (IOException __e)
		{
			suppress = __e;
		}
		
		// Forward close to the target appendable
		Appendable out = this.out;
		if (out instanceof Closeable)
			((Closeable)out).close();
		else if (out instanceof AutoCloseable)
			try
			{
				((AutoCloseable)out).close();
			}
			catch (Exception __e)
			{
				if (suppress != null)
					__e.addSuppressed(suppress);
				
				if (__e instanceof RuntimeException)
					throw (RuntimeException)__e;
				else if (__e instanceof IOException)
					throw (IOException)__e;
				else
					throw new IOException("WRAP", __e);
			}
			
		// Suppressed?
		if (suppress != null)
			throw suppress;
	}
	
	/**
	 * Writes the given value.
	 *
	 * @param __value The value to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void write(T __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		// Write the headers?
		CsvSerializerResult result = this._result;
		if (!this._wroteHeaders)
		{
			this.__writeRow(result._headers);
			this._wroteHeaders = true;
		}
		
		// Determine values to write
		try
		{
			// Serialize value
			result.__reset();
			this.serializer.serialize(__value, result);
			
			/* {@squirreljme.error CS05 End of row not called.} */
			if (!result._endRow)
				throw new IllegalStateException("CS05");
			
			// Write row values
			this.__writeRow(result._values);
		}
		
		// Cleanup always
		finally
		{
			result.__reset();
		}
	}
	
	/**
	 * Writes all the given values.
	 *
	 * @param __values The values to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	@SuppressWarnings("unchecked")
	public void writeAll(T... __values)
		throws IOException, NullPointerException
	{
		if (__values == null)
			throw new NullPointerException("NARG");
		
		this.writeAll(Arrays.asList(__values));
	}
	
	/**
	 * Writes all the given values.
	 *
	 * @param __values The values to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void writeAll(Iterable<? extends T> __values)
		throws IOException, NullPointerException
	{
		if (__values == null)
			throw new NullPointerException("NARG");
		
		for (T value : __values)
			this.write(value);
	}
	
	/**
	 * Writes a row to the output.
	 *
	 * @param __row The row to write.
	 * @throws IOException On write errors.
	 * @since 2023/09/14
	 */
	private void __writeRow(String[] __row)
		throws IOException, NullPointerException
	{
		if (__row == null)
			throw new NullPointerException("NARG");
		
		Appendable out = this.out;
		
		// Handle each column
		for (int i = 0, n = __row.length; i < n; i++)
		{
			// Preface with comma for new columns
			if (i > 0)
				out.append(',');
			
			// Skip nulls and blanks
			String input = __row[i];
			if (input == null || input.isEmpty())
				continue;
			
			// Can directly output with no quoting needed
			if (!CsvWriter.__needQuote(input))
				out.append(input);
			
			// Need to properly encode values
			else
			{
				// Starting quote
				out.append('"');
				
				// Write every character, escaping strings accordingly
				for (int j = 0, p = input.length(); j < p; j++)
				{
					char c = input.charAt(j);
					
					// Escape double quotes
					if (c == '"')
						out.append('"');
					
					out.append(c);
				}
				
				// Ending quote
				out.append('"');
			}
		}
		
		// RFC4180 says to end with CRLF, do so
		out.append('\r');
		out.append('\n');
	}
	
	/**
	 * Does the given string need quoting?
	 *
	 * @param __input The input string.
	 * @return If quoting is needed.
	 * @throws IllegalArgumentException If the input character is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/17
	 */
	private static boolean __needQuote(String __input)
		throws IllegalArgumentException, NullPointerException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		// Check each character
		for (int i = 0, n = __input.length(); i < n; i++)
		{
			char c = __input.charAt(i);
			
			/* {@squirreljme.error CS03 Invalid CSV character.} */
			if (c == '\0' || c == '\r' || c == '\n')
				throw new IllegalArgumentException("CS03");
			
			// Commas, quotes, and start/end whitespace needs quotes
			if (c == ',' || c == '"' ||
				(c <= ' ' && i == 0) || (c <= ' ' && i == (n - 1)))
				return true;
		}
		
		// Not needed
		return false;
	}
}
