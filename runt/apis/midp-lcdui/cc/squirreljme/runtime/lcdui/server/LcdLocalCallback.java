// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.system.type.Array;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import cc.squirreljme.runtime.lcdui.LcdCallback;

/**
 * This class contains a reference to the method to be called back when a
 * callback needs to be performed by the widget to user code.
 *
 * @since 2018/04/03
 */
public final class LcdLocalCallback
{
	/** The method to execute for the callback. */
	protected final RemoteMethod method;
	
	/**
	 * Initializes the callback.
	 *
	 * @param __m The method to callback for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/03
	 */
	public LcdLocalCallback(RemoteMethod __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.method = __m;
	}
	
	/**
	 * Specifies that the given displayable should be painted.
	 *
	 * @param __what What is going to be painted?
	 * @param __pf The format the pixels are in.
	 * @param __cx The clipping X coordinate.
	 * @param __cy The clipping Y coordinate.
	 * @param __cw The clipping width.
	 * @param __ch The clipping height.
	 * @param __buf The buffer to send the result into after drawing.
	 * @param __pal The palette to use for the remote buffer.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @param __alpha Is an alpha channel being used?
	 * @param __pitch The number of elements for the width in the buffer.
	 * @param __offset The offset into the buffer to the actual image data.
	 * @throws NullPointerException On null arguments except for {@code __pal}.
	 * @since 2018/03/18
	 */
	public final void paint(LcdWidget __what, PixelFormat __pf, int __cx,
		int __cy, int __cw, int __ch, Array __buf, IntegerArray __pal,
		int __bw, int __bh, boolean __alpha, int __pitch, int __offset)
		throws NullPointerException
	{
		if (__what == null || __pf == null || __buf == null)
			throw new NullPointerException("NARG");
		
		// Perform the call if it is attached
		RemoteMethod method = this.method;
		if (method != null)
			method.<VoidType>invoke(VoidType.class, LcdCallback.WIDGET_PAINT,
				__what.handle, __pf, __cx, __cy, __cw, __ch, __buf, __pal,
				__bw, __bh, __alpha, __pitch, __offset);
	}
	
	/**
	 * Callback that the size of the widget has changed.
	 *
	 * @param __what What has had its size changed?
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public final void sizeChanged(LcdWidget __what, int __w, int __h)
		throws NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		RemoteMethod method = this.method;
		if (method != null)
			method.<VoidType>invoke(VoidType.class,
				LcdCallback.WIDGET_SIZE_CHANGED, __what.handle, __w, __h);
	}
}

