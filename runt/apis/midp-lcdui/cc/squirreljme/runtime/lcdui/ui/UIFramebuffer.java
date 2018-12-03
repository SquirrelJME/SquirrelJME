// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

/**
 * This class contains information on the framebuffer.
 *
 * @since 2018/11/18
 */
public final class UIFramebuffer
{
	/** The pixel format of the buffer. */
	protected final PixelFormat pixelformat;
	
	/** The buffer data. */
	protected final Object buffer;
	
	/** The palette. */
	protected final int[] palette;
	
	/** The buffer width. */
	protected final int bufferwidth;
	
	/** The buffer height. */
	protected final int bufferheight;
	
	/** Is the alpha channel used? */
	protected final boolean alpha;
	
	/** The pitch. */
	protected final int pitch;
	
	/** The offset into the buffer. */
	protected final int offset;
	
	/** The virtual X origina. */
	protected final int virtxorig;
	
	/** The virtual Y origin. */
	protected final int virtyorig;
	
	/**
	 * Initializes the framebuffer.
	 *
	 * @param __pf The pixel format.
	 * @param __buf The buffer data.
	 * @param __pal The palette.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height,
	 * @param __alpha Is alpha used?
	 * @param __p The pitch.
	 * @param __off The offset.
	 * @param __vxo Virtual X offset.
	 * @param __vyo Virtual Y offset.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public UIFramebuffer(PixelFormat __pf, Object __buf, int[] __pal,
		int __bw, int __bh, boolean __alpha, int __p, int __off,
		int __vxo, int __vyo)
		throws NullPointerException
	{
		if (__pf == null || __buf == null)
			throw new NullPointerException("NARG");
		
		this.pixelformat = __pf;
		this.buffer = __buf;
		this.palette = __pal;
		this.bufferwidth = __bw;
		this.bufferheight = __bh;
		this.alpha = __alpha;
		this.pitch = __p;
		this.offset = __off;
		this.virtxorig = __vxo;
		this.virtyorig = __vyo;
	}
	
	/**
	 * Returns the graphics object for this framebuffer, covering the entire
	 * area.
	 *
	 * @return The graphics for this framebuffer.
	 * @since 2018/11/18
	 */
	public final Graphics graphics()
	{
		// Pixel format takes care of all of this
		return this.pixelformat.createGraphics(this.buffer, this.palette,
			this.bufferwidth, this.bufferheight, this.alpha,
			this.pitch, this.offset, this.virtxorig, this.virtyorig);
	}
	
	/**
	 * Loads the frame buffer for the given native display.
	 *
	 * @param __nid The native display.
	 * @return The framebuffer for this display.
	 * @since 2018/11/18
	 */
	public static UIFramebuffer loadNativeFramebuffer(int __nid)
	{
		// Get framebuffer data
		Object buf = NativeDisplayAccess.framebufferObject(__nid);
		int[] pal = NativeDisplayAccess.framebufferPalette(__nid);
		int[] params = NativeDisplayAccess.framebufferParameters(__nid);
		
		// Build framebuffer object
		return new UIFramebuffer(
			PixelFormat.of(params[NativeDisplayAccess.PARAMETER_PIXELFORMAT]),
			buf,
			pal,
			params[NativeDisplayAccess.PARAMETER_BUFFERWIDTH],
			params[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT],
			params[NativeDisplayAccess.PARAMETER_ALPHA] != 0,
			params[NativeDisplayAccess.PARAMETER_PITCH],
			params[NativeDisplayAccess.PARAMETER_OFFSET],
			params[NativeDisplayAccess.PARAMETER_VIRTXOFF],
			params[NativeDisplayAccess.PARAMETER_VIRTYOFF]);
	}
}

