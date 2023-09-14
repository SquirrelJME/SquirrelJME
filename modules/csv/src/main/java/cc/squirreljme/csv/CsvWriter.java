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
			
			throw Debugging.todo();
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
	private void __writeRow(CharSequence[] __row)
		throws IOException, NullPointerException
	{
		if (__row == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
