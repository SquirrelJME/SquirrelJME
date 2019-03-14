// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.StackMapTable;
import net.multiphasicapps.classfile.StackMapTableState;

/**
 * This class is used to transform normal byte code into register code that
 * is more optimized for VMs.
 *
 * @since 2019/03/14
 */
final class __Registerize__
{
	/** The input byte code to translate. */
	protected final ByteCode bytecode;
	
	/** The state of the stack. */
	protected final __StackState__ stack;
	
	/** The stack map table. */
	protected final StackMapTable stackmap;
	
	/**
	 * Converts the input byte code to a register based code.
	 *
	 * @param __bc The byte code to translate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/14
	 */
	__Registerize__(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		this.bytecode = __bc;
		this.stackmap = __bc.stackMapTable();
		this.stack = new __StackState__(__bc.maxLocals(), __bc.maxStack());
	}
	
	/**
	 * Converts the byte code into register code.
	 *
	 * @return The resulting register code.
	 * @since 2019/03/14
	 */
	public RegisterCode convert()
	{
		ByteCode bytecode = this.bytecode;
		StackMapTable stackmap = this.stackmap;
		__StackState__ stack = this.stack;
		
		// Process every instruction
		for (Instruction inst : bytecode)
		{
			// Debug
			todo.DEBUG.note("Xlate %s", inst);
			
			// If there is a defined stack map table state (this will be for
			// any kind of branch or exception handler), load that so it can
			// be worked from
			StackMapTableState smts = stackmap.get(inst.address());
			if (smts != null)
				stack.fromState(smts);
			
			// Process instructions
			this.__process(inst);
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes a single instruction.
	 *
	 * @param __i The instruction to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/14
	 */
	private final void __process(Instruction __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Depends on the operation to process
		int op;
		switch ((op = __i.operation()))
		{
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
				__runALoad(op - InstructionIndex.ALOAD_0);
				break;
			
			default:
				throw new todo.TODO(__i.toString());
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads single reference from a local to the stack.
	 *
	 * @param __l The reference to load.
	 * @since 2019/03/14
	 */
	private final void __runALoad(int __l)
	{
		throw new todo.TODO();
	}
}

