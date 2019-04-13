// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.ByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeState;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.ExceptionClassEnqueueAndTable;
import dev.shadowtail.classfile.xlate.ExceptionHandlerRanges;
import dev.shadowtail.classfile.xlate.ExceptionStackAndTable;
import dev.shadowtail.classfile.xlate.InvokeType;
import dev.shadowtail.classfile.xlate.JavaStackEnqueueList;
import dev.shadowtail.classfile.xlate.JavaStackResult;
import dev.shadowtail.classfile.xlate.JavaStackState;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import dev.shadowtail.classfile.xlate.StateOperation;
import dev.shadowtail.classfile.xlate.StateOperations;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodReference;

/**
 * This contains the handler for the near native byte code.
 *
 * @since 2019/04/06
 */
public final class NearNativeByteCodeHandler
	implements ByteCodeHandler
{
	/** State of the byte code. */
	public final ByteCodeState state =
		new ByteCodeState();
	
	/** Used to build native code. */
	protected final NativeCodeBuilder codebuilder =
		new NativeCodeBuilder();
	
	/** Exception tracker. */
	protected final ExceptionHandlerRanges exceptionranges;
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** The type of the current class being processed. */
	protected final ClassName thistype;
	
	/** Standard exception handler table. */
	private final Map<ExceptionStackAndTable, __EData__> _ehtable =
		new LinkedHashMap<>();
	
	/** Made exception table. */
	private final Map<ClassStackAndLabel, __EData__> _metable =
		new LinkedHashMap<>();
	
	/** The returns which have been performed. */
	private final List<JavaStackEnqueueList> _returns =
		new ArrayList<>();
	
	/** Last registers enqueued. */
	private JavaStackEnqueueList _lastenqueue;
	
	/**
	 * Initializes the byte code handler.
	 *
	 * @param __bc The byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public NearNativeByteCodeHandler(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		this.exceptionranges = new ExceptionHandlerRanges(__bc);
		this.defaultfieldaccesstime = ((__bc.isInstanceInitializer() ||
			__bc.isStaticInitializer()) ? FieldAccessTime.INITIALIZER :
			FieldAccessTime.NORMAL);
		this.thistype = __bc.thisType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doArrayLength(JavaStackResult.Input __in,
		JavaStackResult.Output __len)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Cannot be null
		codebuilder.addIfZero(__in.register, this.__labelMakeException(
			"java/lang/NullPointerException"), true);
		
		// Read length
		codebuilder.add(NativeInstructionType.ARRAYLEN,
			__in.register, __len.register);
		
		// Clear references
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doArrayStore(DataType __dt,
		JavaStackResult.Input __in, JavaStackResult.Input __dx,
		JavaStackResult.Input __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Cannot be null
		codebuilder.addIfZero(__in.register, this.__labelMakeException(
			"java/lang/NullPointerException"), true);
		
		// Check bounds
		codebuilder.add(NativeInstructionType.IFARRAY_INDEX_OOB_REF_CLEAR,
			__dx.register, __in.register, this.__labelMakeException(
			"java/lang/ArrayIndexOutOfBoundsException"));
		
		// Check that the target type is compatible, but only if the source
		// appears to be an object
		if (__v.type.isObject())
			codebuilder.add(NativeInstructionType.IFARRAY_MISTYPE_REF_CLEAR,
				__v.register, __in.register, this.__labelMakeException(
				"java/lang/ArrayStoreException"));
		
		// Store
		codebuilder.addArrayAccess(__dt, false, __v.register, __in.register,
			__dx.register);
		
		// Clear references
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doCopy(JavaStackResult.Input __in,
		JavaStackResult.Output __out)
	{
		// Push references, the dest may be overwritten
		this.__refPush();
		
		// Perform the copy, make sure to correctly handle wide copies!
		NativeCodeBuilder codebuilder = this.codebuilder;
		if (__in.type.isWide())
			codebuilder.addCopyWide(__in.register, __out.register);
		else
			codebuilder.addCopy(__in.register, __out.register);
		
		// Clear references in the event it was overwritten
		this.__refClear();
	}
	
	/**
	 * Reads a field.
	 *
	 * @param __fr The field reference.
	 * @param __i The instance.
	 * @param __v The output value.
	 * @since 2019/04/12
	 */
	public final void doFieldGet(FieldReference __fr,
		JavaStackResult.Input __i, JavaStackResult.Output __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// The instance register
		int ireg = __i.register;
		
		// Cannot be null
		codebuilder.addIfZero(ireg, this.__labelMakeException(
			"java/lang/NullPointerException"), true);
		
		// Must be the given class
		codebuilder.addIfNotClass(__fr.className(), ireg,
			this.__labelMakeException("java/lang/ClassCastException"), true);
		
		// Read of field memory
		int tempreg = state.stack.usedregisters;
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr), tempreg);
		codebuilder.addMemoryOffReg(
			DataType.of(__fr.memberType().primitiveType()), true,
			__v.register, ireg, tempreg);
			
		// Clear references as needed
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doFieldPut(FieldReference __fr,
		JavaStackResult.Input __i, JavaStackResult.Input __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// The instance register
		int ireg = __i.register;
		
		// Cannot be null
		codebuilder.addIfZero(ireg, this.__labelMakeException(
			"java/lang/NullPointerException"), true);
		
		// Must be the given class
		codebuilder.addIfNotClass(__fr.className(), ireg,
			this.__labelMakeException("java/lang/ClassCastException"), true);
		
		// Read of field memory
		int tempreg = state.stack.usedregisters;
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr), tempreg);
		codebuilder.addMemoryOffReg(
			DataType.of(__fr.memberType().primitiveType()), false,
			__v.register, ireg, tempreg);
			
		// Clear references as needed
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doIfICmp(CompareType __ct, JavaStackResult.Input __a,
		JavaStackResult.Input __b, InstructionJumpTarget __ijt)
	{
		// Push references if needed
		this.__refPush();
		
		// Add comparison
		this.codebuilder.addIfICmp(__ct, __a.register, __b.register,
			this.__labelJava(__ijt));
		
		// Clear references as needed
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/10
	 */
	@Override
	public final void doInvoke(InvokeType __t, MethodReference __r,
		JavaStackResult.Output __out, JavaStackResult.Input... __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Checks on the instance
		if (__t.hasInstance())
		{
			// The instance register
			int ireg = __in[0].register;
			
			// Cannot be null
			codebuilder.addIfZero(ireg, this.__labelMakeException(
				"java/lang/NullPointerException"), true);
			
			// Must be the given class
			codebuilder.addIfNotClass(__r.handle().outerClass(), ireg,
				this.__labelMakeException("java/lang/ClassCastException"),
				true);
		}
		
		// Fill in call arguments
		List<Integer> callargs = new ArrayList<>(__in.length * 2);
		for (int i = 0, n = __r.handle().javaStack(__t.hasInstance()).length;
			i < n; i++)
		{
			// Add the input register
			JavaStackResult.Input in = __in[i];
			callargs.add(in.register);
			
			// But also if it is wide, we need to pass the other one or else
			// the value will be clipped
			if (in.type.isWide())
				callargs.add(in.register + 1);
		}
		
		// Add invocation
		codebuilder.add(NativeInstructionType.INVOKE,
			new InvokedMethod(__t, __r.handle()), new RegisterList(callargs));
		
		// Read in return value, it is just a copy
		if (__out != null)
			if (__out.type.isWide())
				codebuilder.addCopyWide(NativeCode.RETURN_REGISTER,
					__out.register);
			else
				codebuilder.addCopy(NativeCode.RETURN_REGISTER,
					__out.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, JavaStackResult.Input __b,
		JavaStackResult.Output __c)
	{
		this.codebuilder.addMathReg(__dt, __mt, __a.register, __b.register,
			__c.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, Number __b, JavaStackResult.Output __c)
	{
		this.codebuilder.addMathConst(__dt, __mt, __a.register, __b,
			__c.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doNew(ClassName __cn, JavaStackResult.Output __out)
	{
		this.codebuilder.add(NativeInstructionType.NEW,
			__cn, __out.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doNewArray(ClassName __at,
		JavaStackResult.Input __len, JavaStackResult.Output __out)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Cannot be negative
		codebuilder.addIfICmp(CompareType.LESS_THAN, __len.register, 0,
			this.__labelMakeException("java/lang/NegativeArraySizeException"),
			true);
		
		// Allocate array
		codebuilder.add(NativeInstructionType.NEWARRAY,
			__at, __len.register, __out.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doPoolLoad(Object __v, JavaStackResult.Output __out)
	{
		this.codebuilder.add(NativeInstructionType.LOAD_POOL,
			__v, __out.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doReturn(JavaStackResult.Input __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Returning a value?
		if (__in != null)
		{
			throw new todo.TODO();
		}
		
		// Do the return
		this.__generateReturn();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doStateOperations(StateOperations __ops)
		throws NullPointerException
	{
		if (__ops == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doStaticGet(FieldReference __fr,
		JavaStackResult.Output __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Read of static memory
		int tempreg = state.stack.usedregisters;
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.STATIC, __fr), tempreg);
		codebuilder.addMemoryOffConst(
			DataType.of(__fr.memberType().primitiveType()), true,
			__v.register, tempreg, 0);
			
		// Clear references as needed
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doThrow(JavaStackResult.Input __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Cannot be null
		codebuilder.addIfZero(__in.register, this.__labelMakeException(
			"java/lang/NullPointerException"), true);
		
		// Copy into the exception register
		codebuilder.addCopy(__in.register, NativeCode.EXCEPTION_REGISTER);
		
		// Do not jump at this point, just return the exception check will be
		// flagged which will start the exception handling
		return;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void instructionFinish()
	{
		ByteCodeState state = this.state;
		
		// An exception check was requested, do a check on the exception
		// register and jump if there is something there
		if (state.canexception)
			codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
				this.__labelException(), false);
	}
	
	/**
	 * Sets up before processing the instruction.
	 *
	 * @since 2019/04/07
	 */
	public final void instructionSetup()
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		ByteCodeState state = this.state;
		int addr = state.addr;
		
		// Set source line
		codebuilder.setSourceLine(state.line);
		
		// Check if there are operations that need to be performed to make
		// sure the stack state is morphed into correctly
		StateOperations poison = state.stackpoison.get(addr);
		if (poison != null)
			this.doStateOperations(poison);
		
		// Setup a label for this current position, this is done after
		// potential flushing because it is assumed that the current state
		// is always valid even after a flush
		codebuilder.label("java", addr);
	}
	
	/**
	 * Returns the result of the translation.
	 *
	 * @return The translation result.
	 * @since 2019/04/07
	 */
	public final NativeCode result()
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		ByteCodeState state = this.state;
		List<JavaStackEnqueueList> returns = this._returns;
		
		// Temporary register base
		int tempreg = state.stack.usedregisters;
		
		// Generate make exception code
		Map<ClassStackAndLabel, __EData__> metable = this._metable;
		for (Map.Entry<ClassStackAndLabel, __EData__> e : metable.entrySet())
		{
			ClassStackAndLabel csl = e.getKey();
			__EData__ ed = e.getValue();
			
			// Set line/address info
			state.addr = ed.addr;
			int line = ed.line;
			state.line = line;
			codebuilder.setSourceLine(line);
			
			// Set label target for this one
			codebuilder.label(ed.label);
			
			// The class name used
			ClassName exn = csl.classname;
			
			// Allocate exception at the highest register point which acts
			// as a temporary
			codebuilder.add(NativeInstructionType.NEW, exn,
				NativeCode.EXCEPTION_REGISTER);
			
			// Initialize object with constructor
			codebuilder.add(NativeInstructionType.INVOKE,
				new InvokedMethod(InvokeType.SPECIAL, new MethodHandle(exn,
				new MethodName("<init>"), new MethodDescriptor("()V"))),
				new RegisterList(NativeCode.EXCEPTION_REGISTER));
			
			// Generate jump to exception handler
			codebuilder.addGoto(csl.label);
		}
		
		// Generate exception handler tables
		Map<ExceptionStackAndTable, __EData__> ehtab = this._ehtable;
		for (Map.Entry<ExceptionStackAndTable, __EData__> e : ehtab.entrySet())
		{
			ExceptionStackAndTable est = e.getKey();
			JavaStackState jss = est.stack;
			JavaStackEnqueueList enq = jss.possibleEnqueue();
			ExceptionHandlerTable ehtable = est.table;
			__EData__ ed = e.getValue();
			
			// Set line/address info
			state.addr = ed.addr;
			int line = ed.line;
			state.line = line;
			codebuilder.setSourceLine(line);
			
			// If the table is empty, just return
			if (ehtable.isEmpty())
			{
				// If we never saw a cleanup for this return yet we can
				// generate one here to be used for later points
				int rdx = returns.indexOf(enq);
				if (rdx < 0)
					codebuilder.label(ed.label,
						codebuilder.labelTarget(this.__generateReturn(enq)));
				
				// We can just alias this exception to the return point to
				// cleanup everything
				else
					codebuilder.label(ed.label,
						codebuilder.labelTarget("return", rdx));
				
				// Generate the next handler
				continue;
			}
			
			// Set label target for this one
			codebuilder.label(ed.label);
			
			throw new todo.TODO();
		}
		
		return this.codebuilder.build();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/06
	 */
	@Override
	public final ByteCodeState state()
	{
		return this.state;
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
		if (!thistype.equals(__fr.className()))
			return new AccessedField(FieldAccessTime.NORMAL, __at, __fr);
		return new AccessedField(this.defaultfieldaccesstime, __at, __fr);
	}
	
	/**
	 * Generates or jumps to another return point.
	 *
	 * @return The label to this return point.
	 * @since 2019/04/11
	 */
	private final NativeCodeLabel __generateReturn()
	{
		return this.__generateReturn(this.state.stack.possibleEnqueue());
	}
	
	/**
	 * Generates or jumps to another return point for the given enqueue.
	 *
	 * @param __eq The enqueue to return for.
	 * @return The label to this return point.
	 * @throws NullPointerException On null arguments.
	 * @return
	 */
	private final NativeCodeLabel __generateReturn(JavaStackEnqueueList __eq)
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
		NativeCodeLabel lb = new NativeCodeLabel("return", dx);
		
		// If this was never added here, make sure a label exists
		if (freshdx)
			codebuilder.label(lb);
		
		// If the enqueue list is empty then the only thing we need to do
		// is generate a return instruction
		NativeCodeBuilder codebuilder = this.codebuilder;
		if (__eq.isEmpty())
		{
			// Since there is nothing to uncount, just return
			codebuilder.add(NativeInstructionType.RETURN);
			
			return lb;
		}
		
		// If we are not making a fresh index there more things to clear out
		// then just jump to the pre-existing return point
		if (!freshdx && __eq.size() > 1)
		{
			// Jump to label
			codebuilder.addGoto(lb);
			
			return lb;
		}
		
		// Since the enqueue list is not empty, we can just trim a register
		// from the top and recursively go down
		// So uncount the top
		codebuilder.add(NativeInstructionType.UNCOUNT, __eq.top());
		
		// Recursively go down since the enqueues may possibly be shared, if
		// any of these enqueues were previously made then the recursive
		// call will just make a goto
		this.__generateReturn(__eq.trimTop());
		
		// Note that we do not return the recursive result because that
		// will be for another enqueue state
		return lb;
	}
	
	/**
	 * Creates and stores an exception.
	 *
	 * @return The label to the exception.
	 * @since 2019/04/09
	 */
	private final NativeCodeLabel __labelException()
	{
		// Setup key
		ByteCodeState state = this.state;
		ExceptionStackAndTable key = this.exceptionranges.stackAndTable(
			state.stack, state.addr);
		
		// Try to use an already existing point
		Map<ExceptionStackAndTable, __EData__> ehtable = this._ehtable;
		__EData__ rv = ehtable.get(key);
		if (rv != null)
			return rv.label;
			
		// Build new data to record this point
		rv = new __EData__(state.addr, state.line,
			new NativeCodeLabel("exception", ehtable.size()));
		ehtable.put(key, rv);
		
		// Return the created label (where the caller jumps to)
		return rv.label;
	}
	
	/**
	 * Makes a lable which goes from this address to the target address. If
	 * the target address is poisoned then the current stack will be adapted
	 * to the target state through a transition method. Otherwise if the target
	 * is not poisoned it will be a normal jump.
	 *
	 * @param __jt The jump target.
	 * @return The label of the jump.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/12
	 */
	private final NativeCodeLabel __labelJava(InstructionJumpTarget __jt)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
		
		ByteCodeState state = this.state;
		JavaStackState sourcestack = state.stack;
		
		// If the target stack state matches the current state then no
		// adapting is required at all
		int target = __jt.target();
		JavaStackState targetstack = state.stacks.get(target);
		if (sourcestack.equals(targetstack))
			return new NativeCodeLabel("java", target);
		
		// Otherwise, before a jump is done we need to adapt to the target
		// address!
		throw new todo.TODO();
	}
	
	/**
	 * Makes a label which creates the given exception then throws that
	 * exception.
	 *
	 * @param __cl The class to create.
	 * @return The label for that target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	private final NativeCodeLabel __labelMakeException(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Setup key, the label is the target to jump to after the exception
		// has been generated and a throw is performed
		ByteCodeState state = this.state;
		ClassStackAndLabel key = new ClassStackAndLabel(new ClassName(__cl),
			state.stack, this.__labelException());
		
		// Look in the table to see if we made it before
		Map<ClassStackAndLabel, __EData__> metable = this._metable;
		__EData__ rv = metable.get(key);
		if (rv != null)
			return rv.label;
		
		// Build new data to record this point
		rv = new __EData__(state.addr, state.line,
			new NativeCodeLabel("makeexception", metable.size()));
		metable.put(key, rv);
		
		// Return the created label (where the caller jumps to)
		return rv.label;
	}
	
	/**
	 * If anything has been previously pushed then generate code to clear it.
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
		this.codebuilder.add(NativeInstructionType.REF_CLEAR);
		
		// No need to clear anymore
		this._lastenqueue = null;
	}
	
	/**
	 * Generates code to enqueue registers, if there are any. This implicitly
	 * uses the registers from the state.
	 *
	 * @return True if the push list was not empty.
	 * @since 2019/04/10
	 */
	private final boolean __refPush()
		throws NullPointerException
	{
		return this.__refPush(this.state.result.enqueue());
	}
	
	/**
	 * Generates code to enqueue registers, if there are any.
	 *
	 * @param __r The registers to push.
	 * @return True if the push list was not empty.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	private final boolean __refPush(JavaStackEnqueueList __r)
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
		
		// Generate code to push all the given registers
		this.codebuilder.add(NativeInstructionType.REF_PUSH,
			new RegisterList(__r.registers()));
		this._lastenqueue = __r;
		
		// Did enqueue something
		return true;
	}
}

