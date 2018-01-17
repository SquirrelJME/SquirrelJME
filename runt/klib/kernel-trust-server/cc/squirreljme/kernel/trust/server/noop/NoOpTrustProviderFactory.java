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
import cc.squirreljme.kernel.trust.server.TrustProviderFactory;

/**
 * This is used to obtain instances of the noop trust service.
 *
 * @since 2018/01/17
 */
public final class NoOpTrustProviderFactory
	extends TrustProviderFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	protected final TrustProvider protectedCreate()
	{
		return new NoOpTrustProvider();
	}
}

