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

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.ReferenceChain;

/**
 * This class represents references which may be referred to using various
 * different means of attachment, as such this family of classes integrates
 * with the garbage collector.
 *
 * @param <T> The type of object to store.
 * @since 2018/09/23
 */
@SuppressWarnings({"FeatureEnvy", "AbstractClassWithOnlyOneDirectInheritor"})
public abstract class Reference<T>
{
	/** The native pointer to the object this reference is looking at. */
	private volatile long _pointer;
	
	/** The queue this reference is in, dropped when enqueued. */
	private volatile ReferenceQueue<? super T> _queue;
	
	/** Has this been enqueued, since the queue reference can drop? */
	private volatile boolean _enqueued;
	
	/**
	 * Initializes the reference with an optionally specified queue.
	 *
	 * @param __v The object to bind a weak reference to.
	 * @param __q The queue which is given this reference when it has been
	 * cleared.
	 * @since 2020/03/10
	 */
	Reference(Object __v, ReferenceQueue<? super T> __q)
	{
		// References to null are valid, however they have no effect at all
		if (__v == null)
			return;
		
		// These can be set quite simply
		this._pointer = Assembly.objectToPointer(__v);
		this._queue = __q;
		
		// We need to lock the garbage collector because we need to modify an
		// objects reference chain.
		int code = Assembly.atomicTicker();
		try
		{
			// Lock the garbage collector
			for (int count = 0; Assembly.gcLock(code);)
				Assembly.spinLockBurn(count++);
			
			// If the object has no reference chain, we need to create one
			ReferenceChain chain = Assembly.refChainGet(__v);
			if (chain == null)
				Assembly.refChainSet(__v, (chain = new ReferenceChain()));
			
			// Push ourself to this chain
			chain.push(this);
		}
		
		// Clear the lock on the garbage collector
		finally
		{
			Assembly.gcUnlock(code);
		}
	}
	
	/**
	 * Clears this reference without placing it in the queue.
	 *
	 * @since 2018/09/23
	 */
	public void clear()
	{
		// Null pointers cannot be cleared (they are already)
		long pointer = this._pointer;
		if (pointer == 0)
			return;
		
		// Obtain exclusive control over the garbage collector
		int code = Assembly.atomicTicker();
		try
		{
			// Lock the garbage collector
			for (int count = 0; Assembly.gcLock(code);)
				Assembly.spinLockBurn(count++);
			
			// Clear the pointer
			this.__clear();
		}
		
		// Free our control over the garbage collector
		finally
		{
			Assembly.gcUnlock(code);
		}
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
		// If we do not have a queue then this operation does nothing
		ReferenceQueue<? super T> queue = this._queue;
		if (queue == null)
			return false;
		
		// Already has been enqueued?
		if (this._enqueued)
			return false;
		
		// Obtain exclusive control over the garbage collector
		int code = Assembly.atomicTicker();
		try
		{
			// Lock the garbage collector
			for (int count = 0; Assembly.gcLock(code);)
				Assembly.spinLockBurn(count++);
			
			// Enqueue and clear the pointer
			this.__enqueue();
			this.__clear();
			
			// Was enqueued
			return true;
		}
		
		// Free our control over the garbage collector
		finally
		{
			Assembly.gcUnlock(code);
		}
	}
	
	/**
	 * Returns the object that this reference refers to, will be {@code null}
	 * if this never pointed to any object.
	 *
	 * @return The reference of this object.
	 * @since 2018/09/23
	 */
	@SuppressWarnings({"unchecked"})
	public T get()
	{
		// Obtain exclusive control over the garbage collector
		int code = Assembly.atomicTicker();
		try
		{
			// Lock the garbage collector
			for (int count = 0; Assembly.gcLock(code);)
				Assembly.spinLockBurn(count++);
			
			// We can always return the pointer here as long as we are in the
			// GC lock, this is because the pointer is only cleared when the
			// object we are pointing to is facing garbage collection
			return (T)Assembly.pointerToObject(this._pointer);
		}
		
		// Free our control over the garbage collector
		finally
		{
			Assembly.gcUnlock(code);
		}
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
	
	/**
	 * Clears the pointer.
	 *
	 * @since 2020/03/10
	 */
	private void __clear()
	{
		// Clear pointer
		this._pointer = 0;
	}
	
	/**
	 * Enqueues this pointer to the queue.
	 *
	 * @since 2020/03/10
	 */
	private void __enqueue()
	{
		if (true)
			throw new todo.TODO();
		
		// Has been enqueued now
		this._enqueued = true;
	}
}

