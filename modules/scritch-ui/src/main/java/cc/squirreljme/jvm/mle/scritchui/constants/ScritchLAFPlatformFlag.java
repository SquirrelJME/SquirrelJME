// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

/**
 * Platform flags which define how a ScritchUI interface operates on a
 * given platform.
 *
 * @since 2025/05/15
 */
public interface ScritchLAFPlatformFlag
{
	/** Dark mode is enabled. */
	byte DARK_MODE =
		1;

	/** The number pad follows the calculator layout. */
	byte NUMPAD_CALC_LAYOUT =
		2;

	/** Panel only interface. */
	byte PANEL_ONLY =
		4;

	/** Are native alerts available? */
	byte HAS_ALERTS =
		8;
}
