// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal.vt;

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.terminal.Terminal;

/**
 * This implements a terminal drawing interface which can draw terminal
 * screens using standard VT family escape sequences.
 *
 * @since 2016/09/11
 */
public class VTTerminal
	implements Terminal
{
	/** Terminal display output. */
	protected final PrintStream output;
	
	/**
	 * Initializes the terminal output.
	 *
	 * @param __ps The stream to write data to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	VTTerminal(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __ps;
	}
}

