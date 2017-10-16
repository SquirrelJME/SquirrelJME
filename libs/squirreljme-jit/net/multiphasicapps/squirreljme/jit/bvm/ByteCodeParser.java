// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bvm;

import net.multiphasicapps.squirreljme.jit.cff.ByteCode;
import net.multiphasicapps.squirreljme.jit.cff.Instruction;

/**
 * This class parses the byte code for a method and creates a basic virtual
 * machine program from it which contains a very primitive representation of
 * the given program.
 *
 * @since 2017/10/16
 */
public class ByteCodeParser
{
	/** The byte code to source from. */
	protected final ByteCode bytecode;
	
	/**
	 * Initializes the byte code parser.
	 *
	 * @param __bc The byte code to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/16
	 */
	public ByteCodeParser(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		this.bytecode = __bc;
	}
	
	/**
	 * Parses the byte code and builds the program.
	 *
	 * @return The resulting program.
	 * @since 2017/10/16
	 */
	public final BasicProgram parse()
	{
		// Setup parser state for initial entry into the program
		ByteCode bytecode = this.bytecode;
		
		// Execute all instructions
		for (int dx = 0, dxn = bytecode.instructionCount(); dx < dxn; dx++)
		{
			// Setup instruction for execution
			Instruction i = bytecode.getByIndex(dx);
		
			// Depends on the operation
			int op;
			switch ((op = i.operation()))
			{
					// {@squirreljme.error JI3x Cannot execute the given
					// instruction because it is not implemented in the
					// byte code parser. (The instruction)}
				default:
					throw new ParserException(String.format("JI3x %s", i));
			}
		}
		
		throw new todo.TODO();
	}
}

