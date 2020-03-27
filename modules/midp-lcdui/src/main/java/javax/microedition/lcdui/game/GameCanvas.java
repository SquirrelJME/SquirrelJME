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

import cc.squirreljme.runtime.lcdui.gfx.AdvancedGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ForwardingGraphics;
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
	
	/** Forwarding graphics target, since they draw to the same buffer. */
	private final ForwardingGraphics _forwardgfx =
		new ForwardingGraphics();
	
	/** Are game keys being suppressed?. */
	private volatile boolean _suppressgamekeys;
	
	/** Is the buffer preserved after a flush? */
	private volatile boolean _preservebuffer;
	
	/** The A buffer. */
	private volatile __Buffer__ _bufa;
	
	/** The B buffer. */
	private volatile __Buffer__ _bufb;
	
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
		if (!this._preservebuffer)
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
		// Draw into the B buffer
		__Buffer__ buf = this._bufb;
		
		// Get device size
		int dw = this.getWidth(),
			dh = this.getHeight();
		
		// Force device size into bounds
		if (dw < 1)
			dw = 1;
		if (dh < 1)
			dh = 1;
		
		// Get buffer size
		int bw, bh;
		if (buf != null)
		{
			bw = buf._width;
			bh = buf._height;
		}
		else
			bw = bh = 0;
		
		// Recreate the buffer?
		if (buf == null || bw != dw || bh != dh)
		{
			// Create buffer
			buf = new __Buffer__(dw, dh);
			
			// Store buffer state
			this._bufb = buf;
		}
		
		// Create graphics to wrap it, alpha is not used!
		return new AdvancedGraphics(buf._pixels, false, null,
			dw, dh, dw, 0, 0, 0);
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
		// Whatever is in the A buffer is drawn
		__Buffer__ buf = this._bufa;
		if (buf == null)
			return;
			
		// The fastest way to draw onto the screen is to do a direct draw
		// from the RGB pixel data
		int pw = buf._width;
		__g.drawRGB(buf._pixels, 0, pw, 0, 0, pw, buf._height, false);
	}
	
	/**
	 * Performs the graphics flip.
	 *
	 * @since 2019/06/30
	 */
	private void __flip()
	{
		// Get both buffers
		__Buffer__ bufa = this._bufa,
			bufb = this._bufb;
		
		// If never drawn onto, ignore
		if (bufb == null)
			return;
		
		// Get buffer size
		int bw = bufb._width,
			bh = bufb._height;
		
		// Create buffer to copy to
		if (bufa == null || bufa._width != bw && bufa._height != bh)
		{
			bufa = new __Buffer__(bw, bh);
			this._bufa = bufa;
		}
		
		// Copy pixel data (use System since it may be a memory copy)
		System.arraycopy(bufb._pixels, 0,
			bufa._pixels, 0, bw * bh);
		
		// Signal and wait for refresh
		super.repaint(0, 0, bw, bh);
		super.serviceRepaints();
	}
	
	/**
	 * This represents a single buffer, since this class is double buffered.
	 *
	 * @since 2019/06/30
	 */
	private static final class __Buffer__
	{
		/** The buffer pixels. */
		final int[] _pixels;
		
		/** The width. */
		final int _width;
		
		/** The height. */
		final int _height;
		
		/**
		 * Initializes the buffer.
		 *
		 * @param __w The width.
		 * @param __h The height.
		 * @since 2019/06/30
		 */
		__Buffer__(int __w, int __h)
		{
			this._width = (__w < 1 ? (__w = 1) : __w);
			this._height = (__h < 1 ? (__h = 1) : __h);
			this._pixels = new int[__w * __h];
		}
	}
}
