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
 * URI which has a path parameter.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public interface UriPartPathParameter
	extends UriPartPath
{
	/**
	 * Returns the given path parameter.
	 *
	 * @param __dx The index to get.
	 * @return The resultant path parameter.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	String pathParam(int __dx)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the number of path parameters.
	 *
	 * @return The path parameter count.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	int pathParamCount();
	
	/**
	 * Returns the decoded path parameters, with no field splitting.
	 *
	 * @return The path parameters.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	String pathParams();
}
