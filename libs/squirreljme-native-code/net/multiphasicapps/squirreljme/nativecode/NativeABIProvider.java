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

import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;

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
	 * @param __t The CPU to target.
	 * @return The ABI to use for the given CPU type.
	 * @throws NativeCodeException If the ABI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public abstract NativeABI byName(String __n, NativeTarget __t)
		throws NativeCodeException, NullPointerException;
}

