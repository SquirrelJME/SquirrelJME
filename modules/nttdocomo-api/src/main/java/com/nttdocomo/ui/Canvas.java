// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Displayable;

/**
 * Canvas for showing free-form raster graphics and otherwise.
 *
 * @see javax.microedition.lcdui.Canvas
 * @since 2021/11/30
 */
public class Canvas
	extends Frame
{
	/** The native Java Canvas. */
	final __MIDPCanvas__ _midpCanvas =
		new __MIDPCanvas__();
	
	public Graphics getGraphics()
	{
		throw Debugging.todo();
	}
	
	public int getKeypadState()
	{
		throw Debugging.todo();
	}
	
	public void processEvent(int __type, int __param)
	{
		throw Debugging.todo();
	}
	
	public void repaint()
	{
		throw Debugging.todo();
	}
	
	public void repaint(int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	Displayable __displayable()
	{
		return this._midpCanvas;
	}
}
