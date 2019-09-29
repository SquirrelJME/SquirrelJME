// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * Consumes a value which may throw an exception.
 *
 * @param <V> The value to consume.
 * @param <S> A secondary value that may be passed.
 * @param <E> The exception to potentially throw.
 * @since 2016/10/27
 */
public interface Consumer<V, S, E extends Exception>
{
	/**
	 * Accepts a value.
	 *
	 * @param __v The value to access.
	 * @param __s An optional secondary value.
	 * @throws E If this given exception type is thrown.
	 * @since 2016/10/27
	 */
	public abstract void accept(V __v, S __s)
		throws E;
}

