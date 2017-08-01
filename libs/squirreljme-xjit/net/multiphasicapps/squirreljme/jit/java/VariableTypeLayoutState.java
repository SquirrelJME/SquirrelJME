// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the basic verification target for a jump target which does
 * not have any associated allocation information. This is used to quickly
 * verify that a jump from another region of code is valid.
 *
 * @since 2017/05/20
 */
public final class VariableTypeLayoutState
{
	/** The top of the stack. */
	protected final int stacktop;
	
	/** The stack. */
	private final JavaType[] _stack;
	
	/** Local variables. */
	private final JavaType[] _locals;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the verification target state.
	 *
	 * @param __stack The stack state.
	 * @param __top The top of the stack.
	 * @param __locals The local variables.
	 * @throws JITException If the stack top is not valid or other state is
	 * not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/20
	 */
	public VariableTypeLayoutState(JavaType[] __stack, int __top,
		JavaType[] __locals)
		throws JITException, NullPointerException
	{
		// Check
		if (__stack == null || __locals == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ1c The top of the stack is at a negative or
		// a position that is higher than the size of the stack. (The top of
		// the stack)}
		if (__top < 0 || __top > __stack.length)
			throw new JITException(String.format("AQ1c %d", __top));
		
		// Defensive copy and proper initialization
		__stack = __stack.clone();
		__locals = __locals.clone();
		for (int z = 0; z < 2; z++)
		{
			JavaType[] bap = (z == 0 ? __stack : __locals);
			
			for (int i = 0, n = bap.length; i < n; i++)
			{
				JavaType t = bap[i];
				
				// Normalize to nothing so there are no nulls
				if (t == null)
					bap[i] = JavaType.NOTHING;
			}
		}
		
		// Clear anything at and above the top of the stack
		for (int i = __top, n = __stack.length; i < n; i++)
			__stack[i] = JavaType.NOTHING;
		
		// Set
		this.stacktop = __top;
		this._stack = __stack;
		this._locals = __locals;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Add locals first
			StringBuilder sb = new StringBuilder("{locals=[");
			JavaType[] bap = this._locals;
			for (int i = 0, n = bap.length; i < n; i++)
			{
				if (i > 0)
					sb.append(", ");
				sb.append(bap[i]);
			}
			
			// Then the stack
			sb.append("], stack=[");
			bap = this._stack;
			for (int i = 0, n = this.stacktop; i < n; i++)
			{
				if (i > 0)
					sb.append(", ");
				sb.append(bap[i]);
			}
			
			// Finish off
			sb.append("]}");
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

