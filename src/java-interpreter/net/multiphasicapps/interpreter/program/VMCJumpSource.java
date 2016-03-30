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

import net.multiphasicapps.interpreter.JVMVerifyException;

/**
 * This represents the source of a jump (which is not natural) to get to this
 * specific location from another instruction.
 *
 * It should be noted that the address ranges are both inclusive.
 *
 * @since 2016/03/30
 */
public class VMCJumpSource
{
	/** The owning program. */
	protected final VMCProgram program;
	
	/** The type of jump. */
	protected final VMCJumpType type;
	
	/** The low address (inclusive). */
	protected final int lowaddr;
	
	/** The high address (inclusive). */
	protected final int highaddr;
	
	/**
	 * Initializes the jump source.
	 *
	 * @param __prg The program containing the jump source.
	 * @param __jt The type of jump this is.
	 * @param __addr The source address of the jump.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	VMCJumpSource(VMCProgram __prg, VMCJumpType __jt, int __addr)
		throws NullPointerException
	{
		this(__prg, __jt, __addr, __addr);
	}
	
	/**
	 * Initializes the jump source.
	 *
	 * @param __prg The program containing the jump source.
	 * @param __jt The type of jump this is.
	 * @param __lo The inclusive starting address of the jump.
	 * @param __hi The inclusive ending address of the jump.
	 * @throws IllegalArgumentException If the jump type is not exceptional
	 * and the low address is not equal to the high address.
	 * @throws JVMVerifyException If the low or high address are negative or
	 * exceed the program size, or the high address is lower than the lower
	 * address.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	VMCJumpSource(VMCProgram __prg, VMCJumpType __jt, int __lo, int __hi)
		throws IllegalArgumentException, JVMVerifyException,
			NullPointerException
	{
		// Check
		if (__prg == null || __jt == null)
			throw new NullPointerException("NARG");
		if (__lo < 0 || __hi < 0 || __hi < __lo || __lo >= __prg.size() ||
			__hi >= __prg.size())
			throw new JVMVerifyException(String.format("IN29 %s %s",
				__lo, __hi));
		if (__jt != VMCJumpType.EXCEPTIONAL && __lo != __hi)
			throw new IllegalArgumentException("IN2a");
		
		// Set
		program = __prg;
		type = __jt;
		lowaddr = Math.min(__lo, __hi);
		highaddr = Math.max(__lo, __hi);
	}
}

