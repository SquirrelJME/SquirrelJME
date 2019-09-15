// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * This contains the dual class and run-time constant pool information, the
 * run-time relies on the main class one, however only the run-time one is
 * needed for execution.
 *
 * @since 2019/07/17
 */
public final class DualClassRuntimePool
{
	/** The class pool. */
	protected final BasicPool classpool;
	
	/** The run-time pool. */
	protected final BasicPool runpool;
	
	/**
	 * Initializes the dual class/run-time pool.
	 *
	 * @param __cl The class pool.
	 * @param __rt The run-time pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/07
	 */
	public DualClassRuntimePool(BasicPool __cl, BasicPool __rt)
		throws NullPointerException
	{
		if (__cl == null || __rt == null)
			throw new NullPointerException("NARG");
		
		this.classpool = __cl;
		this.runpool = __rt;
	}
	
	/**
	 * Returns the static class pool.
	 *
	 * @return The pool.
	 * @since 2019/09/14
	 */
	public final BasicPool classPool()
	{
		return this.classpool;
	}
	
	/**
	 * Loads a value from the pool by its index.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The entry for the given index.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * pool bounds.
	 * @since 2019/09/07
	 */
	public final BasicPoolEntry getByIndex(boolean __rt, int __dx)
		throws IndexOutOfBoundsException
	{
		return (__rt ? this.runpool : this.classpool).byIndex(__dx);
	}
	
	/**
	 * Returns the runtime class pool.
	 *
	 * @return The pool.
	 * @since 2019/09/14
	 */
	public final BasicPool runtimePool()
	{
		return this.runpool;
	}
}

