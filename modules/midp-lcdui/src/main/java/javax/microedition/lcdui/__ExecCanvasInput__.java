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
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchInputMethodType;
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
	public void inputEvent(ScritchComponentBracket __component,
		int __type, long __time, int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h, int __i, int __j, int __k, int __l)
	{
		Canvas canvas = this._canvas.get();
		if (canvas == null)
			return;
		
		KeyListener keyDefault = canvas.__defaultKeyListener();
		KeyListener keyCustom = canvas._keyListener;
		
		// Debug
		/*
		Debugging.debugNote("Event %d %d %d %d %d %d %d %d %d %d %d %d " +
			"%d %d",
			__type, __time, __a, __b, __c, __d, __e, __f, __g, __h, __i, __j,
			__k, __l);*/
		
		// Depends on the actual event that occurred
		switch (__type)
		{
			case ScritchInputMethodType.KEY_PRESSED:
				keyDefault.keyPressed(__a, __b);
				if (keyCustom != null)
					keyCustom.keyPressed(__a, __b);
				break;
				
			case ScritchInputMethodType.KEY_RELEASED:
				keyDefault.keyReleased(__a, __b);
				if (keyCustom != null)
					keyCustom.keyReleased(__a, __b);
				break;
				
			case ScritchInputMethodType.MOUSE_MOTION:
				// Only care for the first mouse button
				if ((__a & 2) != 0)
					canvas.pointerDragged(__c, __d);
				break;
				
			case ScritchInputMethodType.MOUSE_BUTTON_PRESSED:
				// Only care for the first mouse button
				if (__a == 1)
					canvas.pointerPressed(__c, __d);
				break;
				
			case ScritchInputMethodType.MOUSE_BUTTON_RELEASED:
				// Only care for the first mouse button
				if (__a == 1)
					canvas.pointerReleased(__c, __d);
				break;
		}
	}
}
