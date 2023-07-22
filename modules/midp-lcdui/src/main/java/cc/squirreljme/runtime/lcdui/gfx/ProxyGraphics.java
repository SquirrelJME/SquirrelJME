// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This is a proxy version of {@link Graphics} where each instance can
 * refer to a single graphics instance. When a draw operation occurs, the
 * parameters of this current graphics will be set on the target.
 *
 * @see ProxyGraphicsTarget
 * @since 2022/02/25
 */
public final class ProxyGraphics
	extends Graphics
{
	/** The target graphics to draw into. */
	protected final ProxyGraphicsTarget target;
	
	/** The current alpha color. */
	private int _argbColor;
	
	/** The current blending mode. */
	private int _blendingMode;
	
	/** The clip height. */
	private int _clipHeight;
	
	/** The clip width. */
	private int _clipWidth;
	
	/** The clip X position. */
	private int _clipX;
	
	/** The clip Y position. */
	private int _clipY;
	
	/** The current font used. */
	private Font _font;
	
	/** The current stroke style. */
	private int _strokeStyle;
	
	/** The current X translation. */
	private int _transX;
	
	/** The current Y translation. */
	private int _transY;
	
	/**
	 * Initializes the proxy graphics with the given target.
	 * 
	 * @param __target The target graphics proxy.
	 * @param __width The graphics width.
	 * @param __height The graphics height.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/25
	 */
	public ProxyGraphics(ProxyGraphicsTarget __target, int __width,
		int __height)
		throws NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		this.target = __target;
		this._clipWidth = __width;
		this._clipHeight = __height;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void clipRect(int __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void copyArea(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g)
	{
		this.__graphics().copyArea(__a, __b, __c, __d, __e, __f, __g);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawArc(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		this.__graphics().drawArc(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		this.__graphics().drawARGB16(__data, __off, __scanlen, __x, __y, __w,
			__h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawChar(char __a, int __b, int __c, int __d)
	{
		this.__graphics().drawChar(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawChars(char[] __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.__graphics().drawChars(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawImage(Image __a, int __b, int __c, int __d)
	{
		this.__graphics().drawImage(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawLine(int __a, int __b, int __c, int __d)
	{
		this.__graphics().drawLine(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawRGB(int[] __a, int __b, int __c, int __d, int __e,
		int __f, int __g, boolean __h)
	{
		this.__graphics().drawRGB(__a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		this.__graphics().drawRGB16(__data, __off, __scanlen, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawRect(int __a, int __b, int __c, int __d)
	{
		this.__graphics().drawRect(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawRegion(Image __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, int __i)
	{
		this.__graphics().drawRegion(__a, __b, __c, __d, __e, __f, __g, __h,
			__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest)
	{
		this.__graphics().drawRegion(__src, __xsrc, __ysrc, __w, __h, __trans,
			__xdest, __ydest, __anch, __wdest, __hdest);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawRoundRect(int __a, int __b, int __c, int __d,
		int __e,  int __f)
	{
		this.__graphics().drawRoundRect(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawString(String __a, int __b, int __c, int __d)
	{
		this.__graphics().drawString(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawSubstring(String __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.__graphics().drawSubstring(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		this.__graphics().drawText(__t, __x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void fillArc(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		this.__graphics().fillArc(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void fillRect(int __x, int __y, int __w, int __h)
	{
		this.__graphics().fillRect(__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void fillRoundRect(int __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.__graphics().fillRoundRect(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void fillTriangle(int __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		this.__graphics().fillTriangle(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getAlpha()
	{
		return (this._argbColor >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public int getAlphaColor()
	{
		return this._argbColor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getBlendingMode()
	{
		return this._blendingMode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getBlueComponent()
	{
		return (this._argbColor) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getClipHeight()
	{
		return this._clipHeight;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getClipWidth()
	{
		return this._clipWidth;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getClipX()
	{
		return this._clipX - this._transX;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getClipY()
	{
		return this._clipY - this._transY;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getColor()
	{
		return this._argbColor & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getDisplayColor(int __a)
	{
		return this.__graphics().getDisplayColor(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public Font getFont()
	{
		return this.__font();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getGrayScale()
	{
		return (((this._argbColor >> 16) & 0xFF) +
			((this._argbColor >> 8) & 0xFF) +
			((this._argbColor) & 0xFF)) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getGreenComponent()
	{
		return (this._argbColor >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getRedComponent()
	{
		return (this._argbColor >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getStrokeStyle()
	{
		return this._strokeStyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getTranslateX()
	{
		return this._transX;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public int getTranslateY()
	{
		return this._transY;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setAlpha(int __a)
	{
		this.setAlphaColor(__a,
			this.getRedComponent(),
			this.getGreenComponent(),
			this.getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setAlphaColor(int __argb)
	{
		this._argbColor = __argb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
	{
		/* {@squirreljme.error EB2y Color out of range. (Alpha; Red; Green;
		Blue)} */
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB2y %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this.setAlphaColor((__a << 24) | (__r << 16) | (__g << 8) | __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setBlendingMode(int __m)
	{
		/* {@squirreljme.error EB2x Invalid blending mode. (The mode)} */
		if (__m != Graphics.SRC && __m != Graphics.SRC_OVER)
			throw new IllegalArgumentException("EB2x " + __m);
		
		// Cache locally
		this._blendingMode = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setClip(int __x, int __y, int __w, int __h)
	{
		// Calculate the base clip coordinates
		int startX = __x + this._transX;
		int startY = __y + this._transY;
		int endX = startX + __w;
		int endY = startY + __h;
		
		// Normalize X
		if (endX < startX)
		{
			int temp = endX;
			endX = startX;
			startX = temp;
		}
		
		// Normalize Y
		if (endY < startY)
		{
			int temp = endY;
			endY = startY;
			startY = temp;
		}
		
		// Determine the bounds of all of these
		int clipX = Math.max(0, startX);
		int clipY = Math.max(0, startY);
		int clipEndX = Math.max(0, endX);
		int clipEndY = Math.max(0, endY);
		
		// Record internally
		this._clipX = clipX;
		this._clipY = clipY;
		this._clipWidth = clipEndX - clipX;
		this._clipHeight = clipEndY - clipY;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setColor(int __rgb)
	{
		this.setAlphaColor((this.getAlphaColor() & 0xFF_000000) |
			(__rgb & 0x00_FFFFFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setColor(int __r, int __g, int __b)
	{
		this.setAlphaColor(this.getAlpha(), __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setFont(Font __font)
	{
		this.__graphics().setFont(__font);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void setGrayScale(int __v)
	{
		this.setAlphaColor(this.getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 * @param __style
	 */
	@Override
	public void setStrokeStyle(int __style)
	{
		/* {@squirreljme.error EB2z Illegal stroke style.} */
		if (__style != Graphics.SOLID && __style != Graphics.DOTTED)
			throw new IllegalArgumentException("EB2z");
		
		// Set
		this._strokeStyle = __style;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/25
	 */
	@Override
	public void translate(int __x, int __y)
	{
		// Cache locally
		this._transX += __x;
		this._transY += __y;
	}
	
	/**
	 * Returns the font that should be used.
	 * 
	 * @return The font used.
	 * @since 2022/02/25
	 */
	private Font __font()
	{
		Font rv = this._font;
		if (rv == null)
			rv = Font.getDefaultFont();
		return rv;
	}
	
	/**
	 * Initializes and returns the target graphics accordingly.
	 * 
	 * @return The resultant graphics object.
	 * @since 2022/02/25
	 */
	private Graphics __graphics()
	{
		// This is the graphics we are drawing into
		Graphics target = this.target._target;
		
		// Pass all the adjustable parameters to the target
		target.setAlphaColor(this._argbColor);
		target.setBlendingMode(this._blendingMode);
		target.setFont(this.__font());
		target.setStrokeStyle(this._strokeStyle);
		
		// Translation that is needed for clipping and translation
		int targetTransX = target.getTranslateX();
		int targetTransY = target.getTranslateY();
		
		// Set absolute clipping area
		target.setClip(
			this._clipX - targetTransX, this._clipY - targetTransY,
			this._clipWidth, this._clipHeight);
		target.translate(this._transX - targetTransX,
			this._transY - targetTransY);
		
		// Return the graphics we are drawing into
		return target;
	}
}
