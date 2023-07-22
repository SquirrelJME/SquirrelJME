// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.UIFormShelf;

/**
 * This contains information on UI metrics used by
 * {@link UIFormShelf#metric(cc.squirreljme.jvm.mle.brackets.UIDisplayBracket, int)}.
 *
 * @since 2020/06/30
 */
public interface UIMetricType
{
	/** Is the UI form engine supported? This metric always works. */
	byte UIFORMS_SUPPORTED =
		0;
	
	/** Background color for opaque canvases. */
	byte COLOR_CANVAS_BACKGROUND =
		1;
	
	/** The maximum display width. */
	byte DISPLAY_MAX_WIDTH =
		2;
	
	/** The maximum display height. */
	byte DISPLAY_MAX_HEIGHT =
		3;
	
	/** The current display width. */
	byte DISPLAY_CURRENT_WIDTH =
		4;
	
	/** The current display height. */
	byte DISPLAY_CURRENT_HEIGHT =
		5;
	
	/** Is the display monochromatic? */
	byte DISPLAY_MONOCHROMATIC =
		6;
	
	/** Returns the {@link UIPixelFormat} of the display. */
	byte DISPLAY_PIXEL_FORMAT =
		7;
	
	/** Returns the {@link UIInputFlag} that determine how the UI controls. */
	byte INPUT_FLAGS =
		8;
	
	/** Is vibration supported? */
	byte SUPPORTS_VIBRATION =
		9;
	
	/** The height of list items. */
	byte LIST_ITEM_HEIGHT =
		10;
	
	/** The height of the command bar. */
	byte COMMAND_BAR_HEIGHT =
		11;
	
	/** Supports controlling the backlight. */
	byte SUPPORTS_BACKLIGHT_CONTROL =
		12;
	
	/** The number of supported metrics. */
	byte NUM_METRICS =
		13;
}
