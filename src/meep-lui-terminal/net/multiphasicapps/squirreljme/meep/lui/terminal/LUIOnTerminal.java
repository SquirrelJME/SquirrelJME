// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui.terminal;

import net.multiphasicapps.squirreljme.meep.lui.DisplayDriver;
import net.multiphasicapps.squirreljme.terminal.Terminal;

/**
 * This implements the LUI display on top of an existing terminal.
 *
 * @since 2016/09/08
 */
public class LUIOnTerminal
	extends DisplayDriver
{
	/** The terminal to use. */
	protected final Terminal terminal;
	
	/**
	 * Initializes the line based user interface on a given terminal.
	 *
	 * @param __t The terminal to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public LUIOnTerminal(Terminal __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.terminal = __t;
	}
}

