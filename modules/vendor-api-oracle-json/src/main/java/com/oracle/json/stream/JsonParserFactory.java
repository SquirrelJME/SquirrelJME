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
import com.oracle.json.JsonArray;
import com.oracle.json.JsonException;
import com.oracle.json.JsonObject;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @since 2014/07/25
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface JsonParserFactory
{
	/**
	 * Creates a JSON parser to parse the specified stream, the character
	 * encoding is specified by RFC 4627, the factory configuration is used.
	 *
	 * @param __i Stream to read data from.
	 * @return A parser for JSON.
	 * @throws JsonException If the encoding could not be specified.
	 * @since 2014/07/25
	 */
	@Api
	JsonParser createParser(InputStream __i);
	
	/**
	 * Creates a JSON parser to parse the specified stream with the specified
	 * encoding, the factory configuration is used.
	 *
	 * @param __i Stream to read data from.
	 * @param __charSet Character encoding of the data.
	 * @return A parser for JSON.
	 * @since 2014/07/25
	 */
	@Api
	JsonParser createParser(InputStream __i, String __charSet)
		throws UnsupportedEncodingException;
	
	/**
	 * Creates a JSON parser to parse the specified array, the factory
	 * configuration is used.
	 *
	 * @param __a The array to read data from.
	 * @return A parser for JSON.
	 * @since 2014/07/25
	 */
	@Api
	JsonParser createParser(JsonArray __a);
	
	/**
	 * Creats a JSON parser to parse the specified object, the factory
	 * configuration is used..
	 *
	 * @param __o The object to read data from.
	 * @return A parser for JSON.
	 * @since 2014/07/25
	 */
	@Api
	JsonParser createParser(JsonObject __o);
	
	/**
	 * Creates a JSON parser to parse the specified input reader, the factory
	 * configuration is used.
	 *
	 * @param __r Reader to obtain the data from.
	 * @return A parser for JSON.
	 * @since 2014/07/25
	 */
	@Api
	JsonParser createParser(Reader __r);
	
	/**
	 * Returns a read only map of the current configuration options in use, any
	 * unsupported options will not be included in the map.
	 *
	 * @return A read-only map of the current configuration, the map may be
	 * empty but must never be {@code null}.
	 * @since 2014/07/25
	 */
	@Api
	Map<String, ?> getConfigInUse();
}

