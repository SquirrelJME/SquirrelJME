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
 * URI has a scheme specific part.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public interface UriPartSchemeSpecific
{
	/**
	 * Returns the scheme specific part.
	 *
	 * @return The scheme specific part.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	String getSchemeSpecific();
}
