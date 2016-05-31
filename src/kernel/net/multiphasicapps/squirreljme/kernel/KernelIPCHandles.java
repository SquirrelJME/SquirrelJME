// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This interface describes a client connected socket or a server socket its
 * handle.
 *
 * @since 2016/05/31
 */
public interface KernelIPCHandles
{
	/**
	 * Returns the primary handle.
	 *
	 * @return The primary handle.
	 * @since 2016/05/31
	 */
	public abstract int getPrimaryHandle();
	
	/**
	 * Returns the secondary handle.
	 *
	 * @retrun The secondary handle.
	 * @since 2016/05/31
	 */
	public abstract int getSecondaryHandle();
}

