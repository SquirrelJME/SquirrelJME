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
	protected final ExceptionHandlerRanges exceptionranges;
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** The stacks which have been recorded. */
	private final Map<Integer, JavaStackState> _stacks =
		new LinkedHashMap<>();
	
	/** Exception and enqueue table. */
	private final List<ExceptionEnqueueAndTable> _eettable =
		new ArrayList<>();
	
	/** Table of made exceptions. */
	private final List<ExceptionClassStackAndTable> _ecsttable =
		new ArrayList<>();
	
	/** The returns which have been performed. */
	private final List<JavaStackEnqueueList> _returns =
		new ArrayList<>();
	
	/** The current state of the stack. */
	private JavaStackState _stack;
	
	/** The instruction throws an exception, it must be checked. */
	private boolean _exceptioncheck;
	
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
		this.exceptionranges = new ExceptionHandlerRanges(__bc);
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
			
			// Set line for code generation
			codebuilder.setSourceLine(bytecode.lineOfAddress(addr));
			
			// Reset exception check
			this._exceptioncheck = false;
			
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
					// Dup
				case InstructionIndex.DUP:
					this.__doStackShuffle(JavaStackShuffleType.DUP);
					break;
				
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
				
					// Create new instance of something
				case InstructionIndex.NEW:
					this.__doNew(sji.<ClassName>argument(0, ClassName.class));
					break;
					
					// This literally does nothing so no output code needs to
					// be generated at all
				case InstructionIndex.NOP:
					break;
				
					// Return from method, with no return value
				case InstructionIndex.RETURN:
					this.__doReturn(null);
					break;
				
					// Not yet implemented
				default:
					throw new todo.OOPS(
						sji.toString() + "/" + inst.toString());
			}
			
			// After the operation a new stack is now used
			JavaStackState newstack = this._stack;
			
			// Generate exception handler?
			if (this._exceptioncheck)
				codebuilder.add(RegisterOperationType.JUMP_IF_EXCEPTION,
					this.__exceptionLabel());
			
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
		
		// Generate the code needed for the ECS table
		List<ExceptionClassStackAndTable> ecsttable = this._ecsttable;
		if (!ecsttable.isEmpty())
			this.__processECSTTable(ecsttable);
		
		// Generate exception handlers
		List<ExceptionEnqueueAndTable> eettable = this._eettable;
		if (!eettable.isEmpty())
			this.__processEETTable(eettable);
		
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
		
		// Handle exceptions following
		this._exceptioncheck = true;
		
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
		
		// Checks on the instance
		RegisterCodeBuilder codebuilder = this.codebuilder;
		if (__t.hasInstance())
		{
			// Cannot be null
			codebuilder.add(RegisterOperationType.IFNULL_REF_CLEAR,
				result.in(0).register, this.__makeExceptionLabel(
				"java/lang/NullPointerException"));
			
			// Must also be the right type of object as well
			codebuilder.add(
				RegisterOperationType.JUMP_IF_NOT_INSTANCE_REF_CLEAR,
				__r.handle().outerClass(), result.in(0).register,
				this.__makeExceptionLabel("java/lang/ClassCastException"));
		}
		
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
		
		// New is a complex operation and could fail for many reasons
		this._exceptioncheck = true;
		
		// Just the type is pushed to the stack
		JavaStackResult result = this._stack.doStack(0, new JavaType(__cn));
		this._stack = result.after();
		
		// Allocate and store into register
		RegisterCodeBuilder codebuilder = this.codebuilder;
		codebuilder.add(RegisterOperationType.NEW,
			__cn, result.out(0).register);
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
		// Return this value?
		if (__rt != null)
		{
			throw new todo.TODO();
		}
		
		// Return from this point or jump to an existing return/cleanup point
		this.__generateReturn(this._stack);
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
	}
	
	/**
	 * Creates and stores an exception.
	 *
	 * @return The label to the exception.
	 * @since 2019/04/09
	 */
	private final RegisterCodeLabel __exceptionLabel()
	{
		// Setup
		ExceptionEnqueueAndTable st = this.exceptionranges.enqueueAndTable(
			this._stack.possibleEnqueue(), this._addr);
		
		// Store into the table if it is missing
		List<ExceptionEnqueueAndTable> eettable = this._eettable;
		int dx = eettable.indexOf(st);
		if (dx < 0)
			eettable.add((dx = eettable.size()), st);
		
		// Just create a label to reference it, it is generated later
		return new RegisterCodeLabel("exception", dx);
	}
	
	/**
	 * Generates a return point.
	 *
	 * @param __jss The current stack state.
	 * @since 2019/04/03
	 */
	private final RegisterCodeLabel __generateReturn(JavaStackState __jss)
		throws NullPointerException
	{
		if (__jss == null)
			throw new NullPointerException("NARG");
		
		return this.__generateReturn(__jss.possibleEnqueue());
	}
	
	/**
	 * Generates a return point.
	 *
	 * @param __eq Enqueue list.
	 * @return A label for the return point.
	 * @since 2019/04/03
	 */
	private final RegisterCodeLabel __generateReturn(JavaStackEnqueueList __eq)
		throws NullPointerException
	{
		if (__eq == null)
			throw new NullPointerException("NARG");
		
		// Find unique return point
		boolean freshdx;
		List<JavaStackEnqueueList> returns = this._returns;
		int dx = returns.indexOf(__eq);
		if ((freshdx = (dx < 0)))
			returns.add((dx = returns.size()), __eq);
		
		// Label used for return
		RegisterCodeLabel lb = new RegisterCodeLabel("return", dx);
		
		// If this was never added here, make sure a label exists
		if (freshdx)
			codebuilder.label(lb);
		
		// If the enqueue list
		RegisterCodeBuilder codebuilder = this.codebuilder;
		if (__eq.isEmpty())
		{
			// Since there is nothing to uncount, just return
			codebuilder.add(RegisterOperationType.RETURN);
			
			return lb;
		}
		
		// Since the enqueue list is not empty, we can just trim a register
		// from the top and recursively go down
		// So uncount the top
		codebuilder.add(RegisterOperationType.UNCOUNT,
			__eq.top());
		
		// Recursively go down since the enqueues may possibly be shared
		this.__generateReturn(__eq.trimTop());
		
		// Note that we do not return the recursive result because that
		// will be for another enqueue state
		return lb;
	}
	
	/**
	 * Creates a label which points to code that generates the given
	 * exception.
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
		
		// We need a label at generation time that has the current state of
		// the stack and such after the operation is performed
		this.__exceptionLabel();
		
		// Setup
		ExceptionClassStackAndTable cst = this.exceptionranges.
			classStackAndTable(new ClassName(__cl), this._stack, this._addr);
		
		// Store into the table if it is missing
		List<ExceptionClassStackAndTable> ecsttable = this._ecsttable;
		int dx = ecsttable.indexOf(cst);
		if (dx < 0)
			ecsttable.add((dx = ecsttable.size()), cst);
		
		// Just create a label to reference it, it is generated later
		return new RegisterCodeLabel("makeexception", dx);
	}
	
	/**
	 * Processes the ECST table.
	 *
	 * @param __ecsts The exception class and stack table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	private final void __processECSTTable(
		List<ExceptionClassStackAndTable> __ecsts)
		throws NullPointerException
	{
		if (__ecsts == null)
			throw new NullPointerException("NARG");
			
		// Used in generating the pilot area
		ByteCode bytecode = this.bytecode;
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// This table is used for secondary jump points
		List<ExceptionEnqueueAndTable> eettable = this._eettable;
		
		// Go through the table and process everything
		for (int dx = 0, dxn = __ecsts.size(); dx < dxn; dx++)
		{
			// Get details
			ExceptionClassStackAndTable ecst = __ecsts.get(dx);
			ExceptionStackAndTable sat = ecst.stackandtable;
			ClassName exn = ecst.name;
			ExceptionHandlerTable ehtable = sat.table;
			
			// Set source line as a guess based on highest start address
			// of the table
			codebuilder.setSourceLine(bytecode.lineOfAddress(
				ehtable.maximumStartAddress()));
			
			// Mark label here for this
			codebuilder.label("makeexception", dx);
			
			// Allocate exception at the highest register point which acts
			// as a temporary
			int tempreg = this._stack.usedregisters;
			codebuilder.add(RegisterOperationType.NEW,
				exn, tempreg);
			
			// Initialize object with constructor
			codebuilder.add(RegisterOperationType.INVOKE_METHOD,
				new InvokedMethod(InvokeType.SPECIAL, new MethodHandle(exn,
				new MethodName("<init>"), new MethodDescriptor("()V"))),
				new RegisterList(tempreg));
			
			// Generate jump to exception
			codebuilder.add(RegisterOperationType.JUMP,
				new RegisterCodeLabel("exception", eettable.indexOf(
					new ExceptionEnqueueAndTable(sat.stack.possibleEnqueue(),
						sat.table))));
		}
	}
	
	/**
	 * Processes the EET table.
	 *
	 * @param __eets The exception enqueue table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	private final void __processEETTable(
		List<ExceptionEnqueueAndTable> __eets)
		throws NullPointerException
	{
		if (__eets == null)
			throw new NullPointerException("NARG");
		
		// For code building
		ByteCode bytecode = this.bytecode;
		RegisterCodeBuilder codebuilder = this.codebuilder;
		
		// Used to detect when nothing is to be done
		List<JavaStackEnqueueList> returns = this._returns;
		
		// The base register where exceptions will go on the stack
		int basereg = this._stack.getStack(0).register;
		
		// Go through the table and process everything
		for (int dx = 0, dxn = __eets.size(); dx < dxn; dx++)
		{
			// Get details
			ExceptionEnqueueAndTable eet = __eets.get(dx);
			JavaStackEnqueueList enq = eet.enqueue;
			ExceptionHandlerTable ehtable = eet.table;
			
			// Set source line as a guess based on highest start address
			// of the table
			codebuilder.setSourceLine(bytecode.lineOfAddress(
				ehtable.maximumStartAddress()));
			
			// If the exception handler is empty then we do not have to check
			// anything so just, cleanup and end
			if (ehtable.isEmpty())
			{
				// If we never saw a cleanup for this return yet we can
				// generate one here to be used for later points
				int rdx = returns.indexOf(enq);
				if (rdx < 0)
					this.__generateReturn(enq);
				
				// We can just alias this exception to the return point to
				// cleanup everything
				codebuilder.label("exception", dx,
					codebuilder.labelTarget("return", rdx));
				
				// Handle others
				continue;
			}
			
			// Code will be going here for this exception now, so mark it
			codebuilder.label("exception", dx);
			
			// Uncount anything needed off the stack
			int stackstart = enq.stackstart;
			while (enq.size() > stackstart)
			{
				// Uncount the top
				codebuilder.add(RegisterOperationType.UNCOUNT,
					enq.top());
				
				// Trim off the top and remember the state when we want to
				// make the return
				enq = enq.trimTop();
			}
			
			// Add jumps to labels
			for (ExceptionHandler eh : ehtable)
				codebuilder.add(
					RegisterOperationType.JUMP_IF_INSTANCE_GET_EXCEPTION,
					eh.type(),
					new RegisterCodeLabel("java", eh.handlerAddress()),
					basereg);
			
			// Generate return point with our remaining enqueue
			this.__generateReturn(enq);
		}
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

