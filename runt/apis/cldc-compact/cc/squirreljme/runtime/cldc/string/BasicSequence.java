// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.string;

/**
 * This interface is used for providing access to the data contained within
 * {@link java.lang.String}. It is similar to {@link java.lang.CharSequence}
 * except that it only has valid length and characters.
 *
 * Some strings may refer to characters which have been mapped to another
 * address space and as such will require remote access to the memory of
 * that process.
 *
 * @since 2018/02/24
 */
public abstract class BasicSequence
{
	/**
	 * Returns the character at the given index.
	 *
	 * @param __i The index of the character.
	 * @return The character at the given index.
	 * @throws StringIndexOutOfBoundsException If the index is outside of
	 * bounds.
	 * @since 2018/02/24
	 */
	public abstract char charAt(int __i)
		throws StringIndexOutOfBoundsException;
	
	/**
	 * Returns the length of the string.
	 *
	 * @return The string length.
	 * @since 2018/02/24
	 */
	public abstract int length();
	
	/**
	 * Returns the subsequence of this sequence.
	 *
	 * @param __s The starting point.
	 * @param __e The ending point.
	 * @return The subsequence.
	 * @throws IndexOutOfBoundsException If the sequence is out of bounds.
	 * @since 2018/12/01
	 */
	public SubBasicSequenceSequence subSequence(int __s, int __e)
		throws IndexOutOfBoundsException
	{
		return new SubBasicSequenceSequence(this, __s, __e);
	}
	
	/**
	 * Converts this basic sequence to a character array.
	 *
	 * @return The resulting character array.
	 * @since 2018/12/04
	 */
	public char[] toCharArray()
	{
		int len = this.length();
		char[] rv = new char[len];
		
		for (int i = 0; i < len; i++)
			rv[i] = this.charAt(i);
		
		return rv;
	}
}

