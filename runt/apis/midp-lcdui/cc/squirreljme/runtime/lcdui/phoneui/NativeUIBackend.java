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
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

/**
 * This is a backend which utilizes the SquirrelJME UI interface for all of
 * the related drawing applications.
 *
 * @since 2019/05/16
 */
public final class NativeUIBackend
	implements NativeDisplayEventCallback, PhoneDisplayBackend
{
	/** The native display ID. */
	protected final int nid;
	
	/** The pixel format of the buffer. */
	protected final PixelFormat pixelformat;
		
	/** The buffer data. */
	protected final Object buffer;
	
	/** The palette. */
	protected final int[] palette;
	
	/** The buffer width. */
	public final int width;
	
	/** The buffer height. */
	public final int height;
	
	/** Is the alpha channel used? */
	protected final boolean alpha;
	
	/** The pitch. */
	protected final int pitch;
	
	/** The offset into the buffer. */
	protected final int offset;
	
	/** The virtual X origin. */
	protected final int virtxorig;
	
	/** The virtual Y origin. */
	protected final int virtyorig;
	
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
		Object buf = NativeDisplayAccess.framebufferObject(__nid);
		int[] pal = NativeDisplayAccess.framebufferPalette(__nid);
		int[] params = NativeDisplayAccess.framebufferParameters(__nid);
		
		// Set parameters
		this.pixelformat = PixelFormat.of(
			params[NativeDisplayAccess.PARAMETER_PIXELFORMAT]);
		this.buffer = buf;
		this.palette = pal;
		this.width = params[NativeDisplayAccess.PARAMETER_BUFFERWIDTH];
		this.height = params[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT];
		this.alpha = params[NativeDisplayAccess.PARAMETER_ALPHA] != 0;
		this.pitch = params[NativeDisplayAccess.PARAMETER_PITCH];
		this.offset = params[NativeDisplayAccess.PARAMETER_OFFSET];
		this.virtxorig = params[NativeDisplayAccess.PARAMETER_VIRTXOFF];
		this.virtyorig = params[NativeDisplayAccess.PARAMETER_VIRTYOFF];
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
	 * @since 2019/05/16
	 */
	@Override
	public final void command(int __d, int __c)
	{
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void exitRequest(int __d)
	{
		todo.TODO.note("Implement");
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
		todo.TODO.note("Implement");
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
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final PixelFormat pixelFormat()
	{
		return this.pixelFormat();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void pointerEvent(int __d, int __ty, int __x, int __y,
		int __time)
	{
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
		todo.TODO.note("Implement");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final void sizeChanged(int __d, int __w, int __h)
	{
		todo.TODO.note("Implement");
	}
}

