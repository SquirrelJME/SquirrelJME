// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;


/**
 * This represents the verification state of the stack.
 *
 * @since 2016/08/28
 */
class __SMTStack__
	extends __SMTTread__
{
	/** The top of the stack. */
	private volatile int _top;
	
	/**
	 * Initializes the stack.
	 *
	 * @param __n The number of items in the stack.
	 * @param __top The top of the stack.
	 * @throws ClassFormatException If the top of the stack is out of bounds.
	 * @since 2016/05/12
	 */
	__SMTStack__(int __n, int __top)
		throws ClassFormatException
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
	__SMTStack__(__SMTStack__ __s)
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
	public void from(__SMTTread__ __o)
	{
		// Copy top
		setStackTop(((__SMTStack__)__o)._top);
		
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
	public int push(StackMapType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY3x The Java stack has overflowed.}
		int top = this._top;
		boolean wide;
		int newtop = top + ((wide = __t.isWide()) ? 2 : 1);
		if (newtop > this.count)
			throw new ClassFormatException("AY3x");
		
		// Set type
		set(top, __t);
		
		// Set the top and adjust the top more
		if (wide)
		{
			// Set next to the top type
			int hi = top + 1;
			set(hi, StackMapType.TOP);
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
	 * @throws ClassFormatException If the top of the stack exceeds any bound
	 * of the stack.
	 * @since 2016/08/29
	 */
	public void setStackTop(int __top)
		throws ClassFormatException
	{
		// {@squirreljme.error AY31 The size of the stack either overflows
		// or underflows the number of stack entries. (The position of the
		// top of the stack; The number of entries on the stack)}
		int n = this.count;
		if (__top < 0 || __top > n)
			throw new ClassFormatException(
				String.format("AY31 %d %d", __top, n));
		
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
	 * @param __op The operation parser.
	 * @param __n The number of elements to pop.
	 * @return The cached stack variables to use.
	 * @throws ClassFormatException If popping more items from the stack than
	 * what is on the stack occurs or the number of elements to pop is a
	 * negative value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	void __pop(__OpParser__ __op, int __n)
		throws ClassFormatException, NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Popping nothing
		if (__n == 0)
			return;
		
		// {@squirreljme.error AY41 The stack underflowed.}
		int top = top(), end = top - __n;
		if (__n < 0 || end < 0)
			throw new ClassFormatException("AY41");
		
		// Set new top
		setStackTop(end);
	}
}

