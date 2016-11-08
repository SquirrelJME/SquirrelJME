// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This interface describes parameters which are used to initialize the kernel
 * such as which launcher to select or programs to auto-launch.
 *
 * @since 2016/11/04
 */
public interface KernelLaunchParameters
{
	/**
	 * Obtains the value for the system property with the specified key.
	 *
	 * @param __k The key to get the value for.
	 * @return The value associated with the given key or {@code null} if
	 * no value was assigned.
	 * @since 2016/11/08
	 */
	public abstract String getSystemProperty(String __k);
}

