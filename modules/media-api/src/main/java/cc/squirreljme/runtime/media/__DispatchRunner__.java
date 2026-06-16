// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.Queue;

/**
 * Handles event dispatch.
 *
 * @since 2025/06/03
 */
@KeepWhenCompacting
final class __DispatchRunner__
	implements Runnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/06/03
	 */
	@Override
	public void run()
	{
		Queue<__ListenerEvent__> queue = ListenerDispatch._QUEUE;
		for (;;)
		{
			synchronized (ListenerDispatch.class)
			{
				// Get the next event
				__ListenerEvent__ event = queue.poll();
				if (event == null)
				{
					// Wait for more events as there are none
					try
					{
						ListenerDispatch.class.wait(1000);
					}
					catch (InterruptedException ignored)
					{
					}
					
					// Try reading the next event
					continue;
				}
				
				// Handle event
				event._player.__handleEvent(event._eventType,
					event._eventValue, event._nanoTime);
			}
		}
	}
}
