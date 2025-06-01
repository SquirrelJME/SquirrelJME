// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.lcdui.gfx.DoubleBuffer;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;
import cc.squirreljme.runtime.nttdocomo.ui.LockFlush;
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
	final DoubleBuffer _doubleBuffer =
		new DoubleBuffer(0xFFFFFFFF);
	
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
		
		// This is drawn transparent, as we double buffer and never wipe
		// what is drawn
		this.setPaintMode(false);
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
		
		// Forward to handler
		rv.__key(true, Display.__mapKey(__code));
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
		
		// Forward to handler
		rv.__key(false, Display.__mapKey(__code));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/02/14
	 */
	@Override
	protected void keyRepeated(int __code)
	{
		// There are no key repeats in i-mode, so ignore it as most
		// applications will get very confused by them
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
		
		// Only draw if not being called out of thread, this is a shim of
		// sorts for DoJa applications that either draw correctly or draw
		// themselves in the main loop when they should not
		LockFlush lockFlush = rv._lockFlush;
		DoubleBuffer doubleBuffer = this._doubleBuffer;
		if (!lockFlush.outOfThread())
		{
			// Draw with this buffer size
			int w = rv.getWidth();
			int h = rv.getHeight();
			
			// Perform a standard paint within i-mode using our double buffered
			// image
			BGColor bgColor = rv._bgColor;
			Graphics mg = doubleBuffer.getGraphics(w, h);
			com.nttdocomo.ui.Graphics g = new __Graphics2__(mg,
				bgColor, lockFlush);
			
			// Forward paint call
			rv.paint(g);
		}
		
		// Regardless of whether this drawn in another thread incorrectly,
		// a number of DoJa software depends on the actual proper drawing to
		// perform the actual buffer update
		// If the buffer is locked, do not update as the view will flicker
		if (!lockFlush.isLocked())
		{
			// Paint the buffer to the given target
			doubleBuffer.flush();
			doubleBuffer.paint(__g);
		}
	}
}
