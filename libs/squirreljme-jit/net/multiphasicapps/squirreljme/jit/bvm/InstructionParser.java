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
import net.multiphasicapps.squirreljme.jit.cff.MethodHandle;

/**
 * This class is used to parse instructions for individual methods within the
 * virtual machine. Each parser remembers its own state as to what is occurring
 * within the virtual machine. On what would be a real machine, this parser
 * acts like a single stack frame within a thread of execution.
 *
 * @since 2017/10/15
 */
public class InstructionParser
{
	/** The byte code which is to be parsed. */
	protected final ByteCode bytecode;
	
	/** The program counter address. */
	private volatile int _pcaddr;
	
	/**
	 * Initializes the instruction parser.
	 *
	 * @param __bc The byte code to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/15
	 */
	public InstructionParser(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the program counter address.
	 *
	 * @param __pc The program counter address.
	 * @since 2017/10/15
	 */
	public void setProgramCounter(int __pc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Executes a single instruction within a single step.
	 *
	 * @since 2017/10/15
	 */
	public void singleStep()
	{
		throw new todo.TODO();
	}
}

