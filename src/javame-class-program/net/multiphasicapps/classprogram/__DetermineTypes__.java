// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.classfile.CFConstantEntry;
import net.multiphasicapps.classfile.CFConstantPool;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This determines the types that variables are and is part of the verification
 * state of the program.
 *
 * @since 2016/04/13
 */
final class __DetermineTypes__
{
	/** The owning program. */
	protected final CPProgram program;
	
	/** Operation processing queue. */
	protected final Deque<CPOp> queue =
		new LinkedList<>();
	
	/** The base of the stack. */
	protected final int stackbase;
	
	/** The constant pool. */
	protected final CFConstantPool constantpool;
	
	/**
	 * This calculates stack types.
	 *
	 * @param __prg The program owning this calculator.
	 * @since 2016/04/11
	 */
	__DetermineTypes__(CPProgram __prg)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
		constantpool = program.constantPool();
		
		// Setup stack base
		stackbase = program.maxLocals();
		
		// At the first instruction to the queue initially
		queue.offerLast(program.get(0));
	}
	
	/**
	 * Performs calculations which remain in the queue.
	 *
	 * @return {@code true} if the queue still has items in it.
	 * @since 2016/04/11
	 */
	public boolean calculate()
	{
		for (;;)
		{
			// If the queue is empty, find the next instruction to process is
			// an untouched instruction
			if (queue.isEmpty())
				for (CPOp xop : program)
					if (xop._calccount == 0)
					{
						// Add it
						queue.add(xop);
						
						// Stop processing because it is possible that this
						// instruction jumps to another instruction which
						// might setup a bunch of states if needed.
						break;
					}
			
			// Poll the operation in the queue
			CPOp xop = queue.pollFirst();
			
			// End of execution?
			if (xop == null)
				return !queue.isEmpty();
			
			// Debug
			System.err.printf("DEBUG -- Calc %d%n", xop.address());
			
			// Current operation variables
			CPVariables xin = xop.variables();
			
			// Increase the operation calculation count to later find
			// operations which never got calculations performed for them.
			int clcn = xop._calccount++;
			
			// Multiple jump sources?
			boolean hashandlers = !xop.exceptionsHandled().isEmpty();
			boolean mulsource = (xop.jumpSources().size() > 1) ||
				hashandlers;
			
			// If this is the first address or this handles exceptions then
			// the stack must be empty.
			if (xop.address() == 0 || hashandlers)
			{
				// {@squirreljme.error CP0x The instruction is an exception
				// handler or is the entry of a method and it has elements
				// on the stack. The stack in these situations must be empty.
				// (The current instruction address)}
				int myt, psb;
				if ((myt = xin.getStackTop()) != (psb = program.maxLocals()))
					throw new CPProgramException(String.format("CP0x %d",
						xop.address()));
			}
			
			// Depends on the opcode
			int opcode = xop.instructionCode();
			switch (opcode)
			{
				case 21: __load(xop, xin, CPVariableType.INTEGER); break;
				case 22: __load(xop, xin, CPVariableType.LONG); break;
				case 23: __load(xop, xin, CPVariableType.FLOAT); break;
				case 24: __load(xop, xin, CPVariableType.DOUBLE); break;
				case 25: __load(xop, xin, CPVariableType.OBJECT); break;
				
				case 26:
				case 27:
				case 28:
				case 29:
					__load_n(xop, xin, opcode - 26, CPVariableType.INTEGER);
					break;
				
				case 30:
				case 31:
				case 32:
				case 33:
					__load_n(xop, xin, opcode - 30, CPVariableType.LONG);
					break;
				
				case 34:
				case 35:
				case 36:
				case 37:
					__load_n(xop, xin, opcode - 24, CPVariableType.FLOAT);
					break;
				
				case 38:
				case 39:
				case 40:
				case 41:
					__load_n(xop, xin, opcode - 38, CPVariableType.DOUBLE);
					break;
					
				case 42:
				case 43:
				case 44:
				case 45:
					__load_n(xop, xin, opcode - 42, CPVariableType.OBJECT);
					break;
					
				case 89: __dup(xop, xin); break;
				case 178: __getstatic(xop, xin); break;
				case 187: __new(xop, xin); break;
				
				case 50197: __load_w(xop, xin, CPVariableType.INTEGER); break;
				case 50198: __load_w(xop, xin, CPVariableType.LONG); break;
				case 50199: __load_w(xop, xin, CPVariableType.FLOAT); break;
				case 50200: __load_w(xop, xin, CPVariableType.DOUBLE); break;
				case 50201: __load_w(xop, xin, CPVariableType.OBJECT); break;
				
					// {@squirreljme.error CP0u Cannot calculate the SSA for
					// the given opcode because it is unknown. (The program
					// address; The opcode)}
				default:
					throw new CPProgramException(String.format("CP0u %d %d",
						xop.address(), opcode));
			}
			
			// If this operation handles exceptions then it potentially needs
			// to derive variable state from the source operations.
			for (CPOp e : xop.exceptionsHandled())
				throw new Error("TODO");
			
			// Offer jump targets to the queue
			for (CPOp jt : xop.jumpTargets())
				queue.offerLast(jt);
			
			// Debug
			System.err.printf("DEBUG -- State: %s%n", xop);
		}
	}
	
	/**
	 * Sets the operations for all targets and potentially checks them.
	 *
	 * @parma __op The operation to set targets for.
	 * @param __sl The slot to modify.
	 * @param __top The top of the stack, {@code Integer.MIN_VALUE} if it does
	 * not change.
	 * @param __vt The type of variable to set, if {@code null} it is not
	 * changed.
	 * @since 2016/04/11
	 */
	public void set(CPOp __op, int __sl, int __top, CPVariableType __vt)
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Go through all targets
		for (CPOp xop : __op.jumpTargets())
		{
			// Get the target variables
			CPVariables tvars = xop.variables();
			
			// Set the top of the stack?
			if (__top != Integer.MIN_VALUE)
				tvars.__checkedSetStackTop(__top);
			
			// Get slot
			CPVariables.Slot sl;
			try
			{
				sl = tvars.get(__sl);
			}
			
			// Out of bounds read of slot
			catch (IndexOutOfBoundsException e)
			{
				// {@squirreljme.error CP13 Attempt to access a slot which is
				// not within the program bounds. (The slot index)}
				throw new CPProgramException(String.format("CP13 %s", __sl),
					e);
			}
			
			// Setting a type?
			if (__vt != null)
				sl.__checkedSetType(__vt);
		}
	}
	
	/**
	 * Load variable from locals and push it onto the stack.
	 *
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	private void __load(CPOp __xop, CPVariables __xin, CPVariableType __t)
	{
		__load_n(__xop, __xin, __xop.__readUByte(1), __t);
	}
	
	/**
	 * Load variable from locals and push it onto the stack.
	 *
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __dx The index.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	private void __load_n(CPOp __xop, CPVariables __xin, int __dx,
		CPVariableType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			// {@squirreljme.error CP1b Out of bounds index.}
			if (__dx >= stackbase)
				throw new IndexOutOfBoundsException("CP1b");
			
			// Get local variable slot
			CPVariables.Slot local = __xin.get(__dx);
			
			// {@squirreljme.error CP1a Expected the local variable to be of
			// the given type, however it was not. (The operation address;
			// The slot index; The type the slot should have been; The type
			// it was)}
			CPVariableType was;
			if ((was = local.type()) != __t)
				throw new CPProgramException(String.format("CP1a %d %d %s %s",
					__xop.address(), __dx, __t, was));
			
			// Push to the stack
			int top;
			set(__xop, (top = __xin.getStackTop()), top + 1, __t);
			
			// If wide, add a top
			if (__t.isWide())
			{
				// {@squirreljme.error CP1c Top of long/double is out of
				// bounds.}
				int ndx = __dx + 1;
				if (ndx >= stackbase)
					throw new IndexOutOfBoundsException("CP1c");
				
				// {@squirreljme.error CP1d Read of a wide local variable,
				// however there is not a TOP following it. (The operation
				// address; The index to read from; The type that it was)}
				if ((was = __xin.get(ndx).type()) != CPVariableType.TOP)
					throw new CPProgramException(String.format("CP1d %d %d %s",
						__xop.address(), ndx, was));
				
				// Set it to the top
				set(__xop, (top = __xin.getStackTop()), top + 1,
					CPVariableType.TOP);
			}
		}
		
		// Out of bounds
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error CP19 Attempt to read local variable from a
			// slot which is not within bounds. (The operation address;
			// The slot index)}
			throw new CPProgramException(String.format("CP19 %d %d",
				__xop.address(), __dx), e);
		}
	}
	
	/**
	 * Load variable from locals and push it onto the stack (wide).
	 *
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	private void __load_w(CPOp __xop, CPVariables __xin, CPVariableType __t)
	{
		__load_n(__xop, __xin, __xop.__readUShort(1), __t);
	}
	
	/**
	 * Duplicates the top-most stack item.
	 *
	 * @param __op The current operation.
	 * @param __xin Input variables.
	 * @since 2016/04/12
	 */
	private void __dup(CPOp __op, CPVariables __xin)
	{
		// Get the topmost variable
		int top;
		CPVariables.Slot at = __xin.get((top = __xin.getStackTop()) - 1);
		
		// Duplicate it
		set(__op, top, top + 1, at.type());
	}
	
	/**
	 * Gets a static variable.
	 *
	 * @param __op The current operation.
	 * @param __xin Input variables.
	 * @since 2016/04/12
	 */
	private void __getstatic(CPOp __op, CPVariables __xin)
	{
		// Read the field value which is read
		CFConstantEntry.FieldReference ref = constantpool.
			<CFConstantEntry.FieldReference>getAs(__op.__readUShort(1),
			CFConstantEntry.FieldReference.class);
		
		// Get the variable associated for the given field
		CPVariableType type = CPVariableType.bySymbol(
			ref.nameAndType().getValue().asField());
		
		// Add it to the top of the stack
		int top;
		set(__op, (top = __xin.getStackTop()), top + 1, type);
		
		// If wide, add TOP
		if (type.isWide())
			set(__op, (top = __xin.getStackTop()), top + 1,
				CPVariableType.TOP);
	}
	
	/**
	 * Calculates the new operation.
	 *
	 * @param __op The current operation.
	 * @param __xin Input variables.
	 * @since 2016/04/11
	 */
	private void __new(CPOp __op, CPVariables __xin)
	{
		// Just add an element to the stack
		int top;
		set(__op, (top = __xin.getStackTop()), top + 1, CPVariableType.OBJECT);
	}
}

