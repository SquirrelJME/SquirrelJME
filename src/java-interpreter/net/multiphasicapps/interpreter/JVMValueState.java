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

import java.util.AbstractList;

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
	extends AbstractList<JVMVariableType>
{
	/** Is this used as a stack? */
	protected final boolean dostack;
	
	/** Internal stack state. */
	protected final byte[] state;
	
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
		state = new byte[__count >>> 1];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public JVMVariableType get(int __i)
	{
		// Out of bounds?
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException("IOOB");	
		
		// Get access details
		boolean hi = ((__i & 1) != 0);
		int tdx = __i >>> 1;
		
		// Lock
		byte val;
		synchronized (state)
		{
			// Get the actual value
			val = state[tdx];
		}
		
		// Use special calculation
		if (hi)
			return JVMVariableType.byIndex(val >>>= 4);
		return JVMVariableType.byIndex(val &= 0xF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public JVMVariableType set(int __i, JVMVariableType __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException("IOOB");	
		
		// Get access details
		boolean hi = ((__i & 1) != 0);
		int tdx = __i >>> 1;
		
		// Lock
		JVMVariableType old;
		synchronized (state)
		{
			// Get the actual value
			byte val = state[tdx];
			
			// Determine the old value
			if (hi)
				old = JVMVariableType.byIndex(val >>>= 4);
			else
				old = JVMVariableType.byIndex(val &= 0xF);
			
			// Set the new value
			if (hi)
				val = (byte)((val & 0x0F) | (__v.ordinal() << 4));
			else
				val = (byte)((val & 0xF0) | __v.ordinal());
			
			// Place it
			state[tdx] = val;
		}
		
		// Return the old
		return old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public int size()
	{
		return state.length * 2;
	}
}

