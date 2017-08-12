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
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a single state within the stack map table which contains
 * a listing of all of the types used for local and stack variable along with
 * the current depth of the stack.
 *
 * @since 2017/07/28
 */
public final class StackMapTableState
{
	/** The depth of the stack. */
	protected final int depth;
	
	/** Local variables. */
	private final JavaType[] _locals;
	
	/** Stack variables. */
	private final JavaType[] _stack;
	
	/** String representation of this table. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the stack map table state.
	 *
	 * @param __l Local variables.
	 * @param __s Stack variables.
	 * @param __d The depth of the stack.
	 * @throws JITException If the state is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public StackMapTableState(JavaType[] __l, JavaType[] __s, int __d)
		throws JITException, NullPointerException
	{
		// Check
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI1t The depth of the stack is not within the
		// bounds of the stack. (The stack depth; The stack size)}
		int ns = __s.length;
		if (__d < 0 || __d > ns)
			throw new JITException(String.format("JI1t %d %d", __d, ns));
		
		// Duplicate
		__l = __l.clone();
		__s = __s.clone();
		
		// Clear elements above the stack top
		for (int i = __d; i < ns; i++)
			__s[i] = null;
		
		// Verify each state
		__verify(__l);
		__verify(__s);
		
		// Set
		this._locals = __l;
		this._stack = __s;
		this.depth = __d;
	}
	
	/**
	 * Returns the depth of the stack.
	 *
	 * @return The stack depth.
	 * @since 2017/08/12
	 */
	public int depth()
	{
		return this.depth;
	}
	
	/**
	 * Obtains the local at the given index.
	 *
	 * @param __i The index to get.
	 * @return The type for the variable at the given index.
	 * @throws JITException If the index is out of range.
	 * @since 2017/08/12
	 */
	public JavaType getLocal(int __i)
		throws JITException
	{
		// {@squirreljme.error JI22 The specified local variable is out of
		// range. (The index)}
		JavaType[] locals = this._locals;
		if (__i < 0 || __i >= locals.length)
			throw new JITException(String.format("JI22 %d", __i));
		return locals[__i];
	}
	
	/**
	 * Obtains the stack at the given index.
	 *
	 * @param __i The index to get.
	 * @return The type for the variable at the given index.
	 * @throws JITException If the index is out of range.
	 * @since 2017/08/12
	 */
	public JavaType getStack(int __i)
		throws JITException
	{
		// {@squirreljme.error JI21 The specified stack variable is out of
		// range. (The index)}
		if (__i < 0 || __i >= this.depth)
			throw new JITException(String.format("JI21 %d", __i));
		return this._stack[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("{locals=");
			__stringize(this._locals, sb);
			sb.append(", stack(");
			sb.append(this.depth);
			sb.append(")=");
			__stringize(this._stack, sb);
			sb.append("}");
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Stringizes the specified type array.
	 *
	 * @param __jt The type array to stringize.
	 * @param __sb The destination string builder.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	private static void __stringize(JavaType[] __jt, StringBuilder __sb)
		throws NullPointerException
	{
		// Check
		if (__jt == null || __sb == null)
			throw new NullPointerException("NARG");
		
		// Open
		__sb.append('[');
		
		// Add
		for (int i = 0, n = __jt.length; i < n; i++)
		{
			if (i > 0)
				__sb.append(", ");
			
			__sb.append(__jt[i]);
		}
		
		// Close
		__sb.append(']');
	}
	
	/**
	 * Verifies the types within the map.
	 *
	 * @param __t The types to check.
	 * @throws JITException If the type are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	private static void __verify(JavaType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Go through all entries, w acts as a kind of single entry stack which
		// is used to ensure that the tops of long/double are valid
		JavaType w = null;
		for (int i = 0, n = __t.length; i < n; i++)
		{
			JavaType a = __t[i];
			
			// A wide type was pushed
			if (w != null)
			{
				// {@squirreljme.error JI1v The type at the read index does
				// not match the expected type following a wide type. (The wide
				// type; The expected type; The actual type)}
				JavaType t = w.topType();
				if (!a.equals(t))
					throw new JITException(String.format("JI1v %s %s %s", w, t,
						a));
				
				// Clear
				w = null;
			}
			
			// No real checking has to be done unless it is a wide type
			else
			{
				if (a != null && a.isWide())
					w = a;
			}
		}
		
		// {@squirreljme.error JI1u Long or double appears at the end of the
		// type array and does not have a top associated with it.}
		if (w != null)
			throw new JITException("JI1u");
	}
}

