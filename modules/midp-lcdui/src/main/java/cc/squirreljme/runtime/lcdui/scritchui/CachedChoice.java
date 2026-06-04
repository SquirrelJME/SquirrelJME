// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a choice item that is cached.
 *
 * @since 2025/04/18
 */
@SquirrelJMEVendorApi
public final class CachedChoice
{
	/** The cached string. */
	@SquirrelJMEVendorApi
	public final String string;
	
	/**
	 * Initializes the cached choice.
	 *
	 * @param __string The string value.
	 * @since 2025/04/18
	 */
	@KeepWhenCompacting
	CachedChoice(String __string)
	{
		this.string = __string;
	}
	
	/**
	 * Returns the cached string. 
	 *
	 * @return The cached string.
	 * @since 2025/04/18
	 */
	public String getString()
	{
		return this.string;
	}
}
