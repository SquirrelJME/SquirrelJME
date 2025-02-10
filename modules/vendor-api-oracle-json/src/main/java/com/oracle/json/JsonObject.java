// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.util.Map;

/**
 * This represents an immutable Json object value, essentially key and value
 * pairs. It also provides a map view, however it cannot be modified.
 *
 * If any attempts to modify the map are made, then
 * {@link UnsupportedOperationException} is thrown.
 *
 * The map iterator order depends on the order of appearence when using a
 * builder or when parsing an input file.
 *
 * @since 2014/07/25
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface JsonObject
	extends JsonStructure, Map<String, JsonValue>
{
	/**
	 * Returns the associated boolean value with the specified key.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The specified value.
	 * @throws NullPointerException If the name has no mapping.
	 * @throws ClassCastException If the value is not a boolean.
	 * @since 2014/07/25
	 */
	@Api
	boolean getBoolean(String __n);
	
	/**
	 * Returns the associated boolean value with the specified key, if this
	 * is not a boolean value then {@code __def} is returned.
	 *
	 * @param __n The key name to obtain the value for.
	 * @param __def The value to return if this is not a boolean.
	 * @return The specified value.
	 * @since 2014/07/25
	 */
	@Api
	boolean getBoolean(String __n, boolean __def);
	
	/**
	 * Invokes {@code getJsonNumber(__n).intValue()}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The specified value.
	 * @throws NullPointerException If the name has no mapping.
	 * @throws ClassCastException If the value is not an int.
	 * @since 2014/07/25
	 */
	@Api
	int getInt(String __n);
	
	/**
	 * Invokes {@code getJsonNumber(__n).intValue()}, if the value is not an
	 * int then {@code __def} is returned.
	 *
	 * @param __n The key name to obtain the value for.
	 * @param __def The value to return if this is not an int.
	 * @return The specified value.
	 * @since 2014/07/25
	 */
	@Api
	int getInt(String __n, int __def);
	
	/**
	 * Invokes {@code (JsonArray)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The array value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have an array value.
	 * @since 2014/07/25
	 */
	@Api
	JsonArray getJsonArray(String __n);
	
	/**
	 * Invokes {@code (JsonNumber)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The number value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have a number value.
	 * @since 2014/07/25
	 */
	@Api
	JsonNumber getJsonNumber(String __n);
	
	/**
	 * Invokes {@code (JsonObject)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The object value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have an object value.
	 * @since 2014/07/25
	 */
	@Api
	JsonObject getJsonObject(String __n);
	
	/**
	 * Invokes {@code (JsonString)get(__n)}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The string value or {@code null} if the key was not found.
	 * @throws ClassCastException If the key does not have a string value.
	 * @since 2014/07/25
	 */
	@Api
	JsonString getJsonString(String __n);
	
	/**
	 * Invokes {@code getJsonString(__n).getString()}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return The string value.
	 * @throws NullPointerException If the name has no mapping.
	 * @throws ClassCastException If the value is not castable to
	 * {@link JsonString}.
	 * @since 2014/07/25
	 */
	@Api
	String getString(String __n);
	
	/**
	 * Invokes {@code getJsonString(__n).getString()}, if that fails then the
	 * value of {@code __def} is returned instead.
	 *
	 * @return The string value, or {@code __def}.
	 * @param __n The key name to obtain the value for.
	 * @param __def The value to return if this is not a string.
	 * @since 2014/07/25
	 */
	@Api
	String getString(String __n, String __def);
	
	/**
	 * Returns {@code true} if the specified value is {@link JsonValue#NULL}.
	 *
	 * @param __n The key name to obtain the value for.
	 * @return {@code true} if this is the null literal.
	 * @throws NullPointerException If the name has no mapping.
	 * @since 2014/07/25
	 */
	@Api
	boolean isNull(String __n);
}

