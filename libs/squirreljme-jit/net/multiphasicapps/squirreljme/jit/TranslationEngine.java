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
	
	/**
	 * Initializes the base translation engine.
	 *
	 * @param __c The configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public TranslationEngine(JITConfig<?> __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __c;
	}
	
	/**
	 * Initializes an empty cell allocator which is used to allocate stack
	 * and registers.
	 *
	 * When a method or other special call must be performed, a new cell
	 * allocator will be initialized and setup so that the state required for
	 * the method call is known.
	 *
	 * @return The newly created cell allocator.
	 * @since 2017/02/11
	 */
	public abstract CellAllocator createCellAllocator();
	
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

