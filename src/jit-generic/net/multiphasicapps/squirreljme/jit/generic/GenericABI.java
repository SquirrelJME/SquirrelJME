// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.jit.JITObjectProperties;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;

/**
 * This wraps the native ABI to also provide properties needed by the JIT
 * system.
 *
 * @since 2016/09/09
 */
public final class GenericABI
	implements JITObjectProperties
{
	/** The ABI used. */
	protected final NativeABI abi;
	
	/**
	 * Initializes the ABI wrapper.
	 *
	 * @param __abi The ABI to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public GenericABI(NativeABI __abi)
		throws NullPointerException
	{
		// Check
		if (__abi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.abi = __abi;
	}
	
	/**
	 * Returns the native ABI.
	 *
	 * @return The native ABI.
	 * @since 2016/09/09
	 */
	public final NativeABI abi()
	{
		return this.abi;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public final String[] properties()
	{
		throw new Error("TODO");
	}
}

