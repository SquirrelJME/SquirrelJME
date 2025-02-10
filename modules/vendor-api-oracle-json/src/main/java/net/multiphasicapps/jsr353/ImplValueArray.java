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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * This is an array of values.
 *
 * @since 2014/08/05
 */
public class ImplValueArray
	extends AbstractList<JsonValue>
	implements JsonArray
{
	/** Internal list. */
	private final List<JsonValue> _il;	
	
	/**
	 * Initializes list of values from the base list.
	 *
	 * @param __b Base list to use.
	 * @since 2014/08/05
	 */
	public ImplValueArray(List<JsonValue> __b)
	{
		// Cannot be null
		if (__b == null)
			throw new NullPointerException(
				"No value list specified.");
		
		// Lock on that value, although it may not matter much
		synchronized (__b)
		{
			// Copy through them all, they are immutable always
			List<JsonValue> n = new ArrayList<JsonValue>(__b);
			
			// Set internal list as read-only
			this._il = UnmodifiableList.of(n); 
		}
	}
	
	/**
	 * Returns the element at the specified index.
	 *
	 * @param __i Index to obtain the value from.
	 * @throws IndexOutOfBoundsException If this is out of bounds.
	 * @return The element at this index.
	 * @since 2014/08/05
	 */
	@Override
	public JsonValue get(int __i)
	{
		return this._il.get(__i);
	}
	
	/**
	 * Returns the size of this array.
	 *
	 * @return The size of this array.
	 * @since 2014/08/05
	 */
	@Override
	public int size()
	{
		return this._il.size();
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
		return ValueType.ARRAY;
	}
	
	/**
	 * Returns the string representation of this value.
	 *
	 * @return The value of this array represented as a string.
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
	 * Returns the boolean value at the specified position, {@code true} is
	 * returned if it is {@link JsonValue#TRUE}, otherwise {@code false} is
	 * returned if it is {@link JsonValue#FALSE}.
	 *
	 * @param __i Index into the array to look up.
	 * @return The boolean value at the specified index.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the specific value is not
	 * {@link JsonValue#TRUE} or {@link JsonValue#FALSE}.
	 * @since 2014/08/05
	 */
	@Override
	public boolean getBoolean(int __i)
	{
		JsonValue bv = this.get(__i);
		
		// True
		if (bv == JsonValue.TRUE)
			return true;
		
		// False
		else if (bv == JsonValue.FALSE)
			return false;
		
		// Error
		else
			throw new ClassCastException(
				"Not a boolean value.");
	}
	
	/**
	 * This is similar to {@link #getBoolean(int)} except that if the value is
	 * not a boolean, it returns {@code __def}.
	 *
	 * @param __i Index into the array to look up.
	 * @param __def Default value if this is not a boolean.
	 * @return The specified boolean value or {@code __def}.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/08/05
	 */
	@Override
	public boolean getBoolean(int __i, boolean __def)
	{
		// Return boolean value
		try
		{
			return this.getBoolean(__i);
		}
		
		// Not a boolean
		catch (Exception e)
		{
			// Rethrow if out of bounds
			if (e instanceof IndexOutOfBoundsException)
				throw (IndexOutOfBoundsException)e;
			
			return __def;
		}
	}
	
	/**
	 * Invokes {@code getNumber(__i).intValue()}.
	 *
	 * @param __i Index into the array to look up.
	 * @return The integer value.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonNumber}.
	 * @since 2014/08/05
	 */
	@Override
	public int getInt(int __i)
	{
		return this.getNumber(__i).intValue();
	}
	
	/**
	 * Similar to {@link #getInt(int)} except that if the value is not a
	 * number, {@code __def} is returned.
	 *
	 * @param __i Index into the array to look up.
	 * @param __def Default value, if not an integer.
	 * @return The integer value or {@code __def}.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/08/05
	 */
	@Override
	public int getInt(int __i, int __def)
	{
		try
		{
			return this.getInt(__i);
		}
		
		// Return default
		catch (Exception e)
		{
			// Rethrow if out of bounds
			if (e instanceof IndexOutOfBoundsException)
				throw (IndexOutOfBoundsException)e;
			
			return __def;
		}
	}
	
	/**
	 * Invokes {@code (JsonArray)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonArray}.
	 * @since 2014/08/05
	 */
	@Override
	public JsonArray getArray(int __i)
	{
		return (JsonArray)this.get(__i);
	}
	
	/**
	 * Invokes {@code (JsonNumber)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonNumber}.
	 * @since 2014/08/05
	 */
	@Override
	public JsonNumber getNumber(int __i)
	{
		return (JsonNumber)this.get(__i);
	}
	
	/**
	 * Invokes {@code (JsonObject)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonObject}.
	 * @since 2014/08/05
	 */
	@Override
	public JsonObject getObject(int __i)
	{
		return (JsonObject)this.get(__i);
	}
	
	/**
	 * Invokes {@code (JsonString)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonString}.
	 * @since 2014/08/05
	 */
	@Override
	public JsonString getJsonString(int __i)
	{
		return (JsonString)this.get(__i);
	}
	
	/**
	 * Invokes {@code getJsonString(__i).getString()}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonString}.
	 * @since 2014/08/05
	 */
	@Override
	public String getString(int __i)
	{
		return this.getJsonString(__i).getString();
	}
	
	/**
	 * Similar to {@link #getString(int)} except that if the value is not a
	 * string, {@code __def} is returned.
	 *
	 * @param __i Index into the array to look up.
	 * @param __def Default value, if not a string.
	 * @return The string value or {@code __def}.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/08/05
	 */
	@Override
	public String getString(int __i, String __def)
	{
		// Obtain
		try
		{
			return this.getString(__i);
		}
		
		// Failed
		catch (Exception e)
		{
			// Rethrow if out of bounds
			if (e instanceof IndexOutOfBoundsException)
				throw (IndexOutOfBoundsException)e;
			
			return __def;
		}
	}
	
	/**
	 * Returns an unmodifiable view of the specified array, no checking is
	 * performed therefor it is possible that {@link ClassCastException} may
	 * be thrown when the list is used.
	 *
	 * @param <T> Type to view the list as.
	 * @param __cl The class value for T.
	 * @return {@link List} view of the specified array.
	 * @since 2014/08/05
	 */
	@Override
	public <T extends JsonValue> List<T> getValueAs(Class<T> __cl)
	{
		// Must extend JsonValue
		if (!JsonValue.class.isAssignableFrom(__cl))
			throw new ClassCastException(
				"Class does not extend JsonValue.");
		
		// Return it
		return Unchecked.<List<JsonValue>, List<T>>cast(
			UnmodifiableList.of(this));
	}
	
	/**
	 * Returns {@code true} if the specified index is a {@link JsonValue#NULL}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/08/05
	 */
	@Override
	public boolean isNull(int __i)
	{
		// Get and check
		JsonValue jv = this.get(__i);
		return (jv == JsonValue.NULL);
	}
}

