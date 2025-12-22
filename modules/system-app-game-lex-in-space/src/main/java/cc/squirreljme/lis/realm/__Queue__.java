// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.realm;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Generic object queue handling.
 *
 * @param <T> The type of memory object to store.
 * @since 2025/12/21
 */
final class __Queue__<T extends MemoryObject<T>>
{
	/** Reference to self, keeps everything normalized. */
	final Reference<__Queue__<T>> _self =
		new WeakReference<>(this);
	
	/** Unclaimed memory objects. */
	private final Queue<T> _stack =
		new ArrayDeque<>();
	
	/**
	 * Returns the next unclaimed object.
	 *
	 * @return The next unclaimed object, or {@code null} if there are no
	 * unclaimed objects.
	 * @since 2025/12/21
	 */
	T __next()
	{
		synchronized (this)
		{
			// Just read from the next item
			return this._stack.poll();
		}
	}
}
