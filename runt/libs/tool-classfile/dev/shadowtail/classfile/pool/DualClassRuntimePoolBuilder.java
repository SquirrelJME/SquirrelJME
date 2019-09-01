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
 * This is used as a builder for both class and run-time pools.
 *
 * @since 2019/07/17
 */
public final class DualClassRuntimePoolBuilder
{
	/** The class pool. */
	protected final BasicPoolBuilder classpool =
		new BasicPoolBuilder();
	
	/** The run-time pool. */
	protected final BasicPoolBuilder runpool =
		new BasicPoolBuilder();
	
	/**
	 * Adds the specified pool entry.
	 *
	 * @param __rt Place into the run-time pool?
	 * @param __v The value to store.
	 * @return The resulting pool entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/25
	 */
	public final BasicPoolEntry add(boolean __rt, Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		if (__rt)
			return this.addRuntime(__v);
		return this.addStatic(__v);
	}
	
	/**
	 * Adds an entry to the run-time pool.
	 *
	 * @param __v The value to add.
	 * @return The entry which was created.
	 * @throws IllegalArgumentException If the value cannot be stored into
	 * the specified pool.
	 * @since 2019/09/01
	 */
	public final BasicPoolEntry addRuntime(Object __v)
		throws IllegalArgumentException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Adds an entry to the static pool.
	 *
	 * @param __v The value to add.
	 * @return The entry which was created.
	 * @throws IllegalArgumentException If the value cannot be stored into
	 * the specified pool.
	 * @since 2019/09/01
	 */
	public final BasicPoolEntry addStatic(Object __v)
		throws IllegalArgumentException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the static class pool.
	 *
	 * @return The pool.
	 * @since 2019/07/17
	 */
	public final BasicPoolBuilder classPool()
	{
		return this.classpool;
	}
	
	/**
	 * Returns the runtime class pool.
	 *
	 * @return The pool.
	 * @since 2019/07/17
	 */
	public final BasicPoolBuilder runtimePool()
	{
		return this.runpool;
	}
}

