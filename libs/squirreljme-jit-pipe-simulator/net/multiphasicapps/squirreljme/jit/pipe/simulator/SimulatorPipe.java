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
import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.pipe.ExpandedPipe;

/**
 * This expands basic blocks for the simulator.
 *
 * @since 2017/08/11
 */
public class SimulatorPipe
	implements ExpandedPipe
{
	/** The machine code to write to. */
	protected final MachineCodeOutput out;
	
	/**
	 * Initializes the basic block writer for the simulator.
	 *
	 * @param __own The owning simulator.
	 * @param __key The key for this basic block.
	 * @param __out The assembler to write code to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public SimulatorPipe(SimulatorTranslator __own, BasicBlockKey __key,
		MachineCodeOutput __out)
		throws NullPointerException
	{
		// Check
		if (__own == null || __key == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __own;
		this.key = __key;
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/11
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void copy(TypedVariable __src, Variable __dest)
		throws JITException, NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void countReference(TypedVariable __obj, boolean __up)
		throws JITException, NullPointerException
	{
		// Check
		if (__obj == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void monitorEnter(TypedVariable __obj)
		throws JITException, NullPointerException
	{
		// Check
		if (__obj == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

