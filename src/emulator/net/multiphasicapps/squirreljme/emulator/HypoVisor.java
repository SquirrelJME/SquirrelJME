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
 * This interface is passed to the emulator which specifies how certain
 * undefined behavior for a given system is defined. Essentially this class can
 * be seen as the trap manager for an emulator in that anything unknown by the
 * emulator is handled by this interface.
 *
 * The hypovisor is not thread safe, although it is recommended to use thread
 * safety where it is required.
 *
 * @since 2016/07/30
 */
public interface HypoVisor
{
	/**
	 * Instructs the hypovisor to perform a system specific initialization.
	 *
	 * @param __e The emulator to initialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/06
	 */
	public abstract void init(Emulator __e)
		throws NullPointerException;
}

