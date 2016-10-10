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

public abstract class Graphics
{
	public static final int BASELINE =
		64;
	
	public static final int BOTTOM =
		32;
	
	public static final int DOTTED =
		1;
	
	public static final int HCENTER =
		1;
	
	public static final int LEFT =
		4;
	
	public static final int RIGHT =
		8;
	
	public static final int SOLID =
		0;
	
	public static final int SRC =
		1;
	
	public static final int SRC_OVER =
		0;
	
	public static final int TOP =
		16;
	
	public static final int VCENTER =
		2;
	
	/**
	 * Base initialization of graphics sub-class.
	 *
	 * Note that extending this class is specific to SquirrelJME and that
	 * doing so will cause programs to only run on SquirrelJME.
	 *
	 * @since 2016/10/10
	 */
	protected Graphics()
	{
	}
	
	public abstract void clipRect(int __a, int __b, int __c, int __d);
	
	public abstract void copyArea(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g);
	
	public abstract void drawArc(int __a, int __b, int __c, int __d, int __e,
		int __f);
	
	public abstract void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h);
	
	public abstract void drawChar(char __a, int __b, int __c, int __d);
	
	public abstract void drawChars(char[] __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract void drawImage(Image __a, int __b, int __c, int __d);
	
	public abstract void drawLine(int __a, int __b, int __c, int __d);
	
	public abstract void drawRGB(int[] __a, int __b, int __c, int __d, int __e,
		int __f, int __g, boolean __h);
	
	public abstract void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h);
	
	public abstract void drawRect(int __a, int __b, int __c, int __d);
	
	public abstract void drawRegion(Image __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, int __i);
	
	public abstract void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest);
	
	public abstract void drawRoundRect(int __a, int __b, int __c, int __d,
		int __e,  int __f);
	
	public abstract void drawString(String __a, int __b, int __c, int __d);
	
	public abstract void drawSubstring(String __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract void drawText(Text __t, int __x, int __y);
	
	public abstract void fillArc(int __a, int __b, int __c, int __d, int __e,
		int __f);
	
	public abstract void fillRect(int __a, int __b, int __c, int __d);
	
	public abstract void fillRoundRect(int __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract void fillTriangle(int __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract int getAlpha();
	
	public abstract int getBlendingMode();
	
	public abstract int getBlueComponent();
	
	public abstract int getClipHeight();
	
	public abstract int getClipWidth();
	
	public abstract int getClipX();
	
	public abstract int getClipY();
	
	public abstract int getColor();
	
	public abstract int getDisplayColor(int __a);
	
	public abstract Font getFont();
	
	public abstract int getGrayScale();
	
	public abstract int getGreenComponent();
	
	public abstract int getRedComponent();
	
	public abstract int getStrokeStyle();
	
	public abstract int getTranslateX();
	
	public abstract int getTranslateY();
	
	public abstract void setAlpha(int __a);
	
	public abstract void setAlphaColor(int __argb);
	
	public abstract void setAlphaColor(int __a, int __r, int __g, int __b);
	
	public abstract void setBlendingMode(int __m);
	
	public abstract void setClip(int __a, int __b, int __c, int __d);
	
	public abstract void setColor(int __a);
	
	public abstract void setColor(int __a, int __b, int __c);
	
	public abstract void setFont(Font __a);
	
	public abstract void setGrayScale(int __a);
	
	public abstract void setStrokeStyle(int __a);
	
	public abstract void translate(int __a, int __b);
}


