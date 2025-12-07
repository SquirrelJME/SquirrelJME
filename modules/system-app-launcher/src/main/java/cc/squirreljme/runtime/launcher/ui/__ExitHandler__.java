// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Exit handler.
 *
 * @since 2022/01/26
 */
final class __ExitHandler__
	implements CommandListener
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/01/26
	 */
	@Override
	public void commandAction(Command __c, Displayable __d)
	{
		// Exit the VM since we did an exit on the splash screen
		if (__c == MidletMain.EXIT_COMMAND)
			System.exit(0);
	}
}
