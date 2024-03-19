// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.lang.ref.WeakReference;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Paints on a canvas.
 *
 * @since 2024/03/19
 */
class __ExecCanvasPaint__
	implements ScritchPaintListener
{
	/** The canvas to paint on. */
	protected final WeakReference<Canvas> _canvas;
	
	/**
	 * Initializes the painter.
	 *
	 * @param __canvas The canvas to paint on.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/19
	 */
	__ExecCanvasPaint__(Canvas __canvas)
		throws NullPointerException
	{
		if (__canvas == null)
			throw new NullPointerException("NARG");
		
		this._canvas = new WeakReference<>(__canvas);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/19
	 */
	@Override
	public void paint(@NotNull ScritchComponentBracket __component,
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Nullable int[] __pal,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sx,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh,
		int __special)
	{
		// Debug
		Debugging.debugNote("paint() A");
			
		// Do not draw if the canvas was GCed
		Canvas canvas = this._canvas.get();
		if (canvas == null)
			return;
		
		// Debug
		Debugging.debugNote("paint() B");
		
		// Try to use hardware accelerated graphics where possible
		Graphics gfx = PencilGraphics.hardwareGraphics(__pf,
			__bw, __bh, __buf, __offset, __pal, __sx, __sy,
			__sw, __sh);
		
		// Forward paint
		canvas.__paint(gfx, __sw, __sh, __special);
	}
}
