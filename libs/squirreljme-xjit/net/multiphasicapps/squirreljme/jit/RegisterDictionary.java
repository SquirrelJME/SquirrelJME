// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This register dictionary contains the registers that are available for
 * usage in the target architecture. This contains information needed for
 * storing saved, temporary, argument, and other special registers.
 *
 * All {@link Set}s used in this class must be ordered (insertion order in
 * {@link LinkedHashSet} for example) in that every iteration must return the
 * register in the sequence.
 *
 * If a CPU uses registers that contain both floating point and integer
 * values then the register sets should return the same set of registers.
 *
 * @since 2017/04/01
 */
public abstract class RegisterDictionary
{
	/** Both allocation saved. */
	private volatile Reference<Set<Register>> _bas;
	
	/** Both allocation temporary. */
	private volatile Reference<Set<Register>> _bat;
	
	/**
	 * Returns the set of registers which are available for allocation.
	 *
	 * @param __float If {@code true} then use floating point registers.
	 * @param __saved If {@code true} then request saved registers.
	 * @return The registers available for allocation.
	 * @since 2017/03/25
	 */
	public abstract Set<Register> allocationRegisters(boolean __float,
		boolean __saved);
	
	/**
	 * Returns the argument registers.
	 *
	 * @param __float If {@code true} then use floating point registers.
	 * @return The argument registers.
	 * @since 2017/04/01
	 */
	public abstract Set<Register> argumentRegisters(boolean __float);
	
	/**
	 * Returns the temporary assembler register.
	 *
	 * @return The temporary assembler register.
	 * @since 2017/03/24
	 */
	public abstract RegisterList assemblerTemporaryRegister();
	
	/**
	 * Returns the frame pointer register.
	 *
	 * @return The register at the base of the stack.
	 * @since 2017/03/21
	 */
	public abstract RegisterList framePointerRegister();
	
	/**
	 * The register is contains the global table register.
	 *
	 * @return The global table register.
	 * @since 2017/03/24
	 */
	public abstract RegisterList globalTableRegister();
	
	/**
	 * Returns the set of every register which is saved.
	 *
	 * @param __float If {@code true} then use floating point registers.
	 * @return The set of saved registers.
	 * @since 2017/04/01
	 */
	public abstract Set<Register> savedRegisters(boolean __float);
	
	/**
	 * Returns the stack pointer register.
	 *
	 * @return The stack pointer register.
	 * @since 2017/03/23
	 */
	public abstract RegisterList stackPointerRegister();
	
	/**
	 * Returns the set of every register which is temporary.
	 *
	 * @param __float If {@code true} then use floating point registers.
	 * @return The set of temporary registers.
	 * @since 2017/04/01
	 */
	public abstract Set<Register> temporaryRegisters(boolean __float);
	
	/**
	 * Returns the set of registers which are available for allocation, this
	 * includes both integer and floating point registers.
	 *
	 * @param __saved If {@code true} then request saved registers.
	 * @return The registers available for allocation.
	 * @since 2017/04/19
	 */
	public final Set<Register> bothAllocationRegisters(boolean __saved)
	{
		Reference<Set<Register>> ref = (__saved ? this._bas : this._bat);
		Set<Register> rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
		{
			// Target map
			Set<Register> target = new LinkedHashSet<>();
			
			// Fill with both
			target.addAll(allocationRegisters(false, __saved));
			target.addAll(allocationRegisters(true, __saved));
			
			// Store them
			rv = UnmodifiableSet.<Register>of(target);
			ref = new WeakReference<>(rv);
			if (__saved)
				this._bas = ref;
			else
				this._bat = ref;
		}
		
		return rv;
	}
	
	/**
	 * Checks if the specified register is an argument register.
	 *
	 * @param __r The register to check to see if it is an argument regster.
	 * @return {@code true} if the register is an argument register.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public final boolean isRegisterArgument(Register __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return argumentRegisters(false).contains(__r) ||
			argumentRegisters(true).contains(__r);
	}
	
	/**
	 * Checks if the specified register is a callee saved register, one that
	 * is saved at the start of a method and restored before it returns.
	 *
	 * @param __r The register to check to see if it is saved.
	 * @return {@code true} if the register is saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public final boolean isRegisterSaved(Register __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return savedRegisters(false).contains(__r) ||
			savedRegisters(true).contains(__r);
	}
	
	/**
	 * Checks if the specified register is a caller saved register, one that
	 * is saved before a method call and restored following it.
	 *
	 * @param __r The register to check to see if it is temporary.
	 * @return {@code true} if the register is temporary.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public final boolean isRegisterTemporary(Register __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return temporaryRegisters(false).contains(__r) ||
			temporaryRegisters(true).contains(__r);
	}
}

