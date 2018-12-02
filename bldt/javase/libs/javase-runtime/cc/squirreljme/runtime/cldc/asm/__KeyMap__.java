// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
import java.awt.event.KeyEvent;
import javax.microedition.lcdui.Canvas;

/**
 * Contains key mapping for Swing.
 *
 * @since 2018/12/01
 */
final class __KeyMap__
{
	/**
	 * Maps the key event to a key.
	 *
	 * @param __e The event to map.
	 * @return The key it should do.
	 * @since 2018/12/01
	 */
	static final int __map(KeyEvent __e)
	{
		int keycode = __e.getExtendedKeyCode();
		switch (keycode)
		{
			case KeyEvent.VK_0:
			case KeyEvent.VK_1:
			case KeyEvent.VK_2:
			case KeyEvent.VK_3:
			case KeyEvent.VK_4:
			case KeyEvent.VK_5:
			case KeyEvent.VK_6:
			case KeyEvent.VK_7:
			case KeyEvent.VK_8:
			case KeyEvent.VK_9:
				return Canvas.KEY_NUM0 + (keycode - KeyEvent.VK_0);
			
			case KeyEvent.VK_NUMPAD0:
			case KeyEvent.VK_NUMPAD1:
			case KeyEvent.VK_NUMPAD2:
			case KeyEvent.VK_NUMPAD3:
			case KeyEvent.VK_NUMPAD4:
			case KeyEvent.VK_NUMPAD5:
			case KeyEvent.VK_NUMPAD6:
			case KeyEvent.VK_NUMPAD7:
			case KeyEvent.VK_NUMPAD8:
			case KeyEvent.VK_NUMPAD9:
				return Canvas.KEY_NUM0 +
					(keycode - KeyEvent.VK_NUMPAD0);

			case KeyEvent.VK_ADD:				return '+';
			case KeyEvent.VK_AMPERSAND:			return '&';
			case KeyEvent.VK_ASTERISK:			return '*';
			case KeyEvent.VK_AT:				return '@';
			case KeyEvent.VK_BACK_QUOTE:		return '`';
			case KeyEvent.VK_BACK_SLASH:		return '\\';
			case KeyEvent.VK_BRACELEFT:			return '{';
			case KeyEvent.VK_BRACERIGHT:		return '}';
			case KeyEvent.VK_CIRCUMFLEX:		return '^';
			case KeyEvent.VK_CLOSE_BRACKET:		return ']';
			case KeyEvent.VK_COLON:				return ':';
			case KeyEvent.VK_COMMA:				return ',';
			case KeyEvent.VK_DECIMAL:			return '.';
			case KeyEvent.VK_DIVIDE:			return '/';
			case KeyEvent.VK_DOLLAR:			return '$';
			case KeyEvent.VK_EQUALS:			return '=';
			case KeyEvent.VK_EURO_SIGN:			return 0x20AC;
			case KeyEvent.VK_EXCLAMATION_MARK:	return '!';
			case KeyEvent.VK_GREATER:			return '>';
			case KeyEvent.VK_LEFT_PARENTHESIS:	return '(';
			case KeyEvent.VK_LESS:				return '<';
			case KeyEvent.VK_MINUS:				return '-';
			case KeyEvent.VK_MULTIPLY:			return '*';
			case KeyEvent.VK_NUMBER_SIGN:		return '#';
			case KeyEvent.VK_OPEN_BRACKET:		return '[';
			case KeyEvent.VK_RIGHT_PARENTHESIS:	return ')';
			case KeyEvent.VK_PERIOD:			return '.';
			case KeyEvent.VK_PLUS:				return '+';
			case KeyEvent.VK_QUOTE:				return '\'';
			case KeyEvent.VK_QUOTEDBL:			return '"';
			case KeyEvent.VK_SEMICOLON:			return ';';
			case KeyEvent.VK_SLASH:				return '/';
			case KeyEvent.VK_SPACE:				return ' ';
			case KeyEvent.VK_SUBTRACT:			return '-';
			case KeyEvent.VK_TAB:				return '\t';
			case KeyEvent.VK_UNDERSCORE:		return '_';

			case KeyEvent.VK_ALT:
				return NonStandardKey.ALT;

			case KeyEvent.VK_BACK_SPACE:
				return Canvas.KEY_BACKSPACE;

			case KeyEvent.VK_CAPS_LOCK:
				return NonStandardKey.CAPSLOCK;

			case KeyEvent.VK_CONTEXT_MENU:
				return NonStandardKey.CONTEXT_MENU;

			case KeyEvent.VK_CONTROL:
				return NonStandardKey.CONTROL;

			case KeyEvent.VK_DELETE:
				return Canvas.KEY_DELETE;

			case KeyEvent.VK_DOWN:
				return Canvas.KEY_DOWN;

			case KeyEvent.VK_END:
				return NonStandardKey.END;

			case KeyEvent.VK_ENTER:
				return Canvas.KEY_ENTER;

			case KeyEvent.VK_ESCAPE:
				return Canvas.KEY_ESCAPE;

			case KeyEvent.VK_HOME:
				return NonStandardKey.HOME;

			case KeyEvent.VK_INSERT:
				return NonStandardKey.INSERT;

			case KeyEvent.VK_KP_DOWN:
				return Canvas.KEY_DOWN;

			case KeyEvent.VK_KP_LEFT:
				return Canvas.KEY_LEFT;

			case KeyEvent.VK_KP_RIGHT:
				return Canvas.KEY_RIGHT;

			case KeyEvent.VK_KP_UP:
				return Canvas.KEY_UP;

			case KeyEvent.VK_LEFT:
				return Canvas.KEY_LEFT;

			case KeyEvent.VK_META:
				return NonStandardKey.META;

			case KeyEvent.VK_NUM_LOCK:
				return NonStandardKey.NUMLOCK;

			case KeyEvent.VK_PAGE_DOWN:
				return NonStandardKey.PAGE_DOWN;

			case KeyEvent.VK_PAGE_UP:
				return NonStandardKey.PAGE_UP;

			case KeyEvent.VK_PAUSE:
				return NonStandardKey.PAUSE;

			case KeyEvent.VK_PRINTSCREEN:
				return NonStandardKey.PRINTSCREEN;

			case KeyEvent.VK_RIGHT:
				return Canvas.KEY_RIGHT;

			case KeyEvent.VK_SCROLL_LOCK:
				return NonStandardKey.SCROLLLOCK;

			case KeyEvent.VK_SHIFT:
				return NonStandardKey.SHIFT;

			case KeyEvent.VK_UP:
				return Canvas.KEY_UP;

			case KeyEvent.VK_WINDOWS:
				return NonStandardKey.LOGO;
			
				// Probably a character
			default:
				// Known key?
				int keychar = __e.getKeyChar();
				if (keychar != KeyEvent.CHAR_UNDEFINED)
					return keychar;
				
				// Handle shift so that letter are done like that
				if (keycode >= KeyEvent.VK_A && keycode <= KeyEvent.VK_Z)
					return (__e.isShiftDown() ? 'A' : 'a') +
						(keycode - KeyEvent.VK_A);
				
				// Unknown
				return NonStandardKey.UNKNOWN;
		}
	}
}

