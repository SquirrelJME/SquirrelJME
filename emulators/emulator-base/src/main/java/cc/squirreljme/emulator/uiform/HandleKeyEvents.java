// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.jvm.mle.constants.UIKeyEventType;
import cc.squirreljme.jvm.mle.constants.UIKeyModifier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Handles key events and forwards them to the form.
 *
 * @since 2021/02/16
 */
@Deprecated
public class HandleKeyEvents
	extends AbstractListener
	implements KeyListener, ActionListener
{
	/** Which keys are pressed? */
	private final Set<Integer> _pressedKeys =
		new ConcurrentSkipListSet<>();
	
	/**
	 * Initializes the handler for key events.
	 *
	 * @param __item The item used.
	 * @since 2021/02/16
	 */
	public HandleKeyEvents(SwingWidget __item)
	{
		super(__item);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/26
	 */
	@Override
	public void actionPerformed(ActionEvent __ignored)
	{
		// If there is no widget then we cannot perform actions
		SwingWidget widget = this.item();
		if (widget == null)
			return;
		
		// If there is no callback, we cannot send keys to the client
		UIFormCallback callback = widget.callback();
		if (callback == null)
			return;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/16
	 */
	@Override
	public void keyTyped(KeyEvent __e)
	{
		this.__handleKey(UIKeyEventType.KEY_PRESSED, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/16
	 */
	@Override
	public void keyPressed(KeyEvent __e)
	{
		this.__handleKey(UIKeyEventType.KEY_PRESSED, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/16
	 */
	@Override
	public void keyReleased(KeyEvent __e)
	{
		this.__handleKey(UIKeyEventType.KEY_RELEASE, __e);
	}
	
	/**
	 * Handles key handling.
	 * 
	 * @param __type The {@link UIKeyEventType}.
	 * @param __e The event used.
	 * @since 2021/02/16
	 */
	private void __handleKey(int __type, KeyEvent __e)
	{
		SwingWidget widget = this.item();
		if (widget == null)
			return;
		
		UIFormCallback callback = widget.callback();
		if (callback == null)
			return;
		
		// Determine the key code, and if it is valid
		int keyCode = HandleKeyEvents.__keyCode(__e.getKeyCode(),
			__e.getKeyChar());
		if (keyCode == NonStandardKey.UNKNOWN)
			return;
		
		// Determine if this key was already pressed and we are repeating it
		Set<Integer> pressedKeys = this._pressedKeys;
		if (__type == UIKeyEventType.KEY_PRESSED)
		{
			// If not added to the set, it becomes repeated
			if (!pressedKeys.add(keyCode))
				__type = UIKeyEventType.KEY_REPEATED;
		}
		
		// Otherwise for released, remove it
		else if (__type == UIKeyEventType.KEY_RELEASE)
			pressedKeys.remove(keyCode);
		
		// If the left/right command specials are pressed then count them as
		// specifically pressed so we can do middle commands!
		int modifiers = HandleKeyEvents.__keyModifiers(__e.getModifiers());
		if (pressedKeys.contains((int)NonStandardKey.F1))
			modifiers |= UIKeyModifier.MODIFIER_LEFT_COMMAND;
		if (pressedKeys.contains((int)NonStandardKey.F2))
			modifiers |= UIKeyModifier.MODIFIER_RIGHT_COMMAND;
		
		// Send the event
		callback.eventKey((UIItemBracket)widget, __type,
			keyCode, modifiers);
	}
	
	/**
	 * Returns the key code used.
	 * 
	 * @param __keyCode The Swing Key code.
	 * @param __keyChar The key character.
	 * @return The key code or {@link NonStandardKey}.
	 * @since 2021/02/16
	 */
	private static int __keyCode(int __keyCode, char __keyChar)
	{
		// Function keys, these come in two groups
		if (__keyCode >= KeyEvent.VK_F1 && __keyCode <= KeyEvent.VK_F12)
			return NonStandardKey.F1 + (__keyCode - KeyEvent.VK_F1);
		else if (__keyCode >= KeyEvent.VK_F13 && __keyCode <= KeyEvent.VK_F24)
			return NonStandardKey.F13 + (__keyCode - KeyEvent.VK_F13);
		
		// Page control
		if (__keyCode == KeyEvent.VK_PAGE_UP)
			return NonStandardKey.PAGE_UP;
		else if (__keyCode == KeyEvent.VK_PAGE_DOWN)
			return NonStandardKey.PAGE_DOWN;
		else if (__keyCode == KeyEvent.VK_HOME)
			return NonStandardKey.HOME;
		else if (__keyCode == KeyEvent.VK_END)
			return NonStandardKey.END;
		
		// Arrow keys and directions
		if (__keyChar == KeyEvent.VK_UP ||
			__keyChar == KeyEvent.VK_KP_UP)
			return NonStandardKey.KEY_UP;
		else if (__keyChar == KeyEvent.VK_DOWN ||
			__keyChar == KeyEvent.VK_KP_DOWN)
			return NonStandardKey.KEY_DOWN;
		else if (__keyChar == KeyEvent.VK_LEFT ||
			__keyChar == KeyEvent.VK_KP_LEFT)
			return NonStandardKey.KEY_LEFT;
		else if (__keyChar == KeyEvent.VK_RIGHT ||
			__keyChar == KeyEvent.VK_KP_RIGHT)
			return NonStandardKey.KEY_RIGHT;
		
		// Treat numbers and number pad as the same
		if (__keyCode >= KeyEvent.VK_0 && __keyCode <= KeyEvent.VK_9)
			return '0' + (__keyCode - KeyEvent.VK_0);
		else if (__keyCode >= KeyEvent.VK_NUMPAD0 &&
			__keyCode <= KeyEvent.VK_NUMPAD9)
			return '0' + (__keyCode - KeyEvent.VK_NUMPAD0);
		
		// Other keys on the number pad
		if (__keyCode == KeyEvent.VK_ASTERISK ||
			__keyCode == KeyEvent.VK_MULTIPLY)
			return NonStandardKey.KEY_STAR;
		else if (__keyCode == KeyEvent.VK_NUMBER_SIGN ||
			__keyCode == KeyEvent.VK_DIVIDE)
			return NonStandardKey.KEY_POUND;
		else if (__keyCode == KeyEvent.VK_ADD)
			return NonStandardKey.VGAME_COMMAND_LEFT;
		else if (__keyChar == KeyEvent.VK_SUBTRACT)
			return NonStandardKey.VGAME_COMMAND_RIGHT;
		else if (__keyChar == KeyEvent.VK_DECIMAL)
			return NonStandardKey.VGAME_COMMAND_CENTER;
		
		// Use a character based key if this is one
		if (__keyChar != KeyEvent.CHAR_UNDEFINED)
			return __keyChar;
		
		// In the event there is no ASCII
		if (__keyCode >= KeyEvent.VK_A && __keyCode <= KeyEvent.VK_Z)
			return 'A' + (__keyCode - KeyEvent.VK_A);
		
		// Not a known key
		return NonStandardKey.UNKNOWN;
	}
	
	/**
	 * Converts the modifiers that are used.
	 * 
	 * @param __modifiers The swing modifiers.
	 * @return The {@link UIKeyModifier}.
	 * @since 2021/02/16
	 */
	private static int __keyModifiers(int __modifiers)
	{
		int rv = 0;
		
		if (0 != (__modifiers & InputEvent.SHIFT_DOWN_MASK))
			rv |= UIKeyModifier.MODIFIER_SHIFT;
		
		// The CHR key is essentially a function key, however Swing does not
		// offer one so instead use Ctrl+Alt for it.
		if ((__modifiers & (InputEvent.CTRL_DOWN_MASK |
			InputEvent.ALT_DOWN_MASK)) == (InputEvent.CTRL_DOWN_MASK |
			InputEvent.ALT_DOWN_MASK))
			rv |= UIKeyModifier.MODIFIER_FUNCTION;
		else
		{
			if (0 != (__modifiers & InputEvent.CTRL_DOWN_MASK))
				rv |= UIKeyModifier.MODIFIER_CTRL;
			
			if (0 != (__modifiers & InputEvent.ALT_DOWN_MASK))
				rv |= UIKeyModifier.MODIFIER_ALT;
		}
		
		if (0 != (__modifiers & InputEvent.META_DOWN_MASK))
			rv |= UIKeyModifier.MODIFIER_COMMAND;
		
		return rv;
	}
}
