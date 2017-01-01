// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
	 * Returns the command line which was used when the kernel was initialized.
	 *
	 * @return A copy of the kernel's command line.
	 * @since 2016/12/16
	 */
	public abstract String[] getCommandLine();
	
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

