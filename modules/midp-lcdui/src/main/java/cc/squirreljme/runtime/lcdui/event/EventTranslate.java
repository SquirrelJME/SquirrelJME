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
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Used to translate key events between SquirrelJME, Vendor Specific Keys,
 * and Game Keys. This follows the interface defined
 * in {@link KeyCodeTranslator}.
 * 
 * Translators may be provided via {@link ServiceLoader}.
 *
 * @see KeyCodeTranslator
 * @since 2018/12/09
 */
@SquirrelJMEVendorApi
public final class EventTranslate
{
	/** Event translators. */
	private static volatile KeyCodeTranslator[] _TRANSLATORS;
	
	/** The currently selected translator. */
	private static volatile KeyCodeTranslator _translator;
	
	/**
	 * Not used.
	 *
	 * @since 2018/12/09
	 */
	private EventTranslate()
	{
	}
	
	/**
	 * Converts the game action to a vendor key code.
	 *
	 * @param __ga The game action, this may derive values
	 * from {@link Canvas} however it may also have vendor specific value.
	 * @return The game action or {@code 0} if it is not valid. Returning
	 * a value of {@link KeyCodeTranslator#IMMEDIATE_FAIL} will stop all
	 * processing.
	 * @since 2026/05/12
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = NonStandardKey.class)
	public static int gameActionToVendor(int __ga)
	{
		// Check primary translator
		KeyCodeTranslator trans = EventTranslate.translator();
		if (trans != null)
		{
			int result = trans.gameActionToVendor(__ga, false);
			if (result != 0)
				return (result == KeyCodeTranslator.IMMEDIATE_FAIL ?
					0 : result);
		}
		
		// Default MIDP handling, IMMEDIATE_FAIL will skip this
		// This performs the reverse of vendorToGameAction() except that
		// it maps game keys back to associated number keys.
		// [A ^ B] > [1 2 3]
		// [< F >] > [4 5 6]
		// [C v D] > [7 8 9]
		// [* 0 #] > [* 0 #]
		switch (__ga)
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
		
		// Check fallback last translator
		if (trans != null)
		{
			int result = trans.gameActionToVendor(__ga, true);
			if (result != 0)
				return (result == KeyCodeTranslator.IMMEDIATE_FAIL ?
					0 : result);
		}
		
		// No available key
		return 0;
	}
	
	/**
	 * Normalizes the given key code from SquirrelJME to a vendor specific
	 * code.
	 *
	 * @param __kc The key code, this is a SquirrelJME key.
	 * @return The normalized key code or {@code 0} if it is not
	 * normalizable. Returning
	 * a value of {@link KeyCodeTranslator#IMMEDIATE_FAIL} will stop all
	 * processing.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	public static int keyCodeToVendor(
		@MagicConstant(valuesFromClass = NonStandardKey.class) int __kc)
	{
		// Check primary translator
		KeyCodeTranslator trans = EventTranslate.translator();
		if (trans != null)
		{
			int result = trans.keyCodeToVendor(__kc);
			if (result != 0)
				return (result == KeyCodeTranslator.IMMEDIATE_FAIL ?
					0 : result);
		}
		
		// Direct 1:1 translation
		return __kc;
	}
	
	/**
	 * Returns the currently set translator.
	 *
	 * @return The current translator.
	 * @since 2026/05/13
	 */
	@SquirrelJMEVendorApi
	public static KeyCodeTranslator translator()
	{
		synchronized (EventTranslate.class)
		{
			return EventTranslate._translator;
		}
	}
	
	/**
	 * Sets the translator to the specified identifier, if one is supported.
	 *
	 * @param __identifier The identifier for the translator, this is
	 * in the same format
	 * as {@link KeyCodeTranslator#accepts(String, boolean)}.
	 * @return The newly set translator, if any.
	 * @throws NullPointerException On null arguments.
	 * @see KeyCodeTranslator#accepts(String, boolean)
	 * @since 2026/05/13
	 */
	@SquirrelJMEVendorApi
	public static KeyCodeTranslator translator(
		@Language("rfqdn") String __identifier)
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		// Go through translators
		KeyCodeTranslator exact = null;
		KeyCodeTranslator wild = null;
		for (KeyCodeTranslator maybe : EventTranslate.__translators())
		{
			// Is this an exact translator match?
			if (exact == null && maybe.accepts(__identifier, true))
				exact = maybe;
			
			// Is this a wildcard translator match?
			if (wild == null && maybe.accepts(__identifier, false))
				wild = maybe;
		}
		
