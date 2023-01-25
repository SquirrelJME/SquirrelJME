// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandLayoutPolicy;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * Layout handler for soft keys.
 *
 * @since 2021/11/30
 */
final class __SoftKeyLayout__
	implements CommandLayoutPolicy
{
	/**
	 * Performs layout of the commands.
	 *
	 * @param __d The displayable this is for.
	 * @since 2021/11/30
	 */
	@Override
	public void onCommandLayout(Displayable __d)
	{
		// Set the position of each used command
		for (Command command : __d.getCommands())
		{
			// We can cheat here and refer to the priority to store the
			// position so we do not have to keep references back and forth
			// and such and otherwise.
			__d.setCommand(command,
				Display.SOFTKEY_BOTTOM + 1 + command.getPriority());
		}
	}
}
