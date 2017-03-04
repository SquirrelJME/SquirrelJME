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
import net.multiphasicapps.squirreljme.jit.ActiveBinding;
import net.multiphasicapps.squirreljme.jit.Binding;
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
	
	/** The stack offset. */
	private volatile int _stackoffset =
		Integer.MIN_VALUE;
	
	/** The stack length. */
	private volatile int _stacklength =
		Integer.MIN_VALUE;
	
	/**
	 * Initializes the active binding.
	 *
	 * @since 2017/02/23
	 */
	public MIPSActiveBinding()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	public MIPSRegister[] registers()
	{
		// Stack only
		if (this._stacklength > 0)
			return null;
		
		// Convert
		List<MIPSRegister> registers = this.registers;
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
		
		// Clear the stack
		this._stacklength = Integer.MIN_VALUE;
		this._stackoffset = Integer.MIN_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public int stackLength()
	{
		return this._stacklength;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public int stackOffset()
	{
		return this._stackoffset;
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
		
		// Set stack properties
		this._stackoffset = bind.stackOffset();
		this._stacklength = bind.stackLength();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public String toString()
	{
		int stacklength = this._stacklength;
		if (stacklength <= 0)
			return this.registers.toString();
		else
			return String.format("<%+d, %d>", this._stackoffset,
				stacklength);
	}
}

