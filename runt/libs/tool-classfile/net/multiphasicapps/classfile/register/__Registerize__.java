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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodReference;
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
	
	/** The state of the locals and stack. */
	protected final __StackState__ state;
	
	/** The stack map table. */
	protected final StackMapTable stackmap;
	
	/** Used to build register codes. */
	protected final RegisterCodeBuilder codebuilder =
		new RegisterCodeBuilder();
	
	/** Exception tracker. */
	protected final __ExceptionTracker__ exceptiontracker;
	
	/** The instruction throws an exception, it must be checked. */
	private boolean _exceptioncheck;
	
	/** Exception handler combinations to generate. */
	private final List<__ExceptionCombo__> _usedexceptions =
		new ArrayList<>();
	
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
		this.state = new __StackState__(__bc.maxLocals(), __bc.maxStack());
		this.exceptiontracker = new __ExceptionTracker__(__bc);
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
		__StackState__ state = this.state;
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// Process every instruction
		for (Instruction inst : bytecode)
		{
			// Debug
			todo.DEBUG.note("Xlate %s", inst);
			
			// Add label to refer to this instruction in Java terms
			codebuilder.label("java", inst.address());
			
			// Clear the exception check since not every instruction will
			// generate an exception, this will reduce the code size greatly
			this._exceptioncheck = false;
			
			// If there is a defined stack map table state (this will be for
			// any kind of branch or exception handler), load that so it can
			// be worked from
			int pcaddr;
			StackMapTableState smts = stackmap.get((pcaddr = inst.address()));
			if (smts != null)
				state.fromState(smts);
			
			// Process instructions
			this.__process(inst);
			
			// If an exception is thrown it needs to be handled accordingly
			// This means uncounting anything on the stack, reading the
			// exception register value, then jumping to the exception handler
			// for this instruction
			if (this._exceptioncheck)
			{
				// Create jumping label for this exception
				RegisterCodeLabel ehlab = new RegisterCodeLabel("exception",
					this.__exceptionTrack(pcaddr));
				
				// Just create a jump here
				codebuilder.add(
					RegisterOperationType.JUMP_ON_EXCEPTION, ehlab);
			}
		}
		
		// If we need to generate exception tables, do it now
		List<__ExceptionCombo__> usedexceptions = this._usedexceptions;
		if (!usedexceptions.isEmpty())
			for (int i = 0, n = usedexceptions.size(); i < n; i++)
				this.__exceptionGenerate(usedexceptions.get(i), i);
		
		// Build the final code table
		return codebuilder.build();
	}
	
	/**
	 * Generates exception handler code for the given index.
	 *
	 * @param __ec The combo to generate for.
	 * @param __edx The index of the used exception table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	private final void __exceptionGenerate(__ExceptionCombo__ __ec, int __edx)
		throws NullPointerException
	{
		if (__ec == null)
			throw new NullPointerException("NARG");
		
		RegisterCodeBuilder codebuilder = this.codebuilder;
		__ObjectPositionsSnapshot__ ops = __ec.ops;
		ExceptionHandlerTable ehtable = __ec.table;
		
		// Mark the current position as the handler, so other parts of the
		// code can jump here
		codebuilder.label("exception", __edx);
		
		// Un-count all stack entries
		int stackstart = ops.stackStart();
		for (int i = stackstart, n = ops.size(); i < n; i++)
			codebuilder.add(RegisterOperationType.UNCOUNT, ops.get(i));
		
		// For each exception type, perform a check and a jump to the target
		for (ExceptionHandler eh : ehtable)
			codebuilder.add(RegisterOperationType.EXCEPTION_CLASS_JUMP,
				eh.type(), new RegisterCodeLabel("java", eh.handlerAddress()),
				this.state.stackBaseRegister());
		
		// If this point ever gets reached in code then this will mean that
		// there are no handlers for the exception, uncount any used registers
		for (int i = 0; i < stackstart; i++)
			codebuilder.add(RegisterOperationType.UNCOUNT, ops.get(i));
		
		// Then generate a return, the exception register will remain flagged
		// and containing an exception
		codebuilder.add(RegisterOperationType.RETURN);
	}
	
	/**
	 * Handles the process of exceptions, this just defers the generation
	 * of exception data until the end.
	 *
	 * @param __pc The current PC address.
	 * @return The exception combo index.
	 * @since 2019/03/22
	 */
	private final int __exceptionTrack(int __pc)
	{
		// Create combo for the object and exception data
		__ExceptionCombo__ ec = this.exceptiontracker.createCombo(
			this.state.objectSnapshot(), __pc);
		
		// If this combo is already in the table, do not add it
		List<__ExceptionCombo__> usedexceptions = this._usedexceptions;
		int dx = usedexceptions.indexOf(ec);
		if (dx >= 0)
			return dx;
		
		// Otherwise just add it
		dx = usedexceptions.size();
		usedexceptions.add(ec);
		return dx;
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
			case InstructionIndex.ALOAD:
				this.__runALoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
				this.__runALoad(op - InstructionIndex.ALOAD_0);
				break;
			
			case InstructionIndex.INVOKESPECIAL:
				this.__runInvoke(InvokeType.SPECIAL,
					__i.<MethodReference>argument(0, MethodReference.class));
				break;
			
			case InstructionIndex.LDC:
				this.__runLdc(__i.<ConstantValue>argument(
					0, ConstantValue.class));
				break;
			
			case InstructionIndex.RETURN:
				this.__runReturn(null);
				break;
			
			default:
				throw new todo.TODO(__i.toString());
		}
	}
	
	/**
	 * Loads single reference from a local to the stack.
	 *
	 * @param __l The reference to load.
	 * @since 2019/03/14
	 */
	private final void __runALoad(int __l)
	{
		__StackState__ state = this.state;
		
		// Load from local and push to the stack
		__StackResult__ src = state.localGet(__l);
		__StackResult__ dest = state.stackPush(src.type);
		
		// Add instruction
		this.codebuilder.add(RegisterOperationType.NARROW_COPY_AND_COUNT_DEST,
			src.register, dest.register);
	}
	
	/**
	 * Handles invocation of other methods.
	 *
	 * @param __t The type of invocation to perform.
	 * @param __r The method to invoke.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/20
	 */
	private final void __runInvoke(InvokeType __t, MethodReference __r)
		throws NullPointerException
	{
		if (__t == null || __r == null)
			throw new NullPointerException("NARG");
		
		// The invoked method may throw an exception
		this._exceptioncheck = true;
		
		// The old top of the stack is used to determine how many arguments
		// to forward (into the locals)
		__StackState__ state = this.state;
		int oldtop = state.stackTopRegister();
		
		// Pop all entries off the stack, note any entries which are references
		// that need to be uncounted after the call
		List<Integer> uncount = new ArrayList<>();
		for (JavaType js : __r.handle().javaStack(__t.hasInstance()))
		{
			int pr = state.stackPop().register;
			
			// Uncount reference later?
			if (js.isObject())
				uncount.add(pr);
		}
		
		// The base of the stack is the last popped register
		int newbase = state.stackTopRegister();
		
		// Generate the call, pass the base register and the number of
		// registers to pass to the target method
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.INVOKE_FROM_CONSTANT_POOL,
			new InvokedMethod(__t, __r.handle()), newbase, oldtop - newbase);
		
		// For any references that are used, uncount the positions
		for (Integer i : uncount)
			codebuilder.add(RegisterOperationType.UNCOUNT, i);
		
		// If there is a return result, read it into the register at the top
		// of the stack
		FieldDescriptor rvfd = __r.memberType().returnValue();
		if (rvfd != null)
			throw new todo.TODO();
	}
	
	/**
	 * Load of constant value.
	 *
	 * @param __v The value to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/17
	 */
	private final void __runLdc(ConstantValue __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Get push properties
		JavaType jt = __v.type().javaType();
		
		// Push to the stack
		__StackResult__ dest = this.state.stackPush(jt);
		
		// Generate instruction
		RegisterCodeBuilder codebuilder = this.codebuilder;
		switch (__v.type())
		{
			case INTEGER:
			case FLOAT:
				codebuilder.add(RegisterOperationType.NARROW_CONST,
					__v.boxedValue(), dest.register);
				break;
			
			case LONG:
			case DOUBLE:
				codebuilder.add(RegisterOperationType.WIDE_CONST,
					__v.boxedValue(), dest.register);
				break;
			
			case STRING:
			case CLASS:
				throw new todo.TODO();
			
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Handles method return.
	 *
	 * @param __rt The return type, {@code null} indicates a void return.
	 * @since 2019/03/22
	 */
	private final void __runReturn(JavaType __rt)
	{
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// If we are returning a value, we need to store it into the return
		// register
		if (__rt != null)
		{
			throw new todo.TODO();
		}
		
		// All returns are plain due to the fact that return address registers
		// are used
		codebuilder.add(RegisterOperationType.RETURN);
	}
}

