// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.microedition.media.PlayerListener;

/**
 * Dispatches events for {@link PlayerListener} in its own thread so that
 * any audio related threads need not be delayed with event handling.
 *
 * @since 2025/06/03
 */
@SquirrelJMEVendorApi
public final class ListenerDispatch
{
	/** The dispatch thread. */
	private static volatile Thread _THREAD;
	
	/** The event queue. */
	@SquirrelJMEVendorApi
	static final Queue<__ListenerEvent__> _QUEUE =
		new ArrayDeque<>();
	
	/**
	 * Dispatches the given event.
	 *
	 * @param __player The player to dispatch for.
	 * @param __eventType The event to dispatch.
	 * @param __eventValue The value for the event.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@SquirrelJMEVendorApi
	public static void dispatch(AbstractPlayer __player, String __eventType,
		Object __eventValue)
		throws NullPointerException
	{
		if (__player == null || __eventType == null)
			throw new NullPointerException("NARG");
		
		// Player is not permitted to dispatch events
		synchronized (__player)
		{
			if (__player._ffNoDispatch)
				return;
		}
		
		// Does the dispatch thread need to be created?
		Thread thread = ListenerDispatch._THREAD;
		if (thread == null)
			synchronized (ListenerDispatch.class)
			{
				thread = ListenerDispatch._THREAD;
				if (thread == null)
				{
					// Setup new thread to read in events
					thread = new Thread(new __DispatchRunner__(),
						"ScritchAudioDispatcher");
					
					// Make sure it starts as a daemon thread
					ThreadShelf.javaThreadSetDaemon(thread);
					thread.start();
					
					// Store it for later
					ListenerDispatch._THREAD = thread;
				}
			}
		
		// Queue the event
		synchronized (ListenerDispatch.class)
		{
			// Push to the queue
			ListenerDispatch._QUEUE.offer(new __ListenerEvent__(
				__player, __eventType, __eventValue, System.nanoTime()));
			
			// Notify any monitors
			ListenerDispatch.class.notifyAll();
		}
		
		// Event was dispatched so interrupt the thread
		thread.interrupt();
	}
}
