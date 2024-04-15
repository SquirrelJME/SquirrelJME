// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

import cc.squirreljme.jvm.mle.constants.UIFontFlag;
import javax.microedition.lcdui.Font;

/**
 * This class provides static utility methods for fonts and otherwise.
 *
 * @since 2018/11/24
 */
public final class FontUtilities
{
	/**
	 * Not used.
	 *
	 * @since 2018/11/24
	 */
	private FontUtilities()
	{
	}
	
	/**
	 * Converts a representation of the font to a system font ID.
	 * 
	 * @param __font The font to identify.
	 * @return The identifier for the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/14
	 */
	public static int fontToSystemFont(Font __font)
		throws NullPointerException
	{
		if (__font == null)
			throw new NullPointerException("NARG");
		
		int id = 0;
		
		// Shift in size
		int size = __font.getPixelSize();
		id |= (size > UIFontFlag.PIXEL_SIZE_MASK ? UIFontFlag.PIXEL_SIZE_MASK :
			size);
		
		// Shift in font face
		switch (__font.getFace())
		{
			case Font.FACE_PROPORTIONAL:
				id |= UIFontFlag.FACE_PROPORTIONAL;
				break;
				
			case Font.FACE_MONOSPACE:
				id |= UIFontFlag.FACE_MONOSPACE;
				break;
			
			case Font.FACE_SYSTEM:
			default:
				id |= UIFontFlag.FACE_SYSTEM;
				break;
		}
		
		// Map font style
		int style = __font.getStyle();
		if ((style & Font.STYLE_ITALIC) != 0)
			id |= UIFontFlag.STYLE_ITALIC_FLAG;
		if ((style & Font.STYLE_BOLD) != 0)
			id |= UIFontFlag.STYLE_BOLD_FLAG;
		if ((style & Font.STYLE_UNDERLINED) != 0)
			id |= UIFontFlag.STYLE_UNDERLINED_FLAG;
		
		return id;
	}
	
	/**
	 * Converts the logical font size to pixel size.
	 *
	 * @param __lsz The logical font size.
	 * @return The pixel size.
	 * @throws IllegalArgumentException If the logical font size is not valid.
	 * @since 2018/11/24
	 */
	@SuppressWarnings("MagicNumber")
	public static int logicalSizeToPixelSize(int __lsz)
		throws IllegalArgumentException
	{
		switch (__lsz)
		{
			case Font.SIZE_SMALL:
				return 8;
			
			case Font.SIZE_MEDIUM:
				return 12;
			
			case Font.SIZE_LARGE:
				return 16;
			
				/* {@squirreljme.error EB07 Invalid logical font size.} */
			default:
				throw new IllegalArgumentException("EB07");
		}
	}
	
	/**
	 * Converts the pixel size to a logical font size.
	 *
	 * @param __psz The pixel font size.
	 * @return The logical size.
	 * @since 2018/11/24
	 */
	public static int pixelSizeToLogicalSize(int __psz)
	{
		if (__psz < 10)
			return Font.SIZE_SMALL;
		else if (__psz < 14)
			return Font.SIZE_MEDIUM;
		return Font.SIZE_LARGE;
	}
}

