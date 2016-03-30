// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.program;

import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.interpreter.JVMVerifyException;

/**
 * This represents a jump target for an instruction which translates to other
 * instructions within the method.
 *
 * It is either explicit (specified targets), or implied (natural program
 * progression).
 *
 * @since 2016/03/30
 */
public class VMCJumpTarget
{
	/** The owning program. */
	protected final VMCProgram program;
	
	/** The type of jump. */
	protected final VMCJumpType type;
	
	/** Target address. */
	protected final int address;
	
	/** The exception to throw. */
	protected final BinaryNameSymbol exception;
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __prg The program this jump is within.
	 * @param __type The type of jump to perform.
	 * @param __addr The target address of the jump.
	 * @throws JVMVerifyException If the address is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	public VMCJumpTarget(VMCProgram __prg, VMCJumpType __type, int __addr)
		throws IllegalArgumentException, JVMVerifyException,
			NullPointerException
	{
		this(__prg, __type, null, __addr);
	}
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __prg The program this jump is within.
	 * @param __type The type of jump to perform.
	 * @param __bn The name of the exception being thrown.
	 * @param __addr The target address of the jump.
	 * @throws IllegalArgumentException If it is not
	 * exceptional and a binary name was specified, or a binary name was
	 * not specified and this is an exception.
	 * @throws JVMVerifyException If the address is out of bounds.
	 * @throws NullPointerException On null arguments, except for {@link __bn}
	 * unless the jump type is an exception.
	 * @since 2016/03/30
	 */
	public VMCJumpTarget(VMCProgram __prg, VMCJumpType __type,
		BinaryNameSymbol __bn, int __addr)
		throws IllegalArgumentException, JVMVerifyException,
			NullPointerException
	{
		// Check
		if (__prg == null || __type == null)
			throw new NullPointerException("NARG");
		if (__addr < 0 || __addr >= __prg.size())
			throw new JVMVerifyException(String.format("IN2c %d", __addr));
		if ((__type == VMCJumpType.EXCEPTIONAL) != (__bn != null))
			throw new IllegalArgumentException("IN2b");
		
		// Set
		program = __prg;
		type = __type;
		exception = __bn;
		address = __addr;
	}
	
	/**
	 * Returns the address to jump to.
	 *
	 * @return The address to jump to.
	 * @since 2016/03/30
	 */
	public int address()
	{
		return address;
	}
	
	/**
	 * Returns the jump type that this jump makes.
	 *
	 * @return The jump type used for this jump.
	 * @since 2016/03/30
	 */
	public VMCJumpType getType()
	{
		return type;
	}
}

