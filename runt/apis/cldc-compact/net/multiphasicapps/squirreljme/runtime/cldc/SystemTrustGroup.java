// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This represents a group which tasks run under and which libraries are
 * installed under and represent groups they are installed within.
 *
 * @since 2018/01/09
 */
public interface SystemTrustGroup
{
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/11
	 */
	public abstract void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException;
	
	/**
	 * Returns the index of the trust group.
	 *
	 * @return The trust group index.
	 * @since 2018/01/11
	 */
	public abstract int index();
}

