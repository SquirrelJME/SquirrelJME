// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.jdwp.host.trips.JDWPTripVmState;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import java.lang.ref.Reference;

/**
 * Trip on virtual machine state.
 *
 * @since 2021/04/11
 */
final class __TripVmState__
	extends __TripBase__
	implements JDWPTripVmState
{
	/**
	 * Initializes the trip.
	 * 
	 * @param __controller The controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	__TripVmState__(Reference<JDWPHostController> __controller)
		throws NullPointerException
	{
		super(__controller);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public void alive(Object __bootThread, boolean __alive)
	{
		JDWPHostController controller = this.__controller();
		JDWPHostState state = controller.getState();
		
		// Register this thread for later use
		state.items.put(__bootThread);
		
		// Tell the remote debugger that we started, note we always generate
		// this event and we never hide it
		try (JDWPPacket packet = controller.event(JDWPSuspendPolicy.NONE,
			(__alive ? JDWPEventKind.THREAD_START :
				JDWPEventKind.THREAD_DEATH), 0))
		{
			// Write the initial starting thread
			controller.writeObject(packet, __bootThread);
			
			// Send it away!
			controller.getCommLink().send(packet);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/30
	 */
	@Override
	public void userDefined(Object __thread)
	{
		// Just send the basic user defined event
		JDWPHostController controller = this.__controller();
		
		// Pause all threads
		JDWPViewThread viewThread = controller.viewThread();
		for (Object thread : controller.allThreads(false))
			viewThread.suspension(thread).suspend();
		
		// Send the packet, which does not have much information
		try (JDWPPacket packet = controller.event(JDWPSuspendPolicy.ALL,
			JDWPEventKind.USER_DEFINED, 0))
		{
			// Inform of the thread this is on
			controller.writeObject(packet, __thread);
			
			// Send it away!
			controller.getCommLink().send(packet);
		}
	}
}
