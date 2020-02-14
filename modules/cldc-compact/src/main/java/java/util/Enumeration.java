// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is an old class which duplicates the functionality of {@link Iterator}
 * without having support for removal.
 *
 * Generally, {@link Enumeration}s are not meant to be shared across threads.
 *
 * It is recommended to use {@link Iterator} when writing new code. However
 * this class exists for backwards compatibility with J2ME environments.
 *
 * @param <E> The type of value to iterate over.
 * @see Iterator
 * @since 2016/04/12
 */
public interface Enumeration<E>
{
	/**
	 * Returns {@code true} if there are more elements in the enumeration.
	 *
	 * @return {@code true} if there are more elements available.
	 * @since 2016/04/12
	 */
	public abstract boolean hasMoreElements();
	
	/**
	 * Returns the next element in the enumeration.
	 *
	 * @return The next element.
	 * @throws NoSuchElementException If no more elements remain.
	 * @since 2016/04/12
	 */
	public abstract E nextElement()
		throws NoSuchElementException;
}

