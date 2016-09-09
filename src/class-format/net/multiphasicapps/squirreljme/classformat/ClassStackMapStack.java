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
	/** Indicates that a local/stack item is not a duplicate of anything. */
	static final int UNIQUE_STACK_VALUE =
		Integer.MIN_VALUE;
	
	/**
	 * Flags that the cache is a copy from the stack and not a local.
	 * A bit is used so that mathematical errors caused by negation do not
	 * occur.
	 */
	static final int CACHE_STACK_MASK =
		0x8000_0000;
	
	/** Local/stack variables these values are copied from. */
	private final int[] _cache;
	
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
		
		// Use a cleared cache
		int[] cache = new int[__n];
		this._cache = cache;
		for (int i = 0; i < __n; i++)
			cache[i] = UNIQUE_STACK_VALUE;
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
		
		// Copy cache
		int n = this.count;
		int[] cache = new int[n], from = __s._cache;
		this._cache = cache;
		for (int i = 0; i < n; i++)
			cache[i] = from[i];
	}
	
	/**
	 * Returns the cache reference.
	 *
	 * @param __i The stack index to get the cache for.
	 * @return The cache index value.
	 * @since 2016/09/06
	 */
	public int cache(int __i)
	{
		return this._cache[__i];
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
	 * @param __c Stack caching information, if the uppermost bit is set then
	 * it represents a stack value.
	 * @return The position of the stack variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	public int push(ClassStackMapType __t, int __c)
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
		
		// If the cache points to another stack entry then make it point to
		// the entry that the pointed value points to. So if it points to
		// stack entry B which points to local Q then write Q instead of B.
		if (__c != UNIQUE_STACK_VALUE && 0 != (__c & CACHE_STACK_MASK))
		{
			throw new Error("TODO");
		}
		
		// Set cache
		int[] cache = this._cache;
		cache[top] = __c;
		
		// Set the top and adjust the top more
		if (wide)
		{
			// Set next to the top type
			int hi = top + 1;
			set(hi, ClassStackMapType.TOP);
			
			// Specify that the value is also unique
			if (__c != UNIQUE_STACK_VALUE)
				cache[hi] = UNIQUE_STACK_VALUE;
			
			// Due to stack copies being used by a bit, both can be
			// incremented without using complex negation.
			// Otherwise have it cache the "TOP" of the other entry (be it
			// another stack entry or a local)
			else
				cache[hi] = __c + 1;
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
	 * @throws ClassFormatException If the top of the stack exceeds any bound of the
	 * stack.
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
			throw new ClassFormatException(String.format("AY31 %d %d", __top, n));
		
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
	 * If a stack has a cached value then they will be adjusted accordingly.
	 *
	 * @param __op The operation parser.
	 * @param __n The number of elements to pop.
	 * @return The cached stack variables to use.
	 * @throws ClassFormatException If popping more items from the stack than what
	 * is on the stack occurs or the number of elements to pop is a negative
	 * value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	int[] __pop(__OpParser__ __op, int __n)
		throws ClassFormatException, NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Popping nothing
		if (__n == 0)
			return new int[0];
		
		// {@squirreljme.error AY41 The stack underflowed.}
		int top = top(), end = top - __n;
		if (__n < 0 || end < 0)
			throw new ClassFormatException("AY41");
		
		// If any lower stack entries refer to higher ones then make their
		// values unique and copy them via the parser
		int[] cache = this._cache;
		for (int i = 0; i < end; i++)
		{
			int pointer = cache[i];
			
			// Is a stack pointer
			if (0 != (pointer & CACHE_STACK_MASK))
			{
				int to = pointer & (~CACHE_STACK_MASK);
				if (to >= end)
				{
					// Generate a copy in the parse so that the stack value
					// is preserved and the value becomes unique
					if (true)
						throw new Error("TODO");
					
					// Make the value unique
					cache[i] = UNIQUE_STACK_VALUE;
				}
			}
		}
		
		// Remove items from the stack
		int[] rv = new int[__n];
		for (int i = end, j = 0; i < top; i++, j++)
			rv[j] = cache[i];
		
		// Set new top
		setStackTop(end);
		
		// Return the stack cache mappings
		return rv;
	}
}

