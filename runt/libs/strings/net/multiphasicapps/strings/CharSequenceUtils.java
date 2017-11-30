// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.strings;

import net.multiphasicapps.collections.IntegerList;

/**
 * This contains utilities which operate on character sequences.
 *
 * @since 2017/11/30
 */
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
	 * Splits the specified character sequence using the given delimeter and
	 * returns all of the fields which are contained within. Extra whitespace
	 * within fields are not trimmed.
	 *
	 * @param __delim The delimeter to split fields by.
	 * @param __s The sequence to split.
	 * @return An array containing all of the fields.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public static final CharSequence[] fieldSplit(char __delim,
		CharSequence __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns an array containing all of the indexes that the specified
	 * character appears in the given sequence.
	 *
	 * @param __s The sequence to check in.
	 * @parma __c The character to get the indexes for.
	 * @return An array containing the array indexes for the given character,
	 * if there are none then the array will be empty.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public static final int[] multipleIndexOf(CharSequence __s, char __c)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		IntegerList list = new IntegerList();
		
		// Find every character index
		for (int i = 0, n = __s.length(), lastdx = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Add index to list if found
			if (c == __c)
				list.addInteger(i);
		}
		
		// Finish
		return list.toIntegerArray();
	}
}

