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
import net.multiphasicapps.squirreljme.jit.RegisterList;
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
	
	/** Saved allocated registers (int). */
	private volatile Reference<Set<Register>> _iasregs;
	
	/** Temporary allocated registers (int). */
	private volatile Reference<Set<Register>> _iatregs;
	
	/** Argument registers (int). */
	private volatile Reference<Set<Register>> _iargregs;
	
	/** Saved registers (int). */
	private volatile Reference<Set<Register>> _isaveregs;
	
	/** Temporary registers (int). */
	private volatile Reference<Set<Register>> _itempregs;
	
	/** Saved allocated registers (float). */
	private volatile Reference<Set<Register>> _fasregs;
	
	/** Temporary allocated registers (float). */
	private volatile Reference<Set<Register>> _fatregs;
	
	/** Argument registers (float). */
	private volatile Reference<Set<Register>> _fargregs;
	
	/** Saved registers (float). */
	private volatile Reference<Set<Register>> _fsaveregs;
	
	/** Temporary registers (float). */
	private volatile Reference<Set<Register>> _ftempregs;
	
	/** The temporary assembler register. */
	private volatile Reference<RegisterList> _regat;
	
	/** The frame pointer register. */
	private volatile Reference<RegisterList> _regfp;
	
	/** The stack pointer. */
	private volatile Reference<RegisterList> _regsp;
	
	/** The global table pointer. */
	private volatile Reference<RegisterList> _reggp;
	
	/** The exception register. */
	private volatile Reference<RegisterList> _regex;
	
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
	public Set<Register> allocationRegisters(boolean __float, boolean __saved)
	{
		Reference<Set<Register>> ref = (__float ?
			(__saved ? this._fasregs : this._fatregs) :
			(__saved ? this._iasregs : this._iatregs));
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			Set<Register> s = new LinkedHashSet<>();
			for (MIPSRegister r : NUBI.allocationRegisters(__saved))
				if (__float == r.isFloat())
					s.add(r);
				
			// Store
			ref = new WeakReference<>((rv = UnmodifiableSet.<Register>of(s)));
			if (__float)
				if (__saved)
					this._fasregs = ref;
				else
					this._fatregs = ref;
			else
				if (__saved)
					this._iasregs = ref;
				else
					this._iatregs = ref;
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> argumentRegisters(boolean __float)
	{
		Reference<Set<Register>> ref = (__float ? this._fargregs :
			this._iargregs);
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			if (__float)
				this._iargregs = new WeakReference<>((rv = UnmodifiableSet.
					<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
						NUBI.FA0, NUBI.FA1, NUBI.FA2, NUBI.FA3,
						NUBI.FA4, NUBI.FA5, NUBI.FA6, NUBI.FA7)))));
			else
				this._iargregs = new WeakReference<>((rv = UnmodifiableSet.
					<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
						NUBI.A0, NUBI.A1, NUBI.A2, NUBI.A3, NUBI.A4, NUBI.A5,
						NUBI.A6, NUBI.A7)))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public RegisterList assemblerTemporaryRegister()
	{
		Reference<RegisterList> ref = this._regat;
		RegisterList rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._regat = new WeakReference<>(
				(rv = new RegisterList(NUBI.AT)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/21
	 */
	@Override
	public RegisterList exceptionRegister()
	{
		Reference<RegisterList> ref = this._regex;
		RegisterList rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._regex = new WeakReference<>(
				(rv = new RegisterList(NUBI.A2)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public RegisterList framePointerRegister()
	{
		Reference<RegisterList> ref = this._regfp;
		RegisterList rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._regfp = new WeakReference<>(
				(rv = new RegisterList(NUBI.FP)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public RegisterList globalTableRegister()
	{
		Reference<RegisterList> ref = this._reggp;
		RegisterList rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._reggp = new WeakReference<>(
				(rv = new RegisterList(NUBI.GP)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> savedRegisters(boolean __float)
	{
		Reference<Set<Register>> ref = (__float ? this._fsaveregs :
			this._isaveregs);
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			if (__float)
				this._fsaveregs = new WeakReference<>((rv = UnmodifiableSet.
					<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
						NUBI.FS0, NUBI.FS1, NUBI.FS2, NUBI.FS3, NUBI.FS4,
						NUBI.FS5, NUBI.FS6, NUBI.FS7, NUBI.FS8, NUBI.FS9,
						NUBI.FS10, NUBI.FS11)))));
			else
				this._isaveregs = new WeakReference<>((rv = UnmodifiableSet.
					<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
						NUBI.S0, NUBI.S1, NUBI.S2, NUBI.S3, NUBI.S4, NUBI.S5,
						NUBI.S6, NUBI.S7, NUBI.S8, NUBI.S9, NUBI.S10,
						NUBI.S11)))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public RegisterList stackPointerRegister()
	{
		Reference<RegisterList> ref = this._regsp;
		RegisterList rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._regsp = new WeakReference<>(
				(rv = new RegisterList(NUBI.SP)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public Set<Register> temporaryRegisters(boolean __float)
	{
		Reference<Set<Register>> ref = (__float ? this._ftempregs :
			this._itempregs);
		Set<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			if (__float)
				this._ftempregs = new WeakReference<>((rv = UnmodifiableSet.
					<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
						NUBI.FT1, NUBI.FT2, NUBI.FT3, NUBI.FT4, NUBI.FT5,
						NUBI.FT6, NUBI.FT7, NUBI.FT8, NUBI.FT9, NUBI.FT10,
						NUBI.FT11)))));
			else
				this._itempregs = new WeakReference<>((rv = UnmodifiableSet.
					<Register>of(new LinkedHashSet<>(Arrays.<Register>asList(
						NUBI.PF, NUBI.AT, NUBI.T1, NUBI.T2)))));
		
		return rv;
	}
}

