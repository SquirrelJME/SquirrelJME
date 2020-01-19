// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;

/**
 * This represents a string.
 *
 * @since 2019/05/25
 */
public final class String
{
	/** The first interned string. */
	private static volatile String _FIRST_INTERN;
	
	/** The backing array. */
	transient final char[] _chars;
	
	/** The hashcode for this string. */
	transient int _hashcode;
	
	/** The next intern string in the chain. */
	private transient volatile String _nextintern;
	
	/**
	 * Initializes an empty string.
	 *
	 * @since 2019/05/26
	 */
	public String()
	{
		this._chars = new char[0];
	}
	
	/**
	 * Initializes a string which uses characters which are a copy of the given
	 * character array, using the offset and length.
	 *
	 * @param __c The characters to copy.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	public String(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Copy characters
		char[] copy = new char[__l];
		for (int i = __o, o = 0; o < __l; i++, o++)
			copy[o] = __c[i];
		
		// Just use the copied buffer
		this._chars = copy;
	}
	
	/**
	 * Initializes string decoded from the given UTF-8 byte.
	 *
	 * @param __b The UTF-8 bytes to decode.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	public String(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Create temporary output which has the input characters and such so
		// this will be the maximum used
		int bn = __b.length;
		char[] temp = new char[bn];
		
		// Translate UTF-8 sequences
		int nc = 0;
		for (int i = 0; i < bn;)
		{
			// Get character
			int c = __b[i++] & 0xFF;
			
			// Single byte
			if ((c & 0b1000_0000) == 0)
				temp[nc++] = (char)c;
			
			// Double byte
			else if ((c & 0b1110_0000) == 0b1100_0000)
			{
				c = ((c & 0b0001_1111) << 6);
				c |= (__b[i++] & 0b111111);
				temp[nc++] = (char)c;
			}
			
			// Triple byte
			else if ((c & 0b1111_0000) == 0b1110_0000)
			{
				c = ((c & 0b0000_1111) << 12);
				c |= ((__b[i++] & 0b111111) << 6);
				c |= (__b[i++] & 0b111111);
				temp[nc++] = (char)c;
			}
		}
		
		// Use direct array if the same length
		if (nc == bn)
			this._chars = temp;
		
		// Too short, copy only used chars
		else
		{
			char[] chars = new char[nc];
			for (int i = 0; i < nc; i++)
				chars[i] = temp[i];
			this._chars = chars;
		}
	}
	
	/**
	 * Returns the character at the given index.
	 *
	 * @param __i The index to get.
	 * @return The character here.
	 * @throws IndexOutOfBoundsException If it is not within bounds.
	 * @since 2019/05/27
	 */
	public final char charAt(int __i)
		throws IndexOutOfBoundsException
	{
		char[] chars = this._chars;
		if (__i < 0 || __i >= chars.length)
			throw new IndexOutOfBoundsException();
		return chars[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/26
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof String))
			return false;
		
		String o = (String)__o;
		if (this.hashCode() != o.hashCode())
			return false;
			
		// Character data
		char[] ac = this._chars,
			bc = o._chars;
		
		// If the length differs, they are not equal
		int n = ac.length;
		if (n != bc.length)
			return false;
		
		// Compare individual characters
		for (int i = 0; i < n; i++)
			if (ac[i] != bc[i])
				return false;
		
		// Would be a match!
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/26
	 */
	@Override
	public final int hashCode()
	{
		// If the hashcode was already determined before then use that
		// cache
		int rv = this._hashcode;
		if (rv != 0)
			return rv;
		
		// Calculate the hashCode(), the JavaDoc gives the following formula:
		// == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes!
		char[] ch = this._chars;
		for (int i = 0, n = ch.length; i < n; i++)
			rv = ((rv << 5) - rv) + ch[i];
		
		// Cache hashcode for later
		this._hashcode = rv;
		return rv;
	}
	
	/**
	 * Returns a string which is a unique internal representation of a string.
	 *
	 * @return The unique interned string.
	 * @since 2019/05/26
	 */
	public final String intern()
	{
		// If no strings have ever been interned before, make this intern
		String first = _FIRST_INTERN;
		if (first == null)
		{
			_FIRST_INTERN = this;
			return this;
		}
		
		// Go through the linked list chain finding our string
		String at = first;
		while (at != null)
		{
			// Use the target string if it is the same
			if (this.equals(at))
				return at;
			
			// Go to the next link
			at = at._nextintern;
		}
		
		// Next intern is the first and the first becomes this one
		this._nextintern = first;
		_FIRST_INTERN = this;
		
		// Return our string since it was not in the chain
		return this;
	}
	
