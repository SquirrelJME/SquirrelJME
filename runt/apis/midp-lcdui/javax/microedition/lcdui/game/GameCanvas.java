// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.lcdui.gfx.ForwardingGraphics;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class GameCanvas
	extends Canvas
{
	public static final int DOWN_PRESSED =
		64;
	
	public static final int FIRE_PRESSED =
		256;
	
	public static final int GAME_A_PRESSED =
		512;
	
	public static final int GAME_B_PRESSED =
		1024;
	
	public static final int GAME_C_PRESSED =
		2048;
	
	public static final int GAME_D_PRESSED =
		4096;
	
	public static final int LEFT_PRESSED =
		4;
	
	public static final int RIGHT_PRESSED =
		32;
	
	public static final int UP_PRESSED =
		2;
	
	/** Forwarding graphics target, since they draw to the same buffer. */
	private final ForwardingGraphics _forwardgfx =
		new ForwardingGraphics();
	
	/** Are game keys being suppressed?. */
	private volatile boolean _suppressgamekeys;
	
	/** Is the buffer preserved after a flush? */
	private volatile boolean _preservebuffer;
	
	/** The image to draw onto as a double buffer. */
	private volatile Image _image;
	
	/**
	 * Initializes the game canvas.
	 *
	 * The buffer is preserved by default.
	 *
	 * @param __supke If {@code true} then game key events are suppressed.
	 * @since 2016/10/08
	 */
	protected GameCanvas(boolean __supke)
	{
		this(__supke, true);
	}
	
	/**
	 * Initializes the game canvas.
	 *
	 * It may be an optimization if key events are suppressed if a canvas is
	 * only needed to draw and not receive user input. The only key events
	 * that are suppressed are game keys.
	 *
	 * @param __supke If {@code true} then game key events are suppressed.
	 * @param __preservebuf If {@code true} then the buffer is preserved after
	 * a flush, otherwise if {@code false} the buffer data will be undefined.
	 * @see Canvas
	 * @since 2016/10/08
	 */
	protected GameCanvas(boolean __supke, boolean __preservebuf)
	{
		// Set
		this._suppressgamekeys = __supke;
		this._preservebuffer = __preservebuf;
	}
	
	/**
	 * Flushes the full off-screen buffer to the display.
	 *
	 * @since 2017/02/08
	 */
	public void flushGraphics()
	{
		flushGraphics(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * Flushes the specified off-screen buffer area to the display.
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws IllegalStateException If the buffer is preserved.
	 * @since 2017/02/28
	 */
	public void flushGraphics(int __x, int __y, int __w, int __h)
		throws IllegalStateException
	{
		// Nothing to flush
		if (__w <= 0 || __h <= 0)
			return;
		
		// {@squirreljme.error EB24 Cannot flush the graphics if the buffer
		// is not preserved.}
		if (!this._preservebuffer)
			throw new IllegalStateException("EB24");
		
		// Just tell the canvas to repaint because it is final and our paint
		// method just draws the backing buffer to the screen
		super.repaint(__x, __y, __w, __h);
	}
	
	/**
	 * This returns the off-screen buffer that is used by the game canvas to
	 * draw.
	 *
	 * @return The graphics object for the off-screen buffer.
	 * @since 2016/10/10
	 */
	protected Graphics getGraphics()
	{
		ForwardingGraphics forwardgfx = this._forwardgfx;
		
		// It is possible for the canvas to change size, as such the image in
		// the background must be recreated for the correct size
		int dw = Math.max(1, getWidth()),
			dh = Math.max(1, getHeight());
		Image image = this._image;
		if (image == null || dw != image.getWidth() || dh != image.getHeight())
		{
			this._image = (image = Image.createImage(dw, dh));
			forwardgfx.forwardGraphics(image.getGraphics());
		}
		
		// Always return the forwarded graphics
		return forwardgfx.forwardPlainGraphics();
	}
	
	public int getKeyStates()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	@Override
	public void paint(Graphics __g)
	{
		// The default implementation of this method just takes the offscreen
		// buffer and renders it to the canvas
		Image image = this._image;
		if (image == null)
			return;
		
		// Paint the image to the display
		// Do not worry about using transformations to display it
		__g.drawImage(image, 0, 0, 0);
	}
}


