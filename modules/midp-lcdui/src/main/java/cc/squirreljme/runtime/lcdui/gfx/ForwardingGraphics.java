// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This forwards graphical calls to the specified sub-class.
 *
 * This may be used to change for example the image to draw onto which will
 * have to be recreated if a canvas changes size.
 *
 * @since 2016/10/10
 */
public class ForwardingGraphics
	extends Graphics
{
	/** The graphics to forward to. */
	private Graphics _graphics;
	
	/** The blend mode. */
	private int _blendmode;

	/** The color. */
	private int _color;

	/** The font. */
	private Font _font;

	/** The clipping rectangle start. */
	private int _clipx, _clipy;
	
	/** The clip width. */
	private int _clipw =
		Integer.MAX_VALUE;
	
	/** The clip height. */
	private int _cliph =
		Integer.MAX_VALUE;

	/** The stroke. */
	private int _stroke;

	/** The translation coordinates. */
	private int _translatex, _translatey;
	
	/** Plain forwarder. */
	private Reference<Graphics> _plain;

	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void clipRect(int __x, int __y, int __w, int __h)
	{
		setClip(Math.max(__x, getClipX()),
			Math.max(__y, getClipY()),
			Math.min(__w, getClipWidth()),
			Math.min(__h, getClipHeight()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void copyArea(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.copyArea(__a, __b, __c, __d, __e, __f, __g);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawArc(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawArc(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawARGB16(__data, __off, __scanlen, __x, __y, __w,
			__h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawChar(char __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawChar(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawChars(char[] __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawChars(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawImage(Image __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawImage(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawLine(int __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawLine(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRGB(int[] __a, int __b, int __c, int __d, int __e,
		int __f, int __g, boolean __h)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawRGB(__a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawRGB16(__data, __off, __scanlen, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRect(int __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawRect(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawRegion(Image __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, int __i)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawRegion(__a, __b, __c, __d, __e, __f, __g, __h,
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
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawRegion(__src, __xsrc, __ysrc, __w, __h, __trans,
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
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawRoundRect(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawString(String __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawString(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawSubstring(String __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawSubstring(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.drawText(__t, __x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillArc(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.fillArc(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillRect(int __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.fillRect(__a, __b, __c, __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillRoundRect(int __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.fillRoundRect(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void fillTriangle(int __a, int __b, int __c, int __d,
		int __e, int __f)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.fillTriangle(__a, __b, __c, __d, __e, __f);
	}
	
	/**
	 * Sets the graphics to forward to.
	 *
	 * The current graphical attributes will be copies to the target graphics
	 * if it has been set.
	 *
	 * @param __g Forwards to these graphics, {@code null} clears the
	 * forwarding.
	 * @since 2016/10/10
	 */
	public void forwardGraphics(Graphics __g)
	{
		// Set new
		this._graphics = __g;
		
		// Set all drawing state, so it appears seamless
		if (__g != null)
		{
			__g.setBlendingMode(this._blendmode);
			__g.setAlphaColor(this._color);
			__g.setFont(this._font);
			__g.setClip(this._clipx, this._clipy, this._clipw, this._clipy);
			__g.setStrokeStyle(this._stroke);
			__g.translate(this._translatex - __g.getTranslateX(),
				this._translatey - __g.getTranslateY());
		}
	}
	
	/**
	 * Creates a graphics instance which just forwards to this forwarder.
	 *
	 * @return The graphics which forwards to this.
	 * @since 2016/10/10
	 */
	public Graphics forwardPlainGraphics()
	{
		Reference<Graphics> ref = this._plain;
		Graphics rv;
		
		// Initialize?
		if (ref == null || null == (rv = ref.get()))
			this._plain = new WeakReference<>(
				rv = new BasicForwardingGraphics(this));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getAlpha()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getAlpha();
		
		// Stored?
		return (this._color >>> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public int getAlphaColor()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getAlpha();
		
		// Stored?
		return this._color;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getBlendingMode()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getBlendingMode();
		
		// Stored?
		return this._blendmode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getBlueComponent()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getBlueComponent();
		
		// Stored?
		return this._color & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipHeight()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getClipHeight();
		
		// Stored?
		return this._cliph;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipWidth()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getClipWidth();
		
		// Stored?
		return this._clipw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipX()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getClipX();
		
		// Stored?
		return this._clipx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getClipY()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getClipY();
		
		// Stored?
		return this._clipy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getColor()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getColor();
		
		// Stored?
		return this._color & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getDisplayColor(int __a)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getDisplayColor(__a);
		
		// Since no graphics is bound, this is unknown so return the original
		// value
		return __a & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public Font getFont()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getFont();
		
		// Stored?
		return this._font;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getGrayScale()
	{
		// Average all channels
		return ((getRedComponent() & 0xFF) +
			(getGreenComponent() & 0xFF) +
			(getBlueComponent() & 0xFF)) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getGreenComponent()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getGreenComponent();
		
		// Stored?
		return (this._color >>> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getRedComponent()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getRedComponent();
		
		// Stored?
		return (this._color >>> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getStrokeStyle()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getStrokeStyle();
		
		// Stored?
		return this._stroke;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getTranslateX()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getTranslateX();
		
		// Stored?
		return this._translatex;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public int getTranslateY()
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			return graphics.getTranslateY();
		
		// Stored?
		return this._translatey;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setAlpha(int __a)
	{
		setAlphaColor(__a, getRedComponent(), getGreenComponent(),
			getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setAlphaColor(int __argb)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.setAlphaColor(__argb);
		
		// Store
		this._color = __argb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
	{
		setAlphaColor(((__a & 0xFF) << 24) |
			((__r & 0xFF) << 16) |
			((__g & 0xFF) << 8) |
			(__b & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setBlendingMode(int __m)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.setBlendingMode(__m);
		
		// Set
		this._blendmode = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setClip(int __a, int __b, int __c, int __d)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.setClip(__a, __b, __c, __d);
		
		// Set
		this._clipx = __a;
		this._clipy = __b;
		this._clipw = __c;
		this._cliph = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setColor(int __a)
	{
		setAlphaColor((__a & 0xFFFFFF) | ((getAlpha() & 0xFF) << 24));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setColor(int __a, int __b, int __c)
	{
		setAlphaColor(getAlpha(), __a, __b, __c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setFont(Font __a)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.setFont(__a);
		
		// Store
		this._font = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setGrayScale(int __a)
	{
		setColor(__a, __a, __a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void setStrokeStyle(int __a)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.setStrokeStyle(__a);
		
		// Store
		this._stroke = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void translate(int __a, int __b)
	{
		Graphics graphics = this._graphics;
		if (graphics != null)
			graphics.translate(__a, __b);
		
		// Store
		this._translatex = getTranslateX();
		this._translatey = getTranslateY();
	}
}

