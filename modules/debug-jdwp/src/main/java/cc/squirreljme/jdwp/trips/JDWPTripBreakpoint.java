// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.trips;

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
