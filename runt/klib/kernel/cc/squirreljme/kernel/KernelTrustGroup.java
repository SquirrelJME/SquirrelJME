// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import cc.squirreljme.runtime.cldc.SystemTrustGroup;

/**
 * This represents a trust group within the kernel, tasks are assigned to
 * these groups.
 *
 * @since 2018/01/11
 */
public final class KernelTrustGroup
	implements SystemTrustGroup
{
	/**
	 * Initializes the trust group.
	 *
	 * @since 2018/01/11
	 */
	KernelTrustGroup()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/11
	 */
	@Override
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		// All kernel permissions are valid
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/11
	 */
	@Override
	public final int index()
	{
		// Always uses the zero index
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	public final boolean isTrusted()
	{
		// Always is trusted
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	public final String name()
	{
		return "SquirrelJME Kernel";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	public final String vendor()
	{
		return "Stephanie Gawroriski";
	}
}

