// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.lcdui.gfx.DoubleBuffer;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

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
	
	/** Are game keys being suppressed?. */
	private volatile boolean _suppressGameKeys;
	
	/** Is the buffer preserved after a flush? */
	private volatile boolean _preserveBuffer;
	
	/** The double buffered image. */
	private final DoubleBuffer _doubleBuffer =
		new DoubleBuffer();
	
	/**
	 * Initializes the game canvas.
	 *
	 * The buffer is preserved by default.
	 *
	 * @param __suppressGameKeys If {@code true} then game key events are
	 * suppressed.
	 * @since 2016/10/08
	 */
	protected GameCanvas(boolean __suppressGameKeys)
	{
		this(__suppressGameKeys, true);
	}
	
	/**
	 * Initializes the game canvas.
	 *
	 * It may be an optimization if key events are suppressed if a canvas is
	 * only needed to draw and not receive user input. The only key events
	 * that are suppressed are game keys.
	 *
	 * @param __suppressGameKeys If {@code true} then game key events are
	 * suppressed.
	 * @param __preserveBuffer If {@code true} then the buffer is preserved
	 * after a flush, otherwise if {@code false} the buffer data will be
	 * undefined.
	 * @see Canvas
	 * @since 2016/10/08
	 */
	protected GameCanvas(boolean __suppressGameKeys, boolean __preserveBuffer)
	{
		// Set
		this._suppressGameKeys = __suppressGameKeys;
		this._preserveBuffer = __preserveBuffer;
	}
	
	/**
	 * Flushes the full off-screen buffer to the display.
	 *
	 * @since 2017/02/08
	 */
	public void flushGraphics()
	{
		// Do nothing if this is not on a display
		if (this.getCurrentDisplay() == null)
			return;
		
		// Flip!
		this.__flip();
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
		
		// Do nothing if this is not on a display
		if (this.getCurrentDisplay() == null)
			return;
		
		// {@squirreljme.error EB2w Cannot flush the graphics if the buffer
		// is not preserved.}
		if (!this._preserveBuffer)
			throw new IllegalStateException("EB2w");
		
		// Flip!
		this.__flip();
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
		return this._doubleBuffer.getGraphics(
			this.getWidth(), this.getHeight());
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
		// Just paint the on-screen buffer
		this._doubleBuffer.paint(__g);
	}
	
	/**
	 * Performs the graphics flip.
	 *
	 * @since 2019/06/30
	 */
	private void __flip()
	{
		// Flush off-screen to on-screen
		this._doubleBuffer.flush();
		
		// Signal and wait for refresh
		super.repaint(0, 0, this.getWidth(), this.getHeight());
		super.serviceRepaints();
	}
}
