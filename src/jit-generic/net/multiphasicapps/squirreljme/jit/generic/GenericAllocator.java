// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocator;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterFloatType;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterIntegerType;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterKind;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterType;
import net.multiphasicapps.util.msd.MultiSetDeque;

/**
 * This class as the allocator which bridges the stack-cached singular Java
 * variables to the registers used on an actual machine.
 *
 * @since 2016/09/09
 */
public class GenericAllocator
{
	/** The configuration used. */
	protected final JITOutputConfig.Immutable config;
	
	/** The ABI used. */
	protected final NativeABI abi;
	
	/** The native allocator. */
	protected final NativeAllocator allocator;
	
	/** Registers bound to local variables. */
	volatile __VarStates__ _jlocals;
	
	/** Registers bound to stack variables. */
	volatile __VarStates__ _jstack;
	
	/**
	 * This initializes the allocator.
	 *
	 * @parma __conf The JIT configuration.
	 * @param __abi The ABI used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	GenericAllocator(JITOutputConfig.Immutable __conf, NativeABI __abi)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __abi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.abi = __abi;
		this.allocator = new NativeAllocator(__abi);
	}
	
	/**
	 * Primes the method arguments and sets the initial state that is used
	 * on entry of a method.
	 *
	 * @param __eh Are there exception handlers present?
	 * @param __t The arguments to the method.
	 * @throws JITException If they could not be primed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	public void primeArguments(boolean __eh, StackMapType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Fill input arguments based on the type of arguments the method uses
		List<NativeRegisterType> args = new ArrayList<>();
		if (true)
			throw new Error("TODO");
		
		// Get allocations
		NativeAllocator allocator = this.allocator;
		NativeAllocation[] nas = allocator.argumentAllocate(args.
			<NativeRegisterType>toArray(new NativeRegisterType[args.size()]));
		
		// If this method has an exception handler then local variables are
		// copied to the native stack whenever their values change. This
		// simplifies exception handling.
		if (__eh)
			throw new Error("TODO");
		
		// Take this information and place it into the appropriate set of
		// local variables
		__VarStates__ jlocals = this._jlocals;
		if (true)
			throw new Error("TODO");
		
		// Debug
		System.err.printf("DEBUG -- AllocState: %s%n", this);
	}
	
	/**
	 * Returns the current state of the allocator.
	 *
	 * @return The current allocator state.
	 * @since 2016/09/09
	 */
	public final GenericAllocatorState recordState()
	{
		return new GenericAllocatorState(this);
	}
	
	/**
	 * This registers temporary and local variable slots which are assigned
	 * to registers in a method.
	 *
	 * @param __stack The number of stack entries.
	 * @param __locals The number of local variables.
	 * @throws JITException If they could not be counted.
	 * @since 2016/09/03
	 */
	public final void variableCounts(int __stack, int __locals)
		throws JITException
	{
		// {@squirreljme.error AR1h The number of stack and/or local variables
		// has a negative count. (The stack variable count; The local variable
		// count)}
		if (__stack < 0 || __locals < 0)
			throw new JITException(String.format("AR1h %d %d", __stack,
				__locals));
		
		// Initialize state
		this._jlocals = new __VarStates__(__locals, true);
		this._jstack = new __VarStates__(__stack, false);
	}
}

