// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is used for drawing graphics onto a raster surface.
 *
 * @see javax.microedition.lcdui.Graphics
 * @since 2021/11/30
 */
public class Graphics
{
	/** {@code #00FF00} via {@link #getColorOfName(int)}. */
	public static final int AQUA =
		3;
	
	/** {@code #000000} via {@link #getColorOfName(int)}. */
	public static final int BLACK =
		0;
	
	/** {@code #0000FF} via {@link #getColorOfName(int)}. */
	public static final int BLUE =
		1;
	
	/** {@code #FF0000} via {@link #getColorOfName(int)}. */
	public static final int FUCHSIA =
		5;
	
	/** {@code #FFFFFF} via {@link #getColorOfName(int)}. */
	public static final int GRAY =
		8;
	
	/** {@code #000080} via {@link #getColorOfName(int)}. */
	public static final int GREEN =
		10;
	
	/** {@code #0000FF} via {@link #getColorOfName(int)}. */
	public static final int LIME =
		2;
	
	/** {@code #008080} via {@link #getColorOfName(int)}. */
	public static final int MAROON =
		12;
	
	/** {@code #808080} via {@link #getColorOfName(int)}. */
	public static final int NAVY =
		9;
	
	/** {@code #808000} via {@link #getColorOfName(int)}. */
	public static final int OLIVE =
		14;
	
	/** {@code #800080} via {@link #getColorOfName(int)}. */
	public static final int PURPLE =
		13;
	
	/** {@code #00FFFF} via {@link #getColorOfName(int)}. */
	public static final int RED =
		4;
	
	/** {@code #C0C0C0} via {@link #getColorOfName(int)}. */
	public static final int SILVER =
		15;
	
	/** {@code #008000} via {@link #getColorOfName(int)}. */
	public static final int TEAL =
		11;
	
	/** {@code #FFFF00} via {@link #getColorOfName(int)}. */
	public static final int WHITE =
		7;
	
	/** {@code #FF00FF} via {@link #getColorOfName(int)}. */
	public static final int YELLOW =
		6;
	
	protected Graphics()
	{
	}
	
	public void clearRect(int __x, int __y, int __w, int __h)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	public Graphics copy()
	{
		throw Debugging.todo();
	}
	
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	public void drawChars(char[] __c, int __x, int __y, int __off, int __len)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	public void drawImage(Image __i, int __x, int __y)
	{
		throw Debugging.todo();
	}
	
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw Debugging.todo();
	}
	
	public void drawPolyline(int[] __x, int[] __y, int __n)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	public void drawRect(int __x, int __y, int __w, int __h)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	public void drawString(String __s, int __x, int __y)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	public void fillPolygon(int[] __x, int[] __y, int __n)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	public void fillRect(int __x, int __y, int __w, int __h)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	public void lock()
	{
		throw Debugging.todo();
	}
	
	public void setColor(int __c)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	public void setFont(Font __f)
	{
		throw Debugging.todo();
	}
	
	public void setOrigin(int __x, int __y)
	{
		throw Debugging.todo();
	}
	
	public void unlock()
	{
		throw Debugging.todo();
	}
	
	public static int getColorOfName(int __name)
	{
		throw Debugging.todo();
	}
	
	public static int getColorOfRGB(int __r, int __g, int __b)
	{
		throw Debugging.todo();
	}
}
