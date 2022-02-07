// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.gfx.ref.ReferenceBrush;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This contains a reference implementation of graphics, it is not meant
 * for speed but for correctness and an ease of understanding all of the
 * drawing operations.
 * 
 * @since 2022/02/06
 */
public final class ReferenceGraphics
	extends Graphics
{
	/** The brush for drawing graphics. */
	protected final ReferenceBrush brush;
	
	/**
	 * Initialize reference graphics.
	 * 
	 * @param __brush The brush to draw with.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/06
	 */
	public ReferenceGraphics(ReferenceBrush __brush)
		throws NullPointerException
	{
		if (__brush == null)
			throw new NullPointerException("NARG");
		
		this.brush = __brush;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void clipRect(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void copyArea(int __sx, int __sy, int __w, int __h, int __dx,
		int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawArc(int __x, int __y, int __w, int __h, int __sa,
	 int __aa)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawChars(char[] __s, int __o, int __l, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawRGB(int[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc, int __wsrc,
		int __hsrc, int __trans, int __xdest, int __ydest, int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc, int __wsrc,
		int __hsrc, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawRoundRect(int __x, int __y, int __w, int __h, int __aw,
		int __ah)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawString(String __s, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawSubstring(String __s, int __o, int __l, int __x, int __y,
		int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void fillArc(int __x, int __y, int __w, int __h, int __sa,
	 int __aa)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void fillRect(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void fillRoundRect(int __x, int __y, int __w, int __h, int __aw,
		int __ah)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getAlpha()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getAlphaColor()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getBlendingMode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getBlueComponent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getClipHeight()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getClipWidth()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getClipX()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getClipY()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getColor()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getDisplayColor(int __rgb)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public Font getFont()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getGrayScale()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getGreenComponent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getRedComponent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getStrokeStyle()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getTranslateX()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public int getTranslateY()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setAlpha(int __a)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setAlphaColor(int __argb)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setClip(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setColor(int __rgb)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setFont(Font __font)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setGrayScale(int __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void setStrokeStyle(int __style)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/06
	 */
	@Override
	public void translate(int __x, int __y)
	{
		throw Debugging.todo();
	}
}
