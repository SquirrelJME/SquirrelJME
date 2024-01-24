// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.event;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * An exception only event.
 *
 * @since 2021/04/17
 */
public final class JDWPHostExceptionOnly
{
	/** The type used. */
	protected final Object optionalType;
	
	/** Caught exceptions? */
	protected final boolean caught;
	
	/** Uncaught exceptions? */
	protected final boolean uncaught;
	
	/**
	 * Initializes the exception filter details.
	 * 
	 * @param __optionalType The optional type, may be {@code null}.
	 * @param __caught Report caught exceptions?
	 * @param __uncaught Report uncaught exceptions?
	 * @since 2021/04/17
	 */
	public JDWPHostExceptionOnly(Object __optionalType, boolean __caught,
		boolean __uncaught)
	{
		this.optionalType = __optionalType;
		this.caught = __caught;
		this.uncaught = __uncaught;
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
		return String.format("ExceptionOnly(type=%x, caught=%b, uncaught=%b)",
			System.identityHashCode(this.optionalType),
			this.caught, this.uncaught);
	}
}
