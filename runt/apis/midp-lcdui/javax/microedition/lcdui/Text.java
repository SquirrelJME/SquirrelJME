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

import java.util.Arrays;

/**
 * This text class is one which handles all of the text metrics and drawing and
 * such. It handles different fonts, colors, and styles on a per character
 * basis and performs all the needed operations to support text drawing.
 *
 * The color defaults to {@code Display.getColor(Display.COLOR_FOREGROUND)}.
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
	
	/** Storage for the text. */
	private final __Storage__ _storage =
		new __Storage__();
	
	/** The width. */
	private int _width;
	
	/** The height. */
	private int _height;
	
	/** The default font. */
	private Font _defaultfont =
		Font.getDefaultFont();
	
	/** The default foreground color. */
	private int _defaultcolor =
		Display.getDisplays(0)[0].getColor(Display.COLOR_FOREGROUND);
	
	/** The required display height, is cached. */
	private int _requiredheight;
	
	/** The required line count, is cached. */
	private int _requiredlines;
	
	/** Space above each line. */
	private int _spaceabove;
	
	/** Space below each line. */
	private int _spacebelow;
	
	/** The alignment of each line. */
	private int _align;
	
	/** Indentation. */
	private int _indentation;
	
	/** The direction of the text. */
	private int _direction =
		DIRECTION_NEUTRAL;
	
	/** Scrolling offset (scrolled up by n pixels). */
	private int _scrolloffset;
	
	/** Does character placement have to be updated. */
	private boolean _dirty;
	
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
	
	/**
	 * Returns the extents of the given character.
	 *
	 * @param __i The character to get.
	 * @param __ext The extents of the character: x, y, width, height.
	 * @since 2018/12/01
	 */
	public void getCharExtent(int __i, int[] __ext)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__ext == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB2q Extent array length must at least be 4.}
		if (__ext.length < 4)
			throw new IllegalArgumentException("EB2q");
			
		// Update
		if (this._dirty)
			this.__undirty();
		
		// Need to extract the character and font to determine the width and
		// the height of it
		__Storage__ storage = this._storage;
		
		// Default font?
		Font font = storage._font[__i];
		if (font == null)
			font = this._defaultfont;
		
		// Set extents, the width and height come from the character data
		__ext[0] = storage._x[__i];
		__ext[1] = storage._y[__i];
		__ext[2] = font.charWidth(storage._chars[__i]);
		__ext[3] = font.getHeight();
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
	
	/**
	 * Returns the height of the text.
	 *
	 * @return The height of the text.
	 * @since 2018/12/01
	 */
	public int getHeight()
	{
		return this._height;
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
	
	/**
	 * Returns the height which is required to completely display all of the
	 * text within.
	 *
	 * @return The required height to draw.
	 * @since 2018/12/01
	 */
	public int getRequiredHeight()
	{
		// Update
		if (this._dirty)
			this.__undirty();
		
		return this._requiredheight;
	}
	
	/**
	 * Returns the number of lines which are required to display all of the
	 * text within.
	 *
	 * @return The number of lines which are required.
	 * @since 2018/12/01
	 */
	public int getRequiredLineCount()
	{
		// Update
		if (this._dirty)
			this.__undirty();
		
		return this._requiredlines;
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
	
	/**
	 * Returns the length of this text object.
	 *
	 * @return The length of the text object.
	 * @since 2018/12/01
	 */
	public int getTextLength()
	{
		return this._storage._size;
	}
	
	/**
	 * Returns the width of this text.
	 *
	 * @return The text width.
	 * @since 2018/12/01
	 */
	public int getWidth()
	{
		return this._width;
	}
	
	/**
	 * Inserts the given string at the position.
	 *
	 * @param __i The index to insert at, the index is always forced within
	 * the bounds of the buffer (negative values are inserted at zero and
	 * positions greater than the size are inserted at the end).
	 * @param __s The stirng to index.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/30
	 */
	public void insert(int __i, String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Ignore adding empty strings as there is no purpose to it
		if (__s.isEmpty())
			return;
		
		// The index is always in the bounds of the storage
		__Storage__ storage = this._storage;
		if (__i < 0)
			__i = 0;
		else if (__i > storage._size)
			__i = storage._size;
		
		// Insert space to store the characters
		int sn = __s.length();
		storage.__insert(__i, sn);
		
		// Set character data here
		char[] chars = storage._chars;
		for (int i = 0; i < sn; i++)
			chars[__i++] = __s.charAt(i);
		
		// Becomes dirty
		this._dirty = true;
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
	
	/**
	 * Sets the default font to use when no font has been specified.
	 *
	 * @param __f The default font to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/30
	 */
	public void setFont(Font __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// If this is the same font, just ignore it
		Font olddefaultfont = this._defaultfont;
		if (__f.equals(olddefaultfont))
			return;
		
		// Changing the font becomes dirty since we need to measure the
		// metrics again
		this._defaultfont = __f;
		this._dirty = true;
	}
	
	public void setFont(Font __f, int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the default foreground color to use when drawing.
	 *
	 * @param __argb The color to use.
	 * @since 2018/12/01
	 */
	public void setForegroundColor(int __argb)
	{
		// This does not dirty anything because it is just a color change,
		// nothing needs to be recomputed
		this._defaultcolor = __argb;
	}
	
	public void setForegroundColor(int __argb, int __i, int __l)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the height of the text.
	 *
	 * @param __h The new height.
	 * @throws IllegalArgumentException If the height is negative.
	 * @since 2018/12/01
	 */
	public void setHeight(int __h)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB2r The height of a font cannot be
		// negative.}
		if (__h < 0)
			throw new IllegalArgumentException("EB2r");
		
		// Just set the height, we do not need to clear the dirty bit because
		// as long as the requiredheight is still within the height the text
		// fits
		this._height = __h;
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
	
	/**
	 * Sets the width of this text to the specified width.
	 *
	 * @param __w The width to set.
	 * @throws IllegalArgumentException If the width is negative.
	 * @since 2018/11/30
	 */
	public void setWidth(int __w)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB2o Cannot set the width to a negative value.}
		if (__w < 0)
			throw new IllegalArgumentException("EB2o");
		
		// Ignore on no changes
		int oldwidth = this._width;
		if (__w == oldwidth)
			return;
		
		// Set and mark dirty
		this._width = __w;
		this._dirty = true;
	}
	
	/**
	 * Returns whether all of the text fits within the bounds of the box.
	 *
	 * @return If all of the text fits within the bounds of the box.
	 * @since 2018/12/01
	 */
	public boolean textFits()
	{
		// Update
		if (this._dirty)
			this.__undirty();
		
		// The text will fit if the height needed to display everything is at
		// or below the height of the actual box. This is to make height
		// changes not require a recalculate.
		return this._requiredheight <= this._height;
	}
	
	/**
	 * Undirties the text within and calculates all of the needed bounds and
	 * metrics for each character.
	 *
	 * @since 2018/12/01
	 */
	private final void __undirty()
	{
		if (!this._dirty)
			return;
		todo.DEBUG.note("Text is dirty");
		// Using this gobal stuff
		Font defaultfont = this._defaultfont;
		int width = this._width;
		int height = this._height;
		int spaceabove = this._spaceabove;
		int spacebelow = this._spacebelow;
		int align = this._align;
		int indentation = this._indentation;
		int direction = this._direction;
		int scrolloffset = this._scrolloffset;
		
		// If the direction is neutral, this just becomes the locale default
		// For now just treat it as LTR
		if (direction == DIRECTION_NEUTRAL)
			direction = DIRECTION_LTR;
		
		// Are we going right to left?
		boolean dortl = (direction == DIRECTION_RTL);
		
		// Will use this storage stuff
		__Storage__ storage = this._storage;
		char[] chars = storage._chars;
		Font[] font = storage._font;
		short[] cx = storage._x;
		short[] cy = storage._y;
		
		// The starting Y position is 
		// The starting X and Y position is always zero, when other alignments
		// and such are used they are calculated when the line ends
		// X is offset by the indentation and Y is offset by the scrolling
		int y = -scrolloffset + spaceabove,
			nexty = y;
		
		// X starts with indentation, but that might be modified in right
		// to left mode
		int x = (dortl ? width : 0),
			startx = x;
		
		// Cache parameters of font
		Font lastfont = null;
		int fontheight = 0,
			fontascent = 0,
			fontdescent = 0;
		
		// For the end of line calculator, these are the indexes which are
		// used for each character
		int linedxstart = 0,
			linedxend = 0;
			
		// Redo handling of the current character, this will happen if
		// the line overflows
		boolean redo = false;
		
		// Go through and calculate every character line by line, carefully
		// handling alignment and justification
		// The line height is calculated so that if different fonts of
		// different sizes are on the same line, they all are on the baseline
		int linecount = 0,
			linemaxheight = 0,
			linemaxascent = 0,
			linemaxdescent = 0;
		for (int i = 0, n = storage._size; i <= n; i++)
		{todo.DEBUG.note("Running %d <= %d", i, n);
			// Since we need to handle line indentation, justification and
			// otherwise we need a flag to know when the next line was hit
			// to calculate
			// But the last character always has line stuff done to handle the
			// final alignment/directions and such
			// For now only the X positions are considered because
			boolean donextline = (i == n),
				wasnewlinech = false;
			if (!donextline)
			{
				// Need the character and font here, for metrics
				char ch = chars[i];
				Font f = font[i];
				if (f == null)
					f = defaultfont;
				
				// Font has changed?
				if (lastfont != f)
				{
					// Cache parameters
					fontheight = f.getHeight();
					fontascent = f.getAscent();
					fontdescent = f.getDescent();
					
					// Keep track so we do not need to update every time
					lastfont = f;
					
					// Properties of the line changed due to the font?
					// Cache them and check here accordingly
					if (fontascent > linemaxascent)
						linemaxascent = fontascent;
					if (fontheight > linemaxheight)
						linemaxheight = fontheight;
					if (fontdescent > linemaxdescent)
						linemaxdescent = fontdescent;
				}
				
				// Ignore carriage returns
				if (ch == '\r')
					continue;
				
				// Newlines do go to the next line, if a newline was detected
				// then the X position will be set to indentation, otherwise
				// zero.
				else if (ch == '\n')
				{
					donextline = true;
					wasnewlinech = true;
					
					// Clear a redo
					redo = false;
				}
				
				// Draw every other character
				else
				{
					// Get the properties of this character
					int chw = f.charWidth(ch);
					
					// Calculate draw position and the next X position
					// accordingly
					int dx, nx;
					if (dortl)
					{
						dx = x - chw;
						nx = dx;
					}
					else
					{
						dx = x;
						nx = x + chw;
					}
					
					// Character is still within the bounds?
					if (nx >= 0 && nx < width)
					{
						// Store current X position, this may change due to
						// right to left mode
						cx[i] = (short)dx;
						
						// Next character will be here
						x = nx;
						
						// Store the ascent of the character in the Y slot,
						// this is later used at end of line handler to
						// correctly place each character on the baseline
						cy[i] = (short)fontascent;
						
						// Clear a redo
						redo = false;
					}
					
					// Character exceeds the bounds, need to redo and handle
					// end of line
					else
					{
						// Redo should not be triggered twice
						if (redo)
							throw new todo.OOPS();
						
						donextline = true;
						redo = true;
					}
				}
			}
			
			// End of line reached, handle alignment, justification, etc.
			// Perform any position updates as needed
			if (donextline)
			{
				// The line ends on this index (this is either strlen, a
				// newline, or a character placed on a newline so this is
				// always going to be exclusive)
				linedxend = i;
				
				// The next Y position is going to be the max font height
				// for this line.
				// An extra space above is only added if this was a newline,
				// so that way the next line has the actual space above
				nexty = y + linemaxheight + spacebelow +
					(wasnewlinech ? spaceabove : 0);
				
				// Calculate the correct Y position for each character
				for (int q = linedxstart; q < linedxend; q++)
				{
					// Get the original ascent of the character
					int origascent = cy[q];
					
					// The Y position is just the difference in space between
					// the line's max ascent and the character's actual ascent
					cy[q] = (short)(y + (linemaxascent - origascent));
				}
				
				// Handle non-default alignments
				if ((dortl && align != ALIGN_RIGHT) ||
					(!dortl && align != ALIGN_LEFT))
				{
					throw new todo.TODO();
				}
				
				// A line was here so the line count goes up
				linecount++;
				
				// If redoing this character, decrement i so that way it
				// negates the loop increment
				if (redo)
					i--;
				
				// The new start index
				linedxstart = i + 1;
				
				// Set the previously calculated Y position
				y = nexty;
			}
		}
		
		// Update other needed parameters
		// The required height is our nexty because either at the end or
		// a newline this will always be set
		this._requiredheight = nexty;
		this._requiredlines = linecount;
			
		// Has been updated, no longer dirty
		this._dirty = false;
	}
	
	/**
	 * Manages the storage for the text in multiple different arrays at once
	 * for simplicity.
	 *
	 * @since 2018/11/30
	 */
	private static final class __Storage__
	{
		/** The number of characters to grow at a time. */
		private static final int _GROWTH =
			16;
		
		/** Character storage. */
		char[] _chars =
			new char[0];
		
		/** Font storage. */
		Font[] _font =
			new Font[0];
		
		/** Color storage. */
		int[] _color =
			new int[0];
		
		/** X position. */
		short[] _x =
			new short[0];
		
		/** Y position. */
		short[] _y =
			new short[0];
		
		/** The number of stored characters and their attributes. */
		int _size;
		
		/** The limit of the arrays. */
		int _limit;
		
		/**
		 * Inserts space to store the given length at the given index.
		 *
		 * @throws IndexOutOfBoundsException If the insertion index is negative
		 * or exceeds the size of the storage, or if the length is negative.
		 * @since 2018/11/30
		 */
		final void __insert(int __i, int __l)
			throws IndexOutOfBoundsException
		{
			int size = this._size;
			if (__i < 0 || __i > size || __l < 0)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Storage areas
			char[] chars = this._chars;
			Font[] font = this._font;
			int[] color = this._color;
			short[] x = this._x;
			short[] y = this._y;
			
			// Need to grow the buffer?
			int newsize = size + __l,
				limit = this._limit;
			if (newsize > limit)
			{
				// Calculate a new limit with some extra room
				int newlimit = newsize + _GROWTH;
				
				// Resize all the arrays
				this._chars = (chars = Arrays.copyOf(chars, newlimit));
				this._font = (font = Arrays.<Font>copyOf(font, newlimit));
				this._color = (color = Arrays.copyOf(color, newlimit));
				this._x = (x = Arrays.copyOf(x, newlimit));
				this._y = (y = Arrays.copyOf(y, newlimit));
				
				// Set new limit
				this._limit = (limit = newlimit);
			}
			
			// Move over all the entries to the index to make room for this
			// start from the very right end
			// X and Y are resized but their values are not moved around
			// because the insertion of new elements makes things dirty and
			// all of the X and Y values would be invalidated anyway
			for (int o = newsize - 1, i = size - 1; i >= __i; o--, i--)
			{
				chars[o] = chars[i];
				font[o] = font[i];
				color[o] = color[i];
			}
			
			// Set new size
			this._size = newsize;
		}
	}
}

