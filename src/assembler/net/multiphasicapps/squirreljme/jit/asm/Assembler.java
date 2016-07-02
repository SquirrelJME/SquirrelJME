// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.asm;

/**
 * This class represents the base of the assembler which is used to ultimately
 * generate native machine code.
 *
 * @since 2016/07/02
 */
public final class Assembler
{
	/**
	 * Initializes the assembler instance.
	 *
	 * @param __ac The configuration used for basic assembly operations.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/02
	 */
	public Assembler(AssemblerConfig __ac)
		throws NullPointerException
	{
		// Check
		if (__ac == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Adds the value of register A and register B and
	 * places it into register D.
	 *
	 * @param __aos Where to write the instructions to.
	 * @param __dt The type of the destination register.
	 * @param __d The destination register.
	 * @param __at The type of the left side source register.
	 * @param __a The left side source register.
	 * @param __by The type of the right side source register.
	 * @param __b The right side source register.
	 * @throws AssemblerException If the code could not be generated.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/02
	 */
	public void add(AssemblerOutputStream __aos, AssemblerRegisterType __dt,
		String __d, AssemblerRegisterType __at, String __a,
		AssemblerRegisterType __bt, String __b)
		throws AssemblerException, IOException, NullPointerException
	{
		// Check
		if (__aos == null || __d == null || __a == null || __b == null ||
			__dt == null || __at == null || __bt == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

