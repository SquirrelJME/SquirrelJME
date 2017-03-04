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
	
	/**
	 * Binds the specified cache state for entry into the current method from
	 * an external caller.
	 *
	 * @param __cs The state to bind variables for.
	 * @since 2017/02/19
	 */
	public abstract void bindStateForEntry(ActiveCacheState __cs);
	
	/**
	 * Creates an active binding.
	 *
	 * @return The newly created active binding.
	 * @since 2017/02/23
	 */
	public abstract ActiveBinding createActiveBinding();
	
	/**
	 * Invokes the specified method with the given arguments, returning the
	 * value in the specified slot.
	 *
	 * If a return value is specified, it will always be on the stack. Note
	 * that there may be an existing value at this location.
	 *
	 * @param __ml The method to be invoked.
	 * @param __rv Where the return value is placed, may be {@code null} if
	 * there is no return value.
	 * @param __args Arguments to the method, will be their cached and aliased
	 * slots.
	 * @throws NullPointerException If no method or arguments were specified.
	 * @since 2017/03/03
	 */
	public abstract void invokeMethod(MethodLinkage __ml,
		ActiveCacheState.Slot __rv, ActiveCacheState.Slot[] __args)
		throws NullPointerException;
	
	/**
	 * Creates a snapshot of the specified binding returning an immutable one
	 *
	 * @param __b The binding to snapshot.
	 * @return The snapshotted binding.
	 * @since 2017/03/01
	 */
	public abstract SnapshotBinding snapshotBinding(Binding __b)
		throws NullPointerException;
}

