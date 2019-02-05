// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

/**
 * This class manages the set of registers to work with.
 *
 * @since 2019/01/21
 */
public final class RegisterSet
{
	/** Virtual stack base, which register starts the virtual stack. */
	protected final int vstackbase;
	
	/** The limit to the stack. */
	protected final int vstacklimit;
	
	/** Work registers. */
	private final WorkRegister[] _registers;
	
	/** The position of the virtual stack pointer. */
	private int _vstackptr;
	
	/**
	 * Initializes the register set.
	 *
	 * @param __n The number of registers to store.
	 * @param __vsb The virtual stack base index, used to simulate the Java
	 * stack.
	 * @param __vsl The virtual stack limit.
	 * @since 2019/01/21
	 */
	public RegisterSet(int __n, int __vsb, int __vsl)
	{
		// Initialize work registers
		WorkRegister[] registers = new WorkRegister[__n];
		for (int i = 0; i < __n; i++)
			registers[i] = new WorkRegister(i);
		
		// Set
		this._registers = registers;
		this.vstackbase = __vsb;
		this.vstacklimit = __vsl;
	}
	
	/**
	 * Returns the register at the given index.
	 *
	 * @param __i The index to get.
	 * @return The register at the given index.
	 * @since 2019/01/22
	 */
	public final WorkRegister get(int __i)
	{
		return this._registers[__i];
	}
	
	/**
	 * Sets the virtual Java stack position.
	 *
	 * @param __p The position to set.
	 * @throws IllegalArgumentException If the stack pointer is not within
	 * bounds of the virtual stack.
	 * @since 2019/01/22
	 */
	public final void setJavaStackPos(int __p)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT02 Cannot set the Java stack position because
		// it is not within the bounds of the virtual stack. (The stack
		// position)}
		if (__p < this.vstackbase || __p >= this._registers.length)
			throw new IllegalArgumentException("AT02 " + __p);
		
		this._vstackptr = __p;
	}
	
	/**
	 * Virtual push to the stack.
	 *
	 * @return The index of the entry that was pushed to the stack.
	 * @throws IllegalStateException If the stack overflows.
	 * @since 2019/01/23
	 */
	public final int virtualPush()
		throws IllegalStateException
	{
		return this.virtualPush(1);
	}
	
	/**
	 * Virtual push to the stack, multiple values can be pushed at once.
	 *
	 * @param __n The number of entries to push.
	 * @return The index of the first entry which was pushed.
	 * @throws IllegalArgumentException If the count to push is zero or
	 * negative.
	 * @throws IllegalStateException If the stack overflows.
	 * @since 2019/02/05
	 */
	public final int virtualPush(int __n)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error AT04 Cannot push a negative number of
		// registers.}
		if (__n <= 0)
			throw new IllegalArgumentException("AT04");
		
		// The old top is 
		int vstackptr = this._vstackptr;
		
		// {@squirreljme.error AT03 Stack overflow.}
		int next = vstackptr + 1;
		if (next >= this.vstacklimit)
			throw new IllegalStateException("AT03");
		
		// Set next pointer
		this._vstackptr = vstackptr;
		
		// Returns the old top
		return vstackptr;
	}
}

