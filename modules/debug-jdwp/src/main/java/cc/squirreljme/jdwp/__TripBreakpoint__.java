// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.trips.JDWPTripBreakpoint;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * Trip handler for breakpoints.
 *
 * @since 2021/04/25
 */
class __TripBreakpoint__
	extends __TripBase__
	implements JDWPTripBreakpoint
{
	/**
	 * Initializes the trip.
	 * 
	 * @param __controller The controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/25
	 */
	__TripBreakpoint__(Reference<JDWPController> __controller)
		throws NullPointerException
	{
		super(__controller);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/25
	 */
	@Override
	public void breakpoint(Object __thread)
	{
		JDWPController controller = this.__controller();
		
		// Make sure this thread is registered
		controller.state.items.put(__thread);
		
		// Send the signal
		controller.signal(__thread, EventKind.BREAKPOINT);
	}
}
