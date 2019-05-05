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
	 * @param __sa Region 1.
	 * @param __sb Region 2.
	 * @param __n The number of values to compare.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int memcmp(int __sa, int __sb, int __n)
	{
		Assembly.breakpoint();
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
		Assembly.breakpoint();
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
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory, without regard to case.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strcasecmp(int __sa, int __sb)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in byte arrays, without regards to case.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/05/05
	 */
	public static final int strcasecmp(byte[] __sa, byte[] __sb)
	{
		Assembly.breakpoint();
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
		Assembly.breakpoint();
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
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strcmp(int __sa, int __sb)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in byte arrays.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/05/05
	 */
	public static final int strcmp(byte[] __sa, byte[] __sb)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the length of the given string.
	 *
	 * @param __s The string pointer.
	 * @return The length of the string.
	 * @since 2019/05/04
	 */
	public static final int strlen(int __s)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory, without regard to case.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @param __n The maximum number of characters to compare.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strncasecmp(int __sa, int __sb, int __n)
	{
		Assembly.breakpoint();
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
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two strings in memory.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @param __n The maximum number of characters to compare.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int strncmp(int __sa, int __sb, int __n)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the length of the given string, up to the maximum number of
	 * bytes.
	 *
	 * @param __s The string pointer.
	 * @param __lim The string limit.
	 * @return The length of the string, this will never exceed {@code __lim}.
	 * @since 2019/05/04
	 */
	public static final int strnlen(int __s, int __lim)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads a UTF string in memory and returns it.
	 *
	 * @param __s The sequence to load.
	 * @return The sequence as a set of bytes.
	 * @since 2019/05/05
	 */
	public static final byte[] utfbytes(int __s)
	{
		// The length of the UTF sequence is variable and might not match
		// UTF-8 encoded byte data with regard to NUL values
		int len = C.utflen(__s);
		byte[] rv = new byte[len];
		
		// Read input characters, pointer offset by two for length
		int at = 0;
		for (int i = 0, p = 2; i < len; i++)
		{
			byte a = (byte)Assembly.memReadByte(__s, p++);
			
			// Single byte, just copies
			if ((a & 0b1000_0000) == 0b0000_0000)
				rv[at++] = a;
			
			// Double byte
			else if ((a & 0b1110_0000) == 0b1100_0000)
			{
				// Read next
				byte b = (byte)Assembly.memReadByte(__s, p++);
				
				// Null byte
				if (a == 0b110_00000 && b == 0b10_000000)
					rv[at++] = 0;
				
				// Copy sequence
				else
				{
					rv[at++] = a;
					rv[at++] = b;
				}
			}
			
			// Triple byte, copy sequence
			else if ((a & 0b1111_0000) == 0b1110_0000)
			{
				rv[at++] = a;
				rv[at++] = (byte)Assembly.memReadByte(__s, p++);
				rv[at++] = (byte)Assembly.memReadByte(__s, p++);
			}
			
			// {@squirreljme.error ZZ3z Invalid UTF sequence.}
			else
				throw new RuntimeException("ZZ3z");
		}
		
		// If the sequence was the same it can be kept
		if (at == len)
			return rv;
		
		// Sequence length did not match, NUL was in string
		byte[] old = rv;
		rv = new byte[at];
		for (int i = 0; i < at; i++)
			rv[i] = old[i];
		return rv;
	}
	
	/**
	 * Compares two UTF encoded strings without regards to case.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/05/04
	 */
	public static final int utfcasecmp(int __sa, int __sb)
	{
		return C.strcasecmp(C.utfbytes(__sa), C.utfbytes(__sb));
	}
	
	/**
	 * Compares a UTF encoded string to a string encoded as UTF-8 bytes without
	 * regards to case.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/05/05
	 */
	public static final int utfcasecmp(int __sa, byte[] __sb)
	{
		return C.strcasecmp(C.utfbytes(__sa), __sb);
	}
	
	/**
	 * Compares two UTF encoded strings.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/04/28
	 */
	public static final int utfcmp(int __sa, int __sb)
	{
		return C.strcmp(C.utfbytes(__sa), C.utfbytes(__sb));
	}
	
	/**
	 * Compares a UTF encoded string to a string encoded as UTF-8 bytes.
	 *
	 * @param __sa String 1.
	 * @param __sb String 2.
	 * @return The result of the comparison in Java terms.
	 * @since 2019/05/05
	 */
	public static final int utfcmp(int __sa, byte[] __sb)
	{
		return C.strcmp(C.utfbytes(__sa), __sb);
	}
	
	/**
	 * Returns the length of the UTF-8 string.
	 *
	 * @param __s The UTF string.
	 * @return The length of the string.
	 * @since 2019/05/05
	 */
	public static final int utflen(int __s)
	{
		// The length of the sequence is always the first
		return Assembly.memReadShort(__s, 0) & 0xFFFF;
	}
}

