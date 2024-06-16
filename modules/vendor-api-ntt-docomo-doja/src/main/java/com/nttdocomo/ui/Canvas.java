// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Canvas for showing free-form raster graphics and otherwise.
 *
 * @see javax.microedition.lcdui.Canvas
 * @since 2021/11/30
 */
@Api
public abstract class Canvas
	extends Frame
{
	/** The native Java Canvas. */
	final __MIDPCanvas__ _midpCanvas = new __MIDPCanvas__(
		new WeakReference<>(this));
	
	/** The timers which are associated with the canvas. */
	final Map<Integer, Reference<ShortTimer>> _shortTimers =
		new LinkedHashMap<>();
	
	/**
	 * Paints the given canvas.
	 *
	 * @param __g The graphics to use for drawing.
	 * @since 2024/03/05
	 */
	@Api
	public abstract void paint(Graphics __g);
	
	/**
	 * Initializes the base canvas.
	 *
	 * @since 2022/02/14
	 */
	@Api
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
	@Api
	public Graphics getGraphics()
	{
		// Use the backing double buffered graphics, but without a draw
		return new Graphics(
			this._midpCanvas._doubleBuffer.getGraphics(this.getWidth(),
				this.getHeight()), this._bgColor);
	}
	
	@Api
	public int getKeypadState()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void processEvent(int __type, int __param)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void repaint()
	{
		this.__displayable().repaint();
	}
	
	@Api
	public void repaint(int __x, int __y, int __w, int __h)
	{
		this.__displayable().repaint(__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	__MIDPCanvas__ __displayable()
	{
		return this._midpCanvas;
	}
}
