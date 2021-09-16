// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is an error which is thrown when a condition which should not occur
 * occurs.
 *
 * @since 2018/11/25
 */
@Deprecated
public class OOPS
	extends Error
{
	/**
	 * Generates an oops with no message.
	 *
	 * @since 2018/11/25
	 */
	@Deprecated
	public OOPS()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Generates an oops with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2018/11/25
	 */
	@Deprecated
	public OOPS(String __m)
	{
		throw Debugging.todo(__m);
	}
}

