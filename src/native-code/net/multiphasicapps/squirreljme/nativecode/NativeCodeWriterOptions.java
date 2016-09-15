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
 * This class contains options which are used to configure the native code
 * generator.
 *
 * This class is immutable.
 *
 * @since 2016/09/15
 */
public final class NativeCodeWriterOptions
{
	/** The ABI to use. */
	protected final NativeABI abi;
	
	/**
	 * Initialize the native code writer.
	 *
	 * @param __b The builder containing options to set.
	 * @throws NativeCodeException If not enough information was specified.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	NativeCodeWriterOptions(NativeCodeWriterOptionsBuilder __b)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AR04 An ABI was not specified in the
		// configuration.}
		NativeABI abi = __b._abi;
		if (abi == null)
			throw new NativeCodeException("AR04");
		this.abi = abi;
	}
	
	/**
	 * Returns the ABI to use.
	 *
	 * @return The ABI to use.
	 * @since 2016/09/15
	 */
	public final NativeABI abi()
	{
		return this.abi;
	}
	
	/**
	 * Creates an instance of the register and stack allocator.
	 *
	 * @return The register and stack allocator.
	 * @since 2016/09/15
	 */
	public final NativeAllocator createAllocator()
	{
		return new NativeAllocator(this.abi);
	}
}

