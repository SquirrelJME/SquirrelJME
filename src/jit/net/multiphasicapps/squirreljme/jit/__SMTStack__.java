// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.base.JITException;

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
	 * @throws JITException If the top of the stack is out of bounds.
	 * @since 2016/05/12
	 */
	__SMTStack__(int __n, int __top)
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
	 * Sets the top of the stack.
	 *
	 * @param __top The top of the stack.
	 * @throws JITException If the top of the stack exceeds any bound of the
	 * stack.
	 * @since 2016/08/29
	 */
	public void setStackTop(int __top)
		throws JITException
	{
		// {@squirreljme.error ED01 The size of the stack either overflows
		// or underflows the number of stack entries. (The position of the
		// top of the stack; The number of entries on the stack)}
		int n = this.count;
		if (__top < 0 || __top > n)
			throw new JITException(String.format("ED01 %d %d", __top, n));
		
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
}

