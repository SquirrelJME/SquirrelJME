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
	 * Checks if the specified register is an argument register.
	 *
	 * @param __r The register to check to see if it is an argument regster.
	 * @return {@code true} if the register is an argument register.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public abstract boolean isRegisterArgument(Register __r)
		throws NullPointerException;
	
	/**
	 * Checks if the specified register is a callee saved register, one that
	 * is saved at the start of a method and restored before it returns.
	 *
	 * @param __r The register to check to see if it is saved.
	 * @return {@code true} if the register is saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public abstract boolean isRegisterSaved(Register __r)
		throws NullPointerException;
	
	/**
	 * Checks if the specified register is a caller saved register, one that
	 * is saved before a method call and restored following it.
	 *
	 * @param __r The register to check to see if it is temporary.
	 * @return {@code true} if the register is temporary.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public abstract boolean isRegisterTemporary(Register __r)
		throws NullPointerException;
	
	/**
	 * Returns the stack pointer register.
	 *
	 * @return The stack pointer register.
	 * @since 2017/03/23
	 */
	public abstract Register stackPointerRegister();
}

