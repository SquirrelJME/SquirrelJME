// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.List;

/**
 * This contains some utilities for arrays.
 *
 * @since 2017/11/30
 */
public final class ArrayUtils
{
	/**
	 * Not used.
	 *
	 * @since 2017/11/30
	 */
	private ArrayUtils()
	{
	}
	
	/**
	 * Returns an unmodifiable list over the given array.
	 *
	 * @param <T> The type of list to return.
	 * @param __e The array to wrap.
	 * @return The unmodifiable list.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	@SuppressWarnings({"unchecked"})
	public static final <T> List<T> unmodifiableList(T... __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		return new UnmodifiableArrayList<T>(__e, 0, __e.length);
	}
}

