// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Platform flags which define how a ScritchUI interface operates on a
 * given platform.
 *
 * @since 2025/05/15
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ScritchLAFPlatformFlag
{
	/** Dark mode is enabled. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DARK_MODE =
		1;

	/** The number pad follows the calculator layout. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMPAD_CALC_LAYOUT =
		2;

	/** Panel only interface. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PANEL_ONLY =
		4;

	/** Are native alerts available? */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HAS_ALERTS =
		8;
}
