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
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.Register;
import net.multiphasicapps.squirreljme.jit.RegisterDictionary;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This is the register dictionary for MIPS targets.
 *
 * @since 2017/04/01
 */
public class MIPSRegisterDictionary
	extends RegisterDictionary
{
	/** The configuration. */
	protected final MIPSConfig config;
	
	/** Saved allocated registers. */
	private volatile Reference<Set<Register>> _asregs;
	
	/** Temporary allocated registers. */
	private volatile Reference<Set<Register>> _atregs;
	
	/** Argument registers. */
	private volatile Reference<Set<Register>> _argregs;
	
	/** Saved registers. */
	private volatile Reference<Set<Register>> _saveregs;
	
	/** Temporary registers. */
	private volatile Reference<Set<Register>> _tempregs;
	
	/**
	 * Initializes the dictionary.
	 *
	 * @param __conf The config that is used.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	public MIPSRegisterDictionary(MIPSConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> allocationRegisters(boolean __saved)
	{
		Reference<Set<Register>> ref = (__saved ? this._asregs : this._atregs);
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			Set<Register> s = new LinkedHashSet<>();
			for (Register r : NUBI.allocationRegisters(__saved))
				s.add(r);
				
			// Store
			ref = new WeakReference<>((rv = UnmodifiableSet.<Register>of(s)));
			if (__saved)
				this._asregs = ref;
			else
				this._atregs = ref;
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> argumentRegisters()
	{
		Reference<Set<Register>> ref = this._argregs;
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._argregs = new WeakReference<>((rv = UnmodifiableSet.
				<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
					NUBI.A0, NUBI.A1, NUBI.A2, NUBI.A3, NUBI.A4, NUBI.A5,
					NUBI.A6, NUBI.A7, NUBI.FA0, NUBI.FA1, NUBI.FA2, NUBI.FA3,
					NUBI.FA4, NUBI.FA5, NUBI.FA6, NUBI.FA7)))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Register assemblerTemporaryRegister()
	{
		return NUBI.AT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Register framePointerRegister()
	{
		return NUBI.FP;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Register globalTableRegister()
	{
		return NUBI.GP;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> savedRegisters()
	{
		Reference<Set<Register>> ref = this._saveregs;
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._saveregs = new WeakReference<>((rv = UnmodifiableSet.
				<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
					NUBI.S0, NUBI.S1, NUBI.S2, NUBI.S3, NUBI.S4, NUBI.S5,
					NUBI.S6, NUBI.S7, NUBI.S8, NUBI.S9, NUBI.S10, NUBI.S11,
					NUBI.FS0, NUBI.FS1, NUBI.FS2, NUBI.FS3, NUBI.FS4, NUBI.FS5,
					NUBI.FS6, NUBI.FS7, NUBI.FS8, NUBI.FS9, NUBI.FS10,
					NUBI.FS11)))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Register stackPointerRegister()
	{
		return NUBI.SP;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> temporaryRegisters()
	{
		Reference<Set<Register>> ref = this._tempregs;
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._tempregs = new WeakReference<>((rv = UnmodifiableSet.
				<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
					NUBI.PF, NUBI.AT, NUBI.T1, NUBI.T2, NUBI.FT0, NUBI.FT1,
					NUBI.FT2, NUBI.FT3, NUBI.FT4, NUBI.FT5, NUBI.FT6, NUBI.FT7,
					NUBI.FT8, NUBI.FT9, NUBI.FT10, NUBI.FT11)))));
		
		return rv;
	}
}

