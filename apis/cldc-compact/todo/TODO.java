// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import java.io.PrintStream;

/**
 * This exception is thrown.
 *
 * When constructed, this exception does not normall finish execution.
 *
 * @since 2017/02/28
 */
public class TODO
	extends Error
{
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @since 2017/02/28
	 */
	public TODO()
	{
		// Print a starting banner
		PrintStream ps = System.err;
		ps.println("*******************************************************");
		ps.println("INCOMPLETE CODE HAS BEEN REACHED:");
		
		// Print the trace
		printStackTrace(ps);
		
		// Ending banner
		ps.println("*******************************************************");
		
		// At least try to exit
		try
		{
			System.exit(1);
		}
		
		// Ignore
		catch (SecurityException e)
		{
		}
	}
}

