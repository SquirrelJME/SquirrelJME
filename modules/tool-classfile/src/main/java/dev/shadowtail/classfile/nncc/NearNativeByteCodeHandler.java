// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.ClassLoadingAdjustments;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.ClassInfoPointer;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.FieldAccessTime;
import dev.shadowtail.classfile.pool.FieldAccessType;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.NotedString;
import dev.shadowtail.classfile.pool.NullPoolEntry;
import dev.shadowtail.classfile.pool.UsedString;
import dev.shadowtail.classfile.pool.VirtualMethodIndex;
import dev.shadowtail.classfile.summercoat.HelperFunction;
import dev.shadowtail.classfile.summercoat.pool.InterfaceClassName;
import dev.shadowtail.classfile.summercoat.register.ExecutablePointer;
import dev.shadowtail.classfile.summercoat.register.IntValueRegister;
import dev.shadowtail.classfile.summercoat.register.InterfaceOfObject;
import dev.shadowtail.classfile.summercoat.register.InterfaceVTIndex;
import dev.shadowtail.classfile.summercoat.register.MemHandleRegister;
import dev.shadowtail.classfile.summercoat.register.PlainRegister;
import dev.shadowtail.classfile.summercoat.register.Register;
import dev.shadowtail.classfile.summercoat.register.RuntimePoolPointer;
import dev.shadowtail.classfile.summercoat.register.TypedRegister;
import dev.shadowtail.classfile.summercoat.register.Volatile;
import dev.shadowtail.classfile.summercoat.register.WideRegister;
import dev.shadowtail.classfile.xlate.ByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeState;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.ExceptionHandlerTransition;
import dev.shadowtail.classfile.xlate.JavaStackEnqueueList;
import dev.shadowtail.classfile.xlate.JavaStackResult;
import dev.shadowtail.classfile.xlate.JavaStackState;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import dev.shadowtail.classfile.xlate.StateOperation;
import dev.shadowtail.classfile.xlate.StateOperations;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.LookupSwitch;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.Pool;

/**
 * This contains the handler for the near native byte code.
 *
 * @since 2019/04/06
 */
