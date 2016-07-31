// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.chv.linux;

import net.multiphasicapps.squirreljme.emulator.HypoVisor;

/**
 * This implements the base Linux hypovisor since all Linux systems for every
 * architecture share a very common base.
 *
 * @since 2016/07/30
 */
public abstract class LinuxHypoVisor
	implements HypoVisor
{
	/**
	 * Sets the init program to be ran when the emulator requests hypovisor
	 * initial state setup.
	 *
	 * @param __elf The executable bytes.
	 * @param __args The arguments to the executable.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final void setInit(byte[] __elf, String... __args)
		throws NullPointerException
	{
		// Check
		if (__elf == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

