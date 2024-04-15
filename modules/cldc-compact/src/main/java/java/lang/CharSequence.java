// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This represents a sequence of characters.
 *
 * @since 2018/12/07
 */
@Api
public interface CharSequence
{
	/**
	 * Returns the character at the given index.
	 *
	 * @param __i The index to get.
	 * @return The character at the given index.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/12/07
	 */
	@Api
	char charAt(int __i)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the length of the character sequence.
	 *
	 * @return The length of the sequence.
	 * @since 2018/12/07
	 */
	@Api
	int length();
	
	/**
	 * Returns a sub-sequence of this character sequence.
	 *
	 * @param __s The start index.
	 * @param __e The end index.
	 * @return The sub sequence of this one.
	 * @throws IndexOutOfBoundsException If the start or end exceed the
	 * sequence bounds or start is greater than end.
	 * @since 2018/12/07
	 */
	@Api
	CharSequence subSequence(int __s, int __e)
		throws IndexOutOfBoundsException;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	String toString();
}

