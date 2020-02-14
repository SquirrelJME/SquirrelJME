// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.ref;

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;

/**
 * A weak reference is one which is garbage collected when there are no strong
 * references to it.
 *
 * @param <T> The type of object to store.
 * @since 2018/09/23
 */
public class WeakReference<T>
	extends Reference<T>
{
	/**
	 * Initializes a reference pointing to the given object.
	 *
	 * @param __v The object to point to, may be {@code null}.
	 * @since 2018/09/23
	 */
	public WeakReference(T __v)
	{
		super(ObjectAccess.newWeakReference(), __v, null);
	}
	
	/**
	 * Initializes a reference pointing to the given object and an optionally
	 * specified queue to place this reference into when garbage collection
	 * occurs.
	 *
	 * @param __v The object to point to, may be {@code null}.
	 * @param __q When the given object is garbage collected the specified
	 * queue will be given this reference (not {@link __v} itself}, may be
	 * {@code null}
	 * @since 2018/09/23
	 */
	public WeakReference(T __v, ReferenceQueue<? super T> __q)
	{
		super(ObjectAccess.newWeakReference(), __v, __q);
	}
}

