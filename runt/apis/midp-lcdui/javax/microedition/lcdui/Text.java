// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This text class is one which handles all of the text metrics and drawing and
 * such. It handles different fonts, colors, and styles on a per character
 * basis and performs all the needed operations to support text drawing.
 *
 * @since 2018/11/29
 */
public class Text
{
	public static final int ALIGN_CENTER =
		1;
	
	public static final int ALIGN_DEFAULT =
		4;
	
	public static final int ALIGN_JUSTIFY =
		3;
	
	public static final int ALIGN_LEFT =
		0;
	
	public static final int ALIGN_RIGHT =
		2;
	
	public static final int DIRECTION_LTR =
		10;
	
	public static final int DIRECTION_NEUTRAL =
		12;
	
	public static final int DIRECTION_RTL =
		11;
	
	/** The width. */
	private int _width;
	
	/** The height. */
	private int _height;
	
	/**
	 * Initializes the text with no width or height.
	 *
	 * @since 2018/11/29
	 */
	public Text()
	{
		this("", 0, 0);
	}
	
	/**
	 * Initializes the text with the given width and height.
	 *
	 * @param __c The text to use.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws IllegalArgumentException If the width or height are negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public Text(String __c, int __w, int __h)
		throws IllegalArgumentException, NullPointerException
	{
		this(__c, 0, __c.length(), __w, __h);
	}
	
	/**
	 * Initializes the text with the given width and height.
	 *
	 * @param __c The text to use.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws IllegalArgumentException If the width or height are negative.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	public Text(String __c, int __o, int __l, int __w, int __h)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length())
			throw new IndexOutOfBoundsException("IOOB");
		
		// {@squirreljme.error EB2n The width and height cannot be negative
		// for text.}
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("EB2n");
		
		// Set sizes first
		this._width = __w;
		this._height = __h;
		
		// Insert all of the text
		this.insert(0, __c.substring(__o, __o + __l));
	}
	
	public void delete(int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public int getAlignment()
	{
		throw new todo.TODO();
	}
	
	public int getBackgroundColor()
	{
		throw new todo.TODO();
	}
	
	public int getCaret()
	{
		throw new todo.TODO();
	}
	
	public void getCharExtent(int __i, int[] __ext)
	{
		throw new todo.TODO();
	}
	
	public int getCharIndex(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	public Font getFont()
	{
		throw new todo.TODO();
	}
	
	public Font getFont(int __i)
	{
		throw new todo.TODO();
	}
	
	public int getForegroundColor()
	{
		throw new todo.TODO();
	}
	
	public int getForegroundColor(int __i)
	{
		throw new todo.TODO();
	}
	
	public int getHeight()
	{
		throw new todo.TODO();
	}
	
	public int getHighlightIndex()
	{
		throw new todo.TODO();
	}
	
	public int getHighlightLength()
	{
		throw new todo.TODO();
	}
	
	public int getIndent()
	{
		throw new todo.TODO();
	}
	
	public int getInitialDirection()
	{
		throw new todo.TODO();
	}
	
	public int getRequiredHeight()
	{
		throw new todo.TODO();
	}
	
	public int getRequiredLineCount()
	{
		throw new todo.TODO();
	}
	
	public int getScrollOffset()
	{
		throw new todo.TODO();
	}
	
	public int getSpaceAbove()
	{
		throw new todo.TODO();
	}
	
	public int getSpaceBelow()
	{
		throw new todo.TODO();
	}
	
	public String getText(int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public int getTextLength()
	{
		throw new todo.TODO();
	}
	
	public int getWidth()
	{
		throw new todo.TODO();
	}
	
	public void insert(int __i, String __c)
	{
		throw new todo.TODO();
	}
	
	public int lastRenderedIndex()
	{
		throw new todo.TODO();
	}
	
	public void moveCaret(int __nl)
	{
		throw new todo.TODO();
	}
	
	public void setAlignment(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setBackgroundColor(int __argb)
	{
		throw new todo.TODO();
	}
	
	public void setCaret(int __i)
	{
		throw new todo.TODO();
	}
	
	public void setFont(Font __f)
	{
		throw new todo.TODO();
	}
	
	public void setFont(Font __f, int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public void setForegroundColor(int __argb)
	{
		throw new todo.TODO();
	}
	
	public void setForegroundColor(int __argb, int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public void setHeight(int __h)
	{
		throw new todo.TODO();
	}
	
	public void setHighlight(int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	public void setIndent(int __i)
	{
		throw new todo.TODO();
	}
	
	public void setInitialDirection(int __dir)
	{
		throw new todo.TODO();
	}
	
	public void setScrollOffset(int __o)
	{
		throw new todo.TODO();
	}
	
	public void setSpaceAbove(int __sa)
	{
		throw new todo.TODO();
	}
	
	public void setSpaceBelow(int __sb)
	{
		throw new todo.TODO();
	}
	
	public void setWidth(int __w)
	{
		throw new todo.TODO();
	}
	
	public boolean textFits()
	{
		throw new todo.TODO();
	}
}

