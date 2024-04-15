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
 * This interface is used to set two classes as being comparable to other
 * classes.
 *
 * It is not required to but comparisons performed by this interface should
 * be comparable to {@link Object#equals(Object)} so that
 * {@code a.equals(b) == (a.compareTo(b) == 0)}.
 *
 * It is recommended to implement this class so that
 * {@code a.compareTo(b) == -b.compareTo(a)} is true, this makes the
 * comparisons consistent. However this might not be feasible for speed
 * purposes so it is highly recommended to at least have comparible negative,
 * zero, and positive.
 *
 * @param <T> The type to compare.
 * @since 2018/09/19
 */
@Api
public interface Comparable<T>
{
	/**
	 * Compares this instance to the argument passed, 
	 *
	 * Note that {@code NullPointerException} should be thrown for {@code __b}
	 * since {@code __b.compareTo(this)} would thrown an exception.
	 *
	 * @param __b The object to compare against.
	 * @return The result of the comparison, negative values mean
	 * {@code this < __b}, zero means {@code this == __b}, and positive values
	 * mean {@code this > __b}.
	 * @since 2018/09/19
	 */
	@Api
	int compareTo(T __b);
}


