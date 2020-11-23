// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

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
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.NotedString;
import dev.shadowtail.classfile.pool.NullPoolEntry;
import dev.shadowtail.classfile.pool.UsedString;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
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

/**
 * This contains the handler for the near native byte code.
 *
 * @since 2019/04/06
 */
public final class NearNativeByteCodeHandler
	implements ByteCodeHandler
{
	/** The jvm functions class. */
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
	
	/** Made exception table. */
	private final Map<ClassAndLabel, __EData__> _metable =
		new LinkedHashMap<>();
	
	/** The returns which have been performed. */
	private final List<JavaStackEnqueueList> _returns =
		new ArrayList<>();
	
	/** Java transition labels. */
	private final Map<StateOperationsAndTarget, __EData__> _transits =
		new LinkedHashMap<>();
	
	/** Reference clearing and jumping to label. */
	private final Map<EnqueueAndLabel, __EData__> _refcljumps =
		new LinkedHashMap<>();
	
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
		int volaip = volatiles.get();
		
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
		volatiles.remove(volaip);
		
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
		int volaip = volatiles.get();
		
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
			voltemp = volatiles.get();
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
			volatiles.remove(voltemp);
		}
		
		// No longer used
		volatiles.remove(volaip);
		
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
		
		// Determine volatile registers
		VolatileRegisterStack volatiles = this.volatiles;
		int tempreg = volatiles.get();
		
		// Read field offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr, true), tempreg);
		
		// Data type used
		DataType dt = DataType.of(__fr.memberType().primitiveType());
		
		// Use helper function?
		if (dt.isWide())
		{
			// Read memory
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmMemReadLong", "(II)J",
				ireg, tempreg);
			
			// Copy return value
			codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
				__v.register + 1);
			codebuilder.addCopy(NativeCode.RETURN_REGISTER,
				__v.register);
		}
		
		// Read from memory
		else
			codebuilder.addMemoryOffReg(
				dt, true,
				__v.register, ireg, tempreg);
		
		// Count it up?
		if (__fr.memberType().isObject())
			this.__refCount(__v.register);
		
		// Not used anymore
		volatiles.remove(tempreg);
			
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
		int volfioff = volatiles.get();
		
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
			voltemp = volatiles.get();
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
			volatiles.remove(voltemp);
		}
		
		// No longer used
		volatiles.remove(volfioff);
			
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
		int volwantcldx = volatiles.get();
		
		// Load desired class index type
		this.__loadClassInfo(__cl, volwantcldx);
		
		// Invoke helper method
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmIsInstance", "(II)I", __v.register, volwantcldx);
		
		// Use result
		this.codebuilder.addCopy(NativeCode.RETURN_REGISTER, __o.register);
		
		// No longer needed
		volatiles.remove(volwantcldx);
		
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
		ClassName targetclass = __r.handle().outerClass();
		
		// Invocation of assembly method?
		if ("cc/squirreljme/jvm/Assembly".equals(
			targetclass.toString()))
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
			// Check that the object is of the given class type and is not null
			int ireg = __in[0].register;
			this.__basicCheckNPE(ireg);
			
			// Check types if this is not compatible
			if (!__in[0].isCompatible(__r.handle().outerClass()))
				this.__basicCheckCCE(ireg, __r.handle().outerClass());
			
			// Invoking interface method
			if (__t == InvokeType.INTERFACE)
			{
				// Load the interface we are looking in
				int voliclass = volatiles.get();
				this.__loadClassInfo(__r.handle().outerClass(), voliclass);
				
				// Load the method index of the volatile method in question
				int volimethdx = volatiles.get();
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new MethodIndex(__r.handle().outerClass(),
						__r.handle().name(), __r.handle().descriptor()),
					volimethdx);
				
				// Use helper method to find the method pointer to invoke
				// for this interface (hi=pool, lo=pointer)
				this.__invokeStatic(InvokeType.SYSTEM,
					NearNativeByteCodeHandler.JVMFUNC_CLASS,
					"jvmInterfacePointer", "(III)J",
					ireg, voliclass, volimethdx);
				
				// We need to extract the pool pointer of the class we
				// are calling in so that nothing is horribly incorrect
				codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
					NativeCode.NEXT_POOL_REGISTER);
				
				// Invoke the pointer that this method returned
				codebuilder.add(NativeInstructionType.INVOKE,
					NativeCode.RETURN_REGISTER, reglist);
				
				// Cleanup
				volatiles.remove(voliclass);
				volatiles.remove(volimethdx);
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
				volbh = volatiles.get();
				volbl = volatiles.get();
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
			volatiles.remove(volbh);
			volatiles.remove(volbl);
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
		int volclassobj = volatiles.get();
		
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
		volatiles.remove(volclassobj);
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
		int volresult = volatiles.get();
		
		// Perform new invocation
		this.__invokeNew(__cn, volresult);
		
		// Check for out of memory
		this.__basicCheckOOM(volresult);
		
		// All the exceptions were checked
		this.state.canexception = false;
		
		// Copy to result
		codebuilder.addCopy(volresult, __out.register);
		
		// Not needed
		volatiles.remove(volresult);
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
		
		// Check for negative array size
		this.__basicCheckNAS(__len.register);
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volclassdx = volatiles.get(),
			volresult = volatiles.get();
		
		// Load the class data for the array type
		// If not a fixed class index, then rely on the value in the pool
		this.__loadClassInfo(__at, volclassdx);
		
		// Call internal handler, place into temporary for OOM check
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS, "jvmNewArray",
			"(II)I", volclassdx, __len.register);
		codebuilder.addCopy(NativeCode.RETURN_REGISTER, volresult);
		
		// No longer needed
		volatiles.remove(volclassdx);
		
		// Check for out of memory
		this.__basicCheckOOM(volresult);
		
		// All the exceptions were checked
		this.state.canexception = false;
		
		// Place result in true location
		codebuilder.addCopy(volresult, __out.register);
		
		// Not needed
		volatiles.remove(volresult);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doPoolLoad(Object __v, JavaStackResult.Output __out)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Loading string value?
		if (__v instanceof String)
		{
			// Need volatiles
			VolatileRegisterStack volatiles = this.volatiles;
			
			// Load the potentially cached string pointer
			codebuilder.add(NativeInstructionType.LOAD_POOL,
				new UsedString((String)__v), __out.register);
			
			// Label to jump to if this string is already loaded
			NativeCodeLabel ispresent = new NativeCodeLabel("strloaded",
				this._refclunk++);
			codebuilder.addIfNonZero(__out.register, ispresent);
			
			// Load the noted string
			int volstrptr = volatiles.get();
			codebuilder.add(NativeInstructionType.LOAD_POOL,
				new NotedString((String)__v), volstrptr);
				
			// Call internal string loader
			this.__invokeStatic(InvokeType.SYSTEM,
				NearNativeByteCodeHandler.JVMFUNC_CLASS,
				"jvmLoadString", "(I)Ljava/lang/String;", volstrptr);
			
			// Cleanup
			volatiles.remove(volstrptr);
			
			// Store into the pull and copy the result as well
			codebuilder.add(NativeInstructionType.STORE_POOL,
				new UsedString((String)__v), NativeCode.RETURN_REGISTER);
			codebuilder.addCopy(NativeCode.RETURN_REGISTER, __out.register);
			
			// String is loaded
			codebuilder.label(ispresent);
		}
		
		// Some other value
		else
			codebuilder.add(NativeInstructionType.LOAD_POOL,
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
		int volsfo = volatiles.get();
		
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
		volatiles.remove(volsfo);
			
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
		int volsfo = volatiles.get();
		
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
			voltemp = volatiles.get();
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
			volatiles.remove(voltemp);
		}
		
		// Not needed
		volatiles.remove(volsfo);
		
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
		ByteCodeState state = this.state;
		List<JavaStackEnqueueList> returns = this._returns;
		VolatileRegisterStack volatiles = this.volatiles;
		
		// Was an exception handler generated?
		boolean didehfall = false;
		
		// Generate reference clear jumps
		Map<EnqueueAndLabel, __EData__> refcljumps = this._refcljumps;
		for (Map.Entry<EnqueueAndLabel, __EData__> e : refcljumps.entrySet())
		{
			EnqueueAndLabel eql = e.getKey();
			
			// Set label target for this one
			codebuilder.label(this.__useEDataAndGetLabel(e.getValue()));
			this.__useEDataDebugPoint(e.getValue(), 256);
			
			// Clear references
			JavaStackEnqueueList enq = eql.enqueue;
			for (int i = 0, n = enq.size(); i < n; i++)
				this.__refUncount(enq.get(i));
			
			// Then go to the target
			codebuilder.addGoto(eql.label);
		}
		
		// Generate make exception code
		Map<ClassAndLabel, __EData__> metable = this._metable;
		for (Map.Entry<ClassAndLabel, __EData__> e : metable.entrySet())
		{
			ClassAndLabel csl = e.getKey();
			
			// Set label target for this one
			codebuilder.label(this.__useEDataAndGetLabel(e.getValue()));
			this.__useEDataDebugPoint(e.getValue(), 257);
			
			// Allocate class object, make sure it is not done in the exception
			// register because the method we call may end up just clearing it
			// and stopping if a make exception is done (because there is an
			// exception here).
			int exinst = volatiles.get();
			this.__invokeNew(csl.classname, exinst);
			
			// Initialize the exception
			this.__invokeInstance(InvokeType.SPECIAL, csl.classname, "<init>",
				"()V", new RegisterList(exinst));
			
			// Copy result into the exception register
			codebuilder.addCopy(exinst, NativeCode.EXCEPTION_REGISTER);
			
			// Done with this
			volatiles.remove(exinst);
			
			// Generate jump to exception handler
			codebuilder.addGoto(csl.label);
		}
		
		// Generate exception handler tables
		Map<ExceptionHandlerTransition, __EData__> ehtab = this._ehtable;
		for (Map.Entry<ExceptionHandlerTransition, __EData__> e :
			ehtab.entrySet())
		{
			ExceptionHandlerTransition ehtran = e.getKey();
			StateOperations sops = ehtran.handled;
			JavaStackEnqueueList enq = ehtran.nothandled;
			ExceptionHandlerTable ehtable = ehtran.table;
			
			// Label used for the jump target
			NativeCodeLabel lab = this.__useEDataAndGetLabel(e.getValue());
			this.__useEDataDebugPoint(e.getValue(), 258);
			
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
				int volehclassdx = volatiles.get();
				this.__loadClassInfo(eh.type(), volehclassdx);
				
				// Call instance handler check
				this.__invokeStatic(InvokeType.SYSTEM,
					NearNativeByteCodeHandler.JVMFUNC_CLASS,
					"jvmIsInstance", "(II)I",
					NativeCode.EXCEPTION_REGISTER, volehclassdx);
				
				// Cleanup
				volatiles.remove(volehclassdx);
				
				// If the return value is non-zero then it is an instance, in
				// which case we jump to the handler address.
				codebuilder.addIfNonZero(NativeCode.RETURN_REGISTER,
					this.__labelJavaTransition(sops,
						new InstructionJumpTarget(eh.handlerAddress())));
			}
			
			// No exception handler is available so, just fall through to the
			// caller as needed
			this.__generateReturn(enq);
			
			// Exception handler was generated
		}
		
		// Generate transition labels
		Map<StateOperationsAndTarget, __EData__> trs = this._transits;
		for (Map.Entry<StateOperationsAndTarget, __EData__> e : trs.entrySet())
		{
			StateOperationsAndTarget sot = e.getKey();
			StateOperations ops = sot.operations;
			InstructionJumpTarget target = sot.target;
			
			// Set label target for this one
			codebuilder.label(this.__useEDataAndGetLabel(e.getValue()));
			this.__useEDataDebugPoint(e.getValue(), 259);
			
			// Generate operations
			this.doStateOperations(ops);
			
			// Then just jump to the Java target
			codebuilder.addGoto(new NativeCodeLabel("java", target.target()));
		}
		
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
		int volarraylen = volatiles.get();
		
		// Read length of array
		codebuilder.addMemoryOffConst(DataType.INTEGER, true,
			volarraylen,
			__ir, Constants.ARRAY_LENGTH_OFFSET);
		
		// If the index is greater or equal to the length then the access
		// is invalid
		codebuilder.addIfICmp(CompareType.GREATER_THAN_OR_EQUALS,
			__dxr, volarraylen, lab);
		
		// No longer needed
		volatiles.remove(volarraylen);
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
	 * @since 2019/04/22
	 */
	private void __basicCheckCCE(int __ir, ClassName __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
			
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volwantcldx = volatiles.get();
		
		// Load desired target class type
		this.__loadClassInfo(__cl, volwantcldx);
		
		// Call helper class
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmIsInstance", "(II)I", __ir, volwantcldx);
		
		// If the resulting method call returns zero then it is not an instance
		// of the given class. The return register is checked because the
		// value of that method will be placed there.
		codebuilder.addIfZero(NativeCode.RETURN_REGISTER,
			this.__labelRefClearJump(this.__labelMakeException(
			"java/lang/ClassCastException")));
		
		// No longer needed
		volatiles.remove(volwantcldx);
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
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Check against less than zero
		codebuilder.addIfICmp(CompareType.LESS_THAN,
			__lr, NativeCode.ZERO_REGISTER, this.__labelRefClearJump(
			this.__labelMakeException(
				"java/lang/NegativeArraySizeException")));
	}
	
	/**
	 * Basic check if the instance is null.
	 *
	 * @param __ir The register to check.
	 * @since 2019/04/22
	 */
	private void __basicCheckNPE(int __ir)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Just a plain zero check
		codebuilder.addIfZero(__ir, this.__labelRefClearJump(
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
					ssh = volatiles.get();
					codebuilder.addCopy(NativeCode.RETURN_REGISTER, ssh);
				}
				
				// And wide value, if any
				if (this.isreturnwide)
				{
					ssl = volatiles.get();
					codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1, ssl);
				}
				
				// Uncount and clear out
				this.__monitor(false, this.monitortarget);
				this.__refUncount(this.monitortarget);
				
				// Recover value, if any
				if (this.isreturn)
				{
					codebuilder.addCopy(ssh, NativeCode.RETURN_REGISTER);
					volatiles.remove(ssh);
				}
				
				// Recover wide, if any
				if (this.isreturnwide)
				{
					codebuilder.addCopy(ssl, NativeCode.RETURN_REGISTER + 1);
					volatiles.remove(ssl);
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
			ssh = volatiles.get();
			codebuilder.addCopy(NativeCode.RETURN_REGISTER, ssh);
		}
		
		// And wide value, if any
		if (this.isreturnwide)
		{
			ssl = volatiles.get();
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
			volatiles.remove(ssh);
		}
		
		// Recover wide, if any
		if (this.isreturnwide)
		{
			codebuilder.addCopy(ssl, NativeCode.RETURN_REGISTER + 1);
			volatiles.remove(ssl);
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
	 * Invokes an assembly method.
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
		NativeCodeBuilder codebuilder = this.codebuilder;
		
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
				codebuilder.add(
					NativeInstructionType.ATOMIC_COMPARE_GET_AND_SET,
						__in[0].register,
						__out.register,
						__in[1].register,
						__in[2].register,
						0);
				break;
				
				// Atomic decrement and get
			case "atomicDecrementAndGet":
				codebuilder.add(
					NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET,
						__out.register,
						__in[0].register,
						0);
				break;
				
				// Atomic increment
			case "atomicIncrement":
				codebuilder.add(
					NativeInstructionType.ATOMIC_INT_INCREMENT,
						__in[0].register,
						0);
				break;
				
				// Breakpoint
			case "breakpoint":
				codebuilder.add(NativeInstructionType.BREAKPOINT);
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
					
					codebuilder.addCopy(a, b);
					codebuilder.addCopy(a + 1, b + 1);
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
				
				// object -> pointer
			case "objectToPointer":
				
				// Long unpack high
			case "longUnpackHigh":
				if (__in[0].register != __out.register)
					codebuilder.addCopy(__in[0].register, __out.register);
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
					codebuilder.addCopy(__in[1].register,
						NativeCode.NEXT_POOL_REGISTER);
					
					// Invoke pointer with arguments
					codebuilder.add(NativeInstructionType.INVOKE,
						__in[0].register, new RegisterList(args));
					
					// Copy return value?
					switch (asmfunc)
					{
						case "invokeV":
							codebuilder.addCopy(NativeCode.RETURN_REGISTER,
									__out.register);
							break;
							
						case "invokeVL":
							codebuilder.addCopy(NativeCode.RETURN_REGISTER,
									__out.register);
							codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
								__out.register + 1);
							break;
					}
				}
				break;
				
				// Double/Long pack
			case "doublePack":
			case "longPack":
				if (__in[1].register != __out.register + 1)
					codebuilder.addCopy(__in[1].register, __out.register + 1);
				if (__in[0].register != __out.register)
					codebuilder.addCopy(__in[0].register, __out.register);
				break;
			
			// Long unpack low
			case "longUnpackLow":
				if (__in[0].register + 1 != __out.register)
					codebuilder.addCopy(__in[0].register + 1, __out.register);
				break;
				
				// Read byte memory
			case "memReadByte":
				codebuilder.addMemoryOffReg(DataType.BYTE,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read int memory
			case "memReadInt":
				codebuilder.addMemoryOffReg(DataType.INTEGER,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read java int memory
			case "memReadJavaInt":
				codebuilder.addMemoryOffRegJava(DataType.INTEGER,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read short memory
			case "memReadJavaShort":
				codebuilder.addMemoryOffRegJava(DataType.SHORT,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Read short memory
			case "memReadShort":
				codebuilder.addMemoryOffReg(DataType.SHORT,
					true, __out.register,
					__in[0].register, __in[1].register);
				break;
				
				// Write byte memory
			case "memWriteByte":
				codebuilder.addMemoryOffReg(DataType.BYTE,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write int memory
			case "memWriteInt":
				codebuilder.addMemoryOffReg(DataType.INTEGER,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write Java int memory
			case "memWriteJavaInt":
				codebuilder.addMemoryOffRegJava(DataType.INTEGER,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write Java short memory
			case "memWriteJavaShort":
				codebuilder.addMemoryOffRegJava(DataType.SHORT,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
				
				// Write short memory
			case "memWriteShort":
				codebuilder.addMemoryOffReg(DataType.SHORT,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
			
			// object -> pointer, with ref clear
			case "objectToPointerRefQueue":
				// Push references
				this.__refPush();
				
				// Do the copy
				if (__in[0].register != __out.register)
					codebuilder.addCopy(__in[0].register, __out.register);
				
				// Clear references
				this.__refClear();
				break;
			
				// pointer -> object (and variants)
			case "pointerToObject":
			case "pointerToClassInfo":
				if (__in[0].register != __out.register)
					codebuilder.addCopy(__in[0].register, __out.register);
				
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
						codebuilder.addCopy(__in[0].register,
							NativeCode.RETURN_REGISTER);
						codebuilder.addCopy(__in[1].register,
							NativeCode.RETURN_REGISTER + 1);
						break;
						
					case "(I)V":
						codebuilder.addCopy(__in[0].register,
							NativeCode.RETURN_REGISTER);
						break;
				}
				
				// Always return at the end
				this.__generateReturn();
				break;
				
				// Get the exception register
			case "specialGetExceptionRegister":
				codebuilder.addCopy(NativeCode.EXCEPTION_REGISTER,
					__out.register);
				break;
				
				// Gets the pool register
			case "specialGetPoolRegister":
				codebuilder.addCopy(NativeCode.POOL_REGISTER,
					__out.register);
				break;
				
				// Read return register
			case "specialGetReturnRegister":
			case "specialGetReturnHighRegister":
				codebuilder.addCopy(NativeCode.RETURN_REGISTER,
					__out.register);
				break;
				
				// Read return register (low value)
			case "specialGetReturnLowRegister":
				codebuilder.addCopy(NativeCode.RETURN_REGISTER + 1,
					__out.register);
				break;
				
				// Get static field register
			case "specialGetStaticFieldRegister":
				codebuilder.addCopy(NativeCode.STATIC_FIELD_REGISTER,
					__out.register);
				break;
				
				// Get thread register
			case "specialGetThreadRegister":
				codebuilder.addCopy(NativeCode.THREAD_REGISTER,
					__out.register);
				break;
				
				// Set the exception register
			case "specialSetExceptionRegister":
				codebuilder.addCopy(__in[0].register,
					NativeCode.EXCEPTION_REGISTER);
				break;
				
				// Set pool register
			case "specialSetPoolRegister":
				codebuilder.addCopy(__in[0].register,
					NativeCode.POOL_REGISTER);
				break;
				
				// Set static field register
			case "specialSetStaticFieldRegister":
				codebuilder.addCopy(__in[0].register,
					NativeCode.STATIC_FIELD_REGISTER);
				break;
				
				// Set thread register
			case "specialSetThreadRegister":
				codebuilder.addCopy(__in[0].register,
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
		int volclassid = volatiles.get(),
			volvtable = volatiles.get(),
			methodptr = volatiles.get(),
			volptable = volatiles.get();
		
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
				int volscfo = volatiles.get();
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
				volatiles.remove(volscfo);
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
			new MethodIndex(__cl, __mn, __mt), methodptr);
			
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
		volatiles.remove(volclassid);
		volatiles.remove(volvtable);
		volatiles.remove(methodptr);
		volatiles.remove(volptable);
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
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need a volatile
		VolatileRegisterStack volatiles = this.volatiles;
		int volwantcl = volatiles.get();
		
		// Load class data
		this.__loadClassInfo(__cl, volwantcl);
		
		// Call allocator, copy to result
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS, "jvmNew",
			"(I)I", volwantcl);
		codebuilder.addCopy(NativeCode.RETURN_REGISTER, __out);
		
		// Not needed
		volatiles.remove(volwantcl);
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
		
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need volatile
		VolatileRegisterStack volatiles = this.volatiles;
		int volsmp = volatiles.get(),
			volexe = volatiles.get();
		
		// Load address of the target method
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new InvokedMethod((__it == InvokeType.SYSTEM ? InvokeType.STATIC :
			__it), new MethodHandle(__cl, __mn, __mt)), volsmp);
		
		// Load constant pool of the target class
		this.__loadClassPool(__cl, NativeCode.NEXT_POOL_REGISTER);
		
		// Create a backup of the exception register (system mode)
		if (__it == InvokeType.SYSTEM)
		{
			codebuilder.addCopy(NativeCode.EXCEPTION_REGISTER, volexe);
			codebuilder.addCopy(NativeCode.ZERO_REGISTER,
				NativeCode.EXCEPTION_REGISTER);
		}
		
		// Invoke the static pointer
		codebuilder.add(NativeInstructionType.INVOKE,
			volsmp, __args);
		
		// If the system invoke (which could be from special code) threw an
		// exception just replace our current exception with that one since
		// it definitely would be worse!
		if (__it == InvokeType.SYSTEM)
		{
			NativeCodeLabel doublefault = new NativeCodeLabel(
				"doublefault", this._refclunk++);
			codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
				doublefault);
			
			// Restore our old exception register
			codebuilder.addCopy(volexe, NativeCode.EXCEPTION_REGISTER);
			
			// Target point for double fault
			codebuilder.label(doublefault);
		}
		
		// Not needed
		volatiles.remove(volexe);
		volatiles.remove(volsmp);
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
			int rvlo = volatiles.get(),
				rvhi = volatiles.get();
			
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
			int ipcesid = volatiles.get();
			this.codebuilder.addMathConst(StackJavaType.INTEGER, MathType.ADD,
				NativeCode.ZERO_REGISTER, SystemCallIndex.EXCEPTION_STORE,
				ipcesid);
			
			// Perform system call to clear and read exception
			this.codebuilder.add(NativeInstructionType.SYSTEM_CALL,
				ipcesid, new RegisterList(NativeCode.ZERO_REGISTER));
			
			// Quickly copy out exception value
			int eval = volatiles.get();
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
			volatiles.remove(eval);
			volatiles.remove(ipcesid);
			volatiles.remove(rvhi);
			volatiles.remove(rvlo);
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
	private void __loadClassInfo(ClassName __cl, int __r)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Used for loading code
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Attempt load to the target register
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new ClassInfoPointer(__cl), __r);
		
		// Detect classes which are dynamically initialized or self
		if (!ClassLoadingAdjustments.isDeferredLoad(this.state.classname.toString(),
			__cl.toString()))
			return;
		
		// If the class is already loaded do not try loading
		NativeCodeLabel isloaded = new NativeCodeLabel("ciisloaded",
			this._refclunk++);
		codebuilder.addIfNonZero(__r, isloaded);
		
		// Need register to load the class info
		VolatileRegisterStack volatiles = this.volatiles;
		int volnoted = volatiles.get();
		
		// Load the noted class name
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new NotedString(__cl.toString()), volnoted);
		
		// Initialize the class
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmInitClass", "(I)Lcc/squirreljme/jvm/ClassInfo;", volnoted);
		
		// Store the result of the init into the pool
		codebuilder.add(NativeInstructionType.STORE_POOL,
			new ClassInfoPointer(__cl), NativeCode.RETURN_REGISTER);
		
		// Use the value of it
		codebuilder.addCopy(NativeCode.RETURN_REGISTER, __r);
		
		// Cleanup
		volatiles.remove(volnoted);
		
		// End point is here
		codebuilder.label(isloaded);
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
		int volcdvt = volatiles.get();
		this.__loadClassInfo(__cl, volcdvt);
		
		// Call internal class object loader
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmLoadClass", "(I)Ljava/lang/Class;", volcdvt);
		
		// Cleanup
		volatiles.remove(volcdvt);
		
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
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Load class pool which may already be loaded
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new ClassPool(__cl), __r);
		
		// Detect classes which are dynamically initialized
		if (!ClassLoadingAdjustments.isDeferredLoad(this.state.classname.toString(),
			__cl.toString()))
			return;
		
		// Jump if the pool is already loaded
		NativeCodeLabel isloaded = new NativeCodeLabel("piisloaded",
			this._refclunk++);
		codebuilder.addIfNonZero(__r, isloaded);
		
		// Need volatile to get the class info
		VolatileRegisterStack volatiles = this.volatiles;
		int volcinfo = volatiles.get(),
			volpoolv = volatiles.get(),
			volscfo = volatiles.get();
		
		// Load the ClassInfo for the class we want
		this.__loadClassInfo(__cl, volcinfo);
		
		// Load pool pointer from this ClassInfo
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new AccessedField(FieldAccessTime.NORMAL,
				FieldAccessType.INSTANCE,
			new FieldReference(
				new ClassName("cc/squirreljme/jvm/ClassInfo"),
				new FieldName("pool"),
				new FieldDescriptor("I"))), volscfo);
		codebuilder.addMemoryOffReg(DataType.INTEGER, true,
			volpoolv, volcinfo, volscfo);
		
		// Store it
		codebuilder.add(NativeInstructionType.STORE_POOL,
			new ClassPool(__cl), volpoolv);
		
		// Cleanup
		volatiles.remove(volcinfo);
		volatiles.remove(volpoolv);
		volatiles.remove(volscfo);
		
		// End point is here
		codebuilder.label(isloaded);
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
			volatiles.remove(v);
		}
	}
	
	/**
	 * Generates code to reference count the given register.
	 *
	 * @param __r The register to reference to count.
	 * @since 2019/04/25
	 */
	private void __refCount(int __r)
	{
		// If the object is null then it will not be counted, this is skipped
		NativeCodeLabel ncj = new NativeCodeLabel("refnocount",
			this._refclunk++);
		
		// Do not do any counting if this is zero
		NativeCodeBuilder codebuilder = this.codebuilder;
		codebuilder.addIfZero(__r, ncj);
		
		// Add count
		codebuilder.add(NativeInstructionType.ATOMIC_INT_INCREMENT,
			__r, Constants.OBJECT_COUNT_OFFSET);
		
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
			int v = volatiles.get();
			
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
	 * @param __r The register to reference to uncount.
	 * @since 2019/04/25
	 */
	private void __refUncount(int __r)
	{
		// If the object is null then it will not be uncounted, this is skipped
		NativeCodeLabel ncj = new NativeCodeLabel("refnouncount",
			this._refclunk++);
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volnowcount = volatiles.get();
		
		// Do not do any uncounting if this is null
		NativeCodeBuilder codebuilder = this.codebuilder;
		codebuilder.addIfZero(__r, ncj);
		
		// Add uncount
		codebuilder.add(NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET,
			volnowcount, __r, Constants.OBJECT_COUNT_OFFSET);
		
		// If the count is still positive, we do not GC
		codebuilder.addIfPositive(volnowcount, ncj);
		
		// Call garbage collect on object via helper
		this.__invokeStatic(InvokeType.SYSTEM,
			NearNativeByteCodeHandler.JVMFUNC_CLASS,
			"jvmGarbageCollectObject", "(I)V", __r);
		
		// Reset the variable to zero to prevent it from being used again
		codebuilder.addCopy(NativeCode.ZERO_REGISTER, __r);
		
		// No uncount or not GCed are jumped here
		codebuilder.label(ncj);
		
		// No longer needed
		volatiles.remove(volnowcount);
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

