// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This stores information either for the state of locals or the state of the
 * stack.
 *
 * This class is mutable.
 *
 * This is used for verification and optimization.
 *
 * @since 2016/03/23
 */
public class JVMValueState
{
	/** Is this used as a stack? */
	protected final boolean dostack;
	
	/**
	 * Initializes the value states with the specified limitations.
	 *
	 * @param __stack Is this used as a stack?
	 * @param __count The number of entries to hold.
	 * @throws IllegalArgumentException If the count is negative.
	 * @since 2016/03/23
	 */
	public JVMValueState(boolean __stack, int __count)
		throws IllegalArgumentException
	{
		// Check
		if (__count < 0)
			throw new IllegalArgumentException(String.format("IN1j %d",
				__count));
		
		// Set
		dostack = __stack;
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

