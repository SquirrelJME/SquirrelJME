// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.lcdui.SerializedEvent;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;

/**
 * This is a base class for items which need to be exposed action wise and
 * such. All methods here by default do nothing.
 *
 * @since 2019/05/17
 */
public abstract class ExposedDisplayable
{
	/**
	 * Gets the commands which are available to use.
	 *
	 * @return The available commands.
	 * @since 2019/05/17
	 */
	public Command[] getCommands()
	{
		return new Command[0];
	}
	
	/**
	 * Is this widget a full-screen one?
	 *
	 * @return If this is full-screen or not.
	 * @since 2019/05/17
	 */
	protected boolean isFullscreen()
	{
		return false;
	}
	
	/**
	 * Is this display transparent?
	 *
	 * @return If the display is transparent.
	 * @since 2019/05/17
	 */
	protected boolean isTransparent()
	{
		return true;
	}
	
	/**
	 * This is called when a key has been pressed.
	 *
	 * @param __code The key code, the character is not modified by modifiers.
	 * @since 2019/05/17
	 */
	@SerializedEvent
	protected void keyPressed(int __code)
	{
	}
	
	/**
	 * This is called when a key has been released.
	 *
	 * @param __code The key code, the character is not modified by modifiers.
	 * @since 2019/05/17
	 */
	@SerializedEvent
	protected void keyReleased(int __code)
	{
	}
	
	/**
	 * This is called when a key has been repeated.
	 *
	 * @param __code The key code, the character is not modified by modifiers.
	 * @since 2019/05/17
	 */
	@SerializedEvent
	protected void keyRepeated(int __code)
	{
	}
	
	/**
	 * This is called when the displayable needs to be painted onto the
	 * screen.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2019/05/17
	 */
	@SerializedEvent
	protected void paint(Graphics __g)
	{
	}
	
	/**
	 * This is called when the size of the displayable has changed.
	 *
	 * @param __w The new width of the displayable.
	 * @param __h The new heigh of the displayable.
	 * @since 2019/05/17
	 */
	@SerializedEvent
	protected void sizeChanged(int __w, int __h)
	{
	}
}

