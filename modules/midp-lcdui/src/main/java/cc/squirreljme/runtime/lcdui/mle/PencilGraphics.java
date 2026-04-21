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
import cc.squirreljme.jvm.mle.constants.PencilBlendingMode;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.gfx.ExtraGraphics;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayManager;
import cc.squirreljme.runtime.midlet.MeepRuntime;
import java.io.Closeable;
import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;
import javax.microedition.lcdui.game.Sprite;
import org.jetbrains.annotations.NotNull;

/**
 * This delegates drawing operations to the hardware graphics layer.
 * 
 * This utilizes both {@link PencilShelf} and {@link PencilBracket} for native
 * graphics.
 *
 * @since 2020/09/25
 */
@SquirrelJMEVendorApi
public final class PencilGraphics
	extends Graphics
	implements Closeable, ExtraGraphics
{
	/** The hardware bracket reference. */
	@SquirrelJMEVendorApi
	protected final PencilBracket hardware;

	/** Surface width. */
	@SquirrelJMEVendorApi
	protected final int surfaceW;

	/** Surface height. */
	@SquirrelJMEVendorApi
	protected final int surfaceH;

	/** Is there an alpha channel? */
	@SquirrelJMEVendorApi
	protected final boolean hasAlpha;
	
	/** The current pixel format. */
	private int _pixelFormat;

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

	/** Has this been closed? */
	private volatile boolean _isClosed;

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
	@SquirrelJMEVendorApi
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

		// Cache this pencil's pixel format
		this._pixelFormat = PencilShelf.hardwareGetPixelFormat(__hardware);
		
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
	@SquirrelJMEVendorApi
	public void clipRect(int __x, int __y, int __w, int __h)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Calculate the base clip coordinates
		int startX = __x + this.getTranslateX();
		int startY = __y + this.getTranslateY();
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
		
		// Get the original clip
		int oldX = this._clipX;
		int oldY = this._clipY;
		int oldEndX = oldX + this._clipWidth;
		int oldEndY = oldY + this._clipHeight;
		
		// Determine the bounds of all of these
		int clipX = Math.max(oldX,
			Math.min(this.surfaceW, Math.max(0, startX)));
		int clipY = Math.max(oldY,
			Math.min(this.surfaceH, Math.max(0, startY)));
		int clipEndX = Math.min(oldEndX,
			Math.min(this.surfaceW, Math.max(0, endX)));
		int clipEndY = Math.min(oldEndY,
			Math.min(this.surfaceH, Math.max(0, endY)));
		
		// Record internally
		this._clipX = clipX;
		this._clipY = clipY;
		this._clipWidth = clipEndX - clipX;
		this._clipHeight = clipEndY - clipY;
		
		// Set hardware clipping
		try
		{
			// Translation needs to be undone, and we need to use the properly
			// shrunken clip
			PencilShelf.hardwareSetClip(this.hardware,
				clipX - this.getTranslateX(),
				clipY - this.getTranslateY(),
				clipEndX - clipX, clipEndY - clipY);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/02/05
	 */
	@Override
	public void close()
	{
		synchronized (this)
		{
			// Do nothing if closed
			if (this._isClosed)
				return;
			
			// Always set as closed before actually closing
			this._isClosed = true;
		}
		
		// Close the graphics internally
		try
		{
			PencilShelf.hardwareCloseGraphics(this.hardware);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void copyArea(int __sx, int __sy, int __w, int __h, int __dx,
		int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Out of bounds?
		if (__sx < 0 || __sy < 0 || __w < 0 || __h < 0 ||
			(__sx + __w) > this.surfaceW ||
			(__sy + __h) > this.surfaceH)
			throw new IllegalArgumentException("IOOB");
		
		// Nothing to copy?
		if (__w == 0 || __h == 0)
			return;
		
		// Forward to native call
		try
		{
			PencilShelf.hardwareCopyArea(this.hardware,
				__sx, __sy, __w, __h, __dx, __dy, __anchor);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawArc(int __x, int __y, int __w, int __h, int __startAngle,
		int __arcAngle)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		try
		{
			PencilShelf.hardwareDrawArc(this.hardware, __x, __y, __w, __h,
				__startAngle, __arcAngle);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		throw Debugging.todo();
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawChar(this.hardware,
				__s, __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
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
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawChars(this.hardware,
				__s, __o, __l, __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Drawing may fail
		try
		{
			PencilShelf.hardwareDrawLine(this.hardware,
				__x1, __y1, __x2, __y2);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/20
	 */
	public void drawPfRegion(int __pf, @NotNull Object __data, int __off, int __scanLen,
		boolean __alpha, int __xSrc, int __ySrc, int __wSrc,
		int __hSrc, int __trans, int __xDest, int __yDest, int __anchor,
		int __wDest, int __hDest, int __origImgWidth, int __origImgHeight)
	{
		if (__wSrc < 0 || __hSrc < 0 || __wDest < 0 || __hDest < 0)
			throw new IllegalArgumentException("EB0b");

		if (__data == null)
			throw new NullPointerException("NARG");

		// Do nothing if closed
		if (this._isClosed)
			return;

		try
		{
			PencilShelf.hardwareDrawRegion(this.hardware, __pf, __data, __off,
				__scanLen, __alpha, __xSrc, __ySrc, __wSrc, __hSrc, __trans,
				__xDest, __yDest, __anchor, __wDest, __hDest, __origImgWidth,
				__origImgHeight);
		}

		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	public void drawPolyline(int[] __xp, int __xo, int[] __yp, int __yo,
		int __n)
	{
		if (__xp == null || __yp == null)
			throw new NullPointerException("NARG");

		if (__xo < 0 || __yo < 0 || __n < 0 ||
			__xo + __n > __xp.length ||
			__yo + __n > __yp.length)
			throw new IllegalArgumentException("EB0d");

		// Do nothing if closed
		if (this._isClosed)
			return;

		try
		{
			PencilShelf.hardwareDrawPolyline(this.hardware, __xp, __xo, __yp,
				__yo, __n);
		}

		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawRGB(int[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward Call
		try
		{
			this.__drawRegion(__data, __off, __scanlen, __alpha,
				0, 0, __w, __h, Sprite.TRANS_NONE,
				__x, __y, Graphics.TOP | Graphics.LEFT, __w, __h,
				__scanlen, (__data.length - __off) / __scanlen);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		throw Debugging.todo();
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward to hardware
		try
		{
			PencilShelf.hardwareDrawRect(this.hardware, __x, __y, __w, __h);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward call
		this.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc,
			__trans, __xdest, __ydest, __anch, __wsrc, __hsrc);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
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
		try
		{
			this.__drawRegion(buf, offset, scanLen, __src.hasAlpha(),
				__xsrc, __ysrc, __wsrc, __hsrc, __trans,
				__xdest, __ydest, __anch,
				__wdest, __hdest, __src.getWidth(), __src.getHeight());
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawRoundRect(int __x, int __y, int __w, int __h,
		int __arcWidth, int __arcHeight)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward to hardware
		try
		{
			PencilShelf.hardwareDrawRoundRect(this.hardware, __x, __y, __w,
				__h, __arcWidth, __arcHeight);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawString(String __s, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawSubstring(this.hardware,
				__s, 0, __s.length(), __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawSubstring(String __s, int __o, int __l,
		int __x, int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __s.length())
			throw new StringIndexOutOfBoundsException("IOOB");
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward
		try
		{
			PencilShelf.hardwareDrawSubstring(this.hardware,
				__s, __o, __l, __x, __y, __anchor);
		}
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawText(Text __t, int __x, int __y)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		throw Debugging.todo();
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	public void drawTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;

		try
		{
			PencilShelf.hardwareDrawTriangle(this.hardware, __x1, __y1, __x2,
				__y2, __x3, __y3);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void fillArc(int __x, int __y, int __w, int __h, int __startAngle,
		int __arcAngle)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		try
		{
			PencilShelf.hardwareFillArc(this.hardware, __x, __y, __w, __h,
				__startAngle, __arcAngle);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	public void fillPolygon(int[] __xp, int __xo, int[] __yp, int __yo,
		int __n)
	{
		if (__xp == null || __yp == null)
			throw new NullPointerException("NARG");

		if (__xo < 0 || __yo < 0 || __n < 0 ||
			__xo + __n > __xp.length ||
			__yo + __n > __yp.length)
			throw new IllegalArgumentException("EB0d");

		// Do nothing if closed
		if (this._isClosed)
			return;

		try
		{
			PencilShelf.hardwareFillPolygon(this.hardware, __xp, __xo, __yp,
				__yo, __n);
		}

		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void fillRect(int __x, int __y, int __w, int __h)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward to hardware
		try
		{
			PencilShelf.hardwareFillRect(this.hardware, __x, __y, __w, __h);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void fillRoundRect(int __x, int __y, int __w, int __h,
		int __arcWidth, int __arcHeight)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward to hardware
		try
		{
			PencilShelf.hardwareFillRoundRect(this.hardware, __x, __y, __w,
				__h, __arcWidth, __arcHeight);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward to hardware
		try
		{
			PencilShelf.hardwareFillTriangle(this.hardware,
				__x1, __y1, __x2, __y2, __x3, __y3);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getAlpha()
	{
		return (this._argbColor >> 24) & 0xFF;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getAlphaColor()
	{
		return this._argbColor;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getBlendingMode()
	{
		return this._blendingMode;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getBlueComponent()
	{
		return (this._argbColor) & 0xFF;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getClipHeight()
	{
		return this._clipHeight;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getClipWidth()
	{
		return this._clipWidth;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getClipX()
	{
		return this._clipX - this.getTranslateX();
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getClipY()
	{
		return this._clipY - this.getTranslateY();
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getColor()
	{
		return this._argbColor & 0xFFFFFF;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public Font getFont()
	{
		return this._font;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public int getGreenComponent()
	{
		return (this._argbColor >> 8) & 0xFF;
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	public void getPfRegion(int __pf, @NotNull Object __data, int __off, int __scanLen,
		boolean __alpha, int __xSrc, int __ySrc, int __wSrc, int __hSrc,
		int __anchor)
	{
		if (__wSrc < 0 || __hSrc < 0)
			throw new IllegalArgumentException("EB0b");

		if (__data == null)
			throw new NullPointerException("NARG");

		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Forward to hardware
		try
		{
			PencilShelf.hardwareGetRegion(this.hardware,
				__pf, __data, __off, __scanLen, __alpha, __xSrc, __ySrc,
				__wSrc, __hSrc, __anchor);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getPixelFormat()
	{
		return this._pixelFormat;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getRedComponent()
	{
		return (this._argbColor >> 16) & 0xFF;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getStrokeStyle()
	{
		return this._strokeStyle;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getTranslateX()
	{
		// If closed, return an unknown value
		if (this._isClosed)
			return 0;
		
		return PencilShelf.hardwareTranslateXY(this.hardware, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getTranslateY()
	{
		// If closed, return an unknown value
		if (this._isClosed)
			return 0;
		
		return PencilShelf.hardwareTranslateXY(this.hardware, true);
	}

	/**
	 * Returns the {@link PencilBracket} that this graphics is currently using
	 * so that it may be directly used with {@link PencilShelf}.
	 *
	 * @return The direct {@link PencilBracket}.
	 * @throws IllegalStateException If this graphics instance is closed.
	 * @since 2025/11/25
	 */
	@SquirrelJMEVendorApi
	public PencilBracket pencil()
		throws IllegalStateException
	{
		synchronized (this)
		{
			if (this._isClosed)
				throw new IllegalStateException();
			
			return this.hardware;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setAlpha(int __a)
		throws IllegalArgumentException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
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
	@SquirrelJMEVendorApi
	public void setAlphaColor(int __argb)
	{
		this.setAlphaColor(__argb, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	public void setAlphaColor(int __argb, boolean __alphaBypass)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Force no alpha on older MIDP if the bypass is not in use
		if (!__alphaBypass && MeepRuntime.versionBefore(3, 0))
			__argb |= 0xFF_000000;
		
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
	@SquirrelJMEVendorApi
	public void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3t Color out of range. (Alpha; Red; Green;
		Blue)} */
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB3t %d %d %d %d", __a, __r, __g, __b));
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Set
		this.setAlphaColor((__a << 24) | (__r << 16) | (__g << 8) | __b);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3u Invalid blending mode. (The mode)} */
		if ((__m != Graphics.SRC && __m != Graphics.SRC_OVER) ||
			(__m == Graphics.SRC && !this.hasAlpha))
			throw new IllegalArgumentException("EB3u " + __m);
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// This is directly mapped
		this.setBlendingModeEx(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/22
	 */
	@Override
	public void setBlendingModeEx(int __m)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3u Invalid blending mode. (The mode)} */
		if (__m < 0 || __m >= PencilBlendingMode.NUM_BLENDS ||
			(__m == Graphics.SRC && !this.hasAlpha))
			throw new IllegalArgumentException("EB3u " + __m);
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Cache locally
		this._blendingMode = __m;
		
		// Set hardware clip
		try
		{
			PencilShelf.hardwareSetBlendingMode(this.hardware, __m);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setClip(int __x, int __y, int __w, int __h)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Calculate the base clip coordinates
		int startX = __x + this.getTranslateX();
		int startY = __y + this.getTranslateY();
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
		
		// Set hardware clipping
		try
		{
			PencilShelf.hardwareSetClip(this.hardware, __x, __y, __w, __h);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@SuppressWarnings("MagicNumber")
	@Override
	@SquirrelJMEVendorApi
	public void setColor(int __rgb)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		this.setAlphaColor((this.getAlphaColor() & 0xFF_000000) |
			(__rgb & 0x00_FFFFFF));
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		this.setAlphaColor(this.getAlpha(), __r, __g, __b);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setFont(Font __base, PencilFontBracket __font, 
		int[] __fontParams)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Cache locally
		this._font = __base;
		
		// Set font natively from the font details
		try
		{
			PencilShelf.hardwareSetFont(this.hardware,
				__font, __fontParams);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setGrayScale(int __v)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		this.setAlphaColor(this.getAlpha(), __v, __v, __v);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setStrokeStyle(int __style)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3v Illegal stroke style.} */
		if (__style != Graphics.SOLID && __style != Graphics.DOTTED)
			throw new IllegalArgumentException("EB3v");
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		// Set
		this._strokeStyle = __style;
		
		// Forward to both software and hardware graphics
		try
		{
			PencilShelf.hardwareSetStrokeStyle(this.hardware, __style);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/21
	 */
	@Override
	public int surfaceHeight()
	{
		return this.surfaceH;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/21
	 */
	@Override
	public int surfaceWidth()
	{
		return this.surfaceW;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void translate(int __x, int __y)
	{
		// Do nothing if closed
		if (this._isClosed)
			return;
		
		try
		{
			PencilShelf.hardwareTranslate(this.hardware, __x, __y);
		}
		
		// Unwrap any potential errors.
		catch (MLECallError e)
		{
			throw e.throwDistinct();
		}
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
	@SquirrelJMEVendorApi
	private void __drawRegion(int[] __data, int __off, int __scanlen,
		boolean __alpha, int __xsrc, int __ysrc, int __wsrc, int __hsrc,
		int __trans, int __xdest, int __ydest, int __anch, int __wdest,
		int __hdest, int __origImgWidth, int __origImgHeight)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if closed
		if (this._isClosed)
			return;
		
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
	 * @param __pal The color palette, may be {@code null}. 
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/25
	 */
	@SquirrelJMEVendorApi
	public static PencilGraphics hardwareGraphics(int __pf, int __bw,
		int __bh, Object __buf, int[] __pal, int __sx, int __sy,
		int __sw, int __sh)
		throws NullPointerException
	{
		return new PencilGraphics(__sw, __sh,
			DisplayManager.instance().scritch().hardwareGraphics(
				__pf, __bw, __bh, __buf, __pal, __sx, __sy, __sw, __sh));
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
	@SquirrelJMEVendorApi
	public static PencilGraphics of(ScritchPencilBracket __hw,
		int __sw, int __sh)
		throws NullPointerException
	{
		if (__hw == null)
			throw new NullPointerException("NARG");
		
		return new PencilGraphics(__sw, __sh, __hw);
	}
}
