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
import java.util.Arrays;
import java.util.Objects;
import net.multiphasicapps.squirreljme.jit.Binding;

/**
 * This is a binding for MIPS register or stack values which determines where
 * values exist.
 *
 * This class is immutable.
 *
 * @since 2017/02/19
 */
public final class MIPSBinding
	implements Binding
{
	/** The stack offset. */
	protected final int stackoffset;
	
	/** The stack length. */
	protected final int stacklength;
	
	/** Registers which are associated with this binding. */
	private final MIPSRegister[] _registers;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the MIPS binding to a register.
	 *
	 * @param __r The register to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public MIPSBinding(MIPSRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Set single register
		this._registers = new MIPSRegister[]{__r};
		
		// Not used
		this.stackoffset = Integer.MIN_VALUE;
		this.stacklength = Integer.MIN_VALUE;
	}
	
	/**
	 * Initializes the MIPS binding to multiple registers.
	 *
	 * @param __r The register to bind to.
	 * @param __a The other registers to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public MIPSBinding(MIPSRegister __r, MIPSRegister... __a)
		throws NullPointerException
	{
		// Check
		if (__r == null || __a == null)
			throw new NullPointerException("NARG");
		
		// Copy registers
		int n = 1 + __a.length;
		MIPSRegister[] registers = new MIPSRegister[n];
		registers[0] = __r;
		for (int i = 0, o = 1; o < n; i++, o++)
			registers[o] = Objects.<MIPSRegister>requireNonNull(__a[i], 
				"NARG");
		
		// Set
		this._registers = registers;
		
		// Not used
		this.stackoffset = Integer.MIN_VALUE;
		this.stacklength = Integer.MIN_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			MIPSRegister[] registers = this._registers;
			if (registers != null)
				rv = Arrays.asList(registers).toString();
			else
				rv = String.format("<%+d, %d>", this.stackoffset,
					this.stacklength);
			
			// Store
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
	}
}

