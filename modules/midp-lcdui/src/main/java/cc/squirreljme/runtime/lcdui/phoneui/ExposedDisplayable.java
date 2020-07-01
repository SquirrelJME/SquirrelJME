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
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Ticker;

/**
 * This is a base class for items which need to be exposed action wise and
 * such. All methods here by default do nothing.
 *
 * @since 2019/05/17
 */
@Deprecated
public abstract class ExposedDisplayable
{
	/** Drawing/action state. */
	final State _dstate =
		new State();
	
	/**
	 * Returns the command listener.
	 *
	 * @return The command listener.
	 * @since 2019/05/18
	 */
	protected CommandListener getCommandListener()
	{
		return null;
	}
	
	/**
	 * Gets the commands which are available to use.
	 *
	 * @return The available commands.
	 * @since 2019/05/17
	 */
	protected Command[] getCommands()
	{
		return new Command[0];
	}
	
	/**
	 * Gets the ticker which is being shown on this displayable.
	 *
	 * @return The ticker being shown or {@code null} if there is none.
	 * @since 2019/05/18
	 */
	protected Ticker getTicker()
	{
		return null;
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
	 * This is called when the pointer is being dragged across the canvas, a
	 * drag is when there is movement 
	 *
	 * This requires that motion events are supported which can be known by
	 * calling {@link Canvas#hasPointerMotionEvents()}.
	 *
	 * @param __x The X coordinate of the pointer, on the canvas origin.
	 * @param __y The Y coordinate of the pointer, on the canvas origin.
	 * @since 2017/02/12
	 */
	@SerializedEvent
	protected void pointerDragged(int __x, int __y)
	{
		// Does nothing by default
	}
	
	/**
	 * This is called when the pointer has been pressed on the canvas.
	 *
	 * This requires that pointer events are supported which can be known by
	 * calling {@link Canvas#hasPointerEvents()}.
	 *
	 * @param __x The X coordinate of the pointer, on the canvas origin.
	 * @param __y The Y coordinate of the pointer, on the canvas origin.
	 * @since 2019/05/18
	 */
	@SerializedEvent
	protected void pointerPressed(int __x, int __y)
	{
		// Does nothing by default
	}
	
	/**
	 * This is called when the pointer has been released on the canvas.
	 *
	 * This requires that pointer events are supported which can be known by
	 * calling {@link Canvas#hasPointerEvents()}.
	 *
	 * @param __x The X coordinate of the pointer, on the canvas origin.
	 * @param __y The Y coordinate of the pointer, on the canvas origin.
	 * @since 2019/05/18
	 */
	@SerializedEvent
	protected void pointerReleased(int __x, int __y)
	{
		// Does nothing by default
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

