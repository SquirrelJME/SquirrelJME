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
public interface BasicSequence
{
	/**
	 * Returns the character at the given index.
	 *
	 * @param __i The index of the character.
	 * @return The character at the given index.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2018/02/24
	 */
	public abstract char charAt(int __i)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the length of the string.
	 *
	 * @return The string length.
	 * @since 2018/02/24
	 */
	public abstract int length();
}

