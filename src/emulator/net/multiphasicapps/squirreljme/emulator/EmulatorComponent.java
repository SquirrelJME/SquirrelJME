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
 * This is the root of all components on the system, components essentially
 * have time based events that may be executed if requested.
 *
 * @since 2016/07/26
 */
public abstract class EmulatorComponent
{
	/** The owning emulator group. */
	protected final EmulatorGroup group;
	
	/** The owning emulator system. */
	protected final EmulatorSystem system;
	
	/**
	 * Initializes the base component.
	 *
	 * @param __es The owning emulator system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	public EmulatorComponent(EmulatorSystem __es)
		throws NullPointerException
	{
		// Check
		if (__es == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.system = __es;
		this.group = __es.group();
	}
	
	/**
	 * Returns the owning emulator group.
	 *
	 * @return The emulator group.
	 * @since 2016/07/26
	 */
	public final EmulatorGroup group()
	{
		return this.group;
	}
	
	/**
	 * Returns the owning emulator system.
	 *
	 * @return The emulator system.
	 * @since 2016/07/26
	 */
	public final EmulatorSystem system()
	{
		return this.system;
	}
}

