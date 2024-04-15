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
import com.oracle.json.JsonBuilderFactory;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonObjectBuilder;
import com.oracle.json.JsonReader;
import com.oracle.json.JsonReaderFactory;
import com.oracle.json.JsonWriter;
import com.oracle.json.JsonWriterFactory;
import com.oracle.json.spi.JsonProvider;
import com.oracle.json.stream.JsonGenerator;
import com.oracle.json.stream.JsonGeneratorFactory;
import com.oracle.json.stream.JsonParser;
import com.oracle.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * This class provides an implementation of JSR353 which is for the handling
 * of JSON parsing and generation.
 *
 * @since 2014/08/01
 */
public class ImplProvider
	extends JsonProvider
{
	/**
	 * Creates a new array builder.
	 *
	 * @return A new array builder.
	 * @since 2014/08/01
	 */
	@Override
	public JsonArrayBuilder createArrayBuilder()
	{
		return this.createBuilderFactory(null).createArrayBuilder();
	}
	
	/**
	 * Creates a builder factory which is able to generate
	 * {@link JsonArrayBuilder} and {@link JsonObjectBuilder} with the
	 * specified options passed to the factory.
	 *
	 * Any unsupported options should be ignored.
	 *
	 * @param __cnf A map of settings, may be {@code null}.
	 * @return A new builder factory.
	 * @since 2014/08/01
	 */
	@Override
	public JsonBuilderFactory createBuilderFactory(
		Map<String, ?> __cnf)
	{
		return new ImplMegaFactory(__cnf);
	}
	
	/**
	 * Creates a JSON generator which writes to the specified stream.
	 *
	 * @param __o Stream to write the JSON data to.
	 * @return A new generator.
	 * @since 2014/08/01
	 */
	@Override
	public JsonGenerator createGenerator(OutputStream __o)
	{
		return this.createGeneratorFactory(null).createGenerator(__o);
	}
	
	/**
	 * Creates a JSON generator which writes to the specified stream.
	 *
	 * @param __w Stream to write JSON data to.
	 * @return A new generator.
	 * @since 2014/08/01
	 */
	@Override
	public JsonGenerator createGenerator(Writer __w)
	{
		return this.createGeneratorFactory(null).createGenerator(__w);
	}
	
	/**
	 * Creates a factory which is able to generate {@link JsonGenerator}
	 * objects.
	 *
	 * Any unsupported options should be ignored.
	 *
	 * @param __cnf Configuration options for the factory, may be {@code null}.
	 * @return A new generator factory.
	 * @since 2014/08/01
	 */
	@Override
	public JsonGeneratorFactory createGeneratorFactory(
		Map<String, ?> __cnf)
	{
		return new ImplMegaFactory(__cnf);
	}
	
	/**
	 * Creates a new JSON object builder.
	 *
	 * @return A new builder.
	 * @since 2014/08/01
	 */
	@Override
	public JsonObjectBuilder createObjectBuilder()
	{
		return this.createBuilderFactory(null).createObjectBuilder();
	}
	
	/**
	 * Creates a parser which is capable of reading JSON from the specified
	 * stream.
	 *
	 * @param __i Stream to parse JSON data from.
	 * @return A new parser.
	 * @since 2014/08/01
	 */
	@Override
	public JsonParser createParser(InputStream __i)
	{
		return this.createParserFactory(null).createParser(__i);
	}
	
	/**
	 * Creates a parser which is capable of reading JSON from the specified
	 * stream.
	 *
	 * @param __r Stream to parse JSON data from.
	 * @return A new parser.
	 * @since 2014/08/01
	 */
	@Override
	public JsonParser createParser(Reader __r)
	{
		return this.createParserFactory(null).createParser(__r);
	}
	
	/**
	 * Creates a new factory which is capable of creating {@link JsonParser}
	 * with the specified options.
	 *
	 * Unknown options should be ignored.
	 *
	 * @param __cnf Configuration settings for the factory, may be
	 * {@code null}.
	 * @return A new factory for creating parsers.
	 * @since 2014/08/01
	 */
	@Override
	public JsonParserFactory createParserFactory(
		Map<String, ?> __cnf)
	{
		return new ImplMegaFactory(__cnf);
	}
	
	/**
	 * Creates a JSON reader from the specified byte stream which is encoded
	 * in RFC 4627 form.
	 *
	 * @param __i Stream to read JSON data from, encoded as RFC 4627.
	 * @return A new JSON reader.
	 * @since 2014/08/01
	 */
	@Override
	public JsonReader createReader(InputStream __i)
	{
		return this.createReaderFactory(null).createReader(__i);
	}
	
	/**
	 * Creates a JSON reader from the specified stream.
	 *
	 * @param __r Stream to read JSON data from.
	 * @return A new JSON reader.
	 * @since 2014/08/01
	 */
	@Override
	public JsonReader createReader(Reader __r)
	{
		return this.createReaderFactory(null).createReader(__r);
	}
	
	/**
	 * Creates a factory for creating {@link JsonReader}s with the specified
	 * options.
	 *
	 * Unsupported options should be ignored.
	 *
	 * @param __cnf Configyration settings for the reader, may be {@code null}.
	 * @return A new JSON reader factory.
	 * @since 2014/08/01
	 */
	@Override
	public JsonReaderFactory createReaderFactory(
		Map<String, ?> __cnf)
	{
		return new ImplMegaFactory(__cnf);
	}
	
	/**
	 * Creates a JSON writer to write {@link JsonObject} and {@link JsonArray}
	 * to the specified stream, the bytes are encoded as UTF-8.
	 *
	 * @param __o Output stream to write JSON data to, encoded as UTF-8.
	 * @return A new JSON writer.
	 * @since 2014/08/01
	 */
	@Override
	public JsonWriter createWriter(OutputStream __o)
	{
		return this.createWriterFactory(null).createWriter(__o);
	}
	
	/**
	 * Creates a JSON writer to write {@link JsonObject} and {@link JsonArray}
	 * to the specified stream.
	 *
	 * @param __w Stream to write JSON data to.
	 * @return A new JSON writer.
	 * @since 2014/08/01
	 */
	@Override
	public JsonWriter createWriter(Writer __w)
	{
		return this.createWriterFactory(null).createWriter(__w);
	}
	
	/**
	 * Creates a new factory for generating {@code JsonWriter}s with the
	 * specified options.
	 *
	 * Unknown settings should be ignored.
	 *
	 * @param __cnf Configuration settings, may be {@code null}.
	 * @return A new factory for creating JSON writers.
	 * @since 2014/08/01
	 */
	@Override
	public JsonWriterFactory createWriterFactory(
		Map<String, ?> __cnf)
	{
		return new ImplMegaFactory(__cnf);
	}
}

