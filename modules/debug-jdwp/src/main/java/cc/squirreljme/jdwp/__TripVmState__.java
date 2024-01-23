// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.host.trips.JDWPTripVmState;
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
	__TripVmState__(Reference<JDWPController> __controller)
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
		JDWPController controller = this.__controller();
		JDWPState state = controller.state;
		
		// Register this thread for later use
		state.items.put(__bootThread);
		
		// Tell the remote debugger that we started, note we always generate
		// this event and we never hide it
		try (JDWPPacket packet = controller.__event(SuspendPolicy.NONE,
			(__alive ? EventKind.THREAD_START : EventKind.THREAD_DEATH),
			0))
		{
			// Write the initial starting thread
			packet.writeObject(controller, __bootThread);
			
			// Send it away!
			controller.commLink.send(packet);
		}
	}
}
