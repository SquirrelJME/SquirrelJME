// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Not Described.
 *
 * @since 2021/04/17
 */
public final class CallStackStepping
{
	/** The thread used. */
	private final Object thread;
	
	/** The relative call stack limit. */
	private final int depth;
	
	/** Relative size of each step. */
	private final int size;
	
	/**
	 * Initializes the call stack stepping.
	 * 
	 * @param __thread The thread used.
	 * @param __size The relative call stack limit.
	 * @param __depth Relative size of each step.
	 * @since 2021/04/17
	 */
	public CallStackStepping(Object __thread, int __size, int __depth)
	{
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
