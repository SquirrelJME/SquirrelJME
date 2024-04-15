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
 * Special user interface constants.
 *
 * @since 2020/09/21
 */
public interface UISpecialCode
{
	/** Fire repaint. */
	byte REPAINT_EXECUTE =
		0x0;
	
	/** Repaint X Coordinate ({@link UIWidgetProperty#INT_SIGNAL_REPAINT}. */
	int REPAINT_KEY_X =
		0x1_0000000;
	
	/** Repaint Y Coordinate ({@link UIWidgetProperty#INT_SIGNAL_REPAINT}. */
	int REPAINT_KEY_Y =
		0x2_0000000;
	
	/** Repaint Width ({@link UIWidgetProperty#INT_SIGNAL_REPAINT}. */
	int REPAINT_KEY_WIDTH =
		0x3_0000000;
	
	/** Repaint Height ({@link UIWidgetProperty#INT_SIGNAL_REPAINT}. */
	int REPAINT_KEY_HEIGHT =
		0x4_0000000;
	
	/** Repaint Key Mask ({@link UIWidgetProperty#INT_SIGNAL_REPAINT}. */
	int REPAINT_KEY_MASK =
		0xF_0000000;
	
	/** Repaint Key Value ({@link UIWidgetProperty#INT_SIGNAL_REPAINT}. */
	int REPAINT_VALUE_MASK =
		0x0_FFFFFFF;
}
