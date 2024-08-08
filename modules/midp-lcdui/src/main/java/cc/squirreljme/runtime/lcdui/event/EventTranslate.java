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
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import javax.microedition.lcdui.Canvas;
import net.multiphasicapps.collections.UnmodifiableArrayList;

/**
 * Used to translate key events and such.
 *
 * @since 2018/12/09
 */
@SquirrelJMEVendorApi
public final class EventTranslate
{
	/** Event translators. */
	private static volatile KeyCodeTranslator[] _TRANSLATORS;
	
	/**
	 * Not used.
	 *
	 * @since 2018/12/09
	 */
	private EventTranslate()
	{
	}
	
	/**
	 * Converts the given game action to a key code.
	 *
	 * @param __gc The game action to convert.
	 * @return The resulting key code or {@code 0} if the game action is not
	 * valid.
	 * @since 2019/04/14
	 */
	@SquirrelJMEVendorApi
	public static int gameActionToKeyCode(int __gc)
	{
		// This performs the reverse of keyCodeToGameAction() except that
		// it maps game keys back to associated number keys.
		// [A ^ B] > [1 2 3]
		// [< F >] > [4 5 6]
		// [C v D] > [7 8 9]
		// [* 0 #] > [* 0 #]
		switch (__gc)
		{
			case Canvas.GAME_A:	return Canvas.KEY_NUM1;
			case Canvas.UP:		return Canvas.KEY_NUM2;
			case Canvas.GAME_B:	return Canvas.KEY_NUM3;
			case Canvas.LEFT:	return Canvas.KEY_NUM4;
			case Canvas.FIRE:	return Canvas.KEY_NUM5;
			case Canvas.RIGHT:	return Canvas.KEY_NUM6;
			case Canvas.GAME_C:	return Canvas.KEY_NUM7;
			case Canvas.DOWN:	return Canvas.KEY_NUM8;
			case Canvas.GAME_D:	return Canvas.KEY_NUM9;
		}
		
		return 0;
	}
	
	/**
	 * Converts the key code to a game action.
	 *
	 * @param __kc The key code.
	 * @return The game action or {@code 0} if it is not valid.
	 * @since 2018/12/09
	 */
	@SuppressWarnings("DuplicateBranchesInSwitch")
	@SquirrelJMEVendorApi
	public static int keyCodeToGameAction(int __kc)
	{
		// Game actions are mapped to physical keys such as left/right/up/down
		// and select. Also since some phones only have a dial pad this means
		// that game actions take up actual digits on the phone itself, so
		// this means we have to map those accordingly.
		// [1 2 3] > [A ^ B]
		// [4 5 6] > [< F >]
		// [7 8 9] > [C v D]
		// [* 0 #] > [* 0 #]
		switch (__kc)
		{
				// Map these to game keys using number pad layout
			case Canvas.KEY_NUM1:				return Canvas.GAME_A;
			case Canvas.KEY_NUM2:				return Canvas.UP;
			case Canvas.KEY_NUM3:				return Canvas.GAME_B;
			case Canvas.KEY_NUM4:				return Canvas.LEFT;
			case Canvas.KEY_NUM5:				return Canvas.FIRE;
			case Canvas.KEY_NUM6:				return Canvas.RIGHT;
			case Canvas.KEY_NUM7:				return Canvas.GAME_C;
			case Canvas.KEY_NUM8:				return Canvas.DOWN;
			case Canvas.KEY_NUM9:				return Canvas.GAME_D;
			
				// Arrow keys map to their direct game keys
			case Canvas.KEY_UP:					return Canvas.UP;
			case Canvas.KEY_DOWN:				return Canvas.DOWN;
			case Canvas.KEY_LEFT:				return Canvas.LEFT;
			case Canvas.KEY_RIGHT:				return Canvas.RIGHT;
			
				// Map space bar and enter to fire
			case ' ':
			case '\n':							return Canvas.FIRE;
			
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
		}
		
		// Check other translators
		for (KeyCodeTranslator adapter : EventTranslate.translators())
		{
			int result = adapter.keyCodeToGameAction(__kc);
			if (result != 0)
				return result;
		}
		
		// Not valid
		return 0;
	}
	
	/**
	 * Returns the event translation adapters which are available.
	 * 
	 * @return The adapters which are available.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	public static Iterable<KeyCodeTranslator> translators()
	{
		// Already cached?
		KeyCodeTranslator[] rv = EventTranslate._TRANSLATORS;
		if (rv != null)
			return UnmodifiableArrayList.of(rv);
		
		// Load them in
		List<KeyCodeTranslator> found = new ArrayList<>();
		for (KeyCodeTranslator adapter :
			ServiceLoader.load(KeyCodeTranslator.class))
			found.add(adapter);
		
		// Cache and use it
		rv = found.toArray(new KeyCodeTranslator[found.size()]);
		EventTranslate._TRANSLATORS = rv;
		return UnmodifiableArrayList.of(rv);
	}
}
