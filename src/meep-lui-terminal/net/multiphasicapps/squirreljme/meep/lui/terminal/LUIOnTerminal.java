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
import net.multiphasicapps.squirreljme.meep.lui.DisplayScreen;
import net.multiphasicapps.squirreljme.terminal.Terminal;
import net.multiphasicapps.squirreljme.terminal.TerminalScreen;

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
	
	/** The screen display to use. */
	protected final DisplayScreen screen;
	
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
		
		// Setup initial screen to match the terminal size
		TerminalScreen term = __t.screen();
		this.screen = new DisplayScreen(__t.columns, __t.rows);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public boolean isHardwareAssigned()
	{
		// There is always only a single hardware where input is grabbed from
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public DisplayScreen screen()
	{
		return this.screen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public void setHardwareAssigned(boolean __h)
	{
		// There is only ever one terminal associated with this and as such
		// any call of this has no effect
	}
}

