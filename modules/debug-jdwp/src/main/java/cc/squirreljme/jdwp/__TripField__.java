// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.trips.JDWPTripField;
import java.lang.ref.Reference;

/**
 * Tripping for fields.
 *
 * @since 2021/04/30
 */
final class __TripField__
	extends __TripBase__
	implements JDWPTripField
{
	/**
	 * Initializes the trip.
	 * 
	 * @param __controller The controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/30
	 */
	__TripField__(Reference<JDWPController> __controller)
		throws NullPointerException
	{
		super(__controller);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/30
	 */
	@Override
	public void field(Object __thread, Object __type, int __fieldDx,
		boolean __write, Object __instance, JDWPValue __jVal)
	{
		JDWPController controller = this.__controller();
		JDWPState state = controller.state;
		
		// Make sure these are registered
		if (__type != null)
			state.items.put(__type);
		if (__instance != null)
			state.items.put(__instance);
		
		// Signal access or modification
		controller.signal(__thread,
			(__write ? EventKind.FIELD_MODIFICATION :
				EventKind.FIELD_ACCESS),
			__type, __fieldDx, __write, __instance, __jVal);
	}
}
