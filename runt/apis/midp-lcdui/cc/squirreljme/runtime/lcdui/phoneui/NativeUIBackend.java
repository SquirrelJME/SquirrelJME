// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
import cc.squirreljme.runtime.lcdui.gfx.AcceleratedGraphics;
import cc.squirreljme.runtime.lcdui.gfx.EnforcedDrawingAreaGraphics;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This is a backend which utilizes the SquirrelJME UI interface for all of
 * the related drawing applications.
 *
 * @since 2019/05/16
 */
public final class NativeUIBackend
	implements NativeDisplayEventCallback, PhoneDisplayBackend
{
	/**
	 * {@squirreljme.property cc.squirreljme.lcdui.acceleration=bool
	 * Should accelerated graphics be used if it is available? This defaults
	 * to {@code true} and it is recommended it be used, otherwise it may be
	 * disabled if it causes issues with some software.}
	 */
	public static boolean USE_ACCELERATION =
		Boolean.valueOf(System.getProperty("cc.squirreljme.lcdui.acceleration",
		"true"));
	
	/** The native display ID. */
	protected final int nid;
	
	/** The pixel format of the buffer. */
	protected final PixelFormat pixelformat;
	
	/** The active display to use. */
	private volatile ActiveDisplay _activedisplay;
	
	/**
	 * Initializes the native UI backend using the given display ID.
	 *
	 * @param __nid The native display ID this uses.
	 * @since 2019/05/16
	 */
	public NativeUIBackend(int __nid)
	{
		this.nid = __nid;
		
		// Get data buffers and properties
		int[] params = NativeDisplayAccess.framebufferParameters(__nid);
		
		// Set parameters
		this.pixelformat = PixelFormat.of(
			params[NativeDisplayAccess.PARAMETER_PIXELFORMAT]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void activate(ActiveDisplay __ad)
		throws NullPointerException
	{
		if (__ad == null)
			throw new NullPointerException("NARG");
		
		// Set the active display
		this._activedisplay = __ad;
		
		// Register self for event callbacks
		NativeDisplayAccess.registerEventCallback(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/17
	 */
	@Override
	public final int capabilities()
	{
		return NativeDisplayAccess.capabilities(this.nid);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void command(int __d, int __c)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void exitRequest(int __d)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		// Forward
		this._activedisplay.exitRequest();
	}
	
	/**
	 * Returns the native display graphics.
	 *
	 * @param __a Output boolean if this is accelerated.
	 * @param __dims Screen dimensions.
	 * @since 2019/05/16
	 */
	public final Graphics graphics(boolean[] __a, int[] __dims)
		throws NullPointerException
	{
		if (__dims == null)
			throw new NullPointerException("NARG");
		
		// Read width and height
		int[] params = NativeDisplayAccess.framebufferParameters(nid);
		int width = params[NativeDisplayAccess.PARAMETER_BUFFERWIDTH];
		int height = params[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT];
		
		// Set
		__dims[0] = width;
		__dims[1] = height;
		
		// If acceleration is enabled, try to get accelerated graphics
		int nid = this.nid;
		if (USE_ACCELERATION)
			try
			{
				// Try to get it
				Graphics rv = AcceleratedGraphics.instance(nid);
				
				// Set acceleration flag
				if (__a != null && __a.length > 1)
					__a[0] = true;
				
				return rv;
			}
			catch (UnsupportedOperationException e)
			{
			}
			
		// Get data buffers and properties
		Object buf = NativeDisplayAccess.framebufferObject(nid);
		int[] pal = NativeDisplayAccess.framebufferPalette(nid);
		
		// Set parameters
		boolean alpha = params[NativeDisplayAccess.PARAMETER_ALPHA] != 0;
		int pitch = params[NativeDisplayAccess.PARAMETER_PITCH];
		int offset = params[NativeDisplayAccess.PARAMETER_OFFSET];
		int virtxorig = params[NativeDisplayAccess.PARAMETER_VIRTXOFF];
		int virtyorig = params[NativeDisplayAccess.PARAMETER_VIRTYOFF];
		
		// Create graphics from it
		return this.pixelformat.createGraphics(buf, pal, width, height,
			alpha, pitch, offset, virtxorig, virtyorig);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final boolean isUpsidedown()
	{
		return NativeDisplayAccess.isUpsideDown(this.nid);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void keyEvent(int __d, int __ty, int __kc, int __ch,
		int __time)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		// No display active?
		ActiveDisplay activedisplay = this._activedisplay;
		if (activedisplay == null)
			return;
		
		// No displayable shown?
		Displayable current = activedisplay._current;
		if (current == null)
			return;
		
		// Normalize all key types to mobile cell phone format, if not
		// specified
		if (__ch == '#')
			__kc = Canvas.KEY_POUND;
		else if (__ch == '*')
			__kc = Canvas.KEY_STAR;
		else if (__ch >= '0' && __ch <= '9')
			__kc = Canvas.KEY_NUM0 + (__ch - '0');
			
		// If the key-code is not valid then ignore
		if (__kc == NonStandardKey.UNKNOWN)
			return;
		
		// Depends on the action
		switch (__ty)
		{
			case NativeDisplayEventCallback.KEY_PRESSED:
				current.keyPressed(__kc);
				break;
			
			case NativeDisplayEventCallback.KEY_REPEATED:
				current.keyRepeated(__kc);
				break;
			
			case NativeDisplayEventCallback.KEY_RELEASED:
				current.keyReleased(__kc);
				break;
			
			default:
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void lostCallback()
	{
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void paintDisplay(int __d, int __x, int __y,
		int __w, int __h)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		// Paint whatever is in the active display
		ActiveDisplay ad = this._activedisplay;
		if (ad != null)
		{
			// Get screen graphics and dimensions
			int[] dims = new int[2];
			Graphics g = this.graphics(null, dims);
			
			// Get display size and desired display size
			int gw = dims[0],
				gh = dims[1],
				dw = ad.width,
				dh = ad.height;
			
			// Perform potential centering?
			/*if (gw > dw && gh > dh)
				g = new EnforcedDrawingAreaGraphics(g,
					(gw >> 1) - (dw >> 1), (gh >> 1) - (dh >> 1),
					dw, dh);*/
			
			// Set clipping area
			g.setClip(0, 0, dw, dh);
			
			// Perform painting operation
			ad.paint(g, __x, __y, __w, __h);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final PixelFormat pixelFormat()
	{
		return this.pixelformat;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void pointerEvent(int __d, int __ty, int __x, int __y,
		int __time)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		NativeDisplayAccess.displayRepaint(this.nid, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void shown(int __d, int __shown)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void sizeChanged(int __d, int __w, int __h)
	{
		// Not our display?
		if (__d != this.nid)
			return;
		
		ActiveDisplay ad = this._activedisplay;
		if (ad != null)
		{
			int[] dm = new int[4];
			if (ad.realize(dm))
				((ExposedDisplayable)ad._current).sizeChanged(dm[2], dm[3]);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/17
	 */
	@Override
	public final boolean vibrate(int __ms)
	{
		// Not supported here
		return false;
	}
}

