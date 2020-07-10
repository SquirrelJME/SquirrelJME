// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package security;

import java.security.BasicPermission;

/**
 * This is a permission in name only.
 *
 * @since 2020/07/09
 */
class __NameOnly__
	extends BasicPermission
{
	/**
	 * Initializes the permission.
	 * 
	 * @param __name The name to use.
	 * @since 2020/07/09
	 */
	__NameOnly__(String __name)
	{
		super(__name);
	}
}
