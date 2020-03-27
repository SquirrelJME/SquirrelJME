// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

/**
 * This represents the capabilities that a display supports in hardware.
 *
 * @since 2016/08/30
 */
public enum DisplayCapability
{
	/** Supports background colors. */
	SUPPORTS_BACKGROUND_COLORS,
	
	/** Supports horizontal scrolling. */
	SUPPORTS_HORIZONTAL_SCROLLING,
	
	/** Supports key events. */
	SUPPORTS_KEY_EVENTS,
	
	/** Supports lighting (backlight). */
	SUPPORTS_LIGHTING,
	
	/** Supports lighting with different colors (backlight). */
	SUPPORTS_LIGHTING_COLORS,
	
	/** Supports text colors. */
	SUPPORTS_TEXT_COLORS,
	
	/** Supports vertical scrolling. */
	SUPPORTS_VERTICAL_SCROLLING,
	
	/* End. */
	;
}

