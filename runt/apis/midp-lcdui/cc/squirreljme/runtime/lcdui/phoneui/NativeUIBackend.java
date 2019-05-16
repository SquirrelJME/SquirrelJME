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
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

/**
 * This is a backend which utilizes the SquirrelJME UI interface for all of
 * the related drawing applications.
 *
 * @since 2019/05/16
 */
public final class NativeUIBackend
	implements PhoneDisplayBackend
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
	public final boolean isUpsidedown()
	{
		return NativeDisplayAccess.isUpsideDown(this.nid);
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
	
	/*
	NativeDisplayAccess.displayRepaint(state.nativeid, 0, 0, w, h);
	*/
}

