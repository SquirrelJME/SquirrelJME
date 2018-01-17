// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.server;

import cc.squirreljme.kernel.trust.client.TrustClient;
import cc.squirreljme.kernel.trust.client.TrustClientFactory;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.service.ServiceProvider;
import cc.squirreljme.runtime.cldc.SystemTask;

/**
 * This manages the trusts which are available to be used.
 *
 * @since 2018/01/17
 */
public abstract class TrustProvider
	extends ServiceProvider
{
	/**
	 * Initializes the base trust provider.
	 *
	 * @since 2018/01/17
	 */
	public TrustProvider()
	{
		super(TrustClient.class, TrustClientFactory.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public final TrustServer createInstance(SystemTask __st,
		ServicePacketStream __ps)
		throws NullPointerException
	{
		if (__st == null || __ps == null)
			throw new NullPointerException("NARG");
		
		return new TrustServer(this, __st, __ps);
	}
}

