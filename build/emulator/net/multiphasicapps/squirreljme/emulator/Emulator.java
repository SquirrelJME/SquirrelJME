// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This interface is specified for anything which emulates a binary for
 * execution.
 *
 * @since 2016/08/21
 */
public final class Emulator
	implements Runnable
{
	/** The emulator configuration. */
	protected final EmulatorConfig.Immutable config;
	
	/**
	 * Initializes the emulator.
	 *
	 * @param __conf The config to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	public Emulator(EmulatorConfig.Immutable __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Returns the emulator configuration.
	 *
	 * @return The emulator configuration.
	 * @since 2016/08/21
	 */
	public EmulatorConfig.Immutable config()
	{
		return this.config;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 20916/09/03
	 */
	@Override
	public final void run()
	{
		throw new Error("TODO");
	}
}

