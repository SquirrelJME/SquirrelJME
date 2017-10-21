// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.font;

import javax.microedition.lcdui.Font;
import net.multiphasicapps.squirreljme.unsafe.SystemEnvironment;

/**
 * This class is used as a service and provides management for fonts to be
 * provided to the LCDUI interface.
 *
 * The font manager for the most part just provides some basic details along
 * with their graphical glyphs.
 *
 * @since 2017/10/01
 */
public abstract class FontManager
{
	/** The single instance font manager to use. */
	public static final FontManager FONT_MANAGER;
	
	/**
	 * Locates the font manager to use in this instance or returns the
	 * default font manager with a set of default provided fonts.
	 *
	 * @since 2017/10/20
	 */
	static
	{
		// Use either the provided font manager or a default if none was set
		FontManager fm = SystemEnvironment.<FontManager>systemService(
			FontManager.class);
		FONT_MANAGER = (fm == null ? new DefaultFontManager() : fm);
	}
	
	/**
	 * Returns a primitive font which closest matches the given face.
	 *
	 * @param __face The face of the font to get.
	 * @return The primitive font which closest matches the given face.
	 * @since 2017/10/20
	 */
	public abstract PrimitiveFont getPrimitiveFont(int __face);
}

