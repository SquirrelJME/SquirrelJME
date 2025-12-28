// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.uri;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * URI which has a query parameter.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public interface UriPartQueryParameter
{
	/**
	 * Returns the given query parameter.
	 *
	 * @param __dx The index to get.
	 * @return The resultant query parameter.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	String queryParam(int __dx)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the number of query parameters.
	 *
	 * @return The query parameter count.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	int queryParamCount();
}
