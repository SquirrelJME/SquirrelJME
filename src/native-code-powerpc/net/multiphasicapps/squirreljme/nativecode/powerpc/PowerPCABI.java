// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.powerpc;

import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;

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
	 * @param __b The number of bits the CPU is.
	 * @param __f The floating point type.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeABI eabi(int __b, NativeFloatType __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the OpenPOWER ABI.
	 *
	 * @param __b The number of bits the CPU is.
	 * @param __f The floating point type.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeABI openPower(int __b, NativeFloatType __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the SysV ABI.
	 *
	 * @param __b The number of bits the CPU is.
	 * @param __f The floating point type.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeABI sysV(int __b, NativeFloatType __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

