// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This class provides access to the kernel interface which allows the
 * process to interact with the kernel.
 *
 * @since 2017/12/08
 */
public abstract class KernelInterface
{
	/** This represents a singleton which interacts with the kernel. */
	public static final KernelInterface INSTANCE =
		__instance();
	
	/**
	 * Returns the instance of the kernel interface.
	 *
	 * This method may virtually be replaced to return the correct interface
	 * or the field could be set before or after the fact.
	 *
	 * @return The kernel interface instance.
	 * @since 2017/12/08
	 */
	private static final KernelInterface __instance()
	{
		return KernelInterface.INSTANCE;
	}
}

