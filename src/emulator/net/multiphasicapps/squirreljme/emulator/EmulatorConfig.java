// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This is used to configure the emulator.
 *
 * The configuration state is mutable.
 *
 * @since 2016/08/21
 */
public class EmulatorConfig
{
	/** Configuration lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Initializes the base configuration.
	 *
	 * @since 2016/08/21
	 */
	public EmulatorConfig()
	{
	}
}

