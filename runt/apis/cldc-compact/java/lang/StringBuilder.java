// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This is a string which has a mutable buffer.
 *
 * This class is not thread safe, for that use {@link StringBuffer} instead.
 *
 * The default capacity of this builder is 16 characters.
 *
 * @since 2018/09/22
 */
public final class StringBuilder
	implements Appendable, CharSequence
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
		// {@squirreljme.error ZZ10 The initial capacity cannot be negative.
		// (The initial capacity)}
		if (__c < 0)
			throw new NegativeArraySizeException(
				String.format("ZZ10 %d", __c));
		
		// Initialize buffer
		this._buffer = new char[__c];
		this._limit = __c;
	}
	
	/**
	 * Initializes with the initial characters given by the input sequence,
	 * the internal buffer is the default capacity plus the input string
	 * length.
	 *
	 * @param __s The characters to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	public StringBuilder(String __s)
		throws NullPointerException
	{
		this((CharSequence)__s);
	}
	
	/**
	 * Initializes with the initial characters given by the input sequence,
	 * the internal buffer is the default capacity plus the input string
	 * length.
	 *
	 * @param __s The characters to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	public StringBuilder(CharSequence __cs)
	{
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		// Initialize buffer with the default and the input sequence length
		this._buffer = new char[StringBuilder._DEFAULT_CAPACITY +
			__cs.length()];
		
		// Just append the sequence since the code is the same
		this.append(__cs);
	}
	
	/**
	 * Appends the value to the string.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2018/09/22
	 */
	public StringBuilder append(Object __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * Appends the given string.
	 *
	 * @param __v The string to append.
	 * @return {@code this}.
	 * @since 2018/09/22 
	 */
	public StringBuilder append(String __v)
	{
		if (__v == null)
			__v = "null";
		
		return this.append((CharSequence)__v, 0, __v.length());
	}
	
	/**
	 * Appends the given string buffer.
	 *
	 * @param __v The string buffer to append.
	 * @return {@code this}.
	 * @since 2018/09/22 
	 */
	public StringBuilder append(StringBuffer __v)
	{
		// Is null, cannot lock on it so just forward
		if (__v == null)
			return this.append("null");
		
		// Lock on the buffer because this is thread safe
		synchronized (__v)
		{
			return this.append((CharSequence)__v, 0, __v.length());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(CharSequence __v)
	{
		// Print null instead
		if (__v == null)
			__v = "null";
		
		return this.append(__v, 0, __v.length());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(CharSequence __v, int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// Print null?
		if (__v == null)
			__v = "null";
		
		// Check bounds
		int vn = __v.length();
		if (__s < 0 || __e < 0 || __e > vn || __s > __e)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Length to add
		int len = __e - __s;
		
		// Get buffer properties
		int limit = this._limit,
			at = this._at;
		char[] buffer = (at + len > limit ? this.__buffer(len) : this._buffer);
		
		// Place input characters at this point
		while (__s < __e)
			buffer[at++] = __v.charAt(__s++);
		
		// Set new size
		this._at = at;
		
		return this;
	}
	
	/**
	 * Appends the given characters to the string.
	 *
	 * @param __c The characters to append.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	public StringBuilder append(char[] __c)
		throws NullPointerException
	{
		return this.append(__c, 0, __c.length);
	}
	
	/**
	 * Appends the given characters to the string.
	 *
	 * @param __c The characters to append.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	public StringBuilder append(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get buffer properties
		int limit = this._limit,
			at = this._at;
		char[] buffer = (at + __l > limit ? this.__buffer(__l) : this._buffer);
		
		// Place input characters at this point
		for (int i = 0; i < __l; i++)
			buffer[at++] = __c[__o++];
		
		// Set new size
		this._at = at;
		
		return this;
	}
	
	/**
	 * Appends the value to the string.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2018/09/22
	 */
	public StringBuilder append(boolean __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(char __v)
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
	 * Appends the value to the string.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2018/09/22
	 */
	public StringBuilder append(int __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * Appends the value to the string.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2018/09/22
	 */
	public StringBuilder append(long __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * Appends the value to the string.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2018/09/22
	 */
	public StringBuilder append(float __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * Appends the value to the string.
	 *
	 * @param __v The value to append.
	 * @return {@code this}.
	 * @since 2018/09/22
	 */
	public StringBuilder append(double __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * Returns the current capacity of the internal buffer.
	 *
	 * @return The internal capacity.
	 * @since 2018/09/22
	 */
	public int capacity()
	{
		return this._buffer.length;
	}
	
	/**
	 * Returns the character at the given index.
	 *
	 * @param __dx The index.
	 * @return The character at the given index.
	 * @throws IndexOutOfBoundsException If the character is outside of
	 * bounds.
	 * @since 2018/09/29
	 */
	public char charAt(int __dx)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ11 Out of bound access. (The index)}
		if (__dx < 0 || __dx >= this._at)
			throw new IndexOutOfBoundsException("ZZ11 " + __dx);
		
		return this._buffer[__dx];
	}
	
	public StringBuilder delete(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder deleteCharAt(int __a)
	{
		throw new todo.TODO();
	}
	
	public void ensureCapacity(int __a)
	{
		throw new todo.TODO();
	}
	
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		throw new todo.TODO();
	}
	
	public int indexOf(String __a)
	{
		throw new todo.TODO();
	}
	
	public int indexOf(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, char[] __b, int __c, int __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Inserts the given value at the given position.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/09/29
	 */
	public StringBuilder insert(int __dx, Object __v)
	{
		return this.insert(__dx, (__v == null ? "null" : __v.toString()));
	}
	
	/**
	 * Inserts the given value at the given position.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws StringIndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/09/22
	 */
	public StringBuilder insert(int __dx, String __v)
		throws StringIndexOutOfBoundsException
	{
		return this.insert(__dx, (CharSequence)__v);
	}
	
	public StringBuilder insert(int __a, char[] __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Inserts the given value at the given position.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/09/22
	 */
	public StringBuilder insert(int __dx, CharSequence __v)
		throws IndexOutOfBoundsException
	{
		// Print null instead
		if (__v == null)
			__v = "null";
		
		return this.insert(__dx, __v, 0, __v.length());
	}
	
	/**
	 * Inserts the given value at the given position.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @param __s The start position.
	 * @param __e The end position.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/09/22
	 */
	public StringBuilder insert(int __dx, CharSequence __v, int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ12 Cannot insert sequence at a negative
		// index.}
		if (__dx < 0)
			throw new IndexOutOfBoundsException("ZZ12");
		
		// Check bounds
		int vn = __v.length();
		if (__s < 0 || __e < 0 || __e > vn || __s > __e)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Length to add
		int len = __e - __s;
		
		// Get buffer properties
		int limit = this._limit,
			at = this._at;
		char[] buffer = (at + len > limit ? this.__buffer(len) : this._buffer);
		
		// {@squirreljme.error ZZ13 The index of insertion exceeds the
		// length of the current string. (The insertion index; The string
		// length)}
		if (__dx > at)
			throw new IndexOutOfBoundsException(String.format("ZZ13 %d %d",
				__dx, at));
		
		// First move all characters on the right to the end so that this can
		// properly fit
		for (int i = at - 1, o = i + len; i >= __dx; i--, o--)
			buffer[o] = buffer[i];
		
		// Place input characters at this point
		while (__s < __e)
			buffer[__dx++] = __v.charAt(__s++);
		
		// Set new size
		this._at = at + len;
		
		return this;
	}
	
	public StringBuilder insert(int __a, boolean __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Inserts the given character into the string at the given index.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2018/09/23
	 */
	public StringBuilder insert(int __dx, char __v)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ14 Cannot insert sequence at a negative
		// index.}
		if (__dx < 0)
			throw new IndexOutOfBoundsException("ZZ14");
		
		// Before we go deeper check if the buffer needs to grow
		int limit = this._limit,
			at = this._at;
		char[] buffer = (at + 1 > limit ? this.__buffer(1) : this._buffer);
		
		// {@squirreljme.error ZZ15 The index of insertion exceeds the
		// length of the current string. (The insertion index; The string
		// length)}
		if (__dx > at)
			throw new IndexOutOfBoundsException(String.format("ZZ15 %d %d",
				__dx, at));
		
		// First move all characters on the right to the end so that this can
		// properly fit
		for (int i = at - 1, o = i + 1; i >= __dx; i--, o--)
			buffer[o] = buffer[i];
		
		// Place input characters at this point
		buffer[__dx] = __v;
		
		// Set new size
		this._at = at + 1;
		
		return this;
	}
	
	/**
	 * Inserts the given value into the string at the given index.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2018/09/23
	 */
	public StringBuilder insert(int __dx, int __v)
	{
		return this.insert(__dx, Long.valueOf(__v).toString());
	}
	
	/**
	 * Inserts the given value into the string at the given index.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2018/09/23
	 */
	public StringBuilder insert(int __dx, long __v)
	{
		return this.insert(__dx, Long.valueOf(__v).toString());
	}
	
	public StringBuilder insert(int __a, float __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, double __b)
	{
		throw new todo.TODO();
	}
	
	public int lastIndexOf(String __a)
	{
		throw new todo.TODO();
	}
	
	public int lastIndexOf(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the length of the string.
	 *
	 * @return The string length.
	 * @since 2018/09/29
	 */
	public int length()
	{
		return this._at;
	}
	
	public StringBuilder replace(int __a, int __b, String __c)
	{
		throw new todo.TODO();
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
	
	public void setCharAt(int __dx, char __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the length of the internal buffer, either truncating it or
	 * padding it with NUL characters.
	 *
	 * @param __nl The new length.
	 * @throws IndexOutOfBoundsException If the length is negative.
	 * @since 2018/10/13
	 */
	@ImplementationNote("This does not actually even pad NULs it just " +
		"sets the length or regrows the buffer as needed.")
	public void setLength(int __nl)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ16 Attempt to use a length of a negative
		// size.}
		if (__nl < 0)
			throw new IndexOutOfBoundsException("ZZ16");
		
		// We only need to do something if we are going up, staying the same
		// or going down just sets a variable
		// If the buffer is resized, we do not need to pad for NUL characters
		// because it already has zero values
		int at = this._at;
		if (__nl > at)
			this.__buffer(__nl);
		
		// Set new length
		this._at = __nl;
	}
	
	public CharSequence subSequence(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public String substring(int __a)
	{
		throw new todo.TODO();
	}
	
	public String substring(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public String toString()
	{
		return new String(this._buffer, 0, this._at);
	}
	
	public void trimToSize()
	{
		throw new todo.TODO();
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

