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
	volatile String[] _headers;
	
	/** The currently loaded in values. */
	volatile String[] _values;
	
	/** Was the row ended? */
	volatile boolean _endRow;
	
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
		String[] headers = new String[n];
		for (int i = 0; i < n; i++)
			headers[i] = __headers[i];
		
		this._headers = headers;
		this._values = new String[n];
	}
	
	/**
	 * Indicates the end of a row. 
	 *
	 * @throws IllegalStateException If this was already called.
	 * @since 2023/09/12
	 */
	public void endRow()
		throws IllegalStateException
	{
		/* {@squirreljme.error CS07 End of row already called.} */
		if (this._endRow)
			throw new IllegalStateException("CS07");
		
		// Just set the flag
		this._endRow = true;
	}
	
	/**
	 * Indicates a key set to the given value.
	 *
	 * @param __key The key used.
	 * @param __value The value set.
	 * @throws IllegalArgumentException If the key is not valid.
	 * @throws IllegalStateException If headers were not yet set or the end
	 * of row was called.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void value(String __key, String __value)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CS02 Headers not yet set.} */
		String[] headers = this._headers;
		if (headers == null)
			throw new IllegalStateException("CS02");
		
		/* {@squirreljme.error CS06 End of row already called.} */
		if (this._endRow)
			throw new IllegalStateException("CS06");
		
		// Find header that contains the value and store it there
		String[] values = this._values;
		for (int i = 0, n = headers.length; i < n; i++)
			if (__key.equals(headers[i]))
				values[i] = __value;
	}
	
	/**
	 * Resets the result.
	 *
	 * @since 2023/09/14
	 */
	void __reset()
	{
		// Clear all values
		String[] values = this._values;
		if (values != null)
			for (int i = 0, n = values.length; i < n; i++)
				values[i] = null;
		
		// Clear end of row
		this._endRow = false;
	}
}
