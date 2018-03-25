// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
// Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
 * This class is automatically generated to from a template to support
 * multiple pixel formats which are backed by arrays.
 *
 * @since 2018/03/22
 */
public final class ShortIndexed16ArrayGraphics
	extends Graphics
{
	/** The width of the image. */
	protected final int width;
	/** The height of the image. */
	protected final int height;
	/** The pitch of the image. */
	protected final int pitch;
	/** The offset into the buffer data. */
	protected final int offset;
	/** The number of elements that consist of pixel data. */
	protected final int numelements;
	/** Physical end of the buffer. */
	protected final int lastelement;
	/** The array containing the buffer data. */
	private final short[] _buffer;
	/** The palette used for drawing. */
	private final int[] _palette;
	/**
	 * Initializes the graphics drawer which draws into the given array.
	 *
	 * @param __buf The buffer to draw into.
	 * @param __pal The palette data.
	 * @param __width The width of the image.
	 * @param __height The height of the image.
	 * @param __pitch The image pitch.
	 * @param __offset The data buffer offset.
	 * @throws ArrayIndexOutOfBoundsException If the image dimensions exceeds
	 * the array bounds.
	 * @throws IllegalArgumentException If the width or height is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public ShortIndexed16ArrayGraphics(short[] __buf,
		int[] __pal,
		int __width, int __height, int __pitch, int __offset)
	throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		   NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		if (__pal == null)
			throw new NullPointerException("NARG");
		// The palette is directly used and may change!
		this._palette = __pal;
		// {@squirreljme.error EBT0 Invalid width and/or height specified.}
		if (__width <= 0 || __height <= 0)
			throw new IllegalArgumentException("EBT0");
		// {@squirreljme.error EBT1 The pitch is less than the width.}
		if (__pitch < __width)
			throw new IllegalArgumentException("EBT1");
		// Count the number of actual elements which may be shifted down
		// if there are more pixels per element
		// {@squirreljme.error EBT2 The specified parameters exceed the bounds
		// of the array. (The pitch; The height; The offset; The pitch;
		// The array length; The number of elements in the image)}
		int numelements = (__pitch * __height) >>> 0,
		lastelement = __offset + numelements;
		if (__offset < 0 || lastelement > __buf.length)
			throw new ArrayIndexOutOfBoundsException(
				String.format("EBT2 %d %d %d %d %d %d", __pitch, __height,
					__offset, __pitch, __buf.length, numelements));
		// Set parameters
		this._buffer = __buf;
		this.width = __width;
		this.height = __height;
		this.pitch = __pitch;
		this.offset = __offset;
		this.numelements = numelements;
		this.lastelement = lastelement;
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
	throws IllegalArgumentException, IllegalStateException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	throws NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
	throws NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
	throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
	throws NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	throws NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
	throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
	throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawString(String __s, int __x, int __y,
		int __anchor)
	throws NullPointerException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
	throws NullPointerException, StringIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillRect(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getAlpha()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getAlphaColor()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getBlendingMode()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getBlueComponent()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipHeight()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipWidth()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipX()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipY()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getColor()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getDisplayColor(int __rgb)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final Font getFont()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getGrayScale()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getGreenComponent()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getRedComponent()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getStrokeStyle()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getTranslateX()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getTranslateY()
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlpha(int __a)
	throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __argb)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
	throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setBlendingMode(int __m)
	throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setClip(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __rgb)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __r, int __g, int __b)
	throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setFont(Font __a)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setStrokeStyle(int __a)
	throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		throw new todo.TODO();
	}
}
