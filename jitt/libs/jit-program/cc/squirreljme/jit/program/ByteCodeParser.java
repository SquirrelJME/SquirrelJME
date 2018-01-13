// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.program;

import cc.squirreljme.jit.classfile.ByteCode;
import cc.squirreljme.jit.classfile.Instruction;
import cc.squirreljme.jit.classfile.InstructionIndex;

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
	
	/** The state of every variable which is available. */
	protected final Variables variables;
	
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
		
		// Setup variables, which uses the initial stack map state
		int maxstack = __bc.maxStack(),
			maxlocals = __bc.maxLocals();
		Variables variables = new Variables(maxstack, maxlocals);
		variables.initializeLocalsFrom(__bc.stackMapTable().get(0));
		this.variables = variables;
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
				case InstructionIndex.ALOAD:
					__opLoad(__BasicType__.OBJECT, i.intArgument(0));
					break;
				
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					__opLoad(__BasicType__.OBJECT,
						op - InstructionIndex.ALOAD_0);
					break;
					
				case InstructionIndex.ILOAD:
					__opLoad(__BasicType__.INTEGER, i.intArgument(0));
					break;
				
				case InstructionIndex.ILOAD_0:
				case InstructionIndex.ILOAD_1:
				case InstructionIndex.ILOAD_2:
				case InstructionIndex.ILOAD_3:
					__opLoad(__BasicType__.INTEGER,
						op - InstructionIndex.ILOAD_0);
					break;
					
				case InstructionIndex.LLOAD:
					__opLoad(__BasicType__.LONG, i.intArgument(0));
					break;
				
				case InstructionIndex.LLOAD_0:
				case InstructionIndex.LLOAD_1:
				case InstructionIndex.LLOAD_2:
				case InstructionIndex.LLOAD_3:
					__opLoad(__BasicType__.LONG,
						op - InstructionIndex.LLOAD_0);
					break;
					
				case InstructionIndex.FLOAD:
					__opLoad(__BasicType__.FLOAT, i.intArgument(0));
					break;
				
				case InstructionIndex.FLOAD_0:
				case InstructionIndex.FLOAD_1:
				case InstructionIndex.FLOAD_2:
				case InstructionIndex.FLOAD_3:
					__opLoad(__BasicType__.FLOAT,
						op - InstructionIndex.FLOAD_0);
					break;
					
				case InstructionIndex.DLOAD:
					__opLoad(__BasicType__.DOUBLE, i.intArgument(0));
					break;
				
				case InstructionIndex.DLOAD_0:
				case InstructionIndex.DLOAD_1:
				case InstructionIndex.DLOAD_2:
				case InstructionIndex.DLOAD_3:
					__opLoad(__BasicType__.DOUBLE,
						op - InstructionIndex.DLOAD_0);
					break;
				
				case InstructionIndex.ASTORE:
					__opStore(__BasicType__.OBJECT, i.intArgument(0));
					break;
				
				case InstructionIndex.ASTORE_0:
				case InstructionIndex.ASTORE_1:
				case InstructionIndex.ASTORE_2:
				case InstructionIndex.ASTORE_3:
					__opStore(__BasicType__.OBJECT,
						op - InstructionIndex.ASTORE_0);
					break;
					
				case InstructionIndex.ISTORE:
					__opStore(__BasicType__.INTEGER, i.intArgument(0));
					break;
				
				case InstructionIndex.ISTORE_0:
				case InstructionIndex.ISTORE_1:
				case InstructionIndex.ISTORE_2:
				case InstructionIndex.ISTORE_3:
					__opStore(__BasicType__.INTEGER,
						op - InstructionIndex.ISTORE_0);
					break;
					
				case InstructionIndex.LSTORE:
					__opStore(__BasicType__.LONG, i.intArgument(0));
					break;
				
				case InstructionIndex.LSTORE_0:
				case InstructionIndex.LSTORE_1:
				case InstructionIndex.LSTORE_2:
				case InstructionIndex.LSTORE_3:
					__opStore(__BasicType__.LONG,
						op - InstructionIndex.LSTORE_0);
					break;
					
				case InstructionIndex.FSTORE:
					__opStore(__BasicType__.FLOAT, i.intArgument(0));
					break;
				
				case InstructionIndex.FSTORE_0:
				case InstructionIndex.FSTORE_1:
				case InstructionIndex.FSTORE_2:
				case InstructionIndex.FSTORE_3:
					__opStore(__BasicType__.FLOAT,
						op - InstructionIndex.FSTORE_0);
					break;
					
				case InstructionIndex.DSTORE:
					__opStore(__BasicType__.DOUBLE, i.intArgument(0));
					break;
				
				case InstructionIndex.DSTORE_0:
				case InstructionIndex.DSTORE_1:
				case InstructionIndex.DSTORE_2:
				case InstructionIndex.DSTORE_3:
					__opStore(__BasicType__.DOUBLE,
						op - InstructionIndex.DSTORE_0);
					break;
					
					// {@squirreljme.error AK01 Cannot execute the given
					// instruction because it is not implemented in the
					// byte code parser. (The instruction)}
				default:
					throw new ParserException(String.format("AK01 %s", i));
			}
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Handles the load operation.
	 *
	 * @param __bt The basic type to load.
	 * @param __ldx The local variable index to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/17
	 */
	private void __opLoad(__BasicType__ __bt, int __ldx)
		throws NullPointerException
	{
		if (__bt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Handles the store operation.
	 *
	 * @param __bt The basic type to store.
	 * @param __ldx The local variable index to store into.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/17
	 */
	private void __opStore(__BasicType__ __bt, int __ldx)
		throws NullPointerException
	{
		if (__bt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

