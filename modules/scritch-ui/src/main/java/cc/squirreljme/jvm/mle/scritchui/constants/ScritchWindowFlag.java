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
 * Window flags which affect state and visibility.
 *
 * Not all ScritchUI implementations may support specific window flags,
 * additionally ScritchUI may implement some flags in software if the
 * core implementation does not support it natively.
 *
 * @since 2026/07/05
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ScritchWindowFlag
{
	/** Window does not appear in the task switcher. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_TASK_SWITCHER =
		1;
	
	/**
	 * Window does not have any frame or window manager elements.
	 *
	 * This is alternatively referred to as being a borderless window. This
	 * should not be used with the window
	 * state {@link ScritchWindowState#FULLSCREEN} as this
	 * flag will break native window manager support for fullscreen
	 * windows.
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNDECORATED =
		2;
	
	/** Window is a utility window. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UTILITY =
		4;
	
	/** Window is always on top of the draw stack. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALWAYS_ON_TOP =
		8;
	
	/** Window is always on the bottom of the draw stack. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALWAYS_ON_BOTTOM =
		16;
	
	/** Window does not permit resize. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_RESIZE =
		32;
	
	/** Window does not permit moving. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_MOVE =
		64;
	
	/** Window is floating and cannot be tiled in tiling window managers. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short FORCE_FLOATING =
		128;
	
	/** Window is a dock app to be embedded in a panel or similar. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short DOCK_APP =
		256;
	
	/** Window is part of a torn off menu. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short TORN_MENU =
		512;
	
	/** Window is part of a torn off toolbar. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short TORN_TOOLBAR =
		1024;
	
	/** Disable all glass effects so the window cannot be seen through. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short NO_GLASS =
		2048;
	
	/** Optimize for drawing, this may disable compositing or adjust vsync. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short OPTIMIZE_DRAWING =
		4096;
	
	/** Show window on all desktops. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short ALL_DESKTOPS =
		8192;
}
