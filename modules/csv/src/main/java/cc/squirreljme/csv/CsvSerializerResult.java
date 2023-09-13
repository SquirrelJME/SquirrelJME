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
 * The result for CSV serialization.
 *
 * @since 2023/09/12
 */
public final class CsvSerializerResult
{
	/**
	 * Initializes the headers used for output.
	 *
	 * @param __headers The headers used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void headers(String... __headers)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Indicates the end of a row. 
	 *
	 * @since 2023/09/12
	 */
	public void endRow()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Indicates a key set to the given value.
	 *
	 * @param __key The key used.
	 * @param __value The value set.
	 * @throws IllegalArgumentException If the key is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void value(String __key, String __value)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
}
