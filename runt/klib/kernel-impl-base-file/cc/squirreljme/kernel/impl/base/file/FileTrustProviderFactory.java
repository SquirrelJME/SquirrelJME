// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.kernel.trust.server.TrustProvider;
import cc.squirreljme.kernel.trust.server.TrustProviderFactory;

/**
 * This class is used to initialize the trust provider which backs the trusts
 * on the local filesystem.
 *
 * @since 2018/01/31
 */
public class FileTrustProviderFactory
	extends TrustProviderFactory
{
	/**
	 * Creates an instance of the provider which is implementation
	 * dependent.
	 *
	 * @return The provider instance.
	 * @since 2018/01/31
	 */
	protected FileTrustProvider createFileTrust()
	{
		return new FileTrustProvider();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	protected final TrustProvider createTrust()
	{
		return this.createFileTrust();
	}
}

