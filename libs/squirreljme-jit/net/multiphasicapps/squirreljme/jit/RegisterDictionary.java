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

import java.util.Set;

/**
 * This register dictionary contains the registers that are available for
 * usage in the target architecture. This contains information needed for
 * storing saved, temporary, argument, and other special registers.
 *
 * @since 2017/04/01
 */
public abstract class RegisterDictionary
{
	/**
	 * Returns the set of registers which are available for allocation.
	 *
	 * @param __saved If {@code true} then request saved registers.
	 * @return The registers available for allocation.
	 * @since 2017/03/25
	 */
	public abstract Set<Register> allocationRegisters(boolean __saved);
	
	/**
	 * Returns the argument registers.
	 *
	 * @return The argument registers.
	 * @since 2017/04/01
	 */
	public abstract Set<Register> argumentRegisters();
	
	/**
	 * Returns the temporary assembler register.
	 *
	 * @return The temporary assembler register.
	 * @since 2017/03/24
	 */
	public abstract Register assemblerTemporaryRegister();
	
	/**
	 * Returns the frame pointer register.
	 *
	 * @return The register at the base of the stack.
	 * @since 2017/03/21
	 */
	public abstract Register framePointerRegister();
	
	/**
	 * The register is contains the global table register.
	 *
	 * @return The global table register.
	 * @since 2017/03/24
	 */
	public abstract Register globalTableRegister();
	
	/**
	 * Returns the set of every register which is saved.
	 *
	 * @return The set of saved registers.
	 * @since 2017/04/01
	 */
	public abstract Set<Register> savedRegisters();
	
	/**
	 * Returns the stack pointer register.
	 *
	 * @return The stack pointer register.
	 * @since 2017/03/23
	 */
	public abstract Register stackPointerRegister();
	
	/**
	 * Returns the set of every register which is temporary.
	 *
	 * @return The set of temporary registers.
	 * @since 2017/04/01
	 */
	public abstract Set<Register> temporaryRegisters();
	
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
		
		return argumentRegisters().contains(__r);
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
		
		return savedRegisters().contains(__r);
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
		
		return temporaryRegisters().contains(__r);
	}
}

