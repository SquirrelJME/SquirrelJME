// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.service;

/**
 * This interface is used to initialize client interfaces to services available
 * to the system.
 *
 * @since 2018/03/02
 */
@Deprecated
public interface ServiceClientProvider
{
	/**
	 * Initializes the client object for the given service.
	 *
	 * @param __c The caller interface to use for system calls.
	 * @return The client object for the service.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/02
	 */
	@Deprecated
	public abstract Object initializeClient(ServiceCaller __c)
		throws NullPointerException;
}