public final class NearNativeByteCodeHandler
	implements ByteCodeHandler
{
	/** The package shelves are in, for remapping. */
	public static final BinaryName MLE_SHELF_PACKAGE =
		new BinaryName("cc/squirreljme/jvm/mle");
		
	/** Target packages for shelves to call instead. */
	public static final BinaryName LLE_SHELF_PACKAGE =
		new BinaryName("cc/squirreljme/jvm/summercoat/lle");
	
	/** The jvm functions class. */
	@Deprecated
	public static final ClassName JVMFUNC_CLASS =
		new ClassName("cc/squirreljme/jvm/JVMFunction");
	
	/** State of the byte code. */
	public final ByteCodeState state =
		new ByteCodeState();
	
	/** Used to build native code. */
	protected final NativeCodeBuilder codebuilder =
		new NativeCodeBuilder();
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** Is this method synchronized? */
	protected final boolean issynchronized;
	
	/** Is this an instance method? */
	protected final boolean isinstance;
	
	/** Returning value? */
	protected final boolean isreturn;
	
	/** Returning wide value? */
	protected final boolean isreturnwide;
	
	/** Monitor target register used. */
	protected final int monitortarget;
	
	/** Volatile registers to use. */
	protected final VolatileRegisterStack volatiles;
	
	/** Standard exception handler table. */
	private final Map<ExceptionHandlerTransition, __EData__> _ehtable =
		new LinkedHashMap<>();
	
	/** Queue for exception handling generation. */
	private final Deque<__TransitingExceptionHandler__> _ehTableQueue =
		new LinkedList<>();
	
	/** Made exception table. */
	private final Map<ClassAndLabel, __EData__> _metable =
		new LinkedHashMap<>();
	
	/** Made exception handling queue. */
	private final Deque<__MakeException__> _meTableQueue =
		new LinkedList<>();
	
	/** The returns which have been performed. */
	private final List<JavaStackEnqueueList> _returns =
		new ArrayList<>();
	
	/** Java transition labels. */
	private final Map<StateOperationsAndTarget, __EData__> _transits =
		new LinkedHashMap<>();
	
	/** State operation transits. */
	private final Deque<__StateOpTransit__> _transitQueue =
		new LinkedList<>();
	
	/** Reference clearing and jumping to label. */
	private final Map<EnqueueAndLabel, __EData__> _refcljumps =
		new LinkedHashMap<>();
	
	/** Reference clear jump list. */
	private final Deque<__RefClearJump__> _refClearJumpQueue =
		new LinkedList<>();
	
	/** Last registers enqueued. */
	private JavaStackEnqueueList _lastenqueue;
	
	/** Next reference count/uncount ID number for jump. */
	private int _refclunk;
	
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
		
		this.defaultfieldaccesstime = ((__bc.isInstanceInitializer() ||
			__bc.isStaticInitializer()) ? FieldAccessTime.INITIALIZER :
			FieldAccessTime.NORMAL);
		this.issynchronized = __bc.isSynchronized();
		this.isinstance = __bc.isInstance();
		
		// Determine monitor target register and the volatile base
		int volbase = NativeCode.ARGUMENT_REGISTER_BASE + 2 +
			__bc.maxLocals() + __bc.maxStack();
		this.monitortarget = volbase;
		this.volatiles = new VolatileRegisterStack(volbase + 1);
		
		// Returning values?
		this.isreturn = (__bc.type().returnValue() != null);
		this.isreturnwide = (this.isreturn && __bc.type().returnValue().isWide());
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
		this.__basicCheckNPE(__in.register);
		
		// Must be an array
		if (!__in.isArray())
			this.__basicCheckIsArray(__in.register);
		
		// We already checked the only valid exceptions, so do not perform
		// later handling!
		this.state.canexception = false;
		
		// Read length
		codebuilder.addMemoryOffConst(DataType.INTEGER, true,
			__len.register,
			__in.register, Constants.ARRAY_LENGTH_OFFSET);
		
		// Clear references
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doArrayLoad(DataType __dt,
		JavaStackResult.Input __in, JavaStackResult.Input __dx,
		JavaStackResult.Output __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Cannot be null
		this.__basicCheckNPE(__in.register);
		
		// Must be an array
		if (!__in.isArray())
			this.__basicCheckIsArray(__in.register);
		
		// Check array bounds
		this.__basicCheckArrayBound(__in.register, __dx.register);
		
		// We already checked the only valid exceptions, so do not perform
		// later handling!
		this.state.canexception = false;
		
		// Grab some volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volaip = volatiles.getUnmanaged();
		
		// Determine array index position
		codebuilder.addMathConst(StackJavaType.INTEGER, MathType.MUL,
			__dx.register, __dt.size(), volaip);
		codebuilder.addMathConst(StackJavaType.INTEGER, MathType.ADD,
			volaip, Constants.ARRAY_BASE_SIZE, volaip);
		
		// Use helper function
		if (__dt.isWide())
		{
			// Read memory
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmMemReadLong", "(II)J",
				__in.register, volaip);
			
			// Copy return value
			codebuilder.addCopy(NativeCode.RETURN_REGISTER,
				__v.register);
			codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
				__v.register + 1);
		}
		
		// Use native read
		else
			codebuilder.addMemoryOffReg(__dt, true,
				__v.register, __in.register, volaip);
		
		// Not used anymore
		volatiles.removeUnmanaged(volaip);
		
		// If reading an object reference count up!
		if (__dt == DataType.OBJECT)
			this.__refCount(__v.register);
		
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
		this.__basicCheckNPE(__in.register);
		
		// Must be an array
		if (!__in.isArray())
			this.__basicCheckIsArray(__in.register);
		
		// Check array bounds
		this.__basicCheckArrayBound(__in.register, __dx.register);
		
		// Grab some volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volaip = volatiles.getUnmanaged();
		
		// Determine array index position
		codebuilder.addMathConst(StackJavaType.INTEGER, MathType.MUL,
			__dx.register, __dt.size(), volaip);
		codebuilder.addMathConst(StackJavaType.INTEGER, MathType.ADD,
			volaip, Constants.ARRAY_BASE_SIZE, volaip);
		
		// If we are storing an object....
		int voltemp = -1;
		boolean isobject;
		if ((isobject = __v.type.isObject()))
		{
			// Check if the array type is compatible
			this.__basicCheckArrayStore(__in.register, __v.register);
			
			// Count the object being stored
			this.__refCount(__v.register);
			
			// Read existing object so it can be uncounted later
			voltemp = volatiles.getUnmanaged();
			codebuilder.addMemoryOffReg(DataType.INTEGER, true,
				voltemp, __in.register, volaip);
		}
		
		// We already checked the only valid exceptions, so do not perform
		// later handling!
		this.state.canexception = false;
		
		// Use helper function
		if (__dt.isWide())
		{
			// Write memory
			this.__invokeStatic(InvokeType.STATIC,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmMemWriteLong", "(IIII)V",
				__in.register, volaip, __v.register, __v.register + 1);
		}
		
		// Store value
		else
			codebuilder.addMemoryOffReg(__dt, false,
				__v.register, __in.register, volaip);
		
		// Reference uncount old value
		if (isobject)
		{
			// Uncount old
			this.__refUncount(voltemp);
			
			// Not needed
			volatiles.removeUnmanaged(voltemp);
		}
		
		// No longer used
		volatiles.removeUnmanaged(volaip);
		
		// Clear references
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doCheckCast(ClassName __cl, JavaStackResult.Input __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push reference
		this.__refPush();
		
		// If the value to be checked is null then we do not thrown an
		// exception, we just skip
		NativeCodeLabel nullskip = new NativeCodeLabel("checkcastnull",
			this._refclunk++);
		codebuilder.addIfZero(__v.register, nullskip);
		
		// Add cast check
		this.__basicCheckCCE(__v.register, __cl);
		
		// Null jump goes here
		codebuilder.label(nullskip);
		
		// We already checked the only valid exceptions, so do not perform
		// later handling!
		this.state.canexception = false;
		
		// We do not need to uncount whatever was pushed in because it would
		// be immediately pushed back onto the stack. The counts should only
		// be lowered if ClassCastException is to be thrown. Because otherwise
		// we will just end up collecting things on a normal refclear
		this.__refReset();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/26
	 */
	@Override
	public final void doClassObjectLoad(ClassName __cl,
		JavaStackResult.Output __out)
	{
		this.__loadClassObject(__cl, __out.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/16
	 */
	@Override
	public final void doConvert(StackJavaType __as,
		JavaStackResult.Input __a, StackJavaType __bs,
		JavaStackResult.Output __b)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Doing just a copy
		if (__as == __bs)
		{
			int a = __a.register,
				b = __b.register;
			
			if (__as.isWide())
			{
				codebuilder.addCopy(a, b);
				codebuilder.addCopy(a + 1, b + 1);
			}
			else
				codebuilder.addCopy(a, b);
		}
		
		// Otherwise a conversion
		else
		{
			// Get the software math class for the source type
			ClassName smc = __as.softwareMathClass();
			
			// Invoke converter method (which might be wide)
			if (__as.isWide())
				this.__invokeStatic(InvokeType.SYSTEM, smc,
					"to" + __bs.boxedType(), "(II)" + __bs.signature(),
					__a.register, __a.register + 1);
			else
				this.__invokeStatic(InvokeType.SYSTEM, smc,
					"to" + __bs.boxedType(), "(I)" + __bs.signature(),
					__a.register);
			
			// Read out return value
			int a = NativeCode.RETURN_REGISTER,
				b = __b.register;
			if (__bs.isWide())
			{
				codebuilder.addCopy(a, b);
				codebuilder.addCopy(a + 1, b + 1);
			}
			else
				codebuilder.addCopy(a, b);
		}
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
		{
			codebuilder.addCopy(__in.register, __out.register);
			codebuilder.addCopy(__in.register + 1, __out.register + 1);
		}
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
	@Override
	public final void doFieldGet(FieldReference __fr,
		JavaStackResult.Input __i, JavaStackResult.Output __v)
	{
		// Push references
		this.__refPush();
		
		// The instance register
		MemHandleRegister instance = MemHandleRegister.of(__i.register);
		
		// Cannot be null!
		this.__basicCheckNPE(instance);
		
		// Must be the given class
		if (!__i.isCompatible(__fr.className()))
			this.__basicCheckCCE(instance, __fr.className());
		
		// We already checked the only valid exceptions, so do not perform
		// later handling!
		this.state.canexception = false;
		
		// We need a volatile for the field offset
		try (Volatile<TypedRegister<AccessedField>> fOff =
			this.volatiles.getTyped(AccessedField.class))
		{
			// Read field offset
			NativeCodeBuilder codeBuilder = this.codebuilder;
			codeBuilder.<AccessedField>addPoolLoad(this.__fieldAccess(
				FieldAccessType.INSTANCE, __fr, true), fOff.register);
			
			// Data type used
			DataType dt = DataType.of(__fr.memberType().primitiveType());
			
			// Reading wide value?
			if (dt.isWide())
				codeBuilder.addMemHandleAccess(dt, true,
					WideRegister.of(__v.register, __v.register + 1),
					instance, fOff.register.asIntValue());
			
			// Narrow value
			else
			{
				codeBuilder.addMemHandleAccess(dt, true,
					IntValueRegister.of(__v.register),
					instance, fOff.register.asIntValue());
				
				// Need to count up object?
				if (__fr.memberType().isObject())
					this.__refCount(MemHandleRegister.of(__v.register));
			}
		}
			
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
		this.__basicCheckNPE(ireg);
		
		// Must be the given class
		if (!__i.isCompatible(__fr.className()))
			this.__basicCheckCCE(ireg, __fr.className());
			
		// We already checked the only valid exceptions, so do not perform
		// later handling!
		this.state.canexception = false;
		
		// Get volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volfioff = volatiles.getUnmanaged();
		
		// Read field offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr, false),
			volfioff);
		
		// Data type used
		DataType dt = DataType.of(__fr.memberType().primitiveType());
		
		// If we are storing an object, we need to uncount the value already
		// in this field
		int voltemp = -1;
		boolean isobject;
		if ((isobject = __fr.memberType().isObject()))
		{
			// Count our own reference up
			this.__refCount(__v.register);
			
			// Read the value of the field for later clear
			voltemp = volatiles.getUnmanaged();
			codebuilder.addMemoryOffReg(
				dt, true,
				voltemp, ireg, volfioff);
		}
		
		// Use helper function?
		if (dt.isWide())
		{
			// Write memory
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmMemWriteLong", "(IIII)V",
				ireg, volfioff, __v.register, __v.register + 1);
		}
		
		// Write to memory
		else
			codebuilder.addMemoryOffReg(
				dt, false,
				__v.register, ireg, volfioff);
		
		// If we stored an object, reference count the field after it has
		// been written to
		if (isobject)
		{
			// Uncount
			this.__refUncount(voltemp);
			
			// Not needed
			volatiles.removeUnmanaged(voltemp);
		}
		
		// No longer used
		volatiles.removeUnmanaged(volfioff);
			
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
	 * @since 2019/04/16
	 */
	@Override
	public final void doInstanceOf(ClassName __cl,
		JavaStackResult.Input __v, JavaStackResult.Output __o)
	{
		// Push reference
		this.__refPush();
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volwantcldx = volatiles.getUnmanaged();
		
		// Load desired class index type
		this.__loadClassInfo(__cl, volwantcldx);
		
		// Invoke helper method
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmIsInstance", "(II)I", __v.register, volwantcldx);
		
		// Use result
		this.codebuilder.addCopy(NativeCode.RETURN_REGISTER, __o.register);
		
		// No longer needed
		volatiles.removeUnmanaged(volwantcldx);
		
		// Clear references in the event it was overwritten
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
		// Target class
		ClassName targetClass = __r.handle().outerClass();
		
		// Remap method to LLE implementation of classes?
		if (NearNativeByteCodeHandler.MLE_SHELF_PACKAGE
			.equals(targetClass.inPackage()) &&
			targetClass.toString().endsWith("Shelf"))
		{
			// Map to new class
			__r = new MethodReference(NearNativeByteCodeHandler
				.LLE_SHELF_PACKAGE.resolve(new ClassIdentifier(
					"LLE" + __r.className().simpleName())).toClass(),
				__r.memberName(), __r.memberType(), __r.isInterface());
			
			// Needs to be recycled
			targetClass = __r.handle().outerClass();
		}
		
		// Invoking a helped system call
		else if ("cc/squirreljme/jvm/summercoat/SystemCall".equals(
			targetClass.toString()))
		{
			this.__invokeSysCallHelped(__r, __out, __in);
				
			// Do nothing else
			return;
		}
		
		// Invocation of assembly method?
		else if ("cc/squirreljme/jvm/Assembly".equals(
			targetClass.toString()))
		{
			// Forward
			this.__invokeAssembly(__r.handle().name(),
				__r.handle().descriptor(), __out, __in);
			
			// Do nothing else
			return;
		}
		
		// Code generator
		NativeCodeBuilder codebuilder = this.codebuilder;
		VolatileRegisterStack volatiles = this.volatiles;
		
		// Push references
		this.__refPush();
		
		// Fill in call arguments
		List<Integer> callargs = new ArrayList<>(__in.length * 2);
		for (int i = 0, n = __in.length; i < n; i++)
		{
			// Add the input register
			JavaStackResult.Input in = __in[i];
			callargs.add(in.register);
			
			// But also if it is wide, we need to pass the other one or
			// else the value will be clipped
			if (in.type.isWide())
				callargs.add(in.register + 1);
		}
		
		// Actual arguments to the call
		RegisterList reglist = new RegisterList(callargs);
		
		// If invoking static method, use our helper method
		MethodHandle mh = __r.handle();
		if (__t == InvokeType.STATIC)
		{
			this.__invokeStatic(__t, mh.outerClass(), mh.name(),
				mh.descriptor(), reglist);
		}
		
		// Interface, special, or virtual
		else
		{
			@Deprecated int instReg = __in[0].register;
			PlainRegister objectReg = new PlainRegister(instReg);
			
			// Check that the object is of the given class type and is not null
			this.__basicCheckNPE(instReg);
			
			// Check types if this is not compatible
			if (!__in[0].isCompatible(__r.handle().outerClass()))
				this.__basicCheckCCE(instReg, __r.handle().outerClass());
			
			// Invoking interface method
			if (__t == InvokeType.INTERFACE)
			{
				// Setup temporarily used registers
				try (Volatile<InterfaceOfObject> iOfO =
						volatiles.getInterfaceOfObject();
					Volatile<InterfaceVTIndex> iVti =
						volatiles.getInterfaceVTIndex();
					Volatile<ExecutablePointer> epp =
						volatiles.getExecutablePointer();
					Volatile<RuntimePoolPointer> rpp =
						volatiles.getRuntimePoolPointer())
				{
					// Lookup the interface for a given object, we need this
					// to know which actual class implements the interface so
					// we can load the pool and call into the class
					// This creates a Interface+object.class relationship
					codebuilder.addInterfaceForObject(
						new InterfaceClassName(mh.outerClass()),
						objectReg, iOfO.register);
					
					// The interface method index in the interface table needs
					// to be known in order to know where to grab our pool
					// and method pointers
					codebuilder.addInterfaceVTIndexLookup(
						new InvokedMethod(InvokeType.INTERFACE, mh),
						iOfO.register, iVti.register);
					
					// Load both the pool and target pointer to the method to
					// invoke
					codebuilder.addInterfaceVTLoad(
						iOfO.register, iVti.register,
						epp.register, rpp.register);
						
					// Invoke the given pointer and pool index
					codebuilder.addInterfaceVTLoad(
						iOfO.register, iVti.register,
						epp.register, rpp.register);
					
					// Invoke the interface, return values and exceptions are
					// handled later on
					codebuilder.addInvokePoolAndPointer(
						epp.register, rpp.register, reglist);
				}
				
				// TODO: Old-code, delete this when verified
				/*if (true)
					throw Debugging.todo();
				
				// Load the interface we are looking in
				int voliclass = volatiles.getUnmanaged();
				this.__loadClassInfo(__r.handle().outerClass(), voliclass);
				
				// Load the method index of the volatile method in question
				int volimethdx = volatiles.getUnmanaged();
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new VirtualMethodIndex(__r.handle().outerClass(),
						__r.handle().name(), __r.handle().descriptor()),
					volimethdx);
				
				// Use helper method to find the method pointer to invoke
				// for this interface (hi=pool, lo=pointer)
				this.__invokeStatic(InvokeType.SYSTEM,
					NearNativeByteCodeHandler.JVMFUNC_CLASS,
					"jvmInterfacePointer", "(III)J",
					instReg, voliclass, volimethdx);
				
				// We need to extract the pool pointer of the class we
				// are calling in so that nothing is horribly incorrect
				codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
					NativeCode.NEXT_POOL_REGISTER);
				
				// Invoke the pointer that this method returned
				codebuilder.add(NativeInstructionType.INVOKE,
					NativeCode.RETURN_REGISTER, reglist);
				
				// Cleanup
				volatiles.removeUnmanaged(voliclass);
				volatiles.removeUnmanaged(volimethdx);*/
			}
			
			// Special or virtual
			else
			{
				// Invoke instance method
				this.__invokeInstance(__t, mh.outerClass(), mh.name(),
					mh.descriptor(), reglist);
			}
		}
		
		// Check if exception occurred, before copying the return value!
		if (this.state.canexception)
		{
			// Exception check
			codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
				this.__labelException());
			
			// We did the exception handling, so do not handle later
			this.state.canexception = false;
		}
		
		// Read in return value, it is just a copy
		if (__out != null)
		{
			int a = NativeCode.RETURN_REGISTER,
				b = __out.register;
			
			if (__out.type.isWide())
				codebuilder.addCopy(a + 1, b + 1);
			codebuilder.addCopy(a, b);
		}
		
		// Clear references
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/16
	 */
	@Override
	public final void doLookupSwitch(JavaStackResult.Input __key,
		LookupSwitch __ls)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// The key register
		int keyreg = __key.register;
		
		// Generate checks for all keys
		int[] keys = __ls.keys();
		InstructionJumpTarget[] jumps = __ls.jumps();
		for (int i = 0, n = keys.length; i < n; i++)
			codebuilder.add(NativeInstructionType.IFEQ_CONST,
				keyreg, keys[i], this.__labelJava(jumps[i]));
		
		// Final case is the default jump
		codebuilder.addGoto(this.__labelJava(__ls.defaultJump()));
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
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Integer math is supported natively
		if (__dt == StackJavaType.INTEGER)
		{
			// Check for division by zero, only the integer type can have this
			// done in code because the long can be handled by the software
			// math library code. Otherwise we would need to add more code to
			// the generator to handle this.
			if (__mt == MathType.DIV || __mt == MathType.REM)
			{
				// Perform divide by zero check
				this.__basicCheckDBZ(__b.register);
				
				// We already checked the only valid exceptions, so do not
				// perform later handling!
				this.state.canexception = false;
			}
			
			// Add math operation
			codebuilder.addMathReg(__dt, __mt, __a.register, __b.register,
				__c.register);
		}
		
		// Other kinds of math are done in software
		else
		{
			// Get the software math class for the type
			ClassName smc = __dt.softwareMathClass();
			
			// The function to call is just the lowercased enum
			String func = __mt.name().toLowerCase();
			
			// Remove the L/G from compare as that is only for float/double
			if (__dt == StackJavaType.LONG && func.startsWith("cmp"))
				func = "cmp";
			
			// Handling wide math?
			boolean iswide = __dt.isWide();
			
			// A, B, and C register
			int ah = __a.register,
				bh = __b.register,
				ch = __c.register;
			
			// Low registers
			int al = (ah == 0 ? 0 : ah + 1),
				bl = (bh == 0 ? 0 : bh + 1),
				cl = (ch == 0 ? 0 : ch + 1);
			
			// Determine the call signature
			String type = __mt.signature(__dt);
			RegisterList args;
			switch (__mt)
			{
				case NEG:
				case SIGNX8:
				case SIGNX16:
					if (iswide)
						args = new RegisterList(ah, al);
					else
						args = new RegisterList(ah);
					break;
				
				case SHL:
				case SHR:
				case USHR:
					if (iswide)
						args = new RegisterList(ah, al, bh);
					else
						args = new RegisterList(ah, bh);
					break;
				
				default:
					if (iswide)
						args = new RegisterList(ah, al, bh, bl);
					else
						args = new RegisterList(ah, bh);
					break;
			}
			
			// Perform the call
			this.__invokeStatic(InvokeType.SYSTEM, smc.toString(),
				func, type, args);
			
			// Read out return value
			if (iswide)
			{
				codebuilder.addCopy(NativeCode.RETURN_REGISTER, ch);
				codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1, cl);
			}
			else
				codebuilder.addCopy(NativeCode.RETURN_REGISTER, ch);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, Number __b, JavaStackResult.Output __c)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// If we are dividing by zero just throw an exception
		if ((__dt == StackJavaType.INTEGER || __dt == StackJavaType.LONG) &&
			(__mt == MathType.DIV || __mt == MathType.REM))
			if (__b.longValue() == 0)
			{
				// Directly jump to the make exception handler
				codebuilder.addGoto(this.__labelMakeException(
					"java/lang/ArithmeticException"));
				
				// Already handled this
				this.state.canexception = false;
				
				// Since we are dividing by zero, never actually generate the
				// division code
				return;
			}
		
		// Integer math on constants is natively supported
		if (__dt == StackJavaType.INTEGER)
		{
			codebuilder.addMathConst(__dt, __mt, __a.register, __b,
				__c.register);
		}
		
		// Otherwise store the constant and then do register math on it
		else
		{
			// Need working registers, these must be next to each other!
			VolatileRegisterStack volatiles = this.volatiles;
			int volbh = -1, volbl = -7;
			while (volbl != (volbh + 1))
			{
				volbh = volatiles.getUnmanaged();
				volbl = volatiles.getUnmanaged();
			}
			
			// Read in raw value
			if (__b instanceof Float)
				codebuilder.addMathConst(StackJavaType.INTEGER, MathType.OR,
					0, Float.floatToRawIntBits(__b.floatValue()), volbh);
			else if (__b instanceof Double)
			{
				long bits = Double.doubleToRawLongBits(__b.doubleValue());
				codebuilder.addMathConst(StackJavaType.INTEGER, MathType.OR,
					0, (int)(bits >>> 32), volbh);
				codebuilder.addMathConst(StackJavaType.INTEGER, MathType.OR,
					0, (int)(bits), volbl);
			}
			else
			{
				long bits = __b.longValue();
				codebuilder.addMathConst(StackJavaType.INTEGER, MathType.OR,
					0, (int)(bits >>> 32), volbh);
				codebuilder.addMathConst(StackJavaType.INTEGER, MathType.OR,
					0, (int)(bits), volbl);
			}
			
			// Same as register math except the constant value is
			// virtualized now
			this.doMath(__dt, __mt,
				__a, new JavaStackResult.Input(volbh, JavaType.INTEGER, true),
				__c);
			
			// Cleanup
			volatiles.removeUnmanaged(volbh);
			volatiles.removeUnmanaged(volbl);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/16
	 */
	@Override
	public final void doMonitor(boolean __enter, JavaStackResult.Input __o)
	{
		// Push reference
		this.__refPush();
		
		// Generate instruction
		this.__monitor(__enter, __o.register);
		
		// Clear reference
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/04
	 */
	@Override
	public final void doMultiANewArray(ClassName __cl, int __numdims,
		JavaStackResult.Output __o, JavaStackResult.Input... __dims)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volclassobj = volatiles.getUnmanaged();
		
		// Load the class we want to allocate
		this.__loadClassObject(__cl, volclassobj);
		
		// Determine the number of integer arguments to use, since we
		// are passing multiple arguments to multianewarray
		StringBuilder sb = new StringBuilder(__numdims);
		for (int i = 0; i < __numdims; i++)
			sb.append('I');
			
		// Build arguments to the method call (class, skip, args);
		List<Integer> rl = new ArrayList<>();
		rl.add(volclassobj);
		rl.add(0);
		for (int i = 0; i < __numdims; i++)
			rl.add(__dims[i].register);
		
		// Invoke array utility
		this.__invokeStatic(InvokeType.SYSTEM,
			"cc/squirreljme/runtime/cldc/lang/ArrayUtils", "multiANewArray",
			"(Ljava/lang/Class;I" + sb + ")Ljava/lang/Object;",
			new RegisterList(rl));
		
		// Use this result
		codebuilder.addCopy(NativeCode.RETURN_REGISTER, __o.register);
		
		// Not needed anymore
		volatiles.removeUnmanaged(volclassobj);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doNew(ClassName __cn, JavaStackResult.Output __out)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need result register
		VolatileRegisterStack volatiles = this.volatiles;
		int volresult = volatiles.getUnmanaged();
		
		// Perform new invocation
		this.__invokeNew(__cn, volresult);
		
		// Check for out of memory
		this.__basicCheckOOM(volresult);
		
		// All the exceptions were checked
		this.state.canexception = false;
		
		// Copy to result
		codebuilder.addCopy(volresult, __out.register);
		
		// Not needed
		volatiles.removeUnmanaged(volresult);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doNewArray(ClassName __at,
		JavaStackResult.Input __len, JavaStackResult.Output __out)
	{
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Check if the array is negative, fail here if so
		this.__basicCheckNAS(__len.register);
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		try (Volatile<TypedRegister<ClassInfoPointer>> classInfo =
			volatiles.getTyped(ClassInfoPointer.class))
		{
			// Load the class information for the register
			this.__loadClassInfo(__at, classInfo.register);
			
			// Call the helper for the method
			this.__invokeHelper(HelperFunction.NEW_ARRAY,
				classInfo.register, PlainRegister.of(__len.register));
			
			// Copy the result to the output
			codeBuilder.<MemHandleRegister>addCopy(
				MemHandleRegister.RETURN,
				MemHandleRegister.of(__out.register));
		}
		
		// Negative array and invocation exceptions were checked.
		this.state.canexception = false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doPoolLoadString(String __v,
		JavaStackResult.Output __out)
	{
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Load the string from the pool, it is already a String object here
		// and we do not need to do anything for it at all. It will be interned
		// here always.
		TypedRegister<UsedString> out = TypedRegister.<UsedString>of(
			UsedString.class, __out.register);
		codeBuilder.addPoolLoad(new UsedString(__v), out);
		
		// Count it up so it is not instantly GCed, unless it is marked as
		// not being counted
		if (!__out.nocounting)
			this.__refCount(out.asMemHandle());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doReturn(JavaStackResult.Input __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// If we are returning an object, we need to reference count it up
		// so it does not get garbage collected as a return is happening
		if (__in != null && __in.isObject())
			this.__refCount(__in.register);
		
		// Uncount all references that need to be cleared out as we return
		for (int q : this.state.result.enqueue())
			this.__refUncount(q);
		
		// Copy the returning value at the end to increase it's lifetime
		// as much as possible. Otherwise if we copy too early to the return
		// register then future uncounts can completely trash the value
		if (__in != null)
		{
			int a = __in.register,
				b = NativeCode.RETURN_REGISTER;
			
			// Copy value to return register
			if (__in.type.isWide())
				codebuilder.addCopy(a + 1, b + 1);
			codebuilder.addCopy(a, b);
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
		
		// If there are no operations, just do not bother
		if (__ops.isEmpty())
			return;
		
		// Generate code for the operations
		NativeCodeBuilder codebuilder = this.codebuilder;
		for (StateOperation op : __ops)
			switch (op.type)
			{
				case UNCOUNT:
					this.__refUncount(op.a);
					break;
				
				case COUNT:
					this.__refCount(op.a);
					break;
				
				case COPY:
					codebuilder.addCopy(op.a, op.b);
					break;
				
				case WIDE_COPY:
					codebuilder.addCopy(op.a, op.b);
					codebuilder.addCopy(op.a + 1, op.b + 1);
					break;
				
				default:
					throw Debugging.oops();
			}
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
		
		// Ignore thrown exceptions because field access is checked at link
		// time
		this.state.canexception = false;
		
		// Push references
		this.__refPush();
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volsfo = volatiles.getUnmanaged();
		
		// Read static offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.STATIC, __fr, true),
			volsfo);
		
		// The datatype used
		DataType dt = DataType.of(__fr.memberType().primitiveType());
		
		// Use helper function?
		if (dt.isWide())
		{
			// Read memory
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmMemReadLong", "(II)J",
				NativeCode.STATIC_FIELD_REGISTER, volsfo);
			
			// Copy return value
			codebuilder.addCopy(NativeCode.RETURN_REGISTER,
				__v.register);
			codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
				__v.register + 1);
		}
		
		// Read from memory
		else
			codebuilder.addMemoryOffReg(
				dt, true,
				__v.register, NativeCode.STATIC_FIELD_REGISTER, volsfo);
		
		// Count it up?
		if (__fr.memberType().isObject())
			this.__refCount(__v.register);
		
		// Not needed
		volatiles.removeUnmanaged(volsfo);
			
		// Clear references as needed
		this.__refClear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/13
	 */
	@Override
	public final void doStaticPut(FieldReference __fr,
		JavaStackResult.Input __v)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Ignore thrown exceptions because field access is checked at link
		// time
		this.state.canexception = false;

		// Push references
		this.__refPush();
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volsfo = volatiles.getUnmanaged();
		
		// Read field offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.STATIC, __fr, false),
			volsfo);
		
		// Data type used
		DataType dt = DataType.of(__fr.memberType().primitiveType());
		
		// If we are storing an object, we need to uncount the value already
		// in this field
		int voltemp = -1;
		boolean isobject;
		if ((isobject = __fr.memberType().isObject()))
		{
			// Count our own reference up
			this.__refCount(__v.register);
			
			// Read the value of the field for later clear
			voltemp = volatiles.getUnmanaged();
			codebuilder.addMemoryOffReg(
				dt, true,
				voltemp, NativeCode.STATIC_FIELD_REGISTER, volsfo);
		}
		
		// Use helper function?
		if (dt.isWide())
		{
			// Write memory
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmMemWriteLong", "(IIII)V",
				NativeCode.STATIC_FIELD_REGISTER, volsfo,
				__v.register, __v.register + 1);
		}
		
		// Write to memory
		else
			codebuilder.addMemoryOffReg(
				dt, false,
				__v.register, NativeCode.STATIC_FIELD_REGISTER, volsfo);
		
		// If we wrote an object, uncount the old destination after it
		// has been overwritten
		if (isobject)
		{
			// Uncount
			this.__refUncount(voltemp);
			
			// Not needed
			volatiles.removeUnmanaged(voltemp);
		}
		
		// Not needed
		volatiles.removeUnmanaged(volsfo);
		
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
		this.__basicCheckNPE(__in.register);
		
		// Copy into the exception register
		codebuilder.addCopy(__in.register, NativeCode.EXCEPTION_REGISTER);
		
		// Clear the reference queue because this results in a net reference
		// count if this point is reached, however this is still needed for
		// the NPE check even though null values will never be counted
		this.__refReset();
		
		// Do not jump at this point, just return the exception check will be
		// flagged which will start the exception handling
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void instructionFinish()
	{
		ByteCodeState state = this.state;
		
		// {@squirreljme.error JC12 Enqueues were not cleared, this is an
		// internal compiler error.}
		if (this._lastenqueue != null)
			throw new IllegalStateException("JC12");
		
		// An exception check was requested, do a check on the exception
		// register and jump if there is something there
		if (state.canexception)
			this.codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
				this.__labelException());
		
		// If this instruction naturally flows into another, determine we
		// need to transition to that stack state in order to work properly
		// with any cached values
		if (state.instruction.hasNaturalFlow())
		{
			// Get following stack
			int followaddr = state.followaddr;
			JavaStackState nowstack = state.stack,
				followstack = state.stacks.get(followaddr);
			
			// If our current stack is not compatible with the target stack
			// then we need to transition to that state. However the Java label
			// code already handles transition, so do this to remove duplicate
			// code with just a minor jump around.
			// However if the next state is poisoned we do not do this because
			// we will just naturally transition to a pure de-cache at the
			// start of everything anyway.
			if (!nowstack.equals(followstack) &&
				!state.stackpoison.containsKey(followaddr))
				this.codebuilder.addGoto(
					this.__labelJava(new InstructionJumpTarget(followaddr)));
		}
		
		// Clear volatiles
		this.volatiles.clear();
	}
	
	/**
	 * Sets up before processing the instruction.
	 *
	 * @since 2019/04/07
	 */
	@Override
	public final void instructionSetup()
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		ByteCodeState state = this.state;
		int addr = state.addr;
		
		// Clear volatiles
		this.volatiles.clear();
		
		// Entry point debugging
		if (addr == 0)
		{
			// Debug entry point
			codebuilder.add(NativeInstructionType.DEBUG_ENTRY,
				new NotedString(state.classname.toString()),
				new NotedString(state.methodname.toString()),
				new NotedString(state.methodtype.toString()),
				new NullPoolEntry());
			
			// Setup monitor entry
			if (this.issynchronized)
			{
				// Copy instance to monitor target
				if (this.isinstance)
					codebuilder.addCopy(state.stack.getLocal(0).register,
						this.monitortarget);
				
				// Load class object to monitor
				else
					this.__loadClassObject(state.classname,
						this.monitortarget);
				
				// Enter monitor on this
				this.__refCount(this.monitortarget);
				this.__monitor(true, this.monitortarget);
			}
		}
		
		// Debug single instruction point
		codebuilder.add(NativeInstructionType.DEBUG_POINT,
			state.line & 0x7FFF, state.instruction.operation() & 0xFF,
			state.instruction.address() & 0x7FFF);
		
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
		List<JavaStackEnqueueList> returns = this._returns;
		VolatileRegisterStack volatiles = this.volatiles;
		
		// Queues for various exception handlers
		Deque<__TransitingExceptionHandler__> ehQueue = this._ehTableQueue;
		Deque<__RefClearJump__> refClQueue = this._refClearJumpQueue;
		Deque<__MakeException__> makeExQueue = this._meTableQueue;
		Deque<__StateOpTransit__> transitQueue = this._transitQueue;
		
		// Keep generating these late label transitions and otherwise until
		// they are completely drained out. It is possible some exception
		// handlers may generated special handling calls, in which case they
		// can happen while they are being handled.
		int cycleCount = 0;
		do
		{
			// Extra cycles processed?
			if (++cycleCount > 1)
			{
				// Note the cycle
				Debugging.debugNote("Generation Cycle: %d", cycleCount);
				
				// Dump what is remaining in the cycles, for debugging
				Debugging.debugNote("ehQueue: %s", ehQueue);
				Debugging.debugNote("refClQueue: %s", refClQueue);
				Debugging.debugNote("makeExQueue: %s", makeExQueue);
				Debugging.debugNote("transitQueue: %s", transitQueue);
			}
			
			// Generate reference clear jumps
			while (!refClQueue.isEmpty())
			{
				__RefClearJump__ refClearJump = refClQueue.remove();
				
				EnqueueAndLabel eql = refClearJump._enqueueAndLabel;
				__EData__ eData = refClearJump._eData;
				
				// Set label target for this one
				codebuilder.label(this.__useEDataAndGetLabel(eData));
				this.__useEDataDebugPoint(eData, 256);
				
				// Clear references
				JavaStackEnqueueList enq = eql.enqueue;
				for (int i = 0, n = enq.size(); i < n; i++)
					this.__refUncount(enq.get(i));
				
				// Then go to the target
				codebuilder.addGoto(eql.label);
			}
			
			// Generate make exception code
			while (!makeExQueue.isEmpty())
			{
				__MakeException__ makeException = makeExQueue.remove();
				
				ClassAndLabel csl = makeException._classAndLabel;
				__EData__ eData = makeException._eData;
				
				// Set label target for this one
				codebuilder.label(this.__useEDataAndGetLabel(eData));
				this.__useEDataDebugPoint(eData, 257);
				
				// Allocate class object, make sure it is not done in the exception
				// register because the method we call may end up just clearing it
				// and stopping if a make exception is done (because there is an
				// exception here).
				int exinst = volatiles.getUnmanaged();
				this.__invokeNew(csl.classname, exinst);
				
				// Initialize the exception
				this.__invokeInstance(InvokeType.SPECIAL, csl.classname, "<init>",
					"()V", new RegisterList(exinst));
				
				// Copy result into the exception register
				codebuilder.addCopy(exinst, NativeCode.EXCEPTION_REGISTER);
				
				// Done with this
				volatiles.removeUnmanaged(exinst);
				
				// Generate jump to exception handler
				codebuilder.addGoto(csl.label);
			}
			
			// Generate exception handler tables
			while (!ehQueue.isEmpty())
			{
				__TransitingExceptionHandler__ transit = ehQueue.remove();
				
				ExceptionHandlerTransition ehtran = transit._transition;
				__EData__ eData = transit._eData;
				
				StateOperations sops = ehtran.handled;
				JavaStackEnqueueList enq = ehtran.nothandled;
				ExceptionHandlerTable ehtable = ehtran.table;
				
				// Label used for the jump target
				NativeCodeLabel lab = this.__useEDataAndGetLabel(eData);
				this.__useEDataDebugPoint(eData, 258);
				
				// If the table is empty, just return
				if (ehtable.isEmpty())
				{
					// If we never saw a cleanup for this return yet we can
					// generate one here to be used for later points
					int rdx = returns.indexOf(enq);
					if (rdx < 0)
						codebuilder.label(lab,
							codebuilder.labelTarget(this.__generateReturn(enq)));
					
					// We can just alias this exception to the return point to
					// cleanup everything
					else
						codebuilder.label(lab,
							codebuilder.labelTarget("return", rdx));
					
					// Generate the next handler
					continue;
				}
				
				// Set label target for this one
				codebuilder.label(lab);
				
				// Go through and build the exception handler table
				for (ExceptionHandler eh : ehtable)
				{
					// Load the class type for the exception to check against
					int volehclassdx = volatiles.getUnmanaged();
					this.__loadClassInfo(eh.type(), volehclassdx);
					
					// Call instance handler check
					this.__invokeStatic(InvokeType.SYSTEM,
						NearNativeByteCodeHandler.JVMFUNC_CLASS,
						"jvmIsInstance", "(II)I",
						NativeCode.EXCEPTION_REGISTER, volehclassdx);
					
					// Cleanup
					volatiles.removeUnmanaged(volehclassdx);
					
					// If the return value is non-zero then it is an instance, in
					// which case we jump to the handler address.
					codebuilder.addIfNonZero(NativeCode.RETURN_REGISTER,
						this.__labelJavaTransition(sops,
							new InstructionJumpTarget(eh.handlerAddress())));
				}
				
				// No exception handler is available so, just fall through to the
				// caller as needed
				this.__generateReturn(enq);
			}
			
			// Generate transition labels
			while (!transitQueue.isEmpty())
			{
				__StateOpTransit__ transit = transitQueue.remove();
				
				StateOperationsAndTarget sot = transit._stateOpAndTarget;
				StateOperations ops = sot.operations;
				InstructionJumpTarget target = sot.target;
				
				__EData__ eData = transit._eData;
				
				// Set label target for this one
				codebuilder.label(this.__useEDataAndGetLabel(eData));
				this.__useEDataDebugPoint(eData, 259);
				
				// Generate operations
				this.doStateOperations(ops);
				
				// Then just jump to the Java target
				codebuilder.addGoto(new NativeCodeLabel("java",
					target.target()));
			}
		} while (!ehQueue.isEmpty() || !refClQueue.isEmpty() ||
			!makeExQueue.isEmpty() || !transitQueue.isEmpty());
		
		return codebuilder.build();
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
	 * Checks if an array access is within bounds.
	 *
	 * @param __ir The instance register.
	 * @param __dxr The index register.
	 * @since 2019/04/27
	 */
	private void __basicCheckArrayBound(int __ir, int __dxr)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// This label is shared across many conditions
		NativeCodeLabel lab = this.__labelMakeException(
			"java/lang/ArrayIndexOutOfBoundsException");
		
		// If the index is negative then it is out of bounds
		codebuilder.addIfICmp(CompareType.LESS_THAN, __dxr, 0, lab);
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volarraylen = volatiles.getUnmanaged();
		
		// Read length of array
		codebuilder.addMemoryOffConst(DataType.INTEGER, true,
			volarraylen,
			__ir, Constants.ARRAY_LENGTH_OFFSET);
		
		// If the index is greater or equal to the length then the access
		// is invalid
		codebuilder.addIfICmp(CompareType.GREATER_THAN_OR_EQUALS,
			__dxr, volarraylen, lab);
		
		// No longer needed
		volatiles.removeUnmanaged(volarraylen);
	}
	
	/**
	 * Checks if the target array can store this value.
	 *
	 * @param __ir The instance register.
	 * @param __vr The value register.
	 * @since 2019/04/27
	 */
	private void __basicCheckArrayStore(int __ir, int __vr)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Call helper class
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmCanArrayStore", "(II)I", __ir, __vr);
		
		// Was it invalid?
		codebuilder.addIfZero(NativeCode.RETURN_REGISTER,
			this.__labelMakeException("java/lang/ArrayStoreException"));
	}
	
	/**
	 * Basic check if the instance is of the given class.
	 *
	 * @param __ir The register to check.
	 * @param __cl The class to check.
	 * @deprecated Use the type-safe
	 * {@link #__basicCheckNPE(MemHandleRegister)} instead. 
	 * @since 2019/04/22
	 */
	@Deprecated
	private void __basicCheckCCE(int __ir, ClassName __cl)
		throws NullPointerException
	{
		this.__basicCheckCCE(MemHandleRegister.of(__ir), __cl);
	}
	
	/**
	 * Basic check if the instance is of the given class.
	 *
	 * @param __ir The register to check.
	 * @param __cl The class to check.
	 * @since 2020/11/28
	 */
	private void __basicCheckCCE(MemHandleRegister __ir, ClassName __cl)
		throws NullPointerException
	{
		if (__ir == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Need volatiles
		try (Volatile<TypedRegister<ClassInfoPointer>> classInfo =
			this.volatiles.getTyped(ClassInfoPointer.class))
		{
			// Load desired target class type
			this.__loadClassInfo(__cl, classInfo.register);
			
			// Call helper class
			this.__invokeHelper(HelperFunction.IS_INSTANCE,
				__ir, classInfo.register);
			
			// If the resulting method call returns zero then it is not an
			// instance of the given class. The return register is checked
			// because the value of that method will be placed there.
			this.codebuilder.addIfZero(NativeCode.RETURN_REGISTER,
				this.__labelRefClearJump(this.__labelMakeException(
				"java/lang/ClassCastException")));
		}
	}
	
	/**
	 * Checks for divide by zero.
	 *
	 * @param __br The B register.
	 * @since 2019/06/24
	 */
	private void __basicCheckDBZ(int __br)
	{
		// If the B register is zero, then we throw the exception
		this.codebuilder.addIfZero(__br, this.__labelMakeException(
			"java/lang/ArithmeticException"));
	}
	
	/**
	 * Checks that the given object is an array.
	 *
	 * @param __ir The type to check.
	 * @since 2019/04/27
	 */
	private void __basicCheckIsArray(int __ir)
	{
		// Call internal helper
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS, "jvmIsArray",
			"(I)I", __ir);
		
		// If this is not an array, throw a class cast exception
		this.codebuilder.addIfZero(NativeCode.RETURN_REGISTER,
			this.__labelRefClearJump(this.__labelMakeException(
			"java/lang/ClassCastException")));
	}
	
	/**
	 * Checks if the requested array allocation is negative.
	 *
	 * @param __lr The length register.
	 * @since 2019/06/28
	 */
	private void __basicCheckNAS(int __lr)
	{
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Check against less than zero
		codeBuilder.addIfICmp(CompareType.LESS_THAN,
			__lr, NativeCode.ZERO_REGISTER, this.__labelRefClearJump(
			this.__labelMakeException(
				"java/lang/NegativeArraySizeException")));
	}
	
	/**
	 * Basic check if the instance is null.
	 *
	 * @param __ir The register to check.
	 * @deprecated Use the type safe {@link #__basicCheckNPE(
	 * MemHandleRegister)}. 
	 * @since 2019/04/22
	 */
	@Deprecated
	private void __basicCheckNPE(int __ir)
	{
		this.__basicCheckNPE(MemHandleRegister.of(__ir));
	}
	
	/**
	 * Basic check if the instance is null.
	 *
	 * @param __ir The register to check.
	 * @since 2020/11/28
	 */
	private void __basicCheckNPE(MemHandleRegister __ir)
		throws NullPointerException
	{
		if (__ir == null)
			throw new NullPointerException("NARG");
		
		this.codebuilder.addIfNull(__ir, this.__labelRefClearJump(
			this.__labelMakeException("java/lang/NullPointerException"))); 
	}
	
	/**
	 * Checks if the given allocation ran out of memory.
	 *
	 * @param __ir The register to check.
	 * @since 2019/06/28
	 */
	private void __basicCheckOOM(int __ir)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Just a plain zero check
		codebuilder.addIfZero(__ir, this.__labelRefClearJump(
			this.__labelMakeException("java/lang/OutOfMemoryError")));
	}
	
	/**
	 * Makes an EData for the current position and label.
	 *
	 * @param __lab The stored label.
	 * @return The made EData.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/26
	 */
	private __EData__ __eData(NativeCodeLabel __lab)
		throws NullPointerException
	{
		if (__lab == null)
			throw new NullPointerException("NARG");
		
		ByteCodeState state = this.state;
		return new __EData__(state.addr, state.line,
			state.instruction.address(), state.instruction.operation(), __lab);
	}
	
	/**
	 * Generates an access to a field.
	 *
	 * @param __at The type of access to perform.
	 * @param __fr The reference to the field.
	 * @param __read Is a read being performed?
	 * @return The accessed field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private AccessedField __fieldAccess(FieldAccessType __at,
		FieldReference __fr, boolean __read)
		throws NullPointerException
	{
		if (__at == null || __fr == null)
			throw new NullPointerException("NARG");
		
		// Accessing final fields of another class will always be treated as
		// normal despite being in the constructor of a class
		if (!this.state.classname.equals(__fr.className()))
			return new AccessedField((__read ? FieldAccessTime.READ :
				FieldAccessTime.NORMAL), __at, __fr);
		return new AccessedField((__read ? FieldAccessTime.READ :
			this.defaultfieldaccesstime), __at, __fr);
	}
	
	/**
	 * Generates or jumps to another return point.
	 *
	 * @return The label to this return point.
	 * @since 2019/04/11
	 */
	private NativeCodeLabel __generateReturn()
	{
		return this.__generateReturn(this.state.stack.possibleEnqueue());
	}
	
	/**
	 * Generates or jumps to another return point for the given enqueue.
	 *
	 * @param __eq The enqueue to return for.
	 * @throws NullPointerException On null arguments.
	 * @return The label to this return point.
	 * @since 2019/04/11
	 */
	private NativeCodeLabel __generateReturn(JavaStackEnqueueList __eq)
		throws NullPointerException
	{
		if (__eq == null)
			throw new NullPointerException("NARG");
		
		// Will be used to generate safe spots
		VolatileRegisterStack volatiles = this.volatiles;
		int ssh = -1,
			ssl = -1;
		
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
			this.codebuilder.label(lb);
		
		// If the enqueue list is empty then the only thing we need to do
		// is generate a return instruction
		NativeCodeBuilder codebuilder = this.codebuilder;
		if (__eq.isEmpty())
		{
			// If this is synchronized, we need to exit the monitor
			if (this.issynchronized)
			{
				// Protect return value, if there is one
				if (this.isreturn)
				{
					ssh = volatiles.getUnmanaged();
					codebuilder.addCopy(NativeCode.RETURN_REGISTER, ssh);
				}
				
				// And wide value, if any
				if (this.isreturnwide)
				{
					ssl = volatiles.getUnmanaged();
					codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1, ssl);
				}
				
				// Uncount and clear out
				this.__monitor(false, this.monitortarget);
				this.__refUncount(this.monitortarget);
				
				// Recover value, if any
				if (this.isreturn)
				{
					codebuilder.addCopy(ssh, NativeCode.RETURN_REGISTER);
					volatiles.removeUnmanaged(ssh);
				}
				
				// Recover wide, if any
				if (this.isreturnwide)
				{
					codebuilder.addCopy(ssl, NativeCode.RETURN_REGISTER + 1);
					volatiles.removeUnmanaged(ssl);
				}
			}
			
			// Debug exit
			codebuilder.add(NativeInstructionType.DEBUG_EXIT);
			
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
		
		// Protect return value, if there is one
		if (this.isreturn)
		{
			ssh = volatiles.getUnmanaged();
			codebuilder.addCopy(NativeCode.RETURN_REGISTER, ssh);
		}
		
		// And wide value, if any
		if (this.isreturnwide)
		{
			ssl = volatiles.getUnmanaged();
			codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1, ssl);
		}
		
		// Since the enqueue list is not empty, we can just trim a register
		// from the top and recursively go down
		// So uncount the top
		this.__refUncount(__eq.top());
		
		// Recover value, if any
		if (this.isreturn)
		{
			codebuilder.addCopy(ssh, NativeCode.RETURN_REGISTER);
			volatiles.removeUnmanaged(ssh);
		}
		
		// Recover wide, if any
		if (this.isreturnwide)
		{
			codebuilder.addCopy(ssl, NativeCode.RETURN_REGISTER + 1);
			volatiles.removeUnmanaged(ssl);
		}
		
		// Recursively go down since the enqueues may possibly be shared, if
		// any of these enqueues were previously made then the recursive
		// call will just make a goto
		this.__generateReturn(__eq.trimTop());
		
		// Note that we do not return the recursive result because that
		// will be for another enqueue state
		return lb;
	}
	
	/**
	 * Invokes an {@link Assembly} method.
	 *
	 * @param __name The method name.
	 * @param __type The method type.
	 * @param __out The result.
	 * @param __in The input.
	 * @throws NullPointerException If no name or type were specified.
	 * @since 2019/05/24
	 */
	private void __invokeAssembly(MethodName __name,
		MethodDescriptor __type, JavaStackResult.Output __out,
		JavaStackResult.Input... __in)
		throws NullPointerException
	{
		if (__name == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Code generator
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Force exception cancel for these operations
		this.state.canexception = false;
		
		// Depends on the assembly function
		String asmfunc;
		switch ((asmfunc = __name.toString()))
		{
				// Read lenght of array
			case "arrayLength":
				this.doArrayLength(__in[0], __out);
				break;
				
				// Atomic compare, get and set
			case "atomicCompareGetAndSet":
				codeBuilder.add(
					NativeInstructionType.ATOMIC_COMPARE_GET_AND_SET,
						__in[0].register,
						__out.register,
						__in[1].register,
						__in[2].register,
						0);
				break;
				
				// Atomic decrement and get
			case "atomicDecrementAndGet":
				codeBuilder.add(
					NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET,
						__out.register,
						__in[0].register,
						0);
				break;
				
				// Atomic increment
			case "atomicIncrement":
				codeBuilder.add(
					NativeInstructionType.ATOMIC_INT_INCREMENT,
						__in[0].register,
						0);
				break;
				
				// Breakpoint
			case "breakpoint":
				codeBuilder.add(NativeInstructionType.BREAKPOINT);
				break;
				
				// Load boolean class
			case "classInfoOfBoolean":
				this.__loadClassInfo("boolean", __out.register);
				break;
				
				// Load byte class
			case "classInfoOfByte":
				this.__loadClassInfo("byte", __out.register);
				break;
				
				// Load short class
			case "classInfoOfShort":
				this.__loadClassInfo("short", __out.register);
				break;
				
				// Load character class
			case "classInfoOfCharacter":
				this.__loadClassInfo("char", __out.register);
				break;
				
				// Load int class
			case "classInfoOfInteger":
				this.__loadClassInfo("int", __out.register);
				break;
				
				// Load float class
			case "classInfoOfFloat":
				this.__loadClassInfo("float", __out.register);
				break;
				
				// Load long class
			case "classInfoOfLong":
				this.__loadClassInfo("long", __out.register);
				break;
				
				// Load double class
			case "classInfoOfDouble":
				this.__loadClassInfo("double", __out.register);
				break;
				
				// Long/Double bits
			case "doubleToRawLongBits":
			case "longBitsToDouble":
				if (__in[0].register != __out.register)
				{
					int a = __in[0].register,
						b = __out.register;
					
					codeBuilder.addCopy(a, b);
					codeBuilder.addCopy(a + 1, b + 1);
				}
				break;
				
				// Exception handling
			case "exceptionHandle":
				// This generates no actual codes to check the exception,
				// it just makes the exception check run so that they are
				// checked
				this.state.canexception = true;
				break;
				
				// Integer/Float bits
			case "floatToRawIntBits":
			case "intBitsToFloat":
				if (__in[0].register != __out.register)
					codeBuilder.addCopy(__in[0].register, __out.register);
				break;
				
				// object -> pointer
			case "objectToPointer":
			case "objectToPointerWide":
				if (__in[0].register != __out.register)
					codeBuilder.addCopy(__in[0].register, __out.register);
				break;
				
				// Long unpack high
			case "longUnpackHigh":
				if (__in[0].register != __out.register)
					codeBuilder.addCopy(__in[0].register, __out.register);
				break;
				
				// Invoke method (possibly return a value)
			case "invoke":
			case "invokeV":
			case "invokeVL":
				{
					// Invoked methods can thrown an exception, so do
					// checks! Otherwise the behavior we expect might not
					// happen
					this.state.canexception = true;
					
					// Build the register List
					List<Integer> args = new ArrayList<>();
					int n = __in.length;
					for (int i = 2; i < n; i++)
						args.add(__in[i].register);
					
					// Before we invoke we need to set the next pool so
					// execution is correct!
					codeBuilder.addCopy(__in[1].register,
						NativeCode.NEXT_POOL_REGISTER);
					
					// Invoke pointer with arguments
					codeBuilder.add(NativeInstructionType.INVOKE,
						__in[0].register, new RegisterList(args));
					
					// Copy return value?
					switch (asmfunc)
					{
						case "invokeV":
							codeBuilder.addCopy(NativeCode.RETURN_REGISTER,
									__out.register);
							break;
							
						case "invokeVL":
							codeBuilder.addCopy(NativeCode.RETURN_REGISTER,
									__out.register);
							codeBuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
								__out.register + 1);
							break;
					}
				}
				break;
				
				// Double/Long pack
			case "doublePack":
			case "longPack":
				if (__in[1].register != __out.register + 1)
					codeBuilder.addCopy(__in[1].register, __out.register + 1);
				if (__in[0].register != __out.register)
					codeBuilder.addCopy(__in[0].register, __out.register);
				break;
			
			// Long unpack low
			case "longUnpackLow":
				if (__in[0].register + 1 != __out.register)
					codeBuilder.addCopy(__in[0].register + 1, __out.register);
				break;
				
				// Read byte memory
			case "memReadByte":
				codeBuilder.addMemoryOffReg(DataType.BYTE,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read int memory
			case "memReadInt":
				codeBuilder.addMemoryOffReg(DataType.INTEGER,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read java int memory
			case "memReadJavaInt":
				codeBuilder.addMemoryOffRegJava(DataType.INTEGER,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read short memory
			case "memReadJavaShort":
				codeBuilder.addMemoryOffRegJava(DataType.SHORT,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read short memory
			case "memReadShort":
				codeBuilder.addMemoryOffReg(DataType.SHORT,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Write byte memory
			case "memWriteByte":
				codeBuilder.addMemoryOffReg(DataType.BYTE,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write int memory
			case "memWriteInt":
				codeBuilder.addMemoryOffReg(DataType.INTEGER,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write Java int memory
			case "memWriteJavaInt":
				codeBuilder.addMemoryOffRegJava(DataType.INTEGER,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write Java short memory
			case "memWriteJavaShort":
				codeBuilder.addMemoryOffRegJava(DataType.SHORT,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write short memory
			case "memWriteShort":
				codeBuilder.addMemoryOffReg(DataType.SHORT,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
			
			// object -> pointer, with ref clear
			case "objectToPointerRefQueue":
			case "objectToPointerRefQueueWide":
				// Push references
				this.__refPush();
				
				// Do the copy
				if (__in[0].register != __out.register)
					codeBuilder.addCopy(__in[0].register, __out.register);
				
				// Clear references
				this.__refClear();
				break;
			
				// pointer -> object (and variants)
			case "pointerToObject":
			case "pointerToObjectWide":
			case "pointerToClassInfo":
			case "pointerToClassInfoWide":
				if (__in[0].register != __out.register)
					codeBuilder.addCopy(__in[0].register, __out.register);
				
				// The returned object is electable for reference
				// counting so we need to count it up otherwise it will
				// be just freed (this is just a plain copy)
				this.__refCount(__out.register);
				break;
				
				// Reference count up
			case "refCount":
				this.__refCount(__in[0].register);
				break;
				
				// Reference count down
			case "refUncount":
				this.__refUncount(__in[0].register);
				break;
				
				// Return from frame
			case "returnFrame":
				// This may be a variant which returns multiple values
				switch (__type.toString())
				{
					case "(II)V":
						codeBuilder.addCopy(__in[0].register,
							NativeCode.RETURN_REGISTER);
						codeBuilder.addCopy(__in[1].register,
							NativeCode.RETURN_REGISTER + 1);
						break;
						
					case "(I)V":
						codeBuilder.addCopy(__in[0].register,
							NativeCode.RETURN_REGISTER);
						break;
				}
				
				// Always return at the end
				this.__generateReturn();
				break;
				
				// Get the exception register
			case "specialGetExceptionRegister":
				codeBuilder.addCopy(NativeCode.EXCEPTION_REGISTER,
					__out.register);
				break;
				
				// Gets the pool register
			case "specialGetPoolRegister":
				codeBuilder.addCopy(NativeCode.POOL_REGISTER,
					__out.register);
				break;
				
				// Read return register
			case "specialGetReturnRegister":
			case "specialGetReturnHighRegister":
				codeBuilder.addCopy(NativeCode.RETURN_REGISTER,
					__out.register);
				break;
				
				// Read return register (low value)
			case "specialGetReturnLowRegister":
				codeBuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
					__out.register);
				break;
				
				// Get static field register
			case "specialGetStaticFieldRegister":
				codeBuilder.addCopy(NativeCode.STATIC_FIELD_REGISTER,
					__out.register);
				break;
				
				// Get thread register
			case "specialGetThreadRegister":
				codeBuilder.addCopy(NativeCode.THREAD_REGISTER,
					__out.register);
				break;
				
				// Set the exception register
			case "specialSetExceptionRegister":
				codeBuilder.addCopy(__in[0].register,
					NativeCode.EXCEPTION_REGISTER);
				break;
				
				// Set pool register
			case "specialSetPoolRegister":
				codeBuilder.addCopy(__in[0].register,
					NativeCode.POOL_REGISTER);
				break;
				
				// Set static field register
			case "specialSetStaticFieldRegister":
				codeBuilder.addCopy(__in[0].register,
					NativeCode.STATIC_FIELD_REGISTER);
				break;
				
				// Set thread register
			case "specialSetThreadRegister":
				codeBuilder.addCopy(__in[0].register,
					NativeCode.THREAD_REGISTER);
				break;
				
				// System calls
			case "sysCall":
			case "sysCallV":
				this.__invokeSysCall(false, false, __out, __in);
				break;
				
				// System calls (long return value)
			case "sysCallVL":
				this.__invokeSysCall(false, true, __out, __in);
				break;
				
				// Pure system calls
			case "sysCallP":
			case "sysCallPV":
				this.__invokeSysCall(true, false, __out, __in);
				break;
				
				// Pure system calls (long return value)
			case "sysCallPVL":
				this.__invokeSysCall(true, true, __out, __in);
				break;
			
			default:
				throw Debugging.oops(asmfunc);
		}
	}
	
	/**
	 * Invokes the given helper function.
	 * 
	 * @param __func The function to execute.
	 * @param __r The values to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/28
	 */
	private void __invokeHelper(HelperFunction __func, Register... __r)
		throws NullPointerException
	{
		if (__func == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Call the given static method instead
		this.__invokeStatic(InvokeType.SYSTEM, HelperFunction.HELPER_CLASS,
			__func.member.name(), __func.member.type(), new RegisterList(__r));
		
		// Check if the invocation generated an exception
		this.codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
			this.__labelException());
	}
	
	/**
	 * Invokes instance method, doing the needed pool loading and all the
	 * complicated stuff in a simple point of code.
	 *
	 * @param __it The invocation type.
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeInstance(InvokeType __it, ClassName __cl,
		String __mn, String __mt, RegisterList __args)
		throws NullPointerException
	{
		this.__invokeInstance(__it, __cl, new MethodName(__mn),
			new MethodDescriptor(__mt), __args);
	}
	
	/**
	 * Invokes instance method, doing the needed pool loading and all the
	 * complicated stuff in a simple point of code.
	 *
	 * @param __it The invocation type.
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeInstance(InvokeType __it, ClassName __cl,
		MethodName __mn, MethodDescriptor __mt, RegisterList __args)
		throws NullPointerException
	{
		if (__it == null || __cl == null || __mn == null || __mt == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need volatiles to work with
		VolatileRegisterStack volatiles = this.volatiles;
		int volclassid = volatiles.getUnmanaged(),
			volvtable = volatiles.getUnmanaged(),
			methodptr = volatiles.getUnmanaged(),
			volptable = volatiles.getUnmanaged();
		
		// Special invocation?
		boolean isspecial = (__it == InvokeType.SPECIAL);
		
		// Performing a special invoke which has some modified rules
		if (isspecial)
		{
			// Are we calling a constructor?
			boolean wantcons = __mn.isInstanceInitializer();
			
			// Is the target in this same class?
			boolean sameclass = __cl.equals(this.state.classname);
			
			// Use the exactly specified method if:
			//  * It is an initializer, we want to call that exact one
			//  * The target class is the same class of the current class
			//    being processed (private method)
			if (wantcons || sameclass)
				this.__loadClassInfo(__cl, volclassid);
			
			// Otherwise, we will be calling a super method so we need to load
			// the super class of our current class
			else
			{
				// Read the super class of our current class
				int volscfo = volatiles.getUnmanaged();
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new AccessedField(FieldAccessTime.NORMAL,
						FieldAccessType.INSTANCE,
					new FieldReference(
						new ClassName("cc/squirreljme/jvm/ClassInfo"),
						new FieldName("superclass"),
						new FieldDescriptor(
							"Lcc/squirreljme/jvm/ClassInfo;"))),
					volvtable);
				codebuilder.addMemoryOffReg(DataType.INTEGER, true,
					volclassid, volclassid, volscfo);
				
				// Cleanup
				volatiles.removeUnmanaged(volscfo);
			}
		}
		
		// Otherwise, we will purely act on the class of the instance type
		else
			codebuilder.addMemoryOffReg(DataType.INTEGER, true,
				volclassid, __args.get(0), Constants.OBJECT_CLASS_OFFSET);
		
		// Load the VTable (from the class we obtained above)
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new AccessedField(FieldAccessTime.NORMAL,
				FieldAccessType.INSTANCE,
			new FieldReference(
				new ClassName("cc/squirreljme/jvm/ClassInfo"),
				new FieldName("vtablevirtual"),
				new FieldDescriptor("[I"))),
			volvtable);
		codebuilder.addMemoryOffReg(DataType.INTEGER, true,
			volvtable, volclassid, volvtable);
		
		// Load the pool table which is mapped with the vtable
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new AccessedField(FieldAccessTime.NORMAL,
				FieldAccessType.INSTANCE,
			new FieldReference(
				new ClassName("cc/squirreljme/jvm/ClassInfo"),
				new FieldName("vtablepool"),
				new FieldDescriptor("[I"))),
			volptable);
		codebuilder.addMemoryOffReg(DataType.INTEGER, true,
			volptable, volclassid, volptable);
		
		// Method index
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new VirtualMethodIndex(__cl, __mn, __mt), methodptr);
			
		// Load from the pool table
		codebuilder.add(NativeInstructionType.LOAD_FROM_INTARRAY,
			NativeCode.NEXT_POOL_REGISTER, volptable, methodptr);
		
		// Load method pointer (from integer based array)
		codebuilder.add(NativeInstructionType.LOAD_FROM_INTARRAY,
			methodptr, volvtable, methodptr);
		
		// Invoke the method pointer
		codebuilder.add(NativeInstructionType.INVOKE,
			methodptr, __args);
		
		// Cleanup volatiles
		volatiles.removeUnmanaged(volclassid);
		volatiles.removeUnmanaged(volvtable);
		volatiles.removeUnmanaged(methodptr);
		volatiles.removeUnmanaged(volptable);
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __cl The class to create.
	 * @param __out The output register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeNew(ClassName __cl, int __out)
		throws NullPointerException
	{
		this.__invokeNew(__cl, MemHandleRegister.of(__out));
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __cl The class to create.
	 * @param __out The output register.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/29
	 */
	private void __invokeNew(ClassName __cl, MemHandleRegister __out)
		throws NullPointerException
	{
		if (__cl == null || __out == null)
			throw new NullPointerException("NARG");
		
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// We need a volatile for the class to allocate!
		try (Volatile<TypedRegister<ClassInfoPointer>> classInfo =
			this.volatiles.getTyped(ClassInfoPointer.class))
		{
			// Load class data
			this.__loadClassInfo(__cl, classInfo.register);
			
			// Call allocator
			this.__invokeHelper(HelperFunction.NEW_INSTANCE,
				classInfo.register);
			
			// Handle result
			codeBuilder.<MemHandleRegister>addCopy(
				MemHandleRegister.RETURN, __out);
		}
	}
	
	/**
	 * Invokes static method, doing the needed pool loading and all the
	 * complicated stuff in a simple point of code.
	 *
	 * @param __it The invocation type.
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeStatic(InvokeType __it, String __cl,
		String __mn, String __mt, int... __args)
		throws NullPointerException
	{
		this.__invokeStatic(__it, new ClassName(__cl), new MethodName(__mn),
			new MethodDescriptor(__mt), new RegisterList(__args));
	}
	
	/**
	 * Invokes static method, doing the needed pool loading and all the
	 * complicated stuff in a simple point of code.
	 *
	 * @param __it The invocation type.
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeStatic(InvokeType __it, String __cl,
		String __mn, String __mt, RegisterList __args)
		throws NullPointerException
	{
		this.__invokeStatic(__it, new ClassName(__cl), new MethodName(__mn),
			new MethodDescriptor(__mt), __args);
	}
	
	/**
	 * Invokes static method, doing the needed pool loading and all the
	 * complicated stuff in a simple point of code.
	 *
	 * @param __it The invocation type.
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeStatic(InvokeType __it, ClassName __cl,
		String __mn, String __mt, int... __args)
		throws NullPointerException
	{
		this.__invokeStatic(__it, __cl, new MethodName(__mn),
			new MethodDescriptor(__mt), new RegisterList(__args));
	}
	
	/**
	 * Invokes static method, doing the needed pool loading and all the
	 * complicated stuff in a simple point of code.
	 *
	 * @param __it The invocation type.
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/24
	 */
	private void __invokeStatic(InvokeType __it, ClassName __cl,
		MethodName __mn, MethodDescriptor __mt, RegisterList __args)
		throws NullPointerException
	{
		if (__it == null || __cl == null || __mn == null || __mt == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		try (Volatile<ExecutablePointer> execPtr =
				volatiles.getExecutablePointer();
			Volatile<RuntimePoolPointer> poolHandle =
				volatiles.getRuntimePoolPointer();
			Volatile<TypedRegister<MemHandleRegister>> oldEx =
				volatiles.<MemHandleRegister>getTyped(MemHandleRegister.class))
		{
			// Load address of the target method
			codeBuilder.addPoolLoad(new InvokedMethod(
				(__it == InvokeType.SYSTEM ? InvokeType.STATIC : __it),
					new MethodHandle(__cl, __mn, __mt)),
					new TypedRegister<InvokedMethod>(InvokedMethod.class,
						execPtr.register.register));
			
			// Load constant pool of the target class
			this.__loadClassPool(__cl, poolHandle.register.register);
			
			// Create a backup of the exception register (system mode)
			if (__it == InvokeType.SYSTEM)
			{
				codeBuilder.addCopy(MemHandleRegister.EXCEPTION,
					oldEx.register);
				codeBuilder.addCopy(MemHandleRegister.NULL,
					MemHandleRegister.EXCEPTION);
			}
			
			// Invoke the static method
			codeBuilder.addInvokePoolAndPointer(
				execPtr.register, poolHandle.register, __args);
			
			// If the system invoke (which could be from special code) threw an
			// exception just replace our current exception with that one since
			// it definitely would be worse!
			if (__it == InvokeType.SYSTEM)
			{
				// This is just a jump over restoring the old exception one
				// so that the system one takes priority
				NativeCodeLabel doubleFault = new NativeCodeLabel(
					"doubleFault", this._refclunk++);
				codeBuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
					doubleFault);
				
				// Restore our old exception register
				codeBuilder.addCopy(execPtr.register,
					MemHandleRegister.EXCEPTION);
				
				// Target point for double fault
				codeBuilder.label(doubleFault);
			}
		}
	}
	
	/**
	 * Invokes a system call, which can either be pure or unpure.
	 *
	 * @param __pure Is the system call pure?
	 * @param __long Is this a long system call?
	 * @param __out The return register, may be set.
	 * @param __in The input system call arguments.
	 * @since 2109/05/27
	 */
	private void __invokeSysCall(boolean __pure, boolean __long,
		JavaStackResult.Output __out, JavaStackResult.Input... __in)
	{
		// Invoked methods can thrown an exception, so do
		// checks! Otherwise the behavior we expect might not
		// happen
		this.state.canexception = true;
		
		// Invoke of pure system call?
		if (__pure)
		{
			// Build the register List
			List<Integer> args = new ArrayList<>();
			int n = __in.length;
			for (int i = 1; i < n; i++)
				args.add(__in[i].register);
			
			// Perform the pure call
			this.codebuilder.add(NativeInstructionType.SYSTEM_CALL,
				__in[0].register, new RegisterList(args));
			
			// Need to store the return value of this call
			VolatileRegisterStack volatiles = this.volatiles;
			int rvlo = volatiles.getUnmanaged(),
				rvhi = volatiles.getUnmanaged();
			
			// Defensive copy of return value
			if (__out != null)
			{
				// Low
				this.codebuilder.addCopy(NativeCode.RETURN_REGISTER, rvlo);
				
				// High
				if (__long)
					this.codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1, rvhi);
			}
			
			// Load the system call index for IPC exception store
			int ipcesid = volatiles.getUnmanaged();
			this.codebuilder.addMathConst(StackJavaType.INTEGER, MathType.ADD,
				NativeCode.ZERO_REGISTER, SystemCallIndex.EXCEPTION_STORE,
				ipcesid);
			
			// Perform system call to clear and read exception
			this.codebuilder.add(NativeInstructionType.SYSTEM_CALL,
				ipcesid, new RegisterList(NativeCode.ZERO_REGISTER));
			
			// Quickly copy out exception value
			int eval = volatiles.getUnmanaged();
			this.codebuilder.addCopy(NativeCode.RETURN_REGISTER, eval);
			
			// If this value is set, then we fail
			this.codebuilder.addIfNonZero(eval,
				this.__labelMakeException("cc/squirreljme/jvm/IPCException"));
			
			// Copy out the moved out return values
			if (__out != null)
			{
				// Low value
				this.codebuilder.addCopy(rvlo, __out.register);
				
				// Possible high value
				if (__long)
					this.codebuilder.addCopy(rvhi, __out.register + 1);
			}
			
			// No longer needed
			volatiles.removeUnmanaged(eval);
			volatiles.removeUnmanaged(ipcesid);
			volatiles.removeUnmanaged(rvhi);
			volatiles.removeUnmanaged(rvlo);
		}
		
		// Unpure system call (possibly adapted by our own handler)
		else
		{
			// Since this is a standard invocation, we just pass all the
			// input arguments as is
			int n = __in.length;
			int[] args = new int[n];
			for (int i = 0; i < n; i++)
				args[i] = __in[i].register;
			
			// Perform the unpure call into the JVM helper
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmSystemCall", "(SIIIIIIII)J", args);
				
			// Copy return value
			if (__out != null)
			{
				// Low value
				this.codebuilder.addCopy(NativeCode.RETURN_REGISTER,
					__out.register);
				
				// Possible high value
				if (__long)
					this.codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
						__out.register + 1);
			}
		}
	}
	
	/**
	 * Invokes a helped system call.
	 * 
	 * @param __r The reference to call.
	 * @param __out The output register.
	 * @param __in The input arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/29
	 */
	public final void __invokeSysCallHelped(MethodReference __r,
		JavaStackResult.Output __out, JavaStackResult.Input... __in)
		throws NullPointerException
	{
		if (__r == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Determine the system call index to call into
		int id;
		switch (__r.memberName().toString())
		{
			case "classInfoGetProperty":
				id = SystemCallIndex.CLASS_INFO_GET_PROPERTY;
				break;
			
			case "errorGet":
				id = SystemCallIndex.ERROR_GET;
				break;
			
			case "errorSet":
				id = SystemCallIndex.ERROR_SET;
				break;
			
			case "pdOfStdErr":
				id = SystemCallIndex.PD_OF_STDERR;
				break;
				
			case "pdOfStdIn":
				id = SystemCallIndex.PD_OF_STDIN;
				break;
				
			case "pdOfStdOut": 
				id = SystemCallIndex.PD_OF_STDOUT;
				break;
			
			case "pdWriteByte":
				id = SystemCallIndex.PD_WRITE_BYTE;
				break;
				
				// {@squirreljme.error JC4r The specified system call method
				// is not known. (The method)}
			default:
				throw new IllegalArgumentException("JC4r " + __r); 
		}
		
		// We definitely need volatiles for the index and other parts!
		NativeCodeBuilder codeBuilder = this.codebuilder;
		try (Volatile<IntValueRegister> idReg = this.volatiles.getIntValue())
		{
			// Load integer constant
			codeBuilder.addIntegerConst(id, idReg.register);
			
			// Perform the call
			codeBuilder.addSysCall(idReg.register, new RegisterList(__in));
				
			// Is there a return value?
			if (__out != null)
			{
				codeBuilder.addCopy(IntValueRegister.RETURN,
					IntValueRegister.of(__out.register));
				
				// Wide value?
				if (__out.type.isWide())
					codeBuilder.addCopy(IntValueRegister.RETURN_TWO,
						IntValueRegister.of(__out.register + 1));
			}
			
			// The error get/set are always successful, otherwise bad things
			// will happen, so check to see if an error occurred!
			if (id != SystemCallIndex.ERROR_GET &&
				id != SystemCallIndex.ERROR_SET)
				try (Volatile<IntValueRegister> getErrId =
					this.volatiles.getIntValue())
				{
					// Load the get error index in
					codeBuilder.addIntegerConst(SystemCallIndex.ERROR_GET,
						getErrId.register);
					
					// Perform the call
					codeBuilder.addSysCall(getErrId.register,
						idReg.register);
					
					// Did we fail the system call?
					NativeCodeLabel fail = this.__labelMakeException(
						"cc/squirreljme/jvm/mle/exceptions/MLECallError");
					codeBuilder.addIfNonZero(IntValueRegister.RETURN, fail);
				}
		}
	}
	
	/**
	 * Invokes the given pure system call that returns an integer value.
	 * 
	 * @param __sysCall The {@link SystemCallIndex}.
	 * @param __rv The return value.
	 * @param __regs The registers to call.
	 * @throws IllegalArgumentException If the system call is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/28
	 */
	private void __invokeSysCallV(int __sysCall, IntValueRegister __rv,
		Register... __regs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__rv == null || __regs == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC4q The system call is not valid. (The system
		// call ID)}
		if (__sysCall < 0 || __sysCall >= SystemCallIndex.NUM_SYSCALLS)
			throw new IllegalArgumentException("JC4q " + __sysCall);
		
		// Invoked methods can thrown an exception, so do
		// checks! Otherwise the behavior we expect might not
		// happen
		this.state.canexception = true;
		
		// We need volatiles
		NativeCodeBuilder codeBuilder = this.codebuilder;
		try (Volatile<IntValueRegister> callId = this.volatiles.getIntValue())
		{
			// Load integer constant
			codeBuilder.addIntegerConst(__sysCall, callId.register);
			
			// Perform the call
			codeBuilder.addSysCall(callId.register, __regs);
			
			// Copy return value
			codeBuilder.addCopy(IntValueRegister.RETURN, __rv);
		}
	}
	
	/**
	 * Creates and stores an exception.
	 *
	 * @return The label to the exception.
	 * @since 2019/04/09
	 */
	private NativeCodeLabel __labelException()
	{
		// Get both states for when an exception is handled (transition) and
		// for where it is not handled (full cleanup)
		ByteCodeState state = this.state;
		JavaStackResult handled = state.stack.doExceptionHandler(),
			nothandled = state.stack.doDestroy(false);
		
		// The byte code handler does not know about exception registers so
		// we need to add this operation in so it is handled correctly
		List<StateOperation> newsop = new ArrayList<>();
		for (StateOperation o : handled.operations())
			newsop.add(o);
		newsop.add(StateOperation.copy(false,
			NativeCode.EXCEPTION_REGISTER, handled.out(0).register));
		
		// Clear the exception register when it is handled, otherwise it
		// will just cause an exception fall off when the register is next
		// checked
		newsop.add(StateOperation.copy(false, NativeCode.ZERO_REGISTER,
			NativeCode.EXCEPTION_REGISTER));
		
		// Setup key
		ExceptionHandlerTransition key = new ExceptionHandlerTransition(
			new StateOperations(newsop), nothandled.enqueue(),
			state.exceptionranges.tableOf(state.addr));
		
		// Try to use an already existing point
		Map<ExceptionHandlerTransition, __EData__> ehtable = this._ehtable;
		__EData__ rv = ehtable.get(key);
		if (rv != null)
			return rv.label;
			
		// Build new data to record this point
		rv = this.__eData(new NativeCodeLabel("exception", ehtable.size()));
		ehtable.put(key, rv);
		
		// This exception should be unique, so we need to process it
		this._ehTableQueue.addLast(
			new __TransitingExceptionHandler__(key, rv));
		
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
	private NativeCodeLabel __labelJava(InstructionJumpTarget __jt)
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
		
		// Do a transition to the target stack
		return this.__labelJavaTransition(sourcestack.
			doTransition(targetstack).operations(), __jt);
	}
	
	/**
	 * Creates the actual label which is used for the state transition.
	 *
	 * @param __sops The operation to use.
	 * @param __jt The jump target in the Java address space.
	 * @return The label for this transition.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	private NativeCodeLabel __labelJavaTransition(StateOperations __sops,
		InstructionJumpTarget __jt)
		throws NullPointerException
	{
		if (__sops == null || __jt == null)
			throw new NullPointerException("NARG");
		
		// If no operations were generated then just use a normal jump since
		// there is no point in transitioning anyway
		if (__sops.isEmpty())
			return new NativeCodeLabel("java", __jt.target());
		
		// Setup key
		StateOperationsAndTarget key =
			new StateOperationsAndTarget(__sops, __jt);
		
		// Determine if such a transition was already done, since if the
		// transition is exactly the same we do not need to actually do
		// anything
		Map<StateOperationsAndTarget, __EData__> transits = this._transits;
		__EData__ rv = transits.get(key);
		if (rv != null)
			return rv.label;
		
		// Setup transition for later
		ByteCodeState state = this.state;
		rv = this.__eData(new NativeCodeLabel("transit", transits.size()));
		transits.put(key, rv);
		
		// Queue for later
		this._transitQueue.add(new __StateOpTransit__(key, rv));
		
		return rv.label;
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
	private NativeCodeLabel __labelMakeException(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Setup key, the label is the target to jump to after the exception
		// has been generated and a throw is performed
		ByteCodeState state = this.state;
		ClassAndLabel key = new ClassAndLabel(new ClassName(__cl),
			this.__labelException());
		
		// Look in the table to see if we made it before
		Map<ClassAndLabel, __EData__> metable = this._metable;
		__EData__ rv = metable.get(key);
		if (rv != null)
			return rv.label;
		
		// Build new data to record this point
		rv = this.__eData(new NativeCodeLabel("makeexception",
			metable.size()));
		metable.put(key, rv);
		
		// Add to queue for final processing
		this._meTableQueue.add(new __MakeException__(key, rv));
		
		// Return the created label (where the caller jumps to)
		return rv.label;
	}
	
	/**
	 * Creates a label to potentially reference clear and jumps to the given
	 * label. This uses the current ref queue table.
	 *
	 * @param __tl The target label.
	 * @return The transition label for reference clearing or {@code __tl}
	 * if there are no references to clear.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/24
	 */
	private NativeCodeLabel __labelRefClearJump(NativeCodeLabel __tl)
		throws NullPointerException
	{
		if (__tl == null)
			throw new NullPointerException("NARG");
		
		return this.__labelRefClearJump(this._lastenqueue, __tl);
	}
	
	/**
	 * Creates a label to potentially reference clear and jumps to the given
	 * label.
	 *
	 * @param __eql The reference queue label.
	 * @param __tl The target label.
	 * @return The transition label for reference clearing or {@code __tl}
	 * if there are no references to clear.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/24
	 */
	private NativeCodeLabel __labelRefClearJump(
		JavaStackEnqueueList __eql, NativeCodeLabel __tl)
		throws NullPointerException
	{
		if (__tl == null)
			throw new NullPointerException("NARG");
		
		// If not clearing anything, just return the target label
		if (__eql == null || __eql.isEmpty())
			return __tl;
		
		// Setup key
		EnqueueAndLabel key = new EnqueueAndLabel(__eql, __tl);
		
		// Did we make this before?
		Map<EnqueueAndLabel, __EData__> refcljumps = this._refcljumps;
		__EData__ rv = refcljumps.get(key);
		if (rv != null)
			return rv.label;
		
		// Setup new record
		ByteCodeState state = this.state;
		rv = this.__eData(new NativeCodeLabel("refclear", refcljumps.size()));
		refcljumps.put(key, rv);
		
		// Add to the processing queue for later handling
		this._refClearJumpQueue.add(new __RefClearJump__(key, rv));
		
		// Use the created label
		return rv.label;
	}
	
	/**
	 * Loads the class information for a class.
	 *
	 * @param __cl The class to load.
	 * @param __r The output register.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/19
	 */
	private void __loadClassInfo(String __cl, int __r)
		throws NullPointerException
	{
		this.__loadClassInfo(new ClassName(__cl), __r);
	}
	
	/**
	 * Loads the class information for a class.
	 *
	 * @param __cl The class to load.
	 * @param __r The output register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/15
	 */
	@Deprecated
	private void __loadClassInfo(ClassName __cl, int __r)
		throws NullPointerException
	{
		this.__loadClassInfo(__cl, new TypedRegister<ClassInfoPointer>(
			ClassInfoPointer.class, __r));
	}
	
	/**
	 * Loads the class information for a class.
	 *
	 * @param __cl The class to load.
	 * @param __r The output register.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/28
	 */
	private void __loadClassInfo(ClassName __cl,
		TypedRegister<ClassInfoPointer> __r)
		throws NullPointerException
	{
		if (__cl == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Used for loading code
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Load cached pool entry
		codeBuilder.<ClassInfoPointer>addPoolLoad(
			new ClassInfoPointer(__cl), __r);
		
		// There are classes that must always exist within the VM in an
		// always loaded state, otherwise there can be a very bad infinite
		// recursive trying to load these important classes.
		if (!ClassLoadingAdjustments.isDeferredLoad(
			this.state.classname.toString(), __cl.toString()))
			return;
		
		// Check if the class is initialized or not
		NativeCodeLabel isLoadPt = new NativeCodeLabel("classInfoLoaded",
			this._refclunk++);
		try (Volatile<IntValueRegister> isInitBool =
			this.volatiles.getIntValue())
		{
			// If the class is already initialized do not try loading
			this.__invokeHelper(HelperFunction.IS_CLASS_INIT, __r);
			codeBuilder.addIfNonZero(IntValueRegister.RETURN, isLoadPt);
			
			// Otherwise try to initialize the class
			this.__invokeHelper(HelperFunction.INIT_CLASS, __r);
			
			// End point is here
			codeBuilder.label(isLoadPt);
		}
	}
	
	/**
	 * Loads the Class object for the given class into the given register.
	 *
	 * @param __cl The class to load.
	 * @param __r The register to place it in.
	 * @since 2019/04/26
	 */
	private void __loadClassObject(ClassName __cl, int __r)
	{
		VolatileRegisterStack volatiles = this.volatiles;
		
		// Load the class info for the class
		int volcdvt = volatiles.getUnmanaged();
		this.__loadClassInfo(__cl, volcdvt);
		
		// Call internal class object loader
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmLoadClass", "(I)Ljava/lang/Class;", volcdvt);
		
		// Cleanup
		volatiles.removeUnmanaged(volcdvt);
		
		// Copy return value to the output register
		this.codebuilder.addCopy(NativeCode.RETURN_REGISTER, __r);
	}
	
	/**
	 * Loads the constant pool for the given class.
	 *
	 * @param __cl The class pool to load.
	 * @param __r The output register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/15
	 */
	private void __loadClassPool(ClassName __cl, int __r)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Used for loading code
		NativeCodeBuilder codeBuilder = this.codebuilder;
		
		// Load pool pointer
		codeBuilder.<ClassPool>addPoolLoad(new ClassPool(__cl),
			TypedRegister.<ClassPool>of(ClassPool.class, __r));
		
		// Detect classes which are dynamically initialized
		if (!ClassLoadingAdjustments.isDeferredLoad(
			this.state.classname.toString(), __cl.toString()))
			return;
		
		Debugging.todoNote("Add dynamic pool load for %s!", __cl);
		/*// Jump if the pool is already loaded
		NativeCodeLabel isloaded = new NativeCodeLabel("piisloaded",
			this._refclunk++);
		codeBuilder.addIfNonZero(__r, isloaded);
		
		// Need volatile to get the class info
		VolatileRegisterStack volatiles = this.volatiles;
		int volcinfo = volatiles.getUnmanaged(),
			volpoolv = volatiles.getUnmanaged(),
			volscfo = volatiles.getUnmanaged();
		
		// Load the ClassInfo for the class we want
		this.__loadClassInfo(__cl, volcinfo);
		
		// Load pool pointer from this ClassInfo
		codeBuilder.add(NativeInstructionType.LOAD_POOL,
			new AccessedField(FieldAccessTime.NORMAL,
				FieldAccessType.INSTANCE,
			new FieldReference(
				new ClassName("cc/squirreljme/jvm/ClassInfo"),
				new FieldName("pool"),
				new FieldDescriptor("I"))), volscfo);
		codeBuilder.addMemoryOffReg(DataType.INTEGER, true,
			volpoolv, volcinfo, volscfo);
		
		// Store it
		codeBuilder.add(NativeInstructionType.STORE_POOL,
			new ClassPool(__cl), volpoolv);
		
		// Cleanup
		volatiles.removeUnmanaged(volcinfo);
		volatiles.removeUnmanaged(volpoolv);
		volatiles.removeUnmanaged(volscfo);
		
		// End point is here
		codeBuilder.label(isloaded);*/
	}
	
	/**
	 * Enters the monitor.
	 *
	 * @param __enter Is the monitor being entered? 
	 * @param __r The register to enter a monitor for.
	 * @since 2019/04/26
	 */
	private void __monitor(boolean __enter, int __r)
	{
		// Call helper method
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			(__enter ? "jvmMonitorEnter" : "jvmMonitorExit"), "(I)V", __r);
	}
	
	/**
	 * If anything has been previously pushed then generate code to clear it.
	 *
	 * @since 2019/03/30
	 */
	private void __refClear()
	{
		// Do nothing if nothing has been enqueued
		JavaStackEnqueueList lastenqueue = this._lastenqueue;
		if (lastenqueue == null)
			return;
		
		// No need to clear anymore
		this.__refReset();
		
		// Un-count all of them accordingly
		VolatileRegisterStack volatiles = this.volatiles;
		NativeCodeBuilder codebuilder = this.codebuilder;
		for (int i = 0, n = lastenqueue.size(); i < n; i++)
		{
			// Get the volatile to clear
			int v = lastenqueue.get(i);
			
			// Uncount this one
			this.__refUncount(v);
			
			// Free it for later usage
			volatiles.removeUnmanaged(v);
		}
	}
	
	/**
	 * Generates code to reference count the given register.
	 *
	 * @param __r The register to reference to count.
	 * @deprecated Use the type safe {@link #__refCount(MemHandleRegister)}. 
	 * @since 2019/04/25
	 */
	@Deprecated
	private void __refCount(int __r)
	{
		this.__refCount(MemHandleRegister.of(__r));
	}
	
	/**
	 * Generates code to reference count up the given register.
	 *
	 * @param __r The register reference to count up.
	 * @since 2019/04/25
	 */
	private void __refCount(MemHandleRegister __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// If the object is null then it will not be counted, this is skipped
		NativeCodeLabel ncj = new NativeCodeLabel("refnocount",
			this._refclunk++);
		
		// Do not do any counting if this is a NULL reference
		NativeCodeBuilder codebuilder = this.codebuilder;
		codebuilder.addIfZero(__r.register, ncj);
		
		// Count this handle directly up
		codebuilder.addMemHandleCountUp(__r);
		
		// No count is jumped here
		codebuilder.label(ncj);
	}
	
	/**
	 * Generates code to enqueue registers, if there are any. This implicitly
	 * uses the registers from the state.
	 *
	 * @return True if the push list was not empty.
	 * @since 2019/04/10
	 */
	private boolean __refPush()
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
	private boolean __refPush(JavaStackEnqueueList __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Nothing to enqueue?
		if (__r.isEmpty())
		{
			this.__refReset();
			return false;
		}
		
		// Place anything that is referenced into volatile registers
		VolatileRegisterStack volatiles = this.volatiles;
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Get a volatile register and copy into it, store the volatile
		int n = __r.size();
		int[] use = new int[n];
		for (int i = 0; i < n; i++)
		{
			// Get the volatile register to copy into
			int v = volatiles.getUnmanaged();
			
			// Copy to the volatile
			codebuilder.addCopy(__r.get(i), v);
			
			// Use this volatile register
			use[i] = v;
		}
		
		// We completely lose our original registers and instead use our new
		// set of volatiles for later clearing and removing
		// Note that we keep the same stack offset because it will match
		// exactly as the input (needed for exception handlers), even though
		// the registers might be all over the place.
		this._lastenqueue = new JavaStackEnqueueList(__r.stackstart, use);
		
		// Did enqueue something
		return true;
	}
	
	/**
	 * Resets the reference queue so nothing uses it.
	 *
	 * @since 2019/11/24
	 */
	private void __refReset()
	{
		this._lastenqueue = null;
	}
	
	/**
	 * Generates code to reference uncount the given register.
	 *
	 * @param __r The reference to uncount.
	 * @deprecated Use the type safe {@link #__refUncount(MemHandleRegister)}.  
	 * @since 2019/04/25
	 */
	@Deprecated
	private void __refUncount(int __r)
	{
		this.__refUncount(MemHandleRegister.of(__r));
	}
	
	/**
	 * Generates code to reference uncount the given register.
	 *
	 * @param __r The reference to uncount.
	 * @since 2020/11/28
	 */
	private void __refUncount(MemHandleRegister __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// If the object is null then it will not be uncounted, this is skipped
		NativeCodeLabel ncj = new NativeCodeLabel("RefNoUnCount",
			this._refclunk++);
		
		// Do not do any un-counting if this is null
		NativeCodeBuilder codeBuilder = this.codebuilder;
		codeBuilder.addIfZero(__r.register, ncj);
		
		// We need a register to determine if we should do garbage collection
		try (Volatile<IntValueRegister> count = this.volatiles.getIntValue())
		{
			// Perform the uncount, store the count value somewhere we can
			// check to ensure that we do need to GC
			codeBuilder.addMemHandleCountDown(__r, count.register); 
			
			// If the count is still positive, we do not GC
			codeBuilder.addIfPositive(count.register, ncj);
			
			// Call garbage collect on object via helper
			this.__invokeHelper(HelperFunction.GC_MEM_HANDLE,
				__r);
			
			// Reset the variable to zero to prevent it from being used again
			codeBuilder.addCopy(MemHandleRegister.NULL, __r);
			
			// Null reference or no GC performed
			codeBuilder.label(ncj);
		}
	}
	
	/**
	 * Uses the given EData and returns the used label.
	 *
	 * @param __ed The data to use.
	 * @return The label.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/25
	 */
	private NativeCodeLabel __useEDataAndGetLabel(__EData__ __ed)
		throws NullPointerException
	{
		if (__ed == null)
			throw new NullPointerException("NARG");
		
		// Setup state
		ByteCodeState state = this.state;
		state.addr = __ed.addr;
		state.line = __ed.line;
		
		// And return the label
		return __ed.label;
	}
	
	/**
	 * Sets debug point information for edata.
	 *
	 * @param __ed The EData used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/16
	 */
	private void __useEDataDebugPoint(__EData__ __ed, int __jop)
		throws NullPointerException
	{
		if (__ed == null)
			throw new NullPointerException("NARG");
		
		// Add debug point (operation becomes debug point 1)
		this.codebuilder.add(NativeInstructionType.DEBUG_POINT,
			__ed.line & 0x7FFF, __jop,
			__ed.jpc & 0x7FFF);
	}
}

