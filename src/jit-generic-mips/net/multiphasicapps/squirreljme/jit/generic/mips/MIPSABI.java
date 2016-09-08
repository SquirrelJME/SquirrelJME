// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic.mips;

import net.multiphasicapps.squirreljme.jit.base.JITCPUFloat;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;
import net.multiphasicapps.squirreljme.jit.generic.GenericABIBuilder;
import net.multiphasicapps.squirreljme.jit.generic.GenericRegisterFloatType;
import net.multiphasicapps.squirreljme.jit.generic.GenericRegisterIntegerType;
import net.multiphasicapps.squirreljme.jit.generic.GenericStackDirection;

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
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Setup
		GenericABIBuilder ab = new GenericABIBuilder();
		
		// Floating point?
		JITCPUFloat cpufloat = __t.floatingPoint();
		boolean hasfloat = cpufloat.isAnyHardware();
		
		// Add integer registers
		int bits = __t.bits();
		ab.pointerSize(bits);
		GenericRegisterIntegerType rtint =
			GenericABIBuilder.intRegisterTypeFromTriplet(__t);
		for (int i = 0; i <= 31; i++)
			ab.addRegister(MIPSRegister.of(false, i), rtint);
		
		// Floating point?
		if (hasfloat)
		{
			// Determine the floating point size
			GenericRegisterFloatType flt =
				GenericABIBuilder.floatRegisterTypeFromTriplet(__t);
			
			// Add them
			for (int i = 0; i <= 31; i++)
				ab.addRegister(MIPSRegister.of(true, i), flt);
		}
		
		// Stack grows down
		ab.stack(MIPSRegister.of(false, 29));
		ab.stackAlignment(8);
		ab.stackDirection(GenericStackDirection.HIGH_TO_LOW);
	
		// Arguments
		for (int i = 4; i <= 11; i++)
			ab.addArgument(MIPSRegister.of(false, i));
		
		// Return value
		for (int i = 2; i <= 3; i++)
			ab.addResult(MIPSRegister.of(false, i));
	
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
			
			// Return value
			for (int i = 0; i <= 1; i++)
				ab.addResult(MIPSRegister.of(true, i));
			
			// Temporary, make all registers temporary so that anything
			// that is used, becomes caller saved
			for (int i = 0; i <= 31; i++)
				ab.addTemporary(MIPSRegister.of(true, i));
		}
		
		// Build
		return ab.build();
	}
}

