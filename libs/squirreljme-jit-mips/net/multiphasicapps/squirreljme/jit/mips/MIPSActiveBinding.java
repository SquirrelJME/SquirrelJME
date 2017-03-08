// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.ActiveBinding;
import net.multiphasicapps.squirreljme.jit.ActiveBindingChangeType;
import net.multiphasicapps.squirreljme.jit.ActiveCacheState;
import net.multiphasicapps.squirreljme.jit.Binding;
import net.multiphasicapps.squirreljme.jit.DataType;
import net.multiphasicapps.squirreljme.jit.SnapshotBinding;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is an active binding for MIPS.
 *
 * This class is mutable.
 *
 * @since 2017/02/23
 */
public class MIPSActiveBinding
	extends MIPSBinding
	implements ActiveBinding
{
	/** Registers being used. */
	protected final List<MIPSRegister> registers =
		new ArrayList<>();
	
	/** The translation engine used. */
	protected final MIPSEngine engine;
	
	/** The owning cache state. */
	protected final ActiveCacheState.Slot inslot;
	
	/**
	 * Initializes the active binding.
	 *
	 * @param __e The owning engine.
	 * @param __cs The owning slot.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	public MIPSActiveBinding(MIPSEngine __e, ActiveCacheState.Slot __cs)
		throws NullPointerException
	{
		// Check
		if (__e == null || __cs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.engine = __e;
		this.inslot = __cs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/03
	 */
	@Override
	public void changeBinding(ActiveBindingChangeType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Clear only registers, the stack positions are fixed
		List<MIPSRegister> registers = this.registers;
		registers.clear();
		
		// Is cleared, do nothing
		if (__t == ActiveBindingChangeType.CLEARED)
			return;
		
		// Get target data type, determines which registers are used
		MIPSEngine engine = this.engine;
		DataType dt = engine.__aliasType(__t.target());
		
		// Find registers which are free for usage
		ActiveCacheState state = this.inslot.tread().state();
		ActiveCacheState.Tread stack = state.stack();
		ActiveCacheState.Tread locals = state.locals();
		for (int step = 0; step < 2; step++)
		{
			// Using saved registers?
			boolean usesave = (step == 1);
			MIPSRegister ai, af;
			if (usesave)
			{
				ai = NUBI.FIRST_INT_TEMPORARY;
				af = NUBI.FIRST_FLOAT_TEMPORARY;
			}
			
			// Use temporary registers
			else
			{
				ai = NUBI.FIRST_INT_SAVED;
				af = NUBI.FIRST_FLOAT_SAVED;
			}
			
			throw new todo.TODO();
		}
		
		// About to allocate on the stack but it already has a stack
		// position specified for it
		if (stackLength() >= dt.length())
			return;
		
		// If this point has been reached then that means no registers are
		// available, not even saved ones. As such, allocate the storage areas
		// on the stack.
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/07
	 */
	@Override
	public MIPSRegister getRegister(int __i)
		throws IndexOutOfBoundsException
	{
		return this.registers.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/07
	 */
	@Override
	public int numRegisters()
	{
		return this.registers.size();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public MIPSRegister[] registers()
	{
		// Convert
		List<MIPSRegister> registers = this.registers;
		if (registers.isEmpty())
			return null;
		return registers.<MIPSRegister>toArray(
			new MIPSRegister[registers.size()]);
	}
	
	/**
	 * Sets the registers which are used for the binding.
	 *
	 * @param __r The registers to use.
	 * @since 2017/03/01
	 */
	public void setRegisters(MIPSRegister... __r)
	{
		List<MIPSRegister> registers = this.registers;
		registers.clear();
		for (MIPSRegister r : __r)
			registers.add(r);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public int stackLength()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public int stackOffset()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public void switchFrom(Binding __b)
		throws JITException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Cast
		MIPSBinding bind = (MIPSBinding)__b;
		
		// Copy registers
		List<MIPSRegister> registers = this.registers;
		MIPSRegister[] fromregs = bind.registers();
		registers.clear();
		if (fromregs != null)
			for (int i = 0, n = fromregs.length; i < n; i++)
				registers.add(fromregs[i]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public String toString()
	{
		List<MIPSRegister> registers = this.registers;
		if (!registers.isEmpty())
			return this.registers.toString();
		else
			return String.format("<%+d, %d>", stackOffset(),
				stackLength());
	}
}

