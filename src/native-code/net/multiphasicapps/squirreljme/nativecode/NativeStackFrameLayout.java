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
 * This class represents the native stack frame which is used by the native
 * system to pass arguments and provide storage space for local variables.
 *
 * @see NativeStackFrameLayoutBuilder
 * @since 2016/09/27
 */
public final class NativeStackFrameLayout
{
	/** The current stack register. */
	protected final NativeRegister stack;
	
	/** The stack direction. */
	protected final NativeStackDirection stackdir;
	
	/** The stack alignment. */
	protected final int stackalign;
	
	/** The stack value alignment. */
	protected final int stackvaluealign;
	
	/**
	 * This initializes the stack frame layout.
	 *
	 * @throws NativeCodeException If the stack frame layout is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/27
	 */
	NativeStackFrameLayout(NativeStackFrameLayoutBuilder __b)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AR0v The stack alignment was not set.}
		int stackalign = __b._stackalign;
		if (stackalign <= 0)
			throw new NativeCodeException("AR0v");
		this.stackalign = stackalign;
		
		// {@squirreljme.error AR0w The stack direction was not set.}
		NativeStackDirection stackdir = __b._stackdir;
		if (stackdir == null)
			throw new NativeCodeException("AR0w");
		this.stackdir = stackdir;
		
		// {@squirreljme.error AR0x The stack register was not set.}
		NativeRegister stack = __b._stack;
		if (stack == null)
			throw new NativeCodeException("AR0x");
		this.stack = stack;
		
		// Copy alignment
		this.stackvaluealign = __b._stackvaluealign;
	}
	
	/**
	 * Returns the stack register.
	 *
	 * @return The stack register.
	 * @since 2016/09/01
	 */
	public final NativeRegister stackRegister()
	{
		return this.stack;
	}
	
	/**
	 * This returns the allocation which represents the stack pointer which
	 * may be used by the code generator for copy operations.
	 *
	 * @return The allocation used for stack entries.
	 * @since 2016/09/25
	 */
	public final NativeAllocation stackAllocation()
	{
		// Get
		Reference<NativeAllocation> ref = this._stackalloc;
		NativeAllocation rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._stackalloc = new WeakReference<>((rv = new NativeAllocation(
				-1, 0, pointerType(), this.stack)));
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the direction of the stack.
	 *
	 * @return The stack direction.
	 * @since 2016/09/01
	 */
	public final NativeStackDirection stackDirection()
	{
		return this.stackdir;
	}
	
	/**
	 * This returns the alignment of stack allocations.
	 *
	 * @return The number of bytes that each allocation on the stack must be
	 * aligned to.
	 * @since 2016/09/21
	 */
	public final int stackValueAlignment()
	{
		return this.stackvaluealign;
	}
}

