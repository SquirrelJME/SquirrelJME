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
import cc.squirreljme.jdwp.host.trips.JDWPTripBreakpoint;
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
	__TripBreakpoint__(Reference<JDWPHostController> __controller)
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
		if (JDWPHostController._DEBUG)
			Debugging.debugNote("TRIPPING ON BREAKPOINT!");
		
		JDWPHostController controller = this.__controller();
		
		// Make sure this thread is registered
		controller.getState().items.put(__thread);
		
		// Send the signal
		controller.signal(__thread, JDWPEventKind.BREAKPOINT);
	}
}
