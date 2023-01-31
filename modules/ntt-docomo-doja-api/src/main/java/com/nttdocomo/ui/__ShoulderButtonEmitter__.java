// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * This is used to handle shoulder buttons that are pressed and make it like
 * they are key events.
 *
 * @since 2022/02/14
 */
final class __ShoulderButtonEmitter__
	implements CommandListener
{
	/** The frame this references. */
	final Reference<Frame> _frame;
	
	/**
	 * Initializes the button handler.
	 *
	 * @param __frame The frame to call into.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	__ShoulderButtonEmitter__(Reference<Frame> __frame)
		throws NullPointerException
	{
		if (__frame == null)
			throw new NullPointerException("NARG");
		
		this._frame = __frame;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/02/14
	 */
	@Override
	public void commandAction(Command __command, Displayable __displayable)
	{
		// Ignore if missing
		Frame frame = this._frame.get();
		if (__command == null || !(frame instanceof Canvas))
			return;
		
		// Determine which key was pressed
		int whichKey;
		switch (__command.getPriority())
		{
			case 0:
				whichKey = Display.KEY_SOFT1;
				break;
			case 1:
				whichKey = Display.KEY_SOFT2;
				break;
			
			// Ignore, unknown
			default:
				return;
		}
		
		// Say that the shoulder button was pressed and released accordingly
		Canvas canvas = (Canvas)frame;
		canvas.processEvent(Display.KEY_PRESSED_EVENT, whichKey);
		canvas.processEvent(Display.KEY_RELEASED_EVENT, whichKey);
	}
}
