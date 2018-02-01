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
import cc.squirreljme.runtime.cldc.SystemTrustGroup;

/**
 * This contains the trust provider which uses the backed filesystem to
 * store the trust information.
 *
 * @since 2018/01/31
 */
public final class FileTrustProvider
	extends TrustProvider
{
	/** The paths to use for the trusts. */
	protected final StandardPaths paths;
	
	/**
	 * Initializes the trust provider using the default set of paths.
	 *
	 * @since 2018/01/31
	 */
	public FileTrustProvider()
	{
		this(StandardPaths.DEFAULT);
	}
	
	/**
	 * Initializes the trust provider using the specified path set.
	 *
	 * @param __sp The paths to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public FileTrustProvider(StandardPaths __sp)
		throws NullPointerException
	{
		if (__sp == null)
			throw new NullPointerException("NARG");
			
		this.paths = __sp;
		
		throw new todo.TODO();
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
		
		throw new todo.TODO();
	}
}

