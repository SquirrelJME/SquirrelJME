// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * Listener for tally updates.
 *
 * @since 2024/01/19
 */
public interface TallyListener
{
	/**
	 * Indicates that the tally has been updated.
	 *
	 * @param __which Which tracker did this come from?
	 * @param __old The old value.
	 * @param __new The new value.
	 * @since 2024/01/19
	 */
	void updateTally(TallyTracker __which, int __old, int __new);
}
