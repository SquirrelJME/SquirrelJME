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
import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
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
	/** The chain-link for this reference. */
	private final RefLinkBracket _link;
	
	/** The reference queue which this reference will be sent to when freed. */
	private final ReferenceQueue<? super T> _queue;
	
	/** Has this been enqueued? */
	private volatile boolean _enqueued;
	
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
		// There is no point in reference counting the null object, so have
		// no effect happen here
		if (__v == null)
		{
			this._link = null;
			this._queue = null;
			this._enqueued = true;
			
			return;
		}
		
		// It should be safe to create a new link outside of the lock
		RefLinkBracket link = ReferenceShelf.newLink();
		this._link = link;
		
		// Since nothing else knows about our link yet, we may set the object
		ReferenceShelf.linkSetObject(link, __v);
		
		// Although there is an optimization
		this._queue = __q;
		
		// Link into existing object, if needed
		ReferenceShelf.linkChain(link, __v);
	}
	
	/**
	 * Clears this reference without placing it in the queue.
	 *
	 * @since 2018/09/23
	 */
	@Api
	public void clear()
	{
		// Only unlink once
		synchronized (this)
		{
			if (!this._enqueued)
			{
				// Un-link this link
				ReferenceShelf.linkUnlinkAndClear(this._link);
				
				// Mark this reference as enqueued
				this._enqueued = true;
			}
		}
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
	@Api
	public boolean enqueue()
	{
		synchronized (this)
		{
			// If there is no queue then this has the same effect as clear
			ReferenceQueue<? super T> queue = this._queue;
			if (queue == null)
			{
				this.clear();
				
				// Was not actually pushed to the queue, but the reference is
				// now invalid
				return false;
			}
			
			// Will this get pushed to the queue?
			boolean pushToQueue;
			
			// Placing this in the queue invalidates it
			pushToQueue = !this._enqueued;
			if (pushToQueue)
			{
				// The reference no longer is valid
				
				ReferenceShelf.linkUnlinkAndClear(this._link);
				this._enqueued = true;
			}
			
			// Only push to the queue if was requested
			if (pushToQueue)
				queue.__enqueue(this);
			return pushToQueue;
		}
	}
	
	/**
	 * Returns the object that this reference refers to.
	 *
	 * @return The reference of this object.
	 * @since 2018/09/23
	 */
	@Api
	@SuppressWarnings({"unchecked"})
	public T get()
	{
		// The return value, if this is null then this gets enqueued
		Object rv;
		
		// Lock the GC just in case this link is being used that it does not
		// mess up anything else
		synchronized (this)
		{
			// If this was enqueued, then just return nothing
			if (this._enqueued)
				return null;
			
			// Otherwise, use what the link says our object is
			rv = ReferenceShelf.linkGetObject(this._link);
		}
		
		// If no object was set or was cleared out (perhaps by unlink) then
		// just inform that enqueue happened. Since this enqueue is here, it
		// is very possible that enqueuing happens at the last moment.
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
	@Api
	public boolean isEnqueued()
	{
		return this._enqueued;
	}	
}

