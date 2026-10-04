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
 * The type of element for lookup of fonts.
 *
 * @since 2024/05/17
 */
public interface ScritchLAFFontElementType
{
	/** The font to use for terminals. */
	byte TERMINAL =
		1;
	
	/** The font to use for widget controls such as buttons. */
	byte WIDGET_CONTROLS =
		2;
	
	/** The font to use for title bars. */
	byte TITLE_BAR =
		3;
	
	/** The font to use for menu bars. */
	byte MENU_BAR =
		4;
	
	/** The font to use for toolbars. */
	byte TOOL_BAR =
		5;
	
	/** The font to use for small text. */
	byte SMALL =
		6;
	
	/** The number of valid element types. */
	byte NUM_LAF_FONT_ELEMENT_TYPES =
		7;
}
