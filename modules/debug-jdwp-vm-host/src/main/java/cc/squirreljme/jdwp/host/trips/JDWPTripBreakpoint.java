// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.trips;

/**
 * Handler for breakpoint trips.
 *
 * @since 2021/04/25
 */
public interface JDWPTripBreakpoint
	extends JDWPTrip
{
	/**
	 * Trip for a breakpoint event.
	 * 
	 * @param __thread The thread this is occurring in.
	 * @since 2021/04/25
	 */
	void breakpoint(Object __thread);
}
