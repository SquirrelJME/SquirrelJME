// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.pure;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.bytecode.BCByteCode;
import net.multiphasicapps.squirreljme.bytecode.BCInstructionID;
import net.multiphasicapps.squirreljme.bytecode.BCOperation;
import net.multiphasicapps.squirreljme.bytecode.BCVariablePush;
import net.multiphasicapps.squirreljme.bytecode.BCVariableType;
import net.multiphasicapps.squirreljme.ci.CICodeAttribute;
import net.multiphasicapps.squirreljme.ci.CIClassReference;
import net.multiphasicapps.squirreljme.terp.TerpException;
import net.multiphasicapps.squirreljme.terp.TerpInterpreter;

/**
 * This is the pure interpreter which takes byte code directly for execution.
 *
 * @since 2016/05/12
 */
public class PureInterpreter
	implements TerpInterpreter
{
	/**
	 * Initializes the interpreter which uses the direct byte code.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public PureInterpreter()
		throws NullPointerException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Deprecated
	public Object interpret(Object... __args)
	{
		throw new Error("TODO");
		/*
		// Setup local variables
		int n = __args.length;
		for (int i = 0; i < n; i++)
			throw new Error("TODO");
		
		// Debug
		List<Object> _DEBUGLOCALS = Arrays.<Object>asList(_locals);
		List<Object> _DEBUGSTACK = Arrays.<Object>asList(_stack);
		
		// Execution loop
		BCByteCode program = this.program;
		for (int pcaddr = 0;;)
		{
			// Debug
			System.err.printf("DEBUG -- Locl: %s; Stak: %s%n", _DEBUGLOCALS,
				_DEBUGSTACK);
			
			// Get the current operation
			BCOperation op = program.get(pcaddr);
			
			// Debug
			System.err.printf("DEBUG -- Exec: %s%n", op);
			
			// Depends on the instruction code
			int code = op.instructionId();
			switch (code)
			{
					// Allocate new object
				case BCInstructionID.NEW:
					pcaddr = __new(op);
					break;
					
					// Shuffle entries on the stack
				case BCInstructionID.SYNTHETIC_STACK_SHUFFLE:
					pcaddr = __stackShuffle(op);
					break;
				
					// {@squirreljme.error AN0q The current operation is not
					// known. (The instruction address; The instruction ID; The
					// operation itself)}
				default:
					throw new TerpException(this.core, TerpException.Issue.
						ILLEGAL_OPCODE, String.format("AN0q %d %d %s",
						pcaddr, code, op));
			}
			
			// Set the new address
			this._pcaddr = pcaddr;
		}
		*/
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __op The operation.
	 * @return The next instruction pointer address.
	 * @since 2016/05/13
	 */
	@Deprecated
	private int __new(BCOperation __op)
	{
		throw new Error("TODO");
		/*
		// Lookup the class
		ClassNameSymbol csn = ((CIClassReference)__op.arguments().get(0)).
			get();
		TerpCore core = this.core;
		TerpClass cl = core.initClass(csn);
		
		// Allocate object (arrays are allocated to zero length using new)
		TerpObject obj = new TerpObject(core, cl, 0);
		
		// Set the topmost stack entry to the new object
		int top = _top;
		_stack[top] = obj;
		_top = top + 1;
		
		// Next address
		return __op.address() + 1;
		*/
	}
	
	/**
	 * Shuffles items on the stack.
	 *
	 * @param __op The operation.
	 * @return The next address.
	 * @since 2016/05/13
	 */
	@Deprecated
	private int __stackShuffle(BCOperation __op)
	{
		throw new Error("TODO");
		/*
		// Get the operations to pop
		List<BCVariableType> pops = __op.stackPops();
		int n = pops.size();
		
		// For pop storage for later pushing
		Object[] store = new Object[n];
		
		// Pop all values
		Object[] stack = _stack;
		int top = _top;
		for (int i = n - 1; i >= 0; i--)
		{
			// Get the type to pop
			BCVariableType vt = pops.get(i);
			
			// If wide, clear top
			if (vt.isWide())
				stack[--top] = null;
			
			// Read value into storage
			store[i] = stack[top - 1];
			stack[--top] = null;
		}
		
		// Get values to push
		List<BCVariablePush> pushes = __op.stackPushes();
		int m = pushes.size();
		
		// Push all values
		for (int i = 0; i < m; i++)
		{
			// Get push ID
			BCVariablePush pu = pushes.get(i);
			
			// {@squirreljme.error AN0t The value to push to the stack from
			// and input pop operation is not valid. (The index)}
			int dx = pu.popIndex();
			if (dx < 0 || dx >= n)
				throw new TerpException(core, TerpException.Issue.ILLEGAL_PUSH,
					String.format("AN0t %d", dx));
			
			// Direct copy
			stack[top++] = store[dx];
			
			// If wide, Clear the top portion
			if (pops.get(dx).isWide())
				stack[top++] = null;
		}
		
		// Store new top
		_top = top;
		
		// Next address
		return __op.address() + 1;
		*/
	}
}

