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
	 * @since 2019/05/16
	 */
	public final Graphics graphics()
	{
		// If acceleration is enabled, try to get accelerated graphics
		int nid = this.nid;
		if (USE_ACCELERATION)
			try
			{
				return AcceleratedGraphics.instance(nid);
			}
			catch (UnsupportedOperationException e)
			{
			}
			
		// Get data buffers and properties
		Object buf = NativeDisplayAccess.framebufferObject(nid);
		int[] pal = NativeDisplayAccess.framebufferPalette(nid);
		int[] params = NativeDisplayAccess.framebufferParameters(nid);
		
		// Set parameters
		int width = params[NativeDisplayAccess.PARAMETER_BUFFERWIDTH];
		int height = params[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT];
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
			// Do painting operations (UI stuff)
			ad.paint(__x, __y, __w, __h);
			
			// This image will be drawn onto the screen
			Image image = ad.image;
			
			// Get screen graphics
			Graphics g = this.graphics();
			
			// Draw our image on the screen
			g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
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
		
		todo.TODO.note("Implement");
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

