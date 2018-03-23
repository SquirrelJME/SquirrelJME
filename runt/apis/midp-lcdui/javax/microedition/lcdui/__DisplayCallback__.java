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
import cc.squirreljme.runtime.cldc.system.type.ByteArray;
import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.ShortArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.LcdCallback;
import cc.squirreljme.runtime.lcdui.LcdException;
import cc.squirreljme.runtime.lcdui.gfx.IntArrayGraphics;

/**
 * This is the callback used for displays so that the remote server can call
 * local methods accordingly and do important things.
 *
 * @since 2018/03/18
 */
final class __DisplayCallback__
	extends RemoteMethod
{
	/**
	 * Initializes the display callback.
	 *
	 * @since 2018/03/18
	 */
	__DisplayCallback__()
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
			case DISPLAYABLE_PAINT:
				this.__displayablePaint(
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Array)__args[6],
					(IntegerArray)__args[7],
					(Integer)__args[8],
					(Integer)__args[9],
					(Boolean)__args[10],
					(Integer)__args[11],
					(Integer)__args[12]);
				return VoidType.INSTANCE;
			
			case DISPLAYABLE_SIZE_CHANGED:
				this.__displayableSizeChanged(
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
	 * Specifies that the given displayable should be painted.
	 *
	 * @param __d The displayable to paint.
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
	private void __displayablePaint(int __d, int __cx, int __cy,
		int __cw, int __ch, Array __buf, IntegerArray __pal, int __bw,
		int __bh, boolean __alpha, int __pitch, int __offset)
		throws NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		// Only canvases are drawn on
		Displayable on = __Queue__.INSTANCE.__getDisplayable(__d);
		if (!(on instanceof Canvas))
			return;
		
		// This will be set to the graphics to draw on
		Graphics g;
		
		// Initialize the graphics to draw into, either directly or indirect
		// via a second copy buffer
		ArrayType buftype;
		boolean local;
		switch ((buftype = __buf.type()))
		{
			case INTEGER:
				int[] ibuf;
				if (local = (__buf instanceof LocalIntegerArray))
					ibuf = ((LocalIntegerArray)__buf).localArray();
				else
					throw new todo.TODO();
				
				g = new IntArrayGraphics(ibuf, __bw, __bh, __alpha,
					__pitch, __offset);
				break;
			
				// {@squirreljme.error EB24 Do not know how to handle the
				// specified pixel type. (The pixel type)}
			default:
				throw new LcdException(String.format("EB24 %s", buftype)); 
		}
		
		// Set the clipping bounds so bytes outside of the area are not drawn
		// into at all
		g.setClip(__cx, __cy, __cw, __ch);
		
		// Perform the actual painting operation
		on.__doRepaint(g);
	}
	
	/**
	 * Specifies that the given displayable has changed size.
	 *
	 * @param __d The displayable which had its size changed.
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2018/03/23
	 */
	private final void __displayableSizeChanged(int __d, int __w, int __h)
	{
		Displayable on = __Queue__.INSTANCE.__getDisplayable(__d);
		if (on == null)
			return;
		
		on.__doResize(__w, __h);
	}
}

