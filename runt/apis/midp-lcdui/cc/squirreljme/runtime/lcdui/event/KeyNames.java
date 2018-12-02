// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import javax.microedition.lcdui.Canvas;

/**
 * This is used to translate key codes into key names.
 *
 * @since 2017/02/12
 */
public final class KeyNames
{
	/**
	 * Not used.
	 *
	 * @since 2017/02/12
	 */
	private KeyNames()
	{
	}
	
	/**
	 * Returns the name of the given key.
	 *
	 * @param __c The keycode to get the name for.
	 * @return The name of the key.
	 * @throws IllegalArgumentException If the key is not valid.
	 * @since 2017/02/12
	 */
	public static String getKeyName(int __c)
		throws IllegalArgumentException
	{
		switch (__c)
		{
				// More friendly names
			case Canvas.KEY_BACKSPACE:	return "Backspace";
			case Canvas.KEY_DELETE:		return "Delete";
			case Canvas.KEY_DOWN:		return "Down";
			case Canvas.KEY_ENTER:		return "Enter";
			case Canvas.KEY_ESCAPE:		return "Escape";
			case Canvas.KEY_LEFT:		return "Left";
			case Canvas.KEY_RIGHT:		return "Right";
			case Canvas.KEY_SELECT:		return "Select";
			case Canvas.KEY_SPACE:		return "Space";
			case Canvas.KEY_TAB:		return "Tab";
			case Canvas.KEY_UP:			return "Up";
			
				// Function keys
			case NonStandardKey.F1:		return "F1";
			case NonStandardKey.F2:		return "F2";
			case NonStandardKey.F3:		return "F3";
			case NonStandardKey.F4:		return "F4";
			case NonStandardKey.F5:		return "F5";
			case NonStandardKey.F6:		return "F6";
			case NonStandardKey.F7:		return "F7";
			case NonStandardKey.F8:		return "F8";
			case NonStandardKey.F9:		return "F9";
			case NonStandardKey.F10:	return "F10";
			case NonStandardKey.F11:	return "F11";
			case NonStandardKey.F12:	return "F12";
			case NonStandardKey.F13:	return "F13";
			case NonStandardKey.F14:	return "F14";
			case NonStandardKey.F15:	return "F15";
			case NonStandardKey.F16:	return "F16";
			case NonStandardKey.F17:	return "F17";
			case NonStandardKey.F18:	return "F18";
			case NonStandardKey.F19:	return "F19";
			case NonStandardKey.F20:	return "F20";
			case NonStandardKey.F21:	return "F21";
			case NonStandardKey.F22:	return "F22";
			case NonStandardKey.F23:	return "F23";
			case NonStandardKey.F24:	return "F24";
			
				// Other keys
			case NonStandardKey.LOGO:	return "LOGO";
			
				// Non-standard game keys (used by the VM perhaps)
			case NonStandardKey.GAME_UP:	return "VirtualGameUp";
			case NonStandardKey.GAME_DOWN:	return "VirtualGameDown";
			case NonStandardKey.GAME_LEFT:	return "VirtualGameLeft";
			case NonStandardKey.GAME_RIGHT:	return "VirtualGameRight";
			case NonStandardKey.GAME_FIRE:	return "VirtualGameFire";
			case NonStandardKey.GAME_A:		return "VirtualGameA";
			case NonStandardKey.GAME_B:		return "VirtualGameB";
			case NonStandardKey.GAME_C:		return "VirtualGameC";
			case NonStandardKey.GAME_D:		return "VirtualGameD";
			
				// Unknown or default
			default:
				// Default ASCII character, use the glyph name
				if (__c >= 0x21 && __c <= 0x7E)
					return Character.valueOf((char)__c).toString();
				
				// {@squirreljme.error EB06 Cannot get the name for the given
				// key code because it is not known. (The key code)}
				throw new IllegalArgumentException(String.format("EB06 %d",
					__c));
		}
	}
}

