// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.Collection;
import java.util.HashSet;

/**
 * This is a set which uses the identity of objects for comparison rather than
 * the actual equality of said objects.
 *
 * @param <T> The type of element to store in the set.
 * @since 2017/12/28
 */
public final class IdentityHashSet<T>
	extends __IdentityBaseSet__<T>
{
	/**
	 * Initializes an empty set.
	 *
	 * @since 2017/12/28
	 */
	public IdentityHashSet()
	{
		super(new HashSet<Identity<T>>());
	}
	
	/**
	 * Initializes a set copied from the other collection.
	 *
	 * @param __from The collection to copy values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public IdentityHashSet(Collection<? extends T> __from)
		throws NullPointerException
	{
		super(new HashSet<Identity<T>>(), __from);
	}
}

