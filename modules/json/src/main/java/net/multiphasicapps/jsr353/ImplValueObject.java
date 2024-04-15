// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.oracle.json.JsonArray;
import com.oracle.json.JsonNumber;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonString;
import com.oracle.json.JsonValue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This represents an object which contains many key/value pairs.
 *
 * @since 2014/08/05
 */
public class ImplValueObject
	extends AbstractMap<String, JsonValue>
	implements JsonObject
{
	/** Internal map storage, immutable. */
	private final Map<String, JsonValue> _im;
	
	/**
	 * Initializes the object with the specified value map, values are copied
	 * in the same order that they are declared in.
	 *
	 * @param __v Base valued map to add from, in origin order.
	 * @since 2014/08/05
	 */
	public ImplValueObject(Map<String, JsonValue> __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException(
				"No input map specified.");
		
		// Lock on map, might not be worth it
		synchronized (__v)
		{
			// Copy map values in the same order
			Map<String, JsonValue> m = new LinkedHashMap<String, JsonValue>();
			for (Map.Entry<String, JsonValue> e : __v.entrySet())
				m.put(e.getKey(), e.getValue());
			
			// Set map
			this._im = UnmodifiableMap.of(m);
		}
	}
	
	/**
	 * Returns the set of entries contained within this map.
	 *
	 * @return A set of entries contained in the map.
	 * @since 2014/08/05
	 */
	@Override
	public Set<Map.Entry<String, JsonValue>> entrySet()
	{
		return this._im.entrySet();
	}
	
	/**
	 * Returns the type of value this is.
	 *
	 * @return The type of value that this is.
	 * @since 2014/08/05
	 */
	@Override
	public ValueType getValueType()
	{
		return ValueType.OBJECT;
	}
	
	/**
	 * Returns the string representation of this value.
	 *
	 * @return The value of this object represented as a string.
	 * @since 2014/08/05
	 */
	@SuppressWarnings("StringOperationCanBeSimplified")
	@Override
	public String toString()
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 OutputStreamWriter writer = new OutputStreamWriter(baos))
		{
			new ImplWriter(writer, false).write(this);
			writer.flush();
			
			return new String(baos.toByteArray(), "utf-8");
		}
		catch (IOException e)
		{
			throw Debugging.oops(e);
		}
	}
	
	/**
	 * Returns the associated boolean value with the specified key.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The specified value.
	 * @throws NullPointerException If the name has no mapping.
	 * @throws ClassCastException If the value is not a boolean.
	 * @since 2014/08/05
	 */
	@Override
	public boolean getBoolean(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Find in map
		JsonValue bv = this.get(__n);
		
		// True
		if (bv == JsonValue.TRUE)
			return true;
		
		// False
		else if (bv == JsonValue.FALSE)
			return false;
		
		// No mapping
		else if (bv == null)
			throw new NullPointerException(
				"No mapping for this key.");
		
		// Error
		else
			throw new ClassCastException(
				"Not a boolean value.");
	}
	
	/**
	 * Returns the associated boolean value with the specified key, if this
	 * is not a boolean value then {@code __def} is returned.
	 *
	 * @param __n The key name to obtain the value for.
	 * @param __def The value to return if this is not a boolean.
	 * @return The specified value.
	 * @since 2014/08/05
	 */
	@Override
	public boolean getBoolean(String __n, boolean __def)
	{
		// Return boolean value
		try
		{
			return this.getBoolean(__n);
		}
		
		// Invalid
		catch (Exception e)
		{
			return __def;
		}
	}
	
	/**
	 * Invokes {@code getJsonNumber(__n).intValue()}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The specified value.
	 * @throws NullPointerException If the name has no mapping.
	 * @throws ClassCastException If the value is not an int.
	 * @since 2014/08/05
	 */
	@Override
	public int getInt(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Call
		return this.getJsonNumber(__n).intValue();
	}
	
	/**
	 * Invokes {@code getJsonNumber(__n).intValue()}, if the value is not an
	 * int then {@code __def} is returned.
	 *
	 * @param __n The key name to obtain the value for.
	 * @param __def The value to return if this is not an int.
	 * @return The specified value.
	 * @since 2014/08/05
	 */
	@Override
	public int getInt(String __n, int __def)
	{
		// Obtain number
		try
		{
			return this.getInt(__n);
		}
		
		// Failed
		catch (Exception e)
		{
			return __def;
		}
	}
	
	/**
	 * Invokes {@code (JsonArray)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The array value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have an array value.
	 * @since 2014/08/05
	 */
	@Override
	public JsonArray getJsonArray(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Get
		return (JsonArray)this.get(__n);
	}
	
	/**
	 * Invokes {@code (JsonNumber)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The number value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have a number value.
	 * @since 2014/08/05
	 */
	@Override
	public JsonNumber getJsonNumber(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Get
		return (JsonNumber)this.get(__n);
	}
	
	/**
	 * Invokes {@code (JsonObject)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The object value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have an object value.
	 * @since 2014/08/05
	 */
	@Override
	public JsonObject getJsonObject(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Get
		return (JsonObject)this.get(__n);
	}
	
	/**
	 * Invokes {@code (JsonString)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The string value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have a string value.
	 * @since 2014/08/05
	 */
	@Override
	public JsonString getJsonString(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Get
		return (JsonString)this.get(__n);
	}
	
	/**
	 * Invokes {@code getJsonString(__n).getString()}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The string value.
	 * @throws NullPointerException If the name has no mapping.
	 * @throws ClassCastException If the value is not castable to
	 * {@link JsonString}.
	 * @since 2014/08/05
	 */
	@Override
	public String getString(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Call
		return this.getJsonString(__n).getString();
	}
	
	/**
	 * Invokes {@code getJsonString(__n).getString()}, if that fails then the
	 * value of {@code __def} is returned instead.
	 *
	 * @return The string value, or {@code __def}.
	 * @param __n The key name to obtain the value for.
	 * @param __def The value to return if this is not a string.
	 * @since 2014/08/05
	 */
	@Override
	public String getString(String __n, String __def)
	{
		// Try
		try
		{
			return this.getString(__n);
		}
		
		// Failed
		catch (Exception e)
		{
			return __def;
		}
	}
	
	/**
	 * Returns {@code true} if the specified value is {@link JsonValue#NULL}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return {@code true} if this is the null literal.
	 * @throws NullPointerException If the name has no mapping.
	 * @since 2014/08/05
	 */
	@Override
	public boolean isNull(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException(
				"No key specified.");
		
		// Get
		JsonValue jv = this.get(__n);
		if (jv == null)
			throw new NullPointerException(
				"Key has no assigned value.");
		
		// Check
		return (jv == JsonValue.NULL);
	}
}

