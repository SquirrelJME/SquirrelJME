// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

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
public interface CommandLayoutPolicy
{
	/**
	 * Callback for when the policy is being updated.
	 * 
	 * @param __d The displayable getting the policy set.
	 * @since 2020/09/27
	 */
	void onCommandLayout(Displayable __d);
}

