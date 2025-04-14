// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Represents the type of window manager used by Scritch.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchWindowManagerType
{
	/** One frame per screen. */
	@SquirrelJMEVendorApi
	byte ONE_FRAME_PER_SCREEN = 
		0;
	
	/** Standard desktop interface. */
	@SquirrelJMEVendorApi
	byte STANDARD_DESKTOP =
		1;
	
	/** The number of display types. */
	@SquirrelJMEVendorApi
	byte NUM_TYPES =
		2;
}
