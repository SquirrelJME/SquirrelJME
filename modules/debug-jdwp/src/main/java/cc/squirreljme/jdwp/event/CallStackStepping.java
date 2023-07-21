// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a single step within the call stack.
 *
 * @since 2021/04/17
 */
public final class CallStackStepping
{
	/** The thread used. */
	public final Object thread;
	
	/** The relative call stack limit. */
	public final StepDepth depth;
	
	/** Relative size of each step. */
	public final StepSize size;
	
	/**
	 * Initializes the call stack stepping.
	 * 
	 * @param __thread The thread used.
	 * @param __size The relative call stack limit.
	 * @param __depth Relative size of each step.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/17
	 */
	public CallStackStepping(Object __thread, StepSize __size,
		StepDepth __depth)
		throws NullPointerException
	{
		if (__thread == null || __size == null || __depth == null)
			throw new NullPointerException("NARG");
		
		this.thread = __thread;
		this.size = __size;
		this.depth = __depth;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public int hashCode()
	{
		return this.thread.hashCode() ^
			this.depth.hashCode() ^
			this.size.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public String toString()
	{
		return String.format("CallStackStepping[thread=%s,depth=%s,size=%s]",
			this.thread, this.depth, this.size);
	}
}
