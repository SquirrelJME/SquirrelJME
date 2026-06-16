// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

import cc.squirreljme.jvm.mle.constants.PencilFontFace;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Font;
import org.intellij.lang.annotations.MagicConstant;

/**
 * This class provides static utility methods for fonts and otherwise.
 *
 * @since 2018/11/24
 */
@SquirrelJMEVendorApi
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
	 * Returns the common face for the given logical font name.
	 *
	 * @param __name The name of the font to locate.
	 * @return The face of the logical font.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/11/30
	 */
	@MagicConstant(flagsFromClass = PencilFontFace.class)
	public static int faceNameToPencil(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Serif
		if (__name.equalsIgnoreCase("Serif") ||
			__name.equalsIgnoreCase("Times New Roman") ||
			__name.equalsIgnoreCase("New York") ||
			__name.equalsIgnoreCase("Toronto"))
			return PencilFontFace.SERIF;
		
		// Sans-Serif
		else if (__name.equalsIgnoreCase("SansSerif") ||
			__name.equalsIgnoreCase("Dialog") ||
			__name.equalsIgnoreCase("DialogInput") ||
			__name.equalsIgnoreCase("Helvetica") ||
			__name.equalsIgnoreCase("Arial") ||
			__name.equalsIgnoreCase("Chicago") ||
			__name.equalsIgnoreCase("Geneva"))
			return PencilFontFace.NORMAL;
		
		// Monospaced
		else if (__name.equalsIgnoreCase("Monospace") ||
			__name.equalsIgnoreCase("Monospaced") ||
			__name.equalsIgnoreCase("Courier") ||
			__name.equalsIgnoreCase("Courier New") ||
			__name.equalsIgnoreCase("Monaco"))
			return PencilFontFace.MONOSPACE;
		
		// Unknown, do not consider it in a match
		return PencilFontFace.AUTOMATIC;
	}
	
	/**
	 * Maps a MIDP face to a {@link PencilFontFace}.
	 *
	 * @param __face The face to map.
	 * @return The resulting mapped face.
	 * @since 2026/01/18
	 */
	@MagicConstant(flagsFromClass = PencilFontFace.class)
	public static final int faceToPencil(
		@MagicConstant(flagsFromClass = Font.class) int __face)
	{
		switch (__face)
		{
			case Font.FACE_SYSTEM:
			case Font.FACE_PROPORTIONAL:
				return PencilFontFace.NORMAL;
				
			case Font.FACE_MONOSPACE:
				return PencilFontFace.MONOSPACE;
		}
		
		return PencilFontFace.AUTOMATIC;
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public static int pixelSizeToLogicalSize(int __psz)
	{
		if (__psz < 10)
			return Font.SIZE_SMALL;
		else if (__psz < 14)
			return Font.SIZE_MEDIUM;
		return Font.SIZE_LARGE;
	}
}

