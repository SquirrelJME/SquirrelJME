// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import cc.squirreljme.runtime.cldc.vki.FixedClassIDs;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.FieldAccessTime;
import dev.shadowtail.classfile.pool.FieldAccessType;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.UsedString;
import dev.shadowtail.classfile.xlate.ByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeState;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.ExceptionClassEnqueueAndTable;
import dev.shadowtail.classfile.xlate.ExceptionHandlerRanges;
import dev.shadowtail.classfile.xlate.ExceptionHandlerTransition;
import dev.shadowtail.classfile.xlate.ExceptionStackAndTable;
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
	/** The kernel class. */
	public static final ClassName KERNEL_CLASS =
		new ClassName("cc/squirreljme/runtime/cldc/vki/Kernel");
	
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
	
	/** Reference queue base, register wise. */
	private int _refqbase;
	
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
		this.__basicCheckIsArray(__in.register);
		
		// Read length
		codebuilder.addMemoryOffConst(DataType.INTEGER, true, __len.register,
			__in.register, Kernel.ARRAY_LENGTH_OFFSET);
		
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
			volaip, Kernel.ARRAY_BASE_SIZE, volaip);
		
		// Do the memory read
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
			volaip, Kernel.ARRAY_BASE_SIZE, volaip);
		
		// If we are storing an object....
		if (__v.type.isObject())
		{
			// Check if the array type is compatible
			this.__basicCheckArrayStore(__in.register, __v.register);
			
			// Read existing object so it can be uncounted
			codebuilder.addMemoryOffReg(DataType.INTEGER, true,
				NativeCode.RETURN_REGISTER + 1,
				__in.register, volaip);
			
			// Uncount this object
			this.__refUncount(NativeCode.RETURN_REGISTER + 1);
			
			// Count the object being stored
			this.__refCount(__v.register);
		}
		
		// Store value
		codebuilder.addMemoryOffReg(__dt, false,
			__v.register,
			__in.register, volaip);
		
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
		// Push reference
		this.__refPush();
		
		// Add cast check
		this.__basicCheckCCE(__v.register, __cl);
		
		// We do not need to uncount whatever was pushed in because it would
		// be immediately pushed back onto the stack. The counts should only
		// be lowered if ClassCastException is to be thrown. Because otherwise
		// we will just end up collecting things on a normal refclear
		this._lastenqueue = null;
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
		this.codebuilder.addConversion(__as, __a.register,
			__bs, __b.register);
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
		this.__basicCheckNPE(ireg);
		
		// Must be the given class
		this.__basicCheckCCE(ireg, __fr.className());
		
		// Determine volatile registers
		VolatileRegisterStack volatiles = this.volatiles;
		int tempreg = volatiles.get();
		
		// Read field offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr, true), tempreg);
		
		// Read from memory
		codebuilder.addMemoryOffReg(
			DataType.of(__fr.memberType().primitiveType()), true,
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
		this.__basicCheckCCE(ireg, __fr.className());
		
		// Get volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volfioff = volatiles.get();
		
		// Read field offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.INSTANCE, __fr, false),
			volfioff);
		
		// Write to memory
		codebuilder.addMemoryOffReg(
			DataType.of(__fr.memberType().primitiveType()), false,
			__v.register, ireg, volfioff);
		
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
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			__cl, volwantcldx);
		
		// Invoke helper method
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS,
			"jvmIsInstance", "(II)I", __v.register, volwantcldx);
		
		// Use result
		codebuilder.addCopy(NativeCode.RETURN_REGISTER, __o.register);
		
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
		if ("cc/squirreljme/runtime/cldc/vki/Assembly".equals(
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
		if (__t == InvokeType.STATIC)
		{
			MethodHandle mh = __r.handle();
			this.__invokeStatic(__t, mh.outerClass(), mh.name(),
				mh.descriptor(), reglist);
		}
		
		// Interface, special, or virtual
		else
		{
			// Check that the object is of the given class type and is not null
			int ireg = __in[0].register;
			this.__basicCheckNPE(ireg);
			this.__basicCheckCCE(ireg, __r.handle().outerClass());
			
			// Invoking interface method
			if (__t == InvokeType.INTERFACE)
			{
				if (true)
					throw new todo.TODO();
				
				/*
				// Load the interface class into A
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					__r.handle().outerClass(),
					NativeCode.VOLATILE_A_REGISTER);
				
				// Load the method index for the interface method into B
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new MethodIndex(__r.handle().outerClass(),
						__r.handle().name(), __r.handle().descriptor()),
					NativeCode.VOLATILE_B_REGISTER);
				
				// We can load the direct pointer into the S register
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new InvokedMethod(InvokeType.STATIC, new MethodHandle(
						new ClassName(
							"cc/squirreljme/runtime/cldc/vki/Kernel"),
						new MethodName("jvmInterfacePointer"),
						new MethodDescriptor("(III)I"))),
					NativeCode.VOLATILE_S_REGISTER);
				
				// Call kernel method to help with interface method lookup
				codebuilder.add(NativeInstructionType.INVOKE,
					NativeCode.VOLATILE_S_REGISTER,
					new RegisterList(__in[0].register, 
						NativeCode.VOLATILE_A_REGISTER,
						NativeCode.VOLATILE_B_REGISTER));
				
				// Copy it into volatile A since invoke is done on that.
				codebuilder.addCopy(NativeCode.RETURN_REGISTER,
					NativeCode.VOLATILE_A_REGISTER);
				*/
			}
			
			// Special or virtual
			else
			{
				if (true)
					throw new todo.TODO();
				
				/*
				// Use special invoke table for the instance
				String vtablename;
				if (__t == InvokeType.SPECIAL)
					vtablename = "vtablespecial";
				
				// Use virtual invoke table for the instance
				else
					vtablename = "vtablevirtual";
				
				// Load the Class ID into A
				codebuilder.addMemoryOffReg(DataType.INTEGER, true,
					NativeCode.VOLATILE_A_REGISTER,
					__in[0].register, Kernel.OBJECT_CLASS_OFFSET);
				
				// Load the ClassDataV2 pointer into A
				codebuilder.addLoadTable(
					NativeCode.VOLATILE_A_REGISTER,
					NativeCode.CLASS_TABLE_REGISTER,
					NativeCode.VOLATILE_A_REGISTER);
				
				// Read the offset to the vtable into B
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new AccessedField(FieldAccessTime.NORMAL,
						FieldAccessType.INSTANCE,
					new FieldReference(
						new ClassName(
							"cc/squirreljme/runtime/cldc/lang/ClassDataV2"),
						new FieldName(vtablename),
						FieldDescriptor.INTEGER)),
					NativeCode.VOLATILE_B_REGISTER);
				
				// Read the address of the VTable from A+B into A
				codebuilder.addMemoryOffReg(DataType.INTEGER, true,
					NativeCode.VOLATILE_A_REGISTER,
					NativeCode.VOLATILE_A_REGISTER,
					NativeCode.VOLATILE_B_REGISTER);
				
				// Read the method index for the vtable into B
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new MethodIndex(__r.handle().outerClass(),
						__r.handle().name(), __r.handle().descriptor()),
					NativeCode.VOLATILE_B_REGISTER);
				
				// Read from the vtable using the table index for B
				// This is our invocation pointer
				codebuilder.addLoadTable(
					NativeCode.VOLATILE_A_REGISTER,
					NativeCode.VOLATILE_A_REGISTER,
					NativeCode.VOLATILE_B_REGISTER);
				*/
			}
		}
		
		// Read in return value, it is just a copy
		if (__out != null)
			if (__out.type.isWide())
				codebuilder.addCopyWide(NativeCode.RETURN_REGISTER,
					__out.register);
			else
				codebuilder.addCopy(NativeCode.RETURN_REGISTER,
					__out.register);
		
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
		this.__invokeStatic(InvokeType.STATIC,
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
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volclassdx = volatiles.get();
		
		// If not a fixed class index, then rely on the value in the pool
		int wantfixedtype = FixedClassIDs.of(__at.toString());
		if (wantfixedtype < 0)
			codebuilder.add(NativeInstructionType.LOAD_POOL,
				__at, volclassdx);
		
		// Otherwise use the fixed class identifier
		else
			codebuilder.addMathConst(StackJavaType.INTEGER, MathType.OR,
				0, wantfixedtype, volclassdx);
		
		// Call internal handler
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS, "jvmNewArray",
			"(II)I", volclassdx, __len.register);
		
		// Copy result
		codebuilder.addCopy(NativeCode.RETURN_REGISTER, __out.register);
		
		// No longer needed
		volatiles.remove(volclassdx);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final void doPoolLoad(Object __v, JavaStackResult.Output __out)
	{
		this.codebuilder.add(NativeInstructionType.LOAD_POOL,
			(__v instanceof String ? new UsedString(__v.toString()) : __v),
			__out.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doReturn(JavaStackResult.Input __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Returning a value? Copy it to the return register
		if (__in != null)
			if (__in.type.isWide())
				codebuilder.addCopyWide(__in.register,
					NativeCode.RETURN_REGISTER);
			else
				codebuilder.addCopy(__in.register,
					NativeCode.RETURN_REGISTER);
		
		// Uncount anything which was enqueued
		for (int q : this.state.result.enqueue())
			this.__refUncount(q);
		
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
					codebuilder.addCopyWide(op.a, op.b);
					break;
				
				default:
					throw new todo.OOPS();
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
		state.canexception = false;
		
		// Push references
		this.__refPush();
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volsfo = volatiles.get();
		
		// Read static offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.STATIC, __fr, true),
			volsfo);
		
		// Read from memory
		codebuilder.addMemoryOffReg(
			DataType.of(__fr.memberType().primitiveType()), true,
			__v.register, NativeCode.STATIC_FIELD_REGISTER,
			volsfo);
		
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
		state.canexception = false;

		// Push references
		this.__refPush();
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volsfo = volatiles.get();
		
		// Read field offset
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			this.__fieldAccess(FieldAccessType.STATIC, __fr, false),
			volsfo);
		
		// Write to memory
		codebuilder.addMemoryOffReg(
			DataType.of(__fr.memberType().primitiveType()), false,
			__v.register, NativeCode.STATIC_FIELD_REGISTER,
			volsfo);
		
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
		this._lastenqueue = null;
		
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
		
		// {@squirreljme.error JC49 Enqueues were not cleared, this is an
		// internal compiler error.}
		if (this._lastenqueue != null)
			throw new IllegalStateException("JC49");
		
		// An exception check was requested, do a check on the exception
		// register and jump if there is something there
		if (state.canexception)
			codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
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
				state.classname.toString(),
				state.methodname.toString(),
				state.methodtype.toString());
			
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
		
		// Temporary register base
		int tempreg = state.stack.usedregisters;
		
		// Was an exception handler generated?
		boolean didehfall = false;
		
		// Generate reference clear jumps
		Map<EnqueueAndLabel, __EData__> refcljumps = this._refcljumps;
		for (Map.Entry<EnqueueAndLabel, __EData__> e : refcljumps.entrySet())
		{
			EnqueueAndLabel eql = e.getKey();
			
			// Set label target for this one
			codebuilder.label(this.__useEDataAndGetLabel(e.getValue()));
			
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
			
			// The class name used
			ClassName exn = csl.classname;
			
			if (true)
				throw new todo.TODO();
			/*
			// Allocate exception at the highest register point which acts
			// as a temporary
			codebuilder.add(NativeInstructionType.NEW, exn,
				NativeCode.EXCEPTION_REGISTER);
				
			// Load invocation pointer
			codebuilder.add(NativeInstructionType.LOAD_POOL,
				new InvokedMethod(InvokeType.SPECIAL, new MethodHandle(exn,
					new MethodName("<init>"), new MethodDescriptor("()V"))),
				NativeCode.VOLATILE_A_REGISTER);
			
			// Initialize object with constructor
			codebuilder.add(NativeInstructionType.INVOKE,
				NativeCode.VOLATILE_A_REGISTER,
				new RegisterList(NativeCode.EXCEPTION_REGISTER));
			
			// Generate jump to exception handler
			codebuilder.addGoto(csl.label);
			*/
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
				if (true)
					throw new todo.TODO();
				
				/*
				// The method we are going to call is in the kernel so we need
				// to load its pool identifier
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new ClassPool(KERNEL_CLASS),
					NativeCode.NEXT_POOL_REGISTER);
				
				// Load the class index into the temporary
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					eh.type(), NativeCode.VOLATILE_A_REGISTER);
				
				// Load address of target method
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new InvokedMethod(InvokeType.STATIC, new MethodHandle(
					KERNEL_CLASS, new MethodName("jvmIsInstance"),
					new MethodDescriptor("(II)I"))),
					NativeCode.VOLATILE_B_REGISTER);
				
				// Load kernel pool
				codebuilder.add(NativeInstructionType.LOAD_POOL,
					new ClassPool(KERNEL_CLASS),
					NativeCode.NEXT_POOL_REGISTER);
				
				// Call the instance checker (__ir, checkclassid)
				codebuilder.add(NativeInstructionType.INVOKE,
					NativeCode.VOLATILE_B_REGISTER, new RegisterList(
					NativeCode.EXCEPTION_REGISTER,
					NativeCode.VOLATILE_A_REGISTER));
				
				// Jump to handler if it is met
				codebuilder.addIfZero(NativeCode.RETURN_REGISTER,
					this.__labelJavaTransition(sops,
						new InstructionJumpTarget(eh.handlerAddress())));
				*/
			}
			
			// No exception handler is available so, just fall through to the
			// caller as needed
			this.__generateReturn(enq);
			
			// Exception handler was generated
			didehfall = true;
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
	private final void __basicCheckArrayBound(int __ir, int __dxr)
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
			__ir, Kernel.ARRAY_LENGTH_OFFSET);
		
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
	private final void __basicCheckArrayStore(int __ir, int __vr)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Call helper class
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS,
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
	private final void __basicCheckCCE(int __ir, ClassName __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
			
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volwantcldx = volatiles.get();
		
		// Load desired target class type
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			__cl, volwantcldx);
		
		// Call helper class
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS,
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
	 * Checks that the given object is an array.
	 *
	 * @param __ir The type to check.
	 * @since 2019/04/27
	 */
	private final void __basicCheckIsArray(int __ir)
	{
		// Call internal helper
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS, "jvmIsArray",
			"(I)I", __ir);
		
		// If this is not an array, throw a class cast exception
		codebuilder.addIfZero(NativeCode.RETURN_REGISTER,
			this.__labelRefClearJump(this.__labelMakeException(
			"java/lang/ClassCastException")));
	}
	
	/**
	 * Basic check if the instance is null.
	 *
	 * @param __ir The register to check.
	 * @since 2019/04/22
	 */
	private final void __basicCheckNPE(int __ir)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Just a plain zero check
		codebuilder.addIfZero(__ir, this.__labelRefClearJump(
			this.__labelMakeException("java/lang/NullPointerException")));
	}
	
	/**
	 * Makes an EData for the current position and label.
	 *
	 * @param __lab The stored label.
	 * @return The made EData.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/26
	 */
	private final __EData__ __eData(NativeCodeLabel __lab)
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
	private final AccessedField __fieldAccess(FieldAccessType __at,
		FieldReference __fr, boolean __read)
		throws NullPointerException
	{
		if (__at == null || __fr == null)
			throw new NullPointerException("NARG");
		
		// Accessing final fields of another class will always be treated as
		// normal despite being in the constructor of a class
		if (!state.classname.equals(__fr.className()))
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
			// If this is synchronized, we need to exit the monitor
			if (this.issynchronized)
			{
				this.__monitor(false, this.monitortarget);
				this.__refUncount(this.monitortarget);
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
		
		// Since the enqueue list is not empty, we can just trim a register
		// from the top and recursively go down
		// So uncount the top
		this.__refUncount(__eq.top());
		
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
	private final void __invokeAssembly(MethodName __name,
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
				
				// Breakpoint
			case "breakpoint":
				codebuilder.add(NativeInstructionType.BREAKPOINT);
				break;
				
				// Long/Double bits
			case "doubleToRawLongBits":
			case "longBitsToDouble":
				if (__in[0].register != __out.register)
					codebuilder.addCopyWide(__in[0].register,
						__out.register);
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
					codebuilder.addCopy(__in[0].register, __out.register);
				break;
				
				// Invoke method, no return value is read
			case "invoke":
				{
					// Invoked methods can thrown an exception, so do
					// checks! Otherwise the behavior we expect might not
					// happen
					this.state.canexception = true;
					
					// Build the register List
					List<Integer> args = new ArrayList<>();
					int n = __in.length;
					for (int i = 1; i < n; i++)
						args.add(__in[i].register);
					
					// Invoke pointer with arguments
					codebuilder.add(NativeInstructionType.INVOKE,
						__in[0].register, new RegisterList(args));
				}
				break;
				
				// Invoke method, then read return value
			case "invokeV":
				{
					// Invoked methods can thrown an exception, so do
					// checks! Otherwise the behavior we expect might not
					// happen
					this.state.canexception = true;
					
					// Build the register List
					List<Integer> args = new ArrayList<>();
					int n = __in.length;
					for (int i = 1; i < n; i++)
						args.add(__in[i].register);
					
					// Invoke pointer with arguments
					codebuilder.add(NativeInstructionType.INVOKE,
						__in[0].register, new RegisterList(args));
					
					// Copy return value
					codebuilder.addCopy(NativeCode.RETURN_REGISTER,
						__out.register);
				}
				break;
				
				// Load value from class table
			case "loadClass":
				codebuilder.addLoadTable(__out.register,
					NativeCode.CLASS_TABLE_REGISTER, __in[0].register);
				break;
				
				// Load value from constant pool
			case "loadPool":
				codebuilder.addLoadTable(__out.register,
					NativeCode.POOL_REGISTER, __in[0].register);
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
				
				// Write short memory
			case "memWriteShort":
				codebuilder.addMemoryOffReg(DataType.SHORT,
					false, __in[2].register,
					__in[0].register, __in[1].register);
				break;
			
				// object -> pointer
			case "objectToPointer":
				if (__in[0].register != __out.register)
					codebuilder.addCopy(__in[0].register, __out.register);
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
			
				// pointer -> object
			case "pointerToObject":
				if (__in[0].register != __out.register)
					codebuilder.addCopy(__in[0].register, __out.register);
				
				// The returned object is electable for reference
				// counting so we need to count it up otherwise it will
				// be just freed (this is just a plain copy)
				this.__refCount(__out.register);
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
				
				// Get class table register
			case "specialGetClassTableRegister":
				codebuilder.addCopy(NativeCode.CLASS_TABLE_REGISTER,
					__out.register);
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
				
				// Set class table register
			case "specialSetClassTableRegister":
				codebuilder.addCopy(__in[0].register,
					NativeCode.CLASS_TABLE_REGISTER);
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
				
				// System call
			case "sysCall":
				{
					// Invoked methods can thrown an exception, so do
					// checks! Otherwise the behavior we expect might not
					// happen
					this.state.canexception = true;
					
					// Build the register List
					List<Integer> args = new ArrayList<>();
					int n = __in.length;
					for (int i = 1; i < n; i++)
						args.add(__in[i].register);
					
					// Invoke pointer with arguments
					codebuilder.add(NativeInstructionType.SYSTEM_CALL,
						__in[0].register, new RegisterList(args));
				}
				break;
				
				// System call with return value
			case "sysCallV":
				{
					// Invoked methods can thrown an exception, so do
					// checks! Otherwise the behavior we expect might not
					// happen
					this.state.canexception = true;
					
					// Build the register List
					List<Integer> args = new ArrayList<>();
					int n = __in.length;
					for (int i = 1; i < n; i++)
						args.add(__in[i].register);
					
					// Invoke pointer with arguments
					codebuilder.add(NativeInstructionType.SYSTEM_CALL,
						__in[0].register, new RegisterList(args));
					
					// Copy return value
					codebuilder.addCopy(NativeCode.RETURN_REGISTER,
						__out.register);
				}
				break;
			
			default:
				throw new todo.OOPS(asmfunc);
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
	private final void __invokeStatic(InvokeType __it, String __cl,
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
	private final void __invokeStatic(InvokeType __it, String __cl,
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
	private final void __invokeStatic(InvokeType __it, ClassName __cl,
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
	private final void __invokeStatic(InvokeType __it, ClassName __cl,
		MethodName __mn, MethodDescriptor __mt, RegisterList __args)
		throws NullPointerException
	{
		if (__it == null || __cl == null || __mn == null || __mt == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Need volatile
		VolatileRegisterStack volatiles = this.volatiles;
		int volsmp = volatiles.get();
		
		// Load address of the target method
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new InvokedMethod(__it, new MethodHandle(__cl, __mn, __mt)),
			volsmp);
		
		// Load constant pool of the target class
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new ClassPool(__cl), NativeCode.NEXT_POOL_REGISTER);
		
		// Invoke the static pointer
		codebuilder.add(NativeInstructionType.INVOKE,
			volsmp, __args);
		
		// Not needed
		volatiles.remove(volsmp);
	}
	
	/**
	 * Creates and stores an exception.
	 *
	 * @return The label to the exception.
	 * @since 2019/04/09
	 */
	private final NativeCodeLabel __labelException()
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
	private final NativeCodeLabel __labelJavaTransition(StateOperations __sops,
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
	private final NativeCodeLabel __labelMakeException(String __cl)
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
	private final NativeCodeLabel __labelRefClearJump(NativeCodeLabel __tl)
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
	private final NativeCodeLabel __labelRefClearJump(
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
	 * Loads the Class object for the given class into the given register.
	 *
	 * @param __cl The class to load.
	 * @param __r The register to place it in.
	 * @since 2019/04/26
	 */
	private final void __loadClassObject(ClassName __cl, int __r)
	{
		throw new todo.TODO();
		
		/*
		// Need to calculate the offset of the class in the class table
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			__cl, __r);
		
		// Read from the class table the ClassDataV2 object
		codebuilder.addLoadTable(
			__r, NativeCode.CLASS_TABLE_REGISTER, __r);
		
		// Load field offset from the pool
		codebuilder.add(NativeInstructionType.LOAD_POOL,
			new AccessedField(FieldAccessTime.NORMAL, FieldAccessType.INSTANCE,
			new FieldReference(
				new ClassName("cc/squirreljme/runtime/cldc/lang/ClassDataV2"),
				new FieldName("classobjptr"), FieldDescriptor.INTEGER)),
			NativeCode.VOLATILE_A_REGISTER);
		
		// Read from the field into the register
		codebuilder.addMemoryOffReg(
			DataType.OBJECT, true,
			__r, __r, NativeCode.VOLATILE_A_REGISTER);
		*/
	}
	
	/**
	 * Enters the monitor.
	 *
	 * @param __enter Is the monitor being entered? 
	 * @param __r The register to enter a monitor for.
	 * @since 2019/04/26
	 */
	private final void __monitor(boolean __enter, int __r)
	{
		// Call helper method
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS,
			(__enter ? "jvmMonitorEnter" : "jvmMonitorExit"), "(I)V", __r);
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
		
		// No need to clear anymore
		this._lastenqueue = null;
		
		// Position where all the enqueued values were stored
		int refqbase = this._refqbase;
		
		// Un-count all of them accordingly
		NativeCodeBuilder codebuilder = this.codebuilder;
		for (int i = 0, n = lastenqueue.size(); i < n; i++)
			this.__refUncount(refqbase + i);
	}
	
	/**
	 * Generates code to reference count the given register.
	 *
	 * @param __r The register to reference to count.
	 * @since 2019/04/25
	 */
	private final void __refCount(int __r)
	{
		// If the object is null then it will not be counted, this is skipped
		NativeCodeLabel ncj = new NativeCodeLabel("refnocount",
			this._refclunk++);
		
		// Do not do any counting if this is zero
		NativeCodeBuilder codebuilder = this.codebuilder;
		codebuilder.addIfZero(__r, ncj);
		
		// Add count
		codebuilder.add(NativeInstructionType.ATOMIC_INT_INCREMENT,
			__r, Kernel.OBJECT_COUNT_OFFSET);
		
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
		
		// Register base to use for the reference queue
		int refqbase = this.state.stack.usedregisters + 4;
		this._refqbase = refqbase;
		
		// Copy all references to the temporary spots
		NativeCodeBuilder codebuilder = this.codebuilder;
		for (int i = 0, n = __r.size(); i < n; i++)
			codebuilder.addCopy(__r.get(i), refqbase + i);
		
		// These will be uncounted accordingly
		this._lastenqueue = __r;
		
		// Did enqueue something
		return true;
	}
	
	/**
	 * Generates code to reference uncount the given register.
	 *
	 * @param __r The register to reference to uncount.
	 * @since 2019/04/25
	 */
	private final void __refUncount(int __r)
	{
		// If the object is null then it will not be uncounted, this is skipped
		NativeCodeLabel ncj = new NativeCodeLabel("refnouncount",
			this._refclunk++);
		
		// Need volatiles
		VolatileRegisterStack volatiles = this.volatiles;
		int volnowcount = volatiles.get();
		
		// Do not do any uncounting if this is zero
		NativeCodeBuilder codebuilder = this.codebuilder;
		codebuilder.addIfZero(__r, ncj);
		
		// Add uncount
		codebuilder.add(NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET,
			volnowcount, __r, Kernel.OBJECT_COUNT_OFFSET);
		
		// If the count is still positive, we do not GC
		codebuilder.addIfNonZero(volnowcount, ncj);
		
		// Call garbage collect on object via helper
		this.__invokeStatic(InvokeType.STATIC, KERNEL_CLASS,
			"jvmGarbageCollectObject", "(I)V", __r);
		
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
	private final NativeCodeLabel __useEDataAndGetLabel(__EData__ __ed)
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
}

