// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.trans.simulator;

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedBasicBlock;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.trans.TranslatorService;

/**
 * This is the simulator translator which generates MMIX operations using a
 * very simple approach.
 *
 * @since 2017/08/11
 */
public class SimulatorTranslator
	implements ExpandedByteCode
{
	/** The output MMIX code. */
	protected final MachineCodeOutput out;
	
	/**
	 * Initializes the output.
	 *
	 * @param __o The output where machine code goes.
	 * @throws JITException If it could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public SimulatorTranslator(MachineCodeOutput __o)
		throws JITException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/11
	 */
	@Override
	public void close()
	{
		// No closing has to be performed
	}
}

