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
 * This describes the state of variables that exist for an operation on the
 * local variable list and the stack.
 *
 * This class is mutable.
 *
 * This is used for verification and optimization.
 *
 * @since 2016/03/23
 */
@Deprecated
public class JVMOpState
{
	/** Local variables. */
	protected final JVMValueState locals;
	
	/** Stack variables. */
	protected final JVMValueState stack;
	
	/**
	 * Initializes the operation state.
	 *
	 * @param __ml Max local variables.
	 * @param __ms Max stack entries.
	 * @since 2016/03/23
	 */
	public JVMOpState(int __ml, int __ms)
	{
		locals = new JVMValueState(false, __ml);
		stack = new JVMValueState(true, __ms);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be the same thing
		if (!(__o instanceof JVMOpState))
			return false;
		
		// Cast and check
		JVMOpState o = (JVMOpState)__o;
		return locals.equals(o.locals) && stack.equals(o.stack);
	}
	
	/**
	 * Returns the local variable state.
	 *
	 * @return The local variables state.
	 * @since 2016/03/23
	 */
	public JVMValueState getLocals()
	{
		return locals;
	}
	
	/**
	 * Returns the stack variable state.
	 *
	 * @return The stack variables state.
	 * @since 2016/03/23
	 */
	public JVMValueState getStack()
	{
		return stack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public int hashCode()
	{
		return locals.hashCode() ^ stack.hashCode();
	}
	
	/**
	 * Sets this virtual operational state to another operational state.
	 *
	 * This may deadlock if two states attempt to set each other at the same
	 * time.
	 *
	 * @param __v The other operation state to copy.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/23
	 */
	public JVMOpState set(JVMOpState __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// If this, then this is pointless
		if (__v == this)
			return this;
		
		// Lock on all of the states because they are very intertwined
		// with each other.
		synchronized (__v.locals.lock)
		{
			synchronized (locals.lock)
			{
				synchronized (__v.stack.lock)
				{
					synchronized (stack.lock)
					{
						// Copy the local variable state
						locals.set(__v.locals);
						
						// Copy the stack variable state
						stack.set(__v.stack);
					}
				}
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public String toString()
	{
		// Build string
		StringBuilder sb = new StringBuilder();
		
		// Locals
		sb.append("{locals=");
		sb.append(locals);
		
		// And the stack
		sb.append(", stack=");
		sb.append(stack);
		
		// End it
		sb.append('}');
		return sb.toString();
	}
}

