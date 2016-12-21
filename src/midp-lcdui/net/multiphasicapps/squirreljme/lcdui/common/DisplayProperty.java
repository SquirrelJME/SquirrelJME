// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.common;

/**
 * This is used to access properties of a given display.
 *
 * @since 2016/10/14
 */
public enum DisplayProperty
{
	/** Not a display property. */
	NONE,
	
	/** Display capabilities returned by LCDUI. */
	CAPABILITIES,
	
	/** The dor pitch in micrometers. */
	DOT_PITCH,
	
	/** Has pointer/stylus press/release events? */
	POINTER_PRESS_RELEASE,
	
	/** Has pointer/sytlus move/drag events? */
	POINTER_DRAG_MOVE,
	
	/** The size of the display (w16h16). */
	DISPLAY_SIZE,
	
	/** Best image size for list elements (w16h16). */
	BEST_IMAGE_SIZE_LIST_ELEMENT,
	
	/** Best image size for choice group elements (w16h16). */
	BEST_IMAGE_SIZE_CHOICE_GROUP_ELEMENT,
	
	/** Best image size for alerts (w16h16). */
	BEST_IMAGE_SIZE_ALERT,
	
	/** Best image size for tabs (w16h16). */
	BEST_IMAGE_SIZE_TAB,
	
	/** Best image size for commands (w16h16). */
	BEST_IMAGE_SIZE_COMMAND,
	
	/** Best image size for notifications (w16h16). */
	BEST_IMAGE_SIZE_NOTIFICATION,
	
	/** Best image size for menus (w16h16). */
	BEST_IMAGE_SIZE_MENU,
	
	/** The background color. */
	COLOR_BACKGROUND,
	
	/** The foreground color. */
	COLOR_FOREGROUND,
	
	/** The highlighted background color. */
	COLOR_HIGHLIGHTED_BACKGROUND,
	
	/** The highlighted foreground color. */
	COLOR_HIGHLIGHTED_FOREGROUND,
	
	/** The border color. */
	COLOR_BORDER,
	
	/** The highlighted border color. */
	COLOR_HIGHLIGHTED_BORDER,
	
	/** The idle background color. */
	COLOR_IDLE_BACKGROUND,
	
	/** The idle foreground color. */
	COLOR_IDLE_FOREGROUND,
	
	/** The highlighted idle background color. */
	COLOR_IDLE_HIGHLIGHTED_BACKGROUND,
	
	/** The highlighted idle foreground color. */
	COLOR_IDLE_HIGHLIGHTED_FOREGROUND,
	
	/** The border style for non-highlights. */
	BORDER_STYLE_NORMAL,
	
	/** The border style for highlights. */
	BORDER_STYLE_HIGHLIGHT,
	
	/** Is the display color? */
	IS_COLOR,
	
	/** The number of supported colors. */
	NUM_COLORS,
	
	/** The number of alpha levels. */
	NUM_ALPHA_LEVELS,
	
	/** Is the display built-in? */
	BUILT_IN,
	
	/** End. */
	;
}

