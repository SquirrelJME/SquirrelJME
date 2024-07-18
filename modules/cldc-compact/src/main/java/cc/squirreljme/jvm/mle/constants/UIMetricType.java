// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * This contains information on UI metrics used by
 * {@link UIFormShelf#metric(cc.squirreljme.jvm.mle.brackets.UIDisplayBracket, int)}.
 *
 * @since 2020/06/30
 */
@Deprecated
public interface UIMetricType
{
	/** Is the UI form engine supported? This metric always works. */
	@Deprecated
	byte UIFORMS_SUPPORTED =
		0;
	
	/** Background color for opaque canvases. */
	@Deprecated
	byte COLOR_CANVAS_BACKGROUND =
		1;
	
	/** The maximum display width. */
	@Deprecated
	byte DISPLAY_MAX_WIDTH =
		2;
	
	/** The maximum display height. */
	@Deprecated
	byte DISPLAY_MAX_HEIGHT =
		3;
	
	/** The current display width. */
	@Deprecated
	byte DISPLAY_CURRENT_WIDTH =
		4;
	
	/** The current display height. */
	@Deprecated
	byte DISPLAY_CURRENT_HEIGHT =
		5;
	
	/** Is the display monochromatic? */
	@Deprecated
	byte DISPLAY_MONOCHROMATIC =
		6;
	
	/** Returns the {@link UIPixelFormat} of the display. */
	@Deprecated
	byte DISPLAY_PIXEL_FORMAT =
		7;
	
	/** Returns the {@link UIInputFlag} that determine how the UI controls. */
	@Deprecated
	byte INPUT_FLAGS =
		8;
	
	/** Is vibration supported? */
	@Deprecated
	byte SUPPORTS_VIBRATION =
		9;
	
	/** The height of list items. */
	@Deprecated
	byte LIST_ITEM_HEIGHT =
		10;
	
	/** The height of the command bar. */
	@Deprecated
	byte COMMAND_BAR_HEIGHT =
		11;
	
	/** Supports controlling the backlight. */
	@Deprecated
	byte SUPPORTS_BACKLIGHT_CONTROL =
		12;
	
	/** The number of supported metrics. */
	@Deprecated
	byte NUM_METRICS =
		13;
}
