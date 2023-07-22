// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json.stream;

import cc.squirreljme.runtime.cldc.annotation.Api;
import com.oracle.json.JsonException;
import com.oracle.json.JsonValue;
import java.io.Closeable;
import java.io.IOException;

/**
 * This is a generator which writes JSON data in a stream like form in that
 * most methods return {@code this}.
 *
 * @since 2014/07/25
 */
@Api
public interface JsonGenerator
	extends Closeable
{
	/** Make the generated output nice looking, this must be supported. */
	@Api
	String PRETTY_PRINTING =
		"javax.json.stream.JsonGenerator.prettyPrinting";
	
	/**
	 * Closes this generator and releases any used resources, the underlying
	 * output stream is also closed.
	 *
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If incomplete Json is generated.
	 * @since 2014/07/25
	 */
	@Override
	void close();
	
	/**
	 * Flushes the underlying output stream, if any buffered output is in the
	 * generator it is written to the stream before being flushed.
	 *
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @since 2014/07/25
	 */
	@Api
	void flush();
	
	/**
	 * Writes a boolean value to the output, it writes either {@code true} or
	 * {@code false}.
	 *
	 * @param __v Value to add.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(boolean __v);
	
	/**
	 * Writes the specified number to the array, invokes
	 * {@code BigDecimal.valueOf(__v).toString()}.
	 *
	 * @param __v Value to add.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(double __v);
	
	/**
	 * Writes the specified number to the array, invokes
	 * {@code new BigDecimal(__v).toString()}.
	 *
	 * @param __v Value to add.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(int __v);
	
	/**
	 * Writes the specified value to the array.
	 *
	 * @param __v Value to add.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(JsonValue __v);
	
	/**
	 * Writes the specified number to the array, invokes
	 * {@code new BigDecimal(__v).toString()}.
	 *
	 * @param __v Value to add.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(long __v);
	
	/**
	 * Writes the specified string to the array.
	 *
	 * @param __v Value to add.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __v);
	
	/**
	 * Writes the specified name and value to the object, it either writes
	 * {@code true} or {@code false}.
	 *
	 * @param __n The name of the key in the pair.
	 * @param __v The value of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __n, boolean __v);
	
	/**
	 * Writes the specified name and value to the object, it invokes
	 * {@code BigDecimal.valueOf(__v).toString()}.
	 *
	 * @param __n The name of the key in the pair.
	 * @param __v The value of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __n, double __v);
	
	/**
	 * Writes the specified name and value to the object, it invokes
	 * {@code new BigDecimal(__v).toString()}.
	 *
	 * @param __n The name of the key in the pair.
	 * @param __v The value of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __n, int __v);
	
	/**
	 * Writes the specified name and value to the object.
	 *
	 * @param __n The name of the key in the pair.
	 * @param __v The value of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __n, JsonValue __v);
	
	/**
	 * Writes the specified name and value to the object, it invokes
	 * {@code new BigDecimal(__v).toString()}.
	 *
	 * @param __n The name of the key in the pair.
	 * @param __v The value of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __n, long __v);
	
	/**
	 * Writes the specified name and value to the object.
	 *
	 * @param __n The name of the key in the pair.
	 * @param __v The value of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator write(String __n, String __v);
	
	/**
	 * Writes the end of the current context; if it is an array ']'
	 * is written; if it is an object '}' is written; once the context is
	 * ended, it is switched to the parent context.
	 *
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If there is no context.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeEnd();
	
	/**
	 * Writes the {@code null} value to the array.
	 *
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an array.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeNull();
	
	/**
	 * Writes the {@code null} value with the specified key name.
	 *
	 * @param __n The name of the key in the pair.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If the current context is not an object.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeNull(String __n);
	
	/**
	 * Writes the character representing the start of an array, it is only
	 * valid in an array context or when there is no context (when there is
	 * nothing). This may only be called once in no context mode. After it
	 * is written, the context switches to the newly created array.
	 *
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If this is not with an array context,
	 * or is called more than once when in no context.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeStartArray();
	
	/**
	 * Writes the character representing the start of an array while in the
	 * object context. After it is written, the context switches to the newly
	 * created array.
	 *
	 * @param __n The key name of the value.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If this is not within an object context.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeStartArray(String __n);
	
	/**
	 * Writes the character which represents an object for writing key name
	 * and value pairs; this may only be called in an array context or only
	 * once when in no context; after it is written the context switches to it.
	 *
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If this is not with an array context,
	 * or is called more than once when in no context.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeStartObject();
	
	/**
	 * Writes the character which represents an object for writing key name
	 * and value pairs, after it is written the context switches to it.
	 *
	 * @param __n The key name of the value.
	 * @return {@code this}.
	 * @throws JsonException If an {@link IOException} occurs it will be
	 * wrapped in this exception.
	 * @throws JsonGenerationException If this is not within an object context.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator writeStartObject(String __n);
}

