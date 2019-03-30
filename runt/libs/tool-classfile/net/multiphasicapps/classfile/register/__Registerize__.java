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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueInteger;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InstructionJumpTargets;
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
	
	/** Used to build register codes. */
	protected final RegisterCodeBuilder codebuilder =
		new RegisterCodeBuilder();
	
	/** Exception tracker. */
	protected final __ExceptionTracker__ exceptiontracker;
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** The instruction throws an exception, it must be checked. */
	private boolean _exceptioncheck;
	
	/** Reverse jump table. */
	private Map<Integer, InstructionJumpTargets> _revjumps;
	
	/** Exception handler combinations to generate. */
	private final List<__ExceptionCombo__> _usedexceptions =
		new ArrayList<>();
	
	/** Object position maps to return label points. */
	private final Map<__ObjectPositionsSnapshot__, RegisterCodeLabel>
		_returns =
		new LinkedHashMap<>();
	
	/** The stacks which have been recorded. */
	private final Map<Integer, JavaStackState> _stacks =
		new LinkedHashMap<>();
	
	/** The current state of the stack. */
	private JavaStackState _stack;
	
	/** The current PC being processed. */
	private int _currentprocesspc =
		-1;
	
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
		this.exceptiontracker = new __ExceptionTracker__(__bc);
		this.defaultfieldaccesstime = ((__bc.isInstanceInitializer() ||
			__bc.isStaticInitializer()) ? FieldAccessTime.INITIALIZER :
			FieldAccessTime.NORMAL);
		
		// Load initial Java stack state from the initial stack map
		JavaStackState s;
		this._stack = (s = JavaStackState.of(__bc.stackMapTable().get(0),
			__bc.writtenLocals()));
		this._stacks.put(0, s);
		
		// Load reverse jump table
		this._revjumps = __bc.reverseJumpTargets();
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
		RegisterCodeBuilder codebuilder = this.codebuilder;
		Map<Integer, InstructionJumpTargets> revjumps = this._revjumps;
		
		// Stacks for target methods
		Map<Integer, JavaStackState> stacks = this._stacks;
		
		// Process every instruction
		for (Instruction inst : bytecode)
		{
			// Debug
			todo.DEBUG.note("Xlate %s", inst);
			
			// Current processing this address
			int addr = inst.address();
			this._currentprocesspc = addr;
			
			// Clear the exception check since not every instruction will
			// generate an exception, this will reduce the code size greatly
			this._exceptioncheck = false;
			
			// Set source line for this instruction for debugging purposes
			codebuilder.setSourceLine(bytecode.lineOfAddress(addr));
			
			// {@squirreljme.error JC2s No recorded stack state for this
			// position. (The address to check)}
			JavaStackState stack = stacks.get(addr);
			if (stack == null)
				throw new IllegalArgumentException("JC2s " + addr);
			
			// If this instruction is jumped to from a future address then
			// we need to invalidate our cached stack items
			// This is done to keep the code generator simpler so it does not
			// have to mess around with various different steps and we can
			// keep processing the method linearly
			InstructionJumpTargets ijt = revjumps.get(addr);
			if (ijt != null && ijt.hasLaterAddress(addr))
				throw new todo.TODO();
			
			// Add label to refer to this instruction in Java terms
			codebuilder.label("java", addr);
			
			// Process instructions
			this.__process(inst);
			
			// If an exception is thrown it needs to be handled accordingly
			// This means uncounting anything on the stack, reading the
			// exception register value, then jumping to the exception handler
			// for this instruction
			if (this._exceptioncheck)
			{
				// Create jumping label for this exception
				RegisterCodeLabel ehlab = this.__exceptionTrack(addr);
				
				// Just create a jump here
				codebuilder.add(
					RegisterOperationType.JUMP_IF_EXCEPTION, ehlab);
			}
			
			// Set target stack states for destinations of this instruction
			ijt = inst.jumpTargets();
			if (true)
				 throw new todo.TODO();
		}
		
		// Invalidate source lines for the exception table
		codebuilder.setSourceLine(-1);
		
		// If we need to generate exception tables, do it now
		List<__ExceptionCombo__> usedexceptions = this._usedexceptions;
		if (!usedexceptions.isEmpty())
			for (int i = 0, n = usedexceptions.size(); i < n; i++)
				this.__exceptionGenerate(usedexceptions.get(i), i);
		
		// Go through the labels and make it so that any "freezejava" labels
		// which are not set go directly to their original instruction
		if (true)
			throw new todo.TODO();
		
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
		
		// Debug
		todo.DEBUG.note("Exception gen %s,%s -> %d", ops, ehtable, __edx);
		
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
		int sbreg = this.state.stackBaseRegister();
		for (ExceptionHandler eh : ehtable)
			codebuilder.add(
				RegisterOperationType.JUMP_IF_INSTANCE_GET_EXCEPTION,
				eh.type(), this.__javaLabel(eh.handlerAddress()),
				sbreg);
		
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
	private final RegisterCodeLabel __exceptionTrack(int __pc)
	{
		// Create combo for the object and exception data
		__ExceptionCombo__ ec = this.exceptiontracker.createCombo(
			this.state.objectSnapshot(), __pc);
		
		// If this combo is already in the table, do not add it
		List<__ExceptionCombo__> usedexceptions = this._usedexceptions;
		int dx = usedexceptions.indexOf(ec);
		if (dx >= 0)
			return new RegisterCodeLabel("exception", dx);
		
		// Otherwise just add it
		dx = usedexceptions.size();
		usedexceptions.add(ec);
		
		// Label to jump to
		return new RegisterCodeLabel("exception", dx);
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
		
		// Accessing final fields of another class will always be treated as
		// normal despite being in the constructor of a class
		ByteCode bytecode = this.bytecode;
		if (!bytecode.thisType().equals(__fr.className()))
			return new AccessedField(FieldAccessTime.NORMAL, __at, __fr);
		return new AccessedField(this.defaultfieldaccesstime, __at, __fr);
	}
	
	/**
	 * Creates a label referring to a Java address, this could be replaced
	 * potentially.
	 *
	 * This method should be called after all stack operations have been
	 * performed, otherwise it may result in a confusing state.
	 *
	 * @param __addr The address to point to.
	 * @return The label to jump to.
	 * @since 2019/03/27
	 */
	private final RegisterCodeLabel __javaLabel(int __addr)
	{
		// If the address we are jumping to is at a future point we can use
		// a simple jump ahead
		int currentprocesspc = this._currentprocesspc;
		if (__addr >= currentprocesspc)
			return new RegisterCodeLabel("java", __addr);
		
		// Otherwise we will need to uncount any local variables which are
		// not at the previous location. Additionally since that past
		// instruction would have had its caches and counts completely wiped
		// we need to do the same when entering to make sure it matches the
		// same state
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
			case InstructionIndex.WIDE_ALOAD:
				this.__runLoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
				this.__runLoad(op - InstructionIndex.ALOAD_0);
				break;
			
			case InstructionIndex.ATHROW:
				this.__runAThrow();
				break;
				
			case InstructionIndex.DLOAD:
			case InstructionIndex.WIDE_DLOAD:
				this.__runLoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.DLOAD_0:
			case InstructionIndex.DLOAD_1:
			case InstructionIndex.DLOAD_2:
			case InstructionIndex.DLOAD_3:
				this.__runLoad(op - InstructionIndex.DLOAD_0);
				break;
			
			case InstructionIndex.DUP:
				this.__runDup();
				break;
				
			case InstructionIndex.FLOAD:
			case InstructionIndex.WIDE_FLOAD:
				this.__runLoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.FLOAD_0:
			case InstructionIndex.FLOAD_1:
			case InstructionIndex.FLOAD_2:
			case InstructionIndex.FLOAD_3:
				this.__runLoad(op - InstructionIndex.FLOAD_0);
				break;
			
			case InstructionIndex.GETFIELD:
				this.__runGetField(__i.<FieldReference>argument(0,
					FieldReference.class));
				break;
			
			case InstructionIndex.ICONST_M1:
			case InstructionIndex.ICONST_0:
			case InstructionIndex.ICONST_1:
			case InstructionIndex.ICONST_2:
			case InstructionIndex.ICONST_3:
			case InstructionIndex.ICONST_4:
			case InstructionIndex.ICONST_5:
				this.__runLdc(new ConstantValueInteger(
					op - InstructionIndex.ICONST_0));
				break;
			
			case InstructionIndex.IFEQ:
				this.__runIfZero(CompareType.EQUALS,
					__i.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class));
				break;
				
			case InstructionIndex.IFNE:
				this.__runIfZero(CompareType.NOT_EQUALS,
					__i.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class));
				break;
				
			case InstructionIndex.IFLT:
				this.__runIfZero(CompareType.LESS_THAN,
					__i.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class));
				break;
				
			case InstructionIndex.IFGE:
				this.__runIfZero(CompareType.GREATER_THAN_OR_EQUALS,
					__i.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class));
				break;
				
			case InstructionIndex.IFGT:
				this.__runIfZero(CompareType.GREATER_THAN,
					__i.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class));
				break;
				
			case InstructionIndex.IFLE:
				this.__runIfZero(CompareType.LESS_THAN_OR_EQUALS,
					__i.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class));
				break;
				
			case InstructionIndex.ILOAD:
			case InstructionIndex.WIDE_ILOAD:
				this.__runLoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.ILOAD_3:
				this.__runLoad(op - InstructionIndex.ILOAD_0);
				break;
			
			case InstructionIndex.INVOKESPECIAL:
				this.__runInvoke(InvokeType.SPECIAL,
					__i.<MethodReference>argument(0, MethodReference.class));
				break;
			
			case InstructionIndex.LDC:
				this.__runLdc(__i.<ConstantValue>argument(
					0, ConstantValue.class));
				break;
				
			case InstructionIndex.LLOAD:
			case InstructionIndex.WIDE_LLOAD:
				this.__runLoad(__i.<Integer>argument(0, Integer.class));
				break;
			
			case InstructionIndex.LLOAD_0:
			case InstructionIndex.LLOAD_1:
			case InstructionIndex.LLOAD_2:
			case InstructionIndex.LLOAD_3:
				this.__runLoad(op - InstructionIndex.LLOAD_0);
				break;
			
			case InstructionIndex.NEW:
				this.__runNew(__i.<ClassName>argument(0, ClassName.class));
				break;
			
			case InstructionIndex.NEWARRAY:
				this.__runNewArray(__i.<PrimitiveType>argument(0,
					PrimitiveType.class).toClassName());
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
		
		// Debug
		todo.DEBUG.note("return %s -> %s", __ops, label);
		
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
				label = codebuilder.label("return", this._nextreturndx++);
				returns.put(__ops, label);
			}
		}
		
		// If the return for this operation has already been handled, just
		// do not bother duplicating it. As an optimization to group as many
		// returns up as possible, only a single entry will be uncounted and
		// this method will recursively handle returns until the state is
		// empty
		else if (label == null)
		{
			// Create label at this point and store it for this cleanup state
			label = codebuilder.label("return", this._nextreturndx++);
			returns.put(__ops, label);
			
			// Uncount the top-most entry
			int top = __ops.size() - 1;
			codebuilder.add(RegisterOperationType.UNCOUNT, __ops.get(top));
			
			// Recursively handle this return point, this will generate
			// other cleanup points that can be jumped to. So if this one
			// cleans up 0, 1, and 3 then a cleanup that just does 0 will
			// jump to a point near this.
			this.__return(__ops.trimTop());
			
			// Return the original label for this point!
			return label;
		}
		
		// Otherwise we just perform a jump to that point since it shares
		// the same cleanup code
		else
			codebuilder.add(RegisterOperationType.JUMP, label);
		
		return label;
	}
	
	/**
	 * Throws exception.
	 *
	 * @since 2019/03/26
	 */
	private final void __runAThrow()
	{
		// This operation throws an exception, so we will just go to checking
		// it.
		this._exceptioncheck = true;
		
		// The exception to toss
		__StackResult__ pop = this.state.stackPop();
		
		// Set exception register and flag it
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.SET_AND_FLAG_EXCEPTION,
			pop.register);
		
		// Need to uncount this exception?
		if (pop.needsCounting())
			codebuilder.add(RegisterOperationType.UNCOUNT,
				pop.register);
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
	 * Read a value from a field.
	 *
	 * @param __fr The field to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/26
	 */
	private final void __runGetField(FieldReference __fr)
		throws NullPointerException
	{
		if (__fr == null)
			throw new NullPointerException("NARG");
		
		__StackState__ state = this.state;
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// The data type determined which instruction to use
		DataType dt = DataType.of(__fr.memberType().primitiveType());
		
		// Field access information
		AccessedField ac = this.__fieldAccess(FieldAccessType.INSTANCE, __fr);
		
		// The instance of the object to read from along with where the object
		// goes
		__StackResult__ inst = state.stackPop(),
			dest = state.stackPush(new JavaType(__fr.memberType()));
		
		// If the instance needs counting, before we can replace it we need
		// to read the field
		if (inst.needsCounting())
		{
			// Load value into the field register first
			codebuilder.add(dt.fieldAccessOperation(false, false),
				ac,
				inst.register,
				-1);
			
			// Uncount the instance
			codebuilder.add(RegisterOperationType.UNCOUNT,
				inst.register);
			
			// Load from the field register
			codebuilder.add(
				dt.fieldRegisterOperation(dest.needsCounting(), false),
				dest.register);
		}
		
		// Otherwise if it does not need uncounting, we can just overwrite
		// the value here
		else
		{
			codebuilder.add(dt.fieldAccessOperation(false, false),
				ac,
				inst.register,
				dest.register);
		}
	}
	
	/**
	 * Compars against zero.
	 *
	 * @param __ct The comparison type to perform.
	 * @param __j The jump target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/26
	 */
	private final void __runIfZero(CompareType __ct, InstructionJumpTarget __j)
		throws NullPointerException
	{
		if (__ct == null || __j == null)
			throw new NullPointerException("NARG");
		
		__StackResult__ pop = this.state.stackPop();
		
		// The target of the jump is derived from the jump target
		RegisterCodeLabel label = this.__javaLabel(__j.target());
		
		// If this is something that needs counting, we cannot directly
		// operate on it so it must first be loaded into the field value
		// before we go off and branch
		RegisterCodeBuilder codebuilder = this.codebuilder;
		if (pop.needsCounting())
		{	
			// Need this since each operation is slightly different
			DataType dt = DataType.of(pop.type);
			
			// Load value into field register
			codebuilder.add(dt.fieldRegisterOperation(true, true),
				pop.register);
			
			// Uncount the thing
			codebuilder.add(RegisterOperationType.UNCOUNT,
				pop.register);
			
			// Branch but do it against the field value instead
			codebuilder.add(__ct.fieldIfZeroOperation(),
				label);
		}
		
		// Otherwise we can operate directly on the register
		else
		{
			codebuilder.add(__ct.ifZeroOperation(),
				pop.register,
				label);
		}
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
			if (st.needsCounting())
				uncount.add(pr);
		}
		
		// The base of the stack is the last popped register
		int newbase = state.stackTopRegister();
		
		// Generate the call, pass the base register and the number of
		// registers to pass to the target method
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.INVOKE_METHOD,
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
				codebuilder.add(RegisterOperationType.X32_CONST,
					(Integer)__v.boxedValue(), dest.register);
				break;
				
			case FLOAT:
				codebuilder.add(RegisterOperationType.X32_CONST,
					Float.floatToRawIntBits((Float)__v.boxedValue()),
					dest.register);
				break;
			
			case LONG:
				codebuilder.add(RegisterOperationType.X64_CONST,
					__v.boxedValue(), dest.register);
				break;
				
			case DOUBLE:
				codebuilder.add(RegisterOperationType.X64_CONST,
					Double.doubleToRawLongBits((Double)__v.boxedValue()),
					dest.register);
				break;
			
			case STRING:
			case CLASS:
				codebuilder.add(RegisterOperationType.LOAD_POOL_VALUE,
					__v.boxedValue(), dest.register);
				
				// Do not count the object because it will always have at
				// least one reference due to it being a constant global
				dest.slot._nocounting = true;
				break;
			
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Loads from a local onto the stack.
	 *
	 * @param __l The local to load.
	 * @since 2019/03/14
	 */
	private final void __runLoad(int __l)
	{
		__StackState__ state = this.state;
		
		// Load from local to the stack
		__StackResult__ src = state.localGet(__l);
		__StackResult__ dest = state.localLoad(__l);
		
		// If not cached, we need to actually copy the value
		if (!dest.isCached())
			this.codebuilder.add(
				DataType.of(src.type).copyOperation(dest.needsCounting()),
				src.register, dest.register);
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
		this.codebuilder.add(RegisterOperationType.NEW,
			__cn, this.state.stackPush(new JavaType(__cn)).register);
	}
	
	/**
	 * Allocate a new array.
	 *
	 * @param __t The type of array to create.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private final void __runNewArray(ClassName __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Allocation may fail or the class could be invalid
		this._exceptioncheck = true;
		
		// Although len and dst are in the same spot in the stack, the length
		// could be cached
		__StackState__ state = this.state;
		int len = state.stackPop().register,
			dst = state.stackPush(new JavaType(__t.addDimensions(1))).register;
		
		// Generate instruction
		this.codebuilder.add(RegisterOperationType.NEW_ARRAY,
			__t, len, dst);
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
		
		// [inst, value] ->
		JavaStackResult result = this._stack.doStack(2);
		
		// We will use this state
		this._stack = result.after();
		
		// Generate code
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(
			DataType.of(__fr.memberType().primitiveType()).
				fieldAccessOperation(false, true),
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr),
			result.in(0).register,
			value.in(1).register);
		
		// Run operations on stack output
		this.__stackGenerateCode(result);
		
		// Pop value from stack
		__StackResult__ value = this.state.stackPop();
		__StackResult__ inst = this.state.stackPop();
		
		// Generate code
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(
			DataType.of(__fr.memberType().primitiveType()).
				fieldAccessOperation(false, true),
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr),
			inst.register,
			value.register);
		
		// Need to uncount when removing from the stack?
		if (value.needsCounting())
			codebuilder.add(RegisterOperationType.UNCOUNT,
				value.register);
		if (inst.needsCounting())
			codebuilder.add(RegisterOperationType.UNCOUNT,
				inst.register);
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

