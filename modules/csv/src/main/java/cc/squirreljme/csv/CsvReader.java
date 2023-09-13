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

/**
 * Reads CSV files and writes to an output.
 *
 * @param <T> The type of value to read.
 * @since 2023/09/12
 */
public class CsvReader<T>
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
}
