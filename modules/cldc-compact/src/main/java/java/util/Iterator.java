// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

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
@Api
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
	@Api
	boolean hasNext();
	
	/**
	 * Returns the next element in the iteration or throws an exception if
	 * there are none remaining.
	 *
	 * @return The next element in the iteration.
	 * @throws NoSuchElementException If no more elements are available.
	 * @since 2016/04/12
	 */
	@Api
	E next()
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
	@Api
	void remove()
		throws IllegalStateException, UnsupportedOperationException;
}
