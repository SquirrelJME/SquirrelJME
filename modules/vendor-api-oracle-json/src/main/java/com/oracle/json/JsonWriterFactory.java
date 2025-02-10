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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

/**
 * This is a factory for creating {@link JsonWriter}s, it is possible to be
 * configured.
 *
 * @since 2014/07/26
 */
@Api
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface JsonWriterFactory
{
	/**
	 * Creates a writer to output JSON to the specified stream, the character
	 * encoding uses UTF-8, the specified configuration is used.
	 *
	 * @param __o Stream to write data to.
	 * @return A {@link JsonWriter}.
	 * @since 2014/07/26
	 */
	@Api
	JsonWriter createWriter(OutputStream __o);
	
	/**
	 * Creates a writer to output JSON to the specified stream, with the
	 * specified character encoding, the specified configuration is used.
	 *
	 * @param __o Stream to write data to.
	 * @param __charSet Character encoding to use.
	 * @return A {@link JsonWriter}.
	 * @throws UnsupportedEncodingException If the character set is not
	 * supported.
	 * @since 2014/07/26
	 */
	@Api
	JsonWriter createWriter(OutputStream __o, String __charSet)
		throws UnsupportedEncodingException;
	
	/**
	 * Creates a writer to output JSON to the specified stream, the specified
	 * configuration is used.
	 *
	 * @param __w Stream to write data to.
	 * @return A {@link JsonWriter}.
	 * @since 2014/07/26
	 */
	@Api
	JsonWriter createWriter(Writer __w);
	
	/**
	 * Returns a read only map of the current configuration options in use, any
	 * unsupported options will not be included in the map.
	 *
	 * @return A read-only map of the current configuration, the map may be
	 * empty but must never be {@code null}.
	 * @since 2014/07/26
	 */
	@Api
	Map<String, ?> getConfigInUse();
}

