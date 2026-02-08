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
 * URI which has a path.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public interface UriPartPath
{
	/**
	 * Returns the path component of this URI.
	 *
	 * @return The path component or {@code null} if there is none.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	String getPath();
	
	/**
	 * Is this a directory? This is so if this ends in a slash.
	 *
	 * @return If this ends in a slash.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	boolean isDirectory();
}
