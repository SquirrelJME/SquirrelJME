// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import java.security.BasicPermission;

/**
 * This is a permission which is used and checked in the security manager
 * to verify that the specified permissions are available before the
 * application and task manager are used.
 *
 * {@code "client"} refers to applications which are assigned to the same
 * client.
 *
 * {@code "crossClient"} is usually assigned to the root client which allows
 * a client to control other clients.
 *
 * @since 2016/06/24
 */
public final class SWMPermission
	extends BasicPermission
{
	/**
	 * Initializes a new permission.
	 *
	 * @param __scope The scope.
	 * @param __actions The actions used. If an action is specified multiple
	 * times then it is ignored.
	 * @throws IllegalArgumentException If any action includes a string which
	 * is not permitted or if the scope is incorrect.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public SWMPermission(String __scope, String __actions)
		throws IllegalArgumentException, NullPointerException
	{
		super(__scope, __actions);
		
		// Check
		if (__scope == null || __actions == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

