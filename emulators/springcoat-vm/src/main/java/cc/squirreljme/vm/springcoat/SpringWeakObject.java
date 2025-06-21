// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * A weakly bound reference to an object.
 *
 * @since 2025/06/21
 */
public class SpringWeakObject
	extends SpringSimpleObject
{
	/** Has this been initialized? */
	private volatile boolean _beenInit;
	
	/** The reference this points to. */
	private volatile Reference<SpringObject> _ref; 
	
	/** The queue to push to. */
	private volatile SpringObject _queue;
	
	/**
	 * Initializes the object.
	 *
	 * @param __cl The class of the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/21
	 */
	public SpringWeakObject(SpringClass __cl)
		throws NullPointerException
	{
		super(__cl);
		
		// Must be the weak reference class
		if (!"java/lang/ref/WeakReference".equals(__cl.name().toString()))
			throw new SpringMLECallError("Invalid WeakReference Init");
	}
	
	/**
	 * Clears the weak reference and returns the queue.
	 *
	 * @return The resultant queue.
	 * @since 2025/06/21
	 */
	public SpringObject clear()
	{
		synchronized (this)
		{
			// Is there no reference set?
			Reference<SpringObject> ref = this._ref;
			if (ref == null)
				return null;
			
			// Clear the reference as we no longer need to keep it in memory
			this._ref = null;
			
			// Even though this is clear, we still enqueue the reference
			// as other parts of SpringCoat might be referring to this object
			// and as such are waiting on it for cleanup...
			ref.enqueue();
			
			// Clear the queue and return it
			SpringObject queue = this._queue;
			this._queue = null;
			return queue;
		}
	}
	
	/**
	 * Gets the referenced object.
	 *
	 * @return The object that is referenced or {@code null} if it has been
	 * garbage collected.
	 * @since 2025/06/21
	 */
	public SpringObject get()
	{
		synchronized (this)
		{
			Reference<SpringObject> ref = this._ref;
			if (ref == null)
				return null;
			
			return ref.get();
		}
	}
	
	/**
	 * Initializes the weak reference.
	 *
	 * @param __value The value to point to.
	 * @param __queue The queue to push to when garbage collected.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/21
	 */
	public void init(SpringObject __value, SpringObject __queue)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Can only be initialized once
			if (this._beenInit)
				throw new SpringMLECallError("WeakReference init twice!");
			this._beenInit = true;
			
			// Set fields
			this._ref = new WeakReference<>(__value);
			this._queue = __queue;
		}
	}
	
	/**
	 * Has this been enqueued?
	 *
	 * @return If this has been enqueued.
	 * @since 2025/06/21
	 */
	public boolean isEnqueued()
	{
		synchronized (this)
		{
			// Needs to have been initialized
			if (!this._beenInit)
				return false;
			
			// If the reference is still valid, it is considered enqueued
			// if it was flagged as such
			Reference<SpringObject> ref = this._ref;
			if (ref != null)
				return ref.isEnqueued();
			
			// Otherwise, if there is no queue then assume it was
			return this._queue == null;
		}
	}
}
