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
					// Dup
				case 89:
					__dup(xop);
					break;
									
					// New
				case 187:
					__new(xop);
					break;
				
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
	 * Duplicates the top-most stack item.
	 *
	 * @param __op The current operation.
	 * @since 2016/04/12
	 */
	private void __dup(CPOp __op)
	{
		// Get the input variables
		CPVariables xin = __op.variables();
		
		// Get the topmost variable
		int top;
		CPVariables.Slot at = xin.get((top = xin.getStackTop()) - 1);
		
		// Duplicate it
		set(__op, top, top + 1, at.type());
	}
	
	/**
	 * Calculates the new operation.
	 *
	 * @param __op The current operation.
	 * @since 2016/04/11
	 */
	private void __new(CPOp __op)
	{
		// Get the input variables
		CPVariables xin = __op.variables();
		
		// Just add an element to the stack
		int top;
		set(__op, (top = xin.getStackTop()), top + 1, CPVariableType.OBJECT);
	}
}

