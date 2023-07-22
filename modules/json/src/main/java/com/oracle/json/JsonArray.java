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
import java.util.List;

/**
 * This class represents an immutable JSON array which is also able to be used
 * as an unmodifiable list structure. This object can be created by reading in
 * JSON data or building this from scratch with {@link JsonArrayBuilder}.
 *
 * @since 2014/07/25
 */
@Api
public interface JsonArray
	extends JsonStructure, List<JsonValue>
{
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
	 * @since 2014/07/25
	 */
	@Api
	boolean getBoolean(int __i);
	
	/**
	 * This is similar to {@link #getBoolean(int)} except that if the value is
	 * not a boolean, it returns {@code __def}.
	 *
	 * @param __i Index into the array to look up.
	 * @param __def Default value if this is not a boolean.
	 * @return The specified boolean value or {@code __def}.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/07/25
	 */
	@Api
	boolean getBoolean(int __i, boolean __def);
	
	/**
	 * Invokes {@code getJsonNumber(__i).intValue()}.
	 *
	 * @param __i Index into the array to look up.
	 * @return The integer value.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonNumber}.
	 * @since 2014/07/25
	 */
	@Api
	int getInt(int __i);
	
	/**
	 * Similar to {@link #getInt(int)} except that if the value is not a
	 * number, {@code __def} is returned.
	 *
	 * @param __i Index into the array to look up.
	 * @param __def Default value, if not an integer.
	 * @return The integer value or {@code __def}.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/07/25
	 */
	@Api
	int getInt(int __i, int __def);
	
	/**
	 * Invokes {@code (JsonArray)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonArray}.
	 * @since 2014/07/25
	 */
	@Api
	JsonArray getArray(int __i);
	
	/**
	 * Invokes {@code (JsonNumber)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonNumber}.
	 * @since 2014/07/25
	 */
	@Api
	JsonNumber getNumber(int __i);
	
	/**
	 * Invokes {@code (JsonObject)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonObject}.
	 * @since 2014/07/25
	 */
	@Api
	JsonObject getObject(int __i);
	
	/**
	 * Invokes {@code (JsonString)get(__i)}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonString}.
	 * @since 2014/07/25
	 */
	@Api
	JsonString getJsonString(int __i);
	
	/**
	 * Invokes {@code getJsonString(__i).getString()}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @throws ClassCastException If the value is not assignable to
	 * {@link JsonString}.
	 * @since 2014/07/25
	 */
	@Api
	String getString(int __i);
	
	/**
	 * Similar to {@link #getString(int)} except that if the value is not a
	 * string, {@code __def} is returned.
	 *
	 * @param __i Index into the array to look up.
	 * @param __def Default value, if not a string.
	 * @return The string value or {@code __def}.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/07/25
	 */
	@Api
	String getString(int __i, String __def);
	
	/**
	 * Returns an unmodifiable view of the specified array, no checking is
	 * performed therefor it is possible that {@link ClassCastException} may
	 * be thrown when the list is used.
	 *
	 * @param <T> Type to view the list as.
	 * @param __cl The class value for T.
	 * @return {@link List} view of the specified array.
	 * @since 2014/07/25
	 */
	@Api
	<T extends JsonValue> List<T> getValueAs(Class<T> __cl);
	
	/**
	 * Returns {@code true} if the specified index is a {@link JsonValue#NULL}.
	 *
	 * @param __i Index into the array to look up.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2014/07/25
	 */
	@Api
	boolean isNull(int __i);
}

