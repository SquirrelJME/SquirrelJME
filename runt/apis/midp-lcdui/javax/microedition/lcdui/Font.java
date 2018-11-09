// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a font which is a representation of the glyphs which
 * are used to display text and pictographs.
 *
 * @since 2017/05/25
 */
public final class Font
{
	/** The monospace font. */
	public static final int FACE_MONOSPACE =
		32;
	
	/** Proportional fonts. */
	public static final int FACE_PROPORTIONAL =
		64;
	
	/** The system font. */
	public static final int FACE_SYSTEM =
		0;
	
	/** The font used for input text. */
	public static final int FONT_INPUT_TEXT =
		1;
	
	/** The text used to draw item and screen content, such as buttons. */
	public static final int FONT_STATIC_TEXT =
		0;
	
	/** The font used for unfocused text on the idle screen. */
	public static final int FONT_IDLE_TEXT =
		2;
	
	/** The font used for highlighted and focused text on the idle screen. */ 
	public static final int FONT_IDLE_HIGHLIGHTED_TEXT =
		3;
	
	/** Large font size. */
	public static final int SIZE_LARGE =
		16;
	
	/** Medium font size, this is the default. */
	public static final int SIZE_MEDIUM =
		0;
	
	/** Small font size. */
	public static final int SIZE_SMALL =
		8;
	
	/** Bold text. */
	public static final int STYLE_BOLD =
		1;
	
	/** Italic (slanted) text. */
	public static final int STYLE_ITALIC =
		2;
	
	/** Plain style text. */
	public static final int STYLE_PLAIN =
		0;
	
	/** Underlined text. */
	public static final int STYLE_UNDERLINED =
		4;
	
	/**
	 * Initializes the font.
	 *
	 * @since 2017/10/20
	 */
	private Font()
	{
	}
	
	/**
	 * Returns the width of the specified character.
	 *
	 * @param __c The character to get the width of.
	 * @return The width of the given character.
	 * @since 2017/10/21
	 */
	public int charWidth(char __c)
	{
		throw new todo.TODO();
	}
	
