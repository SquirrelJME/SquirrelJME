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
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Reads CSV files and writes to an output.
 *
 * @param <T> The type of value to read.
 * @since 2023/09/12
 */
public final class CsvReader<T>
{
	/**
	 * Initializes the reader.
	 *
	 * @param __deserializer The deserializer input.
	 * @param __input The input to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReader(CsvDeserializer<T> __deserializer,
		CsvReaderInputStream __input)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Reads a single value.
	 *
	 * @return The single value.
	 * @throws NoSuchElementException If there is nothing left.
	 * @throws IOException On read errors.
	 * @since 2023/09/12
	 */
	public T read()
		throws NoSuchElementException, IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Reads all the values into the given collection.
	 *
	 * @param __into The collection to write into.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void readAll(Collection<T> __into)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
