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
 * Color for a ScritchUI element.
 *
 * @since 2024/03/09
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ScritchLAFElementColor
{
	/** Background color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BACKGROUND =
		0;
	
	/** Border color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BORDER =
		1;
	
	/** Foreground color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FOREGROUND =
		2;
	
	/** Highlighted background color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HIGHLIGHTED_BACKGROUND =
		3;
	
	/** Highlighted border color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HIGHLIGHTED_BORDER =
		4;
	
	/** Highlighted foreground color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte HIGHLIGHTED_FOREGROUND =
		5;
	
	/** Focus border color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FOCUS_BORDER =
		6;
	
	/** Panel background color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PANEL_BACKGROUND =
		7;
	
	/** Panel foreground color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PANEL_FOREGROUND =
		8;
	
	/** Top accent color */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ACCENT_TOP =
		9;
	
	/** Bottom accent color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ACCENT_BOTTOM =
		10;
	
	/** The number of available colors. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_LAF_ELEMENT_COLOR =
		11;
}
