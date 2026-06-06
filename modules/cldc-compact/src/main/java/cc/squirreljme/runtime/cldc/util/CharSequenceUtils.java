// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.Arrays;

/**
 * This contains utilities which operate on character sequences.
 *
 * @since 2017/11/30
 */
@SquirrelJMEVendorApi
public final class CharSequenceUtils
{
	/**
	 * Not used.
	 *
	 * @since 2017/11/30
	 */
	private CharSequenceUtils()
	{
	}
	
	/**
	 * Compares two character sequences.
	 *
	 * @param __a The first sequence.
	 * @param __b The second sequence.
	 * @return The comparison value.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/21
	 */
	@SquirrelJMEVendorApi
	public static int compare(CharSequence __a, CharSequence __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Get both string lengths
		int an = __a.length();
		int bn = __b.length();
		
		// Compare strings, do not pass the higher length
		int max = Math.min(an, bn);
		for (int i = 0; i < max; i++)
		{
			// Get character difference
			int diff = ((int)__a.charAt(i)) - ((int)__b.charAt(i));
			
			// If there is a difference, then return it
			if (diff != 0)
				return diff;
		}
		
		// Remaining comparison is the length parameter, shorter strings are
		// first
		return an - bn;
	}
	
	/**
	 * Compares two character sequences, ignoring case.
	 *
	 * @param __a The first sequence.
	 * @param __b The second sequence.
	 * @return The comparison value.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/21
	 */
	@SquirrelJMEVendorApi
	public static int compareIgnoreCase(CharSequence __a, CharSequence __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Get both string lengths
		int an = __a.length();
		int bn = __b.length();
		
		// Compare strings, do not pass the higher length
		int max = Math.min(an, bn);
		for (int i = 0; i < max; i++)
		{
			// Get both characters and normalize case
			char ca = Character.toLowerCase(
				Character.toUpperCase(__a.charAt(i)));
			char cb = Character.toLowerCase(
				Character.toUpperCase(__b.charAt(i)));
			
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
	 * Checks if the given character sequence ends with the given sequence.
	 *
	 * @param __what The sequence to check.
	 * @param __endsWith If it ends with the given sequence.
	 * @return If it ends with the given sequence.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/21
	 */
	@SquirrelJMEVendorApi
	public static boolean endsWith(CharSequence __what,
		CharSequence __endsWith)
		throws NullPointerException
	{
		if (__what == null || __endsWith == null)
			throw new NullPointerException("NARG");
		
		// Lengths
		int lenWhat = __what.length();
		int lenEnd = __endsWith.length();
		
		// If the other string is empty, it is always a match
		if (lenEnd == 0)
			return true;
		
		// If our string is smaller than the other string then it will not
		// fit and as such, will not match
		if (lenWhat < lenEnd)
			return false;
		
		// Check all characters at the end of the string, we fail if there is
		// a mismatch
		for (int atWhat = lenWhat - lenEnd, atEnd = 0; atWhat < lenWhat;
			 atWhat++, atEnd++)
			if (__what.charAt(atWhat) != __endsWith.charAt(atEnd))
				return false;
		
		// Is a match since nothing failed!
		return true;
	}
	
	/**
	 * Checks if the given character sequences are equal.
	 *
	 * @param __a The first sequence.
	 * @param __b The second sequence.
	 * @return If they are equal or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/21
	 */
	@SquirrelJMEVendorApi
	public static boolean equals(CharSequence __a, CharSequence __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Different length strings will never be equal
		if (__a.length() != __b.length())
			return false;
		
		// Otherwise, same as an equal comparison
		return CharSequenceUtils.compare(__a, __b) == 0;
	}
	
	/**
	 * Checks if the given character sequences are equal, ignoring case.
	 *
	 * @param __a The first sequence.
	 * @param __b The second sequence.
	 * @return If they are equal or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/06
	 */
	@SquirrelJMEVendorApi
	public static boolean equalsIgnoreCase(CharSequence __a, CharSequence __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Different length strings will never be equal
		if (__a.length() != __b.length())
			return false;
		
		// Otherwise, same as an equal, ignoring case, comparison
		return CharSequenceUtils.compareIgnoreCase(__a, __b) == 0;
	}
	
	/**
	 * Splits the specified character sequence using the given delimiter and
	 * returns all the fields which are contained within. Extra whitespace
	 * within fields are not trimmed.
	 *
	 * @param __delim The delimiter to split fields by.
	 * @param __s The sequence to split.
	 * @return An array containing all the fields.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	@SquirrelJMEVendorApi
	public static CharSequence[] fieldSplit(char __delim,
		CharSequence __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Get all indexes of that given character
		int[] ind = CharSequenceUtils.multipleIndexOf(__delim, __s);
		int delCount = ind.length;
		
		int n = delCount + 1;
		CharSequence[] rv = new CharSequence[n];
		for (int l = -1, r = 0, i = 0; i < n; i++, l++, r++)
			rv[i] = __s.subSequence((l >= 0 ? ind[l] + 1 : 0),
				(r < delCount ? ind[r] : __s.length()));
		
		return rv;
	}
	
	/**
	 * Searches the given sequence for the first occurrence of the specified
	 * character.
	 *
	 * @param __c The character to locate.
	 * @param __s The sequence to look inside.
	 * @return The index of the first occurrence.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	@SquirrelJMEVendorApi
	public static int firstIndex(char __c, CharSequence __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		for (int i = 0, n = __s.length(); i < n; i++)
			if (__c == __s.charAt(i))
				return i;
		return -1;
	}
	
	/**
	 * Searches the given sequence for the first occurrence of the specified
	 * characters.
	 *
	 * @param __c The characters to locate.
	 * @param __s The sequence to look inside.
	 * @return The index of the first occurrence.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	@SquirrelJMEVendorApi
	public static int firstIndex(char[] __c, CharSequence __s)
		throws NullPointerException
	{
		if (__c == null || __s == null)
			throw new NullPointerException("NARG");
		
		// For optimization sort the input array to find characters faster
		__c = __c.clone();
		Arrays.sort(__c);
		
		// Forward to one which assumes sorted input
		return CharSequenceUtils.firstIndexSorted(__c, __s);
	}
	
	/**
	 * Searches the given sequence for the first occurrence of the specified
	 * characters.
	 *
	 * @param __c The characters to locate.
	 * @param __s The sequence to look inside.
	 * @return The index of the first occurrence.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	@SquirrelJMEVendorApi
	public static int firstIndex(String __c, CharSequence __s)
		throws NullPointerException
	{
		if (__c == null || __s == null)
			throw new NullPointerException("NARG");
		
		return CharSequenceUtils.firstIndex(__c.toCharArray(), __s);
	}
	
	/**
	 * Searches the given sequence for the first occurrence of the specified
	 * characters. This assumes that the character set has already been
	 * sorted.
	 *
	 * @param __c The characters to locate, this is required to be sorted.
	 * @param __s The sequence to look inside.
	 * @return The index of the first occurrence.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	@SquirrelJMEVendorApi
	public static int firstIndexSorted(char[] __c, CharSequence __s)
		throws NullPointerException
	{
		if (__c == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Go through ever character
		for (int i = 0, n = __s.length(), y = __c.length; i < n; i++)
		{
			// Use binary search because it is faster than checking each
			// and every element
			char c = __s.charAt(i);
			if (Arrays.binarySearch(__c, c) >= 0)
				return i;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns the position where the given string is found.
	 *
	 * @param __src The sequence to look within.
	 * @param __lookFor The sequence to find.
	 * @param __index The starting index.
	 * @return The index of the sequence or {@code -1} if it is not found.
	 * @since 2019/05/14
	 */
	@SquirrelJMEVendorApi
	public static int indexOf(CharSequence __src, CharSequence __lookFor,
		int __index)
	{
		if (__src == null || __lookFor == null)
			throw new NullPointerException("NARG");
		
		// Normalize position
		if (__index < 0)
			__index = 0;
		
		// If the sequence is empty, then it will always be a match
		int srcLen = __src.length();
		int lookLen = __lookFor.length();
		if (lookLen <= 0)
			return __index;
		
		// If the string is longer than ours, then it will never be a match
		if (lookLen > srcLen - __index)
			return -1;
		
		// Do a long complicated loop matching, but we only need to check
		// for as long as the sequence can actually fit
__outer:
		for (int srcAt = __index, lim = (srcLen - lookLen) + 1;
			 srcAt < lim; srcAt++)
		{
			// Check sequence characters
			for (int x = srcAt, b = 0; b < lookLen; x++, b++)
				if (__src.charAt(x) != __lookFor.charAt(b))
					continue __outer;
			
			// Since the inner loop continues to the outer, if this was reached
			// then we know the full sequence was matched
			return srcAt;
		}
		
		// Otherwise, nothing was found because we tried every character
		return -1;
	}
	
	/**
	 * Searches the character sequence for the given character starting from
	 * the given index and returns the index if it was found.
	 *
	 * @param __s The sequence to look within.
	 * @param __c The character to find.
	 * @param __i The index to start at, the values are capped within the
	 * string length.
	 * @return The character index or {@code -1} if it was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/21
	 */
	@SquirrelJMEVendorApi
	public static int indexOf(CharSequence __s, int __c, int __i)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Cap index
		int n = __s.length();
		if (__i < 0)
			__i = 0;
		
		for (int i = __i; i < n; i++)
			if (__c == __s.charAt(i))
				return i;
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns the last occurrence of the given character going backwards from
	 * the given index.
	 *
	 * @param __s The sequence to look within.
	 * @param __c The character to find.
	 * @param __i The index to start at, this is clipped to within the
	 * string bounds accordingly although if it is negative no searching is
	 * done.
	 * @return The last occurrence of the character or {@code -1} if it was
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/29
	 */
	@SquirrelJMEVendorApi
	public static int lastIndexOf(String __s, int __c, int __i)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Never going to find anything at all
		if (__i < 0)
			return -1;
		
		// Cap index
		int n = __s.length();
		if (__i >= n)
			__i = n - 1;
		
		for (; __i >= 0; __i--)
			if (__c == __s.charAt(__i))
				return __i;
		
		// Not found
		return -1;
	}
	
	/**
	 * Returns an array containing all the indexes that the specified
	 * character appears in the given sequence.
	 *
	 * @param __c The character to get the indexes for.
	 * @param __s The sequence to check in.
	 * @return An array containing the array indexes for the given character,
	 * if there are none then the array will be empty.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	@SquirrelJMEVendorApi
	public static int[] multipleIndexOf(char __c, CharSequence __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		IntegerList list = new IntegerList();
		
		// Find every character index
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Add index to list if found
			if (c == __c)
				list.addInteger(i);
		}
		
		// Finish
		return list.toIntegerArray();
	}
	
	/**
	 * Checks if the character sequence starts with the given
	 * character sequence.
	 *
	 * @param __what The sequence to check.
	 * @param __startsWith The sequence to check if this starts with.
	 * @param __startIndex The starting index to check at.
	 * @return If it starts with the specified sequence.
	 * @throws IndexOutOfBoundsException If the starting index is out of
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/22
	 */
	@SquirrelJMEVendorApi
	public static boolean startsWith(CharSequence __what,
		CharSequence __startsWith, int __startIndex)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__what == null || __startsWith == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ1l Starting index in string is out of
		bounds. (The starting index)} */
		if (__startIndex < 0)
			throw new IndexOutOfBoundsException(
				String.format("ZZ1l %d", __startIndex));
		
		// If the second string is empty then it will always match
		int lenWhat = __what.length();
		int lenStart = __startsWith.length();
		if (lenStart == 0)
			return true;
		
		// The second string cannot even fit from this index so do not bother
		// checking anything
		if (__startIndex + lenStart > lenWhat)
			return false;
		
		// Find false match
		for (int atWhat = __startIndex, atStart = 0; atStart < lenStart;
			 atWhat++, atStart++)
			if (__what.charAt(atWhat) != __startsWith.charAt(atStart))
				return false;
		
		// False not found, so it matches
		return true;
	}
}

