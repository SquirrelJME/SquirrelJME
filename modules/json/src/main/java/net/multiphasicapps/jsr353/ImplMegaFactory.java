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
import com.oracle.json.JsonArrayBuilder;
import com.oracle.json.JsonBuilderFactory;
import com.oracle.json.JsonException;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This class provides all the factory-ish methods combined into one since they
 * are all very quite similar in function, such as using a map. 
 *
 * @since 2014/08/01
 */
public class ImplMegaFactory
	implements JsonBuilderFactory, JsonReaderFactory, JsonWriterFactory,
	JsonGeneratorFactory, JsonParserFactory
{
	/** Internal option map. */
	private final Map<String, ?> _iopts;
	
	/** Neat JSON output. */
	private boolean _neat;
	
	/**
	 * Initializes the mega factory with the specified option set.
	 *
	 * @param __ops Option map.
	 * @since 2014/08/01
	 */
	public ImplMegaFactory(Map<String, ?> __ops)
	{
		// By default not neat
		this._neat = false;
		
		// Init map and parse options
		this._iopts = new HashMap<String, Object>();
		if (__ops != null)
		{
			Set<Map.Entry<String, ?>> vmes = Unchecked.<Set<?>,
				Set<Map.Entry<String, ?>>>cast(__ops.entrySet());
			for (Map.Entry<String, ?> e : vmes)
			{
				String k = e.getKey();
				
				// Pretty printing enabled?
				if (k.equals(JsonGenerator.PRETTY_PRINTING))
					this._neat = true;
			}
		}	
	}
	
	/**
	 * Returns a read only map of the current configuration options in use, any
	 * unsupported options will not be included in the map.
	 *
	 * @return A read-only map of the current configuration, the map may be
	 * empty but must never be {@code null}.
	 * @since 2014/08/01
	 */
	@Override
	public final Map<String, ?> getConfigInUse()
	{
		return UnmodifiableMap.of(this._iopts);
	}
	
	/**
	 * Creates a {@link JsonArrayBuilder} which builds {@link JsonArray},
	 * the factory configuration is used.
	 *
	 * @return A new array builder.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonArrayBuilder createArrayBuilder()
	{
		return new ImplArrayBuilder();
	}
	
	/**
	 * Creates a {@link JsonObjectBuilder} which builds {@link JsonObject},
	 * the factory configuration is used.
	 *
	 * @return A new object builder.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonObjectBuilder createObjectBuilder()
	{
		return new ImplObjectBuilder();
	}
	
	/**
	 * Creates a JSON reader from the specified stream, the character encoding
	 * is determined as described in RFC 4627, the factory configuration is
	 * used.
	 *
	 * @param __i Stream to read from.
	 * @return A JSON reader.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonReader createReader(InputStream __i)
	{
		// Cannot be null
		if (__i == null)
			throw new NullPointerException("No input stream specified.");
		
		// Return auto-detection stream, sort of
		return this.createReader(new UTFDetectISR(__i));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/01
	 */
	@Override
	public final JsonReader createReader(InputStream __i, String __cs)
		throws UnsupportedEncodingException
	{
		// Cannot be null
		if (__i == null || __cs == null)
			throw new NullPointerException(String.format(
				"Null arguments: %1$s %2$s.",
				(__i == null ? "__i" : ""), (__cs == null ? "__cs" : "")));
		
		// Return in a stream
		return this.createReader(new InputStreamReader(__i, __cs));
	}

	/**
	 * Creates a JSON reader from the specified stream, the factory
	 * configuration is used.
	 *
	 * @param __r Stream to read from.
	 * @return A JSON reader.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonReader createReader(Reader __r)
	{
		// Cannot be null
		if (__r == null)
			throw new NullPointerException("No reader specified.");
		
		// Create reader
		return new ImplReader(__r);
	}

	/**
	 * Creates a writer to output JSON to the specified stream, the character
	 * encoding uses UTF-8, the specified configuration is used.
	 *
	 * @param __o Stream to write data to.
	 * @return A {@link JsonWriter}.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonWriter createWriter(OutputStream __o)
	{
		try
		{
			return this.createWriter(__o, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw Debugging.oops(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/01
	 */
	@Override
	public final JsonWriter createWriter(OutputStream __o, String __charSet)
		throws UnsupportedEncodingException
	{
		return this.createWriter(new OutputStreamWriter(__o, __charSet));
	}
	
	/**
	 * Creates a writer to output JSON to the specified stream, the specified
	 * configuration is used.
	 *
	 * @param __w Stream to write data to.
	 * @return A {@link JsonWriter}.
	 * @since 2014/08/01
	 */
	@Override
	public JsonWriter createWriter(Writer __w)
	{
		return new ImplWriter(__w, this._neat);
	}

	/**
	 * Creates a generator which writes JSON to the specified stream using the
	 * current factory configuration, the bytes are encoded in UTF-8.
	 *
	 * @param __o Stream to write to.
	 * @return A new generator.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonGenerator createGenerator(OutputStream __o)
	{
		try
		{
			return this.createGenerator(__o, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw Debugging.oops(e);
		}
	}
	
	/**
	 * Creates a generator which writes JSON to the specified stream using the
	 * current factory configuration, the bytes are encoded in the specified
	 * character set.
	 *
	 * @param __o Stream to write to.
	 * @param __charSet Character encoding to write as.
	 * @return A new generator.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonGenerator createGenerator(OutputStream __o,
		String __charSet)
		throws UnsupportedEncodingException
	{
		return this.createGenerator(new OutputStreamWriter(__o, __charSet));
	}
	
	/**
	 * Creates a generator which writes JSON to the specified stream using the
	 * current factory configuration.
	 *
	 * @param __w Stream to write to.
	 * @return A new generator.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonGenerator createGenerator(Writer __w)
	{
		return new ImplGenerator(__w, this._neat);
	}
	
	/**
	 * Creates a JSON parser to parse the specified stream, the character
	 * encoding is specified by RFC 4627, the factory configuration is used.
	 *
	 * @param __i Stream to read data from.
	 * @return A parser for JSON.
	 * @throws JsonException If the encoding could not be specified.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonParser createParser(InputStream __i)
	{
		// Cannot be null
		if (__i == null)
			throw new NullPointerException("No input stream specified.");
		
		// Return auto-detection stream, sort of
		return this.createParser(new UTFDetectISR(__i));
	}
	
	/**
	 * Creates a JSON parser to parse the specified stream with the specified
	 * encoding, the factory configuration is used.
	 *
	 * @param __i Stream to read data from.
	 * @param __charSet Character encoding of the data.
	 * @return A parser for JSON.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonParser createParser(InputStream __i, String __charSet)
		throws UnsupportedEncodingException
	{
		// Cannot be null
		if (__i == null || __charSet == null)
			throw new NullPointerException(String.format(
				"Null arguments: %1$s %2$s.",
				(__i == null ? "__i" : ""), (__charSet == null ? "__cs" : "")));
		
		// Return in a stream
		return this.createParser(new InputStreamReader(__i, __charSet));
	}
	
	/**
	 * Creates a JSON parser to parse the specified array, the factory
	 * configuration is used.
	 *
	 * @param __a The array to read data from.
	 * @return A parser for JSON.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonParser createParser(JsonArray __a)
	{
		// Cannot be null
		if (__a == null)
			throw new NullPointerException("No array specified.");
		
		// Parser for it
		return new ImplParser(new ValueInput(__a));
	}
	
	/**
	 * Creats a JSON parser to parse the specified object, the factory
	 * configuration is used..
	 *
	 * @param __o The object to read data from.
	 * @return A parser for JSON.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonParser createParser(JsonObject __o)
	{
		// Cannot be null
		if (__o == null)
			throw new NullPointerException("No object specified.");
		
		// Parser for it
		return new ImplParser(new ValueInput(__o));
	}
	
	/**
	 * Creates a JSON parser to parse the specified input reader, the factory
	 * configuration is used.
	 *
	 * @param __r Reader to obtain the data from.
	 * @return A parser for JSON.
	 * @since 2014/08/01
	 */
	@Override
	public final JsonParser createParser(Reader __r)
	{
		// Cannot be null
		if (__r == null)
			throw new NullPointerException("No input stream specified.");
		
		// Create
		return new ImplParser(__r);
	}
}

