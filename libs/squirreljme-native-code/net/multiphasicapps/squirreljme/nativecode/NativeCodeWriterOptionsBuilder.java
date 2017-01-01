// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This is used to build {@link NativeCodeWriter} which is used to configure
 * the native code generator.
 *
 * @since 2016/09/15
 */
public class NativeCodeWriterOptionsBuilder
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The ABI to use for register allocation and method calls. */
	volatile NativeABI _abi;
	
	/**
	 * Builds the native code writer options.
	 *
	 * @return An immutable set of options.
	 * @throws NativeCodeException If not enough information was specified.
	 * @since 2016/09/15
	 */
	public final NativeCodeWriterOptions build()
		throws NativeCodeException
	{
		// Lock
		synchronized (this.lock)
		{
			return new NativeCodeWriterOptions(this);
		}
	}
	
	/**
	 * Sets the ABI to use which determines which registers are used for
	 * method calls.
	 *
	 * @param __abi The ABI to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public final void setABI(NativeABI __abi)
		throws NullPointerException
	{
		// Check
		if (__abi == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._abi = __abi;
		}
	}
}

