// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

import javax.microedition.lcdui.Font;

/**
 * This class provides static methods for the conversion of font sizes from
 * pixel sizes to logical sizes.
 *
 * @since 2018/11/24
 */
public final class FontSizeConversion
{
	/**
	 * Not used.
	 *
	 * @since 2018/11/24
	 */
	private FontSizeConversion()
	{
	}
	
	/**
	 * Converts the logical font size to pixel size.
	 *
	 * @param __lsz The logical font size.
	 * @return The pixel size.
	 * @throws IllegalArgumentException If the logical font size is not valid.
	 * @since 2018/11/24
	 */
	public static final int logicalSizeToPixelSize(int __lsz)
		throws IllegalArgumentException
	{
		switch (__lsz)
		{
			case Font.SIZE_SMALL:
				return 8;
			
			case Font.SIZE_MEDIUM:
				return 16;
			
			case Font.SIZE_LARGE:
				return 24;
			
				// {@squirreljme.error EB2e Invalid logical font size.}
			default:
				throw new IllegalArgumentException("EB2e");
		}
	}
	
	/**
	 * Converts the pixel size to a logical font size.
	 *
	 * @praam __psz The pixel font size.
	 * @return The logical size.
	 * @since 2018/11/24
	 */
	public static final int pixelSizeToLogicalSize(int __psz)
	{
		if (__psz < 12)
			return Font.SIZE_SMALL;
		else if (__psz < 20)
			return Font.SIZE_MEDIUM;
		return Font.SIZE_LARGE;
	}
}

