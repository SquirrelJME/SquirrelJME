// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.client;

import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import cc.squirreljme.runtime.cldc.service.ServiceClientProvider;

/**
 * This factory is used by the client and enables communication with the
 * library service on the remote side.
 *
 * @since 2018/01/05
 */
public final class LibrariesClientFactory
	implements ServiceClientProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/02
	 */
	@Override
	public final Object initializeClient(ServiceCaller __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		return new LibrariesClient(__c);
	}
}

