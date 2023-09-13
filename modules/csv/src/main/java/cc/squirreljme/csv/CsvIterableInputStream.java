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

/**
 * Input stream for CSVs based on {@code Iterable}.
 *
 * @since 2023/09/12
 */
public class CsvIterableInputStream
	implements CsvInputStream
{
	/**
	 * Initializes the input stream.
	 *
	 * @param __it The iterable to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvIterableInputStream(Iterable<String> __it)
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
}
