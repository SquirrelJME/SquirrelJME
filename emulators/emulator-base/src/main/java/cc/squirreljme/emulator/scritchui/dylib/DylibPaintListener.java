// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Wrapped listener for paints.
 *
 * @since 2024/04/24
 */
public class DylibPaintListener
	implements ScritchPaintListener
{
	/** The listener to forward to. */
	protected final ScritchPaintListener listener;
	
	/**
	 * Initializes the wrapped listener.
	 *
	 * @param __listener The listener to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/24
	 */
	public DylibPaintListener(ScritchPaintListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		this.listener = __listener;
	}
	
	/**
	 * Callback that is used to draw the given component.
	 *
	 * @param __component The component to draw on.
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __offset The offset to the start of the buffer.
	 * @param __pal The color palette, may be {@code null}.
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @param __special Special value for painting, may be {@code 0} or any
	 * other value if it is meaningful to what is being painted.
	 * @since 2024/04/24
	 */
	public void paint(ScritchComponentBracket __component,
		int __pf, int __bw, int __bh,
		ByteBuffer __buf, int __offset,
		ByteBuffer __pal,
		int __sx, int __sy, int __sw, int __sh,
		int __special)
	{
		// Make sure the buffer is always in native order
		__buf.order(ByteOrder.nativeOrder());
		if (__pal != null)
			__pal.order(ByteOrder.nativeOrder());
		
		// Map array to base type
		Buffer mappedBuf, arrayBuf;
		Supplier<Buffer> makeArrayBuf;
		switch (__pf)
		{
			case UIPixelFormat.INT_RGBA8888:
			case UIPixelFormat.INT_RGB888:
				mappedBuf = __buf.asIntBuffer();
				makeArrayBuf = () -> IntBuffer.wrap(
					new int[__buf.limit() / 4]);
				break;
			
			case UIPixelFormat.SHORT_RGBA4444:
			case UIPixelFormat.SHORT_RGB565:
			case UIPixelFormat.SHORT_RGB555:
			case UIPixelFormat.SHORT_ABGR1555:
			case UIPixelFormat.SHORT_INDEXED65536:
				mappedBuf = __buf.asShortBuffer();
				makeArrayBuf = () -> IntBuffer.wrap(
					new int[__buf.limit() / 2]);
				break;
				
			case UIPixelFormat.BYTE_INDEXED256:
			case UIPixelFormat.PACKED_INDEXED4:
			case UIPixelFormat.PACKED_INDEXED2:
			case UIPixelFormat.PACKED_INDEXED1:
				mappedBuf = __buf;
				makeArrayBuf = null;
				break;
				
			default:
				throw new IllegalArgumentException("Invalid pf: " + __pf);
		}
		
		// We cannot directly access the array, so do we need to clone it?
		if (mappedBuf.hasArray() || makeArrayBuf == null)
			arrayBuf = mappedBuf;
		else
		{
			
		}
		
		try
		{
			// Forward
			this.listener.paint(__component, __pf, __bw, __bh,
				arrayBuf.array(), __offset,
				(__pal != null ? __pal.asIntBuffer().array() : null),
				__sx, __sy, __sw, __sh, __special);
		}
		
		// Copy data back to the buffer
		finally
		{
			if (!mappedBuf.hasArray() && makeArrayBuf != null)
			{
				// Rewind back to the start
				mappedBuf.rewind();
				
				// Copy everything in from the old buffer
				mappedBuf.rewind();
				
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/24
	 */
	@Override
	public void paint(@NotNull ScritchComponentBracket __component, int __pf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Nullable int[] __pal,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sx,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh, int __special)
	{
		this.listener.paint(__component, __pf, __bw, __bh, __buf,
			__offset, __pal, __sx, __sy, __sw, __sh, __special);
	}
}
