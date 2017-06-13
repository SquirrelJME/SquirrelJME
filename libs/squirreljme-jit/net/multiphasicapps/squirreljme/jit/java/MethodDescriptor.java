// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents the type descriptor of a method.
 *
 * @since 2017/06/12
 */
public final class MethodDescriptor
{
	/** String representation of the descriptor. */
	protected final String string;
	
	/** The return value, null is void. */
	protected final FieldDescriptor rvalue;
	
	/** The arguments in the method. */
	private final FieldDescriptor[] _args;
	
	/**
	 * Initializes the method descriptor.
	 *
	 * @param __n The method descriptor to decode.
	 * @throws JITException If it is not a valid method descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public MethodDescriptor(String __n)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __n;
		
		// {@squirreljme.error JI0j Method descriptors must start with an open
		// parenthesis. (The method descriptor)}
		if (!__n.startsWith("("))
			throw new JITException(String.format("JI0j %s", __n));
		
		// Parse all input arguments
		List<FieldDescriptor> args = new ArrayList<>();
		int i = 1, n = __n.length();
		for (; i < n; i++)
		{
			char c = __n.charAt(i);
			
			// End of descriptor arguments
			if (c == ')')
				break;
			
			throw new todo.TODO();
		}
		this._args = args.<FieldDescriptor>toArray(
			new FieldDescriptor[args.size()]);
		
		// {@squirreljme.error JI0k The method descriptor has no return
		// value. (The method descriptor)}
		if (i >= n)
			throw new JITException(String.format("JI0k %s", __n));
		
		// No return value?
		char c = __n.charAt(i);
		if (c == 'V' && (i + 1) == n)
			this.rvalue = null;
		
		// Parse as a field
		else
			this.rvalue = new FieldDescriptor(__n.substring(i));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MethodDescriptor))
			return false;
		
		return this.string.equals(((MethodDescriptor)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

