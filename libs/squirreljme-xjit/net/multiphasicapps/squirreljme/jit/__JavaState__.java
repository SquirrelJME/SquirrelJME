// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This represents a verification state which on entry to an operation, the
 * specified state must be matched. If it is not matched then a verification
 * exception would be thrown to indicate badly verified code.
 *
 * @since 2016/03/31
 */
final class __JavaState__
{
	/** The state of the stack. */
	final Stack _stack;
	
	/** The state of the locals. */
	final Locals _locals;

	/**
	 * Initializes the stack storage states.
	 *
	 * @param __ms Maximum number of stack variables.
	 * @param __ml Maximum number of local variables.
	 * @since 2016/08/29
	 */
	__JavaState__(int __ms, int __ml)
	{
		this._stack = new Stack(__ms, 0);
		this._locals = new Locals(__ml);
	}
	
	/**
	 * Creates a new state which is duplicated from the existing state.
	 *
	 * @param __s The other state to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	__JavaState__(__JavaState__ __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Duplicate
		this._stack = new Stack(__s._stack);
		this._locals = new Locals(__s._locals);
	}
	
	/**
	 * Copies the state from the other stack information to the current one.
	 *
	 * @param __o The other stack to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	public void from(__JavaState__ __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set from
		this._stack.from(__o._stack);
		this._locals.from(__o._locals);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/29
	 */
	@Override
	public String toString()
	{
		return "{locals=" + this._locals + ", stack=" + this._stack + "}";
	}
	
	/**
	 * This is the base class for stack and local verification type storage.
	 *
	 * @since 2016/08/28
	 */
	static abstract class Tread
		extends AbstractList<JavaType>
		implements RandomAccess
	{
		/** The number of entries in the tread. */
		protected final int count;
	
		/** The variable storage area. */
		protected final JavaType[] storage;
	
		/**
		 * Initializes the base tread.
		 *
		 * @param __n The number of entries.
		 * @since 2016/05/12
		 */
		Tread(int __n)
		{
			// Initialize
			this.count = __n;
			this.storage = new JavaType[__n];
		}
	
		/**
		 * Initializes the basic tread information from another tread.
		 *
		 * @param __t The tread to base off.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/08/29
		 */
		protected Tread(Tread __t)
			throws NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
		
			// Set
			int n = __t.count;
			this.count = n;
		
			// Copy storage
			JavaType[] from = __t.storage;
			JavaType[] storage = new JavaType[n];
			this.storage = storage;
			for (int i = 0; i < n; i++)
				storage[i] = from[i];
		}
	
		/**
		 * Copies state from another state.
		 *
		 * @param __o The state to copy from.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/04
		 */
		public void from(Tread __o)
			throws NullPointerException
		{
			// Check
			if (__o == null)
				throw new NullPointerException("NARG");
		
			// Copy all variables
			int n = this.count;
			for (int i = 0; i < n; i++)
				set(i, __o.get(i));
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/05/12
		 */
		@Override
		public final JavaType get(int __i)
		{
			// Always make sure there is a value here
			JavaType rv = storage[__i];
			if (rv == null)
				return JavaType.NOTHING;
			return rv;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/08/29
		 */
		@Override
		public final JavaType set(int __i, JavaType __t)
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
		
			JavaType[] storage = this.storage;
			JavaType rv = storage[__i];
			storage[__i] = __t;
			return rv;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/05/12
		 */
		@Override
		public final int size()
		{
			return count;
		}
	}
	
	/**
	 * This represents the verification state of local variables.
	 *
	 * @since 2016/08/28
	 */
	static final class Locals
		extends Tread
	{
		/**
		 * Initializes the local variable types.
		 *
		 * @param __n The number of local variables used.
		 * @since 2016/05/12
		 */
		Locals(int __n)
		{
			super(__n);
		}
	
		/**
		 * Initializes local variable state from an existing one.
		 *
		 * @param __l The state to copy from.
		 * @since 2016/08/29
		 */
		Locals(Locals __l)
		{
			super(__l);
		}
	}

	/**
	 * This represents the verification state of the stack.
	 *
	 * @since 2016/08/28
	 */
	static final class Stack
		extends Tread
	{
		/** The top of the stack. */
		private volatile int _top;
	
		/**
		 * Initializes the stack.
		 *
		 * @param __n The number of items in the stack.
		 * @param __top The top of the stack.
		 * @throws JITException If the top of the stack is out of
		 * bounds.
		 * @since 2016/05/12
		 */
		Stack(int __n, int __top)
			throws JITException
		{
			super(__n);
		
			// Set
			setStackTop(__top);
		}
	
		/**
		 * Copies the stack state from an existing one.
		 *
		 * @param __s The state to copy from.
		 * @since 2016/08/29
		 */
		Stack(Stack __s)
		{
			super(__s);
		
			// Set top
			setStackTop(__s._top);
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/09/04
		 */
		@Override
		public void from(Tread __o)
		{
			// Copy top
			setStackTop(((Stack)__o)._top);
		
			// Copy variables also
			super.from(__o);
		}
	
		/**
		 * Pushes the specified variable to the stack.
		 *
		 * @param __t The type of variable to push.
		 * @return The position of the stack variable.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/04
		 */
		public int push(JavaType __t)
			throws NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
		
			// {@squirreljme.error AQ12 The Java stack has overflowed.}
			int top = this._top;
			boolean wide;
			int newtop = top + ((wide = __t.isWide()) ? 2 : 1);
			if (newtop > this.count)
				throw new JITException("AQ12");
		
			// Set type
			set(top, __t);
		
			// Set the top and adjust the top more
			if (wide)
			{
				// Set next to the top type
				int hi = top + 1;
				set(hi, JavaType.TOP);
			}
		
			// New top
			setStackTop(newtop);
		
			// Return the position the entry was placed at
			return top;
		}
	
		/**
		 * Sets the top of the stack.
		 *
		 * @param __top The top of the stack.
		 * @throws JITException If the top of the stack exceeds any bound
		 * of the stack.
		 * @since 2016/08/29
		 */
		public void setStackTop(int __top)
			throws JITException
		{
			// {@squirreljme.error AQ13 The size of the stack either overflows
			// or underflows the number of stack entries. (The position of the
			// top of the stack; The number of entries on the stack)}
			int n = this.count;
			if (__top < 0 || __top > n)
				throw new JITException(
					String.format("AQ13 %d %d", __top, n));
		
			// Set
			this._top = __top;
		}
	
		/**
		 * Returns the top of the stack.
		 *
		 * @return The top of the stack.
		 * @since 2016/05/12
		 */
		public int top()
		{
			return this._top;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/08/29
		 */
		@Override
		public String toString()
		{
			return this._top + ":" + super.toString();
		}
	
		/**
		 * Pops a value from the stack.
		 *
		 * @param __n The number of elements to pop.
		 * @return The stack type used.
		 * @throws JITException If popping more items from the stack than
		 * what is on the stack occurs or the number of elements to pop is a
		 * negative value.
		 * @since 2016/09/06
		 */
		JavaType __pop(int __n)
			throws JITException
		{
			// {@squirreljme.error AQ14 Not popping anything.}
			if (__n <= 0)
				throw new IllegalArgumentException("AQ14");
		
			// {@squirreljme.error AQ15 The stack underflowed.}
			int top = top(), end = top - __n;
			if (__n < 0 || end < 0)
				throw new JITException("AQ15");
		
			// Set new top
			JavaType rv = get(end);
			setStackTop(end);
		
			// Return the popped type
			return rv;
		}
	}
}

