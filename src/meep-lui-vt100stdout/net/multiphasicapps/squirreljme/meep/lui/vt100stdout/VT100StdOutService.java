// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui.vt100stdout;

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.meep.lui.DisplayService;

/**
 * This is a service which writes to a standard stream which understands the
 * VT100 escape codes to have more ability in changing what is displayed on
 * an output terminal.
 *
 * @since 2016/09/08
 */
public class VT100StdOutService
	extends DisplayService
{
	/** Default terminal column size. */
	public static final int DEFAULT_COLUMNS =
		80;
	
	/** Default terminal column rows. */
	public static final int DEFAULT_ROWS =
		24;
	
	/** The stream to write to. */
	protected final PrintStream out;
	
	/**
	 * Initializes the output service to a standard VT100 display.
	 *
	 * @param __ps The stream to write to.
	 * @param __c The number of columns to use.
	 * @param __r The number of rows to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/08
	 */
	public VT100StdOutService(PrintStream __ps, int __c, int __r)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __ps;
		
		// Set size
		setDisplaySize(__c, __r);
	}
}

