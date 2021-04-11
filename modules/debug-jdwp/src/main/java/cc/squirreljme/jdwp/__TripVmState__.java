// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.trips.JDWPTrip;
import cc.squirreljme.jdwp.trips.JDWPTripVmState;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

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
	public void alive(boolean __alive)
	{
		throw Debugging.todo();
	}
}
