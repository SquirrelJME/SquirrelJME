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
import cc.squirreljme.runtime.cldc.ref.PrimitiveReference;

/**
 * This class represents references which may be referred to using various
 * different means of attachment, as such this family of classes integrates
 * with the garbage collector.
 *
 * @param <T> The type of object to store.
 * @since 2018/09/23
 */
public abstract class Reference<T>
{
	/** The primitive reference used to access the object. */
	private final PrimitiveReference _ref;
	
	/** The queue this reference is in, volatile to be clearned. */
	private volatile ReferenceQueue<? super T> _queue;
	
	/** Has this been enqueued? */
	private volatile boolean _enqueued;
	
	/**
	 * Initializes a reference pointing to the given object and an optionally
	 * specified queue to place this reference into when garbage collection
	 * occurs.
	 *
	 * @param __r The primitive reference storage.
	 * @param __v The object to point to, may be {@code null}.
	 * @param __q When the given object is garbage collected the specified
	 * queue will be given this reference (not {@link __v} itself}, may be
	 * {@code null}
	 * @since 2018/09/23
	 */
	Reference(PrimitiveReference __r, T __v, ReferenceQueue<? super T> __q)
	{
		// Set
		this._ref = __r;
		this._queue = __q;
		
		// Set primitive reference data
		ObjectAccess.referenceSet(__r, __v);
	}
	
	/**
	 * Clears this reference without placing it in the queue.
	 *
	 * @since 2018/09/23
	 */
	public void clear()
	{
		ObjectAccess.referenceSet(this._ref, null);
	}
	
	/**
	 * Places this reference in the queue.
	 *
	 * @return If it was added to the queue then this will return true,
	 * otherwise if there is no queue or it was already added this will
	 * return false.
	 * @since 2018/09/23
	 */
	public boolean enqueue()
	{
		// Already been enqueued
		ReferenceQueue<? super T> queue = this._queue;
		if (this._enqueued || queue == null)
			return false;
		
		// Enqueue it
		queue.__enqueue(this);
		
		// The queue is not needed anymore so there is no need to keep a
		// reference to it around, this will help remove circular references
		// if one forgets to drain the queues.
		this._enqueued = true;
		this._queue = null;
		
		// Was enqueued
		return true;
	}
	
	/**
	 * Returns the object that this reference refers to.
	 *
	 * @return The reference of this object.
	 * @since 2018/09/23
	 */
	@SuppressWarnings({"unchecked"})
	public T get()
	{
		// If the reference was cleared, enqueue it!
		Object rv = ObjectAccess.referenceGet(this._ref);
		if (rv == null)
			this.enqueue();
		
		return (T)rv;
	}
	
	/**
	 * Returns if this reference was enqueued into the reference queue.
	 *
	 * @return If this object was enqueued.
	 * @since 2018/09/23
	 */
	public boolean isEnqueued()
	{
		return this._enqueued;
	}
}

