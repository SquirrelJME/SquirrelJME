// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * This is a set which uses the identity of objects for comparison rather than
 * the actual equality of said objects. The order of elements in the set is
 * linked according to {@link java.util.LinkedHashSet}.
 *
 * @param <T> The type of element to store in the set.
 * @since 2017/12/28
 */
public final class IdentityLinkedHashSet<T>
	extends __IdentityBaseSet__<T>
{
	/**
	 * Initializes an empty set.
	 *
	 * @since 2017/12/28
	 */
	public IdentityLinkedHashSet()
	{
		super(new LinkedHashSet<__IdentityWrapper__<T>>());
	}
	
	/**
	 * Initializes a set copied from the other collection.
	 *
	 * @param __from The collection to copy values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public IdentityLinkedHashSet(Collection<? extends T> __from)
		throws NullPointerException
	{
		super(new LinkedHashSet<__IdentityWrapper__<T>>(), __from);
	}
}

