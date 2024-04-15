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
import com.oracle.json.stream.JsonLocation;
import com.oracle.json.stream.JsonParser;
import com.oracle.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;

/**
 * This is an event based parser which decodes JSON input data.
 *
 * @since 2014/08/01
 */
public class ImplParser
	extends BaseDecoder
	implements JsonParser
{
	/** Current event. */
	private Event _e;
	
	/** Has been closed. */
	private boolean _closed;
	
	/**
	 * Reads JSON data from the specified stream.
	 *
	 * @param __r Stream to read from.
	 * @since 2014/08/01
	 */
	public ImplParser(Reader __r)
	{
		super(new ReaderInput(__r));
	}
	
	/**
	 * Reads JSON data from an input.
	 *
	 * @param __i Input mechanism to use.
	 * @since 2014/08/08
	 */
	public ImplParser(BaseDecoder.Input __i)
	{
		super(__i);
	}
	
	/**
	 * Closes the parser and releases resources associated with it, the input
	 * source is also closed.
	 *
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @since 2014/08/01
	 */
	@Override
	public void close()
	{
		synchronized (this.lock)
		{
			// Ignore if closed
			if (this._closed)
				return;	
			
			// Close and set super close (it does the work)
			this._closed = true;
			super.close();
		}
	}
	
	/**
	 * When the event is {@link Event#VALUE_NUMBER}, this returns the integer
	 * value of it as if {@code new BigDecimal(getString()).intValue()} were
	 * called. It is possible that information may be lost.
	 *
	 * @return The integer value.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/08/01
	 */
	@Override
	public int getInt()
	{
		return this.__getNumber().intValue();
	}
	
	/**
	 * Returns information on the parser's current state within the JSON
	 * stream, said information is only valid in the current state. If the
	 * state is modified, the values here are invalid.
	 *
	 * @return A non-{@code null} location matching the current parser state.
	 * @since 2014/08/01
	 */
	@Override
	public JsonLocation getLocation()
	{
		synchronized (this.lock)
		{
			// Cannot be closed
			if (this._closed)
				throw new IllegalStateException("Parser has been closed.");
			
			// Input has the potential location
			return this.input.getLocation();
		}
	}
	
	/**
	 * When the event is {@link Event#VALUE_NUMBER}, this returns the long
	 * value of it as if {@code new BigDecimal(getString()).longValue()} were
	 * called. It is possible that information may be lost.
	 *
	 * @return The long value.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/08/01
	 */
	@Override
	public long getLong()
	{
		return this.__getNumber().longValue();
	}
	
	/**
	 * Returns either the key name or a value if the parser is in the 
	 * {@link Event#VALUE_NUMBER}, {@link Event#VALUE_STRING}, or
	 * {@link Event#KEY_NAME} states.
	 *
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}, {@link Event#VALUE_STRING}, or
	 * {@link Event#KEY_NAME}.
	 * @since 2014/08/01
	 */
	@Override
	public String getString()
	{
		synchronized (this.lock)
		{
			// Cannot be closed
			if (this._closed)
				throw new IllegalStateException(
					"Parser has been closed.");
			
			// Invalid state
			if (this._e != Event.VALUE_NUMBER &&
				this._e != Event.VALUE_STRING && this._e != Event.KEY_NAME)
				throw new IllegalStateException(String.format(
					"Invalid state: %1$s.", this._e));
			
			throw new RuntimeException("TODO -- ImplParser::getString() " +
				"is not implemented.");
		}
	}
	
	/**
	 * Returns {@code true} if there are more states to parse, otherwise
	 * {@code false} will be returned at the end.
	 *
	 * @throws JsonException A standard error, {@link IOException} may be
	 * wrapped if it occurs.
	 * @throws JsonParsingException Invalid JSON data was detected.
	 * @since 2014/08/01
	 */
	@Override
	public boolean hasNext()
	{
		synchronized (this.lock)
		{
			// Cannot be closed
			if (this._closed)
				throw new IllegalStateException(
					"Parser has been closed.");
			
			// Check
			throw new RuntimeException("TODO -- ImplParser::hasNext() " +
				"is not implemented.");
		}
	}
	
	/**
	 * Returns true if the specified numeric value is an integer (that is, it
	 * has no fractional values).
	 *
	 * @return {@code true} if this number is integral.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/08/01
	 */
	@Override
	public boolean isIntegralNumber()
	{
		return new ImplValueNumber(this.__getNumber()).isIntegral();
	}
	
	/**
	 * Advances to the next state returning the type of data read.
	 *
	 * This relies on {@link #hasNext()} to obtain the next token to be read
	 * so that duplicate code is reduced.
	 *
	 * @throws JsonException A standard error, {@link IOException} may be
	 * wrapped if it occurs.
	 * @throws JsonParsingException Invalid JSON data was detected.
	 * @throws NoSuchElementException If there is nothing left.
	 * @since 2014/08/01
	 */
	@Override
	public Event next()
	{
		synchronized (this.lock)
		{
			// Cannot be closed
			if (this._closed)
				throw new IllegalStateException(
					"Parser has been closed.");
			
			throw new RuntimeException("TODO -- ImplParser::next() is not " +
				"implemented.");
		}
	}
	
	/**
	 * When the event is {@link Event#VALUE_NUMBER}, this returns the decimal
	 * value of it as if {@code new BigDecimal(getString())} were called.
	 *
	 * @return The decimal number the value is set to.
	 * @throws IllegalStateException If the state is not
	 * {@link Event#VALUE_NUMBER}.
	 * @since 2014/08/01
	 */
	private Number __getNumber()
	{
		synchronized (this.lock)
		{
			// Cannot be closed
			if (this._closed)
				throw new IllegalStateException("Parser has been closed.");
			
			// Invalid state
			if (this._e != Event.VALUE_NUMBER)
				throw new IllegalStateException(String.format(
					"Invalid state: %1$s.", this._e));
			
			return ImplValueNumber.__parseNumber(this.getString());
		}
	}
	
}

