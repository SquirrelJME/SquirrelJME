// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.simulator;

import net.multiphasicapps.squirreljme.jit.JITTriplet;

/**
 * This is a mutable configuration which stores the input configuration for
 * a given simulator.
 *
 * @since 2016/07/06
 */
public final class SimulatorConfig
{
	/** Set lock. */
	protected final Object lock =
		new Object();
	
	/** The triplet of the target system to simulate. */
	private volatile JITTriplet _triplet;
	
	/**
	 * This is an immutable snapshot of a given configuration which cannot be
	 * changed.
	 *
	 * @since 2016/07/06
	 */
	public static final class Immutable
	{
	}
}

