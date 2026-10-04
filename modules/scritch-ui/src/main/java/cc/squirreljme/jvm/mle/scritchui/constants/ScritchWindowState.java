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
 * The state that a window may be in.
 *
 * Not all ScritchUI implementations may support specific window states,
 * additionally ScritchUI may implement some states in software if the
 * core implementation does not support it natively.
 *
 * @since 2026/07/06
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ScritchWindowState
{
	/** Window is "restored" to its default state. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte RESTORED =
		0;
	
	/** Window is minimized */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MINIMIZED =
		1;
	
	/** Window is maximized horizontally. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MAXIMIZED_HORIZ =
		2;
	
	/** Window is maximized vertically. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MAXIMIZED_VERT =
		3;
	
	/** Window is maximized both horizontally and vertically. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MAXIMIZED_BOTH =
		4;
	
	/** Window is shaded, only the title bar is visible. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SHADED =
		5;
	
	/**
	 * Window is fullscreen.
	 *
	 * Note that this does not imply in any way that the window is undecorated
	 * and/or borderless. Window managers that support native
	 * fullscreen for applications may provide access to an autohidden
	 * title bar and/or menu through a screen edge or mnemonic, as such
	 * this should not be used with the window
	 * flag {@link ScritchWindowFlag#UNDECORATED}.
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FULLSCREEN =
		6;
	
	/** The number of valid window states. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_STATES =
		7;
}
