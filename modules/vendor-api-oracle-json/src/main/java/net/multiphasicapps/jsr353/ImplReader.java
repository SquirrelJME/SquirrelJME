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
import com.oracle.json.JsonArrayBuilder;
import com.oracle.json.JsonException;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonObjectBuilder;
import com.oracle.json.JsonReader;
import com.oracle.json.JsonStructure;
import com.oracle.json.JsonValue;
import com.oracle.json.stream.JsonLocation;
import com.oracle.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * This read a JSON Array or JSON Object from the specified input stream, this
 * class is for reading already formed JSON into actual objects.
 *
 * @since 2014/08/01
 */
public class ImplReader
	extends BaseDecoder
	implements JsonReader
{
	/** Closed? */
	private volatile boolean _closed;
	
	/** Did this already? */
	private volatile boolean _did;
	
	/** Builder stack. */
	private List<SomeBuilder> _bs;
	
	/** Key stack (needed for arrays and such). */
	private List<String> _ks;
	
	/**
	 * Reads an array or object from the specified stream.
	 *
	 * @param __r Stream to read single object from.
	 * @since 2014/08/01
	 */
	public ImplReader(Reader __r)
	{
		super(new ReaderInput(__r));
		
		// Local work variables
		this._bs = new LinkedList<SomeBuilder>();
		this._ks = new LinkedList<String>();
	}
	
	/**
	 * Closes the reader and releases resources associated with it, the input
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
			// Already closed
			if (this._closed)
				return;
			
			// Close
			super.close();
		
			// Clear working vars
			this._closed = true;
			this._bs = null;
			this._ks = null;
		}
	}
	
	/**
	 * Reads an object or an array, this only needs to be called once.
	 *
	 * @return An object or an array.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws JsonParsingException The input JSON is incorrect.
	 * @since 2014/08/01
	 */
	@Override
	public JsonStructure read()
	{
		synchronized (this.lock)
		{
			// Already closed
			if (this._closed)
				throw new JsonException(
					"Stream has been closed.");
			
			// Already read a value?
			if (this._did)
				throw new JsonException(
					"Data has already been read.");
			this._did = true;
			
			// Read bits constantly
			JsonStructure rv = null;
			for (;;)
			{
				// Get the next bit and the kind
				JsonLocation jl = this.input.getLocation();
				BaseDecoder.Bit b = this.nextBit();
				if (b == null)	// Done
					break;
				BaseDecoder.Bit.Kind k = b.getKind();
				
				// Push new object to stack (a builder)
				if (k == BaseDecoder.Bit.Kind.PUSH_OBJECT)
				{
					this._bs.add(0, new ImplObjectBuilder());
					continue;
				}
				
				// Push an array to the stack
				else if (k == BaseDecoder.Bit.Kind.PUSH_ARRAY)
				{
					this._bs.add(0, new ImplArrayBuilder());
					continue;
				}
				
				// End of object
				else if (k == BaseDecoder.Bit.Kind.FINISHED_OBJECT)
				{
					// Return built object
					rv = ((JsonObjectBuilder)this._bs.remove(0)).build();
					break;
				}
				
				// End of array
				else if (k == BaseDecoder.Bit.Kind.FINISHED_ARRAY)
				{
					// Return built array
					rv = ((JsonArrayBuilder)this._bs.remove(0)).build();
					break;
				}
				
				// Declare a new key and put into the stack
				else if (k == BaseDecoder.Bit.Kind.DECLARE_KEY)
				{
					this._ks.add(0, (String)b.get(0));
					continue;
				}
				
				// Add key/value pair to the thing.
				else if ((k == BaseDecoder.Bit.Kind.ADD_OBJECT_KEYVAL) ||
					(k == BaseDecoder.Bit.Kind.POP_ARRAY_ADD_OBJECT_KEYVAL) ||
					(k == BaseDecoder.Bit.Kind.POP_OBJECT_ADD_OBJECT_KEYVAL))
				{
					// Obtain value
					JsonValue v = null;
					if (k == BaseDecoder.Bit.Kind.POP_ARRAY_ADD_OBJECT_KEYVAL)
						v = ((JsonArrayBuilder)this._bs.remove(0)).build();
					else if (k == BaseDecoder.Bit.Kind.
						POP_OBJECT_ADD_OBJECT_KEYVAL)
						v = ((JsonObjectBuilder)this._bs.remove(0)).build();
					else if (k == BaseDecoder.Bit.Kind.ADD_OBJECT_KEYVAL)
						v = (JsonValue)b.get(0);
					
					// Add to current object
					JsonObjectBuilder ob = (JsonObjectBuilder)this._bs.get(0);
					
					// Add key and value
					ob.add(this._ks.get(0), v);
					
					// Drop key not needed
					this._ks.remove(0);
					
					// Keep going
					continue;
				}
				
				// Add value to array
				else if ((k == BaseDecoder.Bit.Kind.ADD_ARRAY_VALUE) ||
					(k == BaseDecoder.Bit.Kind.POP_OBJECT_ADD_ARRAY) ||
					(k == BaseDecoder.Bit.Kind.POP_ARRAY_ADD_ARRAY))
				{
					// Obtain value
					JsonValue v = null;
					if (k == BaseDecoder.Bit.Kind.ADD_ARRAY_VALUE)
						v = (JsonValue)b.get(0);
					else if (k == BaseDecoder.Bit.Kind.POP_OBJECT_ADD_ARRAY)
						v = ((JsonObjectBuilder)this._bs.remove(0)).build();
					else if (k == BaseDecoder.Bit.Kind.POP_ARRAY_ADD_ARRAY)
						v = ((JsonArrayBuilder)this._bs.remove(0)).build();
					
					// Add to current object
					JsonArrayBuilder ob = (JsonArrayBuilder)this._bs.get(0);
					
					// Add value
					ob.add(v);
					
					// Keep going
					continue;
				}
				
				// Unknown
				else
					throw new JsonParsingException(String.format(
						"Unhandled kind action %1$s.", k), jl);
			}
			
			// Not read (maybe not finished)
			if (rv == null)
				throw new JsonParsingException(String.format(
					"No value has been read."), new SomeLocation());
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads an array, this only needs to be called once.
	 *
	 * @return An array.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws JsonParsingException The input JSON is incorrect.
	 * @since 2014/08/01
	 */
	@Override
	public JsonArray readArray()
	{
		synchronized (this.lock)
		{
			// Read an array only
			JsonStructure js = this.read();
			if (js != null && js instanceof JsonArray && js.getValueType() ==
				JsonValue.ValueType.ARRAY)
				return (JsonArray)js;
			throw new JsonException(
				"An array was not read.");
		}
	}
	
	/**
	 * Reads an object, this only needs to be called once.
	 *
	 * @return An object.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws JsonParsingException The input JSON is incorrect.
	 * @since 2014/08/01
	 */
	@Override
	public JsonObject readObject()
	{
		synchronized (this.lock)
		{
			// Read an object only
			JsonStructure js = this.read();
			if (js != null && js instanceof JsonObject && js.getValueType() ==
				JsonValue.ValueType.OBJECT)
				return (JsonObject)js;
			throw new JsonException(
				"An object was not read.");
		}
	}
}

