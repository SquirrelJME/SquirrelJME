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

import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;
import cc.squirreljme.runtime.cldc.trust.SystemTrustGroupUtils;

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
	
	/** The owning trust client. */
	protected final TrustClient client;
	
	/**
	 * Initializes the local trust.
	 *
	 * @param __dx The trust index.
	 * @param __cl The trust client which created this.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/18
	 */
	__LocalTrust__(int __dx, TrustClient __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.index = __dx;
		this.client = __cl;
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
	 * @since 2018/02/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof SystemTrustGroup))
			return false;
		
		return SystemTrustGroupUtils.equals(this, (SystemTrustGroup)__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/12
	 */
	@Override
	public int hashCode()
	{
		return SystemTrustGroupUtils.hashCode(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/18
	 */
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	public final boolean isTrusted()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	public final String name()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	public final String vendor()
	{
		throw new todo.TODO();
	}
}

