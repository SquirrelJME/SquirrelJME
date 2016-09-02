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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;
import net.multiphasicapps.squirreljme.jit.generic.GenericABIBuilder;
import net.multiphasicapps.squirreljme.jit.generic.GenericStackDirection;

/**
 * These are common ABIs that may be used on MIPS based systems.
 *
 * @since 2016/09/01
 */
public final class MIPSABI
{
	/** Cached EABI (float). */
	private static volatile Reference<GenericABI> _eabihf;
	
	/** Cached EABI (soft). */
	private static volatile Reference<GenericABI> _eabisf;
	
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
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Floating point?
		boolean hasfloat = __t.floatingPoint().isAnyHardware();
		
		// Get
		Reference<GenericABI> ref = (hasfloat ? MIPSABI._eabihf :
			MIPSABI._eabisf);
		GenericABI rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Setup
			GenericABIBuilder ab = new GenericABIBuilder();
		
			// Stack grows down
			ab.stack(MIPSRegister.of(false, 29));
			ab.stackDirection(GenericStackDirection.HIGH_TO_LOW);
		
			// Arguments
			for (int i = 4; i <= 11; i++)
				ab.addArgument(MIPSRegister.of(false, i));
		
			// Temporary
			for (int i = 1; i <= 15; i++)
				ab.addTemporary(MIPSRegister.of(false, i));
			for (int i = 24; i <= 25; i++)
				ab.addTemporary(MIPSRegister.of(false, i));
		
			// Saved registers
			for (int i = 16; i <= 23; i++)
				ab.addSaved(MIPSRegister.of(false, i));
			ab.addSaved(MIPSRegister.of(false, 30));
			
			// Floating point?
			if (hasfloat)
			{
				// Arguments
				for (int i = 12; i <= 19; i++)
					ab.addArgument(MIPSRegister.of(true, i));
				
				// Temporary, make all registers temporary so that anything
				// that is used, becomes caller saved
				for (int i = 1; i <= 31; i++)
					ab.addTemporary(MIPSRegister.of(true, i));
			}
			
			// Build
			rv = ab.build();
			
			// Store
			ref = new WeakReference<>(rv);
			if (hasfloat)
				MIPSABI._eabihf = ref;
			else
				MIPSABI._eabisf = ref;
		}
		
		// Return it
		return rv;
	}
}

