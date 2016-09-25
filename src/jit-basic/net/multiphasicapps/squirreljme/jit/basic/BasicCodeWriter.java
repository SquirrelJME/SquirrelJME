// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.classformat.MethodInvokeType;
import net.multiphasicapps.squirreljme.classformat.MethodLinkage;
import net.multiphasicapps.squirreljme.classformat.MethodReference;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITCodeWriter;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocationType;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocator;
import net.multiphasicapps.squirreljme.nativecode.NativeArgumentInput;
import net.multiphasicapps.squirreljme.nativecode.NativeArgumentOutput;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterFloatType;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterIntegerType;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterType;
import net.multiphasicapps.util.boolset.BooleanSet;
import net.multiphasicapps.util.boolset.FixedSizeBooleanSet;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is used to parse stack cached Java byte code to produce native machine
 * code from it.
 *
 * @since 2016/09/14
 */
public class BasicCodeWriter
	implements JITCodeWriter
{
	/** The owning namespace. */
	protected final BasicNamespaceWriter namespace;
	
	/** Where native machine code is to be placed. */
	protected final ExtendedDataOutputStream output;
	
	/** The constant pool to be utilized. */
	protected final BasicConstantPool pool;
	
	/** Native code generator. */
	protected final NativeCodeWriter writer;
	
	/** Native writer options. */
	protected final NativeCodeWriterOptions options;
	
	/** Native register and stack allocator. */
	protected final NativeAllocator allocator;
	
	/** The JIT configuration .*/
	protected final JITConfig config;
	
	/** Variable to allocation mapping. */
	private final Map<CodeVariable, NativeAllocation> _vartoalloc =
		new SortedTreeMap<>();
	
	/** Jump targets where entry state must be stored. */
	private volatile BooleanSet _jumptargets;
	
	/** The current piece of code being written. */
	final __Code__ _code;
	
	/**
	 * Initializes the code writer.
	 *
	 * @param __ns The writer for namespaces.
	 * @param __o Where native machine code is to be placed.
	 * @param __c Method code table holder.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	BasicCodeWriter(BasicNamespaceWriter __ns, ExtendedDataOutputStream __o,
		__Code__ __c)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __o == null || __c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __ns;
		this.output = __o;
		this._code = __c;
		this.config = __ns._config;
		this.pool = __ns._pool;
		
		// Create code writer instance
		NativeCodeWriter writer;
		this.writer = (writer = __ns._jitoutput.createCodeWriter(__o));
		NativeCodeWriterOptions options;
		this.options = (options = writer.options());
		
		// Create allocator
		this.allocator = options.createAllocator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		// If this is a jump target then restore the register allocation state
		if (this._jumptargets.get(__pos))
		{
			// Restore state
			if (true)
				throw new Error("TODO");
		
			// Record native code entry position
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public void codeLength(int __n)
	{
		// Valid jump target positions
		this._jumptargets = new FixedSizeBooleanSet(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public void copy(StackMapType __type, CodeVariable __from,
		CodeVariable __to)
		throws NullPointerException
	{
		// Check
		if (__type == null || __from == null || __to == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Converts a field type to a register type.
	 *
	 * @param __f The field symbol to convert.
	 * @return The native register type for the given field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public NativeRegisterType fieldToRegisterType(FieldSymbol __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__f.toString())
		{
				// Always use the default type because arguments are passed
				// to methods as if they were int, despite if it was boolean
				// However, return values are masked to their size.
			default:
				return stackMapToRegisterType(StackMapType.bySymbol(__f));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public void invokeMethod(MethodLinkage __link, int __d, CodeVariable __rv,
		StackMapType __rvt, CodeVariable[] __args, StackMapType[] __targs)
		throws NullPointerException
	{
		// Check
		if (__link == null || __args == null || __targs == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Basic Invoke %s (%s) -> %s%n",
			__link, Arrays.<CodeVariable>asList(__args), __rv);
		
		// Get variable allocated, needed for copying values around later
		Map<CodeVariable, NativeAllocation> vartoalloc = this._vartoalloc;
		
		// Debug
		System.err.printf("DEBUG -- Invoke initial VTA: %s%n", vartoalloc);
		
		// Will need to write the instructions
		NativeCodeWriter writer = this.writer;
		
		// Original allocations of temporaries before they were saved, this is
		// used for method invocations since values may be cached. A cached
		// local could be in a temporary (if say the argument registers are
		// temporary)
		Map<CodeVariable, NativeAllocation> origtemp = new LinkedHashMap<>();
		
		// Go through all allocations and allocate any temporary registers
		// and store their value onto the stack
		NativeABI abi = this.options.abi();
		NativeAllocator allocator = this.allocator;
		for (Map.Entry<CodeVariable, NativeAllocation> e :
			vartoalloc.entrySet())
		{
			CodeVariable k = e.getKey();
			NativeAllocation v = e.getValue();
			
			// If the allocation is already on the stack then nothing must
			// be copied
			if (!v.useRegisters())
				continue;
			
			// If the variable to be stored is allocated to a stack
			// variable then do not store it if it exceeds the stack depth.
			// These values are popped into arguments and as such are lost
			// in the method call (assuming they are not cached)
			if (k.isStack() && k.id() >= __d)
				continue;
			
			// Determine if the allocation for this variable is using any
			// temporary registers.
			boolean hastemp = false;
			for (NativeRegister r : v.registers())
				if (abi.isTemporary(r))
				{
					hastemp = true;
					break;
				}
			
			// Save any temporary registers which may be used
			if (hastemp)
			{
				// Allocate the value onto the stack
				NativeAllocation ons = allocator.allocate(null,
					NativeAllocationType.STACK, v.valueType(), true);
				
				// Debug
				System.err.printf("DEBUG -- Save temporary %s -> %s%n", k,
					ons);
				
				// Copy the stored value to the stack
				writer.copy(v, ons);
				
				// Update the allocation state to indicate that the value
				// is now on the stack
				vartoalloc.put(k, ons);
				
				// Store original temporary
				origtemp.put(k, v);
			}
		}
		
		// The top of the stack is needed to set the new stack base
		int stacksz = allocator.stackSize();
		
		// Debug
		System.err.printf("DEBUG -- Invoke VTA after save: %s (changed: %s)" +
			", stack size=%d%n", vartoalloc, origtemp, stacksz);
		
		// Allocate arguments that are needed for a method call to be setup
		NativeArgumentOutput<Integer>[] pargs = __allocateArguments(false,
			__targs);
		
		// Debug
		System.err.printf("DEBUG -- Call spec: %s%n", Arrays.asList(pargs));
		
		// Copy register values to target allocation positions from the cached
		// sources as required.
		int n = __args.length;
		for (int i = 0; i < n; i++)
		{
			// The variable to pass
			CodeVariable pass = __args[i];
			
			throw new Error("TODO");
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void jumpTargets(int[] __t)
		throws NullPointerException
	{
		// Fill in jump targets
		BooleanSet jumptargets = this._jumptargets;
		for (int i : __t)
			jumptargets.set(i, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void primeArguments(StackMapType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Allocate the input arguments
		NativeArgumentOutput<Integer>[] pargs = __allocateArguments(true, __t);
		
		// Debug
		System.err.printf("DEBUG -- Primed args in: %s -> %s%n",
			Arrays.<StackMapType>asList(__t), Arrays.asList(pargs));
		
		// Map code variables to argument positions
		Map<CodeVariable, NativeAllocation> vartoalloc = this._vartoalloc;
		int n = pargs.length;
		for (int i = 0; i < n; i++)
		{
			// Get output
			NativeArgumentOutput<Integer> out = pargs[i];
			
			// Store
			vartoalloc.put(CodeVariable.of(false, out.special()),
				out.allocation());
		}
		
		// Debug
		System.err.printf("DEBUG -- Primed args alloced: %s%n", vartoalloc);
	}
	
	/**
	 * Converts a Java stack map type to a native register type.
	 *
	 * @param __t The type to convert.
	 * @return The register type for the given register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public NativeRegisterType stackMapToRegisterType(StackMapType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		JITConfig config = this.config;
		JITTriplet triplet = config.triplet();
		NativeTarget nativetarget = triplet.nativeTarget();
		NativeFloatType floating = nativetarget.floatingPoint();
		switch (__t)
		{
				// As-is
			case INTEGER: return NativeRegisterIntegerType.INTEGER;
			case LONG: return NativeRegisterIntegerType.LONG;
			
				// Depends on the pointer size
			case OBJECT:
				switch (nativetarget.bits())
				{
					case 8: return NativeRegisterIntegerType.BYTE;
					case 16: return NativeRegisterIntegerType.SHORT;
					case 32: return NativeRegisterIntegerType.INTEGER;
					case 64: return NativeRegisterIntegerType.LONG;
					
						// Unknown
					default:
						throw new RuntimeException("OOPS");
				}
				
				// Could be in software
			case FLOAT:
				if (floating.isHardwareFloat())
					return NativeRegisterFloatType.FLOAT;
				return NativeRegisterIntegerType.INTEGER;
			
				// Could also be in software
			case DOUBLE:
				if (floating.isHardwareDouble())
					return NativeRegisterFloatType.DOUBLE;
				return NativeRegisterIntegerType.LONG;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void variableCounts(int __ms, int __ml)
	{
		// Ignore this
	}
	
	/**
	 * This allocates
	 *
	 * @param __store Should the return allocations be stored in the allocator?
	 * @param __t The types to allocate.
	 * @return The allocation positions of the input types and the associated
	 * index values based on the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/25
	 */
	private NativeArgumentOutput<Integer>[] __allocateArguments(
		boolean __store, StackMapType... __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Map input types to register types, values are mapped to their
		// original input index, which permits top to be ignored.
		List<NativeArgumentInput<Integer>> in = new ArrayList<>();
		int n = __t.length;
		for (int i = 0; i < n; i++)
		{
			StackMapType t = __t[i];
			
			// Add input
			in.add(new NativeArgumentInput<>(stackMapToRegisterType(t),
				Integer.valueOf(i)));
			
			// Skip top if this is wide
			if (t.isWide())
				i++;
		}
		
		// Perform allocation
		return this.allocator.<Integer>argumentAllocate(__store,
			in.<NativeArgumentInput<Integer>>toArray(
			NativeArgumentInput.<Integer>allocateArray(in.size())));
	}
}

