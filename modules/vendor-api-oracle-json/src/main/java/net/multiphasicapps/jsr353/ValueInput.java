// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonException;
import com.oracle.json.JsonValue;
import com.oracle.json.stream.JsonParsingException;
import java.io.Closeable;

/**
 * This reads input from an existing JSON object.
 *
 * @since 2014/08/08
 */
public class ValueInput
	extends BaseDecoder.Input
	implements Closeable
{
	/** Value to read from. */
	private JsonValue _val;
	
	/**
	 * Reads JSON data from the specified value.
	 *
	 * @param __v Value to read data from.
	 * @since 2014/08/08
	 */
	public ValueInput(JsonValue __v)
	{
		
		// Cannot be null
		if (__v == null)
			throw new NullPointerException("No base value specified.");
		
		// Set
		this._val = __v;
	}
	
	/**
	 * Reads some input token which could be from some varying kind of
	 * input.
	 *
	 * @return The raw type of token just read, {@code null} if there is
	 * nothing left.
	 * @throws JsonException Any internal reading errors possibly.
	 * @throws JsonParsingException On any parser errors.
	 * @since 2014/08/08
	 */
	@Override
	protected Data next()
	{
		throw new RuntimeException("TODO -- ValueInput::next() is not " +
			"implemented.");
	}
	
	/**
	 * Closes the input.
	 *
	 * @throws JsonException If it cannot be closed.
	 * @since 2014/08/08
	 */
	@Override
	public void close()
	{
		synchronized (this.ilock)
		{
			// Ignore if already closed
			if (this._val == null)
				return;
			
			// Mark closed
			this._val = null;
		}
	}
}

