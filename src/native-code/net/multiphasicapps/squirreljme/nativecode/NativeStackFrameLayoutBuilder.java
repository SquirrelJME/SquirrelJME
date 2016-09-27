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
 * This is used to build instances of {@link NativeStackFrameLayout} which is
 * used to describe the layout of the stack.
 *
 * @see NativeStackFrameLayout
 * @since 2016/09/27
 */
public class NativeStackFrameLayoutBuilder
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The current stack register. */
	volatile NativeRegister _stack;
	
	/** The stack direction. */
	volatile NativeStackDirection _stackdir;
	
	/** The stack alignment. */
	volatile int _stackalign =
		-1;
	
	/** The stack value alignment. */
	volatile int _stackvaluealign =
		1;
	
	/**
	 * Sets the stack register.
	 *
	 * @param __r The register to use for the stack.
	 * @throws NativeCodeException If the register is temporary, saved, or is
	 * a scratch register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void stackRegister(NativeRegister __r)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error AR0p Cannot use the specified register as
			// the stack register because it is saved and/or temporary, or is
			// a scratch regiser. (The register to be used as the stack
			// register)}
			if (this._saved.contains(__r) || this._temps.contains(__r) ||
				this._scratch.contains(__r))
				throw new NativeCodeException(String.format("AR0p %s", __r));
			
			this._stack = __r;
		}
	}
	
	/**
	 * Sets the requires stack alignment needed for method calls.
	 *
	 * @param __i The number of bytes to align to.
	 * @throws NativeCodeException If the alignment is zero or negative.
	 * @since 2016/09/01
	 */
	public final void stackAlignment(int __i)
		throws NativeCodeException
	{
		// {@squirreljme.error AR0u The stack alignment is zero or negative.
		// (The alignment)}
		if (__i <= 0)
			throw new NativeCodeException(String.format("AR0u %d", __i));
		
		// Lock
		synchronized (this.lock)
		{
			this._stackalign = __i;
		}
	}
	
	/**
	 * Sets the direction of the stack.
	 *
	 * @param __d The stack direction.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void stackDirection(NativeStackDirection __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._stackdir = __d;
		}
	}
	
	/**
	 * Sets the alignment of values on the stack.
	 *
	 * @param __a The alignment to use for values.
	 * @throws NativeCodeException If the alignment requested is zero or
	 * negative.
	 * @since 2016/09/21
	 */
	public final void stackValueAlignment(int __a)
		throws NativeCodeException
	{
		// {@squirreljme.error AR07 Cannot set the stack value alignment to
		// a zero or negative value. (The alignment count)}
		if (__a <= 0)
			throw new NativeCodeException(String.format("AR07 %s", __a));
		
		// Lock
		synchronized (this.lock)
		{
			this._stackvaluealign = __a;
		}
	}
}

