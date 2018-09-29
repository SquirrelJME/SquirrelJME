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

/**
 * This is a string .
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
		// {@squirreljme.error ZZ1c The initial capacity cannot be negative.
		// (The initial capacity)}
		if (__c < 0)
			throw new NegativeArraySizeException(
				String.format("ZZ1c %d", __c));
		
		// Initialize buffer
		this._buffer = new char[__c];
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
		return this.insert(this._at, __v);
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
			return this.insert(this._at, (CharSequence)null);
		
		// Lock on the buffer because this is thread safe
		synchronized (__v)
		{
			return this.insert(this._at, (CharSequence)__v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(CharSequence __v)
	{
		return this.insert(this._at, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(CharSequence __v, int __o, int __l)
		throws IndexOutOfBoundsException
	{
		return this.insert(this._at, __v, __o, __l);
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
		return this.insert(this._at, __c);
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
		return this.insert(this._at, __c, __o, __l);
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
		return this.insert(this._at, __v);
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
	
	public char charAt(int __dx)
	{
		throw new todo.TODO();
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
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/09/22
	 */
	public StringBuilder insert(int __dx, CharSequence __v, int __o, int __l)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error ZZ1d Cannot insert sequence at a negative
		// index.}
		if (__dx < 0)
			throw new IndexOutOfBoundsException("ZZ1d");
		
		// Print null?
		if (__v == null)
			__v = "null";
		
		// Check bounds
		if (__o < 0 || __l < 0 || (__o + __l) > __v.length())
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get buffer properties
		char[] buffer = this.__buffer(__l);
		int limit = buffer.length,
			at = this._at;
		
		// {@squirreljme.error ZZ1e The index of insertion exceeds the
		// length of the current string. (The insertion index; The string
		// length)}
		if (__dx > at)
			throw new IndexOutOfBoundsException(String.format("ZZ1e %d %d",
				__dx, at));
		
		// First move all characters on the right to the end so that this can
		// properly fit
		for (int i = at - 1, o = i + __l; i >= __dx; i--, o--)
			buffer[o] = buffer[i];
		
		// Place input characters at this point
		for (int i = __dx, s = __o, se = (__o + __l); s < se; i++, s++)
			buffer[i] = __v.charAt(s);
		
		// Set new size
		this._at = at + __l;
		
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
		// {@squirreljme.error ZZ1h Cannot insert sequence at a negative
		// index.}
		if (__dx < 0)
			throw new IndexOutOfBoundsException("ZZ1h");
		
		// Get buffer properties
		char[] buffer = this.__buffer(1);
		int limit = buffer.length,
			at = this._at;
		
		// {@squirreljme.error ZZ1g The index of insertion exceeds the
		// length of the current string. (The insertion index; The string
		// length)}
		if (__dx > at)
			throw new IndexOutOfBoundsException(String.format("ZZ1g %d %d",
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
	
	public void setLength(int __nl)
	{
		throw new todo.TODO();
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
		int limit = buffer.length,
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
		}
		
		return buffer;
	}
}

