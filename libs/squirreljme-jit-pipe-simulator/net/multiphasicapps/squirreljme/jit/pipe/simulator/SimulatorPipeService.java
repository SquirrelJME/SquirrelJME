// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.pipe.simulator;

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.pipe.ExpandedPipe;
import net.multiphasicapps.squirreljme.jit.pipe.ExpandedPipeService;

/**
 * This is capable of creating the translator used by the simulator.
 *
 * @since 2017/08/11
 */
public class SimulatorPipeService
	implements ExpandedPipeService
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/11
	 */
	@Override
	public ExpandedPipe createPipe(MachineCodeOutput __o)
		throws JITException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return new SimulatorPipe(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/11
	 */
	@Override
	public String name()
	{
		return "simulator";
	}
}

