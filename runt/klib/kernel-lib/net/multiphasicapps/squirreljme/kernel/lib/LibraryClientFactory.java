// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib;

import net.multiphasicapps.squirreljme.kernel.service.ClientInstance;
import net.multiphasicapps.squirreljme.kernel.service.ClientInstanceFactory;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;

/**
 * This factory is used by the client and enables communication with the
 * library service on the remote side.
 *
 * @since 2018/01/05
 */
public final class LibraryClientFactory
	implements ClientInstanceFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ClientInstance createClient(ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__sps == null)
			throw new NullPointerException("NARG");
		
		return new LibraryClient(__sps);
	}
}

