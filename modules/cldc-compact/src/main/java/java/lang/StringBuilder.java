// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.CharArrayCharSequence;
import cc.squirreljme.runtime.cldc.util.CharSequenceUtils;
import java.util.Arrays;

/**
 * This is a string which has a mutable buffer.
 *
 * This class is not thread safe, for that use {@link StringBuffer} instead.
 *
 * The default capacity of this builder is 16 characters.
 *
 * @since 2018/09/22
 */
@Api
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
	@Api
	public StringBuilder()
	{
		this(StringBuilder._DEFAULT_CAPACITY);
	}
	
	/**
	 * Initializes with the given capacity.
	 *
	 * @param __c The initial capacity.
	 * @throws NegativeArraySizeException If the capacity is negative.
	 * @since 2018/09/22
	 */
	@Api
	public StringBuilder(int __c)
		throws NegativeArraySizeException
	{
		/* {@squirreljme.error ZZ1n The initial capacity cannot be negative.
		(The initial capacity)} */
		if (__c < 0)
			throw new NegativeArraySizeException(
				String.format("ZZ1n %d", __c));
		
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
	@Api
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
	 * @param __cs The characters to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	@Api
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
	@Api
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
	@Api
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
	@Api
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
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
		int limit = this._buffer.length;
		int at = this._at;
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
	@Api
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
	@Api
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
	@Api
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
		int limit = this._buffer.length;
		int at = this._at;
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
	@Api
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
	@Api
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
	@Api
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
	@Api
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
	@Api
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
	@Override
	public char charAt(int __dx)
		throws IndexOutOfBoundsException
	{
		/* {@squirreljme.error ZZ1o Out of bound access. (The index)} */
		if (__dx < 0 || __dx >= this._at)
			throw new IndexOutOfBoundsException("ZZ1o " + __dx);
		
		return this._buffer[__dx];
	}
	
	/**
	 * Deletes the given indexes from the string.
	 * 
	 * @param __fromInclusive The index to start from, inclusive.
	 * @param __toExclusive The index to end at, exclusive.
	 * @return {@code this}.
	 * @throws StringIndexOutOfBoundsException If {@code __fromInclusive} is
	 * negative, greater than {@link #length()}, or greater than
	 * {@code __toExclusive}.
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder delete(int __fromInclusive, int __toExclusive)
		throws StringIndexOutOfBoundsException
	{
		int at = this._at;
		if (__fromInclusive < 0 || __fromInclusive > __toExclusive ||
			__fromInclusive > at)
			throw new StringIndexOutOfBoundsException("IOOB");
		
		int realEnd = Math.min(at, __toExclusive);
		int deleteLen = realEnd - __fromInclusive;
		
		// Pointless deletion?
		if (__fromInclusive == __toExclusive || deleteLen == 0)
			return this;
		
		// Move everything down from above, if any
		char[] buffer = this._buffer;
		System.arraycopy(buffer, realEnd,
			buffer, __fromInclusive, at - deleteLen);
		at -= deleteLen;
		
		// Wipe everything at the end (security?)
		ObjectShelf.arrayFill(buffer, at, buffer.length - at, '\0');
		
		// Set new position
		this._at = at;
		
		// And then just returns self
		return this;
	}
	
	/**
	 * Deletes the character at the given index.
	 * 
	 * @param __dx The index to delete.
	 * @return {@code this}.
	 * @throws StringIndexOutOfBoundsException If the index if outside of
	 * the string bounds.
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder deleteCharAt(int __dx)
		throws StringIndexOutOfBoundsException
	{
		if (__dx < 0 || __dx >= this._at)
			throw new StringIndexOutOfBoundsException("IOOB");
		
		// This handles all the deletion logic
		this.delete(__dx, __dx + 1);
		
		return this;
	}
	
	/**
	 * Ensures that the given capacity is made available to the buffer if the
	 * current capacity is less than the specified {@code __minCapacity}.
	 * 
	 * @param __minCapacity The capacity to check against, if too small then
	 * the capacity {@code max(__minCapacity, (capacity() * 2) + 2)} is used.
	 * @since 2022/06/29
	 */
	@Api
	public void ensureCapacity(int __minCapacity)
	{
		// Pointless
		if (__minCapacity <= 0)
			return;
		
		int limit = this._buffer.length;
		if (limit < __minCapacity)
			this.__buffer(Math.max(__minCapacity, (limit << 1) + 2));
	}
	
	@Api
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the position where the given string is found.
	 *
	 * @param __s The sequence to find.
	 * @return The index of the sequence or {@code -1} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/29
	 */
	@Api
	public int indexOf(String __s)
		throws NullPointerException
	{
		return this.indexOf(__s, 0);
	}
	
	/**
	 * Returns the position where the given string is found.
	 *
	 * @param __s The sequence to find.
	 * @param __index The starting index.
	 * @return The index of the sequence or {@code -1} if it is not found.
	 * @since 2022/06/29
	 */
	@Api
	public int indexOf(String __s, int __index)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return CharSequenceUtils.indexOf(this, __s, __index);
	}
	
	/**
	 * Inserts the given value at the given position.
	 *
	 * @param __dx The index to insert at.
	 * @param __c The characters to insert.
	 * @param __o The offset into the array.
	 * @param __l The number of characters to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder insert(int __dx, char[] __c, int __o, int __l)
	{
		return this.insert(__dx, new CharArrayCharSequence(__c, __o, __l));
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
	@Api
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
	@Api
	public StringBuilder insert(int __dx, String __v)
		throws StringIndexOutOfBoundsException
	{
		return this.insert(__dx, (CharSequence)__v);
	}
	
	/**
	 * Inserts the given value at the given position.
	 *
	 * @param __dx The index to insert at.
	 * @param __chars The value to insert.
	 * @return {@code this}.
	 * @throws StringIndexOutOfBoundsException If the index is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder insert(int __dx, char[] __chars)
		throws StringIndexOutOfBoundsException, NullPointerException
	{
		if (__chars == null)
			throw new NullPointerException("NARG");
		
		return this.insert(__dx, new CharArrayCharSequence(__chars));
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
	@Api
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
	@Api
	public StringBuilder insert(int __dx, CharSequence __v, int __s, int __e)
		throws IndexOutOfBoundsException
	{
		/* {@squirreljme.error ZZ1p Cannot insert sequence at a negative
		index.} */
		if (__dx < 0)
			throw new IndexOutOfBoundsException("ZZ1p");
		
		// Check bounds
		int vn = __v.length();
		if (__s < 0 || __e < 0 || __e > vn || __s > __e)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Length to add
		int len = __e - __s;
		
		// Get buffer properties
		int limit = this._buffer.length;
		int at = this._at;
		char[] buffer = (at + len > limit ? this.__buffer(len) : this._buffer);
		
		/* {@squirreljme.error ZZ1q The index of insertion exceeds the
		length of the current string. (The insertion index; The string
		length)} */
		if (__dx > at)
			throw new IndexOutOfBoundsException(
				String.format("ZZ1q %d %d", __dx, at));
		
		// First move all characters on the right to the end so that this can
		// properly fit
		System.arraycopy(buffer, __dx,
			buffer, __dx + len, at - __dx);
		/*for (int i = at - 1, o = i + len; i >= __dx; i--, o--)
			buffer[o] = buffer[i];*/
		
		// Place input characters at this point
		while (__s < __e)
			buffer[__dx++] = __v.charAt(__s++);
		
		// Set new size
		this._at = at + len;
		
		return this;
	}
	
	/**
	 * Inserts the given boolean into the string at the given index.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder insert(int __dx, boolean __v)
	{
		return this.insert(__dx, Boolean.valueOf(__v).toString());
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
	@Api
	public StringBuilder insert(int __dx, char __v)
		throws IndexOutOfBoundsException
	{
		/* {@squirreljme.error ZZ1r Cannot insert sequence at a negative
		index.} */
		if (__dx < 0)
			throw new IndexOutOfBoundsException("ZZ1r");
		
		// Before we go deeper check if the buffer needs to grow
		int limit = this._buffer.length;
		int at = this._at;
		char[] buffer = (at + 1 > limit ? this.__buffer(1) : this._buffer);
		
		/* {@squirreljme.error ZZ1s The index of insertion exceeds the
		length of the current string. (The insertion index; The string
		length)} */
		if (__dx > at)
			throw new IndexOutOfBoundsException(String.format(
				"ZZ1s %d %d", __dx, at));
		
		// First move all characters on the right to the end so that this can
		// properly fit
		System.arraycopy(buffer, __dx,
			buffer, __dx + 1, at - __dx);
		/*for (int i = at - 1, o = i + 1; i >= __dx; i--, o--)
			buffer[o] = buffer[i];*/
		
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
	@Api
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
	@Api
	public StringBuilder insert(int __dx, long __v)
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
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder insert(int __dx, float __v)
	{
		return this.insert(__dx, Float.valueOf(__v).toString());
	}
	
	/**
	 * Inserts the given value into the string at the given index.
	 *
	 * @param __dx The index to insert at.
	 * @param __v The value to insert.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2022/06/29
	 */
	@Api
	public StringBuilder insert(int __dx, double __v)
	{
		return this.insert(__dx, Double.valueOf(__v).toString());
	}
	
	@Api
	public int lastIndexOf(String __s)
	{
		return this.lastIndexOf(__s, Integer.MAX_VALUE);
	}
	
	@Api
	public int lastIndexOf(String __s, int __fromDx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the length of the string.
	 *
	 * @return The string length.
	 * @since 2018/09/29
	 */
	@Override
	public int length()
	{
		return this._at;
	}
	
	@Api
	public StringBuilder replace(int __a, int __b, String __c)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Reverses all of the characters in the string.
	 *
	 * @return {@code this}.
	 * @since 2018/09/23
	 */
	@Api
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
	 * Sets the character at the given index.
	 * 
	 * @param __dx The index to set, must be {@code [0, length)}.
	 * @param __c The character to set.
	 * @throws IndexOutOfBoundsException If the index is not within the bounds
	 * of this {@link StringBuilder}.
	 * @since 2022/06/29
	 */
	@Api
	public void setCharAt(int __dx, char __c)
		throws IndexOutOfBoundsException
	{
		// Check the bounds first
		int at = this._at;
		if (__dx < 0 || __dx >= at)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Now set it
		this._buffer[__dx] = __c;
	}
	
	/**
	 * Sets the length of the internal buffer, either truncating it or
	 * padding it with NUL characters.
	 *
	 * @param __nl The new length.
	 * @throws IndexOutOfBoundsException If the length is negative.
	 * @since 2018/10/13
	 */
	@Api
	public void setLength(int __nl)
		throws IndexOutOfBoundsException
	{
		/* {@squirreljme.error ZZ1t Attempt to use a length of a negative
		size.} */
		if (__nl < 0)
			throw new IndexOutOfBoundsException("ZZ1t");
		
		// We only need to do something if we are going up, staying the same
		// or going down just sets a variable
		// If the buffer is resized, we do not need to pad for NUL characters
		// because it already has zero values
		int at = this._at;
		if (__nl > at)
			this.__buffer(__nl);
		
		// Set new length
		this._at = __nl;
		
		// Erase old characters in the buffer (security?)
		char[] buffer = this._buffer;
		ObjectShelf.arrayFill(buffer, __nl, buffer.length - __nl,
			'\0');
	}
	
	/**
	 * Invokes {@code this.substring(__s, __e)}.
	 *
	 * @param __s The start.
	 * @param __e The end.
	 * @return The sub-sequence.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2019/05/11
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		return this.substring(__s, __e);
	}
	
	/**
	 * Returns a string which is a substring of the given portion of the
	 * string.
	 *
	 * @param __s The start.
	 * @return The resulting sub-string.
	 * @throws StringIndexOutOfBoundsException If the specified positions
	 * are not within the string bounds.
	 * @since 2019/05/11
	 */
	@Api
	public String substring(int __s)
		throws StringIndexOutOfBoundsException
	{
		return this.substring(__s, this.length());
	}
	
	/**
	 * Returns a string which is a substring of the given portion of the
	 * string.
	 *
	 * @param __s The start.
	 * @param __e The end.
	 * @return The resulting sub-string.
	 * @throws StringIndexOutOfBoundsException If the specified positions
	 * are not within the string bounds.
	 * @since 2019/05/11
	 */
	@Api
	public String substring(int __s, int __e)
		throws StringIndexOutOfBoundsException
	{
		/* {@squirreljme.error ZZ1u String index out of bounds.} */
		int len = this._at;
		if (__s < 0 || __e < 0 || __s > __e || __s > len || __e > len)
			throw new StringIndexOutOfBoundsException("ZZ1u");
		
		// Would be an empty string
		if (__s == __e)
			return "";
		
		// Build string and operate directly on the buffer
		return new String(this._buffer, __s, __e - __s);
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
	
	/**
	 * Trims the internal buffer to the size that is needed to store the
	 * string.
	 * 
	 * @since 2022/06/29
	 */
	@Api
	public void trimToSize()
	{
		char[] buffer = this._buffer;
		int at = this._at;
		int limit = buffer.length;
		
		if (limit > at)
			this._buffer = Arrays.copyOf(buffer, at);
	}
	
	/**
	 * Obtains the buffer, potentially resizing it to fit the given amount
	 * of characters.
	 *
	 * @param __l The number of characters to add.
	 * @return The buffer.
	 * @since 2018/09/23
	 */
	@Api
	private char[] __buffer(int __l)
	{
		// Get buffer properties
		char[] buffer = this._buffer;
		int limit = buffer.length;
		int at = this._at;
		
		// Need to resize the buffer to fit this?
		int nextAt = at + __l;
		if (nextAt > limit)
		{
			int newCapacity = nextAt + StringBuilder._DEFAULT_CAPACITY;
			
			// Copy characters over
			char[] extra = Arrays.copyOf(buffer, newCapacity);
			
			// Erase the old buffer (security?)
			ObjectShelf.arrayFill(buffer, 0, buffer.length, '\0');
			
			// Store the new buffer
			this._buffer = (buffer = extra);
		}
		
		return buffer;
	}
}

