// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPClassStatus;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.host.trips.JDWPTripClassStatus;
import java.lang.ref.Reference;

/**
 * Trip for when the status of a class has changed.
 *
 * @since 2021/04/17
 */
final class __TripClassStatus__
	extends __TripBase__
	implements JDWPTripClassStatus
{
	/**
	 * Initializes the trip.
	 * 
	 * @param __controller The controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	__TripClassStatus__(Reference<JDWPHostController> __controller)
		throws NullPointerException
	{
		super(__controller);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public void classStatus(Object __thread, Object __which,
		JDWPClassStatus __status)
	{
		JDWPHostController controller = this.__controller();
		JDWPHostState state = controller.getState();
		
		// Record these so they can be grabbed later
		state.items.put(__thread);
		state.items.put(__which);
		
		JDWPEventKind eventKind;
		switch (__status)
		{
			case UNLOAD:
				eventKind = JDWPEventKind.CLASS_UNLOAD;
				break;
			
			case VERIFIED:
				eventKind = JDWPEventKind.CLASS_LOAD;
				break;
			
			case PREPARED:
			case INITIALIZED:
				eventKind = JDWPEventKind.CLASS_PREPARE;
				break;
			
			// Unknown, so do nothing
			default:
				return;
		}
		
		// Signal this event
		controller.signal(__thread, eventKind, __which, __status);
	}
}
