// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.common;

import java.util.Arrays;
import javax.microedition.lcdui.Font;

/**
 * Manages the storage for the text in multiple different arrays at once
 * for simplicity.
 *
 * @since 2018/11/30
 */
public final class TextStorage
{
	/** The number of characters to grow at a time. */
	public static final int GROWTH =
		16;
	
	/** Character storage. */
	public char[] chars =
		new char[0];
	
	/** Font storage. */
	public Font[] font =
		new Font[0];
	
	/** Color storage, zero is use default. */
	public int[] color =
		new int[0];
	
	/** X position. */
	public short[] x =
		new short[0];
	
	/** Y position. */
	public short[] y =
		new short[0];
	
	/** The number of stored characters and their attributes. */
	public int size;
	
	/** The limit of the arrays. */
	public int limit;
	
	/**
	 * Deletes space at the given index.
	 *
	 * @param __i The index.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the deletion index is out
	 * of bounds.
	 * @since 2018/12/02
	 */
	public final void delete(int __i, int __l)
		throws IndexOutOfBoundsException
	{
		// Check bounds
		int size = this.size;
		if (__i < 0 || __i > size || __l < 0 || (__i + __l) > size)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Storage areas
		char[] chars = this.chars;
		Font[] font = this.font;
		int[] color = this.color;
		
		// Determine the new size
		int newsize = size - __l;
		
		// Move everything down from the higher point
		for (int o = __i, eo = __i + __l, i = eo; o < eo && i < size;
			o++, i++)
		{
			chars[o] = chars[i];
			font[o] = font[i];
			color[o] = color[i];
		}
		
		// Set new size
		this.size = newsize;
	}
	
	/**
	 * Inserts space to store the given length at the given index.
	 *
	 * @param __i The index.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the insertion index is negative
	 * or exceeds the size of the storage, or if the length is negative.
	 * @since 2018/11/30
	 */
	public final void insert(int __i, int __l)
		throws IndexOutOfBoundsException
	{
		int size = this.size;
		if (__i < 0 || __i > size || __l < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Storage areas
		char[] chars = this.chars;
		Font[] font = this.font;
		int[] color = this.color;
		short[] x = this.x;
		short[] y = this.y;
		
		// Need to grow the buffer?
		int newsize = size + __l,
			limit = this.limit;
		if (newsize > limit)
		{
			// Calculate a new limit with some extra room
			int newlimit = newsize + TextStorage.GROWTH;
			
			// Resize all the arrays
			this.chars = (chars = Arrays.copyOf(chars, newlimit));
			this.font = (font = Arrays.<Font>copyOf(font, newlimit));
			this.color = (color = Arrays.copyOf(color, newlimit));
			this.x = (x = Arrays.copyOf(x, newlimit));
			this.y = (y = Arrays.copyOf(y, newlimit));
			
			// Set new limit
			this.limit = (limit = newlimit);
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
		this.size = newsize;
	}
}

