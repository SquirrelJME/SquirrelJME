// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.PencilShelfError;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;
import javax.microedition.lcdui.game.Sprite;

/**
 * This delegates drawing operations to the hardware graphics layer.
 * 
 * This utilizes both {@link PencilShelf} and {@link PencilBracket} for native
 * graphics.
 *
 * @since 2020/09/25
 */
public final class PencilGraphics
	extends Graphics
{
	/** The hardware bracket reference. */
	protected final PencilBracket hardware;
	
	/** Surface width. */
	protected final int surfaceW;
	
	/** Surface height. */
	protected final int surfaceH;
	
	/** Is there an alpha channel? */
	protected final boolean hasAlpha;
	
	/** Single character. */
	private final char[] _singleChar =
		new char[1];
	
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
	 * Initializes the pencil graphics system.
	 *
	 * @param __sw The surface width.
	 * @param __sh The surface height.
	 * @param __hardware The hardware bracket reference for drawing.
	 * @throws IllegalArgumentException If hardware graphics are not capable
	 * enough to be used at all.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/25
	 */
	private PencilGraphics(int __sw, int __sh, PencilBracket __hardware)
		throws IllegalArgumentException, NullPointerException
	{
		if (__hardware == null)
			throw new NullPointerException("NARG");
		
		this.hardware = __hardware;
		
		// These are used to manage the clip
		this.surfaceW = __sw;
		this.surfaceH = __sh;
		
		// Determines which blending modes are valid
		this.hasAlpha = PencilShelf.hardwareHasAlpha(__hardware);
		
		// Set initial parameters for the graphics and make sure they are
		// properly forwarded as well
		this.setAlphaColor(0xFF000000);
		this.setBlendingMode(Graphics.SRC_OVER);
		this.setStrokeStyle(Graphics.SOLID);
		this.setFont(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void clipRect(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void copyArea(int __sx, int __sy, int __w, int __h, int __dx,
		int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		// Forward to native call
		try
		{
			PencilShelf.hardwareCopyArea(this.hardware,
				__sx, __sy, __w, __h, __dx, __dy, __anchor);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			switch (e.distinction)
			{
				case PencilShelfError.ILLEGAL_ARGUMENT:
					throw new IllegalArgumentException(e.getMessage(), e);
					
				case PencilShelfError.ILLEGAL_STATE:
					throw new IllegalStateException(e.getMessage(), e);
			}
			
			// No distinction
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawArc(int __x, int __y, int __w, int __h, int __startAngle,
	 int __arcAngle)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		// Fill single character first
		char[] singleChar = this._singleChar;
		singleChar[0] = __s;
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawChars(this.hardware,
				singleChar, 0, 1, __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			RuntimeException x;
			switch (e.distinction)
			{
				case PencilShelfError.ILLEGAL_ARGUMENT:
					throw new IllegalArgumentException(e.getMessage(), e);
			}
			
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawChars(char[] __s, int __o, int __l, int __x, int __y,
		int __anchor)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __s.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawChars(this.hardware,
				__s, __o, __l, __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			RuntimeException x;
			switch (e.distinction)
			{
				case PencilShelfError.INDEX_OUT_OF_BOUNDS:
					x = new IndexOutOfBoundsException(e.getMessage());
					x.initCause(e);
					throw x;
					
				case PencilShelfError.ILLEGAL_ARGUMENT:
					throw new IllegalArgumentException(e.getMessage(), e);
			}
			
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		// This is a duplicate function, so it gets forwarded
		this.drawRegion(__i, 0, 0,
			__i.getWidth(), __i.getHeight(), 0,
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		PencilShelf.hardwareDrawLine(this.hardware, __x1, __y1, __x2, __y2);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawRGB(int[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		// Forward Call
		this.__drawRegion(__data, __off, __scanlen, __alpha,
			0, 0, __w, __h, Sprite.TRANS_NONE,
			__x, __y, Graphics.TOP | Graphics.LEFT, __w, __h,
			__scanlen, (__data.length - __off) / __scanlen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		// Forward to hardware
		PencilShelf.hardwareDrawRect(this.hardware, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		// Forward call
		this.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc,
			__trans, __xdest, __ydest, __anch, __wsrc, __hsrc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// If the image is direct, use the buffer that is inside rather than
		// a copy, so we do not waste time copying from it!
		int[] buf;
		int offset;
		int scanLen;
		if (__src.squirreljmeIsDirect())
		{
			buf = __src.squirreljmeDirectRGBInt();
			offset = __src.squirreljmeDirectOffset();
			scanLen = __src.squirreljmeDirectScanLen();
		}
		
		// Image is not directly accessible, so get a copy of it
		else
		{
			// Obtain image properties
			int iW = __src.getWidth();
			int iH = __src.getHeight();
			int totalPixels = iW * iH;
			
			// Read RGB data
			buf = new int[totalPixels];
			offset = 0;
			scanLen = iW;
			__src.getRGB(buf, offset, scanLen, 0, 0, iW, iH);
		}
		
		// Perform the internal draw
		this.__drawRegion(buf, offset, scanLen, __src.hasAlpha(),
			__xsrc, __ysrc, __wsrc, __hsrc, __trans,
			__xdest, __ydest, __anch,
			__wdest, __hdest, __src.getWidth(), __src.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawString(String __s, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawSubstring(this.hardware,
				__s, 0, __s.length(), __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			RuntimeException x;
			switch (e.distinction)
			{
				case PencilShelfError.ILLEGAL_ARGUMENT:
					throw new IllegalArgumentException(e.getMessage(), e);
			}
			
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawSubstring(String __s, int __o, int __l,
		int __x, int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __s.length())
			throw new StringIndexOutOfBoundsException("IOOB");
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawSubstring(this.hardware,
				__s, __o, __l, __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			RuntimeException x;
			switch (e.distinction)
			{
				case PencilShelfError.ILLEGAL_ARGUMENT:
					throw new IllegalArgumentException(e.getMessage(), e);
			}
			
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void fillArc(int __x, int __y, int __w, int __h, int __startAngle,
	 int __arcAngle)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void fillRect(int __x, int __y, int __w, int __h)
	{
		// Forward to hardware
		PencilShelf.hardwareFillRect(this.hardware, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		// Forward to hardware
		PencilShelf.hardwareFillTriangle(this.hardware, __x1, __y1, __x2, __y2,
			__x3, __y3);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getAlpha()
	{
		return (this._argbColor >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getAlphaColor()
	{
		return this._argbColor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getBlendingMode()
	{
		return this._blendingMode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getBlueComponent()
	{
		return (this._argbColor) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getClipHeight()
	{
		return this._clipHeight;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getClipWidth()
	{
		return this._clipWidth;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getClipX()
	{
		return this._clipX - this._transX;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getClipY()
	{
		return this._clipY - this._transY;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getColor()
	{
		return this._argbColor & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getDisplayColor(int __rgb)
	{
		throw Debugging.todo();
		/*// We can just ask the software graphics for the color we are using
		// since it should hopefully match the hardware one.
		return this.software.getDisplayColor(__rgb);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public Font getFont()
	{
		return this._font;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
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
	 * @since 2020/09/25
	 */
	@Override
	public int getGreenComponent()
	{
		return (this._argbColor >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getRedComponent()
	{
		return (this._argbColor >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getStrokeStyle()
	{
		return this._strokeStyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getTranslateX()
	{
		return this._transX;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public int getTranslateY()
	{
		return this._transY;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setAlpha(int __a)
		throws IllegalArgumentException
	{
		this.setAlphaColor(__a,
			this.getRedComponent(),
			this.getGreenComponent(),
			this.getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setAlphaColor(int __argb)
	{
		// Mirror locally
		this._argbColor = __argb;
		
		// Set on the hardware side
		PencilShelf.hardwareSetAlphaColor(this.hardware, __argb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3t Color out of range. (Alpha; Red; Green;
		Blue)} */
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB3t %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this.setAlphaColor((__a << 24) | (__r << 16) | (__g << 8) | __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3u Invalid blending mode. (The mode)} */
		if ((__m != Graphics.SRC && __m != Graphics.SRC_OVER) ||
			(__m == Graphics.SRC && !this.hasAlpha))
			throw new IllegalArgumentException("EB3u " + __m);
		
		// Cache locally
		this._blendingMode = __m;
		
		// Forward to both software and hardware graphics
		PencilShelf.hardwareSetBlendingMode(this.hardware, __m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
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
		int clipX = Math.min(this.surfaceW, Math.max(0, startX));
		int clipY = Math.min(this.surfaceH, Math.max(0, startY));
		int clipEndX = Math.min(this.surfaceW, Math.max(0, endX));
		int clipEndY = Math.min(this.surfaceH, Math.max(0, endY));
		
		// Record internally
		this._clipX = clipX;
		this._clipY = clipY;
		this._clipWidth = clipEndX - clipX;
		this._clipHeight = clipEndY - clipY;
		
		// Forward to both software and hardware graphics
		PencilShelf.hardwareSetClip(this.hardware, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@SuppressWarnings("MagicNumber")
	@Override
	public void setColor(int __rgb)
	{
		this.setAlphaColor((this.getAlphaColor() & 0xFF_000000) |
			(__rgb & 0x00_FFFFFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		this.setAlphaColor(this.getAlpha(), __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setFont(Font __font)
	{
		// Cache locally
		this._font = __font;
		
		// Clearing the font?
		if (__font == null)
			PencilShelf.hardwareSetDefaultFont(this.hardware);
		
		// Set font natively from the font details
		else
			PencilShelf.hardwareSetFont(this.hardware,
				((PencilFontProvider)__font).__squirreljmePencilFont());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setGrayScale(int __v)
	{
		this.setAlphaColor(this.getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void setStrokeStyle(int __style)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3v Illegal stroke style.} */
		if (__style != Graphics.SOLID && __style != Graphics.DOTTED)
			throw new IllegalArgumentException("EB3v");
		
		// Set
		this._strokeStyle = __style;
		
		// Forward to both software and hardware graphics
		PencilShelf.hardwareSetStrokeStyle(this.hardware, __style);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public void translate(int __x, int __y)
	{
		// Set locally
		this._transX += __x;
		this._transY += __y;
		
		// Forward to both software and hardware graphics
		PencilShelf.hardwareTranslate(this.hardware, __x, __y);
	}
	
	/**
	 * Draws a direct RGB region of an image.
	 * 
	 * @param __data The source buffer.
	 * @param __off The offset into the buffer.
	 * @param __scanlen The scanline length.
	 * @param __alpha Drawing with the alpha channel?
	 * @param __xsrc The source X position.
	 * @param __ysrc The source Y position.
	 * @param __wsrc The width of the source region.
	 * @param __hsrc The height of the source region.
	 * @param __trans Sprite translation and/or rotation, see {@link Sprite}.
	 * @param __xdest The destination X position, is translated.
	 * @param __ydest The destination Y position, is translated.
	 * @param __anch The anchor point.
	 * @param __wdest The destination width.
	 * @param __hdest The destination height.
	 * @param __origImgWidth Original image width.
	 * @param __origImgHeight Original image height.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/01/26
	 */
	private void __drawRegion(int[] __data, int __off, int __scanlen,
		boolean __alpha, int __xsrc, int __ysrc, int __wsrc, int __hsrc,
		int __trans, int __xdest, int __ydest, int __anch, int __wdest,
		int __hdest, int __origImgWidth, int __origImgHeight)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Forward to the native region drawing method
		PencilShelf.hardwareDrawXRGB32Region(this.hardware,
			__data, __off, __scanlen,
			__alpha, __xsrc, __ysrc, __wsrc, __hsrc,
			__trans, __xdest, __ydest, __anch,
			__wdest, __hdest, __origImgWidth, __origImgHeight);
	}
	
	/**
	 * Creates a graphics that is capable of drawing on hardware if it is
	 * supported, but falling back to software level graphics.
	 * 
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __offset The offset to the start of the buffer.
	 * @param __pal The color palette, may be {@code null}. 
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/25
	 */
	public static Graphics hardwareGraphics(int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh)
		throws NullPointerException
	{
		throw Debugging.todo();
		/*
		// Setup software graphics
		Graphics software = SoftwareGraphicsFactory.softwareGraphics(__pf,
			__bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh);
		
		// Get the capabilities of the native system, if it is not supported
		// then operations will purely be implemented in software
		// It can also be disabled via a system property
		int caps = PencilShelf.capabilities(__pf); 
		if (PencilGraphics._IS_FORCE_SOFTWARE ||
			(caps & PencilCapabilities.MINIMUM) == 0)
			return software;
		
		return new PencilGraphics(caps, software, __sw, __sh,
			PencilShelf.hardwareGraphics(__pf,
			__bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh), __pf);
		 */
	}
	
	/**
	 * Initializes a new graphics interface.
	 *
	 * @param __hw The hardware graphics to use.
	 * @param __sw The surface width.
	 * @param __sh The surface height.
	 * @return The wrapped graphics.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/12
	 */
	public static Graphics of(ScritchPencilBracket __hw, int __sw, int __sh)
		throws NullPointerException
	{
		if (__hw == null)
			throw new NullPointerException("NARG");
		
		return new PencilGraphics(__sw, __sh, __hw);
	}
}
