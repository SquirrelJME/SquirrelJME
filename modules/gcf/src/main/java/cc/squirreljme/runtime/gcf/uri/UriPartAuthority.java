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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Not Described.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public interface UriPartAuthority
{
	/**
	 * Returns the authority.
	 *
	 * @return The authority or {@code null} if there is none.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	String getAuthority();
}
