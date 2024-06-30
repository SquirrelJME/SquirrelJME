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
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * Input event handler for canvases.
 *
 * @since 2024/06/30
 */
class __ExecCanvasInput__
	extends __ExecCanvas__
	implements ScritchInputListener
{
	/**
	 * Initializes the listener.
	 *
	 * @param __canvas The canvas to handle events for.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/30
	 */
	__ExecCanvasInput__(Canvas __canvas)
		throws NullPointerException
	{
		super(__canvas);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/30
	 */
	@Override
	public void inputEvent(@NotNull ScritchComponentBracket __component,
		int __type, long __time, int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h, int __i, int __j, int __k, int __l)
	{
		throw Debugging.todo();
	}
}
