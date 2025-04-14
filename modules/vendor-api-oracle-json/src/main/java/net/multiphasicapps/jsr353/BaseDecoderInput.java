// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonException;
import com.oracle.json.stream.JsonLocation;
import com.oracle.json.stream.JsonParsingException;
import java.io.Closeable;

/**
 * This class reads input in the form of very raw JSON tokens as it is up
 * to the decoder to handle them correctly.
 *
 * @since 2014/08/03
 */
public abstract class BaseDecoderInput
	implements Closeable
{
	/** Internal input lock. */
	protected final Object ilock;
	
	/**
	 * Initializes the lock.
	 *
	 * @since 2014/08/04
	 */
	protected BaseDecoderInput()
	{
		this.ilock = new Object();
	}
	
	/**
	 * Closes the input.
	 *
	 * @throws JsonException If it cannot be closed.
	 * @since 2014/08/03
	 */
	@Override
	public abstract void close();
	
	/**
	 * Reads some input token which could be from some varying kind of
	 * input.
	 *
	 * @return The raw type of token just read, {@code null} if there is
	 * nothing left.
	 * @throws JsonException Any internal reading errors possibly.
	 * @throws JsonParsingException On any parser errors.
	 * @since 2014/08/03
	 */
	protected abstract BaseDecoderData next();
	
	/**
	 * Returns the current JSON location.
	 *
	 * @return The current location.
	 * @since 2014/08/04
	 */
	public JsonLocation getLocation()
	{
		// Replaced by sub-classes if they support this.
		return new SomeLocation();
	}
	
}
