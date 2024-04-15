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
import java.io.Closeable;
import java.io.IOException;

/**
 * This class provides a stream based reading of JSON data, it reads the input
 * in terms of events. This is used similar to a {@link java.util.Iterator} in
 * that you use {@link #hasNext()} and {@link #next()} to obtain the next
 * processed bits. This does not form any automatically structured objects or
 * arrays.
 *
 * @since 2014/07/25
 */
@Api
public interface JsonParser
	extends Closeable
{
	/**
	 * Closes the parser and releases resources associated with it, the input
	 * source is also closed.
	 *
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @since 2014/07/25
	 */
	@Override
	void close();
	
	/**
	 * When the event is {@link Event#VALUE_NUMBER}, this returns the integer
	 * value of it as if {@code new BigDecimal(getString()).intValue()} were
	 * called. It is possible that information may be lost.
	 *
	 * @return The integer value.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/07/25
	 */
	@Api
	int getInt();
	
	/**
	 * Returns information on the parser's current state within the JSON
	 * stream, said information is only valid in the current state. If the
	 * state is modified, the values here are invalid.
	 *
	 * @return A non-{@code null} location matching the current parser state.
	 * @since 2014/07/25
	 */
	@Api
	JsonLocation getLocation();
	
	/**
	 * When the event is {@link Event#VALUE_NUMBER}, this returns the long
	 * value of it as if {@code new BigDecimal(getString()).longValue()} were
	 * called. It is possible that information may be lost.
	 *
	 * @return The long value.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/07/25
	 */
	@Api
	long getLong();
	
	/**
	 * Returns either the key name or a value if the parser is in the 
	 * {@link Event#VALUE_NUMBER}, {@link Event#VALUE_STRING}, or
	 * {@link Event#KEY_NAME} states.
	 *
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}, {@link Event#VALUE_STRING}, or
	 * {@link Event#KEY_NAME}.
	 * @since 2014/07/25
	 */
	@Api
	String getString();
	
	/**
	 * Returns {@code true} if there are more states to parse, otherwise
	 * {@code false} will be returned at the end.
	 *
	 * @throws JsonException A standard error, {@link IOException} may be
	 * wrapped if it occurs.
	 * @throws JsonParsingException Invalid JSON data was detected.
	 * @since 2014/07/25
	 */
	@Api
	boolean hasNext();
	
	/**
	 * Returns true if the specified numeric value is an integer (that is, it
	 * has no fractional values).
	 *
	 * @return {@code true} if this number is integral.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/07/25
	 */
	@Api
	boolean isIntegralNumber();
	
	/**
	 * Advances to the next state returning the type of data read.
	 *
	 * @throws JsonException A standard error, {@link IOException} may be
	 * wrapped if it occurs.
	 * @throws JsonParsingException Invalid JSON data was detected.
	 * @throws java.util.NoSuchElementException If there is nothing left.
	 * @since 2014/07/25
	 */
	@Api
	Event next();
	
	/**
	 * This is an enumeration which represents the type of the fragment which
	 * was just read.
	 *
	 * @since 2014/07/25
	 */
	@Api
	enum Event
	{
		/** The start of an array, the parser sits after the '['. */
		@Api
		START_ARRAY,
		
		/** The start of an object, the parser sits after the '{'. */
		@Api
		START_OBJECT,
		
		/**
		 * The name of an object key, the parser sits after the name,
		 * to obtain the key use {@link JsonParser#getString()}.
		 */
		@Api
		KEY_NAME,
		
		/**
		 * The value of an object (a string), the parser sits after the value,
		 * to obtain the value use {@link JsonParser#getString()}.
		 */
		@Api
		VALUE_STRING,
		
		/**
		 * The value of an object (a number), the parser sits after the value,
		 * to obtain the value use {@link JsonParser#getInt()} or
		 * {@link JsonParser#getLong()}.
		 */
		@Api
		VALUE_NUMBER,
		
		/** The value is {@code true}, the parser sits after the literal. */
		@Api
		VALUE_TRUE,
		
		/** The value is {@code false}, the parser sits after the literal. */
		@Api
		VALUE_FALSE,
		
		/** The value is {@code null}, the parser sits after the literal. */
		@Api
		VALUE_NULL,
		
		/** End of an object, the parser sits after the '}'. */
		@Api
		END_OBJECT,
		
		/** End of an array, the parser sits after the ']'. */
		@Api
		END_ARRAY,
		
		/** End. */
		;
	}
}

