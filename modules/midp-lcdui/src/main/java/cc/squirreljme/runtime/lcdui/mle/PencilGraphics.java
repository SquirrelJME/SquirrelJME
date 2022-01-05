// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.PencilCapabilities;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This delegates drawing operations to either the hardware graphics layer
 * or the software graphics layer.
 * 
 * This utilizes both {@link PencilShelf} and {@link PencilBracket} for native
 * graphics.
 *
 * @since 2020/09/25
 */
public final class PencilGraphics
	extends Graphics
{
	/** Software graphics backend. */
	protected final Graphics software;
	
	/** The hardware bracket reference. */
	protected final PencilBracket bracket;
	
	/** The capabilities of the graphics hardware. */
	protected final int capabilities;
	
	/** Surface width. */
	protected final int surfaceW;
	
	/** Surface height. */
	protected final int surfaceH;
	
	/**
	 * Initializes the pencil graphics system.
	 *
	 * @param __caps Capabilities of the hardware, this determines the
	 * functions that are available.
	 * @param __software The fallback software graphics rasterizer.
	 * @param __sw The surface width.
	 * @param __sh The surface height.
	 * @param __bracket The hardware bracket reference for drawing.
	 * @throws IllegalArgumentException If hardware graphics are not capable
	 * enough to be used at all.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/25
	 */
	private PencilGraphics(int __caps, Graphics __software, int __sw, int __sh,
		PencilBracket __bracket)
		throws IllegalArgumentException, NullPointerException
	{
		if (__software == null || __bracket == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB3g Hardware graphics not capable enough.}
		if ((__caps & PencilCapabilities.MINIMUM) == 0)
			throw new IllegalArgumentException("EB3g " + __caps);
		
		this.software = __software;
		this.bracket = __bracket;
		this.capabilities = __caps;
		
		// These are used to manage the clip
		this.surfaceW = __sw;
		this.surfaceH = __sh;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void copyArea(int __sx, int __sy, int __w, int __h, int __dx,
		int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		if (0 == (this.capabilities & PencilCapabilities.COPY_AREA))
		{
			this.software.copyArea(__sx, __sy, __w, __h, __dx, __dy, __anchor);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
	 int __aa)
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_ARC))
		{
			this.software.drawArc(__x, __y, __w, __h, __sa, __aa);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_XRGB16_SIMPLE))
		{
			this.software.drawARGB16(__data, __off, __scanlen, __x, __y, __w,
				__h);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
		{
			this.software.drawChar(__s, __x, __y, __anchor);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawChars(char[] __s, int __o, int __l, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
		{
			this.software.drawChars(__s, __o, __l, __x, __y, __anchor);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_XRGB32_REGION))
		{
			this.software.drawImage(__i, __x, __y, __anchor);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_LINE))
		{
			this.software.drawLine(__x1, __y1, __x2, __y2);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawRGB(int[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_XRGB32_SIMPLE))
		{
			this.software.drawRGB(__data, __off, __scanlen, __x, __y, __w,
				__h, __alpha);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_XRGB16_SIMPLE))
		{
			this.software.drawRGB16(__data, __off, __scanlen, __x, __y,
				__w, __h);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_RECT))
		{
			this.software.drawRect(__x, __y, __w, __h);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_XRGB32_REGION))
		{
			this.software.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc,
				__trans, __xdest, __ydest, __anch);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_XRGB32_REGION))
		{
			this.software.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc,
				__trans, __xdest, __ydest, __anch, __wdest, __hdest);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		if (0 == (this.capabilities & PencilCapabilities.DRAW_ROUND_RECT))
		{
			this.software.drawRoundRect(__x, __y, __w, __h, __aw, __ah);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawString(String __s, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
		{
			this.software.drawString(__s, __x, __y, __anchor);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l,
		int __x, int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
		{
			this.software.drawSubstring(__s, __o, __l, __x, __y, __anchor);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
		{
			this.software.drawText(__t, __x, __y);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
	 int __aa)
	{
		if (0 == (this.capabilities & PencilCapabilities.FILL_ARC))
		{
			this.software.fillArc(__x, __y, __w, __h, __sa, __aa);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void fillRect(int __x, int __y, int __w, int __h)
	{
		if (0 == (this.capabilities & PencilCapabilities.FILL_RECT))
		{
			this.software.fillRect(__x, __y, __w, __h);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		if (0 == (this.capabilities & PencilCapabilities.FILL_ROUND_RECT))
		{
			this.software.fillRoundRect(__x, __y, __w, __h, __aw, __ah);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		if (0 == (this.capabilities & PencilCapabilities.FILL_TRIANGLE))
		{
			this.software.fillTriangle(__x1, __y1, __x2, __y2, __x3, __y3);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getAlpha()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getAlphaColor()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getBlendingMode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getBlueComponent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getClipHeight()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getClipWidth()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getClipX()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getClipY()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getColor()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getDisplayColor(int __rgb)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final Font getFont()
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
			return this.software.getFont();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getGrayScale()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getGreenComponent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getRedComponent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getStrokeStyle()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getTranslateX()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final int getTranslateY()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setAlpha(int __a)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setAlphaColor(int __argb)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setClip(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setColor(int __rgb)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setFont(Font __font)
	{
		if (0 == (this.capabilities & PencilCapabilities.FONT_TEXT))
		{
			this.software.setFont(__font);
			return;
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void setStrokeStyle(int __a)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/25
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		throw Debugging.todo();
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
		// Setup software graphics
		Graphics software = SoftwareGraphicsFactory.softwareGraphics(__pf,
			__bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh);
		
		// Get the capabilities of the native system, if it is not supported
		// then operations will purely be implemented in software
		int caps = PencilShelf.capabilities(__pf); 
		if ((caps & PencilCapabilities.MINIMUM) == 0)
			return software;
		
		return new PencilGraphics(caps, software, __sw, __sh,
			PencilShelf.hardwareGraphics(__pf,
			__bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh));
	}
}
