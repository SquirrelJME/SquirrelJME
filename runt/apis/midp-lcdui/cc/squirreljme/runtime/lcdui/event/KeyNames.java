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
			case Canvas.KEY_BACKSPACE:		return "Backspace";
			case Canvas.KEY_DELETE:			return "Delete";
			case Canvas.KEY_DOWN:			return "Down";
			case Canvas.KEY_ENTER:			return "Enter";
			case Canvas.KEY_ESCAPE:			return "Escape";
			case Canvas.KEY_LEFT:			return "Left";
			case Canvas.KEY_RIGHT:			return "Right";
			case Canvas.KEY_SELECT:			return "Select";
			case Canvas.KEY_SPACE:			return "Space";
			case Canvas.KEY_TAB:			return "Tab";
			case Canvas.KEY_UP:				return "Up";
			
				// Other keys
			case NonStandardKey.ALT:		return "Alt";
			case NonStandardKey.CAPSLOCK:	return "CapsLock";
			case NonStandardKey.CONTEXT_MENU:	return "ContextMenu";
			case NonStandardKey.CONTROL:	return "Control";
			case NonStandardKey.END:		return "End";
			case NonStandardKey.HOME:		return "Home";
			case NonStandardKey.INSERT:		return "Insert";
			case NonStandardKey.LOGO:		return "Logo";
			case NonStandardKey.META:		return "Meta";
			case NonStandardKey.NUMLOCK:	return "NumLock";
			case NonStandardKey.PAGE_UP:	return "PageUp";
			case NonStandardKey.PAGE_DOWN:	return "PageDown";
			case NonStandardKey.PAUSE:		return "Pause";
			case NonStandardKey.PRINTSCREEN:return "PrintScreen";
			case NonStandardKey.SCROLLLOCK:	return "ScrollLock";
			case NonStandardKey.SHIFT:		return "Shift";
			
				// Non-standard game keys (used by the VM perhaps)
			case NonStandardKey.VGAME_UP:	return "VirtualGameUp";
			case NonStandardKey.VGAME_DOWN:	return "VirtualGameDown";
			case NonStandardKey.VGAME_LEFT:	return "VirtualGameLeft";
			case NonStandardKey.VGAME_RIGHT:return "VirtualGameRight";
			case NonStandardKey.VGAME_FIRE:	return "VirtualGameFire";
			case NonStandardKey.VGAME_A:	return "VirtualGameA";
			case NonStandardKey.VGAME_B:	return "VirtualGameB";
			case NonStandardKey.VGAME_C:	return "VirtualGameC";
			case NonStandardKey.VGAME_D:	return "VirtualGameD";
			
				// Default character or unknown
			default:
				// Anything space and above just becomes the unicode character 
				if (__c >= ' ')
					return Character.valueOf((char)__c).toString();
				
				// {@squirreljme.error EB06 Cannot get the name for the given
				// key code because it is not known. (The key code)}
				throw new IllegalArgumentException(String.format("EB06 %d",
					__c));
		}
	}
}

