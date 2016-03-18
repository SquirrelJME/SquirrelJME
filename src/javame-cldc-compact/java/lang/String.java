// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

public final class String
	implements Comparable<String>, CharSequence
{
	/** The ISO encoding. */
	private static final String _ENCODING_ISO =
		"iso-8859-1";
	
	/** The UTF-8 encoding. */
	private static final String _ENCODING_UTF =
		"utf-8";
	
	public String()
	{
		throw new Error("TODO");
	}
	
	public String(String __a)
	{
		throw new Error("TODO");
	}
	
	public String(char[] __a)
	{
		throw new Error("TODO");
	}
	
	public String(char[] __a, int __o, int __l)
	{
		throw new Error("TODO");
	}
	
	public String(byte[] __a, int __o, int __l, String __e)
		throws NullPointerException, UnsupportedEncodingException
	{
		throw new Error("TODO");
	}
	
	public String(byte[] __a, String __e)
		throws NullPointerException, UnsupportedEncodingException
	{
		this(__a, 0, __a.length, __e);
	}
	
	public String(byte[] __a, int __o, int __l)
	{
		throw new Error("TODO");
	}
	
	public String(byte[] __a)
	{
		throw new Error("TODO");
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
	
	public char charAt(int __a)
	{
		throw new Error("TODO");
	}
	
	public int compareTo(String __a)
	{
		throw new Error("TODO");
	}
	
	public int compareToIgnoreCase(String __a)
	{
		throw new Error("TODO");
	}
	
	public String concat(String __a)
	{
		throw new Error("TODO");
	}
	
	public boolean contains(CharSequence __a)
	{
		throw new Error("TODO");
	}
	
	public boolean contentEquals(StringBuffer __a)
	{
		throw new Error("TODO");
	}
	
	public boolean contentEquals(CharSequence __a)
	{
		throw new Error("TODO");
	}
	
	public boolean endsWith(String __a)
	{
		throw new Error("TODO");
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
		int mlen = length();
		int olen = o.length();
		
		// If the length differs, they are not equal
		if (mlen != olen)
			return false;
		
		throw new Error("TODO");
	}
	
	public boolean equalsIgnoreCase(String __a)
	{
		throw new Error("TODO");
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
		
		throw new Error("TODO");
	}
	
	public byte[] getBytes()
	{
		// Wrap it
		try
		{
			return getBytes(System.getProperty("microedition.encoding"));
		}
		
		// Should not occur
		catch (UnsupportedEncodingException uee)
		{
			throw new RuntimeException("WTFX", uee);
		}
	}
	
	public void getChars(int __a, int __b, char[] __c, int __d)
	{
		throw new Error("TODO");
	}
	
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public int indexOf(int __a)
	{
		throw new Error("TODO");
	}
	
	public int indexOf(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public int indexOf(String __a)
	{
		throw new Error("TODO");
	}
	
	public int indexOf(String __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public String intern()
	{
		throw new Error("TODO");
	}
	
	public boolean isEmpty()
	{
		throw new Error("TODO");
	}
	
	public int lastIndexOf(int __a)
	{
		throw new Error("TODO");
	}
	
	public int lastIndexOf(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public int lastIndexOf(String __a)
	{
		throw new Error("TODO");
	}
	
	public int lastIndexOf(String __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public int length()
	{
		throw new Error("TODO");
	}
	
	public boolean regionMatches(int __a, String __b, int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	public boolean regionMatches(boolean __a, int __b, String __c, int __d,
		int __e)
	{
		throw new Error("TODO");
	}
	
	public String replace(char __a, char __b)
	{
		throw new Error("TODO");
	}
	
	public boolean startsWith(String __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public boolean startsWith(String __a)
	{
		throw new Error("TODO");
	}
	
	public CharSequence subSequence(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public String substring(int __a)
	{
		throw new Error("TODO");
	}
	
	public String substring(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public char[] toCharArray()
	{
		throw new Error("TODO");
	}
	
	public String toLowerCase()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public String toUpperCase()
	{
		throw new Error("TODO");
	}
	
	public String trim()
	{
		throw new Error("TODO");
	}
	
	public static String copyValueOf(char[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static String copyValueOf(char[] __a)
	{
		throw new Error("TODO");
	}
	
	public static String format(String __a, Object... __b)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(Object __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(char[] __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(char[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(boolean __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(char __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(int __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(long __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(float __a)
	{
		throw new Error("TODO");
	}
	
	public static String valueOf(double __a)
	{
		throw new Error("TODO");
	}
}


