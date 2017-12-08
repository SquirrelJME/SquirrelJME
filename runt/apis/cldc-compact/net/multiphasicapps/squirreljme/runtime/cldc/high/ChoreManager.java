// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.high;

/**
 * This is used to manage tasks within the system and is intended to be used
 * by the SWM sub-system.
 *
 * @since 2017/12/07
 */
public abstract class ChoreManager
{
	/**
	 * Lists the chores which are currently running.
	 *
	 * @param __sys If {@code true} then system chores are included.
	 * @return The identifiers of the chores which are running within
	 * SquirrelJME.
	 * @since 2017/12/07
	 */
	public abstract int[] listChores(boolean __sys);
}

