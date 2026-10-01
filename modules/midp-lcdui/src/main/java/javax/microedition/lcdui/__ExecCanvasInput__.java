// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchInputMethodType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SpecificFlags;
import cc.squirreljme.runtime.lcdui.event.EventTranslate;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.extra.ExtraDisplayable;
import cc.squirreljme.runtime.lcdui.scritchui.extra.ExtraGameKeys;
import cc.squirreljme.runtime.lcdui.scritchui.extra.ExtraStateManager;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Input event handler for canvases.
 *
 * @since 2024/06/30
 */
class __ExecCanvasInput__
	extends __ExecCanvas__
	implements ScritchInputListener
{
	/** The cached game key reference. */
	private volatile Reference<ExtraGameKeys> _gameKeys;
	
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
		
		// Ignore canvases which are not shown
		DisplayableState state = canvas.__state();
		DisplayState display = state.currentDisplay();
		if (display == null)
			return;
		
		KeyListener keyDefault = canvas.__defaultKeyListener();
		KeyListener keyCustom = canvas._keyListener;
		
		// Debug
		if (Debugging.VERBOSE)
			Debugging.debugNote(
				"Event %d %d %d %d %d %d %d %d %d %d %d %d %d %d",
				__type, __time, __a, __b, __c, __d, __e, __f, __g, __h,
				__i, __j, __k, __l);
		
		// Remap some special keys in the event a system is not capable of
		// typing such keys
		if (__type == ScritchInputMethodType.KEY_PRESSED ||
			__type == ScritchInputMethodType.KEY_RELEASED ||
			__type == ScritchInputMethodType.KEY_REPEATED)
			__a = this.__remapDialCalc(display, __a);
		
		// Perform vendor key translation, if it maps to no key then ignore it
		// as the vendor compatibility layer says it does not have it
		int vc = EventTranslate.keyCodeToVendor(__a);
		if (vc == 0)
			return;
		
		// Depends on the actual event that occurred
		DisplayScale scale = display.display()._scale;
		switch (__type)
		{
			case ScritchInputMethodType.KEY_PRESSED:
			case ScritchInputMethodType.KEY_RELEASED:
			case ScritchInputMethodType.KEY_REPEATED:
				this.__keyEvent(__type, vc, __b, keyDefault, keyCustom,
					canvas);
				break;
				
			case ScritchInputMethodType.MOUSE_MOTION:
				// Only care for the first mouse button
				if ((__a & 1) != 0)
					canvas.pointerDragged(
						scale.textureX(__c),
						scale.textureY(__d));
				break;
				
			case ScritchInputMethodType.MOUSE_BUTTON_PRESSED:
				// Only care for the first mouse button
				if (__e == 1)
					canvas.pointerPressed(
						scale.textureX(__c),
						scale.textureY(__d));
				break;
				
			case ScritchInputMethodType.MOUSE_BUTTON_RELEASED:
				// Only care for the first mouse button
				if (__e == 1)
					canvas.pointerReleased(
						scale.textureX(__c),
						scale.textureY(__d));
				break;
		}
	}
	
	/**
	 * Returns the game key state.
	 *
	 * @return The game key state, {@code null} if there is no state.
	 * @since 2026/09/25
	 */
	private ExtraGameKeys __gameKeys()
	{
		// Canvas is no longer valid or not a GameCanvas?
		Canvas canvas = this._canvas.get();
		if (canvas == null || !(canvas instanceof GameCanvas))
			return null;
		
		// Is a cached reference available?
		Reference<ExtraGameKeys> gameKeys = this._gameKeys;
		if (gameKeys != null)
		{
			ExtraGameKeys rv = gameKeys.get();
			if (rv != null)
				return rv;
		}
		
		// Locate the game keys, assuming there is valid state
		ExtraGameKeys rv = ExtraStateManager.locate(
			ExtraGameKeys.class, this._canvas);
		if (rv != null)
			this._gameKeys = new WeakReference<>(rv);
		
		// Return whatever was found
		return rv;
	}
	
	/**
	 * Handles a generic key event.
	 *
	 * @param __type The event type.
	 * @param __vc The virtual key code.
	 * @param __rc The raw key code.
	 * @param __keyDefault The default key handler.
	 * @param __keyCustom The custom key handler.
	 * @param __canvas The canvas this is being emitted on.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/09/25
	 */
	private void __keyEvent(int __type, int __vc, int __rc,
		KeyListener __keyDefault, KeyListener __keyCustom, Canvas __canvas)
		throws NullPointerException
	{
		if (__keyDefault == null || __canvas == null)
			throw new NullPointerException("NARG");
		
		// Only handle game keys if they are not being suppressed
		// This only cares about when the keys are actually pressed and not
		// released, so provided the key is pressed at least once between
		// the calls to trigger the latch it will return the keys as being set
		ExtraGameKeys gameKeys = this.__gameKeys();
		int flags = DisplayableState.locate(__canvas).flags();
		if ((__type == ScritchInputMethodType.KEY_PRESSED ||
			__type == ScritchInputMethodType.KEY_REPEATED) &&
			gameKeys != null &&
			(flags & SpecificFlags.CANVAS_SUPPRESS_GAME_KEY) == 0)
		{
			// Map a virtual key to a game key, only handle the first 32 game
			// keys as that is the current theoretical limit of LCDUI
			// Note that zero is not a valid key
			int gameKey = __canvas.getGameAction(__vc);
			if (gameKey > 0 && gameKey < 32)
			{
				// Change to a mask as it will be raised
				gameKey = 1 << gameKey;
				
				// Raise the latch on the bits
				gameKeys.raise(gameKey);
			}
		}
		
		// Now handle the event.
		switch (__type)
		{
			case ScritchInputMethodType.KEY_PRESSED:
				__keyDefault.keyPressed(__vc, __rc);
				if (__keyCustom != null)
					__keyCustom.keyPressed(__vc, __rc);
				break;
			
			case ScritchInputMethodType.KEY_RELEASED:
				__keyDefault.keyReleased(__vc, __rc);
				if (__keyCustom != null)
					__keyCustom.keyReleased(__vc, __rc);
				break;
			
			case ScritchInputMethodType.KEY_REPEATED:
				__keyDefault.keyRepeated(__vc, __rc);
				if (__keyCustom != null)
					__keyCustom.keyRepeated(__vc, __rc);
				break;
		}
	}
	
	/**
	 * Remaps the given key, handling the number pad, dial pad, and
	 * calculator layouts.
	 *
	 * @param __display The display this is under.
	 * @param __code The code to remap.
	 * @return The remapped code.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/15
	 */
	private int __remapDialCalc(DisplayState __display, int __code)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		boolean calcLayout = __display.isCalcLayout();
		switch (__code)
		{
			case NonStandardKey.NUMPAD_MULTIPLY:
			case NonStandardKey.NUMPAD_PLUS:
				return '*';
				
			case NonStandardKey.NUMPAD_DIVIDE:
			case NonStandardKey.NUMPAD_MINUS:
				return '#';
				
			case NonStandardKey.NUMPAD_0:
				return '0';
				
			case NonStandardKey.NUMPAD_1:
				if (calcLayout)
					return '7';
				return '1';
				
			case NonStandardKey.NUMPAD_2:
				if (calcLayout)
					return '8';
				return '2';
				
			case NonStandardKey.NUMPAD_3:
				if (calcLayout)
					return '9';
				return '3';
				
			case NonStandardKey.NUMPAD_4:
				return '4';
				
			case NonStandardKey.NUMPAD_5:
				return '5';
				
			case NonStandardKey.NUMPAD_6:
				return '6';
				
			case NonStandardKey.NUMPAD_7:
				if (calcLayout)
					return '1';
				return '7';
				
			case NonStandardKey.NUMPAD_8:
				if (calcLayout)
					return '2';
				return '8';
				
			case NonStandardKey.NUMPAD_9:
				if (calcLayout)
					return '3';
				return '9';
				
			case NonStandardKey.NUMPAD_ENTER:
				return '\n';
				
			case NonStandardKey.NUMPAD_DECIMAL:
				return '.';		
		}
		
		// Untouched
		return __code;
	}
}
