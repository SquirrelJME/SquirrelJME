// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a font.
 *
 * @see javax.microedition.lcdui.Font
 * @since 2021/11/30
 */
@Api
public class Font
{
	@Api
	public static final int FACE_MONOSPACE =
		0x7200_0000;
	
	@Api
	public static final int FACE_PROPORTIONAL =
		0x7300_0000;
	
	@Api
	public static final int FACE_SYSTEM =
		0x7100_0000;
	
	@Api
	public static final int SIZE_LARGE =
		0x70000300;
	
	@Api
	public static final int SIZE_MEDIUM =
		0x70000200;
	
	@Api
	public static final int SIZE_SMALL =
		0x70000100;
	
	@Api
	public static final int SIZE_TINY =
		0x70000400;
	
	@Api
	public static final int STYLE_BOLD =
		0x70110000;
	
	@Api
	public static final int STYLE_BOLDITALIC =
		0x70130000;
	
	@Api
	public static final int STYLE_ITALIC =
		0x70120000;
	
	@Api
	public static final int STYLE_PLAIN =
		0x70100000;
	
	@Api
	public static final int TYPE_DEFAULT =
		0x00000000;
	
	@Api
	public static final int TYPE_HEADING =
		0x00000001;
	
	/** The mask for the font face. */
	private static final int _FACE_MASK =
		Font.FACE_MONOSPACE | Font.FACE_PROPORTIONAL | Font.FACE_SYSTEM;
	
	/** The mask for the size. */
	private static final int _SIZE_MASK =
		Font.SIZE_LARGE | Font.SIZE_MEDIUM | Font.SIZE_SMALL | Font.SIZE_TINY;
	
	/** The mask for the style. */
	private static final int _STYLE_MASK =
		Font.STYLE_BOLD | Font.STYLE_BOLDITALIC | Font.STYLE_ITALIC |
			Font.STYLE_PLAIN;
	
	/** The valid bits used for the font style. */
	private static final int _VALID_BITS =
		Font._FACE_MASK | Font._SIZE_MASK | Font._STYLE_MASK;
	
	/** The default font. */
	private static volatile Font _DEFAULT;
	
	/** The MIDP font to be based upon. */
	final javax.microedition.lcdui.Font _midpFont;
	
	/**
	 * Initializes the wrapped font.
	 * 
	 * @param __midpFont The font to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/07
	 */
	Font(javax.microedition.lcdui.Font __midpFont)
		throws NullPointerException
	{
		if (__midpFont == null)
			throw new NullPointerException("NARG");
		
		this._midpFont = __midpFont;
	}
	
	/**
	 * This returns the ascent of the font from the top of the line to the
	 * average point where the tops of characters are.
	 *
	 * @return The ascent of the font.
	 * @since 2022/10/07
	 */
	public int getAscent()
	{
		return this._midpFont.getAscent();
	}
	
	@Api
	public int getBBoxHeight(String __s)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getBBoxWidth(String __s)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the descent of the font, this is the distance from the baseline
	 * to the bottom edge of the font.
	 *
	 * @return The descent of the font in pixels.
	 * @since 2024/01/06
	 */
	@Api
	public int getDescent()
	{
		return this._midpFont.getDescent();
	}
	
	/**
	 * Returns the height of the font which is the sum of the ascent and
	 * descent.
	 *
	 * @return The font height.
	 * @since 2024/09/14
	 */
	@Api
	public int getHeight()
	{
		return this.getAscent() + this.getDescent();
	}
	
	@Api
	public int getLineBreak(String __s, int __off, int __len, int __w)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the width of the string for the given font.
	 *
	 * @param __s The string to get the width of.
	 * @return The resultant string width in pixels.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	@Api
	public int stringWidth(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return this._midpFont.stringWidth(__s);
	}
	
