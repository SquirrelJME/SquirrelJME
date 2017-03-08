// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.jit.DataType;
import net.multiphasicapps.squirreljme.jit.CacheState;

/**
 * This class is used to store the offsets for each slot which exists on the
 * stack. 
 *
 * If a 64-bit value is set and a 32-bit value is not set then the 32-bit
 * value will be set from the 64-bit one (with potential offset). Otherwise
 * if the 64-bit offset is set after a 32-bit one a new position will be
 * used because there is no room to store a value without overwriting
 * adjacent space.
 *
 * Generally these offsets are shared across the entire method so that
 * shuffling is not required.
 *
 * @since 2017/03/06
 */
public class StackSlotOffsets
{
	/** Invalid stack offset. */
	private static final int _INVALID_STACK_OFFSET =
		Integer.MAX_VALUE;
	
	/** The owning engine. */
	protected final MIPSEngine engine;
	
	/** The number of local entries. */
	protected final int numlocals;
	
	/** The number of stack entries. */
	protected final int numstack;
	
	/** Total number of entries. */
	protected final int total;
	
	/** The offset of each stack entry, 0 is 32-bit, 1 is 64-bit. */
	private final int[][] _offsets;
	
	/** The current stack depth. */
	private volatile int _depth;
	
	/**
	 * Initialzies the stack slot offsets.
	 *
	 * @param __e The owning engine.
	 * @param __ms Number of stack entries.
	 * @param __ml Number of local entries.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	public StackSlotOffsets(MIPSEngine __e, int __ms, int __ml)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.engine = __e;
		this.numstack = __ms;
		this.numlocals = __ml;
		int total = __ms + __ml;
		this.total = total;
		
		// Initialize offsets
		int[][] offsets = new int[2][total];
		this._offsets = offsets;
		for (int i = 0; i < total; i++)
		{
			offsets[0][i] = _INVALID_STACK_OFFSET;
			offsets[1][i] = _INVALID_STACK_OFFSET;
		}
	}
	
	/**
	 * Deepens the stack by the specified amount.
	 *
	 * @param __v The number of stack entries to deepen by.
	 * @throws IllegalArgumentException If the amount to deepen by is zero
	 * or positive.
	 * @since 2017/03/06
	 */
	public void deepen(int __v)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AM05 The stack may only be deepened with
		// negative values. (The amount to deepen by)}
		if (__v >= 0)
			throw new IllegalArgumentException(String.format("AM05 %d", __v));
		
		// Modify
		this._depth += __v;
	}
	
	/**
	 * Returns the stack offset of the specified variable.
	 *
	 * @param __stack Is this on the stack?
	 * @param __i The index of the entry.
	 * @param __t The type stored in the variable.
	 * @return The stack position of the slot or {@link Integer#MIN_VALUE} if
	 * it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/07
	 */
	public int get(boolean __stack, int __i, DataType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the stack offset of the specified variable.
	 *
	 * @param __cv The variable to get offet for.
	 * @param __t The type stored in the variable.
	 * @return The stack position of the slot or {@link Integer#MIN_VALUE} if
	 * it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/07
	 */
	public int get(CodeVariable __cv, DataType __t)
		throws NullPointerException
	{
		// Check
		if (__cv == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return get(__cv.isStack(), __cv.id(), __t);
	}
	
	/**
	 * Returns the stack offset of the specified slot.
	 *
	 * @param __s The slot to get the offset for.
	 * @return The stack position of the slot or {@link Integer#MIN_VALUE} if
	 * it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/07
	 */
	public int get(CacheState.Slot __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return get(__s.isStack(), __s.index(),
			this.engine.__aliasType(__s.type()));
	}
	
	/**
	 * Returns the depth of the stack.
	 *
	 * @return The stack depth, the value will be negative.
	 * @since 2017/03/06
	 */
	public int stackDepth()
	{
		return this._depth;
	}
}

