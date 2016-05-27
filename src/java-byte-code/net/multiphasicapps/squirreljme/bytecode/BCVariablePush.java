// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * This represents the type of variable to push.
 *
 * @since 2016/05/12
 */
public final class BCVariablePush
{
	/** A new object. */
	private static final BCVariablePush _NEW_OBJECT =
		new BCVariablePush(BCVariableType.OBJECT);
	
	/** The type of value to push. */
	protected final BCVariableType pushtype;
	
	/** The value to use when popping. */
	protected final int popindex;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Pushes a new value which is an entirely new value.
	 *
	 * @param __t The type of value to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	BCVariablePush(BCVariableType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		pushtype = __t;
		popindex = -1;
	}
	
	/**
	 * Pushes a new value to the stack which is derived from a local value.
	 *
	 * @param __l The local variable to push.
	 * @throws IllegalArgumentException If the local varaible is not read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	BCVariablePush(BCLocalAccess __l)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Pushes an existing stack value to the stack unchanged.
	 *
	 * @param __pops The operations to pop.
	 * @param __i The index in the last being pushed.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	BCVariablePush(List<BCVariableType> __pops, int __i)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__pops == null)
			throw new NullPointerException("NARG");
		
		// Use the specified index
		popindex = __i;
		pushtype = __pops.get(__i);
		
		// {@squirreljme.error AX0w There was no type associated with the
		// push index.}
		if (pushtype == null)
			throw new NullPointerException("AX0w");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the value that this is a copy of in the given pop index.
	 *
	 * @return The pop index or a negitive value if it is not valid.
	 * @since 2016/05/13
	 */
	public int popIndex()
	{
		return this.popindex;
	}
	
	/**
	 * Returns the type of value being pushed.
	 *
	 * @return The push value type.
	 * @since 2016/05/13
	 */
	public BCVariableType pushType()
	{
		return this.pushtype;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
		{
			// Setup builder
			StringBuilder sb = new StringBuilder("(");
			
			// Add the type to push
			sb.append(pushtype);
			
			// Has a pop index?
			int pi = popIndex();
			if (pi >= 0)
			{
				sb.append(":POP=");
				sb.append(pi);
			}
			
			// Finish it
			sb.append(')');
			_string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return the string
		return rv;
	}
	
	/**
	 * This returns a push representation which indicates that a new value
	 * which is not derived from any input stack entries or local variables
	 * is pushed to the stack.
	 *
	 * @return A push representation which specifies a new object value.
	 * @since 2016/05/12
	 */
	public static BCVariablePush newObject()
	{
		return _NEW_OBJECT;
	}
}