		// Prefer exact over a wildcard
		KeyCodeTranslator chosen = (exact != null ? exact : wild);
		EventTranslate.translator(chosen);
		return chosen;
	}
	
	/**
	 * Sets the event translator.
	 *
	 * @param __translator The translator to use, may be {@code null} to
	 * clear it.
	 * @since 2026/05/13
	 */
	@SquirrelJMEVendorApi
	public static void translator(KeyCodeTranslator __translator)
	{
		synchronized (EventTranslate.class)
		{
			EventTranslate._translator = __translator;
		}
	}
	
	/**
	 * Sets the translator to the specified identifier if it valid and if
	 * there is no currently translator set.
	 *
	 * @param __identifier The identifier for the translator, this is
	 * in the same format
	 * as {@link KeyCodeTranslator#accepts(String, boolean)}.
	 * @return The newly set translator, if any.
	 * @throws NullPointerException On null arguments.
	 * @see KeyCodeTranslator#accepts(String, boolean)
	 * @see EventTranslate#translator(String)
	 * @since 2026/05/13
	 */
	@SquirrelJMEVendorApi
	public static KeyCodeTranslator translatorDefault(
		@Language("rfqdn") String __identifier)
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		synchronized (EventTranslate.class)
		{
			// If there is no translator set, then set one
			KeyCodeTranslator was = EventTranslate.translator();
			if (was == null)
				return EventTranslate.translator(__identifier);
			
			// Otherwise return the one that was already set
			return was;
		}
	}
	
	/**
	 * Sets the translator to the specific translator if no other translator
	 * has already been set.
	 *
	 * @param __translator The translator to set.
	 * @return The newly set translator, if any.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/05/13
	 */
	@SquirrelJMEVendorApi
	public static KeyCodeTranslator translatorDefault(
		KeyCodeTranslator __translator)
		throws NullPointerException
	{
		if (__translator == null)
			throw new NullPointerException("NARG");
		
		synchronized (EventTranslate.class)
		{
			// If there is no translator set, then set it to one that was
			// passed
			KeyCodeTranslator was = EventTranslate.translator();
			if (was == null)
			{
				EventTranslate.translator(__translator);
				return __translator;
			}
			
			// Otherwise return the one that was already set
			return was;
		}
	}
	
	/**
	 * Converts a vendor specific key code to a vendor specific game action.
	 *
	 * @param __vc The vendor specific key code.
	 * @return The game action or {@code 0} if it is not valid. This may
	 * derive values from {@link Canvas} however it may also have vendor
	 * specific value. Returning
	 * a value of {@link KeyCodeTranslator#IMMEDIATE_FAIL} will stop all
	 * processing.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	public static int vendorToGameAction(int __vc)
	{
		// Check primary translator
		KeyCodeTranslator trans = EventTranslate.translator();
		if (trans != null)
		{
			int result = trans.vendorToGameAction(__vc, false);
			if (result != 0)
				return (result == KeyCodeTranslator.IMMEDIATE_FAIL ?
					0 : result);
		}
		
		// Default MIDP handling, IMMEDIATE_FAIL will skip this
		// Game actions are mapped to physical keys such as left/right/up/down
		// and select. Also since some phones only have a dial pad this means
		// that game actions take up actual digits on the phone itself, so
		// this means we have to map those accordingly.
		// [1 2 3] > [A ^ B]
		// [4 5 6] > [< F >]
		// [7 8 9] > [C v D]
		// [* 0 #] > [* 0 #]
		switch (__vc)
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
			
				// A-D as their corresponding game keys
			case 'a':
			case 'A':							return Canvas.GAME_A;
			case 'b':
			case 'B':							return Canvas.GAME_B;
			case 'c':
			case 'C':							return Canvas.GAME_C;
			case 'd':
			case 'D':							return Canvas.GAME_D;
			
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
		
		// Check fallback last translators
		if (trans != null)
		{
			int result = trans.vendorToGameAction(__vc, true);
			if (result != 0)
				return (result == KeyCodeTranslator.IMMEDIATE_FAIL ?
					0 : result);
		}
		
		// No available key
		return 0;
	}
	
	/**
	 * Converts a vendor specific key to a SquirrelJME key
	 *
	 * @param __vc The vendor specific key code.
	 * @return A SquirrelJME key {@code 0} if it is not valid. Returning
	 * a value of {@link KeyCodeTranslator#IMMEDIATE_FAIL} will stop all
	 * processing.
	 * @since 2026/05/12
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = NonStandardKey.class)
	public static int vendorToKeyCode(int __vc)
	{
		// Check primary translators
		KeyCodeTranslator trans = EventTranslate.translator();
		if (trans != null)
		{
			int result = trans.vendorToKeyCode(__vc);
			if (result != 0)
				return (result == KeyCodeTranslator.IMMEDIATE_FAIL ?
					0 : result);
		}
		
		// Direct 1:1 mapping
		return __vc;
	}
	
	/**
	 * Returns the event translation adapters which are available
	 * through {@link ServiceLoader}.
	 * 
	 * @return The adapters which are available.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	private static KeyCodeTranslator[] __translators()
	{
		// Already cached?
		KeyCodeTranslator[] rv = EventTranslate._TRANSLATORS;
		if (rv != null)
			return rv;
		
		// Load them in
		List<KeyCodeTranslator> found = new ArrayList<>();
		for (KeyCodeTranslator adapter :
			ServiceLoader.load(KeyCodeTranslator.class))
			found.add(adapter);
		
		// Cache and use it
		rv = found.toArray(new KeyCodeTranslator[found.size()]);
		EventTranslate._TRANSLATORS = rv;
		return rv;
	}
}
