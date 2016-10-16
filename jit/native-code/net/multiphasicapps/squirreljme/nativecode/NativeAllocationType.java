// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This represents the type of allocation that an allocation is or how
 * registers or stack is to be allocated.
 *
 * @since 2016/09/18
 */
public enum NativeAllocationType
{
	/** Allocate to registers. */
	REGISTER(true, false),
	
	/** Allocate to stack. */
	STACK(false, true),
	
	/** Allocate to any. */
	BOTH(true, true),
	
	/** End. */
	;
	
	/** Claim registers? */
	protected final boolean reg;
	
	/** Claim stack? */
	protected final boolean stack;
	
	/**
	 * Initializes the allocation type.
	 *
	 * @param __r Are registers to be claimed?
	 * @param __s Is stack to be claimed?
	 * @since 2016/09/18
	 */
	private NativeAllocationType(boolean __r, boolean __s)
	{
		this.reg = __r;
		this.stack = __s;
	}
	
	/**
	 * Returns whether or not registers are being claimed.
	 *
	 * @return {@code true} if claiming registers.
	 * @since 2016/09/18
	 */
	public final boolean claimRegisters()
	{
		return this.reg;
	}
	
	/**
	 * Returns whether or not the stack is being claimed.
	 *
	 * @return {@code true} if claiming the stack.
	 * @since 2016/09/18
	 */
	public final boolean claimStack()
	{
		return this.stack;
	}
}

