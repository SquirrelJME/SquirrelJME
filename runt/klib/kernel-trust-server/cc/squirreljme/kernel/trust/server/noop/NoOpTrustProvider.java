// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.server.noop;

import cc.squirreljme.kernel.trust.server.TrustProvider;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;

/**
 * This is a trust provider which does not allow new trusts to be created
 * as such.
 *
 * @since 2018/01/17
 */
public final class NoOpTrustProvider
	extends TrustProvider
{
	/**
	 * Initializes the provider.
	 *
	 * @since 2018/01/17
	 */
	public NoOpTrustProvider()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	protected final SystemTrustGroup createTrustGroup(boolean __trusted,
		int __dx, String __name, String __vendor)
		throws NullPointerException
	{
		if (__name == null || __vendor == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK02 Cannot create new trust groups.}
		throw new RuntimeException("BK02");
	}
}

