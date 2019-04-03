// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
 * This is a translator which is designed to run as quick as possible while
 * translating all of the instructions.
 *
 * @since 2019/04/03
 */
public class QuickTranslator
	implements Translator
{
	/** The byte code to translate. */
	protected final ByteCode bytecode;
	
	/** Used to build register codes. */
	protected final RegisterCodeBuilder codebuilder =
		new RegisterCodeBuilder();
	
	/** Exception tracker. */
	protected final __ExceptionTracker__ exceptiontracker;
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** The stacks which have been recorded. */
	private final Map<Integer, JavaStackState> _stacks =
		new LinkedHashMap<>();
	
	/** The current state of the stack. */
	private JavaStackState _stack;
	
	/** The current address being processed. */
	private int _addr =
		-1;
	
	/** Last registers enqueued. */
	private JavaStackEnqueueList _lastenqueue;
	
	/**
	 * Converts the input byte code to a register based code.
	 *
	 * @param __bc The byte code to translate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public QuickTranslator(ByteCode __bc)
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
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/03
	 */
	@Override
	public RegisterCode convert()
	{
		ByteCode bytecode = this.bytecode;
		RegisterCodeBuilder codebuilder = this.codebuilder;
		Map<Integer, JavaStackState> stacks = this._stacks;
		
		// Process every instruction
		for (Instruction inst : bytecode)
		{
			// Translate to simple instruction for easier handling
			SimplifiedJavaInstruction sji =
				new SimplifiedJavaInstruction(inst);
			
			// Debug
			todo.DEBUG.note("Xlate %s (%s)", sji, inst);
			
			// Current processing this address
			int addr = inst.address();
			this._addr = addr;
			
			// {@squirreljme.error JC37 No recorded stack state for this
			// position. (The address to check)}
			JavaStackState stack = stacks.get(addr);
			if (stack == null)
				throw new IllegalArgumentException("JC37 " + addr);
			
			// Add label to refer to this instruction in the Java instruction
			// space
			codebuilder.label("java", addr);
			
			// Handle the operation
			switch (sji.operation())
			{
					// Load local variable to the stack
				case SimplifiedJavaInstruction.LOAD:
					this.__doLoad(sji.<JavaType>argument(0, JavaType.class),
						sji.intArgument(1));
					break;
					
				case InstructionIndex.INVOKEINTERFACE:
					this.__doInvoke(InvokeType.INTERFACE, sji.<MethodReference>
						argument(0, MethodReference.class));
					break;
				
				case InstructionIndex.INVOKESPECIAL:
					this.__doInvoke(InvokeType.SPECIAL, sji.<MethodReference>
						argument(0, MethodReference.class));
					break;
				
				case InstructionIndex.INVOKESTATIC:
					this.__doInvoke(InvokeType.STATIC, sji.<MethodReference>
						argument(0, MethodReference.class));
					break;
					
				case InstructionIndex.INVOKEVIRTUAL:
					this.__doInvoke(InvokeType.VIRTUAL, sji.<MethodReference>
						argument(0, MethodReference.class));
					break;
				
					// Load constant
				case InstructionIndex.LDC:
					this.__doLdc(sji.<ConstantValue>argument(0,
						ConstantValue.class));
					break;
					
					// This literally does nothing so no output code needs to
					// be generated at all
				case InstructionIndex.NOP:
					break;
				
					// Not yet implemented
				default:
					throw new todo.OOPS(
						sji.toString() + "/" + inst.toString());
			}
			
			// After the operation a new stack is now used
			JavaStackState newstack = this._stack;
			
			// Set target stack states for destinations of this instruction
			// Calculate the exception state only if it is needed
			JavaStackState hypoex = null;
			InstructionJumpTargets ijt = inst.jumpTargets();
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
					
					// Is empty state
					JavaStackState dss = stacks.get(jta);
					if (dss == null)
						stacks.put(jta, use);
				}
		}
		
		if (true)
			throw new todo.TODO();
		
		// Build the final code
		return codebuilder.build();
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
		JavaStackResult result = (!hasrv ? this._stack.doStack(popcount) :
			this._stack.doStack(popcount, new JavaType(rv)));
		this._stack = result.after();
		
		// Enqueue the input for counting
		this.__refEnqueue(result.enqueue());
		
		// Cannot be null if an instance type
		RegisterCodeBuilder codebuilder = this.codebuilder;
		if (__t.hasInstance())
			codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
				result.in(0).register, this.__makeExceptionLabel(
				"java/lang/NullPointerException"));
		
		// Setup registers to use for the method call
		List<Integer> callargs = new ArrayList<>(popcount);
		for (int i = 0; i < popcount; i++)
		{
			// Add the input register
			JavaStackResult.Input in = result.in(i);
			callargs.add(in.register);
			
			// But also if it is wide, we need to pass the other one or else
			// the value will be clipped
			if (in.type.isWide())
				callargs.add(in.register + 1);
		}
		
		// Generate the call, pass the base register and the number of
		// registers to pass to the target method
		codebuilder.add(RegisterOperationType.INVOKE_METHOD,
			new InvokedMethod(__t, __r.handle()), new RegisterList(callargs));
		
		// Uncount any used references
		this.__refClear();
		
		// Load the return value onto the stack
		if (hasrv)
			throw new todo.TODO();
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
		
		// Push to the stack this type
		JavaStackResult result = this._stack.doStack(0, jt);
		this._stack = result.after();
		
		// Generate instruction
		RegisterCodeBuilder codebuilder = this.codebuilder;
		switch (__v.type())
		{
			case INTEGER:
				codebuilder.add(RegisterOperationType.X32_CONST,
					(Integer)__v.boxedValue(),
					result.out(0).register);
				break;
				
			case FLOAT:
				codebuilder.add(RegisterOperationType.X32_CONST,
					Float.floatToRawIntBits((Float)__v.boxedValue()),
					result.out(0).register);
				break;
			
			case LONG:
				codebuilder.add(RegisterOperationType.X64_CONST,
					__v.boxedValue(),
					result.out(0).register);
				break;
				
			case DOUBLE:
				codebuilder.add(RegisterOperationType.X64_CONST,
					Double.doubleToRawLongBits((Double)__v.boxedValue()),
					result.out(0).register);
				break;
			
			case STRING:
			case CLASS:
				codebuilder.add(RegisterOperationType.LOAD_POOL_VALUE,
					__v.boxedValue(), result.out(0).register);
				codebuilder.add(RegisterOperationType.COUNT,
					result.out(0).register);
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
	private final void __doLoad(JavaType __jt, int __from)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		// Push to the stack
		JavaStackResult result = this._stack.doStack(0, __jt);
		this._stack = result.after();
		
		// Do the copy
		this.codebuilder.add(DataType.of(__jt).copyOperation(false),
			result.before().getLocal(__from).register,
			result.out(0).register);
	}
	
	/**
	 * Creates a label which
	 *
	 * @param __cl The class type to throw.
	 * @return The label to the exception generator.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	private final RegisterCodeLabel __makeExceptionLabel(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * If anything has been previous enqueued then generate code to clear it.
	 *
	 * @since 2019/03/30
	 */
	private final void __refClear()
	{
		// Do nothing if nothing has been enqueued
		JavaStackEnqueueList lastenqueue = this._lastenqueue;
		if (lastenqueue == null)
			return;
		
		// Generate instruction to clear the enqueue
		this.codebuilder.add(RegisterOperationType.REF_CLEAR);
		
		// No need to clear anymore
		this._lastenqueue = null;
	}
	
	/**
	 * Generates code to enqueue registers, if there are any.
	 *
	 * @param __r The registers to enqueue.
	 * @return True if the enqueue list was not empty.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	private final boolean __refEnqueue(JavaStackEnqueueList __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Nothing to enqueue?
		if (__r.isEmpty())
		{
			this._lastenqueue = null;
			return false;
		}
		
		// Generate enqueue and set for clearing next time
		this.codebuilder.add(RegisterOperationType.REF_ENQUEUE,
			new RegisterList(__r.registers()));
		this._lastenqueue = __r;
		
		// Did enqueue something
		return true;
	}
}

