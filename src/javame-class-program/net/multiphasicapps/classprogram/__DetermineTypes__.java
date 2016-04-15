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
				case 21: __load(xop, CPVariableType.INTEGER); break;
				case 22: __load(xop, CPVariableType.LONG); break;
				case 23: __load(xop, CPVariableType.FLOAT); break;
				case 24: __load(xop, CPVariableType.DOUBLE); break;
				case 25: __load(xop, CPVariableType.OBJECT); break;
				
				case 26:
				case 27:
				case 28:
				case 29:
					__load_n(xop, opcode - 26, CPVariableType.INTEGER);
					break;
				
				case 30:
				case 31:
				case 32:
				case 33:
					__load_n(xop, opcode - 30, CPVariableType.LONG);
					break;
				
				case 34:
				case 35:
				case 36:
				case 37:
					__load_n(xop, opcode - 24, CPVariableType.FLOAT);
					break;
				
				case 38:
				case 39:
				case 40:
				case 41:
					__load_n(xop, opcode - 38, CPVariableType.DOUBLE);
					break;
					
				case 42:
				case 43:
				case 44:
				case 45:
					__load_n(xop, opcode - 42, CPVariableType.OBJECT);
					break;
				
				case 54: __store(xop, CPVariableType.INTEGER); break;
				case 55: __store(xop, CPVariableType.LONG); break;
				case 56: __store(xop, CPVariableType.FLOAT); break;
				case 57: __store(xop, CPVariableType.DOUBLE); break;
				case 58: __store(xop, CPVariableType.OBJECT); break;
				
				case 59:
				case 60:
				case 61:
				case 62:
					__store_n(xop, opcode - 59, CPVariableType.INTEGER);
					break;
				
				case 63:
				case 64:
				case 65:
				case 66:
					__store_n(xop, opcode - 63, CPVariableType.LONG);
					break;
				
				case 67:
				case 68:
				case 69:
				case 70:
					__store_n(xop, opcode - 67, CPVariableType.FLOAT);
					break;
				
				case 71:
				case 72:
				case 73:
				case 74:
					__store_n(xop, opcode - 71, CPVariableType.DOUBLE);
					break;
				
				case 75:
				case 76:
				case 77:
				case 78:
					__store_n(xop, opcode - 75, CPVariableType.OBJECT);
					break;
					
				case 89: __dup(xop); break;
				
					// Return, does nothing
				case 177: break;
				
				case 178: __getstatic(xop); break;
				case 179: __putstatic(xop); break;
				case 182:
				case 183: __invoke(xop, true); break;
				case 184: __invoke(xop, false); break;
				case 185: __invoke(xop, true); break;
				case 187: __new(xop); break;
				
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
				Object v = __vts[pi++];
				
				// End?
				if (v == null)
					break;
				
				throw new Error("TODO");
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
		catch (ClassCastException|IndexOutOfBoundsException e)
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
	 * Invokes a method.
	 *
	 * @param __op The input operation.
	 * @param __inst Instance method?*
	 * @since 2016/04/13
	 */
	private void __invoke(CPOp __op, boolean __inst)
	{
		// Read the method to invoke
		CFMethodReference ref = (CFMethodReference)__op.arguments().get(0);
		MethodSymbol desc = ref.nameAndType().getValue().asMethod();
		
		// Get argument count and any instance variables
		int argc = desc.argumentCount();
		int ivc = (__inst ? 1 : 0);
		FieldSymbol rv;
		int rvc = (((rv = desc.returnValue()) != null) ? 1 : 0);
		
		// Setup operation array
		Object[] ops = new Object[2 + argc + ivc + rvc];
		int wp = 0;
		
		// There are no locals
		ops[wp++] = null;
		
		// Pop method arguments from last to first
		for (int i = argc - 1; i >= 0; i--)
			ops[wp++] = CPVariableType.bySymbol(desc.get(i));
		
		// If an instance, pop an extra object
		if (__inst)
			ops[wp++] = CPVariableType.OBJECT;
		
		// Spacer null
		ops[wp++] = null;
		
		// Push the return type
		if (rv != null)
			ops[wp++] = CPVariableType.bySymbol(rv);
		
		// Perform operation
		operate(__op, ops);
	}
	
	/**
	 * Load variable from locals and push it onto the stack.
	 *
	 * @param __op The input operation.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	private void __load(CPOp __op, CPVariableType __t)
	{
		__load_n(__op, ((Number)__op.arguments().get(0)).intValue(), __t);
	}
	
	/**
	 * Load variable from locals and push it onto the stack.
	 *
	 * @param __op The input operation.
	 * @param __dx The index.
	 * @param __t The type of value to load.
	 * @since 2016/04/12
	 */
	private void __load_n(CPOp __op, int __dx, CPVariableType __t)
	{
		// Load it onto the stack
		operate(__op, -__dx, __t, null, null, __t);
	}
	
	/**
	 * Load variable from locals and push it onto the stack (wide).
	 *
	 * @param __op The input operation.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	private void __load_w(CPOp __op, CPVariableType __t)
	{
		__load_n(__op, ((Number)__op.arguments().get(0)).intValue(), __t);
	}
	
	/**
	 * Duplicates the top-most stack item.
	 *
	 * @param __op The current operation.
	 * @param __xin Input variables.
	 * @since 2016/04/12
	 */
	private void __dup(CPOp __op)
	{
		// Handle
		CPVariables xin = __op.variables();
		
		// Get the topmost variable
		int top;
		CPVariables.Slot at = xin.get((top = xin.getStackTop()) - 1);
		
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
	private void __getstatic(CPOp __op)
	{
		operate(__op, null, null, CPVariableType.bySymbol(
			((CFFieldReference)__op.arguments().get(0)).
			nameAndType().getValue().asField()));
	}
	
	/**
	 * Calculates the new operation.
	 *
	 * @param __op The current operation.
	 * @param __xin Input variables.
	 * @since 2016/04/11
	 */
	private void __new(CPOp __op)
	{
		// Just add an element to the stack
		operate(__op, null, null, CPVariableType.OBJECT);
	}
	
	/**
	 * Stores a value into a static field.
	 *
	 * @param __op The operation.
	 * @param __xin The input variables.
	 * @since 2016/04/15
	 */
	private void __putstatic(CPOp __op)
	{
		operate(__op, null, CPVariableType.bySymbol(
			((CFFieldReference)__op.arguments().get(0)).
			nameAndType().getValue().asField()), null);
	}
	
	/**
	 * Store variable from the stack and place it in a local (narrow).
	 *
	 * @param __op The input operation.
	 * @param __t The type of value to store.
	 * @since 2016/04/14
	 */
	private void __store(CPOp __op, CPVariableType __t)
	{
		__store_n(__op, ((Number)__op.arguments().get(0)).intValue(), __t);
	}
	
	/**
	 * Store variable from the stack and place it in a local.
	 *
	 * @param __op The input operation.
	 * @param __dx The index.
	 * @param __t The type of value to store.
	 * @since 2016/04/14
	 */
	private void __store_n(CPOp __op, int __dx, CPVariableType __t)
	{
		operate(__op, __dx, __t, null, __t, null);
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
		__store_n(__op, ((Number)__op.arguments().get(0)).intValue(), __t);
	}
}

