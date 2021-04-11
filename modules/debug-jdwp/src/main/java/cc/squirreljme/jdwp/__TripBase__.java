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
	private final Reference<JDWPController> _controller;
	
	/**
	 * Initializes the trip base.
	 * 
	 * @param __controller The controller used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	__TripBase__(Reference<JDWPController> __controller)
		throws NullPointerException
	{
		if (__controller == null)
			throw new NullPointerException("NARG");
		
		this._controller = __controller;
	}
}
