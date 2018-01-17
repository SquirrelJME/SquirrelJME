// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.client;

import cc.squirreljme.kernel.service.ClientInstanceFactory;
import cc.squirreljme.kernel.service.ServicePacketStream;

/**
 * This is a factory which initializes trust clients.
 *
 * @since 2018/01/17
 */
public final class TrustClientFactory
	extends ClientInstanceFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	protected final TrustClient initializeClient(ServicePacketStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		return new TrustClient(__ps);
	}
}

