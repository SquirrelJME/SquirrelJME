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
 * The state that a window may be in.
 *
 * Not all ScritchUI implementations may support specific window states,
 * additionally ScritchUI may implement some states in software if the
 * core implementation does not support it natively.
 *
 * @since 2026/07/06
 */
@SquirrelJMEVendorApi
public interface ScritchWindowState
{
	/** Window is "restored" to its default state. */
	@SquirrelJMEVendorApi
	byte RESTORED =
		0;
	
	/** Window is minimized */
	@SquirrelJMEVendorApi
	byte MINIMIZED =
		1;
	
	/** Window is maximized horizontally. */
	@SquirrelJMEVendorApi
	byte MAXIMIZED_HORIZ =
		2;
	
	/** Window is maximized vertically. */
	@SquirrelJMEVendorApi
	byte MAXIMIZED_VERT =
		3;
	
	/** Window is maximized both horizontally and vertically. */
	@SquirrelJMEVendorApi
	byte MAXIMIZED_BOTH =
		4;
	
	/** Window is shaded, only the title bar is visible. */
	@SquirrelJMEVendorApi
	byte SHADED =
		5;
	
	/** Window is fullscreen. */
	@SquirrelJMEVendorApi
	byte FULLSCREEN =
		6;
	
	/** The number of valid window states. */
	@SquirrelJMEVendorApi
	byte NUM_STATES =
		7;
}
