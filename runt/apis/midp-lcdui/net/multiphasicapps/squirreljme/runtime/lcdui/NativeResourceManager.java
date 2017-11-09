// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import net.multiphasicapps.squirreljme.unsafe.SystemProcess;

/**
 * This is a class which manages native resources, it is used to manage
 * native resources which may be associated with objects and memory areas
 * which cannot be garbage collected.
 *
 * @since 2017/10/24
 */
public final class NativeResourceManager
	implements Runnable
{
	/** Single instance of the resource manager. */
	public static final NativeResourceManager RESOURCE_MANAGER =
		new NativeResourceManager();
	
	/** The thread which manages resources. */
	protected final Thread thread;
	
	/** Global lock for resource access. */
	private final Object _lock =
		new Object();
	
	/** Weak binding map of native objects to native resources. */
	private final Map<Object, NativeResource> _binds =
		new WeakHashMap<>();
	
	/** Binding of references which are used for queue cleanup. */
	private final Map<Reference<Object>, NativeResource> _natives =
		new HashMap<>();
	
	/** Queue for resources. */
	private final ReferenceQueue<Object> _queue =
		new ReferenceQueue<>();
	
	/**
	 * Initializes the native resource manager.
	 *
	 * @since 2017/10/24
	 */
	private NativeResourceManager()
	{
		// Initialize and run the thread last
		Thread thread = SystemProcess.createDaemonThread(this,
			"NativeResourceManager");
		this.thread = thread;
		thread.start();
	}
	
	/**
	 * Obtains the native resource which 
	 *
	 * @param <N> The type of native resource to get.
	 * @param __cl The class type of the native resource.
	 * @param __v The object to get the native resource for.
	 * @return The native resource.
	 * @throws NoSuchElementException If the native resource does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public <N extends NativeResource> N getNative(Class<N> __cl, Object __v)
		throws NoSuchElementException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Object, NativeResource> binds = this._binds;
		synchronized (this._lock)
		{
			// {@squirreljme.error EB0z Could not get the native resource for
			// the given class type. (The class)}
			NativeResource rv = binds.get(__v);
			if (rv == null)
				throw new NoSuchElementException(
					String.format("EB0z %s", __v.getClass()));
			
			return __cl.cast(rv);
		}
	}
	
	/**
	 * Registers the resource with the native resource.
	 *
	 * @param __nr
	 * @throws IllegalStateException If the object is already registered or
	 * does not match the native resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public void register(NativeResource __nr, Object __v)
		throws IllegalStateException, NullPointerException
	{
		if (__nr == null || __v == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB10 Cannot register the specified object
		// because it is not bound to the native resource.}
		if (__v != __nr.boundObject().get())
			throw new IllegalStateException("EB10");
		
		// Lock
		ReferenceQueue<Object> queue = this._queue;
		Map<Reference<Object>, NativeResource> natives = this._natives;
		Map<Object, NativeResource> binds = this._binds;
		synchronized (this._lock)
		{
			// {@squirreljme.error EB11 The specified object has already
			// been registered.}
			if (binds.containsKey(__v))
				throw new IllegalStateException("EB11");
			
			// Create and store reference
			Reference<Object> ref = new WeakReference<>(__v, queue);
			natives.put(ref, __nr);
			binds.put(__v, __nr);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/24
	 */
	@Override
	public void run()
	{
		// Constantly try to garbage collect native resources
		ReferenceQueue<Object> queue = this._queue;
		Map<Reference<Object>, NativeResource> natives = this._natives;
		for (;;)
		{
			// Try to remove an object
			Reference<? extends Object> gced;
			try
			{
				gced = queue.remove();
			}
			
			// Ignore interrupts
			catch (InterruptedException e)
			{
				continue;
			}
			
			// Remove from object maps
			NativeResource nrc;
			synchronized (this._lock)
			{
				// {@squirreljme.error EB12 An unregistered native resource
				// was garbage collected.}
				nrc = natives.remove(gced);
				if (nrc == null)
					throw new IllegalStateException("EB12");
			}
			
			// Free the resource
			try
			{
				nrc.freeResource();
			}
			
			// Do not allow the thread clearing resources to die from an
			// exception
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
	}
}

