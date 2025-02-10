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
import com.oracle.json.stream.JsonParsingException;
import java.io.Closeable;
import java.io.IOException;

/**
 * Reads a JSON object or array from an input source, this class is for reading
 * an entire single chunks of JSON data.
 *
 * @since 2014/07/25
 */
@Api
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface JsonReader
	extends Closeable
{
	/**
	 * Closes the reader and releases resources associated with it, the input
	 * source is also closed.
	 *
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @since 2014/07/25
	 */
	@Override
	void close();
	
	/**
	 * Reads an object or an array, this only needs to be called once.
	 *
	 * @return An object or an array.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws JsonParsingException The input JSON is incorrect.
	 * @since 2014/07/25
	 */
	@Api
	JsonStructure read();
	
	/**
	 * Reads an array, this only needs to be called once.
	 *
	 * @return An array.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws JsonParsingException The input JSON is incorrect.
	 * @since 2014/07/25
	 */
	@Api
	JsonArray readArray();
	
	/**
	 * Reads an object, this only needs to be called once.
	 *
	 * @return An object.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws JsonParsingException The input JSON is incorrect.
	 * @since 2014/07/25
	 */
	@Api
	JsonObject readObject();
}

