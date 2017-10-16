// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

/**
 * The working stack map table state.
 *
 * @since 2017/10/16
 */
final class __WorkingState__
{
	/** Stack variables. */
	private final StackMapTableEntry[] _stack;
	
	/** Local variables. */
	private final StackMapTableEntry[] _locals;
	
	/** The top of the stack. */
	private volatile int _top;
	
	/**
	 * Initializes the working state.
	 *
	 * @param __ms The maximum stack entries.
	 * @param __ml The maximum local entries.
	 * @since 2017/10/16
	 */
	__WorkingState__(int __ms, int __ml)
	{
		this._stack = new StackMapTableEntry[__ms];
		this._locals = new StackMapTableEntry[__ml];
	}
	
	/**
	 * Builds the stack map table state.
	 *
	 * @return The resulting state.
	 * @since 2017/10/16
	 */
	public StackMapTableState build()
	{
		return new StackMapTableState(this._locals, this._stack, this._top);
	}
	
	/**
	 * Sets the local variable to the specified type.
	 *
	 * @param __i The index to set.
	 * @param __e The entry to set, may be {@code null} to clear it.
	 * @throws InvalidClassFormatException If the index is out of range.
	 * @since 2017/10/16
	 */
	public void setLocal(int __i, StackMapTableEntry __e)
		throws InvalidClassFormatException
	{
		// {@squirreljme.error JI3m The specified local variable index is out
		// of range. (The index to set)}
		StackMapTableEntry[] locals = this._locals;
		if (__i < 0 || __i >= locals.length)
			throw new InvalidClassFormatException(
				String.format("JI3m %d", __i));
		
		locals[__i] = __e;
	}
	
	/**
	 * Sets the stack variable to the specified type.
	 *
	 * @param __i The index to set.
	 * @param __e The entry to set, may be {@code null} to clear it.
	 * @throws InvalidClassFormatException If the index is out of range.
	 * @since 2017/10/16
	 */
	public void setStack(int __i, StackMapTableEntry __e)
		throws InvalidClassFormatException
	{
		// {@squirreljme.error JI3n The specified stack variable index is out
		// of range of the current stack. (The index to set)}
		if (__i < 0 || __i >= this._top)
			throw new InvalidClassFormatException(
				String.format("JI3n %d", __i));
		
		this._stack[__i] = __e;
	}
	
	/**
	 * Sets the top of the stack to the specified position.
	 *
	 * @param __i The index to set.
	 * @since 2017/10/16
	 */
	public void setStackTop(int __i)
	{
		// {@squirreljme.error JI3n Cannot set the specified top of the stack
		// because it is not within the stack limit. (The top of the stack to
		// set)}
		if (__i < 0 || __i > this._stack.length)
			throw new InvalidClassFormatException(
				String.format("JI3n %d", __i));
		
		this._top = __i;
	}
}

