// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.PencilFontShelf;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.PencilFontFace;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.font.FontUtilities;
import cc.squirreljme.runtime.lcdui.mle.PencilFontProvider;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * This class represents a font which is a representation of the glyphs which
 * are used to display text and pictographs.
 *
 * @since 2017/05/25
 */
@Api
public final class Font
	extends PencilFontProvider
{
	/** The monospace font. */
	@Api
	public static final int FACE_MONOSPACE =
		32;
	
	/** Proportional fonts. */
	@Api
	public static final int FACE_PROPORTIONAL =
		64;
	
	/** The system font. */
	@Api
	public static final int FACE_SYSTEM =
		0;
	
	/** The font used for input text. */
	@Api
	public static final int FONT_INPUT_TEXT =
		1;
	
	/** The text used to draw item and screen content, such as buttons. */
	@Api
	public static final int FONT_STATIC_TEXT =
		0;
	
	/** The font used for unfocused text on the idle screen. */
	@Api
	public static final int FONT_IDLE_TEXT =
		2;
	
	/** The font used for highlighted and focused text on the idle screen. */ 
	@Api
	public static final int FONT_IDLE_HIGHLIGHTED_TEXT =
		3;
	
	/** Large font size. */
	@Api
	public static final int SIZE_LARGE =
		16;
	
	/** Medium font size, this is the default. */
	@Api
	public static final int SIZE_MEDIUM =
		0;
	
	/** Small font size. */
	@Api
	public static final int SIZE_SMALL =
		8;
	
	/** Bold text. */
	@Api
	public static final int STYLE_BOLD =
		1;
	
	/** Italic (slanted) text. */
	@Api
	public static final int STYLE_ITALIC =
		2;
	
	/** Plain style text. */
	@Api
	public static final int STYLE_PLAIN =
		0;
	
	/** Underlined text. */
	@Api
	public static final int STYLE_UNDERLINED =
		4;
	
	/** The default font size. */
	private static final int _DEFAULT_FONT_SIZE =
		12;
	
	/** Built-in available fonts. */
	private static Font[] _BUILTIN_FONTS;
	
	/** The default font. */
	private static Font _DEFAULT_FONT;
	
	/** The bracket used to access the font. */
	final PencilFontBracket _font;
	
	/** The name of this font. */
	private final String _name;
	
	/** The ScritchUI interface used. */
	private final ScritchInterface _scritch;
	
	/** The style of this font. */
	private final int _style;
	
	/** The pixel size of the font. */
	private final int _pixelsize;
	
	/** The face of the font. */
	private final int _face;
	
	/** The height of the font, is pre-calculated. */
	private int _height =
		-1;
	
	/**
	 * Initializes a font that wraps a bracket.
	 *
	 * @param __scritch The ScritchUI API.
	 * @param __bracket The bracket to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/17
	 */
	Font(ScritchInterface __scritch, PencilFontBracket __bracket)
		throws NullPointerException
	{
		if (__scritch == null || __bracket == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this._scritch = __scritch;
		this._font = __bracket;
		
		// Get information on the font
		this._name = PencilFontShelf.metricFontName(__bracket);
		
		// What is the face of this font?
		int face = PencilFontShelf.metricFontFace(__bracket);
		if ((face & PencilFontFace.MONOSPACE) != 0)
			this._face = Font.FACE_MONOSPACE;
		else
			this._face = Font.FACE_PROPORTIONAL;
		
		// Get pixel size of font
		this._pixelsize = PencilFontShelf.metricPixelSize(__bracket);
		
		// Font style directly maps
		this._style = PencilFontShelf.metricFontStyle(__bracket);
	}
	
	/**
	 * Returns the width of the specified character.
	 *
	 * @param __c The character to get the width of.
	 * @return The width of the given character.
	 * @since 2017/10/21
	 */
	@Api
	public int charWidth(char __c)
	{
		throw Debugging.todo();
		/*
		return this._sqf.charWidth(SQFFont.mapChar(__c));
		
		 */
	}
	
	/**
	 * Returns the width of the specified characters, as if it were drawn
	 * on the screen.
	 *
	 * @param __c The characters to check.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The width of the string.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	@Api
	public int charsWidth(char[] __c, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __c.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
		/*
		SQFFont sqf = this._sqf;
		
		// Calculate width
		int x = 0,
			max = 0;
		for (int i = 0; i < __l; i++)
		{
			char c = __c[__o++];
			
			// Ignore carriage returns
			if (c == '\r')
				continue;
			
			// Next line?
			else if (c == '\n')
			{
				if (x > max)
					max = x;
				x = 0;
			}
			
			// Add character
			else
				x += sqf.charWidth(SQFFont.mapChar(c));
		}
		
		// Return the higher of the two
		return (x > max ? x : max);
		
		 */
	}
	
	/**
	 * Derives a font using the given pixel size.
	 *
	 * @param __pixelSize The pixel size of the font.
	 * @return The derived font.
	 * @throws IllegalArgumentException If this font is a bitmap font and
	 * no font is available using that size.
	 * @since 2018/11/24
	 */
	@Api
	public Font deriveFont(int __pixelSize)
		throws IllegalArgumentException
	{
		return this.deriveFont(this.getStyle(), __pixelSize);
	}
	
	/**
	 * Derives a font using the given style and pixel size.
	 *
	 * @param __style The style of the font.
	 * @param __pixelSize The pixel size of the font.
	 * @return The derived font.
	 * @throws IllegalArgumentException If this font is a bitmap font and
	 * no font is available using that size, or the style is not valid.
	 * @since 2018/11/24
	 */
	@Api
	public Font deriveFont(int __style, int __pixelSize)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1t Invalid font style specified.
		(The style)} */
		if ((__style & ~(Font.STYLE_PLAIN | Font.STYLE_UNDERLINED |
			Font.STYLE_BOLD)) != 0)
			throw new IllegalArgumentException(String.format("EB1t %d",
				__style));
		
		// Use default font size?
		if (__pixelSize == 0)
			__pixelSize = FontUtilities.logicalSizeToPixelSize(
				Font.SIZE_MEDIUM);
		
		/* {@squirreljme.error EB1u The pixel size of a font cannot be
		negative.} */
		else if (__pixelSize < 0)
			throw new IllegalArgumentException("EB1u");
		
		// Same exact font?
		if (this._style == __style && this._pixelsize == __pixelSize)
			return this;
		
		// Derive font
		ScritchInterface scritch = this._scritch;
		return new Font(scritch, scritch.environment()
			.fontDerive(this._font, __style, __pixelSize));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Same object?
		if (__o == this)
			return true;
		
		// Not a font?
		if (!(__o instanceof Font))
			return false;
		
		Font o = (Font)__o;
		return this._pixelsize == o._pixelsize &&
			this._style == o._style &&
			this._name.equals(o._name) &&
			PencilFontShelf.equals(this._font, o._font);
	}
	
	/**
	 * This returns the ascent of the font from the top of the line to the
	 * average point where the tops of characters are.
	 *
	 * @return The ascent of the font.
	 * @since 2017/10/24
	 */
	@Api
	public int getAscent()
	{
		return PencilFontShelf.metricPixelAscent(this._font, false);
	}
	
	/**
	 * Returns the baseline position of the font which will be the maximum
	 * baseline position of every character.
	 *
	 * @return The baseline of the font.
	 * @since 2018/11/29
	 */
	@Api
	public int getBaselinePosition()
	{
		return PencilFontShelf.metricPixelBaseline(this._font);
	}
	
	/**
	 * This returns the descent of the font from the baseline of the font to
	 * the bottom of most alphanumeric characters.
	 *
	 * @return The descent of the font.
	 * @since 2017/10/24
	 */
	@Api
	public int getDescent()
	{
		return PencilFontShelf.metricPixelDescent(this._font, false);
	}
	
	/**
	 * Returns the face of the font.
	 *
	 * @return The font face.
	 * @since 2018/12/13
	 */
	@Api
	public int getFace()
	{
		return this._face;
	}
	
	@Api
	public String getFamily()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the name of this font.
	 *
	 * @return The font name.
	 * @since 2018/12/01
	 */
	@Api
	public String getFontName()
	{
		return this._name;
	}
	
	/**
	 * Returns the standard height of a line for this font. This is equal
	 * to the sum of the leading, ascent, and the descent.
	 *
	 * @return The standard font height.
	 * @since 2017/10/20
	 */
	@Api
	public int getHeight()
	{
		int height = this._height;
		if (height == -1)
			this._height = (height = this.getLeading() + this.getAscent() +
				this.getDescent());
		return height;
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
	@Api
	public int getLeading()
	{
		return PencilFontShelf.metricPixelLeading(this._font);
	}
	
	@Api
	public int getMaxAscent()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMaxDescent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the size of this font in pixels.
	 *
	 * @return The font size in pixels.
	 * @since 2018/11/24
	 */
	@Api
	public int getPixelSize()
	{
		return this._pixelsize;
	}
	
	/**
	 * Returns the logical size of the font.
	 *
	 * @return The logical font size.
	 * @since 2018/11/24
	 */
	@Api
	public int getSize()
	{
		return FontUtilities.pixelSizeToLogicalSize(this._pixelsize);
	}
	
	/**
	 * Gets the style of the font.
	 *
	 * @return The style used.
	 * @since 2018/11/24
	 */
	@Api
	public int getStyle()
	{
		return this._style;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int hashCode()
	{
		return this._name.hashCode() ^
			this._style ^
			this._pixelsize;
	}
	
	@Api
	public boolean isBold()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isItalic()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isPlain()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isUnderlined()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the width of the specified string in pixels.
	 *
	 * @param __s The string to get the width.
	 * @return The width of the string in pixels.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	@Api
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
	@Api
	public int substringWidth(String __s, int __o, int __l)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 ||
			(__o + __l) > __s.length())
			throw new StringIndexOutOfBoundsException("IOOB");
		
		// Access this font always
		PencilFontBracket font = this._font;
		
		// Basic width calculation
		int totalW = 0;
		for (int i = 0, at = __o; i < __l; i++, at++)
		{
			// Get character here
			char c = __s.charAt(at);
			
			// Add to the width
			totalW += PencilFontShelf.pixelCharWidth(font, c);
		}
		
		// Give the total
		return totalW;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/25
	 */
	@Override
	@ApiStatus.Internal
	protected PencilFontBracket __squirreljmePencilFont()
	{
		return this._font;
	}
	
	@Api
	public static Font createFont(InputStream __data)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns all the fonts which are available.
	 *
	 * @return All the available fonts.
	 * @since 2018/11/24
	 */
	@Api
	public static Font[] getAvailableFonts()
	{
		// Already read these fonts?
		Font[] rv = Font._BUILTIN_FONTS;
		if (rv != null)
			return rv.clone();
		
		DisplayManager manager = DisplayManager.instance();
		
		// Obtain built-in fonts
		ScritchInterface scritch = manager.scritch();
		PencilFontBracket[] builtin = scritch.environment()
			.builtinFonts();
		
		// Wrap built-in fonts
		int n = builtin.length;
		rv = new Font[n]; 
		for (int i = 0; i < n; i++)
			rv[i] = new Font(scritch, builtin[i]);
		
		// Cache and use
		Font._BUILTIN_FONTS = rv;
		return rv.clone();
	}
	
	/**
	 * Returns all the fonts which are available on the system using the
	 * standard font size.
	 *
	 * @param __style The style of the font, may be a combination of styles.
	 * @return An array of matching font and styles.
	 * @throws IllegalArgumentException If the parameters are not correct.
	 * @since 2017/05/25
	 */
	@Api
	public static Font[] getAvailableFonts(int __style)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1v Invalid font style specified. (The style)} */
		if ((__style & ~(Font.STYLE_PLAIN | Font.STYLE_ITALIC |
			Font.STYLE_UNDERLINED | Font.STYLE_BOLD)) != 0)
			throw new IllegalArgumentException(String.format("EB1v %d",
				__style));
		
		List<Font> rv = new ArrayList<>();
		for (Font f : Font.getAvailableFonts())
			try
			{
				rv.add(f.deriveFont(__style, f.getPixelSize()));
			}
			catch (IllegalArgumentException e)
			{
			}
		
		return rv.<Font>toArray(new Font[rv.size()]);
	}
	
	/**
	 * Returns all the fonts which are available in the given format.
	 *
	 * @param __face The font face, this is a single value, one of:
	 * {@link Font#FACE_SYSTEM}, {@link Font#FACE_MONOSPACE}, or
	 * {@link Font#FACE_PROPORTIONAL}.
	 * @param __style The style of the font, this may be a combination of
	 * values, one of: {@link Font#STYLE_BOLD}, {@link Font#STYLE_ITALIC},
	 * {@link Font#STYLE_PLAIN}, or {@link Font#STYLE_UNDERLINED}.
	 * @param __pxs The pixel size of the font.
	 * @return The available fonts.
	 * @throws IllegalArgumentException If the parameters are not correct.
	 * @since 2018/11/24
	 */
	@Api
	public static Font[] getAvailableFonts(int __face, int __style, int __pxs)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1w Invalid font style specified. (The style)} */
		if ((__style & ~(Font.STYLE_PLAIN | Font.STYLE_ITALIC |
			Font.STYLE_UNDERLINED | Font.STYLE_BOLD)) != 0)
			throw new IllegalArgumentException(String.format("EB1w %d",
				__style));
		
		// Need to filter by face, then derive
		List<Font> rv = new ArrayList<>();
		for (Font f : Font.getAvailableFonts())
		{
			// Has the wrong face, ignore
			if (f.getFace() != __face)
				continue;
			
			// Derive it
			try
			{
				rv.add(f.deriveFont(__style, __pxs));
			}
			catch (IllegalArgumentException e)
			{
			}
		}
		
		return rv.<Font>toArray(new Font[rv.size()]);
	}
	
	/**
	 * Returns the default system font.
	 *
	 * @return The default system font.
	 * @since 2017/05/24
	 */
	@Api
	public static Font getDefaultFont()
	{
		// Use cached default
		Font rv = Font._DEFAULT_FONT;
		if (rv != null)
			return rv;
		
		// Use the first found font as the default
		Font._DEFAULT_FONT = (rv = Font.getAvailableFonts()[0]);
		return rv;
	}
	
	/**
	 * Returns the font by the given specifier.
	 *
	 * @param __spec The specifier of the font to get.
	 * @return The font for the given specifier.
	 * @throws IllegalArgumentException If the specifier is not valid.
	 * @since 2018/11/24
	 */
	@Api
	public static Font getFont(int __spec)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1x Invalid font specifiers. (The specifiers)} */
		if (__spec != Font.FONT_INPUT_TEXT &&
			__spec != Font.FONT_STATIC_TEXT &&
			__spec != Font.FONT_IDLE_TEXT &&
			__spec != Font.FONT_IDLE_HIGHLIGHTED_TEXT)
			throw new IllegalArgumentException("EB1x " + __spec);
		
		// This is always the default font
		return Font.getDefaultFont();
	}
	
	/**
	 * Locates a font which matches the specified parameters the closest, a
	 * font will always be returned if the parameters do not match.
	 *
	 * @param __face The font face, this is a single value, one of:
	 * {@link Font#FACE_SYSTEM}, {@link Font#FACE_MONOSPACE}, or
	 * {@link Font#FACE_PROPORTIONAL}.
	 * @param __style The style of the font, this may be a combination of
	 * values, one of: {@link Font#STYLE_BOLD}, {@link Font#STYLE_ITALIC},
	 * {@link Font#STYLE_PLAIN}, or {@link Font#STYLE_UNDERLINED}.
	 * @param __size The size of the font, this is a single value, one of:
	 * {@link Font#SIZE_SMALL}, {@link Font#SIZE_MEDIUM}, or
	 * {@link Font#SIZE_LARGE}.
	 * @return The nearest font which matches the specified parameters.
	 * @throws IllegalArgumentException If the input parameters are not valid.
	 * @since 2017/05/25
	 */
	@Api
	public static Font getFont(int __face, int __style, int __size)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1y Invalid font face specified. (The face)} */
		if ((__face & ~(Font.FACE_SYSTEM | Font.FACE_MONOSPACE |
			Font.FACE_PROPORTIONAL)) != 0 || Integer.bitCount(__face) > 1)
			throw new IllegalArgumentException(String.format("EB1y %d",
				__face));
		
		/* {@squirreljme.error EB1z Invalid font size specified. (The size)} */
		if ((__size & ~(Font.SIZE_SMALL | Font.SIZE_MEDIUM |
			Font.SIZE_LARGE)) != 0 || Integer.bitCount(__size) > 1)
			throw new IllegalArgumentException(String.format("EB1z %d",
				__size));
		
		// Get fonts that might exist
		Font[] scan = Font.getAvailableFonts(__face, __style,
			FontUtilities.logicalSizeToPixelSize(__size));
		
		// If no fonts were found, use a default font with a derived pixel
		// size as such
		if (scan.length == 0)
		{
			// Try to derive this font to the style and size, but if that
			// fails then just do the style
			Font d = Font.getDefaultFont();
			try
			{
				return d.deriveFont(__style,
					FontUtilities.logicalSizeToPixelSize(__size));
			}
			catch (IllegalArgumentException e)
			{
				try
				{
					return d.deriveFont(__style, d.getPixelSize());
				}
				catch (IllegalArgumentException f)
				{
					return d;
				}
			}
		}
		
		// Use the first font, since it should be correct hopefully
		return scan[0];
	}
	
	/**
	 * Returns a font which matches the given name.
	 *
	 * @param __name The name of the font to find.
	 * @param __style The style to use.
	 * @param __pxs The pixel size of the font.
	 * @return The font.
	 * @throws IllegalArgumentException If no font was found or the style
	 * and/or pixel size were not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/24
	 */
	@Api
	public static Font getFont(String __name, int __style, int __pxs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		Font first = null;
		Font closest = null;
		
		// Find the closest font then derive it
		for (Font f : Font.getAvailableFonts())
		{
			// Same name?
			if (__name.equals(f.getFontName()))
			{
				// First font of this name?
				if (closest == null)
					closest = f;
				
				// Closest in terms of size?
				else if (Math.abs(f.getPixelSize() - __pxs) <
					Math.abs(closest.getPixelSize() - __pxs))
					closest = f;
			}
			
			// Fallback font
			else if (first == null)
				first = f;
		}
		
		// Derive the closest font
		if (closest != null)
			return closest.deriveFont(__style, __pxs);
		
		// Or the first font?
		if (first != null)
			return first.deriveFont(__style, __pxs);
		
		/* {@squirreljme.error EB20 Could not locate a font by the given
		name. (The font name; The style; The pixel size)} */
		throw new IllegalArgumentException(__error__("EB20",
			__name, __style, __pxs));
	}
	
	/**
	 * Returns the pixel size of the given font.
	 *
	 * @param __name The name of the font.
	 * @return The pixel size of the font.
	 * @throws IllegalArgumentException If the font does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/24
	 */
	@Api
	public static int getPixelSize(String __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		for (Font f : Font.getAvailableFonts())
			if (__name.equals(f.getFontName()))
				return f.getPixelSize();
		
		/* {@squirreljme.error EB21 No font with the given name exists.
		(The font name)} */
		throw new IllegalArgumentException("EB21 " + __name);
	}
	
	/**
	 * Returns the style of the given font.
	 *
	 * @param __name The name of the font.
	 * @return The style of the font.
	 * @throws IllegalArgumentException If the font does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/24
	 */
	@Api
	public static int getStyle(String __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		for (Font f : Font.getAvailableFonts())
			if (__name.equals(f.getFontName()))
				return f.getStyle();
		
		/* {@squirreljme.error EB22 No font with the given name exists.
		(The font name)} */
		throw new IllegalArgumentException("EB2g " + __name);
	}
}


