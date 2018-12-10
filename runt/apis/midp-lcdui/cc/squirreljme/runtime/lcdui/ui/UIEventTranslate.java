// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
import javax.microedition.lcdui.Canvas;

/**
 * Used to translate key events and such.
 *
 * @since 2018/12/09
 */
public final class UIEventTranslate
{
	/**
	 * Not used.
	 *
	 * @since 2018/12/09
	 */
	private UIEventTranslate()
	{
	}
	
	/**
	 * Converts the key code to a game action.
	 *
	 * @param __kc The key code.
	 * @return The game action or {@code 0} if it is not valid.
	 * @since 2018/12/09
	 */
	public static final int keyCodeToGameAction(int __kc)
	{
		switch (__kc)
		{
				// Map these keys to standard keys
			case Canvas.KEY_UP:		return Canvas.UP;
			case Canvas.KEY_DOWN:	return Canvas.DOWN;
			case Canvas.KEY_LEFT:	return Canvas.LEFT;
			case Canvas.KEY_RIGHT:	return Canvas.RIGHT;
			
				// Map these character keys to specific keys
			case ' ':			return Canvas.FIRE;
			case 'a': case 'A':
			case 'h': case 'H':	return Canvas.GAME_A;
			case 's': case 'S':
			case 'j': case 'J':	return Canvas.GAME_B;
			case 'd': case 'D':
			case 'k': case 'K':	return Canvas.GAME_C;
			case 'f': case 'F':
			case 'l': case 'L':	return Canvas.GAME_D;
			
				// Virtually mapped game keys, likely from a VM running on top
			case NonStandardKey.VGAME_UP:		return Canvas.UP;
			case NonStandardKey.VGAME_DOWN:		return Canvas.DOWN;
			case NonStandardKey.VGAME_LEFT:		return Canvas.LEFT;
			case NonStandardKey.VGAME_RIGHT:	return Canvas.RIGHT;
			case NonStandardKey.VGAME_FIRE:		return Canvas.FIRE;
			case NonStandardKey.VGAME_A:		return Canvas.GAME_A;
			case NonStandardKey.VGAME_B:		return Canvas.GAME_B;
			case NonStandardKey.VGAME_C:		return Canvas.GAME_C;
			case NonStandardKey.VGAME_D:		return Canvas.GAME_D;
			
				// Invalid
			default:
				return 0;
		}
	}
}

