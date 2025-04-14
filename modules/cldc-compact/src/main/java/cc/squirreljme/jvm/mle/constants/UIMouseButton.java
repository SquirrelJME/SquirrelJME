// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Represents the mouse button that was used.
 *
 * @since 2020/07/19
 */
@SquirrelJMEVendorApi
public interface UIMouseButton
{
	/** Primary mouse button. */
	@SquirrelJMEVendorApi
	byte PRIMARY_BUTTON =
		0;
	
	/** Secondary mouse button. */
	@SquirrelJMEVendorApi
	byte SECONDARY_BUTTON =
		1;
	
	/** The number of buttons. */
	@SquirrelJMEVendorApi
	byte NUM_BUTTONS =
		2;
}
