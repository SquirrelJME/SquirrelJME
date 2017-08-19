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

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.arch.FuturePositionMarker;
import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.arch.PositionMarker;
import net.multiphasicapps.squirreljme.jit.arch.Register;
import net.multiphasicapps.squirreljme.jit.arch.ZeroComparisonType;
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
	
	/** The addresses where basic block start, for local jumps. */
	private final Map<BasicBlockKey, PositionMarker> _bbstarts =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the basic block writer for the simulator.
	 *
	 * @param __out The assembler to write code to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/11
	 */
	public SimulatorPipe(MachineCodeOutput __out)
		throws NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Set
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
		
		MachineCodeOutput out = this.out;
		
		// Only modify reference counts if the object is not-null
		Register or = __variableToRegister(__obj);
		FuturePositionMarker fpm = out.createFuturePositionMarker();
		out.compareAndRelativeBranch(ZeroComparisonType.ZERO, or, fpm);
		
		// Counting up is simple because the garbage collector and such is
		// not involved
		if (__up)
		{
			if (true)
				throw new todo.TODO();
		}
		
		// However, counting down requires a special method call because it may
		// cause the resulting object to be garbage collected as soon it is
		// free
		else
		{
			if (true)
				throw new todo.TODO();
		}
		
		// If the object is null then a jump is made here
		out.markFuturePosition(fpm);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/13
	 */
	@Override
	public void enterBlock(BasicBlockKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Mark position
		this._bbstarts.put(__k, this.out.positionMarker());
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
	
	/**
	 * Converts a variable to a register.
	 *
	 * @param __v The variable to convert.
	 * @return The register for the given variable.
	 * @throws JITException If it could not be converted.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/19
	 */
	private Register __variableToRegister(TypedVariable __v)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

