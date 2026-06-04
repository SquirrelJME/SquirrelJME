// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.StringShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.i18n.DefaultLocale;
import cc.squirreljme.runtime.cldc.i18n.Locale;
import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Decoder;
import cc.squirreljme.runtime.cldc.io.Encoder;
import cc.squirreljme.runtime.cldc.util.CharSequenceUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Formatter;
import org.intellij.lang.annotations.PrintFormat;

/**
 * A {@link String} represents a sequence of characters which make up a group
 * of alphanumeric or other symbols and represents text.
 *
 * Note that only {@link #toLowerCase()} and {@link #toUpperCase()} take
 * locale into considering during their conversion.
 *
 * @since 2018/09/16
 */
@Api
@SuppressWarnings({"StringOperationCanBeSimplified",
	"JavaExistingMethodCanBeUsed", "ClassWithTooManyConstructors"})
public final class String
	implements Comparable<String>, CharSequence
{
	/** The minimum trim character. */
	private static final char _MIN_TRIM_CHAR =
		' ';
	
	/**
	 * Initializes a new empty string.
	 *
	 * @since 2018/02/24
	 */
	@Api
	public String()
	{
		// Set internal string
		StringShelf.stringInit(this);
	}
	
	/**
	 * Initializes a string which is an exact copy of the other string.
	 *
	 * @param __s The other string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	@Api
	public String(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set internal string
		StringShelf.stringInit(this, __s);
	}
	
	/**
	 * Initializes a string which uses characters which are a copy of the given
	 * character array.
	 *
	 * @param __c The characters to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	@Api
	public String(char[] __c)
		throws NullPointerException
	{
		this(__c, 0, __c.length);
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
	@Api
	public String(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Set internal string
		StringShelf.stringInit(this, __c, __o, __l);
	}
	
	/**
	 * Decodes the given bytes to a string using the specified encoding.
	 *
	 * @param __b The input bytes to decode.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to code.
	 * @param __e The encoding to use when decoding the bytes.
	 * @throws IndexOutOfBoundsException If offset and/or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/11/06
	 */
	@Api
	public String(byte[] __b, int __o, int __l, String __e)
		throws IndexOutOfBoundsException, NullPointerException,
			UnsupportedEncodingException
	{
		this(__b, __o, __l, CodecFactory.decoder(__e));
	}
	
	/**
	 * Decodes the given bytes to a string using the specified encoding.
	 *
	 * @param __b The input bytes to decode.
	 * @param __e The encoding to use when decoding the bytes.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/11/06
	 */
	@Api
	public String(byte[] __b, String __e)
		throws NullPointerException, UnsupportedEncodingException
	{
		this(__b, 0,  __b.length, CodecFactory.decoder(__e));
	}
	
	/**
	 * Decodes the given bytes to a string using the default encoding.
	 *
	 * @param __b The input bytes to decode.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to code.
	 * @throws IndexOutOfBoundsException If offset and/or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	@Api
	public String(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		this(__b, __o, __l, CodecFactory.defaultDecoder());
	}
	
	/**
	 * Decodes the given bytes to a string using the specified encoding.
	 *
	 * @param __b The input bytes to decode.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	@Api
	public String(byte[] __b)
		throws NullPointerException
	{
		this(__b, 0, __b.length, CodecFactory.defaultDecoder());
	}
	
	/**
	 * Initializes string from the given buffer.
	 *
	 * @param __a The input buffer.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	@Api
	public String(StringBuffer __a)
		throws NullPointerException
	{
		this(__a.toString());
	}
	
	/**
	 * Initializes string from the given builder.
	 *
	 * @param __a The input builder.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	@Api
	public String(StringBuilder __a)
		throws NullPointerException
	{
		this(__a.toString());
	}
	
	/**
	 * Decodes the given bytes to a string using the specified decoder.
	 *
	 * @param __b The input bytes to decode.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to code.
	 * @param __dec The decoder to use for the input bytes.
	 * @throws IndexOutOfBoundsException If offset and/or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	@Api
	private String(byte[] __b, int __o, int __l, Decoder __dec)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null || __dec == null)
			throw new NullPointerException("NARG");
		int bn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bn)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Set up a temporary character array with average sequence length to
		// hopefully have enough room to store the decoded characters
		int cap = (int)(__l * __dec.averageSequenceLength());
		int at = 0;
		char[] out = new char[cap];
		
		// Start decoding input sequences
		for (int i = __o, e = i + __l; i < e;)
		{
			// Decode sequence (the decoded char is in the low 16-bits)
			int left = e - i;
			int rc = __dec.decode(__b, i, left);
			
			// If there is not enough room to decode a sequence then store the
			// replacement character
			char ch;
			if (rc < 0)
			{
				ch = 0xFFFD;
				i = e;
			}
			
			// The lower 16-bits contains the character, the upper bytes
			// contains the number of characters read
			else
			{
				ch = (char)rc;
				i += (rc >> 16);
			}
			
			// Need to re-allocate?
			if (at >= cap)
				out = Arrays.copyOf(out, (cap += left));
			
			// Store
			out[at++] = ch;
		}
		
		// Setup native string
		StringShelf.stringInit(this, out, 0, at);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public char charAt(int __i)
		throws IndexOutOfBoundsException
	{
		if (__i < 0 || __i >= StringShelf.stringLength(this))
			throw new StringIndexOutOfBoundsException(__i);
		
		return StringShelf.stringCharAt(this, __i);
	}
	
	/**
	 * Compares the character values of this string and compares it to the
	 * character values of the other string.
	 *
	 * Smaller strings always precede longer strings.
	 *
	 * This is equivalent to the standard POSIX {@code strcmp()} with the "C"
	 * locale.
	 *
	 * Internally this does not handle the special variants of this class and
	 * is a general purpose method.
	 *
	 * @param __o The string to compare against.
	 * @return A negative value if this string precedes the other string, a
	 * positive value if this string precedes the other string, or zero if the
	 * strings are equal.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/02
	 */
	@Override
	public int compareTo(String __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Refers to the same exact string?
		//noinspection StringEquality
		if (this == __o)
			return 0;
		
		// Use standard utility method comparison
		return CharSequenceUtils.compare(this, __o);
	}
	
	/**
	 * Compares two strings lexicographically without regards to case. This
	 * method does not take locale into account.
	 *
	 * @param __o The other string.
	 * @return The comparison of the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	@Api
	public int compareToIgnoreCase(String __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Refers to the same exact string?
		//noinspection StringEquality
		if (this == __o)
			return 0;
		
		// Use standard utility method comparison
		return CharSequenceUtils.compareIgnoreCase(this, __o);
	}
	
	/**
	 * Concatenates the given string to the end of this string.
	 *
	 * If the given string has a length of zero then this string is returned,
	 * otherwise if this string has a length of zero the given string is
	 * returned.
	 *
	 * @param __s The string to append to this string (returning a new string).
	 * @return The new string with the appended characters.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	@Api
	public String concat(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Do not let the compiler do syntactic sugar on this
		int aLen = StringShelf.stringLength(this);
		int bLen = StringShelf.stringLength(__s);
		int tLen = aLen + bLen;
		
		// Setup resultant array
		char[] result = new char[tLen];
		
		// Fill in
		StringShelf.stringToChar(this, 0,
			result, 0, aLen);
		StringShelf.stringToChar(__s, 0,
			result, aLen, bLen);
		
		// Setup string
		return StringShelf.stringValueOf(false, result, 0, tLen);
	}
	
	/**
	 * Returns {@code true} if the string contains the given sequence.
	 *
	 * @param __b The sequence to find.
	 * @return If the string contains the given sequence or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/14
	 */
	@Api
	public boolean contains(CharSequence __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// This is the same operation
		return CharSequenceUtils.indexOf(this, __b, 0) >= 0;
	}
	
	/**
	 * Checks whether the content of this string is equal to the specified
	 * string buffer, the input buffer will be synchronized.
	 *
	 * @param __s The character sequence to check, this is synchronized.
	 * @return If the content equals the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	@Api
	public boolean contentEquals(StringBuffer __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// StringBuffers are synchronized and as such a lock is used to
		// prevent it from being changed.
		synchronized (__s)
		{
			return this.__contentEquals(__s);
		}
	}
	
	/**
	 * Checks whether the content of this string is equal to the specified
	 * character sequence.
	 *
	 * @param __s The character sequence to check, if it is a
	 * {@link StringBuffer} then it will be synchronized.
	 * @return If the content equals the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	@Api
	public boolean contentEquals(CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Synchronize on StringBuffers
		if (__s instanceof StringBuffer)
			return this.contentEquals((StringBuffer)__s);
		
		return this.__contentEquals(__s);
	}
	
	/**
	 * Tests if this string ends with the given string.
	 *
	 * @param __s The string to test.
	 * @return If this string ends with the other string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/20
	 */
	@Api
	public boolean endsWith(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Use common utility for this
		return CharSequenceUtils.endsWith(this, __s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/21
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Short circuit match, the same string object is always equals
		if (this == __o)
			return true;
		
		// If not a string, fail
		if (!(__o instanceof String))
			return false;
		
		// Use native equals call
		return StringShelf.stringEquals(this, (String)__o);
	}
	
	/**
	 * Checks if one string is equal to the other, ignoring case and ignoring
	 * any locale differences.
	 *
	 * A character is considered equal if they are the same character, the
	 * uppercase result of both characters is the same, or the lowercase
	 * result of each character is the same.
	 *
	 * @param __o The other string to check.
	 * @return If the two strings are equal.
	 * @since 2018/10/29
	 */
	@Api
	@ProgrammerTip("Locale is not considered.")
	public boolean equalsIgnoreCase(String __o)
	{
		// Always false
		if (__o == null)
			return false;
		
		// Use common utility method
		return CharSequenceUtils.compareIgnoreCase(this, __o) == 0;
	}
	
	/**
	 * Translates this string into a byte array using the default encoding.
	 *
	 * @return The resulting byte array.
	 * @since 2018/12/08
	 */
	@Api
	public byte[] getBytes()
	{
		return this.__getBytes(CodecFactory.defaultEncoder());
	}
	
	/**
	 * Translates this string using into a byte array using the specified
	 * character encoding.
	 *
	 * @param __enc The character encoding to use.
	 * @return A byte array with the characters of this string converted to
	 * bytes.
	 * @throws NullPointerException If no encoding was specified.
	 * @throws UnsupportedEncodingException If the encoding is unknown.
	 * @since 2018/12/08
	 */
	@Api
	public byte[] getBytes(String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		// Check
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		// Wrap it
		try
		{
			return this.__getBytes(CodecFactory.encoder(__enc));
		}
		
		/* {@squirreljme.error ZZ1k The specified encoding is not supported by
		the virtual machine. (The encoding)} */
		catch (UnsupportedEncodingException uee)
		{
			throw new AssertionError("ZZ1k " + __enc);
		}
	}
	
	/**
	 * Copies characters from this string to the given character array.
	 *
	 * @param __from The source character index.
	 * @param __to The destination character index, exclusive.
	 * @param __dest The destination character array.
	 * @param __destOff The offset into the array to write at.
	 * @throws IndexOutOfBoundsException If the source indexes are not within
	 * the bounds of the string, or the destination indexes are not within
	 * the bounds of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/10/15
	 */
	@Api
	public void getChars(int __from, int __to, char[] __dest, int __destOff)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__dest == null)
			throw new NullPointerException("NARG");
		
		int srcLen = this.length();
		int copyLen = __to - __from;
		if (__from < 0 || __from > __to || __to > srcLen || __to < 0 ||
			__destOff < 0 || (__destOff + copyLen) > __dest.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Perform the actual copy
		while (__from < __to)
			__dest[__destOff++] =
				StringShelf.stringCharAt(this, __from++);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public int hashCode()
	{
		// Get native hashcode
		int rv = StringShelf.stringHash(this);
		if (rv != 0)
			return rv;
		
		// Calculate the hashCode(), the JavaDoc gives the following formula:
		// == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes!
		for (int i = 0, n = StringShelf.stringLength(this); i < n; i++)
			rv = ((rv << 5) - rv) + StringShelf.stringCharAt(this, i);
		
		return rv;
	}
	
	/**
	 * Searches the string for the given character and returns the index if it
	 * was found.
	 *
	 * @param __c The character to find.
	 * @return The character index or {@code -1} if it was not found.
	 * @since 2018/09/20
	 */
	@Api
	public int indexOf(int __c)
	{
		return this.indexOf(__c, 0);
	}
	
	/**
	 * Searches the string for the given character starting from the given
	 * index and returns the index if it was found.
	 *
	 * @param __c The character to find.
	 * @param __i The index to start at, the values are capped within the
	 * string length.
	 * @return The character index or {@code -1} if it was not found.
	 * @since 2018/09/20
	 */
	@Api
	public int indexOf(int __c, int __i)
	{
		return CharSequenceUtils.indexOf(this, __c, __i);
	}
	
	/**
	 * Returns the position where the given string is found.
	 *
	 * @param __b The sequence to find.
	 * @return The index of the sequence or {@code -1} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/14
	 */
	@Api
	public int indexOf(String __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return CharSequenceUtils.indexOf(this, __b, 0);
	}
	
	/**
	 * Returns the position where the given string is found.
	 *
	 * @param __b The sequence to find.
	 * @param __i The starting index.
	 * @return The index of the sequence or {@code -1} if it is not found.
	 * @since 2019/05/14
	 */
	@Api
	public int indexOf(String __b, int __i)
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return CharSequenceUtils.indexOf(this, __b, __i);
	}
	
	/**
	 * This returns the unique string instance used for the current string, if
	 * the current string is not within the internal map then it is added. If
	 * it already exists in the map then that pre-existing value is returned.
	 * The purpose of this method is for potential optimizations where there
	 * are a large number of long-term string objects in memory which may be
	 * duplicated in many places (such as in a database). As such, only
	 * persistent strings should be interned, never short-lived strings.
	 *
	 * Although this may be used for {@code ==} to work, it is not recommended
	 * using this method for such things.
	 *
	 * @return The unique string instance.
	 * @since 2016/04/01
	 */
	@Api
	public String intern()
	{
		// Is this already intern?
		if (StringShelf.stringIsIntern(this))
			return this;
		
		// Not intern, so make an intern string
		return StringShelf.stringValueOf(true, this);
	}
	
	/**
	 * Returns {@code true} if this string is empty.
	 *
	 * @return {@code true} if this string is empty.
	 * @since 2017/08/15
	 */
	@Api
	@SuppressWarnings("Since15")
	public boolean isEmpty()
	{
		return StringShelf.stringLength(this) == 0;
	}
	
	/**
	 * Returns the last occurrence of the given character.
	 *
	 * @param __c The character to find.
	 * @return The last occurrence of the character or {@code -1} if it was
	 * not found.
	 * @since 2018/09/29
	 */
	@Api
	public int lastIndexOf(int __c)
	{
		return this.lastIndexOf(__c, Integer.MAX_VALUE);
	}
	
	/**
	 * Returns the last occurrence of the given character going backwards from
	 * the given index.
	 *
	 * @param __c The character to find.
	 * @param __dx The index to start at, this is clipped to within the
	 * string bounds accordingly although if it is negative no searching is
	 * done.
	 * @return The last occurrence of the character or {@code -1} if it was
	 * not found.
	 * @since 2018/09/29
	 */
	@Api
	public int lastIndexOf(int __c, int __dx)
	{
		return CharSequenceUtils.lastIndexOf(this, __c, __dx);
	}
	
	/**
	 * Returns the last occurrence of the given string.
	 *
	 * @param __s The string to find.
	 * @return The last occurrence of the string or {@code -1} if it was
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	@Api
	public int lastIndexOf(String __s)
		throws NullPointerException
	{
		return this.lastIndexOf(__s, Integer.MAX_VALUE);
	}
	
	@Api
	public int lastIndexOf(String __s, int __dx)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the length of this string.
	 *
	 * @return The length of this string.
	 * @since 2018/09/19
	 */
	@Override
	public int length()
	{
		return StringShelf.stringLength(this);
	}
	
	/**
	 * Compares the given string regions to see if they match.
	 *
	 * @param __toff The offset for this string.
	 * @param __b The other string to compare against.
	 * @param __boff The offset of the target string.
	 * @param __len The number of characters to compare.
	 * @return If the region matches or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/18
	 */
	@Api
	public boolean regionMatches(int __toff, String __b, int __boff, int __len)
	{
		return this.regionMatches(false, __toff, __b, __boff, __len);
	}
	
	/**
	 * Compares the given string regions to see if they match.
	 *
	 * @param __ignCase Is case to be ignored?
	 * @param __toff The offset for this string.
	 * @param __b The other string to compare against.
	 * @param __boff The offset of the target string.
	 * @param __len The number of characters to compare, if this is negative
	 * then this is treated as zero.
	 * @return If the region matches or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/18
	 */
	@Api
	public boolean regionMatches(boolean __ignCase, int __toff, String __b,
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
		if (__ignCase)
			for (int i = 0; i < __len; i++, __toff++, __boff++)
			{
				char a = this.charAt(__toff);
				char b = __b.charAt(__boff);
				
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
	 * Returns a string with all characters which match the starting character
	 * which are then replaced with the replacement character.
	 *
	 * @param __a The starting character.
	 * @param __b The replacement character.
	 * @return The resulting string.
	 * @since 2018/09/22
	 */
	@Api
	public String replace(char __a, char __b)
	{
		// If a character is going to be replaced with itself then no
		// replacement has to actually be performed. Or if the original
		// character is not even in the string. Then do not bother.
		if (__a == __b || this.indexOf(__a) < 0)
			return this;
		
		// Setup result
		int n = StringShelf.stringLength(this);
		char[] result = new char[n];
		
		// Copy data into an array with translated characters
		for (int i = 0; i < n; i++)
		{
			char c = StringShelf.stringCharAt(this, i);
			
			// Replacing?
			if (c == __a)
				c = __b;
			
			result[i] = c;
		}
		
		// Setup new string
		return StringShelf.stringValueOf(false, result, 0, n);
	}
	
	/**
	 * Checks if this string starts with the other string at the given index.
	 *
	 * @param __s The string to check for a starting match.
	 * @param __sdx The starting index to start checking at.
	 * @return If this string starts with the given string.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	@Api
	public boolean startsWith(String __s, int __sdx)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Use common utility
		return CharSequenceUtils.startsWith(this, __s, __sdx);
	}
	
	/**
	 * Checks if this string starts with the other string.
	 *
	 * @param __s The string to check for a starting match.
	 * @return If this string starts with the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	@Api
	public boolean startsWith(String __s)
		throws NullPointerException
	{
		return this.startsWith(__s, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
	{
		// Is the same code as substring
		return this.substring(__s, __e);
	}
	
	/**
	 * Returns a substring of this string starting at the given index.
	 *
	 * @param __s The index to start at.
	 * @return The sub-string for that index.
	 * @throws IndexOutOfBoundsException If the start is outside the bounds.
	 * @since 2018/11/04
	 */
	@Api
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
	 * @param __s The starting index, inclusive.
	 * @param __e The ending index, exclusive.
	 * @return The resulting string.
	 * @throws IndexOutOfBoundsException If the string region is outside of
	 * bounds.
	 * @since 2018/09/29
	 */
	@Api
	public String substring(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// The entire string region requires no new string
		int n = StringShelf.stringLength(this);
		if (__s == 0 && __e == n)
			return this;
		
		// A blank string with no characters
		if (__s == __e)
			return "";
		
		/* {@squirreljme.error ZZ1m String substring is outside of bounds.
		(The start index; The end index; The length)} */
		if (__s < 0 || __s > __e || __e > n)
			throw new IndexOutOfBoundsException("ZZ1m " + __s + " " + __e +
				" " + n);
		
		// Derive sub-sequence
		int nl = __e - __s;
		char[] result = new char[nl];
		
		// Fill in
		StringShelf.stringToChar(this, __s, result, 0, nl);
		
		// Build
		return StringShelf.stringValueOf(false, result, 0, nl);
	}
	
	/**
	 * Returns a character array which contains every character within this
	 * string.
	 *
	 * @return A character array containing the characters of this string.
	 * @since 2017/08/15
	 */
	@Api
	public char[] toCharArray()
	{
		// Setup resultant array
		int n = StringShelf.stringLength(this);
		char[] result = new char[n];
		
		// Fill in
		StringShelf.stringToChar(this, 0, result, 0, n);
		
		// Use the result
		return result;
	}
	
	/**
	 * Translates this string to lowercase using the current locale.
	 *
	 * Java ME specifies that only Latin-1 characters are supported
	 *
	 * @return The lowercased result of this string.
	 * @since 2018/09/20
	 */
	@Api
	public String toLowerCase()
	{
		// Check if the string will change first
		int n = StringShelf.stringLength(this);
		boolean changed = false;
		Locale locale = DefaultLocale.defaultLocale();
		for (int i = 0; i < n; i++)
		{
			char c = StringShelf.stringCharAt(this, i);
			changed |= (c != locale.toLowerCase(c));
		}
		
		// Ignore if unchanged
		if (!changed)
			return this;
		
		// Map characters
		char[] result = new char[n];
		for (int i = 0; i < n; i++)
			result[i] = locale.toLowerCase(
				StringShelf.stringCharAt(this, i));
		
		// Setup new string
		return StringShelf.stringValueOf(false, result, 0, n);
	}
	
	/**
	 * Returns {@code this}.
	 *
	 * @return {@code this}.
	 * @since 2017/08/15
	 */
	@Override
	public String toString()
	{
		return this;
	}
	
	/**
	 * Translates this string to uppercase using the current locale.
	 *
	 * Java ME specifies that only Latin-1 characters are supported
	 *
	 * @return The uppercased result of this string.
	 * @since 2018/09/29
	 */
	@Api
	public String toUpperCase()
	{
		// Check if the string will change first
		int n = StringShelf.stringLength(this);
		Locale locale = DefaultLocale.defaultLocale();
		boolean changed = false;
		for (int i = 0; i < n; i++)
		{
			char c = StringShelf.stringCharAt(this, i);
			changed |= (c != locale.toUpperCase(c));
		}
		
		// Ignore if unchanged
		if (!changed)
			return this;
		
		// Map characters
		char[] result = new char[n];
		for (int i = 0; i < n; i++)
			result[i] = locale.toUpperCase(
				StringShelf.stringCharAt(this, i));
		
		// Setup new string
		return StringShelf.stringValueOf(false, result, 0, n);
	}
	
	/**
	 * This trims all the low ASCII whitespace and control characters at
	 * the start and the end of this string and returns a new string with
	 * the trimmed whitespace.
	 *
	 * This does not handle any other potential characters which may act as
	 * whitespace in the high Unicode range and only handles the first 32
	 * ASCII characters.
	 *
	 * @return A string with the whitespace trimmed, if the string does not
	 * start or end in whitespace then {@code this} is returned.
	 * @since 2016/04/20
	 */
	@Api
	@SuppressWarnings("StatementWithEmptyBody")
	public String trim()
	{
		// Empty strings do not need trimming
		int n = StringShelf.stringLength(this);
		if (n == 0)
			return this;
		
		// Find starting trim position
		int s;
		for (s = 0; s < n &&
			StringShelf.stringCharAt(this, s) <= String._MIN_TRIM_CHAR;
			s++)
			;
		
		// Find ending trim position
		int e;
		for (e = n; e > s &&
			StringShelf.stringCharAt(this, e - 1) <=
				String._MIN_TRIM_CHAR; e--)
			;
		
		// Already considered trimmed?
		if (s == 0 && e == n)
			return this;
		
		// Return trimmed variant of it
		return this.substring(s, e);
	}
	
	/**
	 * Checks to see if this string matches the target sequence.
	 *
	 * @param __s The input sequence to check against.
	 * @return If they are the same or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	private boolean __contentEquals(CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return CharSequenceUtils.equals(this, __s);
	}
	
	/**
	 * Returns the encoded byte sequence.
	 *
	 * @param __e The encoder to use when writing bytes.
	 * @return The bytes from this string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/08
	 */
	private byte[] __getBytes(Encoder __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Maximum size of sequences that can be encoded
		int msl;
		byte[] seq = new byte[(msl = __e.maximumSequenceLength())];
		
		// We operate directly on the sequence
		int n = StringShelf.stringLength(this);
		
		// Write here
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
			(int)(n * __e.averageSequenceLength())))
		{
			// Encode every character!
			for (int i = 0; i < n; i++)
			{
				int sz = __e.encode(StringShelf.stringCharAt(this, i),
					seq, 0, msl);
				
				// Should not occur
				if (sz < 0)
					throw Debugging.oops();
				
				baos.write(seq, 0, sz);
			}
			
			// Use this byte array
			return baos.toByteArray();
		}
		
		// Should not occur
		catch (IOException e)
		{
			throw Debugging.oops();
		}
	}
	
	/**
	 * Invokes {@code String.valueOf(__c, __o, __l)}.
	 *
	 * @param __c The input character array to copy.
	 * @param __o The starting offset.
	 * @param __l The number of characters to copy.
	 * @return The result of the other call.
	 * @since 2016/06/13
	 */
	@Api
	public static String copyValueOf(char[] __c, int __o, int __l)
	{
		return String.valueOf(__c, __o, __l);
	}
	
	/**
	 * Invokes {@code String.valueOf(__a)}.
	 *
	 * @param __c The input character array to copy.
	 * @return The result of the other call.
	 * @since 2016/06/13
	 */
	@Api
	public static String copyValueOf(char[] __c)
	{
		return String.valueOf(__c);
	}
	
	/**
	 * Returns a formatted string using a temporary instance of the
	 * {@link Formatter} class.
	 *
	 * @param __fmt The format specifiers.
	 * @param __args The arguments to the formatter.
	 * @return The formatted string.
	 * @throws IllegalArgumentException If the format is not correct.
	 * @throws NullPointerException On null arguments.
	 * @see Formatter
	 * @since 2018/11/02
	 */
	@Api
	public static String format(@PrintFormat String __fmt, Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		// Just forward everything to this formatter
		return new Formatter().format(__fmt, __args).toString();
	}
	
	/**
	 * Returns the value of the given object as a string.
	 *
	 * @param __a The object to get the string value of, if {@code null} then
	 * the string {@code "null"} is returned.
	 * @return The string value of the given object or {@code "null"}
	 * @since 2016/06/13
	 */
	@Api
	public static String valueOf(Object __a)
	{
		// The value is a string already
		if (__a instanceof String)
			return (String)__a;
		
		// If null use null
		if (__a == null)
			return "null";
		
		// Just return the toString of the given object.
		return __a.toString();
	}
	
	/**
	 * Returns a string representation of the given character array.
	 *
	 * @param __c The array.
	 * @return The resulting string.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(char[] __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		return StringShelf.stringValueOf(false,
			__c, 0, (__c != null ? __c.length : 0));
	}
	
	/**
	 * Returns a string representation of the given character array.
	 *
	 * @param __c The input array.
	 * @param __o The offset.
	 * @param __l The number of characters to set.
	 * @return The resulting string.
	 * @throws IndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length || (__o + __l) < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		return StringShelf.stringValueOf(false, __c, __o, __l);
	}
	
	/**
	 * Returns a string representation of the given value.
	 *
	 * @param __a The value.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(boolean __a)
	{
		return Boolean.valueOf(__a).toString();
	}
	
	/**
	 * Returns a string representation of the given value.
	 *
	 * @param __a The value.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(char __a)
	{
		// This allows the cache to be used
		return Character.valueOf(__a).toString();
	}
	
	/**
	 * Returns a string representation of the given value.
	 *
	 * @param __a The value.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(int __a)
	{
		return Integer.valueOf(__a).toString();
	}
	
	/**
	 * Returns a string representation of the given value.
	 *
	 * @param __a The value.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(long __a)
	{
		return Long.valueOf(__a).toString();
	}
	
	/**
	 * Returns a string representation of the given value.
	 *
	 * @param __a The value.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(float __a)
	{
		return Float.valueOf(__a).toString();
	}
	
	/**
	 * Returns a string representation of the given value.
	 *
	 * @param __a The value.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	@Api
	public static String valueOf(double __a)
	{
		return Double.valueOf(__a).toString();
	}
}

