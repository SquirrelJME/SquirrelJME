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
 * This is used to represent how an instruction finishes execution and how
 * control is passed.
 *
 * @since 2017/05/10
 */
@Deprecated
class __ExecutionFlow__
{
	/** The next instruction. */
	public static final __ExecutionFlow__ NEXT =
		new __ExecutionFlow__(__ExecutionFlowType__.NEXT);
	
	/** The method is returned from. */
	public static final __ExecutionFlow__ RETURN =
		new __ExecutionFlow__(__ExecutionFlowType__.RETURN);
	
	/** An exception is thrown. */
	public static final __ExecutionFlow__ THROW =
		new __ExecutionFlow__(__ExecutionFlowType__.THROW);
	
	/** The flow type used. */
	protected final __ExecutionFlowType__ type;
	
	/**
	 * Initializes the flow.
	 *
	 * @param __t The type of flow being performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/10
	 */
	private __ExecutionFlow__(__ExecutionFlowType__ __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
	}
	
	/**
	 * Initializes execution flow with multiple address targets.
	 *
	 * @param __addr Address targets.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/11
	 */
	__ExecutionFlow__(int... __addr)
		throws NullPointerException
	{
		// Check
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/10
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/10
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/10
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type of the execution flow.
	 *
	 * @return The type of flow used.
	 * @since 2017/05/11
	 */
	public __ExecutionFlowType__ type()
	{
		return this.type;
	}
}

