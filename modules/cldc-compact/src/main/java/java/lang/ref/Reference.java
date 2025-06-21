// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.ref;

import cc.squirreljme.jvm.mle.ReferenceShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This class represents references which may be referred to using various
 * different means of attachment, as such this family of classes integrates
 * with the garbage collector.
 *
 * @param <T> The type of object to store.
 * @since 2018/09/23
 */
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
@Api
public abstract class Reference<T>
{
	/**
	 * Initializes a reference pointing to the given object and an optionally
	 * specified queue to place this reference into when garbage collection
	 * occurs.
	 *
	 * @param __v The object to point to, may be {@code null}.
	 * @param __q When the given object is garbage collected the specified
	 * queue will be given this reference (not {@code __v} itself}, may be
	 * {@code null}
	 * @since 2020/05/30
	 */
	Reference(T __v, ReferenceQueue<? super T> __q)
	{
		// Initialize and chain the link
		ReferenceShelf.weakInit(this, __v, __q);
	}
	
	/**
	 * Clears this reference without placing it in the queue.
	 *
	 * @since 2018/09/23
	 */
	@Api
	public void clear()
	{
		// Unlink and clear, we do not care about the result of this
		// as this is an unconditional clear
		ReferenceShelf.weakUnlinkAndClear(this);
	}
	
	/**
	 * Places this reference in the queue and removes the reference, if there
	 * is no queue this will be the same as {@link #clear()}.
	 *
	 * @return If it was added to the queue then this will return true,
	 * otherwise if there is no queue, or it was already added this will
	 * return false.
	 * @since 2018/09/23
	 */
	@SuppressWarnings({"unchecked", "DataFlowIssue"})
	@Api
	public boolean enqueue()
	{
		// Clear the link, if no queue is returned either a queue was never
		// requested or it was already enqueued
		ReferenceQueue<? super T> queue =
			ReferenceShelf.weakUnlinkAndClear(this);
		if (queue == null)
			return false;
		
		// Push to the queue
		((__ReferenceQueue__<? super T>)((Object)queue))
			.__enqueue(this);
		return true;
	}
	
	/**
	 * Returns the object that this reference refers to.
	 *
	 * @return The reference of this object.
	 * @since 2018/09/23
	 */
	@Api
	@SuppressWarnings({"unchecked", "DataFlowIssue"})
	public T get()
	{
		// The return value, if this is null then this gets enqueued
		T rv = ReferenceShelf.weakGet(this);
		
		// If null, enqueue this
		if (rv == null)
		{
			// Unlink this reference, if it is already unlinked then it
			// was enqueued in the past or there was no queue
			ReferenceQueue<? super T> queue =
				ReferenceShelf.weakUnlinkAndClear(this);
			if (queue == null)
				return null;
			
			// Push to the queue
			((__ReferenceQueue__<? super T>)((Object)queue))
				.__enqueue(this);
		}
		
		// Return the resultant object
		return rv;
	}
	
	/**
	 * Returns if this reference was enqueued into the reference queue.
	 *
	 * @return If this object was enqueued.
	 * @since 2018/09/23
	 */
	@Api
	public boolean isEnqueued()
	{
		return ReferenceShelf.weakIsEnqueued(this);
	}	
}

