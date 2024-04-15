// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.ref;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * A weak reference is one which is garbage collected when there are no strong
 * references to it.
 *
 * @param <T> The type of object to store.
 * @since 2018/09/23
 */
@Api
public class WeakReference<T>
	extends Reference<T>
{
	/**
	 * Initializes a reference pointing to the given object.
	 *
	 * @param __v The object to point to, may be {@code null}.
	 * @since 2018/09/23
	 */
	@Api
	public WeakReference(T __v)
	{
		super(__v, null);
	}
	
	/**
	 * Initializes a reference pointing to the given object and an optionally
	 * specified queue to place this reference into when garbage collection
	 * occurs.
	 *
	 * @param __v The object to point to, may be {@code null}.
	 * @param __q When the given object is garbage collected the specified
	 * queue will be given this reference (not {@code __v} itself}, may be
	 * {@code null}
	 * @since 2018/09/23
	 */
	@Api
	public WeakReference(T __v, ReferenceQueue<? super T> __q)
	{
		super(__v, __q);
	}
}

