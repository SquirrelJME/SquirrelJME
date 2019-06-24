// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This class is used to build instances of string.
 *
 * @since 2019/05/25
 */
public final class StringBuilder
{
	/** Default capacity of the internal array. */
	private static final int _DEFAULT_CAPACITY =
		16;
	
	/** The internal buffer for storing characters. */
	private char[] _buffer;
	
	/** The characters which are in the buffer. */
	private int _at;
	
	/** The limit of the string buffer. */
	private int _limit;
	
	/**
	 * Initializes with the default capacity.
	 *
	 * @since 2018/09/22
	 */
	public StringBuilder()
	{
		this(StringBuilder._DEFAULT_CAPACITY);
	}
	
	/**
	 * Initailizes with the given capacity.
	 *
	 * @param __c The initial capacity.
	 * @throws NegativeArraySizeException If the capacity is negative.
	 * @since 2018/09/22
	 */
	public StringBuilder(int __c)
		throws NegativeArraySizeException
	{
		if (__c < 0)
			throw new NegativeArraySizeException();
		
		// Initialize buffer
		this._buffer = new char[__c];
		this._limit = __c;
	}
	
	/**
	 * Appends a character into this one.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2019/06/18
	 */
	public final StringBuilder append(char __v)
	{
		// Before we go deeper check if the buffer needs to grow
		int limit = this._limit,
			at = this._at;
		char[] buffer = (at + 1 > limit ? this.__buffer(1) : this._buffer);
		
		// Add to the end
		buffer[at] = __v;
		this._at = at + 1;
		
		// Self
		return this;
	}
	
	/**
	 * Appends a string into this one.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2019/05/25
	 */
	public final StringBuilder append(String __v)
	{
		// Print null?
		if (__v == null)
			__v = "null";
		
		// Length to append
		int len = __v.length();
		
		// Get buffer properties
		int limit = this._limit,
			at = this._at;
		char[] buffer = (at + len > limit ? this.__buffer(len) : this._buffer);
		
		// Place input characters at this point
		for (int i = 0; i < len; i++)
			buffer[at++] = __v.charAt(i);
		
		// Set new size
		this._at = at;
		
		return this;
	}
	
	/**
	 * Reverses all of the characters in the string.
	 *
	 * @return {@code this}.
	 * @since 2018/09/23
	 */
	public StringBuilder reverse()
	{
		// Get the buffer
		char[] buffer = this._buffer;
		int at = this._at;
		
		// Swap all the characters, a less than be because if it reaches the
		// center there will be no need to swap anything
		for (int a = 0, b = at - 1; a < b; a++, b--)
		{
			char x = buffer[a];
			buffer[a] = buffer[b];
			buffer[b] = x;
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/18
	 */
	@Override
	public final String toString()
	{
		return new String(this._buffer, 0, this._at);
	}
	
	/**
	 * Obtains the buffer, potentially resizing it to fit the given amount
	 * of characters.
	 *
	 * @param __l The number of characters to add.
	 * @return The buffer.
	 * @since 2018/09/23
	 */
	private final char[] __buffer(int __l)
	{
		// Get buffer properties
		char[] buffer = this._buffer;
		int limit = this._limit,
			at = this._at;
		
		// Need to resize the buffer to fit this?
		int nextat = at + __l;
		if (nextat > limit)
		{
			int newcapacity = nextat + StringBuilder._DEFAULT_CAPACITY;
			
			// Copy characters over
			char[] extra = new char[newcapacity];
			for (int i = 0; i < at; i++)
				extra[i] = buffer[i];
			
			this._buffer = (buffer = extra);
			this._limit = newcapacity;
		}
		
		return buffer;
	}
}