	/**
	 * Returns the default font.
	 *
	 * @return The default font.
	 * @since 2024/08/11
	 */
	@Api
	public static Font getDefaultFont()
	{
		synchronized (Font.class)
		{
			// Need to seed a default?
			Font result = Font._DEFAULT;
			if (result == null)
			{
				result = new Font(
					javax.microedition.lcdui.Font.getDefaultFont());
				Font._DEFAULT = result;
			}
			
			return result;
		}
	}
	
	/**
	 * Obtains the given font.
	 * 
	 * @param __type The type of font to get.
	 * @return The given font.
	 * @throws IllegalArgumentException If a parameter was not correct.
	 * @since 2022/10/07
	 */
	@Api
	public static Font getFont(int __type)
		throws IllegalArgumentException
	{
		// Use the default font?
		if (__type == Font.TYPE_DEFAULT)
			return new Font(javax.microedition.lcdui.Font.getDefaultFont());
		
		// Use a heading font
		else if (__type == Font.TYPE_HEADING)
			return new Font(javax.microedition.lcdui.Font.getFont(
				javax.microedition.lcdui.Font.FACE_SYSTEM,
				javax.microedition.lcdui.Font.STYLE_BOLD,
				javax.microedition.lcdui.Font.SIZE_MEDIUM));
		
		// {@squirreljme.error AH0s Invalid font specified.}
		if (0 != (__type & (~Font._VALID_BITS)))
			throw new IllegalArgumentException("AH0s");
		
		// Determine the face of the font
		int midpFace;
		switch (__type & Font._FACE_MASK)
		{
			case Font.FACE_MONOSPACE:
				midpFace = javax.microedition.lcdui.Font.FACE_MONOSPACE;
				break;
				
			case Font.FACE_PROPORTIONAL:
				midpFace = javax.microedition.lcdui.Font.FACE_PROPORTIONAL;
				break;
				
			case Font.FACE_SYSTEM:
				midpFace = javax.microedition.lcdui.Font.FACE_SYSTEM;
				break;
			
			default:
				throw new IllegalArgumentException("AH0s");
		}
		
		// Determine the size of the font
		int midpSize;
		switch (__type & Font._SIZE_MASK)
		{
			case Font.SIZE_LARGE:
				midpSize = javax.microedition.lcdui.Font.SIZE_LARGE;
				break;
				
			case Font.SIZE_MEDIUM:
				midpSize = javax.microedition.lcdui.Font.SIZE_MEDIUM;
				break;
				
			case Font.SIZE_SMALL:
			case Font.SIZE_TINY:
				midpSize = javax.microedition.lcdui.Font.SIZE_SMALL;
				break;
			
			default:
				throw new IllegalArgumentException("AH0s");
		}
		
		// Determine the style of the font
		int midpStyle;
		switch (__type & Font._STYLE_MASK)
		{
			case Font.STYLE_PLAIN:
				midpStyle = javax.microedition.lcdui.Font.STYLE_PLAIN;
				break;
				
			case Font.STYLE_BOLD:
				midpStyle = javax.microedition.lcdui.Font.STYLE_BOLD;
				break;
				
			case Font.STYLE_ITALIC:
				midpStyle = javax.microedition.lcdui.Font.STYLE_ITALIC;
				break;
				
			case Font.STYLE_BOLDITALIC:
				midpStyle = javax.microedition.lcdui.Font.STYLE_BOLD |
					javax.microedition.lcdui.Font.STYLE_ITALIC;
				break;
			
			default:
				throw new IllegalArgumentException("AH0s");
		}
		
		// Setup font wrapper
		return new Font(javax.microedition.lcdui.Font.getFont(midpFace,
			midpStyle, midpSize));
	}
	
	/**
	 * Sets the default font. 
	 *
	 * @param __f The default font to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/11
	 */
	@Api
	public static void setDefaultFont(Font __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		synchronized (Font.class)
		{
			Font._DEFAULT = __f;
		}
	}
}
