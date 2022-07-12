// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * This is a factory which is able to create {@link JsonReader}s, it is
 * possible for it to be configured.
 *
 * @since 2014/07/25
 */
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
	JsonReader createReader(InputStream __i);
	
	/**
	 * Creates a JSON reader from the specified stream using the specified
	 * character encoding, the factory configuration is used.
	 *
	 * @param __i Stream to read from.
	 * @param __cs Character encoding of the stream.
	 * @return A JSON reader.
	 * @since 2014/07/25
	 */
	JsonReader createReader(InputStream __i, Charset __cs);

	/**
	 * Creates a JSON reader from the specified stream, the factory
	 * configuration is used.
	 *
	 * @param __r Stream to read from.
	 * @return A JSON reader.
	 * @since 2014/07/25
	 */
	JsonReader createReader(Reader __r);

	/**
	 * Returns a read only map of the current configuration options in use, any
	 * unsupported options will not be included in the map.
	 *
	 * @return A read-only map of the current configuration, the map may be
	 * empty but must never be {@code null}.
	 * @since 2014/07/25
	 */
	Map<String, ?> getConfigInUse();
}

