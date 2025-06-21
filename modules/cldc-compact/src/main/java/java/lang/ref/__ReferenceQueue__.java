// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.ref;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Reference queue base, this class mostly exists so that IDEs do not
 * have errors with {@link ReferenceQueue}.
 *
 * @param <T> The type to enqueue references for.
 * @since 2025/06/21
 */
abstract class __ReferenceQueue__<T>
{
	/** Internal queue of references. */
	final Deque<Reference<? extends T>> _queue =
		new LinkedList<>();
	
	/**
	 * Enqueues the reference into this queue.
	 *
	 * @param __ref The reference to enqueue.
	 * @since 2018/09/23
	 */
	@SuppressWarnings("unused")
	final void __enqueue(Reference<? extends T> __ref)
	{
		// Just ignore and do nothing
		if (__ref == null)
			return;
		
		// Lock on the queue to add it
		Deque<Reference<? extends T>> queue = this._queue;
		synchronized (this)
		{
			queue.add(__ref);
			
			// Signal all waiting threads, one will grab it
			queue.notifyAll();
		}
	}
}
