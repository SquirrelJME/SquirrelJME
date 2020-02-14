// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Global PrintF state.
 *
 * @since 2018/09/28
 */
final class __PrintFGlobal__
{
	/** Arguments. */
	final Object[] _args;
	
	/** Linear index, used to implicitly define which argument to use. */
	int _lineardx;
	
	/**
	 * Initializes the global state.
	 *
	 * @param __args The arguments.
	 * @since 2018/09/29
	 */
	__PrintFGlobal__(Object... __args)
	{
		this._args = (__args == null ? new Object[0] : __args);
	}
}

