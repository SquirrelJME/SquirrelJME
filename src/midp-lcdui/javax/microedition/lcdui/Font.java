// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.InputStream;
import java.io.IOException;

public final class Font
{
	public static final int FACE_MONOSPACE =
		32;
	
	public static final int FACE_PROPORTIONAL =
		64;
	
	public static final int FACE_SYSTEM =
		0;
	
	public static final int FONT_INPUT_TEXT =
		1;
	
	public static final int FONT_STATIC_TEXT =
		0;
	
	public static final int FONT_IDLE_TEXT =
		2;
	
	public static final int FONT_IDLE_HIGHLIGHTED_TEXT =
		3;
	
	public static final int SIZE_LARGE =
		16;
	
	public static final int SIZE_MEDIUM =
		0;
	
	public static final int SIZE_SMALL =
		8;
	
	public static final int STYLE_BOLD =
		1;
	
	public static final int STYLE_ITALIC =
		2;
	
	public static final int STYLE_PLAIN =
		0;
	
	public static final int STYLE_UNDERLINED =
		4;
	
	private Font()
	{
		super();
		throw new Error("TODO");
	}
	
	public int charWidth(char __a)
	{
		throw new Error("TODO");
	}
	
	public int charsWidth(char[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public Font deriveFont(int __pxs)
	{
		throw new Error("TODO");
	}
	
	public Font deriveFont(int __style, int __pxs)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	public int getAscent()
	{
		throw new Error("TODO");
	}
	
	public int getBaselinePosition()
	{
		throw new Error("TODO");
	}
	
	public int getDescent()
	{
		throw new Error("TODO");
	}
	
	public int getFace()
	{
		throw new Error("TODO");
	}
	
	public String getFamily()
	{
		throw new Error("TODO");
	}
	
	public int getHeight()
	{
		throw new Error("TODO");
	}
	
	public int getLeading()
	{
		throw new Error("TODO");
	}
	
	public int getMaxAscent()
	{
		throw new Error("TODO");
	}
	
	public int getMaxDescent()
	{
		throw new Error("TODO");
	}
	
	public int getPixelSize()
	{
		throw new Error("TODO");
	}
	
	public int getSize()
	{
		throw new Error("TODO");
	}
	
	public int getStyle()
	{
		throw new Error("TODO");
	}
	
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public boolean isBold()
	{
		throw new Error("TODO");
	}
	
	public boolean isItalic()
	{
		throw new Error("TODO");
	}
	
	public boolean isPlain()
	{
		throw new Error("TODO");
	}
	
	public boolean isUnderlined()
	{
		throw new Error("TODO");
	}
	
	public int stringWidth(String __a)
	{
		throw new Error("TODO");
	}
	
	public int substringWidth(String __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static Font createFont(InputStream __data)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public static Font[] getAvailableFonts()
	{
		throw new Error("TODO");
	}
	
	public static Font[] getAvailableFonts(int __style)
	{
		throw new Error("TODO");
	}
	
	public static Font[] getAvailableFonts(int __face, int __style, int __pxs)
	{
		throw new Error("TODO");
	}
	
	public static Font getDefaultFont()
	{
		throw new Error("TODO");
	}
	
	public static Font getFont(int __a)
	{
		throw new Error("TODO");
	}
	
	public static Font getFont(int __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static Font getFont(String __name, int __style, int __pxs)
	{
		throw new Error("TODO");
	}
	
	public static int getPixelSize(String __name)
	{
		throw new Error("TODO");
	}
	
	public static int getStyle(String __name)
	{
		throw new Error("TODO");
	}
}


