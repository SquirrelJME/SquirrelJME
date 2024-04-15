// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json.spi;

import cc.squirreljme.runtime.cldc.annotation.Api;
import com.oracle.json.JsonArray;
import com.oracle.json.JsonArrayBuilder;
import com.oracle.json.JsonBuilderFactory;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonObjectBuilder;
import com.oracle.json.JsonReader;
import com.oracle.json.JsonReaderFactory;
import com.oracle.json.JsonWriter;
import com.oracle.json.JsonWriterFactory;
import com.oracle.json.stream.JsonGenerator;
import com.oracle.json.stream.JsonGeneratorFactory;
import com.oracle.json.stream.JsonParser;
import com.oracle.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * This provides a service which is able to allow processing of JSON data.
 *
 * All methods are to be thread safe and useable by multiple threads.
 *
 * {@link ServiceLoader} is used to register implementations and thus use the
 * file {@code META-INF/services/com.oracle.spi.JsonProvider} in your JAR to
 * make your implementation visible.
 *
 * @since 2014/07/26
 */
@Api
public abstract class JsonProvider
{
	/** Default provider for JSR353. */
	private static final String DEFAULTPROVIDER =
		"net.multiphasicapps.jsr353.ImplProvider";	
	
	/** Cache. */
	private static JsonProvider _jpc;
	
	/**
	 * Base initialization.
	 *
	 * @since 2014/07/26
	 */
	@Api
	protected JsonProvider()
	{
	}
	
	/**
	 * Creates a new array builder.
	 *
	 * @return A new array builder.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonArrayBuilder createArrayBuilder();
	
	/**
	 * Creates a builder factory which is able to generate
	 * {@link JsonArrayBuilder} and {@link JsonObjectBuilder} with the
	 * specified options passed to the factory.
	 *
	 * Any unsupported options should be ignored.
	 *
	 * @param __cnf A map of settings, may be {@code null}.
	 * @return A new builder factory.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonBuilderFactory createBuilderFactory(
		Map<String, ?> __cnf);
	
	/**
	 * Creates a JSON generator which writes to the specified stream.
	 *
	 * @param __o Stream to write the JSON data to.
	 * @return A new generator.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonGenerator createGenerator(OutputStream __o);
	
	/**
	 * Creates a JSON generator which writes to the specified stream.
	 *
	 * @param __w Stream to write JSON data to.
	 * @return A new generator.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonGenerator createGenerator(Writer __w);
	
	/**
	 * Creates a factory which is able to generate {@link JsonGenerator}
	 * objects.
	 *
	 * Any unsupported options should be ignored.
	 *
	 * @param __cnf Configuration options for the factory, may be {@code null}.
	 * @return A new generator factory.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonGeneratorFactory createGeneratorFactory(
		Map<String, ?> __cnf);
	
	/**
	 * Creates a new JSON object builder.
	 *
	 * @return A new builder.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonObjectBuilder createObjectBuilder();
	
	/**
	 * Creates a parser which is capable of reading JSON from the specified
	 * stream.
	 *
	 * @param __i Stream to parse JSON data from.
	 * @return A new parser.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonParser createParser(InputStream __i);
	
	/**
	 * Creates a parser which is capable of reading JSON from the specified
	 * stream.
	 *
	 * @param __r Stream to parse JSON data from.
	 * @return A new parser.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonParser createParser(Reader __r);
	
	/**
	 * Creates a new factory which is capable of creating {@link JsonParser}
	 * with the specified options.
	 *
	 * Unknown options should be ignored.
	 *
	 * @param __cnf Configuration settings for the factory, may be
	 * {@code null}.
	 * @return A new factory for creating parsers.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonParserFactory createParserFactory(
		Map<String, ?> __cnf);
	
	/**
	 * Creates a JSON reader from the specified byte stream which is encoded
	 * in RFC 4627 form.
	 *
	 * @param __i Stream to read JSON data from, encoded as RFC 4627.
	 * @return A new JSON reader.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonReader createReader(InputStream __i);
	
	/**
	 * Creates a JSON reader from the specified stream.
	 *
	 * @param __r Stream to read JSON data from.
	 * @return A new JSON reader.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonReader createReader(Reader __r);
	
	/**
	 * Creates a factory for creating {@link JsonReader}s with the specified
	 * options.
	 *
	 * Unsupported options should be ignored.
	 *
	 * @param __cnf Configuration settings for the reader, may be {@code null}.
	 * @return A new JSON reader factory.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonReaderFactory createReaderFactory(
		Map<String, ?> __cnf);
	
	/**
	 * Creates a JSON writer to write {@link JsonObject} and {@link JsonArray}
	 * to the specified stream, the bytes are encoded as UTF-8.
	 *
	 * @param __o Output stream to write JSON data to, encoded as UTF-8.
	 * @return A new JSON writer.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonWriter createWriter(OutputStream __o);
	
	/**
	 * Creates a JSON writer to write {@link JsonObject} and {@link JsonArray}
	 * to the specified stream.
	 *
	 * @param __w Stream to write JSON data to.
	 * @return A new JSON writer.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonWriter createWriter(Writer __w);
	
	/**
	 * Creates a new factory for generating {@code JsonWriter}s with the
	 * specified options.
	 *
	 * Unknown settings should be ignored.
	 *
	 * @param __cnf Configuration settings, may be {@code null}.
	 * @return A new factory for creating JSON writers.
	 * @since 2014/07/26
	 */
	@Api
	public abstract JsonWriterFactory createWriterFactory(
		Map<String, ?> __cnf);
	
	/**
	 * Uses the {@link ServiceLoader} to search for an implementation, if none
	 * is found the default is used.
	 *
	 * @return A provider for the JSON implementation.
	 * @since 2014/07/26
	 */
	@Api
	public static JsonProvider provider()
	{
		synchronized (JsonProvider.class)
		{
			// Cache it if possible
			if (JsonProvider._jpc == null)
			{
				// Attempt service load
				ServiceLoader<JsonProvider> sl =
					ServiceLoader.<JsonProvider>load(JsonProvider.class);
		
				// Go through services and return one
				if (sl != null)
					for (Iterator<JsonProvider> it = sl.iterator();;)
						try
						{
							if ((JsonProvider._jpc = it.next()) != null)
								return JsonProvider._jpc;
						}
						
						// none left
						catch (NoSuchElementException nsee)
						{
							break;
						}
		
				// Failed use default service
				try
				{
					Class<?> cl = Class.forName(JsonProvider.DEFAULTPROVIDER);
					Object o = cl.newInstance();
					if (!(o instanceof JsonProvider))
						throw new RuntimeException("Default JsonProvider " +
							"is not a JsonProvider");
					JsonProvider._jpc = (JsonProvider)o;
				}
				
				// Oops
				catch (ClassNotFoundException cnfe)
				{
					throw new RuntimeException("Could not find default " +
						"JsonProvider.");
				}
				
				// Could not new
				catch (InstantiationException ie)
				{
					throw new RuntimeException("Default JsonProvider could " +
						"not be initialized.");
				}
				
				// Not accessable
				catch (IllegalAccessException iae)
				{
					throw new RuntimeException("Default JsonProvider is " +
						"not accessable (probably not public).");
				}
			}
			
			// Return cached result
			return JsonProvider._jpc;
		}
	}
}

