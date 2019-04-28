// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This contains static methods which are very similar to existing C
 * methods. These are not implemented in C, however with many C based APIs
 * and such when it comes to native systems it is very possible that these
 * will be needed. All of these do however use native {@link Assembly}
 * functions to perform all of their native magic.
 *
 * @since 2019/04/28
 */
public final class C
{
	/**
	 * Compares two regions of memory.
	 *
	 * @param __s1 Region 1.
	 * @param __s2 Region 2.
	 * @param __n The number of values to compare.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int memcmp(int __s1, int __s2, int __n)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Copies memory from one address to another.
	 *
	 * @param __dest The destination.
	 * @param __src The source.
	 * @param __n The number of bytes to copy.
	 * @return {@code __dest}.
	 * @since 2019/04/28
	 */
	public static final int memmove(int __dest, int __src, int __n)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the memory at the given region to the specified value.
	 *
	 * @param __s The pointer to set.
	 * @param __c The value to set.
	 * @param __n The number of bytes to set.
	 * @return {@code __s}.
	 * @since 2019/04/28
	 */
	public static final int memset(int __s, int __c, int __n)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory, without regard to case.
	 *
	 * @param __s1 String 1.
	 * @param __s2 String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strcasecmp(int __s1, int __s2)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Concatenates the string from the source onto the destination at the
	 * end.
	 *
	 * @param __dest The destination.
	 * @param __src The source.
	 * @return {@code __dest}.
	 * @since 2019/04/28 
	 */
	public static final int strcat(int __dest, int __src)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Finds the first occurance of the character in the string.
	 *
	 * @param __s The string to look in.
	 * @param __c The character to find.
	 * @return A pointer to that character or {@code 0} if it was not found.
	 * @since 2019/04/28
	 */
	public static final int strchr(int __s, int __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory.
	 *
	 * @param __s1 String 1.
	 * @param __s2 String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strcmp(int __s1, int __s2)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory, without regard to case.
	 *
	 * @param __s1 String 1.
	 * @param __s2 String 2.
	 * @param __n The maximum number of characters to compare.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strncasecmp(int __s1, int __s2, int __n)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Concatenates the string from the source onto the destination at the
	 * end.
	 *
	 * @param __dest The destination.
	 * @param __src The source.
	 * @param __n The maximum size of the destination string buffer.
	 * @return {@code __dest}.
	 * @since 2019/04/28 
	 */
	public static final int strncat(int __dest, int __src, int __n)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory.
	 *
	 * @param __s1 String 1.
	 * @param __s2 String 2.
	 * @param __n The maximum number of characters to compare.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strncmp(int __s1, int __s2, int __n)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Compares two UTF encoded in the {@link java.io.DataInputStream} and
	 * {@link java.io.DataOutputStream} format.
	 *
	 * @param __s1 String 1.
	 * @param __s2 String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int utfstrcmp(int __s1, int __s2)
	{
		throw new todo.TODO();
	}
}

