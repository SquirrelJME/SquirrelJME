// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import net.multiphasicapps.squirreljme.runtime.cldc.SystemTrustGroup;

/**
 * This represents a trust group within the kernel, tasks are assigned to
 * these groups.
 *
 * @since 2018/01/11
 */
public final class KernelTrustGroup
	implements SystemTrustGroup
{
	/** The trust group index. */
	protected final int index;
	
	/** Should all permissions be allowed? */
	protected final boolean allpermissions;
	
	/** Permissions for this task. */
	protected final KernelPermissions permissions =
		new KernelPermissions();
	
	/**
	 * Initializes the trust group.
	 *
	 * @param __dx The trust group index.
	 * @since 2018/01/11
	 */
	KernelTrustGroup(int __dx)
	{
		this(false, __dx);
	}
	
	/**
	 * Initializes the trust group, care should be taken when calling this
	 * constructor as it can be set to ignore permission checks.
	 *
	 * @param __allperm Should all permissions be enabled?
	 * @param __dx The trust group index.
	 * @since 2018/01/11
	 */
	KernelTrustGroup(boolean __allperm, int __dx)
	{
		this.allpermissions = __allperm;
		this.index = __dx;
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
		
		// Only perform the permission check if they are not ignored
		if (!this.allpermissions)
			this.permissions.checkPermission(__cl, __n, __a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/11
	 */
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Provides access to the permissions.
	 *
	 * @return The permissions for this group.
	 * @since 2018/01/11
	 */
	final KernelPermissions __permissions()
	{
		return this.permissions;
	}
}

