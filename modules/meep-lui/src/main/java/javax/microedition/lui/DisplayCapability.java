// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This represents the capabilities that a display supports in hardware.
 *
 * @since 2016/08/30
 */
@Api
public enum DisplayCapability
{
	/** Supports background colors. */
	@Api
	SUPPORTS_BACKGROUND_COLORS,
	
	/** Supports horizontal scrolling. */
	@Api
	SUPPORTS_HORIZONTAL_SCROLLING,
	
	/** Supports key events. */
	@Api
	SUPPORTS_KEY_EVENTS,
	
	/** Supports lighting (backlight). */
	@Api
	SUPPORTS_LIGHTING,
	
	/** Supports lighting with different colors (backlight). */
	@Api
	SUPPORTS_LIGHTING_COLORS,
	
	/** Supports text colors. */
	@Api
	SUPPORTS_TEXT_COLORS,
	
	/** Supports vertical scrolling. */
	@Api
	SUPPORTS_VERTICAL_SCROLLING,
	
	/** End. */
	;
}

