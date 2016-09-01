// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;

/**
 * These are common ABIs that may be used on MIPS based systems.
 *
 * @since 2016/09/01
 */
public final class MIPSABI
{
	/**
	 * Not used.
	 *
	 * @since 2016/09/01
	 */
	private MIPSABI()
	{
	}
	
	/**
	 * This returns the EABI ABI definition for MIPS systems.
	 *
	 * This ABI is described at
	 * {@link http://www.cygwin.com/ml/binutils/2003-06/msg00436.html}.
	 *
	 * @param __t The triplet, used to determine floating point and whether the
	 * CPU is 32-bit or 64-bit.
	 * @return The ABI.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public static GenericABI eabi(JITTriplet __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

