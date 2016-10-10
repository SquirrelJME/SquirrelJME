// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This forwards all calls to the specified graphics object, this is used to
 * prevent malicious classes from changing the graphics canvases and items
 * draw to.
 *
 * @since 2016/10/10
 */
class __PlainForward__
	extends Graphics
{
	/** The graphics to forward to. */
	protected final Graphics graphics;
	
	/**
	 * Initializes the forwarder.
	 *
	 * @param __g The graphics to draw to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/10
	 */
	__PlainForward__(Graphics __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.graphics = __g;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void clipRect(int __a, int __b, int __c, int __d)
	{
		this.graphics.clipRect(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void copyArea(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g)
	{
		this.graphics.copyArea(__a, __b, __c, __d, __e, __f, __g);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawArc(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		this.graphics.drawArc(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		this.graphics.drawARGB16(__data, __off, __scanlen, __x, __y, __w,
			__h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawChar(char __a, int __b, int __c, int __d)
	{
		this.graphics.drawChar(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawChars(char[] __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.graphics.drawChars(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawImage(Image __a, int __b, int __c, int __d)
	{
		this.graphics.drawImage(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawLine(int __a, int __b, int __c, int __d)
	{
		this.graphics.drawLine(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRGB(int[] __a, int __b, int __c, int __d, int __e,
		int __f, int __g, boolean __h)
	{
		this.graphics.drawRGB(__a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		this.graphics.drawRGB16(__data, __off, __scanlen, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRect(int __a, int __b, int __c, int __d)
	{
		this.graphics.drawRect(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRegion(Image __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, int __i)
	{
		this.graphics.drawRegion(__a, __b, __c, __d, __e, __f, __g, __h,
			__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest)
	{
		this.graphics.drawRegion(__src, __xsrc, __ysrc, __w, __h, __trans,
			__xdest, __ydest, __anch, __wdest, __hdest);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRoundRect(int __a, int __b, int __c, int __d,
		int __e,  int __f)
	{
		this.graphics.drawRoundRect(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawString(String __a, int __b, int __c, int __d)
	{
		this.graphics.drawString(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawSubstring(String __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.graphics.drawSubstring(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		this.graphics.drawText(__t, __x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillArc(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		this.graphics.fillArc(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillRect(int __a, int __b, int __c, int __d)
	{
		this.graphics.fillRect(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillRoundRect(int __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.graphics.fillRoundRect(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillTriangle(int __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.graphics.fillTriangle(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getAlpha()
	{
		return this.graphics.getAlpha();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getBlendingMode()
	{
		return this.graphics.getBlendingMode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getBlueComponent()
	{
		return this.graphics.getBlueComponent();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipHeight()
	{
		return this.graphics.getClipHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipWidth()
	{
		return this.graphics.getClipWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipX()
	{
		return this.graphics.getClipX();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipY()
	{
		return this.graphics.getClipY();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getColor()
	{
		return this.graphics.getColor();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getDisplayColor(int __a)
	{
		return this.graphics.getDisplayColor(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public Font getFont()
	{
		return this.graphics.getFont();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getGrayScale()
	{
		return this.graphics.getGrayScale();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getGreenComponent()
	{
		return this.graphics.getGreenComponent();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getRedComponent()
	{
		return this.graphics.getRedComponent();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getStrokeStyle()
	{
		return this.graphics.getStrokeStyle();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getTranslateX()
	{
		return this.graphics.getTranslateX();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getTranslateY()
	{
		return this.graphics.getTranslateY();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setAlpha(int __a)
	{
		this.graphics.setAlpha(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setAlphaColor(int __argb)
	{
		this.graphics.setAlphaColor(__argb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
	{
		this.graphics.setAlphaColor(__a, __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setBlendingMode(int __m)
	{
		this.graphics.setBlendingMode(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setClip(int __a, int __b, int __c, int __d)
	{
		this.graphics.setClip(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setColor(int __a)
	{
		this.graphics.setColor(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setColor(int __a, int __b, int __c)
	{
		this.graphics.setColor(__a, __b, __c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setFont(Font __a)
	{
		this.graphics.setFont(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setGrayScale(int __a)
	{
		this.graphics.setGrayScale(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setStrokeStyle(int __a)
	{
		this.graphics.setStrokeStyle(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void translate(int __a, int __b)
	{
		this.graphics.translate(__a, __b);
	}
}

