// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

/**
 * This is used to provide access to the service caller for use in the
 * LCDUI code to interact with the server.
 *
 * @since 2018/03/16
 */
public final class LcdServiceCall
{
	/** Lock for the service accessor. */
	private static final Object _LOCK =
		new Object();
	
	/**
	 * Instances of this class do nothing.
	 *
	 * @since 2018/03/16
	 */
	LcdServiceCall()
	{
		throw new todo.TODO();
	}
}

