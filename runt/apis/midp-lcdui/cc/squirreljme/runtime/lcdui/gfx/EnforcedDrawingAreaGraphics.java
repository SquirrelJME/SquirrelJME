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

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This is a graphics which enforces a translation and maximum drawing bounds
 * to a target graphics object.
 *
 * @since 2018/12/02
 */
public final class EnforcedDrawingAreaGraphics
	extends Graphics
{
	/** The graphics to draw into. */
	protected final Graphics graphics;
	
	/** The X translation. */
	protected final int x;
	
	/** The Y translation. */
	protected final int y;
	
	/** The width. */
	protected final int width;
	
	/** The height. */
	protected final int height;
	
	/** Draw X actual. */
	private int _dx;
	
	/** Draw Y actual. */
	private int _dy;
	
	/** Current X translation. */
	private int _transx;
	
	/** Current Y translation. */
	private int _transy;
	
	/** Clip X. */
	private int _clipx;
	
	/** Clip Y. */
	private int _clipy;
	
	/** Clip Width. */
	private int _clipw;
	
	/** Clip Height. */
	private int _cliph;
	
	/** Has this been initialized? */
	private boolean _beeninit;
	
	/**
	 * Initializes the enforced drawing area.
	 *
	 * @param __g The graphics to draw to.
	 * @param __x The X translation.
	 * @param __y The Y translation.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public EnforcedDrawingAreaGraphics(Graphics __g, int __x, int __y,
		int __w, int __h)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this.graphics = __g;
		this.x = __x;
		this.y = __y;
		this.width = __w;
		this.height = __h;
		
		// Initialize our translation with our offsets
		this._transx = 0;
		this._transy = 0;
		this._dx = __x;
		this._dy = __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.copyArea(this._dx + __sx, this._dy + __sy,
			__w, __h, this._dx + __dx, this._dy + __dy, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawArc(this._dx + __x, this._dy + __y,
			__w, __h, __sa, __aa);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawARGB16(__data, __off, __scanlen,
			this._dx + __x, this._dy + __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawChar(__s, this._dx + __x, this._dy + __y,
			__anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawChars(__s, __o, __l,
			this._dx + __x, this._dy + __y,
			__anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawImage(__i, this._dx + __x, this._dy + __y,
			__anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawLine(this._dx + __x1, this._dy + __y1,
			this._dx + __x2, this._dy + __y2);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawRGB(__data, __off, __scanlen,
			this._dx + __x, this._dy + __y, __w, __h, __alpha);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawRGB16(__data, __off, __scanlen,
			this._dx + __x, this._dy + __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawRect(this._dx + __x, this._dy + __y,
			__w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc,
			__trans, this._dx + __xdest, this._dy + __ydest, __anch);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc,
			__trans, this._dx + __xdest, this._dy + __ydest, __anch,
			__wdest, __hdest);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawRoundRect(this._dx + __x, this._dy + __y,
			__w, __h, __aw, __ah);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawString(String __s, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawString(__s, this._dx + __x, this._dy + __y,
			__anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawSubstring(__s, __o, __l,
			this._dx + __x, this._dy + __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.drawText(__t, this._dx + __x, this._dy + __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.fillArc(this._dx + __x, this._dy + __y,
			__w, __h, __sa, __aa);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void fillRect(int __x, int __y, int __w, int __h)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.fillRect(this._dx + __x, this._dy + __y,
			__w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		this.graphics.fillRoundRect(this._dx + __x, this._dy + __y,
			__w, __h, __aw, __ah);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		int x = this._transx, y = this._transy;
		this.graphics.fillTriangle(
			x + __x1, y + __y1,
			x + __x2, y + __y2,
			x + __x3, y + __y3);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getAlpha()
	{
		return this.graphics.getAlpha();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getAlphaColor()
	{
		return this.graphics.getAlphaColor();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getBlendingMode()
	{
		return this.graphics.getBlendingMode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getBlueComponent()
	{
		return this.graphics.getBlueComponent();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getClipHeight()
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		return this._cliph;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getClipWidth()
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		return this._clipw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getClipX()
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		return this._clipx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getClipY()
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		return this._clipy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getColor()
	{
		return this.graphics.getColor();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getDisplayColor(int __rgb)
	{
		return this.graphics.getDisplayColor(__rgb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final Font getFont()
	{
		return this.graphics.getFont();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getGrayScale()
	{
		return this.graphics.getGrayScale();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getGreenComponent()
	{
		return this.graphics.getGreenComponent();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getRedComponent()
	{
		return this.graphics.getRedComponent();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getStrokeStyle()
	{
		return this.graphics.getStrokeStyle();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getTranslateX()
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		return this._transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final int getTranslateY()
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		return this._transy;
	}
	
	/**
	 * Reclips the clipping area and sets up translation.
	 *
	 * @since 2018/12/02
	 */
	public final void initialize()
	{
		Graphics g = this.graphics;
		
		// Our viewing area
		int x = this.x,
			y = this.y,
			width = this.width,
			height = this.height;
		
		// Set new clipping area
		g.clipRect(x, y, width, height);
		this._clipx = 0;
		this._clipy = 0;
		this._clipw = width;
		this._cliph = height;
		
		// Reset translation and draw position
		this._dx = x;
		this._dy = y;
		this._transx = 0;
		this._transy = 0;
		
		// Set as initialized
		this._beeninit = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setAlpha(int __a)
		throws IllegalArgumentException
	{
		this.graphics.setAlpha(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setAlphaColor(int __argb)
	{
		this.graphics.setAlphaColor(__argb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		this.graphics.setAlphaColor(__a, __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		this.graphics.setBlendingMode(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setClip(int __x, int __y, int __w, int __h)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setColor(int __rgb)
	{
		this.graphics.setColor(__rgb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		this.graphics.setColor(__r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setFont(Font __f)
	{
		this.graphics.setFont(__f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		this.graphics.setGrayScale(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void setStrokeStyle(int __s)
		throws IllegalArgumentException
	{
		this.graphics.setStrokeStyle(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		// Initialize?
		if (!this._beeninit)
			this.initialize();
		
		// Add our translation, we manage it ourselves
		int transx = this._transx + __x,
			transy = this._transy + __y;
		
		// Set our own translation but not to the target
		this._transx = transx;
		this._transy = transy;
		
		// Our draw offset is our offset and our own translation
		this._dx = this.x + transx;
		this._dy = this.y + transy;
	}
}

