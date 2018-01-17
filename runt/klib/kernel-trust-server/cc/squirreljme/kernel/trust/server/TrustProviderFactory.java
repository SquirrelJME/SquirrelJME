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

import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.service.ServiceProviderFactory;

/**
 * This creates instances of the trust provider.
 *
 * @since 2018/01/17
 */
public abstract class TrustProviderFactory
	implements ServiceProviderFactory
{
	/**
	 * Creates an instance of the provider which is implementation
	 * dependent.
	 *
	 * @return The provider instance.
	 * @since 2018/01/17
	 */
	protected abstract TrustProvider protectedCreate();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public final TrustProvider createProvider()
	{
		return protectedCreate();
	}
}

