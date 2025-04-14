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
import com.oracle.json.JsonNumber;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonObjectBuilder;
import com.oracle.json.JsonString;
import com.oracle.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * This class builds arrays.
 *
 * @since 2014/08/02
 */
public class ImplArrayBuilder
	extends SomeBuilder
	implements JsonArrayBuilder
{
	/** Array builder. */
	private List<JsonValue> _order;
	
	/**
	 * Initializes a new object builder.
	 *
	 * @since 2014/08/05
	 */
	public ImplArrayBuilder()
	{
		
		// Order init
		this._order = new ArrayList<JsonValue>();
	}
	
	/**
	 * Adds {@link JsonValue#TRUE} or {@link JsonValue#FALSE}.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(boolean __v)
	{
		synchronized (this.lock)
		{
			return this.add((__v ? JsonValue.TRUE : JsonValue.FALSE));
		}
	}
	
	/**
	 * Adds a {@link JsonNumber}.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @throws NumberFormatException If {@code __v} is an infinity or NaN.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(double __v)
	{
		synchronized (this.lock)
		{
			return this.add(new ImplValueNumber(__v));
		}
	}
	
	/**
	 * Adds a {@link JsonNumber}.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(int __v)
	{
		synchronized (this.lock)
		{
			return this.add(new ImplValueNumber(__v));
		}
	}
	
	/**
	 * Adds a {@link JsonArray} from the specified builder.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @throws NullPointerException If {@code __v} is null.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(JsonArrayBuilder __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException("No array builder specified.");
		
		// Build
		synchronized (this.lock)
		{
			return this.add(__v.build());
		}
	}
	
	/**
	 * Adds a {@link JsonObject} from the specified builder.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @throws NullPointerException If {@code __v} is null.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(JsonObjectBuilder __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException("No object builder specified.");
		
		// Build
		synchronized (this.lock)
		{
			return this.add(__v.build());
		}
	}
	
	/**
	 * Adds a value to the array.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @throws NullPointerException If {@code __v} is null.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(JsonValue __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException("No value specified.");
		
		// Locked
		synchronized (this.lock)
		{
			// add and return this
			this._order.add(__v);
			return this;
		}
	}
	
	/**
	 * Adds a {@link JsonNumber}.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(long __v)
	{
		synchronized (this.lock)
		{
			return this.add(new ImplValueNumber(__v));
		}
	}
	
	/**
	 * Adds a {@link JsonString}.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @throws NullPointerException If {@code __v} is null.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder add(String __v)
	{
		synchronized (this.lock)
		{
			return this.add(new ImplValueString(__v));
		}
	}
	
	/**
	 * Adds {@link JsonValue#NULL} to the array.
	 *
	 * @return {@code this}.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArrayBuilder addNull()
	{
		synchronized (this.lock)
		{
			return this.add(JsonValue.NULL);
		}
	}
	
	/**
	 * Returns the current array as a {@link JsonArray}.
	 *
	 * @return A freshly constructed array.
	 * @since 2014/08/02
	 */
	@Override
	public JsonArray build()
	{
		synchronized (this.lock)
		{
			return new ImplValueArray(UnmodifiableList.of(this._order));
		}
	}
}

