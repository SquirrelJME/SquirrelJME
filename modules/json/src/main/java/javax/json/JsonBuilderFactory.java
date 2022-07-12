// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.json;

import java.util.Map;

/**
 * This factory creates {@link JsonObjectBuilder} and {@link JsonArrayBuilder}
 * instances. It is possible for a factory to be configured.
 *
 * All methods here should be thread safe.
 *
 * @since 2014/07/25
 */
public interface JsonBuilderFactory
{
	/**
	 * Creates a {@link JsonArrayBuilder} which builds {@link JsonArray},
	 * the factory configuration is used.
	 *
	 * @return A new array builder.
	 * @since 2014/07/25
	 */
	JsonArrayBuilder createArrayBuilder();
	
	/**
	 * Creates a {@link JsonObjectBuilder} which builds {@link JsonObject},
	 * the factory configuration is used.
	 *
	 * @return A new object builder.
	 * @since 2014/07/25
	 */
	JsonObjectBuilder createObjectBuilder();
	
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

