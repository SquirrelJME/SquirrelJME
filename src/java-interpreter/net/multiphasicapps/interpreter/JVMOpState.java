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
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public int hashCode()
	{
		return locals.hashCode() ^ stack.hashCode();
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

