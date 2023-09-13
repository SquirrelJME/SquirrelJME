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

/**
 * Writes CSV data to an output file.
 *
 * @param <T> The type to write.
 * @since 2023/09/12
 */
public final class CsvWriter<T>
	implements Closeable
{
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * Writes all the given values.
	 *
	 * @param __values The values to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void writeAll(Iterable<T> __values)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
