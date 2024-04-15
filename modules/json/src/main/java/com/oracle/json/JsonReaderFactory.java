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
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * This is a factory which is able to create {@link JsonReader}s, it is
 * possible for it to be configured.
 *
 * @since 2014/07/25
 */
@Api
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface JsonReaderFactory
{
	/**
	 * Creates a JSON reader from the specified stream, the character encoding
	 * is determined as described in RFC 4627, the factory configuration is
	 * used.
	 *
	 * @param __i Stream to read from.
	 * @return A JSON reader.
	 * @since 2014/07/25
	 */
	@Api
	JsonReader createReader(InputStream __i);
	
	/**
	 * Creates a JSON reader from the specified stream using the specified
	 * character encoding, the factory configuration is used.
	 *
	 * @param __i Stream to read from.
	 * @param __cs Character encoding of the stream.
	 * @return A JSON reader.
	 * @throws UnsupportedEncodingException If the character set is not
	 * supported.
	 * @since 2014/07/25
	 */
	@Api
	JsonReader createReader(InputStream __i, String __cs)
		throws UnsupportedEncodingException;

	/**
	 * Creates a JSON reader from the specified stream, the factory
	 * configuration is used.
	 *
	 * @param __r Stream to read from.
	 * @return A JSON reader.
	 * @since 2014/07/25
	 */
	@Api
	JsonReader createReader(Reader __r);

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

