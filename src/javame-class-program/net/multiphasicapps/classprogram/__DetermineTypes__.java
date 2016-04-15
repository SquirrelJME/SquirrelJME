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
				
				case 54: __store(xop, xin, CPVariableType.INTEGER); break;
				case 55: __store(xop, xin, CPVariableType.LONG); break;
				case 56: __store(xop, xin, CPVariableType.FLOAT); break;
				case 57: __store(xop, xin, CPVariableType.DOUBLE); break;
				case 58: __store(xop, xin, CPVariableType.OBJECT); break;
				
				case 59:
				case 60:
				case 61:
				case 62:
					__store_n(xop, xin, opcode - 59, CPVariableType.INTEGER);
					break;
				
				case 63:
				case 64:
				case 65:
				case 66:
					__store_n(xop, xin, opcode - 63, CPVariableType.LONG);
					break;
				
				case 67:
				case 68:
				case 69:
				case 70:
					__store_n(xop, xin, opcode - 67, CPVariableType.FLOAT);
					break;
				
				case 71:
				case 72:
				case 73:
				case 74:
					__store_n(xop, xin, opcode - 71, CPVariableType.DOUBLE);
					break;
				
				case 75:
				case 76:
				case 77:
				case 78:
					__store_n(xop, xin, opcode - 75, CPVariableType.OBJECT);
					break;
					
				case 89: __dup(xop, xin); break;
				
					// Return, does nothing
				case 177: break;
				
				case 178: __getstatic(xop, xin); break;
				case 179: __putstatic(xop, xin); break;
				case 182:
				case 183: __invoke(xop, xin, true); break;
				case 184: __invoke(xop, xin, false); break;
				case 185: __invoke(xop, xin, true); break;
				case 187: __new(xop, xin); break;
				
				case 50197: __load_w(xop, xin, CPVariableType.INTEGER); break;
				case 50198: __load_w(xop, xin, CPVariableType.LONG); break;
				case 50199: __load_w(xop, xin, CPVariableType.FLOAT); break;
				case 50200: __load_w(xop, xin, CPVariableType.DOUBLE); break;
				case 50201: __load_w(xop, xin, CPVariableType.OBJECT); break;
				
				case 50230: __store_w(xop, xin, CPVariableType.INTEGER); break;
				case 50231: __store_w(xop, xin, CPVariableType.LONG); break;
				case 50232: __store_w(xop, xin, CPVariableType.FLOAT); break;
				case 50233: __store_w(xop, xin, CPVariableType.DOUBLE); break;
				case 50234: __store_w(xop, xin, CPVariableType.OBJECT); break;
				
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
	 * {@link CPVariableType} type pairs; {@code null} specifies the end of
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
				
				throw new Error("TODO");
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
			
			// Set the size of the stack
			int pushcount = pin - pi;
			set(__op, Integer.MIN_VALUE, newt + pushcount, null);
			
			// Push values to the stack
			for (; pi < pin;)
			{
				Object v = __vts[pi++];
				
				throw new Error("TODO");
			}
		}
		
		// Incorrect input type; Out of bounds
		catch (ClassCastException|IndexOutOfBoundsException e)
		{
			// {@squirreljme.error CP1l Operation exceeded bounds while it
			// was being performed or the input type is incorrect.
			// (The operation address; The operations to be performed)}
			throw new CPProgramException(String.format("CP1l %d %d",
				__xop.address(), Arrays.<Object>asList(__vts)), e);
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
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __inst Instance method?*
	 * @since 2016/04/13
	 */
	private void __invoke(CPOp __xop, CPVariables __xin, boolean __inst)
	{
		// Read the method to invoke
		CFMethodReference ref = (CFMethodReference)__xop.arguments().get(0);
		
		// Could fail
		try
		{
			// Pop from last to first
			MethodSymbol desc = ref.nameAndType().getValue().asMethod();
			int n = desc.argumentCount();
			int j = 1;
			int basetop = __xin.getStackTop(); 
			for (int i = n - 1; i >= 0; i--)
			{
				// Get argument here
				FieldSymbol arg = desc.get(i);
				CPVariableType type = CPVariableType.bySymbol(arg);
			
				// If wide, pop a top
				if (type.isWide())
				{
					// Must be a wide at the top
					int top;
					CPVariables.Slot sl = __xin.get(
						(top = __xin.getStackTop()) - j);
				
					// {@squirreljme CP1e Excepted the stack to contain the top
					// of a long/double. (The current operation address; The
					// index of this slot; The type that it was)}
					CPVariableType was = sl.type();
					if (was != CPVariableType.TOP)
						throw new CPProgramException(String.format(
							"CP1e %d %d %s", __xop.address(), sl.index, was));
					
					// Reduce the stack
					j++;
				}
				
				// Get the current top
				int top;
				CPVariables.Slot sl = __xin.get(
					(top = __xin.getStackTop()) - j);
				
				// {@squirreljme.error CP1g Expected the top of the stack to
				// be of a specific type, however it was not that type. (The
				// operation address; The index of this slot; The type that it
				// was; The type it should have been)}
				CPVariableType was = sl.type();
				if (was != type)
					throw new CPProgramException(String.format(
						"CP1g %d %d %s %s", __xop.address(), sl.index, was,
						type));
				
				// Reduce the stack
				j++;
			}
			
			// If an instance, pop an object
			if (__inst)
			{
				// Pop the top
				int top;
				CPVariables.Slot sl = __xin.get(
					(top = __xin.getStackTop()) - j);
				
				// {@squirreljme.error CP1h Expected the last entry to be
				// popped before an instance method call to be an object
				// which contains the instance of the object to invoke the
				// method on. (The operation address; The index of this slot;
				// The type that it was)}
				CPVariableType was = sl.type();
				if (was != CPVariableType.OBJECT)
					throw new CPProgramException(String.format(
						"CP1h %d %d %s", __xop.address(), sl.index, was));
			}
			
			// No return value?
			FieldSymbol rvt = desc.returnValue();
			if (rvt == null)
			{
				// Set the final stack size
				set(__xop, Integer.MIN_VALUE, basetop - j, null);
			}
			
			// Need to add the return value back in
			else
			{
				// Get the type of return value used
				CPVariableType rt = CPVariableType.bySymbol(rvt);
				
				// Reduce the number of eaten stack items.
				int bottom = basetop - j;
				
				// Set wide type 
				if (rt.isWide())
				{
					// Size
					set(__xop, Integer.MIN_VALUE, bottom + 2, null);
					
					// Values
					set(__xop, bottom, Integer.MIN_VALUE, rt);
					set(__xop, bottom + 1, Integer.MIN_VALUE,
						CPVariableType.TOP);
				}
				
				// Narrow otherwise
				else
				{
					// Size
					set(__xop, Integer.MIN_VALUE, bottom + 1, null);
					
					// Value
					set(__xop, bottom, Integer.MIN_VALUE, rt);
				}
			}
		}
		
		// Out of bounds
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error CP1f Out of bounds stack access when
			// popping and pushing on the stack for an method invocation. (The
			// current operation address)}
			throw new CPProgramException(String.format("CP1f %d",
				__xop.address()), e);
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
		__load_n(__xop, __xin, ((Number)__xop.arguments().get(0)).intValue(),
			__t);
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
		__load_n(__xop, __xin, ((Number)__xop.arguments().get(0)).intValue(),
			__t);
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
		CFFieldReference ref = (CFFieldReference)__op.arguments().get(0);
		
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
	
	/**
	 * Stores a value into a static field.
	 *
	 * @param __op The operation.
	 * @param __xin The input variables.
	 * @since 2016/04/15
	 */
	private void __putstatic(CPOp __xop, CPVariables __xin)
	{
		// Read the field value which is read
		CFFieldReference ref = (CFFieldReference)__xop.arguments().get(0);
		
		// Get the variable associated for the given field
		CPVariableType type = CPVariableType.bySymbol(
			ref.nameAndType().getValue().asField());
			
		System.err.printf("DEBUG -- Put static %s%n", ref);
		
		// Get stack top
		int top;
		int newt = (top = __xin.getStackTop());
		
		// Wide value?
		if (type.isWide())
		{
			// {@squirreljme.error CP1m Expected the type TOP to be on the
			// stack for a put of a static field, however it was not.
			// (The operation address; The slot index; The type that it was)}
			CPVariables.Slot sl = __xin.get(newt - 1);
			CPVariableType ty = sl.type();
			if (ty != CPVariableType.TOP)
				throw new CPProgramException(String.format("CP1m %d %d %s",
					__xop.address(), newt - 1, ty));
			
			// Remove item
			newt--;	
		}
		
		// {@squirreljme.error CP1n Expected the given type when storing a
		// value into a static field. (The operation address; The slot index;
		// The type which was expected; The type that it was)}
		CPVariables.Slot sl = __xin.get(newt - 1);
		CPVariableType ty = sl.type();
		if (ty != type)
			throw new CPProgramException(String.format("CP1n %d %d %s %s",
				__xop.address(), newt - 1, type, ty));
		
		// Remove it
		newt--;
		
		// Set new top
		set(__xop, Integer.MIN_VALUE, newt, null);
	}
	
	/**
	 * Store variable from the stack and place it in a local (narrow).
	 *
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __t The type of value to store.
	 * @since 2016/04/14
	 */
	private void __store(CPOp __xop, CPVariables __xin, CPVariableType __t)
	{
		__store_n(__xop, __xin, ((Number)__xop.arguments().get(0)).intValue(),
			__t);
	}
	/**
	 * Store variable from the stack and place it in a local.
	 *
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __dx The index.
	 * @param __t The type of value to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/14
	 */
	private void __store_n(CPOp __xop, CPVariables __xin, int __dx,
		CPVariableType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			// {@squirreljme.error CP1i Out of bounds index.}
			if (__dx >= stackbase)
				throw new IndexOutOfBoundsException("CP1i");
			
			// If wide, pop TOP
			int top = __xin.getStackTop();
			int newt;
			if (__t.isWide())
			{
				// New stack top
				newt = top - 2;
				
				// Set both variables
				set(__xop, __dx, Integer.MIN_VALUE, __t);
				set(__xop, __dx + 1, Integer.MIN_VALUE, CPVariableType.TOP);
				
				// {@squirreljme.error CP1j Expected the top of the stack to
				// contain TOP for long/double. (The operation address; The
				// slot index; The type the top of the stack contained)}
				CPVariables.Slot sl = __xin.get(newt + 1);
				CPVariableType was = sl.type();
				if (was != CPVariableType.TOP)
					throw new CPProgramException(String.format("CP1j %d %d %s",
						__xop.address(), __dx + 1, was));
			}
			
			// Narrow
			else
			{
				// New stack top
				newt = top - 1;
				
				// Set just one
				set(__xop, __dx, Integer.MIN_VALUE, __t);
			}
			
			// Set the new stack top
			set(__xop, Integer.MIN_VALUE, newt, null);
			
			// {@squirreljme.error CP1k Expected to pop a value of a specific
			// type for storage into a local variable. (The operation address;
			// The slot index; The expected type; The type it actually was)
			CPVariables.Slot sl = __xin.get(newt);
			CPVariableType was = sl.type();
			if (was != __t)
				throw new CPProgramException(String.format("CP1k %d %d %s %s",
					__xop.address(), __dx, __t, was));
		}
		
		// Out of bounds
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error CP1l Attempt to read write variable from a
			// slot which is not within bounds. (The operation address;
			// The slot index)}
			throw new CPProgramException(String.format("CP1l %d %d",
				__xop.address(), __dx), e);
		}
	}
	
	/**
	 * Store variable from the stack and place it in a local (wide).
	 *
	 * @param __xop The input operation.
	 * @param __xin The input variables.
	 * @param __t The type of value to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/14
	 */
	private void __store_w(CPOp __xop, CPVariables __xin, CPVariableType __t)
	{
		__store_n(__xop, __xin, ((Number)__xop.arguments().get(0)).intValue(),
			__t);
	}
}

