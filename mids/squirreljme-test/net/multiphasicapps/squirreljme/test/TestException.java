// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test;

/**
 * This is used to wrap an exception and record its family so that profiling
 * can be used for comparison. Stack traces are not recorded. This just places
 * the class family tree of the input exception to record compability between
 * exceptions. So if one system throws {@link ArrayIndexOutOfBoundsException}
 * it will be legal for {@link IndexOutOfBoundsException} but not the other way
 * around.
 *
 * @since 2017/03/28
 */
public final class TestException
{
	/**
	 * Initializes the exception holder sourced from the given Throwable.
	 *
	 * @param __t The throwable to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public TestException(Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		throw new todo.TODO();
	}
	
	/**
	 * Checks if this wrapped exception is compatible with the specified
	 * wrapped exception.
	 *
	 * @param __o The wrapped exception to check.
	 * @return {@code true} if the exceptions are compatible.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public boolean isCompatible(TestException __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/28
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

