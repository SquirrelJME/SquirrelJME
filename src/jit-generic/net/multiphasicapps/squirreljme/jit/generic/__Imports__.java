// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This contains the static import table for the given namespace, it is used
 * by the runtime as essentially a table of pointers which refer to other
 * memory locations. Each import can refer to a class, method, field, or
 * string.
 *
 * Instance fields are not directly pointed to, but are relative to the object.
 *
 * Strings must act as if they {@link String#intern()} has been called on them.
 *
 * @since 2016/08/09
 */
final class __Imports__
{
	/**
	 * Initializes the import table.
	 *
	 * @since 2016/08/09
	 */
	__Imports__()
	{
	}
}

