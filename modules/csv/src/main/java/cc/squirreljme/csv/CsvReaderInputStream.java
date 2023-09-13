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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Not Described.
 *
 * @since 2023/09/12
 */
public class CsvReaderInputStream
	implements CsvInputStream
{
	/**
	 * Initializes the reader.
	 *
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReaderInputStream(ByteArrayOutputStream __in)
		throws NullPointerException
	{
		this(__in.toByteArray());
	}
	
	/**
	 * Initializes the reader.
	 *
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReaderInputStream(byte[] __in)
		throws NullPointerException
	{
		this(new ByteArrayInputStream(__in));
	}
	
	/**
	 * Initializes the reader.
	 *
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReaderInputStream(InputStream __in)
		throws NullPointerException
	{
		this(CsvReaderInputStream.__newUtfReader(__in));
	}
	
	/**
	 * Initializes the reader.
	 *
	 * @param __in The input.
	 * @param __encoding The encoding used.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReaderInputStream(InputStream __in, String __encoding)
		throws IOException, NullPointerException
	{
		this(new InputStreamReader(__in, __encoding));
	}
	
	/**
	 * Initializes the reader.
	 *
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReaderInputStream(Reader __in)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public boolean next(StringBuilder __line)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes a new reader for the given data.
	 *
	 * @param __in The stream to read from.
	 * @return The resultant reader.
	 * @since 2023/09/12
	 */
	private static Reader __newUtfReader(InputStream __in)
	{
		try
		{
			return new InputStreamReader(__in, "utf-8");
		}
		catch (IOException __e)
		{
			throw new RuntimeException("UTF8");
		}
	}
}
