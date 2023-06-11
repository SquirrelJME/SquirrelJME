// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.lcdui.gfx.DoubleBuffer;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Graphics;

/**
 * This manages the canvas drawing operations for i-appli.
 *
 * @since 2021/11/30
 */
final class __MIDPCanvas__
	extends javax.microedition.lcdui.Canvas
{
	/** The canvas to forward to. */
	private final Reference<Canvas> _imodeCanvas;
	
	/** Double buffered image for drawing operations. */
	final DoubleBuffer _doubleBuffer = new DoubleBuffer(0xFFFFFFFF);
	
	/**
	 * Initializes the base canvas.
	 *
	 * @param __imodeCanvas The canvas to draw into.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	__MIDPCanvas__(Reference<Canvas> __imodeCanvas)
		throws NullPointerException
	{
		if (__imodeCanvas == null)
			throw new NullPointerException("NARG");
		
		this._imodeCanvas = __imodeCanvas;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/02/14
	 */
	@Override
	protected void keyPressed(int __code)
	{
		// Ignore event if missing
		Canvas rv = this._imodeCanvas.get();
		if (rv == null)
			return;
		
		rv.processEvent(Display.KEY_PRESSED_EVENT, Display.__mapKey(__code));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/02/14
	 */
	@Override
	protected void keyReleased(int __code)
	{
		// Ignore event if missing
		Canvas rv = this._imodeCanvas.get();
		if (rv == null)
			return;
		
		rv.processEvent(Display.KEY_RELEASED_EVENT, Display.__mapKey(__code));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/02/14
	 */
	@Override
	protected void keyRepeated(int __code)
	{
		// There are no key repeats in i-mode, so say it is a press
		this.keyPressed(__code);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	protected void paint(Graphics __g)
	{
		// Ignore paint if the canvas was GCed, this will eventually probably
		// go away
		Canvas rv = this._imodeCanvas.get();
		if (rv == null)
			return;
		
		// Perform a standard paint within i-mode using our double buffered
		// image
		DoubleBuffer doubleBuffer = this._doubleBuffer;
		rv.paint(new com.nttdocomo.ui.Graphics(
			doubleBuffer.getGraphics(rv.getWidth(), rv.getHeight()),
			rv._bgColor));
		
		// Paint the buffer to the given target
		doubleBuffer.flush();
		doubleBuffer.paint(__g);
	}
}
