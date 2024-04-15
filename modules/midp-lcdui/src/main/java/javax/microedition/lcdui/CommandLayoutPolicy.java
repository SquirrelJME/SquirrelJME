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
 * This interface is called when commands need to be laid out onto the display.
 * 
 * This allows the use of:
 *  - {@link Displayable#setCommand(Command, int)}.
 *  - {@link Displayable#setMenu(Menu, int)}.
 *  - {@link Displayable#removeCommandOrMenu(int)}. 
 * 
 * @since 2020/09/27
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface CommandLayoutPolicy
{
	/**
	 * Callback for when the policy is being updated.
	 * 
	 * @param __d The displayable getting the policy set.
	 * @since 2020/09/27
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	void onCommandLayout(Displayable __d);
}