	/**
	 * Returns the last occurance of the given character.
	 *
	 * @param __c The character to find.
	 * @return The last occurance of the character or {@code -1} if it was
	 * not found.
	 * @since 2018/09/29
	 */
	public int lastIndexOf(int __c)
	{
		return this.lastIndexOf(__c, Integer.MAX_VALUE);
	}
	
	/**
	 * Returns the last occurance of the given character going backwards from
	 * the given index.
	 *
	 * @param __c The character to find.
	 * @param __dx The index to start at, this is clipped to within the
	 * string bounds accordingly although if it is negative no searching is
	 * done.
	 * @return The last occurance of the character or {@code -1} if it was
	 * not found.
	 * @since 2018/09/29
	 */
	public int lastIndexOf(int __c, int __dx)
	{
		// Never going to find anything at all
		if (__dx < 0)
			return -1;
		
		// Cap index
		char[] ch = this._chars;
		int n = ch.length;
		if (__dx >= n)
			__dx = n - 1;
		
		for (; __dx >= 0; __dx--)
			if (__c == ch[__dx])
				return __dx;
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns the string length.
	 *
	 * @return The string length.
	 * @since 2019/05/27
	 */
	public final int length()
	{
		return this._chars.length;
	}
	
	/**
	 * Compares the given string regions to see if they match.
	 *
	 * @param __toff The offset for this string.
	 * @param __o The other string to compare against.
	 * @param __ooff The offset of the target string.
	 * @param __len The number of characters to compare.
	 * @return If the region matches or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/18
	 */
	public boolean regionMatches(int __toff, String __o, int __ooff, int __len)
	{
		return this.regionMatches(false, __toff, __o, __ooff, __len);
	}
	
	/**
	 * Compares the given string regions to see if they match.
	 *
	 * @param __igncase Is case to be ignored?
	 * @param __toff The offset for this string.
	 * @param __b The other string to compare against.
	 * @param __boff The offset of the target string.
	 * @param __len The number of characters to compare.
	 * @return If the region matches or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/18
	 */
	public boolean regionMatches(boolean __igncase, int __toff, String __b,
		int __boff, int __len)
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Automatically false
		if (__toff < 0 || __boff < 0 ||
			__toff + __len > this.length() || __boff + __len > __b.length())
			return false;
		
		// A quirk of the standard is that negative lengths are not an error
		// but are treated as matches
		if (__len < 0)
			return true;
		
		// Disregarding case
		if (__igncase)
			for (int i = 0; i < __len; i++, __toff++, __boff++)
			{
				char a = this.charAt(__toff),
					b = __b.charAt(__boff);
				
				if (Character.toLowerCase(a) != Character.toLowerCase(b) &&
					Character.toUpperCase(a) != Character.toUpperCase(b))
					return false;
			}
		
		// Regarding case
		else
			for (int i = 0; i < __len; i++, __toff++, __boff++)
				if (this.charAt(__toff) != __b.charAt(__boff))
					return false;
		
		// Matches
		return true;
	}
	
	/**
	 * Returns a substring of this string starting at the given index.
	 *
	 * @param __s The index to start at.
	 * @return The sub-string for that index.
	 * @throws IndexOutOfBoundsException If the start is outside of the bounds.
	 * @since 2019/11/15
	 */
	public String substring(int __s)
		throws IndexOutOfBoundsException
	{
		// A substring starting at the zero character is the same
		if (__s == 0)
			return this;
		
		// Call other
		return this.substring(__s, this.length());
	}
	
	/**
	 * Returns a substring of this string.
	 *
	 * @param __s The starting index.
	 * @param __e The ending index.
	 * @throws IndexOutOfBoundsException If the string region is outside of
	 * bounds.
	 * @since 2019/11/15
	 */
	public String substring(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error SV0s String substring is outside of bounds.}
		if (__s < 0 || __s > __e || __e > this.length())
			throw new IndexOutOfBoundsException("SV0s");
		
		return new String(this._chars, __s, (__e - __s));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/26
	 */
	@Override
	public final String toString()
	{
		return this;
	}
}

