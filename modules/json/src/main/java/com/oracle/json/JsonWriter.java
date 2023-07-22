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
import java.io.Closeable;
import java.io.IOException;

/**
 * This class writes a {@link JsonArray} or {@link JsonObject} to an output
 * stream, it is possible for this to be configured by a factory.
 *
 * All methods must only be called once.
 *
 * @since 2014/07/26
 */
@Api
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface JsonWriter
	extends Closeable
{
	/**
	 * Closes the JSON writer and releases any resources associated with it,
	 * this also closes the underlying stream.
	 *
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @since 2014/07/26
	 */
	@Override
	void close();
	
	/**
	 * Writes a {@link JsonArray} or {@link JsonObject} to the output stream,
	 * this may only be called once.
	 *
	 * @param __v Value to write.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws IllegalStateException If any method in this interface was
	 * called previously.
	 * @since 2014/07/26
	 */
	@Api
	void write(JsonStructure __v);
	
	/**
	 * Writes an array to the output stream, this may only be called once.
	 *
	 * @param __v Value to write.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws IllegalStateException If any method in this interface was
	 * called previously.
	 * @since 2014/07/26
	 */
	@Api
	void writeArray(JsonArray __v);
	
	/**
	 * Writes an object to the output stream, this may only be called once.
	 *
	 * @param __v Value to write.
	 * @throws JsonException If an {@link IOException}, it will be wrapped,
	 * otherwise it is unspecified.
	 * @throws IllegalStateException If any method in this interface was
	 * called previously.
	 * @since 2014/07/26
	 */
	@Api
	void writeObject(JsonObject __v);
}

