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
 * Platform flags which define how a ScritchUI interface operates on a
 * given platform.
 *
 * @since 2025/05/15
 */
@SquirrelJMEVendorApi
public interface ScritchLAFPlatformFlag
{
	/** Dark mode is enabled. */
	@SquirrelJMEVendorApi
	byte DARK_MODE =
		1;

	/** The number pad follows the calculator layout. */
	@SquirrelJMEVendorApi
	byte NUMPAD_CALC_LAYOUT =
		2;

	/** Panel only interface. */
	@SquirrelJMEVendorApi
	byte PANEL_ONLY =
		4;

	/** Are native alerts available? */
	@SquirrelJMEVendorApi
	byte HAS_ALERTS =
		8;
}
