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
 * Writes CSV data to an output file.
 *
 * @param <T> The type to write.
 * @since 2023/09/12
 */
public class CsvWriter<T>
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
}
