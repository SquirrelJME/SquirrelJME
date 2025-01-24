// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This represents the state of the display hardware.
 *
 * @since 2017/10/01
 */
@SquirrelJMEVendorApi
public enum DisplayHardwareState
{
	/** Enabled. */
	@SquirrelJMEVendorApi
	ENABLED,
	
	/** Disabled. */
	@SquirrelJMEVendorApi
	DISABLED,
	
	/** Removed. */
	@SquirrelJMEVendorApi
	ABSENT,
	
	/** End. */
	;
	
	/**
	 * Is this forced to be disabled?
	 *
	 * @return If this is forced to be disabled.
	 * @since 2017/10/01
	 */
	@SquirrelJMEVendorApi
	public final boolean forceDisabled()
	{
		return this == DisplayHardwareState.DISABLED ||
			this == DisplayHardwareState.ABSENT;
	}
}

