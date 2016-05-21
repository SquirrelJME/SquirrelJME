// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.PrintStream;

/**
 * This is a kernel tracer which just prints to an output stream.
 *
 * @since 2016/05/21
 */
public final class KernelTracePrinter
	extends KernelTrace
{
	/** The output stream to print to. */
	protected final PrintStream out;
	
	/**
	 * Intiializes the trace printer.
	 *
	 * @param __out The output stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	public KernelTracePrinter(PrintStream __out)
		throws NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __out;
	}
}

