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
 * This represents a client socket, it acts as a buffer between two endpoints.
 *
 * @since 2016/05/31
 */
public class KernelIPCClient
	extends KernelIPCSocket
{
	/**
	 * Initializes the kernel based IPC client.
	 *
	 * @param __id The socket identifier.
	 * @throws IllegalArgumentException If socket handle is negative.
	 * @since 2016/05/31
	 */
	KernelIPCClient(int __id)
		throws IllegalArgumentException
	{
		super(__id);
	}
}

