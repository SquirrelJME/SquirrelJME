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
	
	public StringBuilder append(Object __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Appends the given string.
	 *
	 * @param __s The string to append.
	 * @return {@code this}.
	 * @since 2018/09/22 
	 */
	public StringBuilder append(String __s)
	{
		return this.append((CharSequence)__s);
	}
	
	/**
	 * Appends the given string buffer.
	 *
	 * @param __s The string buffer to append.
	 * @return {@code this}.
	 * @since 2018/09/22 
	 */
	public StringBuilder append(StringBuffer __s)
	{
		// Is null, cannot lock on it so just forward
		if (__s == null)
			return this.append((CharSequence)null);
		
		// Lock on the buffer because this is thread safe
		synchronized (__s)
		{
			return this.append((CharSequence)__s);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(CharSequence __cs)
	{
		// Forward call
		return this.append(__cs, 0, __cs.length());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(CharSequence __cs, int __o, int __l)
		throws IndexOutOfBoundsException
	{
		// Switch to null
		if (__cs == null)
			__cs = "null";
		
		// Check bounds
		if (__o < 0 || __l < 0 || (__o + __l) > __cs.length())
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get buffer properties
		char[] buffer = this._buffer;
		int limit = buffer.length,
			at = this._at;
		
		// Resize the buffer if the string cannot fit
		if (at + __l > limit)
		{
			throw new todo.TODO();
		}
		
		// Store data
		for (int i = __o, o = at, endi = i + __l; i < endi; i++, o++)
			buffer[o] = __cs.charAt(i);
		
		// Set new length
		this._at = at + __l;
		
		return this;
	}
	
	public StringBuilder append(char[] __a)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder append(char[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder append(boolean __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/22
	 */
	@Override
	public StringBuilder append(char __a)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder append(int __a)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder append(long __a)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder append(float __a)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder append(double __a)
	{
		throw new todo.TODO();
	}
	
	public int capacity()
	{
		throw new todo.TODO();
	}
	
	public char charAt(int __a)
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
	
	public StringBuilder insert(int __a, Object __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, String __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, char[] __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, CharSequence __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, CharSequence __b, int __c,
		int __d)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, boolean __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, char __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder insert(int __a, long __b)
	{
		throw new todo.TODO();
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
	
	public int length()
	{
		throw new todo.TODO();
	}
	
	public StringBuilder replace(int __a, int __b, String __c)
	{
		throw new todo.TODO();
	}
	
	public StringBuilder reverse()
	{
		throw new todo.TODO();
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
}

