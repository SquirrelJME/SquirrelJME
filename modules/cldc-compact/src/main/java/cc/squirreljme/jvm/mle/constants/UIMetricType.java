// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.UIFormShelf;

/**
 * This contains information on UI metrics used by
 * {@link UIFormShelf#metric(int)}.
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
	
	/** The number of supported metrics. */
	byte NUM_METRICS =
		6;
}
