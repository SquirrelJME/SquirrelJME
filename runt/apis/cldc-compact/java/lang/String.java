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
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;
import cc.squirreljme.runtime.cldc.i18n.DefaultLocale;
import cc.squirreljme.runtime.cldc.i18n.Locale;
import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Decoder;
import cc.squirreljme.runtime.cldc.string.BasicSequence;
import cc.squirreljme.runtime.cldc.string.CharArraySequence;
import cc.squirreljme.runtime.cldc.string.CharSequenceSequence;
import cc.squirreljme.runtime.cldc.string.EmptySequence;
import cc.squirreljme.runtime.cldc.string.SubBasicSequenceSequence;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A {@link String} represents a sequence of characters which make up a group
 * of alphanumeric or other symbols and represents text.
 *
 * Note that only {@link #toLowerCase()} and {@link #toUpperCase()} take
 * locale into considering during their conversion.
 *
 * @since 2018/09/16
 */
@ImplementationNote("The internal representation of characters in this " +
	"class is not a concrete character array, instead the strings are " +
	"handled by classes which represent sequences and such. This allows " +
	"for potential compression or representation of different strings " +
	"without needing to up the complexity of this class greatly. There " +
	"is slight overhead, but it will allow for Strings to be for the most " +
	"part be directly mapped into memory which will be the case when " +
	"stuff from the string table is to be used.")
public final class String
	implements Comparable<String>, CharSequence
{
	/** The ISO encoding. */
	private static final String _ENCODING_ISO =
		"iso-8859-1";
	
	/** The UTF-8 encoding. */
	private static final String _ENCODING_UTF =
		"utf-8";
	
	/** The minumum trim character. */
	private static final char _MIN_TRIM_CHAR =
		' ';
	
	/** Is this string already lowercased? */
	private static final int _QUICK_ISLOWER =
		0b0000_0000__0000_0000___0000_0000__0000_0001;
	
	/** Is this string already uppercased? */
	private static final int _QUICK_ISUPPER =
		0b0000_0000__0000_0000___0000_0000__0000_0010;
	
	/** The basic character sequence data. */
	private final BasicSequence _sequence;
	
	/** Quick determination flags for speedy operations. */
	private volatile int _quickflags;
	
	/** The hash code for this string, is cached. */
	private int _hashcode;
	
	/**
	 * Initializes a new empty string.
	 *
	 * @since 2018/02/24
	 */
	public String()
	{
		this(EmptySequence.INSTANCE);
	}
	
	public String(String __a)
	{
		this(new CharSequenceSequence(__a));
	}
	
	/**
	 * Initializes a string which uses characters which are a copy of the given
	 * character array.
	 *
	 * @param __c The characters to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
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
		this._sequence = new CharArraySequence(copy);
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
	public String(byte[] __b, String __e)
		throws NullPointerException, UnsupportedEncodingException
	{
		this(__b, 0, __b.length, CodecFactory.decoder(__e));
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
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/11/06
	 */
	public String(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		this(__b, __o, __l, CodecFactory.defaultDecoder());
	}
	
	/**
	 * Decodes the given bytes to a string using the specified encoding.
	 *
	 * @param __b The input bytes to decode.
	 * @param __e The encoding to use when decoding the bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	public String(byte[] __b)
		throws NullPointerException
	{
		this(__b, 0, __b.length, CodecFactory.defaultDecoder());
	}
	
	public String(StringBuffer __a)
		throws NullPointerException
	{
		this(__a.toString());
	}
	
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
	private String(byte[] __b, int __o, int __l, Decoder __dec)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null || __dec == null)
			throw new NullPointerException("NARG");
		int bn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bn)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Setup a temporary character array with average sequence length to
		// hopefully have enough room to store the decoded characters
		int cap = (int)(__l * __dec.averageSequenceLength()),
			at = 0;
		char[] out = new char[cap];
		
		// Start decoding input sequences
		for (int i = __o, e = i + __l; i < e;)
		{
			// Decode sequence (the decoded char is in the low 16-bits)
			int left = e - i,
				rc = __dec.decode(__b, i, left);
			
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
		
		// Just allocate an exact buffer since the estimate could have been off
		if (at != cap)
			out = Arrays.copyOf(out, at);
		this._sequence = new CharArraySequence(out);
	}
	
	/**
	 * Initializes the string using the given sequence for characters.
	 *
	 * @param __bs The sequence of characters to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	String(BasicSequence __bs)
		throws NullPointerException
	{
		this(__bs, 0);
	}
	
	/**
	 * Initializes the string using the given sequence for characters.
	 *
	 * @param __bs The sequence of characters to use.
	 * @param __qf Quick determination flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	private String(BasicSequence __bs, int __qf)
		throws NullPointerException
	{
		if (__bs == null)
			throw new NullPointerException("NARG");
		
		this._sequence = __bs;
		this._quickflags = __qf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public char charAt(int __i)
		throws IndexOutOfBoundsException
	{
		// Bounds checking is handled by the sequence
		return this._sequence.charAt(__i);
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
	 * @param __os The string to compare against.
	 * @return A negative value if this string precedes the other string, a
	 * positive value if this string procedes the other string, or zero if the
	 * strings are equal.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/02
	 */
	public int compareTo(String __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Get both string lengths
		int an = this.length();
		int bn = __os.length();
		
		// Max comparison length
		int max = Math.min(an, bn);
		
		// Compare both strings
		for (int i = 0; i < max; i++)
		{
			// Get character difference
			int diff = ((int)this.charAt(i)) - ((int)__os.charAt(i));
			
			// If there is a difference, then return it
			if (diff != 0)
				return diff;
		}
		
		// Remaining comparison is the length parameter, shorter strings are
		// first
		return an - bn;
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
	public int compareToIgnoreCase(String __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Get both string lengths
		int an = this.length();
		int bn = __o.length();
		
		// Max comparison length
		int max = Math.min(an, bn);
		
		// Compare both strings
		for (int i = 0; i < max; i++)
		{
			// Get both characters and normalize case
			char ca = Character.toLowerCase(
					Character.toUpperCase(this.charAt(i))),
				cb = Character.toLowerCase(
					Character.toUpperCase(__o.charAt(i)));
			
			// Get character difference
			int diff = ca - cb;
			
			// If there is a difference, then return it
			if (diff != 0)
				return diff;
		}
		
		// Remaining comparison is the length parameter, shorter strings are
		// first
		return an - bn;
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
	public String concat(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Short circuits to not do anything
		if (this.length() == 0)
			return __s;
		else if (__s.length() == 0)
			return this;
		
		// Just build a new string this way because it is much simpler than
		// having to mess with an internal representation of specially
		// encoded strings
		StringBuilder sb = new StringBuilder(this);
		sb.append(__s);
		return sb.toString();
	}
	
	public boolean contains(CharSequence __s)
	{
		throw new todo.TODO();
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
	public boolean contentEquals(CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Synchronize on StringBuffers
		if (__s instanceof StringBuffer)
			return contentEquals((StringBuffer)__s);
		
		return __contentEquals(__s);
	}
	
	public boolean endsWith(String __a)
	{
		throw new todo.TODO();
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
		
		// Cast
		String o = (String)__o;
		
		// Quickly determine if the string is likely the same via the hashcode
		// This at best removes all loops and just results in a simple integer
		// comparison being used
		int an = this.hashCode(),
			bn = o.hashCode();
		if (an != bn)
			return false;
		
		// Work on sequences now
		BasicSequence sa = this._sequence,
			sb = o._sequence;
		
		// If the sequence is the same then this represents the same string
		if (sa == sb)
			return true;
		
		// If the length differs, they are not equal
		an = sa.length();
		bn = sb.length();
		if (an != bn)
			return false;
		
		// Compare individual characters
		for (int i = 0; i < an; i++)
			if (sa.charAt(i) != sb.charAt(i))
				return false;
		
		// Would be a match!
		return true;
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
	@ProgrammerTip("Locale is not considered.")
	public boolean equalsIgnoreCase(String __o)
	{
		// Always false
		if (__o == null)
			return false;
		
		// Directly use sequences, is faster
		BasicSequence sa = this._sequence,
			sb = __o._sequence;
		
		// Two strings of inequal length will never be the same
		int n = sa.length();
		if (n != sb.length())
			return false;
		
		// Check characters
		for (int i = 0; i < n; i++)
		{
			char a = sa.charAt(i),
				b = sb.charAt(i);
			
			// Is a different character?
			if (a != b &&
				Character.toUpperCase(a) != Character.toUpperCase(b) &&
				Character.toLowerCase(b) != Character.toLowerCase(b))
				return false;
		}
		
		// The same
		return true;
	}
	
	/**
	 * Translates this string using into a byte array using the specified
	 * character encoding.
	 *
	 * @param __enc The character encoding to use.
	 * @return A byte array with the characters of this string converted to
	 * bytes.
	 * @throws NullPointerException If no encoding was specified.
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] getBytes(String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		// Check
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	public byte[] getBytes()
	{
		// Wrap it
		try
		{
			return getBytes(System.getProperty("microedition.encoding"));
		}
		
		// {@squirreljme.error ZZ0x The default encoding is not supported by
		// the virtual machine.}
		catch (UnsupportedEncodingException uee)
		{
			throw new AssertionError("ZZ0x");
		}
	}
	
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public int hashCode()
	{
		// If the hashcode was already determined before then use that
		// cache
		int rv = this._hashcode;
		if (rv != 0)
			return rv;
		
		// Calculate the hashCode(), the JavaDoc gives the following formula:
		// == s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] .... yikes!
		BasicSequence sequence = this._sequence;
		for (int i = 0, n = sequence.length(); i < n; i++)
			rv = ((rv << 5) - rv) + sequence.charAt(i);
		
		// Cache hashcode for later
		this._hashcode = rv;
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
	public int indexOf(int __c, int __i)
	{
		BasicSequence sequence = this._sequence;
		
		// Cap index
		int n = sequence.length();
		if (__i < 0)
			__i = 0;
		
		for (int i = __i; i < n; i++)
			if (__c == sequence.charAt(i))
				return i;
		
		// Not found
		return -1;
	}
	
	public int indexOf(String __a)
	{
		throw new todo.TODO();
	}
	
	public int indexOf(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the unique string instance used for the current string, if
	 * the current string is not within the internal map then it is added. If
	 * it already exists in the map then that pre-existing value is returned.
	 * The purpose of this method is for potential optimizations where there
	 * are a large number of long-term string objects in memory which may be
	 * duplicated in many places (such as in a database). As such, only
	 * persistant strings should be interned, never short lived strings.
	 *
	 * Although this may be used for {@code ==} to work, it is not recommended
	 * to use this method for such things.
	 *
	 * @return The unique string instance.
	 * @since 2016/04/01
	 */
	public String intern()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if this string is empty.
	 *
	 * @return {@code true} if this string is empty.
	 * @since 2017/08/15
	 */
	public boolean isEmpty()
	{
		return this.length() == 0;
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
		return this.lastIndexOf(__c, this.length() - 1);
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
		BasicSequence sequence = this._sequence;
		int n = sequence.length();
		if (__dx >= n)
			__dx = n - 1;
		
		for (; __dx >= 0; __dx--)
			if (__c == sequence.charAt(__dx))
				return __dx;
		
		// Not found
		return -1;
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
	 * Returns the length of this string.
	 *
	 * @return The length of this string.
	 * @since 2018/09/19
	 */
	public int length()
	{
		return this._sequence.length();
	}
	
	public boolean regionMatches(int __a, String __b, int __c, int __d)
	{
		throw new todo.TODO();
	}
	
	public boolean regionMatches(boolean __a, int __b, String __c, int __d,
		int __e)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns a string with all characters which match the starting character
	 * which are then replaced with the replacement character.
	 *
	 * @param __a The starting character.
	 * @param __b The replacement character.
	 * @since 2018/09/22
	 */
	public String replace(char __a, char __b)
	{
		// If a character is going to be replaced with itself then no
		// replacement has to actually be performed.
		if (__a == __b)
			return this;
		
		// Get source sequence
		BasicSequence bs = this._sequence;
		int n = bs.length();
		
		// Copy data into an array with translated characters
		char[] rv = new char[n];
		for (int i = 0; i < n; i++)
		{
			char c = bs.charAt(i);
			if (c == __a)
				c = __b;
			rv[i] = c;
		}
		
		// Build new string
		return new String(new CharArraySequence(rv));
	}
	
	/**
	 * Checks if this string starts with the other string at the given index.
	 *
	 * @param __s The string to check for a starting match.
	 * @param __sdx The starting index to start checking at.
	 * @return If this string starts with the given string.
	 * @throws IndexOutOfBoundsException
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
	public boolean startsWith(String __s, int __sdx)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0y Starting index in string is out of
		// bounds. (The starting index)}
		if (__sdx < 0)
			throw new IndexOutOfBoundsException(
				String.format("ZZ0y %d", __sdx));
		
		// Need to work on both sequences
		BasicSequence sa = this._sequence,
			sb = __s._sequence;
		
		// If the second string is empty then it will always match
		int na = sa.length(),
			nb = sb.length();
		if (nb == 0)
			return true;
		
		// The second string cannot even fit from this index so do not bother
		// checking anything
		if (__sdx + nb > na)
			return false;
		
		// Find false match
		for (int ia = __sdx, ib = 0; ib < nb; ia++, ib++)
			if (sa.charAt(ia) != sb.charAt(ib))
				return false;
		
		// False not found, so it matches
		return true;
	}
	
	/**
	 * Checks if this string starts with the other string.
	 *
	 * @param __s The string to check for a starting match.
	 * @return If this string starts with the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/22
	 */
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
	 * @throws IndexOutOfBoundsException If the start is outside of the bounds.
	 * @since 2018/11/04
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
	 * @since 2018/09/29
	 */
	public String substring(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// The entire string region requires no new string
		BasicSequence sequence = this._sequence;
		int n = sequence.length();
		if (__s == 0 && __e == n)
			return this;
		
		// A blank string with no characters
		if (__s == __e)
			return "";
		
		// {@squirreljme.error ZZ0z String substring is outside of bounds.
		// (The start index; The end index; The length)}
		if (__s < 0 || __s > __e || __e > n)
			throw new IndexOutOfBoundsException("ZZ0z " + __s + " " + __e +
				" " + n);
		
		// Derive sub-sequence
		return new String(sequence.subSequence(__s, __e));
	}
	
	/**
	 * Returns a character array which contains every character within this
	 * string.
	 *
	 * @return A character array containing the characters of this string.
	 * @since 2017/08/15
	 */
	public char[] toCharArray()
	{
		// The sequence may have a faster way to setup a char array
		return this._sequence.toCharArray();
	}
	
	/**
	 * Translates this string to lowercase using the current locale.
	 *
	 * Java ME specifies that only Latin-1 characters are supported
	 *
	 * @return The lowercased result of this string.
	 * @since 2018/09/20
	 */
	public String toLowerCase()
	{
		// If this string is lowercased already do not mess with it
		if ((this._quickflags & String._QUICK_ISLOWER) != 0)
			return this;
		
		// Needed for case conversion
		BasicSequence sequence = this._sequence;
		Locale locale = DefaultLocale.defaultLocale();
		
		// Setup new character array for the conversion
		int n = this.length();
		char[] rv = new char[n];
		
		// Copy and convert characters
		boolean changed = false;
		for (int i = 0; i < n; i++)
		{
			char a = sequence.charAt(i),
				b = locale.toLowerCase(a);
			
			// Detect if the string actually changed
			if (!changed && a != b)
				changed = true;
			
			rv[i] = b;
		}
		
		// String was unchanged, so forget about the array we just handled and
		// set that the string is lowercase
		if (!changed)
		{
			this._quickflags |= String._QUICK_ISLOWER;
			return this;
		}
		
		// New string will be lowercase, so ignore this operation
		return new String(new CharArraySequence(rv), String._QUICK_ISLOWER);
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
	public String toUpperCase()
	{
		// If this string is uppercased already do not mess with it
		if ((this._quickflags & String._QUICK_ISUPPER) != 0)
			return this;
		
		// Needed for case conversion
		BasicSequence sequence = this._sequence;
		Locale locale = DefaultLocale.defaultLocale();
		
		// Setup new character array for the conversion
		int n = this.length();
		char[] rv = new char[n];
		
		// Copy and convert characters
		boolean changed = false;
		for (int i = 0; i < n; i++)
		{
			char a = sequence.charAt(i),
				b = locale.toUpperCase(a);
			
			// Detect if the string actually changed
			if (!changed && a != b)
				changed = true;
			
			rv[i] = b;
		}
		
		// String was unchanged, so forget about the array we just handled and
		// set that the string is lowercase
		if (!changed)
		{
			this._quickflags |= String._QUICK_ISUPPER;
			return this;
		}
		
		// New string will be uppercase, so ignore this operation
		return new String(new CharArraySequence(rv), String._QUICK_ISUPPER);
	}
	
	/**
	 * This trims all of the low ASCII whitespace and control characters at
	 * the start and the end of this string and returns a new string with
	 * the trimmed whitespace.
	 *
	 * This does not handle any other potential characters which may act as
	 * whitespace in the high unicode range and only handles the first 32
	 * ASCII characters.
	 *
	 * @return A string with the whitespace trimmed, if the string does not
	 * start or end in whitespace then {@code this} is returned.
	 * @since 2016/04/20
	 */
	public String trim()
	{
		// Empty strings do not need trimming
		BasicSequence sequence = this._sequence;
		int n = sequence.length();
		if (n <= 0)
			return this;
		
		// Find starting trim position
		int s;
		for (s = 0; s < n && sequence.charAt(s) <= _MIN_TRIM_CHAR; s++)
			;
		
		// Find ending trim position
		int e;
		for (e = n - 1; e > s && sequence.charAt(e) <= _MIN_TRIM_CHAR; e--)
			;
		
		// Return trimmed variant of it
		return substring(s, e);
	}
	
	/**
	 * Checks to see if this string matches the target sequence.
	 *
	 * @param __s The input sequence to check against.
	 * @return If they are the same or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/15
	 */
	private final boolean __contentEquals(CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// If the two have different lengths they will never be equal
		int al = this.length(),
			bl = __s.length();
		if (al != bl)
			return false;
		
		// Check each character
		for (int i = 0; i < al; i++)
			if (this.charAt(i) != __s.charAt(i))
				return false;
		
		// If reached, they are equal
		return true;
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
	 * @throws IllegalArgumentException If the format is not correct.
	 * @throws NullPointerException On null arguments.
	 * @see java.util.Formatter
	 * @since 2018/11/02
	 */
	public static String format(String __fmt, Object... __args)
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
	
	public static String valueOf(char[] __a)
		throws NullPointerException
	{
		return valueOf(__a, 0, (__a != null ? __a.length : 0));
	}
	
	public static String valueOf(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	public static String valueOf(boolean __a)
	{
		return Boolean.valueOf(__a).toString();
	}
	
	public static String valueOf(char __a)
	{
		return Character.valueOf(__a).toString();
	}
	
	public static String valueOf(int __a)
	{
		return Integer.valueOf(__a).toString();
	}
	
	public static String valueOf(long __a)
	{
		return Long.valueOf(__a).toString();
	}
	
	public static String valueOf(float __a)
	{
		return Float.valueOf(__a).toString();
	}
	
	public static String valueOf(double __a)
	{
		return Double.valueOf(__a).toString();
	}
}

