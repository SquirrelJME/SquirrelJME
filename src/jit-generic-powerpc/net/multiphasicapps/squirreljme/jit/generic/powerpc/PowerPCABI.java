// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic.powerpc;

import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;

/**
 * This is a utility class which builds PowerPC based ABI setups.
 *
 * @since 2016/09/02
 */
public final class PowerPCABI
{
	/**
	 * Not used.
	 *
	 * @since 2016/09/02
	 */
	private PowerPCABI()
	{
	}
	
	/**
	 * Returns the EABI ABI.
	 *
	 * @param __t The used triplet.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static GenericABI eabi(JITTriplet __t)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the OpenPOWER ABI.
	 *
	 * @param __t The used triplet.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static GenericABI openPower(JITTriplet __t)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the SysV ABI.
	 *
	 * @param __t The used triplet.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static GenericABI sysV(JITTriplet __t)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
}

