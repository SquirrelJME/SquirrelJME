// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This provides support for linear iteration through unspecified elements.
 *
 * Generally, {@link Iterator}s are not meant to be shared across threads.
 *
 * This class should be used instead of {@link Enumeration}.
 *
 * @param <E> The element to iterate over.
 * @see Iterable
 * @since 2016/04/12
 */
public interface Iterator<E>
{
	/**
	 * Returns {@code true} if there are more elements in the iteration
	 * sequence.
	 *
	 * @return {@code true} if more elements are available, otherwise
	 * {@code false}.
	 * @since 2016/04/12
	 */
	public abstract boolean hasNext();
	
	/**
	 * Returns the next element in the iteration or throws an exception if
	 * there are none remaining.
	 *
	 * @return The next element in the iteration.
	 * @throws NoSuchElementException If no more elements are available.
	 * @since 2016/04/12
	 */
	public abstract E next()
		throws NoSuchElementException;
	
	/**
	 * This removes the last element which was returned by {@link #next()}. If
	 * removal is supported then it may only be performed once per call to
	 * {@link #next()}.
	 *
	 * @throws IllegalStateException If the object returned by {@code #next()}
	 * was already removed or said method was never called.
	 * @throws UnsupportedOperationException If removal of elements is not
	 * supported by this iteration.
	 * @since 2016/04/12
	 */
	public abstract void remove()
		throws IllegalStateException, UnsupportedOperationException;
}
