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

import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;

/**
 * This interface is used with the service loader and is used to provide access
 * to the native ABI to be used for native code generation.
 *
 * @since 2016/09/15
 */
public interface NativeABIProvider
{
	/**
	 * Returns the ABI by the given name.
	 *
	 * @param __n The name of the ABI to choose.
	 * @param __b The number of bits in the CPU.
	 * @param __f The native floating point type.
	 * @return The ABI to use for the given CPU type.
	 * @throws NativeCodeException If the ABI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public abstract NativeABI byName(String __n, int __b, NativeFloatType __f)
		throws NativeCodeException, NullPointerException;
}

