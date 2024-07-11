// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Garbage collects native weak references which are attached to standard
 * Java objects.
 *
 * @since 2024/07/11
 */
final class __Collector__
	implements Runnable
{
	/** Queue for references to objects to clear. */
	private static final ReferenceQueue<? super Object> _QUEUE =
		new ReferenceQueue<>();
	
	/** Objects which are bound to native weak pointers. */
	private static final Map<Reference<?>, Long> _NATIVES =
		new LinkedHashMap<>();
	
	/** Collection handler thread. */
	private static volatile Thread _THREAD;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/11
	 */
	@Override
	public void run()
	{
		ReferenceQueue<? super Object> queue = __Collector__._QUEUE;
		Map<Reference<?>, Long> natives = __Collector__._NATIVES;
		for (;;)
			try
			{
				// Wait for object to come in
				Reference<?> ref = queue.remove(1_000);
				
				// Remove from queue if it is found
				synchronized (__Collector__.class)
				{
					// Scan through and find it
					Iterator<Map.Entry<Reference<?>, Long>> it =
						natives.entrySet().iterator();
					while (it.hasNext())
					{
						Map.Entry<Reference<?>, Long> entry = it.next();
						
						// If this is the one, then call native code to
						// remove the weak reference on the native side
						if (entry.getKey() == ref)
						{
							// We need the pointer before we demolish
							// the entry
							long weakP = entry.getValue();
							
							// Clear from this
							it.remove();
							
							// Tell native code to delete this
							NativeScritchDylib.__weakDelete(weakP);
							
							// Stop
							break;
						}
					}
				}
			}
			catch (InterruptedException __e)
			{
				break;
			}
	}
	
	/**
	 * Pushes an object to the queue.
	 *
	 * @param __object The object to push.
	 * @param __weakP The weak pointer it is bound to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/11
	 */
	static void __push(Object __object, long __weakP)
		throws NullPointerException
	{
		if (__object == null || __weakP == 0)
			throw new NullPointerException("NARG");
		
		// Setup reference which points to the queue
		Reference<Object> ref = new WeakReference<>(__object,
			__Collector__._QUEUE);
		
		// Store native in the map
		synchronized (__Collector__.class)
		{
			__Collector__._NATIVES.put(ref, __weakP);
			
			// Need to setup thread to watch references?
			if (__Collector__._THREAD == null)
			{
				// Setup thread
				Thread thread = new Thread(new __Collector__(),
					"SquirrelJMEDylibCollector");
				thread.setDaemon(true);
				
				// Store for later handling
				__Collector__._THREAD = thread;
				
				// Start it!
				thread.start();
			}
		}
	}
}
