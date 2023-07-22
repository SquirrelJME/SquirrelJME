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
 * In SquirrelJME this is just a {@link WeakReference}, note that this is not
 * in Java ME at all and should not be used.
 *
 * @deprecated Only in SquirrelJME, not in standard Java ME 8.
 * @param <T> The type used.
 * @since 2022/06/19
 */
@Deprecated
@Api
public class SoftReference<T>
	extends WeakReference<T>
{
	/**
	 * Initializes a reference pointing to the given object.
	 *
	 * @param __v The object to point to, may be {@code null}.
	 * @deprecated Only in SquirrelJME, not in standard Java ME 8.
	 * @since 2022/06/19
	 */
	@Deprecated
	@Api
	public SoftReference(T __v)
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
	 * @deprecated Only in SquirrelJME, not in standard Java ME 8.
	 * @since 2022/06/19
	 */
	@Deprecated
	@Api
	public SoftReference(T __v, ReferenceQueue<? super T> __q)
	{
		super(__v, __q);
	}
}
