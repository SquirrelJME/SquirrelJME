// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * A character sequence of a {@code char[]} array.
 *
 * @since 2022/06/29
 */
public final class CharArrayCharSequence
	implements CharSequence
{
	/** The characters within the array. */
	private final char[] _chars;
	
	/** The offset into the array. */
	private final int _offset;
	
	/** The length of the array. */
	private final int _length;
	
	/**
	 * Initializes the character sequence wrapper.
	 * 
	 * @param __c The character array.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/29
	 */
	public CharArrayCharSequence(char[] __c)
		throws NullPointerException
	{
		this(__c, 0, __c.length);
	}
	
	/**
	 * Initializes the character sequence wrapper.
	 * 
	 * @param __c The character array.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/29
	 */
	public CharArrayCharSequence(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._chars = __c;
		this._offset = __o;
		this._length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/29
	 */
	@Override
	public char charAt(int __i)
		throws IndexOutOfBoundsException
	{
		if (__i < 0 || __i >= this._length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return this._chars[this._offset + __i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/29
	 */
	@Override
	public int length()
	{
		return this._length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/29
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
}
