// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * This is an exit command which terminates the application.
 *
 * @since 2018/12/02
 */
public class Exit
	implements CommandListener
{
	/** The command used to exit. */
	public static final Command command =
		new Command("Exit", Command.EXIT, 1);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	public final void commandAction(Command __c, Displayable __d)
	{
		// Exiting the VM?
		if (__c == Exit.command)
		{
			System.exit(0);
		}
	}
}

