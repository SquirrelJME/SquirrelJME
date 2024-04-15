// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
