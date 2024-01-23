// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.host.trips.JDWPTrip;
import java.lang.ref.Reference;

/**
 * The base class for trip handlers.
 *
 * @since 2021/04/11
 */
abstract class __TripBase__
	implements JDWPTrip
{
	/** The controller reference. */
	private final Reference<JDWPHostController> _controller;
	
	/**
	 * Initializes the trip base.
	 * 
	 * @param __controller The controller used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	__TripBase__(Reference<JDWPHostController> __controller)
		throws NullPointerException
	{
		if (__controller == null)
			throw new NullPointerException("NARG");
		
		this._controller = __controller;
	}
	
	/**
	 * Returns the debug controller.
	 * 
	 * @return The debug controller.
	 * @since 2021/04/11
	 */
	final JDWPHostController __controller()
	{
		/* {@squirreljme.error AG0n The debug controller was garbage
		collected.} */
		JDWPHostController rv = this._controller.get();
		if (rv == null)
			throw new IllegalStateException("AG0n");
		
		return rv;
	}
}
