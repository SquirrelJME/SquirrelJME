// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueNumber;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InstructionJumpTargets;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.classfile.StackMapTable;
import net.multiphasicapps.classfile.StackMapTableState;

/**
 * This class goes through the byte code for a method and then performs stack
 * and instruction work on it.
 *
 * @since 2019/04/06
 */
public final class ByteCodeProcessor
{
	/** The input byte code to be read. */
	protected final ByteCode bytecode;
	
	/** Handle for byte-code operations. */
	protected final ByteCodeHandler handler;
	
	/** The state of the byte code. */
	protected final ByteCodeState state;
	
	/** Reverse jump table. */
	private final Map<Integer, InstructionJumpTargets> _revjumps;
	
	/** Jump targets for this instruction. */
	private InstructionJumpTargets _ijt;
	
	/** Flase is the preprocessor state, otherwise run the handler. */
	private boolean _dohandling;
	
	/**
	 * Initializes the byte code processor.
	 *
	 * @param __bc The target byte code.
	 * @param __h Handler for the byte code operations.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	public ByteCodeProcessor(ByteCode __bc, ByteCodeHandler __h)
		throws NullPointerException
	{
		if (__bc == null || __h == null)
			throw new NullPointerException("NARG");
		
		this.bytecode = __bc;
		this.handler = __h;
		
		// The state is used to share between the processor and the handler
		ByteCodeState state = __h.state();
		this.state = state;
		
		// Load initial Java stack state from the initial stack map
		JavaStackState s;
		state.stack = (s = JavaStackState.of(__bc.stackMapTable().get(0),
			__bc.writtenLocals()));
		state.stacks.put(0, s);
		
		// Reverse jump table to detect jump backs
		this._revjumps = __bc.reverseJumpTargets();
	}
	
	/**
	 * Processes the byte code and calls the destination handler.
	 *
	 * @since 2019/04/06
	 */
	public final void process()
	{
		ByteCode bytecode = this.bytecode;
		ByteCodeState state = this.state;
		ByteCodeHandler handler = this.handler;
		Map<Integer, JavaStackState> stacks = state.stacks;
		Map<Integer, JavaStackPoison> stackpoison = state.stackpoison;
		
		// Go through each operation twice, performing pre-processing first
		// to make things a bit simpler and more well known when it comes
		// to caching.
		for (int pp = 0; pp < 2; pp++)
		{
			// Is handling to be done?
			boolean dohandling = (pp == 2);
			this._dohandling = dohandling;
			
			// Since this is the start, the last address needs to be reset
			// because it will be invalid!
			state.lastaddr = -1;
			
			// Process instruction
			for (Instruction inst : bytecode)
			{
				// Translate to simple instruction for easier handling
				SimplifiedJavaInstruction sji =
					new SimplifiedJavaInstruction(inst);
				
				// Debug
				todo.DEBUG.note("%s %s (%s)", (dohandling ? "Handling" :
					"Preprocessing"), sji, inst);
				
				// Current instruction info
				state.instruction = inst;
				state.simplified = sji;
				
				// Store the last processed address
				state.lastaddr = state.addr;
				
				// Current processing this address
				int addr = inst.address();
				state.addr = addr;
				
				// Set line where this code was found
				state.line = bytecode.lineOfAddress(addr);
				
				// These jump targets are used to map out the state of stacks
				// across various points
				InstructionJumpTargets ijt, rijt;
				this._ijt = (ijt = inst.jumpTargets());
				state.jumptargets = ijt;
				
				// Reverse jump targets are used to detect jumps to previous
				// addresses
				rijt = this._revjumps.get(addr);
				state.reversejumptargets = (rijt != null ? rijt :
					new InstructionJumpTargets());
				
				// {@squirreljme.error JC37 No recorded stack state for this
				// position. (The address to check)}
				JavaStackState stack = stacks.get(addr);
				if (stack == null)
					throw new IllegalArgumentException("JC37 " + addr);
				
				// Load stack
				state.stack = stack;
				
				// Preprocessing operations
				if (!dohandling)
				{
					// If we are jumping back to this instruction at any point
					// we need to flush the stack so that nothing is cached on
					// it. The resulting flushed stack is then used instead
					// Note that if we jump to ourselves we might have entered
					// with something cached and might end up using that when
					// we do not want to
					if (state.reversejumptargets.hasSameOrLaterAddress(addr))
					{
						// Perform a flush of the cache
						JavaStackResult fres = stack.doCacheFlush();
						
						// Generate the moving around operations
						if (true)
							throw new todo.TODO();
						
						// Set stack as poisoned at this point
						if (true)
							stackpoison.put(addr, null);
						
						// Use the result of the flush as the state instead so
						// that it propagates ahead from now on
						stack = fres.after();
					}
					
					// Was the stack change?
					if (state.stack != stack)
						state.stack = stack;
				}
				
				// Handle instruction
				else
				{
					// Call pre-handler
					handler.instructionSetup();
					
					// If the stack has been adjusted for any reason, replace
					// the stored stack for this point
					JavaStackState mns = state.stack;
					if (!stack.equals(mns))
						stacks.put(addr, (stack = mns));
				}
				
				// Handle the operation
				switch (sji.operation())
				{
						// Object array load
					case InstructionIndex.AALOAD:
						this.__doArrayLoad(null);
						break;
						
						// Object array store
					case InstructionIndex.AASTORE:
						this.__doArrayStore(null);
						break;
						
						// Allocate new array
					case InstructionIndex.ANEWARRAY:
						this.__doNewArray(sji.<ClassName>argument(0,
							ClassName.class));
						break;
						
						// Length of array
					case InstructionIndex.ARRAYLENGTH:
						this.__doArrayLength();
						break;
						
						// Throw exception
					case InstructionIndex.ATHROW:
						this.__doThrow();
						break;
						
						// Check that object is of a type, or fail
					case InstructionIndex.CHECKCAST:
						this.__doCheckCast(sji.<ClassName>argument(0,
							ClassName.class));
						break;
					
						// Dup
					case InstructionIndex.DUP:
						this.__doStackShuffle(JavaStackShuffleType.DUP);
						break;
						
						// Get field
					case InstructionIndex.GETFIELD:
						this.__doFieldGet(sji.<FieldReference>argument(0,
							FieldReference.class));
						break;
						
						// Get static
					case InstructionIndex.GETSTATIC:
						this.__doStaticGet(sji.<FieldReference>argument(0,
							FieldReference.class));
						break;
						
						// Goto
					case InstructionIndex.GOTO:
						this.__doGoto(sji.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class));
						break;
						
						// If comparison against zero
					case SimplifiedJavaInstruction.IF:
						this.__doIf(sji.<DataType>argument(0, DataType.class),
							sji.<CompareType>argument(1, CompareType.class),
							sji.<InstructionJumpTarget>argument(2,
								InstructionJumpTarget.class));
						break;
						
						// Compare two values
					case SimplifiedJavaInstruction.IF_CMP:
						this.__doIfCmp(
							sji.<DataType>argument(0, DataType.class),
							sji.<CompareType>argument(1, CompareType.class),
							sji.<InstructionJumpTarget>argument(2,
								InstructionJumpTarget.class));
						break;
						
						// Increment local
					case InstructionIndex.IINC:
						this.__doIInc(sji.intArgument(0), sji.intArgument(1));
						break;
						
						// Invoke interface
					case InstructionIndex.INVOKEINTERFACE:
						this.__doInvoke(InvokeType.INTERFACE,
							sji.<MethodReference>argument(0,
								MethodReference.class));
						break;
					
						// Invoke special
					case InstructionIndex.INVOKESPECIAL:
						this.__doInvoke(InvokeType.SPECIAL,
							sji.<MethodReference>argument(0,
								MethodReference.class));
						break;
					
						// Invoke static
					case InstructionIndex.INVOKESTATIC:
						this.__doInvoke(InvokeType.STATIC,
							sji.<MethodReference>argument(0,
								MethodReference.class));
						break;
						
						// Invoke virtual
					case InstructionIndex.INVOKEVIRTUAL:
						this.__doInvoke(InvokeType.VIRTUAL,
							sji.<MethodReference>argument(0,
								MethodReference.class));
						break;
					
						// Load constant
					case InstructionIndex.LDC:
						this.__doLdc(sji.<ConstantValue>argument(0,
							ConstantValue.class));
						break;
					
						// Load local variable to the stack
					case SimplifiedJavaInstruction.LOAD:
						this.__doLoad(sji.<DataType>argument(0,
							DataType.class), sji.intArgument(1));
						break;
						
						// Math
					case SimplifiedJavaInstruction.MATH:
						this.__doMath(sji.<DataType>argument(0,
							DataType.class), sji.<MathType>argument(1,
								MathType.class));
						break;
					
						// Create new instance of something
					case InstructionIndex.NEW:
						this.__doNew(sji.<ClassName>argument(0,
							ClassName.class));
						break;
						
						// This literally does nothing so no output code needs to
						// be generated at all
					case InstructionIndex.NOP:
						this.__doNop();
						break;
						
						// Primitive array load
					case SimplifiedJavaInstruction.PALOAD:
						this.__doArrayLoad(sji.<PrimitiveType>argument(0,
							PrimitiveType.class));
						break;
						
						// Primitive array store
					case SimplifiedJavaInstruction.PASTORE:
						this.__doArrayStore(sji.<PrimitiveType>argument(0,
							PrimitiveType.class));
						break;
					
						// Put of instance field
					case InstructionIndex.PUTFIELD:
						this.__doFieldPut(sji.<FieldReference>argument(0,
							FieldReference.class));
						break;
					
						// Return from method, with no return value
					case InstructionIndex.RETURN:
						this.__doReturn(null);
						break;
					
						// Place stack variable into local
					case SimplifiedJavaInstruction.STORE:
						this.__doStore(sji.<DataType>argument(0, DataType.class),
							sji.intArgument(1));
						break;
						
						// Return value
					case SimplifiedJavaInstruction.VRETURN:
						this.__doReturn(sji.<DataType>argument(0, DataType.class).
							toJavaType());
						break;
					
						// Not yet implemented
					default:
						throw new todo.OOPS(
							sji.toString() + "/" + inst.toString());
				}
				
				// Call post-handler
				if (dohandling)
					handler.instructionFinish();
			}
		}
	}
	
	/**
	 * Gets length of array.
	 *
	 * @since 2019/04/06
	 */
	private final void __doArrayLength()
	{
		throw new todo.TODO();
		/*
		// [array] -> [len]
		JavaStackResult result = this._stack.doStack(1, JavaType.INTEGER);
		this._stack = result.after();
		
		// Possibly clear the instance later
		this.__refEnqueue(result.enqueue());
		
		// Cannot be null
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
			result.in(0).register,
			this.__makeExceptionLabel("java/lang/NullPointerException"));
		
		// Get length
		codebuilder.add(RegisterOperationType.ARRAY_LENGTH,
			result.in(0).register,
			result.out(0).register);
		
		// Clear references
		this.__refClear();
		*/
	}
	
	/**
	 * Loads value from value.
	 *
	 * @param __pt The type to load, {@code null} is considered to be an
	 * object.
	 * @since 2019/04/06
	 */
	private final void __doArrayLoad(PrimitiveType __pt)
	{
		throw new todo.TODO();
		/*
		// [array, index] -> [value]
		JavaStackResult result = this._stack.doStack(2, (__pt == null ?
			new JavaType(new ClassName("java/lang/Object")) :
			__pt.stackJavaType()));
		this._stack = result.after();
		
		// Possibly clear the instance later
		this.__refEnqueue(result.enqueue());
		
		// Check for NPE, and OOB
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
			result.in(0).register,
			this.__makeExceptionLabel("java/lang/NullPointerException"));
		codebuilder.add(RegisterOperationType.ARRAY_BOUND_CHECK_AND_REF_CLEAR,
			result.in(0).register, result.in(1).register,
			this.__makeExceptionLabel("java/lang/IndexOutOfBoundsException"));
		
		// Generate
		codebuilder.add(DataType.of(__pt).arrayOperation(false),
			result.in(0).register,
			result.in(1).register,
			result.out(0).register);
		
		// Sign-extend signed types?
		if (__pt == PrimitiveType.BYTE || __pt == PrimitiveType.SHORT)
			codebuilder.add((__pt == PrimitiveType.BYTE ?
					RegisterOperationType.SIGN_X8 :
					RegisterOperationType.SIGN_X16),
				result.out(0).register);
		
		// Clear references
		this.__refClear();
		*/
	}
	
	/**
	 * Stores value into an array.
	 *
	 * @param __pt The type to store, {@code null} is considered to be an
	 * object.
	 * @since 2019/04/06
	 */
	private final void __doArrayStore(PrimitiveType __pt)
	{
		throw new todo.TODO();
		/*
		// [array, index, value]
		JavaStackResult result = this._stack.doStack(3);
		this._stack = result.after();
		
		// Possibly clear the instance or value later
		this.__refEnqueue(result.enqueue());
		
		// Check for NPE and OOB
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
			result.in(0).register, this.__makeExceptionLabel(
			"java/lang/NullPointerException"));
		codebuilder.add(RegisterOperationType.ARRAY_BOUND_CHECK_AND_REF_CLEAR,
			result.in(0).register, result.in(1).register,
			this.__makeExceptionLabel("java/lang/IndexOutOfBoundsException"));
		
		// Check for store exception
		if (__pt == null)
			codebuilder.add(
				RegisterOperationType.ARRAY_STORE_CHECK_AND_REF_CLEAR,
				result.in(0).register, result.in(2).register,
				this.__makeExceptionLabel("java/lang/ArrayStoreException"));
		
		// Generate
		this.codebuilder.add(DataType.of(__pt).arrayOperation(true),
			result.in(0).register,
			result.in(1).register,
			result.in(2).register);
		
		// Clear references
		this.__refClear();
		*/
	}
	
	/**
	 * Checks that the object on the stack is of the given type.
	 *
	 * @param __cn The name of the class to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doCheckCast(ClassName __cn)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// The stack is unchanged, we just push the same type
		JavaStackResult result = this._stack.doStack(1,
			new JavaType(__cn));
		
		// Enqueue instance possibly, is only cleared on jump
		this.__refEnqueue(result.enqueue());
		
		// Has to be of the right type
		this.codebuilder.add(
			RegisterOperationType.JUMP_IF_NOT_INSTANCE_REF_CLEAR,
			__cn, result.in(0).register,
			this.__makeExceptionLabel("java/lang/ClassCastException"));
		
		// Reset enqueues
		this.codebuilder.add(RegisterOperationType.REF_RESET);
		*/
	}
	
	/**
	 * Reads a value from a field.
	 *
	 * @param __fr The field reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doFieldGet(FieldReference __fr)
		throws NullPointerException
	{
		if (__fr == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// The data type determines which instruction to use
		PrimitiveType pt = __fr.memberType().primitiveType();
		DataType dt = DataType.of(pt);
		
		// Field access information
		AccessedField ac = this.__fieldAccess(FieldAccessType.INSTANCE, __fr);
		
		// Do stack operations
		JavaStackResult result = this._stack.doStack(1,
			new JavaType(__fr.memberType()));
		this._stack = result.after();
		
		// Enqueue instance possibly
		this.__refEnqueue(result.enqueue());
		
		// Cannot be null
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
			result.in(0).register, this.__makeExceptionLabel(
			"java/lang/NullPointerException"));
			
		// Also has to be the right type
		codebuilder.add(RegisterOperationType.JUMP_IF_NOT_INSTANCE_REF_CLEAR,
			__fr.className(), result.in(0).register,
			this.__makeExceptionLabel("java/lang/ClassCastException"));
		
		// Generate code
		codebuilder.add(dt.fieldAccessOperation(false, false),
			ac,
			result.in(0).register,
			result.out(0).register);
		
		// Sign-extend signed types?
		if (pt == PrimitiveType.BYTE || pt == PrimitiveType.SHORT)
			codebuilder.add((pt == PrimitiveType.BYTE ?
					RegisterOperationType.SIGN_X8 :
					RegisterOperationType.SIGN_X16),
				result.out(0).register);
		
		// Clear references
		this.__refClear();
		*/
	}
	
	/**
	 * Puts a value into a field.
	 *
	 * @param __fr The field reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/04
	 */
	private final void __doFieldPut(FieldReference __fr)
		throws NullPointerException
	{
		if (__fr == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// [inst, value] ->
		JavaStackResult result = this._stack.doStack(2);
		this._stack = result.after();
		
		// Clear out any input references
		this.__refEnqueue(result.enqueue());
		
		// Cannot be null
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
			result.in(0).register,
			this.__makeExceptionLabel("java/lang/NullPointerException"));
		
		// Also has to be the right type
		codebuilder.add(RegisterOperationType.JUMP_IF_NOT_INSTANCE_REF_CLEAR,
			__fr.className(), result.in(0).register,
			this.__makeExceptionLabel("java/lang/ClassCastException"));
		
		// Generate code
		codebuilder.add(DataType.of(__fr.memberType().primitiveType()).
				fieldAccessOperation(false, true),
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr),
			result.in(0).register,
			result.in(1).register);
		
		// Clear references as needed
		this.__refClear();
		*/
	}
	
	/**
	 * Goes to another address.
	 *
	 * @param __jt The target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doGoto(InstructionJumpTarget __jt)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		this.codebuilder.add(RegisterOperationType.JUMP,
			this.__javaLabel(__jt));
		*/
	}
	
	/**
	 * Performs if comparison against zero.
	 *
	 * @param __type The type to work with on the stack.
	 * @param __ct The comparison type.
	 * @param __ijt The instruction jump target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/05
	 */
	private final void __doIf(DataType __type, CompareType __ct,
		InstructionJumpTarget __ijt)
		throws NullPointerException
	{
		if (__type == null || __ct == null || __ijt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Pop input from the stack
		JavaStackResult result = this._stack.doStack(1);
		this._stack = result.after();
		
		// Enqueue the input for counting
		boolean doenq = this.__refEnqueue(result.enqueue());
		
		// Generate code, no later refclear needs to be done because if
		// zero operation if doenq is set will clear the references
		this.codebuilder.add(__ct.ifZeroOperation(doenq),
			result.in(0).register, this.__javaLabel(__ijt));
		*/
	}
	
	/**
	 * Performs if comparison of two values against each other.
	 *
	 * @param __type The type to work with on the stack.
	 * @param __ct The comparison type.
	 * @param __ijt The instruction jump target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doIfCmp(DataType __type, CompareType __ct,
		InstructionJumpTarget __ijt)
		throws NullPointerException
	{
		if (__type == null || __ct == null || __ijt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Pop input from the stack
		JavaStackResult result = this._stack.doStack(2);
		this._stack = result.after();
		
		// Enqueue the input for counting
		boolean doenq = this.__refEnqueue(result.enqueue());
		
		// Generate code, no later refclear needs to be done because if
		// zero operation if doenq is set will clear the references
		this.codebuilder.add(__ct.ifABOperation(doenq),
			result.in(0).register, result.in(1).register,
			this.__javaLabel(__ijt));
		*/
	}
	
	/**
	 * Increments local variable.
	 *
	 * @param __l The local to increment.
	 * @param __v The value to increment by.
	 * @since 2019/04/06
	 */
	private final void __doIInc(int __l, int __v)
	{
		throw new todo.TODO();
		/*
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// Load constant into temporary register
		JavaStackState stack = this._stack;
		int tempbase = stack.usedregisters;
		codebuilder.add(RegisterOperationType.X32_CONST,
			__v, tempbase);
		
		// Perform the add with the topmost local
		JavaStackState.Info local = stack.getLocal(__l);
		codebuilder.add(RegisterOperationType.INT_ADD,
			local.register, tempbase, local.register);
		*/
	}
	
	/**
	 * Handles invocation of other methods.
	 *
	 * @param __t The type of invocation to perform.
	 * @param __r The method to invoke.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	private final void __doInvoke(InvokeType __t, MethodReference __r)
		throws NullPointerException
	{
		if (__t == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Return value type, if any
		MethodHandle mf = __r.handle();
		FieldDescriptor rv = mf.descriptor().returnValue();
		boolean hasrv = (rv != null);
		
		// Number of argument to pop
		int popcount = mf.javaStack(__t.hasInstance()).length;
		
		// Perform stack operation
		ByteCodeState state = this.state;
		JavaStackResult result = (!hasrv ? state.stack.doStack(popcount) :
			state.stack.doStack(popcount, new JavaType(rv)));
		this.__update(result);
		
		// Stop processing here
		if (!this._dohandling)
			return;
		
		// Forward
		this.handler.doInvoke(__t, __r, (!hasrv ? null :
			result.out(0)), result.in());
	}
	
	/**
	 * Loads constant value onto the stack.
	 *
	 * @param __v The value to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	private final void __doLdc(ConstantValue __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Get push properties
		JavaType jt = __v.type().javaType();
		
		// Push to the stack this type, the result is always cached
		JavaStackResult result = this.state.stack.doStack(0, true, jt);
		this.__update(result);
		
		// Do not call generator, we just want the stack result
		if (!this._dohandling)
			return;
		
		// Call the appropriate handler
		ByteCodeHandler handler = this.handler;
		switch (__v.type())
		{
			case INTEGER:
				handler.doMath(StackJavaType.INTEGER, MathType.OR,
					JavaStackResult.INPUT_ZERO, (Integer)__v.boxedValue(),
					result.out(0));
				break;
				
			case FLOAT:
				handler.doMath(StackJavaType.FLOAT, MathType.OR,
					JavaStackResult.INPUT_ZERO, (Float)__v.boxedValue(),
					result.out(0));
				break;
			
			case LONG:
				handler.doMath(StackJavaType.LONG, MathType.OR,
					JavaStackResult.INPUT_ZERO, (Long)__v.boxedValue(),
					result.out(0));
				break;
				
			case DOUBLE:
				handler.doMath(StackJavaType.DOUBLE, MathType.OR,
					JavaStackResult.INPUT_ZERO, (Double)__v.boxedValue(),
					result.out(0));
				break;
			
			case STRING:
			case CLASS:
				if (true)
					throw new todo.TODO();
				/*
				codebuilder.add(RegisterOperationType.LOAD_POOL_VALUE,
					__v.boxedValue(), result.out(0).register);
				codebuilder.add(RegisterOperationType.COUNT,
					result.out(0).register);
				*/
				break;
			
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Loads from a local and puts to the stack.
	 *
	 * @param __jt The type to push.
	 * @param __from The source local.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	private final void __doLoad(DataType __jt, int __from)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		// Load from local variable
		JavaStackResult result = this.state.stack.doLocalLoad(__from);
		this.__update(result);
		
		// Stop processing here
		if (!this._dohandling)
			return;
		
		// Only perform the copy if the value is different, because otherwise
		// it would have just been cached
		if (result.in(0).register != result.out(0).register)
			this.handler.doCopy(result.in(0), result.out(0));
	}
	
	/**
	 * Performs math operation.
	 *
	 * @param __pt The primitive type.
	 * @param __mot The math operation type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doMath(DataType __pt, MathType __mot)
		throws NullPointerException
	{
		if (__pt == null || __mot == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// [a, b] -> [result]
		JavaStackResult result = this._stack.doStack(2, __pt.toJavaType());
		this._stack = result.after();
		
		// Perform the math
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(__mot.operation(__pt),
			result.in(0).register, result.in(1).register,
			result.out(0).register);
		*/
	}
	
	/**
	 * Creates a new instance of the given class.
	 *
	 * @param __cn The class to create.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/04
	 */
	private final void __doNew(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// New is a complex operation and could fail for many reasons
		this._exceptioncheck = true;
		
		// Just the type is pushed to the stack
		JavaStackResult result = this._stack.doStack(0, new JavaType(__cn));
		this._stack = result.after();
		
		// Allocate and store into register
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.NEW,
			__cn, result.out(0).register);
		*/
	}
	
	/**
	 * Allocates a new array.
	 *
	 * @param __cn The class to allocate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/05
	 */
	private final void __doNewArray(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Allocation may fail or the class could be invalid
		this._exceptioncheck = true;
		
		// Only the length is on the stack
		JavaStackResult result = this._stack.doStack(1,
			new JavaType(__cn.addDimensions(1)));
		this._stack = result.after();
		
		// Cannot be negative
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFLT,
			result.in(0).register, this.__makeExceptionLabel(
			"java/lang/NegativeArraySizeException"));
		
		// Generate
		codebuilder.add(RegisterOperationType.NEW_ARRAY,
			__cn, result.in(0).register, result.out(0).register);
		*/
	}
	
	/**
	 * Do nothing.
	 *
	 * @since 2019/04/07
	 */
	private final void __doNop()
	{
		// Just do nothing
		this.__update(this.state.stack.doNothing());
	}
	
	/**
	 * Handles returning.
	 *
	 * @param __rt The type to return, {@code null} means nothing is to be
	 * returned.
	 * @since 2019/04/03
	 */
	private final void __doReturn(JavaType __rt)
	{
		ByteCodeState state = this.state;
		JavaStackState stack = state.stack;
		
		// Handle returning a value?
		JavaStackResult.Input rvslot;
		if (__rt != null)
		{
			throw new todo.TODO();
			/*
			// Pop return value
			JavaStackResult result = this._stack.doStack(1);
			this._stack = result.after();
			
			// Store into the return register
			this.codebuilder.add(DataType.of(__rt).returnValueStoreOperation(),
				result.in(0).register);
			*/
		}
		
		// No value is returned
		else
		{
			// No return value slot used
			rvslot = null;
		}
		
		// Stop pre-processing here
		if (!this._dohandling)
			return;
		
		// Call handler
		this.handler.doReturn(rvslot);
	}
	
	/**
	 * Performs shuffling of the stack.
	 *
	 * @param __st The type of shuffle to do.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/04
	 */
	private final void __doStackShuffle(JavaStackShuffleType __st)
		throws NullPointerException
	{
		if (__st == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// Find shuffle function to use
		JavaStackState stack = this._stack;
		JavaStackShuffleType.Function stfunc = stack.findShuffleFunction(__st);
		
		// Debug
		todo.DEBUG.note("Found function: %s", stfunc);
		
		// Pop all the input values, making sure it affects the stack
		JavaStackResult result = stack.doStack(stfunc.in.max);
		
		// One simple way to handle this operation without doing anything
		// really that complex is to move off the temporaries to a new
		// location after all the defined variables. Then once they are
		// all copied, they are copied back
		// The following contains the position information and where
		// registered are stored.
		int tempbase = stack.usedregisters,
			stackbase = result.in(0).register,
			numpop = result.inCount();
		int[] virt = new int[numpop];
		
		// First do plain copies of the input to a bunch of temporary
		// registers
		JavaStackResult.Input[] ins = new JavaStackResult.Input[numpop];
		for (int i = 0; i < numpop; i++)
		{
			// Keep track of input here
			JavaStackResult.Input in = result.in(i);
			ins[i] = in;
			
			// The virtual register is from the temporary base and the
			// pop index
			int vat = tempbase + (in.register - stackbase);
			virt[i] = vat;
			
			// Perform the non-counting copy
			codebuilder.add(DataType.of(in.type).copyOperation(true),
				in.register, vat);
		}
		
		// This will keep track of how many times an input is used so it will
		// either be uncounted on zero, or counted for every value above 1
		int[] inusage = new int[numpop];
		
		// Before the stack can be pushed to we need to fill it with the
		// right types, so go through the destination placements and place
		// accordingly. Note that the amount to push is not 1:1 to the
		// function pushes
		List<JavaType> pushtypes = new ArrayList<>();
		List<Integer> pushindex = new ArrayList<>();
		for (int i = 0, n = stfunc.out.max; i < n; i++)
		{
			// Ignore negative values because this represents a top one
			int povar = stfunc.out.variable(i);
			if (povar < 0)
				continue;
			
			// Get the variable we are wanting to push
			JavaStackResult.Input pin = ins[povar];
			
			// Register it for pushing
			pushtypes.add(pin.type);
			pushindex.add(povar);
			
			// If a variable is used more than once count it up
			if (++inusage[povar] > 1)
				if (pin.type.isObject())
					codebuilder.add(RegisterOperationType.COUNT, virt[povar]);
		}
		
		// For any variables which were not used at all, do not count
		for (int i = 0; i < numpop; i++)
			if (inusage[i] == 0)
				codebuilder.add(RegisterOperationType.UNCOUNT, virt[i]);
		
		// Push all of the types and store this stack result
		result = result.after().doStack(0,
			pushtypes.<JavaType>toArray(new JavaType[pushtypes.size()]));
		this._stack = result.after();
		
		// Do data copies of the variables from temporary space
		for (int i = 0, n = result.outCount(); i < n; i++)
			codebuilder.add(
				DataType.of(result.out(i).type).copyOperation(true),
				virt[pushindex.get(i)], result.out(i).register);
		*/
	}
	
	/**
	 * Reads a value from a static field.
	 *
	 * @param __fr The field reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doStaticGet(FieldReference __fr)
		throws NullPointerException
	{
		if (__fr == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// The data type determines which instruction to use
		PrimitiveType pt = __fr.memberType().primitiveType();
		DataType dt = DataType.of(pt);
		
		// Field access information
		AccessedField ac = this.__fieldAccess(FieldAccessType.STATIC, __fr);
		
		// Do stack operations
		JavaStackResult result = this._stack.doStack(0,
			new JavaType(__fr.memberType()));
		this._stack = result.after();
		
		// Generate code
		codebuilder.add(dt.fieldAccessOperation(true, false),
			ac, result.out(0).register);
		
		// Sign-extend signed types?
		if (pt == PrimitiveType.BYTE || pt == PrimitiveType.SHORT)
			codebuilder.add((pt == PrimitiveType.BYTE ?
					RegisterOperationType.SIGN_X8 :
					RegisterOperationType.SIGN_X16),
				result.out(0).register);
		*/
	}
	
	/**
	 * Stores an entry on the stack.
	 *
	 * @param __jt The type to pop.
	 * @param __to The destination local.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	private final void __doStore(DataType __jt, int __to)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Pop from the stack, but since we set a new result remember the
		// input
		JavaStackResult result = this._stack.doStack(1);
		JavaStackResult.Input in = result.in(0);
		
		// Store into the local as well
		result = result.after().doLocalSet(in.type, __to);
		this._stack = result.after();
		
		// Uncount destination area
		RegisterCodeBuilder codebuilder = this.codebuilder;
		for (int i : result.enqueue())
			codebuilder.add(RegisterOperationType.UNCOUNT,
				i);
		
		// Do the copy, do not count because there will be a net result
		codebuilder.add(__jt.copyOperation(true),
			in.register, result.out(0).register);
		*/
	}
	
	/**
	 * Performs a throw of an exception on the stack.
	 *
	 * @since 2019/04/05
	 */
	private final void __doThrow()
	{
		throw new todo.TODO();
		/*
		// This operation throws an exception, so we will just go to checking
		// it.
		this._exceptioncheck = true;
		
		// Pop from the stack
		JavaStackResult result = this._stack.doStack(1);
		this._stack = result.after();
		
		// Enqueue?
		boolean doenq = this.__refEnqueue(result.enqueue());
		
		// Cannot be null
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
			result.in(0).register, this.__makeExceptionLabel(
			"java/lang/NullPointerException"));
		
		// Generate code
		codebuilder.add(RegisterOperationType.SET_AND_FLAG_EXCEPTION,
			result.in(0).register);
		
		// Clear references
		if (doenq)
			this.__refClear();
		*/
	}
	
	/**
	 * Updates the stack state and result.
	 *
	 * @param __jsr The stack result.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/07
	 */
	private final void __update(JavaStackResult __jsr)
		throws NullPointerException
	{
		if (__jsr == null)
			throw new NullPointerException("NARG");
		
		// The new stack state
		JavaStackState newstack = __jsr.after();
		
		// Needed for processing
		ByteCodeState state = this.state;
		Map<Integer, JavaStackState> stacks = state.stacks;
		int addr = state.addr;
		
		// Store result and the new stack
		state.result = __jsr;
		state.stack = newstack;
		
		// Target stack states are not touched in the normal handling state
		// because collisions and transitioning of states is handled in the
		// pre-processing step
		if (this._dohandling)
			return;
		
		// The result of the jump calculations may result in the stack
		// being poisoned potentially
		Map<Integer, JavaStackPoison> stackpoison = state.stackpoison;
		
		// Set target stack states for destinations of this instruction
		// Calculate the exception state only if it is needed
		JavaStackState hypoex = null;
		InstructionJumpTargets ijt = this._ijt;
		if (ijt != null && !ijt.isEmpty())
			for (int i = 0, n = ijt.size(); i < n; i++)
			{
				int jta = ijt.get(i).target();
				
				// Lazily calculate the exception handler since it might
				// not always be needed
				boolean isexception = ijt.isException(i);
				if (isexception && hypoex == null)
					hypoex = newstack.doExceptionHandler(new JavaType(
						new ClassName("java/lang/Throwable"))).after();
				
				// The type of stack to target
				JavaStackState use = (isexception ? hypoex : newstack);
				
				// Is empty state, use this state because we defined it first
				JavaStackState dss = stacks.get(jta);
				if (dss == null)
					stacks.put(jta, use);
				
				// For later addresses which do not have an exact stack state
				// match, a partial un-cache will have to be used.
				// Note that jump backs are ignored here since those were
				// processed and we cannot adjust the states anymore
				else if (jta > addr && !use.equals(dss))
				{
					todo.DEBUG.note("Transition is required!");
					
					throw new todo.TODO();
				}
			}
	}
}

