// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.linkage.MethodLinkage;

/**
 * This is the base class for the JIT translation engine and is used to
 * translate class files to {@link ExecutableClass}es which may be executed
 * directly by the system, cached for later, or act as part of the kernel build
 * step.
 *
 * @since 2017/01/30
 */
public abstract class TranslationEngine
{
	/** The configuration used. */
	protected final JITConfig<?> config;
	
	/** The accessor to the JIT. */
	protected final JITStateAccessor accessor;
	
	/**
	 * Initializes the base translation engine.
	 *
	 * @param __c The configuration.
	 * @param __jsa The JIT state accessor which is used to communicate and
	 * access the JIT directly.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public TranslationEngine(JITConfig<?> __c, JITStateAccessor __jsa)
		throws NullPointerException
	{
		// Check
		if (__c == null || __jsa == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __c;
		this.accessor = __jsa;
	}
	
	/**
	 * Returns the allocations for entry into a method with the specified set
	 * of arguments.
	 *
	 * @param __t The arguments of the method.
	 * @return The allocations for the method entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public abstract ArgumentAllocation[] allocationForEntry(StackMapType[] __t)
		throws NullPointerException;
	
	/**
	 * Invokes the specified method with the given arguments, returning the
	 * value in the specified slot.
	 *
	 * If a return value is specified, it will always be on the stack. Note
	 * that there may be an existing value at this location.
	 *
	 * @param __in Input state
	 * @param __out Output state.
	 * @param __ml The method to be invoked.
	 * @param __rv Where the return value is placed, may be {@code null} if
	 * there is no return value.
	 * @param __args Arguments to the method, will be their cached and aliased
	 * slots. Arguments are used as input.
	 * @param __allocs Allocations used for the target method call.
	 * @throws NullPointerException If no input state, output state, method, or
	 * arguments were specified.
	 * @since 2017/03/03
	 */
	public abstract void invokeMethod(CacheState __in, ActiveCacheState __out,
		MethodLinkage __ml, ActiveCacheState.Slot __rv,
		CacheState.Slot[] __args, ArgumentAllocation[] __allocs)
		throws NullPointerException;
	
	/**
	 * Checks if the specified register is an argument register.
	 *
	 * @param __r The register to check to see if it is an argument regster.
	 * @return {@code true} if the register is an argument register.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public abstract boolean isRegisterArgument(Register __r)
		throws NullPointerException;
	
	/**
	 * Checks if the specified register is a callee saved register, one that
	 * is saved at the start of a method and restored before it returns.
	 *
	 * @param __r The register to check to see if it is saved.
	 * @return {@code true} if the register is saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public abstract boolean isRegisterSaved(Register __r)
		throws NullPointerException;
	
	/**
	 * Checks if the specified register is a caller saved register, one that
	 * is saved before a method call and restored following it.
	 *
	 * @param __r The register to check to see if it is temporary.
	 * @return {@code true} if the register is temporary.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	public abstract boolean isRegisterTemporary(Register __r)
		throws NullPointerException;
	
	/**
	 * This stores the register to the specified offset with the given register
	 * at its base.
	 *
	 * @param __t The type of data to be stored.
	 * @param __src The source register.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the store is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public abstract void storeRegister(DataType __t, Register __src,
		int __off, Register __base)
		throws JITException, NullPointerException;
	
	/**
	 * This translates the specified stack type to the given data type, this
	 * is used for allocating space on the stack to store the value.
	 *
	 * @param __t The stack type to translate to a data type.
	 * @return The data type for the given stack type.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/11
	 */
	public abstract DataType toDataType(StackMapType __t)
		throws NullPointerException;
	
	/**
	 * This returns the configuration that the translation engine was
	 * initialized with.
	 *
	 * @param <C> The class to cast to.
	 * @param __cl The class to cast to.
	 * @return The configuration to use for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public final <C extends JITConfig<C>> JITConfig<C> config(Class<C> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this.config);
	}
}

