// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.trips.JDWPTripThread;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * Trip when a thread changes from being alive or dead.
 *
 * @since 2021/04/11
 */
final class __TripThreadAlive__
	extends __TripBase__
	implements JDWPTripThread
{
	/**
	 * Initializes the trip.
	 * 
	 * @param __controller The controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	__TripThreadAlive__(Reference<JDWPController> __controller)
		throws NullPointerException
	{
		super(__controller);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public void alive(Object __thread, boolean __isAlive)
	{
		JDWPController controller = this.__controller();
		JDWPState state = controller.state;
		
		// Register this thread for later use
		state.items.put(__thread);
		
		// Forward generic event
		controller.signal(__thread, (__isAlive ? EventKind.THREAD_START :
			EventKind.THREAD_DEATH), null, __thread);
	}
}
