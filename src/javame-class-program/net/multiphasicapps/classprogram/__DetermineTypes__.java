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

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.classfile.CFConstantEntry;
import net.multiphasicapps.classfile.CFConstantPool;
import net.multiphasicapps.classfile.CFFieldReference;
import net.multiphasicapps.classfile.CFMethodReference;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

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
			
			// Lookup the determiner
			__VMWorkers__.__Determiner__ det = CPOp._VMWORKERS.__determine(
				opcode);
			
			// Call it
			try
			{
				det.determine(this, xop);
			}
			
			// Unknown operation
			catch (__VMWorkers__.__UnknownOp__ e)
			{
				// {@squirreljme.error CP1f Cannot determine the types for the
				// given opcode at the specified address. (The operation
				// address; The opcode)}
				throw new CPProgramException(String.format("CP1f %d %d",
					xop.address(), opcode), e);
			}
			
			/*switch (opcode)
			{
				
				case 172: __return(xop, CPVariableType.INTEGER); break;
				case 173: __return(xop, CPVariableType.LONG); break;
				case 174: __return(xop, CPVariableType.FLOAT); break;
				case 175: __return(xop, CPVariableType.DOUBLE); break;
				
				case 50197: __load_w(xop, CPVariableType.INTEGER); break;
				case 50198: __load_w(xop, CPVariableType.LONG); break;
				case 50199: __load_w(xop, CPVariableType.FLOAT); break;
				case 50200: __load_w(xop, CPVariableType.DOUBLE); break;
				case 50201: __load_w(xop, CPVariableType.OBJECT); break;
				
				case 50230: __store_w(xop, CPVariableType.INTEGER); break;
				case 50231: __store_w(xop, CPVariableType.LONG); break;
				case 50232: __store_w(xop, CPVariableType.FLOAT); break;
				case 50233: __store_w(xop, CPVariableType.DOUBLE); break;
				case 50234: __store_w(xop, CPVariableType.OBJECT); break;
				
					// {@squirreljme.error CP0u Cannot calculate the types for
					// the given opcode because it is unknown. (The program
					// address; The opcode)}
				default:
					throw new CPProgramException(String.format("CP0u %d %d",
						xop.address(), opcode));
			}*/
			
			// If this operation handles exceptions then it potentially needs
			// to derive variable state from the source operations.
			for (CPOp e : xop.exceptionsHandled())
				throw new Error("TODO");
			
			// Offer jump targets to the queue
			for (CPOp jt : xop.jumpTargets())
				queue.offerLast(jt);
		}
	}
	
	/**
	 * Performs stack popping and pushing along with setting of local variable
	 * types.
	 *
	 * Popping is done in the input sequential order, thus to pop INT and
	 * OBJECT the input arguments must be OBJECT INT.
	 *
	 * @param __op The current operation.
	 * @param __vts The first set of inputs are {@link Integer} and
	 * {@link CPVariableType} type pairs, negative values are checked reads
	 * and positive values are stores; {@code null} specifies the end of
	 * local variable setting; the types to be popped in sequential order;
	 * {@code null}; The types to be pushed in sequential order.
	 * @throws CPProgramException If there are too little or too many nulls,
	 * or there is an invalid current state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/15
	 */
	public void operate(CPOp __op, Object... __vts)
		throws CPProgramException, NullPointerException
	{
		// Check
		if (__vts == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			// Get variables
			CPVariables vars = __op.variables();
			int top = vars.getStackTop();
			int newt = top;
			
			// Input parse index
			int pi = 0;
			final int pin = __vts.length;
			
			// Handle local variables
			for (;;)
			{
				Object v = __vts[pi++];
				
				// End?
				if (v == null)
					break;
				
				// Get the local index and type
				int dx = ((Number)v).intValue();
				CPVariableType type = (CPVariableType)__vts[pi++];
				
				// Is this a load or store operation?
				boolean load = (dx < 0);
				if (load)
					dx = -dx;
				
				// {@squirreljme.error CP19 Local variable index is out of
				// bounds. (The current operation address; The slot index)}
				if (dx >= (program.maxLocals() - (type.isWide() ? 1 : 0)))
					throw new CPProgramException(String.format("CP19 %d %d",
						__op.address(), dx));
				
				// Loading?
				if (load)
				{
					// {@squirreljme.error CP1a The local variable is not of
					// the correct type. (The operation address; The slot
					// index; The expected type; The type that it was)}
					CPVariables.Slot sl = vars.get(dx);
					CPVariableType was = sl.type();
					if (was != type)
						throw new CPProgramException(String.format(
							"CP1a %d %d %s %s", __op.address(), dx, type,
							was));
					
					// Check top
					if (type.isWide())
					{
						// {@squirreljme.error CP1b Expected TOP to follow
						// LONG/DOUBLE when loading local variable.
						// (The operation address; The slot index; The type
						// that it was)}
						sl = vars.get(dx + 1);
						was = sl.type();
						if (was != CPVariableType.TOP)
							throw new CPProgramException(String.format(
								"CP1b %d %d %s", __op.address(), dx, was));
					}
				}
				
				// Storing?
				else
				{
					// Single
					set(__op, dx, Integer.MIN_VALUE, type);
					
					// Wide?
					if (type.isWide())
						set(__op, dx + 1, Integer.MIN_VALUE,
							CPVariableType.TOP);
				}
			}
			
			// Pop values from the stack
			for (;;)
			{
				CPVariableType v = (CPVariableType)__vts[pi++];
				
				// End?
				if (v == null)
					break;
				
				// {@squirreljme.error CP1c Stack underflow popping from the
				// stack. (The operation address)}
				if ((newt - 1) < stackbase)
					throw new CPProgramException(String.format("CP1c %d",
						__op.address()));
				
				// Handle wide values
				if (v.isWide())
				{
					// {@squirreljme.error CP1e Expected TOP to be at the top
					// of the stack. (The operation address; The slot index;
					// The type it was)}
					int dx;
					CPVariables.Slot sl = vars.get(dx = (--newt));
					CPVariableType was = sl.type();
					if (was != CPVariableType.TOP)
						throw new CPProgramException(String.format(
							"CP1e %d %d %s", __op.address(), dx, was));
				}
				
				// {@squirreljme.error CP1d Popped the incorrect type from the
				// stack. (The operation address; The slot index;
				// The expected type; The type it was)}
				int dx;
				CPVariables.Slot sl = vars.get(dx = (--newt));
				CPVariableType was = sl.type();
				if (was != v)
					throw new CPProgramException(String.format(
						"CP1d %d %d %s %s", __op.address(), dx, v, was));
			}
			
			// Determine where the top of the stack will be
			int pushcount = 0;
			for (int vpi = pi; vpi < pin; vpi++)
			{
				// Get it
				CPVariableType v = (CPVariableType)__vts[vpi++];
				
				// Wide gets two
				if (v.isWide())
					pushcount += 2;
				else
					pushcount++;
			}
			
			// Set the top of the stack
			set(__op, Integer.MIN_VALUE, newt + pushcount, null);
			
			// Push values to the stack
			for (; pi < pin;)
			{
				// Get it
				CPVariableType v = (CPVariableType)__vts[pi++];
				
				// Place onto the stack
				set(__op, newt++, Integer.MIN_VALUE, v);
				
				// If wide, add a top
				if (v.isWide())
					set(__op, newt++, Integer.MIN_VALUE, CPVariableType.TOP);
			}
		}
		
		// Incorrect input type; Out of bounds
		catch (ClassCastException|IndexOutOfBoundsException|
			NullPointerException e)
		{
			// {@squirreljme.error CP1l Operation exceeded bounds while it
			// was being performed or the input type is incorrect.
			// (The operation address; The operations to be performed)}
			throw new CPProgramException(String.format("CP1l %d %d",
				__op.address(), Arrays.<Object>asList(__vts)), e);
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
			if (__sl != Integer.MIN_VALUE)
			{
				CPVariables.Slot sl;
				try
				{
					sl = tvars.get(__sl);
				}
			
				// Out of bounds read of slot
				catch (IndexOutOfBoundsException e)
				{
					// {@squirreljme.error CP13 Attempt to access a slot which
					// is not within the program bounds. (The slot index)}
					throw new CPProgramException(String.format("CP13 %s",
						__sl), e);
				}
			
				// Setting a type?
				if (__vt != null)
					sl.__checkedSetType(__vt);
			}
		}
	}
	
	/**
	 * Store variable from the stack and place it in a local (wide).
	 *
	 * @param __op The input operation.
	 * @param __t The type of value to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/14
	 */
	private void __store_w(CPOp __op, CPVariableType __t)
	{
		__Determine64To79__.__store_n(this, __op,
			((Number)__op.arguments().get(0)).intValue(), __t);
	}
}

