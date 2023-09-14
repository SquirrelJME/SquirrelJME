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
	/** The headers used. */
	volatile CharSequence[] _headers;
	
	/**
	 * Initializes the headers used for output.
	 *
	 * @param __headers The headers used.
	 * @throws IllegalStateException If headers were already defined.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void headers(String... __headers)
		throws IllegalStateException, NullPointerException
	{
		if (__headers == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CS01 Headers already determined.} */
		if (this._headers != null)
			throw new IllegalStateException("CS01");
		
		// Copy over
		int n = __headers.length;
		CharSequence[] headers = new CharSequence[n];
		for (int i = 0; i < n; i++)
			headers[i] = __headers[i];
		
		this._headers = headers;
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
	 * @throws IllegalStateException If headers were not yet set.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void value(String __key, String __value)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__key == null || __value == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CS02 Headers not yet set.} */
		CharSequence[] headers = this._headers;
		if (headers != null)
			throw new IllegalStateException("CS02");
		
		throw Debugging.todo();
	}
	
	/**
	 * Resets the result.
	 *
	 * @since 2023/09/14
	 */
	void __reset()
	{
		throw Debugging.todo();
	}
}
