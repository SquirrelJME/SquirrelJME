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

import cc.squirreljme.runtime.cldc.SystemTrustGroup;

/**
 * This represents the client side of a trust.
 *
 * @since 2018/01/18
 */
final class __LocalTrust__
	implements SystemTrustGroup
{
	/** The trust index. */
	protected final int index;
	
	/**
	 * Initializes the local trust.
	 *
	 * @param __dx The trust index.
	 * @since 2018/01/18
	 */
	__LocalTrust__(int __dx)
	{
		this.index = __dx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/18
	 */
	@Override
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/18
	 */
	@Override
	public final int index()
	{
		throw new todo.TODO();
	}
}

