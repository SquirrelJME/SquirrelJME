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
		int keycode = __e.getKeyCode();
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

			case KeyEvent.VK_F1:
			case KeyEvent.VK_F2:
			case KeyEvent.VK_F3:
			case KeyEvent.VK_F4:
			case KeyEvent.VK_F5:
			case KeyEvent.VK_F6:
			case KeyEvent.VK_F7:
			case KeyEvent.VK_F8:
			case KeyEvent.VK_F9:
			case KeyEvent.VK_F10:
			case KeyEvent.VK_F11:
			case KeyEvent.VK_F12:
			case KeyEvent.VK_F13:
			case KeyEvent.VK_F14:
			case KeyEvent.VK_F15:
			case KeyEvent.VK_F16:
			case KeyEvent.VK_F17:
			case KeyEvent.VK_F18:
			case KeyEvent.VK_F19:
			case KeyEvent.VK_F20:
			case KeyEvent.VK_F21:
			case KeyEvent.VK_F22:
			case KeyEvent.VK_F23:
			case KeyEvent.VK_F24:
				return NonStandardKey.F1 +
					(keycode - KeyEvent.VK_F1);

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
				return NonStandardKey.NUMPAD0 +
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

			case KeyEvent.VK_ACCEPT:
				return NonStandardKey.ACCEPT;

			case KeyEvent.VK_AGAIN:
				return NonStandardKey.AGAIN;

			case KeyEvent.VK_ALL_CANDIDATES:
				return NonStandardKey.ALL_CANDIDATES;

			case KeyEvent.VK_ALPHANUMERIC:
				return NonStandardKey.ALPHANUMERIC;

			case KeyEvent.VK_ALT:
				return NonStandardKey.ALT;

			case KeyEvent.VK_ALT_GRAPH:
				return NonStandardKey.ALT_GRAPH;

			case KeyEvent.VK_BACK_SPACE:
				return Canvas.KEY_BACKSPACE;

			case KeyEvent.VK_BEGIN:
				return NonStandardKey.BEGIN;

			case KeyEvent.VK_CANCEL:
				return NonStandardKey.CANCEL;

			case KeyEvent.VK_CAPS_LOCK:
				return NonStandardKey.CAPS_LOCK;

			case KeyEvent.VK_CLEAR:
				return NonStandardKey.CLEAR;

			case KeyEvent.VK_CODE_INPUT:
				return NonStandardKey.CODE_INPUT;

			case KeyEvent.VK_COMPOSE:
				return NonStandardKey.COMPOSE;

			case KeyEvent.VK_CONTEXT_MENU:
				return NonStandardKey.CONTEXT_MENU;

			case KeyEvent.VK_CONTROL:
				return NonStandardKey.CONTROL;

			case KeyEvent.VK_CONVERT:
				return NonStandardKey.CONVERT;

			case KeyEvent.VK_COPY:
				return NonStandardKey.COPY;

			case KeyEvent.VK_CUT:
				return NonStandardKey.CUT;

			case KeyEvent.VK_DEAD_ABOVEDOT:
				return NonStandardKey.DEAD_ABOVEDOT;

			case KeyEvent.VK_DEAD_ABOVERING:
				return NonStandardKey.DEAD_ABOVERING;

			case KeyEvent.VK_DEAD_ACUTE:
				return NonStandardKey.DEAD_ACUTE;

			case KeyEvent.VK_DEAD_BREVE:
				return NonStandardKey.DEAD_BREVE;

			case KeyEvent.VK_DEAD_CARON:
				return NonStandardKey.DEAD_CARON;

			case KeyEvent.VK_DEAD_CEDILLA:
				return NonStandardKey.DEAD_CEDILLA;

			case KeyEvent.VK_DEAD_CIRCUMFLEX:
				return NonStandardKey.DEAD_CIRCUMFLEX;

			case KeyEvent.VK_DEAD_DIAERESIS:
				return NonStandardKey.DEAD_DIAERESIS;

			case KeyEvent.VK_DEAD_DOUBLEACUTE:
				return NonStandardKey.DEAD_DOUBLEACUTE;

			case KeyEvent.VK_DEAD_GRAVE:
				return NonStandardKey.DEAD_GRAVE;

			case KeyEvent.VK_DEAD_IOTA:
				return NonStandardKey.DEAD_IOTA;

			case KeyEvent.VK_DEAD_MACRON:
				return NonStandardKey.DEAD_MACRON;

			case KeyEvent.VK_DEAD_OGONEK:
				return NonStandardKey.DEAD_OGONEK;

			case KeyEvent.VK_DEAD_SEMIVOICED_SOUND:
				return NonStandardKey.DEAD_SEMIVOICED_SOUND;

			case KeyEvent.VK_DEAD_TILDE:
				return NonStandardKey.DEAD_TILDE;

			case KeyEvent.VK_DEAD_VOICED_SOUND:
				return NonStandardKey.DEAD_VOICED_SOUND;

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

			case KeyEvent.VK_FINAL:
				return NonStandardKey.FINAL;

			case KeyEvent.VK_FIND:
				return NonStandardKey.FIND;

			case KeyEvent.VK_FULL_WIDTH:
				return NonStandardKey.FULL_WIDTH;

			case KeyEvent.VK_HALF_WIDTH:
				return NonStandardKey.HALF_WIDTH;

			case KeyEvent.VK_HELP:
				return NonStandardKey.HELP;

			case KeyEvent.VK_HIRAGANA:
				return NonStandardKey.HIRAGANA;

			case KeyEvent.VK_HOME:
				return NonStandardKey.HOME;

			case KeyEvent.VK_INPUT_METHOD_ON_OFF:
				return NonStandardKey.INPUT_METHOD_ON_OFF;
				
			case KeyEvent.VK_INSERT:
				return NonStandardKey.INSERT;

			case KeyEvent.VK_INVERTED_EXCLAMATION_MARK:
				return NonStandardKey.INVERTED_EXCLAMATION_MARK;

			case KeyEvent.VK_JAPANESE_HIRAGANA:
				return NonStandardKey.JAPANESE_HIRAGANA;

			case KeyEvent.VK_JAPANESE_KATAKANA:
				return NonStandardKey.JAPANESE_KATAKANA;

			case KeyEvent.VK_JAPANESE_ROMAN:
				return NonStandardKey.JAPANESE_ROMAN;

			case KeyEvent.VK_KANA:
				return NonStandardKey.KANA;

			case KeyEvent.VK_KANA_LOCK:
				return NonStandardKey.KANA_LOCK;

			case KeyEvent.VK_KANJI:
				return NonStandardKey.KANJI;

			case KeyEvent.VK_KATAKANA:
				return NonStandardKey.KATAKANA;

			case KeyEvent.VK_KP_DOWN:
				return NonStandardKey.KP_DOWN;

			case KeyEvent.VK_KP_LEFT:
				return NonStandardKey.KP_LEFT;

			case KeyEvent.VK_KP_RIGHT:
				return NonStandardKey.KP_RIGHT;

			case KeyEvent.VK_KP_UP:
				return NonStandardKey.KP_UP;

			case KeyEvent.VK_LEFT:
				return Canvas.KEY_LEFT;

			case KeyEvent.VK_META:
				return NonStandardKey.META;

			case KeyEvent.VK_MODECHANGE:
				return NonStandardKey.MODECHANGE;

			case KeyEvent.VK_NONCONVERT:
				return NonStandardKey.NONCONVERT;

			case KeyEvent.VK_NUM_LOCK:
				return NonStandardKey.NUM_LOCK;

			case KeyEvent.VK_PAGE_DOWN:
				return NonStandardKey.PAGE_DOWN;

			case KeyEvent.VK_PAGE_UP:
				return NonStandardKey.PAGE_UP;

			case KeyEvent.VK_PASTE:
				return NonStandardKey.PASTE;

			case KeyEvent.VK_PAUSE:
				return NonStandardKey.PAUSE;

			case KeyEvent.VK_PREVIOUS_CANDIDATE:
				return NonStandardKey.PREVIOUS_CANDIDATE;
				
			case KeyEvent.VK_PRINTSCREEN:
				return NonStandardKey.PRINTSCREEN;

			case KeyEvent.VK_PROPS:
				return NonStandardKey.PROPS;

			case KeyEvent.VK_RIGHT:
				return Canvas.KEY_RIGHT;

			case KeyEvent.VK_RIGHT_PARENTHESIS:
				return NonStandardKey.RIGHT_PARENTHESIS;

			case KeyEvent.VK_ROMAN_CHARACTERS:
				return NonStandardKey.ROMAN_CHARACTERS;

			case KeyEvent.VK_SCROLL_LOCK:
				return NonStandardKey.SCROLL_LOCK;

			case KeyEvent.VK_SEPARATOR:
				return NonStandardKey.SEPARATOR;

			case KeyEvent.VK_SHIFT:
				return NonStandardKey.SHIFT;

			case KeyEvent.VK_STOP:
				return NonStandardKey.STOP;

			case KeyEvent.VK_UNDO:
				return NonStandardKey.UNDO;

			case KeyEvent.VK_UP:
				return Canvas.KEY_UP;

			case KeyEvent.VK_WINDOWS:
				return NonStandardKey.LOGO;
			
				// Probably a character
			default:
				// Handle shift so that letter are done like that
				if (keycode >= KeyEvent.VK_A && keycode <= KeyEvent.VK_Z)
					return (__e.isShiftDown() ? 'A' : 'a') +
						(keycode - KeyEvent.VK_A);
				
				// Unknown
				return NonStandardKey.UNKNOWN;
		}
	}
}

