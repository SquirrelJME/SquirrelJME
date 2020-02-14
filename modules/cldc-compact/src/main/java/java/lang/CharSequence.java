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

/**
 * This represents a sequence of characters.
 *
 * @since 2018/12/07
 */
public interface CharSequence
{
	/**
	 * Returns the character at the given index.
	 *
	 * @param __i The index to get.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/12/07
	 */
	public abstract char charAt(int __i)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the length of the character sequence.
	 *
	 * @return The length of the sequence.
	 * @since 2018/12/07
	 */
	public abstract int length();
	
	/**
	 * Returns a sub-sequence of this character sequence.
	 *
	 * @param __s The start index.
	 * @param __e The end index.
	 * @throws IndexOutOfBoundsException If the start or end exceed the
	 * sequence bounds or start is greater than end.
	 * @since 2018/12/07
	 */
	public abstract CharSequence subSequence(int __s, int __e)
		throws IndexOutOfBoundsException;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public abstract String toString();
}

