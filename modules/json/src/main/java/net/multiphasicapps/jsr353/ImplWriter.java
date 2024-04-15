// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonArray;
import com.oracle.json.JsonException;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonStructure;
import com.oracle.json.JsonWriter;
import com.oracle.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.Writer;

/**
 * This writes output JSON to the specified output writer, whatever it is.
 *
 * @since 2014/08/05
 */
public class ImplWriter
	implements JsonWriter
{
	/** Generator to use. */
	private final JsonGenerator _jg;
	
	/** Has been closed. */
	private boolean _closed;
	
	/** Lock. */
	private final Object _lock;
	
	/** Did already. */
	private volatile boolean _did;
	
	/**
	 * Initializes the writer implementation.
	 *
	 * @param __w Stream to write to.
	 * @param __pretty Whether it looks nice (the output text).
	 * @since 2014/08/05
	 */
	public ImplWriter(Writer __w, boolean __pretty)
	{
		this._jg = new ImplGenerator(__w, __pretty);
		this._lock = new Object();
	}
	
	/**
	 * Closes the JSON writer and releases any resources associated with it,
	 * this also closes the underlying stream.
	 *
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @since 2014/08/05
	 */
	@Override
	public void close()
	{
		synchronized (this._lock)
		{
			// Ignore if closed already
			if (this._closed)
				return;
			
			// Close stream
			this._closed = true;
			this._jg.close();
		}
	}
	
	/**
	 * Writes a {@link JsonArray} or {@link JsonObject} to the output stream,
	 * this may only be called once. This is recursive.
	 *
	 * @param __v Value to write.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws IllegalStateException If any method in this interface was
	 * called previously.
	 * @since 2014/08/05
	 */
	@Override
	public void write(JsonStructure __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException(
				"No object was specified to write.");
		
		// Lockde
		synchronized (this._lock)
		{
			// Already closed or did already
			if (this._closed || this._did)
				throw new IllegalStateException(
					"Stream was closed or already " +
					"performed an operation.");
			this._did = true;
			
			// Write object
			this._jg.write(__v);
		}
	}
	
	/**
	 * Writes an array to the output stream, this may only be called once.
	 *
	 * @param __v Value to write.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws IllegalStateException If any method in this interface was
	 * called previously.
	 * @since 2014/08/05
	 */
	@Override
	public void writeArray(JsonArray __v)
	{
		this.write(__v);
	}
	
	/**
	 * Writes an object to the output stream, this may only be called once.
	 *
	 * @param __v Value to write.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws IllegalStateException If any method in this interface was
	 * called previously.
	 * @since 2014/08/05
	 */
	@Override
	public void writeObject(JsonObject __v)
	{
		this.write(__v);
	}
}