	public int charsWidth(char[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public Font deriveFont(int __pxs)
	{
		throw new todo.TODO();
	}
	
	public Font deriveFont(int __style, int __pxs)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the ascent of the font from the top of the line to the
	 * average point where the tops of characters are.
	 *
	 * @return The ascent of the font.
	 * @since 2017/10/24
	 */
	public int getAscent()
	{
		throw new todo.TODO();
	}
	
	public int getBaselinePosition()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the descent of the font from the baseline of the font to
	 * the bottom of most alphanumeric characters.
	 *
	 * @return The descent of the font.
	 * @since 2017/10/24
	 */
	public int getDescent()
	{
		throw new todo.TODO();
	}
	
	public int getFace()
	{
		throw new todo.TODO();
	}
	
	public String getFamily()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the standard height of a line for this font. This is equal
	 * to the sum of the leading, ascent, and the descent.
	 *
	 * @return The standard font height.
	 * @since 2017/10/20
	 */
	public int getHeight()
	{
		return this.getLeading() + this.getAscent() + this.getDescent();
	}
	
	/**
	 * Returns the standard leading of the font in pixels. The leading is the
	 * standard number of pixels which are between each line. The space is
	 * reserved between the descent of the first line and the ascent of the
	 * next line.
	 *
	 * @return The standard leading.
	 * @since 2017/10/24
	 */
	public int getLeading()
	{
		throw new todo.TODO();
	}
	
	public int getMaxAscent()
	{
		throw new todo.TODO();
	}
	
	public int getMaxDescent()
	{
		throw new todo.TODO();
	}
	
	public int getPixelSize()
	{
		throw new todo.TODO();
	}
	
	public int getSize()
	{
		throw new todo.TODO();
	}
	
	public int getStyle()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public boolean isBold()
	{
		throw new todo.TODO();
	}
	
	public boolean isItalic()
	{
		throw new todo.TODO();
	}
	
	public boolean isPlain()
	{
		throw new todo.TODO();
	}
	
	public boolean isUnderlined()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the width of the specified string in pixels.
	 *
	 * @param __s The string to get the width.
	 * @return The width of the string in pixels.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	public int stringWidth(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return this.substringWidth(__s, 0, __s.length());
	}
	
	/**
	 * Returns the width of the specified sub-string in pixels.
	 *
	 * @param __s The string to get the width.
	 * @param __o The offset into the string.
	 * @param __l The number of characters to count.
	 * @return The width of the string in pixels.
	 * @throws NullPointerException On null arguments.
	 * @throws StringIndexOutOfBoundsException If the string index is not
	 * within bounds.
	 * @since 2017/10/21
	 */
	public int substringWidth(String __s, int __o, int __l)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		try
		{
			throw new todo.TODO();
			/*
			return this._handle.sequencePixelWidth(__s, __o, __l);
			*/
		}
		
		// For compatibility just wrap out of bounds, since it is
		// confusidly used
		catch (IndexOutOfBoundsException e)
		{
			StringIndexOutOfBoundsException t =
				new StringIndexOutOfBoundsException(e.getMessage());
			t.initCause(e);
			throw t;
		}
	}
	
	public static Font createFont(InputStream __data)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public static Font[] getAvailableFonts()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the fonts which are available on the system using the
	 * standard font size.
	 *
	 * @param __style The style of the font, may be a combination of styles.
	 * @return An array of matching font and styles.
	 * @since 2017/05/25
	 */
	public static Font[] getAvailableFonts(int __style)
	{
		throw new todo.TODO();
	}
	
	public static Font[] getAvailableFonts(int __face, int __style, int __pxs)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the default system font.
	 *
	 * @return The default system font.
	 * @since 2017/05/24
	 */
	public static Font getDefaultFont()
	{
		return Font.getFont(0, 0, 0);
	}
	
	public static Font getFont(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Locates a font which matches the specified parameters the closest.
	 *
	 * @param __face The font face, this is a single value.
	 * @param __style The style of the font, this may be a combination of
	 * values.
	 * @param __size The size of the font, this is a single value.
	 * @return The nearest font which matches the specified parameters.
	 * @throws IllegalArgumentException If the input parameters are not valid.
	 * @since 2017/05/25
	 */
	public static Font getFont(int __face, int __style, int __size)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB1g Invalid font face specified. (The face)}
		if ((__face & ~(FACE_SYSTEM | FACE_MONOSPACE | FACE_PROPORTIONAL)) != 0
			|| Integer.bitCount(__face) > 1)
			throw new IllegalArgumentException(String.format("EB1g %d",
				__face));
		
		// {@squirreljme.error EB1h Invalid font size specified. (The size)}
		if ((__size & ~(SIZE_SMALL | SIZE_MEDIUM | SIZE_LARGE)) != 0
			|| Integer.bitCount(__size) > 1)
			throw new IllegalArgumentException(String.format("EB1h %d",
				__size));
		
		// {@squirreljme.error EB1i Invalid font style specified. (The style)}
		if ((__style & ~(STYLE_PLAIN | STYLE_UNDERLINED | STYLE_BOLD |
			STYLE_ITALIC)) != 0)
			throw new IllegalArgumentException(String.format("EB1i %d",
				__style));
		
		throw new todo.TODO();
		/*
		// Setup font to the given handle, the font size needs to be adjusted
		// so that it uses pixel sizes rather than abstract sizes
		DisplayHead dh = DisplayManager.defaultDisplayHead();
		return new Font(FontManager.FONT_MANAGER.createFont(__face, __style,
			dh.fontSizeToPixelSize(__size)));*/
	}
	
	public static Font getFont(String __name, int __style, int __pxs)
	{
		throw new todo.TODO();
	}
	
	public static int getPixelSize(String __name)
	{
		throw new todo.TODO();
	}
	
	public static int getStyle(String __name)
	{
		throw new todo.TODO();
	}
}


