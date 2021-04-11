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
 * Trip on virtual machine state.
 *
 * @since 2021/04/11
 */
public interface JDWPTripVmState
	extends JDWPTrip
{
	/**
	 * Is the virtual machine alive?
	 * 
	 * @param __alive If the virtual machine is alive.
	 * @since 2021/04/11
	 */
	void alive(boolean __alive);
}
