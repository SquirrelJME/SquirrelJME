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
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.NativeABIProvider;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;

/**
 * This is a utility class which builds PowerPC based ABI setups.
 *
 * @since 2016/09/02
 */
public class PowerPCABI
	implements NativeABIProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public NativeABI byName(String __n, NativeTarget __t)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__n)
		{
				// Embedded ABI
			case "eabi":
				return PowerPCABI.eabi(__t);
				
				// OpenPOWER
			case "openpower":
				return PowerPCABI.openPower(__t);
				
				// System V
			case "sysv":
				return PowerPCABI.sysV(__t);
			
				// {@squirreljme.error BT01 Unknown ABI. (The ABI)}
			default:
				throw new NativeCodeException(String.format("BT01 %s", __n));
		}
	}
	
	/**
	 * Returns the EABI ABI.
	 *
	 * @param __t The target.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeABI eabi(NativeTarget __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the OpenPOWER ABI.
	 *
	 * @param __t The target.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeABI openPower(NativeTarget __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the SysV ABI.
	 *
	 * @param __t The target.
	 * @return The ABI definition.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeABI sysV(NativeTarget __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

