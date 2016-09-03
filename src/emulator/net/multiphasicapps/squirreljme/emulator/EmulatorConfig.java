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
 * This is used to configure the emulator for when it runs.
 *
 * @since 2016/08/21
 */
public final class EmulatorConfig
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
	
	/**
	 * Creates an immutable copy of this configuration.
	 *
	 * @return The configuration copy.
	 * @since 2016/09/03
	 */
	public final Immutable immutable()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This contains an immutable copy of the emulator configuration.
	 *
	 * @since 2016/09/03
	 */
	public static final class Immutable
	{
	}
}

