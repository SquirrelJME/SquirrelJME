// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.WeakReference;

/**
 * Canvas for showing free-form raster graphics and otherwise.
 *
 * @see javax.microedition.lcdui.Canvas
 * @since 2021/11/30
 */
public abstract class Canvas
	extends Frame
{
	/** The native Java Canvas. */
	final __MIDPCanvas__ _midpCanvas =
		new __MIDPCanvas__(new WeakReference<>(this));
	
	public abstract void paint(Graphics __g);
	
	/**
	 * Initializes the base canvas.
	 * 
	 * @since 2022/02/14
	 */
	public Canvas()
	{
		// Needed to initialize the command listener 
		this.__postConstruct();
	}
	
	/**
	 * Returns the graphics object that is used for drawing onto the canvas.
	 * 
	 * @return A {@link Graphics} object for drawing onto the canvas surface.
	 * @since 2022/02/25
	 */
	public Graphics getGraphics()
	{
		// Use the backing double buffered graphics, but without a draw
		return new com.nttdocomo.ui.Graphics(
			this._midpCanvas._doubleBuffer.getGraphics(
				this.getWidth(), this.getHeight()),
			this._bgColor);
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
		this.__displayable().repaint();
	}
	
	public void repaint(int __x, int __y, int __w, int __h)
	{
		this.__displayable().repaint(__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	__MIDPCanvas__ __displayable()
	{
		return this._midpCanvas;
	}
}
