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

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericRegisterAllocator;
import net.multiphasicapps.squirreljme.jit.generic.
	GenericRegisterAllocatorFactory;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This factory creates register allocators for the MIPS output code.
 *
 * @since 2016/08/30
 */
public class MIPSRegisterAllocatorFactory
	implements GenericRegisterAllocatorFactory
{
	/**
	 * Initializes the MIPS register allocator factory.
	 *
	 * @param __t The used triplet.
	 * @throws JITException If it could not be created (likely because the
	 * CPU is not known).
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public MIPSRegisterAllocatorFactory(JITTriplet __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public GenericRegisterAllocator create()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public String[] properties()
	{
		throw new Error("TODO");
	}
}

