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
 * Color for a ScritchUI element.
 *
 * @since 2024/03/09
 */
public interface ScritchLAFElementColor
{
	/** Background color. */
	byte BACKGROUND =
		0;
	
	/** Border color. */
	byte BORDER =
		1;
	
	/** Foreground color. */
	byte FOREGROUND =
		2;
	
	/** Highlighted background color. */
	byte HIGHLIGHTED_BACKGROUND =
		3;
	
	/** Highlighted border color. */
	byte HIGHLIGHTED_BORDER =
		4;
	
	/** Highlighted foreground color. */
	byte HIGHLIGHTED_FOREGROUND =
		5;
	
	/** Focus border color. */
	byte FOCUS_BORDER =
		6;
	
	/** Panel background color. */
	byte PANEL_BACKGROUND =
		7;
	
	/** Panel foreground color. */
	byte PANEL_FOREGROUND =
		8;
	
	/** Top accent color */
	byte ACCENT_TOP =
		9;
	
	/** Bottom accent color. */
	byte ACCENT_BOTTOM =
		10;
	
	/** The number of available colors. */
	byte NUM_LAF_ELEMENT_COLOR =
		11;
}
