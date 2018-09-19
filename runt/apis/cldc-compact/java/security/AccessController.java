// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

/**
 * This is used for access control on resources that may be available at
 * run-time.
 *
 * @since 2018/09/18
 */
public final class AccessController
{
	/**
	 * Checks the specified permission.
	 *
	 * @param __p The permission to check.
	 * @throws AccessControlException If access is denied or the permission
	 * is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/18
	 */
	public static void checkPermission(Permission __p)
		throws AccessControlException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		todo.TODO.note("Check permission: %s", __p);
	}
}

