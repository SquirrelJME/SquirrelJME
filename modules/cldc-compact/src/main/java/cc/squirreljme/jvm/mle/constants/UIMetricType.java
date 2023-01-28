// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This contains information on UI metrics used by
 * {@link UIFormShelf#metric(cc.squirreljme.jvm.mle.brackets.UIDisplayBracket, int)}.
 *
 * @since 2020/06/30
 */
@Exported
public interface UIMetricType
{
	/** Is the UI form engine supported? This metric always works. */
	@Exported
	byte UIFORMS_SUPPORTED =
		0;
	
	/** Background color for opaque canvases. */
	@Exported
	byte COLOR_CANVAS_BACKGROUND =
		1;
	
	/** The maximum display width. */
	@Exported
	byte DISPLAY_MAX_WIDTH =
		2;
	
	/** The maximum display height. */
	@Exported
	byte DISPLAY_MAX_HEIGHT =
		3;
	
	/** The current display width. */
	@Exported
	byte DISPLAY_CURRENT_WIDTH =
		4;
	
	/** The current display height. */
	@Exported
	byte DISPLAY_CURRENT_HEIGHT =
		5;
	
	/** Is the display monochromatic? */
	@Exported
	byte DISPLAY_MONOCHROMATIC =
		6;
	
	/** Returns the {@link UIPixelFormat} of the display. */
	@Exported
	byte DISPLAY_PIXEL_FORMAT =
		7;
	
	/** Returns the {@link UIInputFlag} that determine how the UI controls. */
	@Exported
	byte INPUT_FLAGS =
		8;
	
	/** Is vibration supported? */
	@Exported
	byte SUPPORTS_VIBRATION =
		9;
	
	/** The height of list items. */
	@Exported
	byte LIST_ITEM_HEIGHT =
		10;
	
	/** The height of the command bar. */
	@Exported
	byte COMMAND_BAR_HEIGHT =
		11;
	
	/** Supports controlling the backlight. */
	@Exported
	byte SUPPORTS_BACKLIGHT_CONTROL =
		12;
	
	/** The number of supported metrics. */
	@Exported
	byte NUM_METRICS =
		13;
}
