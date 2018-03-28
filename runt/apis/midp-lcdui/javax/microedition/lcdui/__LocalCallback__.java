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

import cc.squirreljme.runtime.cldc.system.type.Array;
import cc.squirreljme.runtime.cldc.system.type.ArrayType;
import cc.squirreljme.runtime.cldc.system.type.ArrayUtils;
import cc.squirreljme.runtime.cldc.system.type.ByteArray;
import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalArray;
import cc.squirreljme.runtime.cldc.system.type.LocalByteArray;
import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalShortArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.ShortArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.gfx.AbstractArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ByteIndexed1ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ByteIndexed2ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ByteIndexed4ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ByteIndexed8ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ByteRGB332ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.IntArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.IntArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.IntArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.IntegerARGB8888ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.IntegerRGB888ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.PixelArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import cc.squirreljme.runtime.lcdui.gfx.ShortARGB4444ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ShortIndexed16ArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.ShortRGB565ArrayGraphics;
import cc.squirreljme.runtime.lcdui.LcdCallback;
import cc.squirreljme.runtime.lcdui.LcdException;


/**
 * This is the callback used for displays so that the remote server can call
 * local methods accordingly and do important things.
 *
 * @since 2018/03/18
 */
final class __LocalCallback__
	extends RemoteMethod
{
	/** The single instance of the local callback. */
	static final __LocalCallback__ INSTANCE =
		new __LocalCallback__();
	
	/**
	 * Initializes the display callback.
	 *
	 * @since 2018/03/18
	 */
	private __LocalCallback__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	protected final Object internalInvoke(Object[] __args)
	{
		if (__args == null)
			__args = new Object[0];
		
		LcdCallback func = ((EnumType)__args[0]).<LcdCallback>asEnum(
			LcdCallback.class);
		switch (func)
		{
			case WIDGET_PAINT:
				this.__widgetPaint(
					(Integer)__args[1],
					((EnumType)__args[2]).<PixelFormat>asEnum(
						PixelFormat.class),
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Integer)__args[6],
					(Array)__args[7],
					(IntegerArray)__args[8],
					(Integer)__args[9],
					(Integer)__args[10],
					(Boolean)__args[11],
					(Integer)__args[12],
					(Integer)__args[13]);
				return VoidType.INSTANCE;
			
			case WIDGET_SHOWN:
				this.__widgetShown(
					(Integer)__args[1],
					(Boolean)__args[2]);
				return VoidType.INSTANCE;
			
			case WIDGET_SIZE_CHANGED:
				this.__widgetSizeChanged(
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return VoidType.INSTANCE;
			
				// {@squirreljme.error EB23 Unknown LCDUI callback function.
				// (The function)}
			default:
				throw new RuntimeException(String.format("EB23 %s", func));
		}
	}
	
	/**
	 * Specifies that the given widget should be painted.
	 *
	 * @param __d The widget to paint.
	 * @param __pf Pixel format to use.
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
	private void __widgetPaint(int __d, PixelFormat __pf, int __cx, int __cy,
		int __cw, int __ch, Array __buf, IntegerArray __pal, int __bw,
		int __bh, boolean __alpha, int __pitch, int __offset)
		throws NullPointerException
	{
		if (__pf == null || __buf == null)
			throw new NullPointerException("NARG");
		
		// Can be a canvas or custom item
		__Widget__ on = __Queue__.INSTANCE.__getWidget(__d);
		if (on == null)
			return;
		
		// Copy the palette
		int[] pal = null;
		if (__pal != null)
		{
			int n = __pal.length();
			pal = new int[n];
			__pal.get(0, pal, 0, n);
		}
		
		// Create a local buffer which is stored completely within the
		// clipping region, although for simpler operation in copying, keep
		// the pitch but lose the y values
		// So this will end up just copying only Y values as drawn from the
		// source buffer because otherwise more calls will be needed for
		// interleaving.
		Object shadow = __pf.createBuffer(__pitch, __ch);
		
		// Setup graphics object to draw on with an absolute translation
		// offset from the clipping rectangle
		AbstractArrayGraphics g = __pf.createGraphics(
			shadow, pal, __cw, __ch, __alpha, __pitch, 0, 0, -__cy);
		
		// Set the clipping bounds so bytes outside of the area are not drawn
		// into at all.
		g.setClip(__cx, __cy, __cw, __ch);
		
		// Get the start and end element of the drawing region
		int cs = g.getClipElementStart(),
			cw = g.getClipElementEnd() - cs,
			olds = __offset + (__pitch * __cy) + cs;
		
		// Copy the old buffer data into this one before drawing
		ArrayUtils.copy(__buf, olds, shadow, cs, cw);
		
		// Perform the actual painting operation
		on.__doPaint(g, __bw, __bh);
		
		// Since a new buffer was setup temporarily, copy the drawn bytes
		// into the destination
		ArrayUtils.copy(shadow, cs, __buf, olds, cw);
	}
	
	/**
	 * This is called when the visibility state of a widget has changed.
	 *
	 * @param __d The widget which has changed.
	 * @param __shown Is this widget shown?
	 * @since 2018/03/24
	 */
	private final void __widgetShown(int __d, boolean __shown)
	{
		__Widget__ on = __Queue__.INSTANCE.__getWidget(__d);
		if (on == null)
			return;
		on.__doShown(__shown);
	}
	
	/**
	 * Specifies that the given displayable has changed size.
	 *
	 * @param __d The displayable which had its size changed.
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2018/03/23
	 */
	private final void __widgetSizeChanged(int __d, int __w, int __h)
	{
		__Widget__ on = __Queue__.INSTANCE.__getWidget(__d);
		if (on == null)
			return;
		
		on.__doSizeChanged(__w, __h);
	}
	
	/**
	 * Setups up a new buffer that is drawn into instead of the original
	 * source buffer.
	 *
	 * @param __a The buffer to shadow.
	 * @param __p The pitch of the image.
	 * @param __h The height of the graphics.
	 * @return The shadowed buffer or {@code __a} if it is not shadowed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	private static final Array __shadowBuffer(Array __a, int __p, int __h)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Do not shadow local arrays
		if (__a instanceof LocalArray)
			return __a;
		
		throw new todo.TODO();
	}
}

