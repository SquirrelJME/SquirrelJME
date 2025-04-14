// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Canvas;

/**
 * This is used to translate key codes into key names.
 *
 * @since 2017/02/12
 */
@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public static String getKeyName(int __c)
		throws IllegalArgumentException
	{
		switch (__c)
		{
				// Unknown
			case NonStandardKey.UNKNOWN:	return "Unknown";
			
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
			
				// Dial keys
			case Canvas.KEY_STAR:			return "Star";
			case Canvas.KEY_POUND:			return "Pound";
			
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
			
				// Function keys
			case NonStandardKey.F1:			return "F1";
			case NonStandardKey.F2:			return "F2";
			case NonStandardKey.F3:			return "F3";
			case NonStandardKey.F4:			return "F4";
			case NonStandardKey.F5:			return "F5";
			case NonStandardKey.F6:			return "F6";
			case NonStandardKey.F7:			return "F7";
			case NonStandardKey.F8:			return "F8";
			case NonStandardKey.F9:			return "F9";
			case NonStandardKey.F10:		return "F10";
			case NonStandardKey.F11:		return "F11";
			case NonStandardKey.F12:		return "F12";
			case NonStandardKey.F13:		return "F13";
			case NonStandardKey.F14:		return "F14";
			case NonStandardKey.F15:		return "F15";
			case NonStandardKey.F16:		return "F16";
			case NonStandardKey.F17:		return "F17";
			case NonStandardKey.F18:		return "F18";
			case NonStandardKey.F19:		return "F19";
			case NonStandardKey.F20:		return "F20";
			case NonStandardKey.F21:		return "F21";
			case NonStandardKey.F22:		return "F22";
			case NonStandardKey.F23:		return "F23";
			case NonStandardKey.F24:		return "F24";
			
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
				// These are purely valid characters
				if (__c > 0)
					return Character.valueOf((char)__c).toString();
				
				/* {@squirreljme.error EB06 Cannot get the name for the given
				key code because it is not known. (The key code)} */
				throw new IllegalArgumentException(String.format("EB06 %d",
					__c));
		}
	}
}

