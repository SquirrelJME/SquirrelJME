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
import cc.squirreljme.runtime.cldc.i18n.DefaultLocale;
import cc.squirreljme.runtime.cldc.i18n.Locale;
import cc.squirreljme.runtime.cldc.string.BasicSequence;
import cc.squirreljme.runtime.cldc.string.CharArraySequence;
import cc.squirreljme.runtime.cldc.string.CharSequenceSequence;
import cc.squirreljme.runtime.cldc.string.EmptySequence;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Comparator;
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
		0b0000_0000__0000_0000___0000_0000__0000_0000;
	
	/** The basic character sequence data. */
	private final BasicSequence _sequence;
	
	/** Quick determination flags for speedy operations. */
	private volatile int _quickflags;
	
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
	
	public String(char[] __a)
	{
		throw new todo.TODO();
	}
	
	public String(char[] __a, int __o, int __l)
	{
		throw new todo.TODO();
	}
	
	public String(byte[] __a, int __o, int __l, String __e)
		throws NullPointerException, UnsupportedEncodingException
	{
		throw new todo.TODO();
	}
	
	public String(byte[] __a, String __e)
		throws NullPointerException, UnsupportedEncodingException
	{
		this(__a, 0, __a.length, __e);
	}
	
	public String(byte[] __a, int __o, int __l)
	{
		throw new todo.TODO();
	}
	
	public String(byte[] __a)
	{
		throw new todo.TODO();
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
	 * Initializes the string using the given sequence for characters.
	 *
	 * @param __bs The sequence of characters to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	private String(BasicSequence __bs)
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
	
	public int compareToIgnoreCase(String __a)
	{
		throw new todo.TODO();
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
		
		// Get length of both strings
		int mlen = this.length();
		int olen = o.length();
		
		// If the length differs, they are not equal
		if (mlen != olen)
			return false;
		
		throw new todo.TODO();
	}
	
	public boolean equalsIgnoreCase(String __a)
	{
		throw new todo.TODO();
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
		
		// {@squirreljme.error ZZ04 The default encoding is not supported by
		// the virtual machine.}
		catch (UnsupportedEncodingException uee)
		{
			throw new AssertionError("ZZ04");
		}
	}
	
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public int indexOf(int __a)
	{
		throw new todo.TODO();
	}
	
	public int indexOf(int __a, int __b)
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
	
	public int lastIndexOf(int __a)
	{
		throw new todo.TODO();
	}
	
	public int lastIndexOf(int __a, int __b)
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
	
	public String replace(char __a, char __b)
	{
		// If a character is going to be replaced with itself then no
		// replacement has to actually be performed.
		if (__a == __b)
			return this;
		
		throw new todo.TODO();
	}
	
	public boolean startsWith(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public boolean startsWith(String __a)
	{
		throw new todo.TODO();
	}
	
	public CharSequence subSequence(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public String substring(int __s)
		throws IndexOutOfBoundsException
	{
		// A substring starting at the zero character is the same
		if (__s == 0)
			return this;
		
		// Call other
		return this.substring(__s, length());
	}
	
	public String substring(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		// The entire string region requires no new string
		int n = length();
		if (__s == 0 && __e == n)
			return this;
		
		throw new todo.TODO();
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
		int n = this.length();
		char[] rv = new char[n];
		
		for (int i = 0; i < n; i++)
			rv[i] = this.charAt(i);
		
		return rv;
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
		Locale locale = DefaultLocale.defaultLocale();
		
		// Setup new character array for the conversion
		int n = this.length();
		char[] rv = new char[n];
		
		// Copy and convert characters
		boolean changed = false;
		for (int i = 0; i < n; i++)
		{
			char a = this.charAt(i),
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
	
	public String toUpperCase()
	{
		throw new todo.TODO();
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
		// If there are no viable characters to trim, then return self
		int n = this.length();
		if (n <= 0 ||
			(charAt(0) > _MIN_TRIM_CHAR && charAt(n - 1) > _MIN_TRIM_CHAR))
			return this;
		
		// Find starting trim position
		int s;
		for (s = 0; s < n && charAt(s) <= _MIN_TRIM_CHAR; s++)
			;
		
		// Find ending trim position
		int e;
		for (e = n - 1; e >= 0 && charAt(e) <= _MIN_TRIM_CHAR; e--)
			;
		
		// Return trimmed variant of it
		return substring(s, e + 1);
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
	
	public static String format(String __a, Object... __b)
	{
		throw new todo.TODO();
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

