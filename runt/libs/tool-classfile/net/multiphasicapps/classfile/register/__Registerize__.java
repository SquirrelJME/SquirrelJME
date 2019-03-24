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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.PrimitiveType;
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
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** The instruction throws an exception, it must be checked. */
	private boolean _exceptioncheck;
	
	/** Exception handler combinations to generate. */
	private final List<__ExceptionCombo__> _usedexceptions =
		new ArrayList<>();
	
	/** Object position maps to return label points. */
	private final Map<__ObjectPositionsSnapshot__, RegisterCodeLabel>
		_returns =
		new LinkedHashMap<>();
	
	/** Next returning index? */
	private int _nextreturndx;
	
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
		this.defaultfieldaccesstime = ((__bc.isInstanceInitializer() ||
			__bc.isStaticInitializer()) ? FieldAccessTime.INITIALIZER :
			FieldAccessTime.NORMAL);
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
		
		// Scan the code to see if basic stack caching can be performed
		this.__checkBasicStackCache();
		
		// Process every instruction
		for (Instruction inst : bytecode)
		{
			// Debug
			todo.DEBUG.note("Xlate %s", inst);
			
			// Set source line for this instruction for debugging purposes
			int addr = inst.address();
			codebuilder.setSourceLine(bytecode.lineOfAddress(addr));
			
			// Add label to refer to this instruction in Java terms
			codebuilder.label("java", addr);
			
			// Clear the exception check since not every instruction will
			// generate an exception, this will reduce the code size greatly
			this._exceptioncheck = false;
			
			// If there is a defined stack map table state (this will be for
			// any kind of branch or exception handler), load that so it can
			// be worked from
			int pcaddr;
			StackMapTableState smts = stackmap.get((pcaddr = addr));
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
		
		// Invalidate source lines for the exception table
		codebuilder.setSourceLine(-1);
		
		// If we need to generate exception tables, do it now
		List<__ExceptionCombo__> usedexceptions = this._usedexceptions;
		if (!usedexceptions.isEmpty())
			for (int i = 0, n = usedexceptions.size(); i < n; i++)
				this.__exceptionGenerate(usedexceptions.get(i), i);
		
		// Build the final code table
		return codebuilder.build();
	}
	
	/**
	 * Goes through all of the instructions and flags any local variables which
	 * are written to. This is a quick and simple way to determine if some
	 * pointless copies can be removed.
	 *
	 * @since 2019/03/22
	 */
	private final void __checkBasicStackCache()
	{
		__StackState__ state = this.state;
		
		// Set initial entry state, so we know which locals are actually
		// touchable
		state.fromState(this.stackmap.get(0));
		
		// Go through every instruction to find ones which touch locals
		for (Instruction inst : this.bytecode)
		{
			// Anything which is wide hits the adjacent local as well
			boolean wide = false;
			
			// Only specific instructions will do so
			int hit, op;
			switch ((op = inst.operation()))
			{
				case InstructionIndex.ASTORE:
				case InstructionIndex.WIDE_ASTORE:
					hit = inst.intArgument(0);
					break;
				
				case InstructionIndex.ASTORE_0:
				case InstructionIndex.ASTORE_1:
				case InstructionIndex.ASTORE_2:
				case InstructionIndex.ASTORE_3:
					hit = op - InstructionIndex.ASTORE_0;
					break;
				
				case InstructionIndex.DSTORE:
				case InstructionIndex.WIDE_DSTORE:
					hit = inst.intArgument(0);
					wide = true;
					break;
				
				case InstructionIndex.DSTORE_0:
				case InstructionIndex.DSTORE_1:
				case InstructionIndex.DSTORE_2:
				case InstructionIndex.DSTORE_3:
					hit = op = InstructionIndex.DSTORE_0;
					wide = true;
					break;
				
				case InstructionIndex.FSTORE:
				case InstructionIndex.WIDE_FSTORE:
					hit = inst.intArgument(0);
					break;
				
				case InstructionIndex.FSTORE_0:
				case InstructionIndex.FSTORE_1:
				case InstructionIndex.FSTORE_2:
				case InstructionIndex.FSTORE_3:
					hit = op = InstructionIndex.FSTORE_0;
					break;
				
				case InstructionIndex.IINC:
				case InstructionIndex.WIDE_IINC:
					hit = inst.intArgument(0);
					break;
				
				case InstructionIndex.ISTORE:
				case InstructionIndex.WIDE_ISTORE:
					hit = inst.intArgument(0);
					break;
				
				case InstructionIndex.ISTORE_0:
				case InstructionIndex.ISTORE_1:
				case InstructionIndex.ISTORE_2:
				case InstructionIndex.ISTORE_3:
					hit = op = InstructionIndex.ISTORE_0;
					break;
				
				case InstructionIndex.LSTORE:
				case InstructionIndex.WIDE_LSTORE:
					hit = inst.intArgument(0);
					wide = true;
					break;
				
				case InstructionIndex.LSTORE_0:
				case InstructionIndex.LSTORE_1:
				case InstructionIndex.LSTORE_2:
				case InstructionIndex.LSTORE_3:
					hit = op = InstructionIndex.LSTORE_0;
					wide = true;
					break;
				
				default:
					continue;
			}
			
			// Set local as being written to, handle wides as well
			state.localSlot(hit)._written = true;
			if (wide)
				state.localSlot(hit + 1)._written = true;
		}
		
		// Debug
		todo.DEBUG.note("QuickCache: %s", state);
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
		
		// If the exception handler table is empty, we are just going to
		// go up the stack anyway, so there is no point in generating our
		// own handler!
		if (ehtable.isEmpty())
		{
			// If there was already a return point which represents how we
			// would uncount and return, then just make the exception point
			// at this jump point. So when the labels are resolved no jumps
			// are generated, the JUMP_ON_EXCEPTION will just point to one
			// of the return points
			Map<__ObjectPositionsSnapshot__, RegisterCodeLabel> rs =
				this._returns;
			RegisterCodeLabel rcl = rs.get(ops);
			if (rcl != null)
			{
				// Just point this exception to that return location
				codebuilder.label("exception", __edx,
					codebuilder.labelTarget(rcl));
				return;
			}
			
			// Label here has usual and just create a return
			codebuilder.label("exception", __edx);
			this.__return(ops);
			
			// Do no more work
			return;
		}
		
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
		
		// Uncount just the locals and perform a return to propogate up
		this.__return(ops.localsOnly());
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
	 * Generates an access to a field.
	 *
	 * @param __at The type of access to perform.
	 * @param __fr The reference to the field.
	 * @return The accessed field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private final AccessedField __fieldAccess(FieldAccessType __at,
		FieldReference __fr)
		throws NullPointerException
	{
		if (__at == null || __fr == null)
			throw new NullPointerException("NARG");
		
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
			case InstructionIndex.ALOAD:
				this.__runALoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
				this.__runALoad(op - InstructionIndex.ALOAD_0);
				break;
			
			case InstructionIndex.DUP:
				this.__runDup();
				break;
			
			case InstructionIndex.INVOKESPECIAL:
				this.__runInvoke(InvokeType.SPECIAL,
					__i.<MethodReference>argument(0, MethodReference.class));
				break;
			
			case InstructionIndex.LDC:
				this.__runLdc(__i.<ConstantValue>argument(
					0, ConstantValue.class));
				break;
			
			case InstructionIndex.NEW:
				this.__runNew(__i.<ClassName>argument(0, ClassName.class));
				break;
			
			case InstructionIndex.PUTFIELD:
				this.__runPutField(__i.<FieldReference>argument(0,
					FieldReference.class));
				break;
			
			case InstructionIndex.RETURN:
				this.__runReturn(null);
				break;
			
			default:
				throw new todo.TODO(__i.toString());
		}
	}
	
	/**
	 * Generate code or a jump for a return using the given object position
	 * snapshot.
	 *
	 * @param __ops The position snapshot to use.
	 * @return The label of the target that is used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	private final RegisterCodeLabel __return(__ObjectPositionsSnapshot__ __ops)
		throws NullPointerException
	{
		if (__ops == null)
			throw new NullPointerException("NARG");
		
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// Try to get existing labels
		Map<__ObjectPositionsSnapshot__, RegisterCodeLabel> returns =
			this._returns;
		RegisterCodeLabel label = returns.get(__ops);
		
		// If the object operations is empty and nothing needs to be uncounted
		// we can just return directly. This is faster than jumping to another
		// point that returns
		if (__ops.isEmpty())
		{
			codebuilder.add(RegisterOperationType.RETURN);
			
			// Always create the label if it does not exist
			// This might be used by exception handlers potentially
			if (label == null)
			{
				label = codebuilder.label("return", this._nextreturndx);
				returns.put(__ops, label);
			}
		}
		
		// If the return for this operation has already been handled, just
		// do not bother duplicating it
		else if (label == null)
		{
			// Create label at this point and store it for this cleanup state
			label = codebuilder.label("return", this._nextreturndx);
			returns.put(__ops, label);
			
			// Un-count any references in locals or the stack
			__ObjectPositionsSnapshot__ snap = this.state.objectSnapshot();
			for (int i = 0, n = snap.size(); i < n; i++)
				codebuilder.add(RegisterOperationType.UNCOUNT, snap.get(i));
			
			// All returns are plain due to the fact that return address
			// registers are used
			codebuilder.add(RegisterOperationType.RETURN);
		}
		
		// Otherwise we just perform a jump to that point since it shares
		// the same cleanup code
		else
			codebuilder.add(RegisterOperationType.JUMP, label);
		
		return label;
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
		
		// Load from local to the stack
		__StackResult__ src = state.localGet(__l);
		__StackResult__ dest = state.localLoad(__l);
		
		// Only actually copy and count the destination if it has not been
		// cached!
		if (!dest.cached)
			this.codebuilder.add(
				RegisterOperationType.NARROW_COPY_AND_COUNT_DEST,
				src.register, dest.register);
	}
	
	/**
	 * Duplicate top most stack entry.
	 *
	 * @since 2019/03/24
	 */
	private final void __runDup()
	{
		// No complex work is needed, the top-most entry is just a cached
		// alias of the entry just above it
		this.state.stackDup();
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
		
		// Registers to use for the method call
		List<Integer> callargs = new ArrayList<>();
		
		// Pop all entries off the stack, note any entries which are references
		// that need to be uncounted after the call
		List<Integer> uncount = new ArrayList<>();
		JavaType[] pops = __r.handle().javaStack(__t.hasInstance());
		for (int i = pops.length - 1; i >= 0; i--)
		{
			__StackResult__ st = state.stackPop();
			
			// Add to call arguments, added at the start because we pop the
			// last argument first
			int pr = st.register;
			if (pops[i].isWide())
				callargs.add(0, pr + 1);
			callargs.add(0, pr);
			
			// Uncount later? Only do this if the register is not cached
			// because otherwise we might end up early-freeing objects
			if (!st.cached && pops[i].isObject())
				uncount.add(pr);
		}
		
		// The base of the stack is the last popped register
		int newbase = state.stackTopRegister();
		
		// Generate the call, pass the base register and the number of
		// registers to pass to the target method
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.INVOKE_FROM_CONSTANT_POOL,
			new InvokedMethod(__t, __r.handle()), new RegisterList(callargs));
		
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
	 * Handles class allocation.
	 *
	 * @param __cn The class to allocate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private final void __runNew(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Allocation may fail or the class could be invalid
		this._exceptioncheck = true;
		
		// Allocate and store into register
		this.codebuilder.add(RegisterOperationType.ALLOCATE_CLASS, __cn,
			this.state.stackPush(new JavaType(__cn)).register);
	}
	
	/**
	 * Puts a value into a field.
	 *
	 * @param __fr The field to put into.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private final void __runPutField(FieldReference __fr)
		throws NullPointerException
	{
		if (__fr == null)
			throw new NullPointerException("NARG");
		
		// Pop the value from the stack
		int from = this.state.stackPop().register;
		
		// Need to setup field access
		AccessedField af = this.__fieldAccess(FieldAccessType.INSTANCE, __fr);
		
		// Generate code
		RegisterCodeBuilder codebuilder = this.codebuilder;
		PrimitiveType pt = __fr.memberType().primitiveType();
		if (pt == null)
			throw new todo.TODO();
		else
			switch (pt)
			{
				default:
					throw new todo.OOPS(__fr.toString());
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
		__StackState__ state = this.state;
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// If we are returning a value, we need to store it into the return
		// register
		if (__rt != null)
		{
			throw new todo.TODO();
		}
		
		// Either generate a new cleanup or jump to it
		this.__return(this.state.objectSnapshot());
	}
}

