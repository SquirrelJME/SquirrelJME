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
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchVisibleListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Callback for when a canvas visibility has changed.
 *
 * @since 2024/06/28
 */
final class __ExecCanvasVisible__
	extends __ExecCanvas__
	implements ScritchVisibleListener
{
	/**
	 * Initializes the callback.
	 *
	 * @param __canvas The canvas to call for.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/28
	 */
	__ExecCanvasVisible__(Canvas __canvas)
		throws NullPointerException
	{
		super(__canvas);
	}
	
	@Override
	public void visibilityChanged(ScritchComponentBracket __component,
		boolean __from, boolean __to)
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		// Ignore if GCed
		Canvas canvas = this._canvas.get();
		if (canvas == null)
			return;
		
		// Call the appropriate handler
		if (__to)
			canvas.showNotify();
		else
			canvas.hideNotify();
	}
}
