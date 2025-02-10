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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

/**
 * This is a factory which creates {@link JsonGenerator} instances, it is
 * possible for the factory to be configured.
 *
 * All methods here should be thread safe.
 *
 * @since 2014/07/25
 */
@Api
public interface JsonGeneratorFactory
{
	/**
	 * Creates a generator which writes JSON to the specified stream using the
	 * current factory configuration, the bytes are encoded in UTF-8.
	 *
	 * @param __o Stream to write to.
	 * @return A new generator.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator createGenerator(OutputStream __o);
	
	/**
	 * Creates a generator which writes JSON to the specified stream using the
	 * current factory configuration, the bytes are encoded in the specified
	 * character set.
	 *
	 * @param __o Stream to write to.
	 * @param __charSet Character encoding to write as.
	 * @return A new generator.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator createGenerator(OutputStream __o, String __charSet)
		throws UnsupportedEncodingException;
	
	/**
	 * Creates a generator which writes JSON to the specified stream using the
	 * current factory configuration.
	 *
	 * @param __w Stream to write to.
	 * @return A new generator.
	 * @since 2014/07/25
	 */
	@Api
	JsonGenerator createGenerator(Writer __w);
	
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

