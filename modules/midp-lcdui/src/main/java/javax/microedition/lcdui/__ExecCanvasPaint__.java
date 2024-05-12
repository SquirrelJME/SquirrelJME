// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.lang.ref.WeakReference;

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
	public void paint(ScritchComponentBracket __component,
		ScritchPencilBracket __g,
		int __sw, int __sh, int __special)
	{
		// Do not draw if the canvas was GCed
		Canvas canvas = this._canvas.get();
		if (canvas == null)
			return;
		
		// Forward paint
		canvas.__paint(PencilGraphics.of(__g, __sw, __sh),
			__sw, __sh, __special);
	}
}
