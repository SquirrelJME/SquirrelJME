// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import org.jetbrains.annotations.Async;

/**
 * This interface is for any listeners on commands when they occur.
 * 
 * @since 2020/11/13
 */
@Api
public interface CommandListener
{
	/**
	 * This is called when an action has been performed on a command.
	 * 
	 * @param __command The command that has been activated.
	 * @param __displayable The displayable the command is acting under.
	 * @since 2020/11/13
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	void commandAction(Command __command, Displayable __displayable);
}


