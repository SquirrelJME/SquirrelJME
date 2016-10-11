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

import net.multiphasicapps.squirreljme.compat.AccessChange;
import net.multiphasicapps.squirreljme.compat.AccessType;
import net.multiphasicapps.squirreljme.compat.NowAbstract;

@NowAbstract("Public API, internally implemented. Forces implementation.")
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
	@AccessChange(from=AccessType.PRIVATE, to=AccessType.PUBLIC,
		value="So it can be used in the 'game' sub-package.")
	protected Graphics()
	{
	}
	
	@NowAbstract("No exceptional stub.")
	public abstract void clipRect(int __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void copyArea(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawArc(int __a, int __b, int __c, int __d, int __e,
		int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawChar(char __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawChars(char[] __a, int __b, int __c, int __d,
		int __e, int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawImage(Image __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawLine(int __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawRGB(int[] __a, int __b, int __c, int __d, int __e,
		int __f, int __g, boolean __h);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawRect(int __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawRegion(Image __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, int __i);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawRoundRect(int __a, int __b, int __c, int __d,
		int __e,  int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawString(String __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawSubstring(String __a, int __b, int __c, int __d,
		int __e, int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract void drawText(Text __t, int __x, int __y);
	
	@NowAbstract("No exceptional stub.")
	public abstract void fillArc(int __a, int __b, int __c, int __d, int __e,
		int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract void fillRect(int __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void fillRoundRect(int __a, int __b, int __c, int __d,
		int __e, int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract void fillTriangle(int __a, int __b, int __c, int __d,
		int __e, int __f);
	
	@NowAbstract("No exceptional stub.")
	public abstract int getAlpha();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getBlendingMode();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getBlueComponent();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getClipHeight();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getClipWidth();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getClipX();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getClipY();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getColor();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getDisplayColor(int __a);
	
	@NowAbstract("No exceptional stub.")
	public abstract Font getFont();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getGrayScale();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getGreenComponent();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getRedComponent();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getStrokeStyle();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getTranslateX();
	
	@NowAbstract("No exceptional stub.")
	public abstract int getTranslateY();
	
	@NowAbstract("No exceptional stub.")
	public abstract void setAlpha(int __a);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setAlphaColor(int __argb);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setAlphaColor(int __a, int __r, int __g, int __b);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setBlendingMode(int __m);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setClip(int __a, int __b, int __c, int __d);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setColor(int __a);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setColor(int __a, int __b, int __c);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setFont(Font __a);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setGrayScale(int __a);
	
	@NowAbstract("No exceptional stub.")
	public abstract void setStrokeStyle(int __a);
	
	@NowAbstract("No exceptional stub.")
	public abstract void translate(int __a, int __b);
}


