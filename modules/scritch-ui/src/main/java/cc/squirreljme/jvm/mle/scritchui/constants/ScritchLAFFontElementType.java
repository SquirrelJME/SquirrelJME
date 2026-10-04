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
 * The type of element for lookup of fonts.
 *
 * @since 2024/05/17
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ScritchLAFFontElementType
{
	/** The font to use for terminals. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TERMINAL =
		1;
	
	/** The font to use for widget controls such as buttons. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte WIDGET_CONTROLS =
		2;
	
	/** The font to use for title bars. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TITLE_BAR =
		3;
	
	/** The font to use for menu bars. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MENU_BAR =
		4;
	
	/** The font to use for toolbars. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TOOL_BAR =
		5;
	
	/** The font to use for small text. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SMALL =
		6;
	
	/** The number of valid element types. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_LAF_FONT_ELEMENT_TYPES =
		7;
}
