// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json.stream;

import cc.squirreljme.runtime.cldc.annotation.Api;
import com.oracle.json.JsonException;

/**
 * Indicates that invalid JSON is being parsed.
 *
 * @since 2014/07/25
 */
@Api
public class JsonParsingException
	extends JsonException
{
	/** Serialized version ID. */
	private static final long serialVersionUID = 0xBAD5EE1A715A7100L;
	
	/** Where the parsing error was encountered. */
	private final JsonLocation _loc;	
	
	/**
	 * Initializes with the specified message and location, however with no
	 * cause.
	 *
	 * @param __m Message to set.
	 * @param __l Location of the parsing error.
	 * @since 2014/07/25
	 */
	@Api
	public JsonParsingException(String __m, JsonLocation __l)
	{
		super(__m);
		
		// Set location
		this._loc = (__l == null ? new BlankLocation() : __l);
	}
	
	/**
	 * Initializes with the specified message, location, and cause.
	 *
	 * @param __m Message to set.
	 * @param __c Cause of the exception.
	 * @param __l Location of the parsing error.
	 * @since 2014/07/25
	 */
	@Api
	public JsonParsingException(String __m, Throwable __c, JsonLocation __l)
	{
		super(__m, __c);
		
		// Set location
		this._loc = (__l == null ? new BlankLocation() : __l);
	}
	
	/**
	 * Returns the location of the position within the stream where the parse
	 * error was encountered.
	 *
	 * @return The location of the parse error, is never {@code null}.
	 * @since 2014/07/25
	 */
	@Api
	public JsonLocation getLocation()
	{
		return this._loc;
	}
	
	/**
	 * This is a blank location for when one has not been specified.
	 *
	 * @since 2015/06/07
	 */
	@Api
	static class BlankLocation
		implements JsonLocation
	{
		/**
		 * {@inheritDoc}
		 * @since 2015/06/07
		 */
		@Override
		public long getColumnNumber()
		{
			return -1L;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2015/06/07
		 */
		@Override
		public long getLineNumber()
		{
			return -1L;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2015/06/07
		 */
		@Override
		public long getStreamOffset()
		{
			return -1L;
		}
	}
}

