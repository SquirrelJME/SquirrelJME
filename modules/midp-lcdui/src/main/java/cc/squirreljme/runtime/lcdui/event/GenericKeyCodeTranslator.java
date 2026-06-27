// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------
package cc.squirreljme.runtime.lcdui.event;

import cc.squirreljme.csv.CsvReader;
import cc.squirreljme.csv.CsvReaderInputStream;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import javax.microedition.lcdui.Canvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class provides a generic translator for most vendor-specific keycode
 * events.
 *
 * This generic translator is not meant to be used as a service, and as such,
 * it is not included in {@code META-INF/services}. Any key translations that
 * do not require special or dynamic device behavior should be handled here
 * whenever possible.
 *
 * @since 2026/05/13
 */
@SquirrelJMEVendorApi
public class GenericKeyCodeTranslator
	implements KeyCodeTranslator
{
	/** Specific vendor event translator system property. */
	@SquirrelJMEVendorApi
	public static final String KEY_VENDOR_PROPERTY =
		"cc.squirreljme.keymap";

	/** Specific vendor event translator environment property. */
	@SquirrelJMEVendorApi
	public static final String KEY_VENDOR_ENV =
		"SQUIRRELJME_VENDOR_KEY";

	/**
	 * Vendor-specific mappings array, the mappings are loaded from a CSV file
	 * that matches the vendor whose keymaps must be translated.
	 */
	private final int[] _vendorKeys;

	/**
	 * This constructor should only be used internally.
	 *
	 * @param __keymap The vendor keymap that this translator will translate
	 * keys from/to.
	 * @throws NullPointerException If {@code __keymap} is {@code null}.
	 * @since 2026/05/22
	 */
	private GenericKeyCodeTranslator(@NotNull int[] __keymap)
		throws NullPointerException
	{
		if (__keymap == null)
			throw new NullPointerException("NARG");

		this._vendorKeys = __keymap;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean accepts(String __identifier, boolean __exact)
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");

		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	@SquirrelJMEVendorApi
	public int gameActionToVendor(int __ga, boolean __last)
	{
		int[] vendorKeys = this._vendorKeys;

		switch (__ga)
		{
			case Canvas.DOWN:
				return vendorKeys[__VendorKeys__.KEYDOWN.ordinal()];

			case Canvas.FIRE:
				return vendorKeys[__VendorKeys__.KEYCENTER.ordinal()];

			case Canvas.GAME_A:
				return vendorKeys[__VendorKeys__.KEYNUM1.ordinal()];

			case Canvas.GAME_B:
				return vendorKeys[__VendorKeys__.KEYNUM3.ordinal()];

			case Canvas.GAME_C:
				return vendorKeys[__VendorKeys__.KEYNUM7.ordinal()];

			case Canvas.GAME_D:
				return vendorKeys[__VendorKeys__.KEYNUM9.ordinal()];

			case Canvas.LEFT:
				return vendorKeys[__VendorKeys__.KEYLEFT.ordinal()];

			case Canvas.RIGHT:
				return vendorKeys[__VendorKeys__.KEYRIGHT.ordinal()];

			case Canvas.UP:
				return vendorKeys[__VendorKeys__.KEYUP.ordinal()];
		}

		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	@SquirrelJMEVendorApi
	public int keyCodeToVendor(int __kc)
	{
		int[] vendorKeys = this._vendorKeys;

		switch (__kc)
		{
			case GenericDefaultKeys.ARROW_DOWN:
			case NonStandardKey.VGAME_DOWN:
				return vendorKeys[__VendorKeys__.KEYDOWN.ordinal()];

			case GenericDefaultKeys.ARROW_LEFT:
			case NonStandardKey.VGAME_LEFT:
				return vendorKeys[__VendorKeys__.KEYLEFT.ordinal()];

			case GenericDefaultKeys.ARROW_RIGHT:
			case NonStandardKey.VGAME_RIGHT:
				return vendorKeys[__VendorKeys__.KEYRIGHT.ordinal()];

			case GenericDefaultKeys.ARROW_UP:
			case NonStandardKey.VGAME_UP:
				return vendorKeys[__VendorKeys__.KEYUP.ordinal()];

				// TODO: We might need a VGAME key for this one
			case GenericDefaultKeys.MENU_BACK:
				return vendorKeys[__VendorKeys__.KEYBACK.ordinal()];

			case GenericDefaultKeys.MENU_ITEM_1:
			case NonStandardKey.VGAME_COMMAND_LEFT:
				return vendorKeys[__VendorKeys__.KEYSOFT1.ordinal()];

			case GenericDefaultKeys.MENU_ITEM_2:
			case NonStandardKey.VGAME_COMMAND_RIGHT:
				return vendorKeys[__VendorKeys__.KEYSOFT2.ordinal()];

			case GenericDefaultKeys.MENU_ITEM_3:
			case NonStandardKey.VGAME_COMMAND_CENTER:
			case ' ':
			case '\n':
				return vendorKeys[__VendorKeys__.KEYCENTER.ordinal()];

				// TODO: We also might need VGAME keys for all these, a few jars
				// don't use action keys for navigation at all, and that will be
				// an issue on jars tailored for keyboard phone layouts.
			case Canvas.KEY_NUM0:
			case NonStandardKey.NUMPAD_0:
				return vendorKeys[__VendorKeys__.KEYNUM0.ordinal()];

			case NonStandardKey.NUMPAD_1:
				return vendorKeys[__VendorKeys__.KEYNUM7.ordinal()];

			case NonStandardKey.NUMPAD_2:
				return vendorKeys[__VendorKeys__.KEYNUM8.ordinal()];

			case NonStandardKey.NUMPAD_3:
				return vendorKeys[__VendorKeys__.KEYNUM9.ordinal()];

			case NonStandardKey.NUMPAD_4:
				return vendorKeys[__VendorKeys__.KEYNUM4.ordinal()];

			case NonStandardKey.NUMPAD_5:
				return vendorKeys[__VendorKeys__.KEYNUM5.ordinal()];

			case NonStandardKey.NUMPAD_6:
				return vendorKeys[__VendorKeys__.KEYNUM6.ordinal()];

			case NonStandardKey.NUMPAD_7:
				return vendorKeys[__VendorKeys__.KEYNUM1.ordinal()];

			case NonStandardKey.NUMPAD_8:
				return vendorKeys[__VendorKeys__.KEYNUM2.ordinal()];

			case NonStandardKey.NUMPAD_9:
				return vendorKeys[__VendorKeys__.KEYNUM3.ordinal()];

			case NonStandardKey.KEY_POUND:
				return vendorKeys[__VendorKeys__.KEYPOUND.ordinal()];

			case NonStandardKey.KEY_STAR:
				return vendorKeys[__VendorKeys__.KEYSTAR.ordinal()];
		}

		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	@SquirrelJMEVendorApi
	public int vendorToGameAction(int __vc, boolean __last)
	{
		int[] vendorKeys = this._vendorKeys;

		// Vendors expose these as physical Key IDs, so do the same here.
		if (__vc == vendorKeys[__VendorKeys__.KEYCENTER.ordinal()])
			return Canvas.FIRE;

		if (__vc == vendorKeys[__VendorKeys__.KEYDOWN.ordinal()])
			return Canvas.DOWN;

		if (__vc == vendorKeys[__VendorKeys__.KEYLEFT.ordinal()])
			return Canvas.LEFT;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM1.ordinal()])
			return Canvas.GAME_A;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM3.ordinal()])
			return Canvas.GAME_B;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM7.ordinal()])
			return Canvas.GAME_C;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM9.ordinal()])
			return Canvas.GAME_D;

		if (__vc == vendorKeys[__VendorKeys__.KEYRIGHT.ordinal()])
			return Canvas.RIGHT;

		if (__vc == vendorKeys[__VendorKeys__.KEYUP.ordinal()])
			return Canvas.UP;

		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	@SquirrelJMEVendorApi
	public int vendorToKeyCode(int __vc)
	{
		int[] vendorKeys = this._vendorKeys;

		// Vendors expose these as physical Key IDs, so do the same here.
		if (__vc == vendorKeys[__VendorKeys__.KEYCENTER.ordinal()])
			return GenericDefaultKeys.MENU_ITEM_3;

		if (__vc == vendorKeys[__VendorKeys__.KEYBACK.ordinal()])
			return GenericDefaultKeys.MENU_BACK;

		if (__vc == vendorKeys[__VendorKeys__.KEYDOWN.ordinal()])
			return GenericDefaultKeys.ARROW_DOWN;

		if (__vc == vendorKeys[__VendorKeys__.KEYLEFT.ordinal()])
			return GenericDefaultKeys.ARROW_DOWN;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM0.ordinal()])
			return NonStandardKey.NUMPAD_0;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM1.ordinal()])
			return NonStandardKey.NUMPAD_7;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM2.ordinal()])
			return NonStandardKey.NUMPAD_8;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM3.ordinal()])
			return NonStandardKey.NUMPAD_9;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM4.ordinal()])
			return NonStandardKey.NUMPAD_4;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM5.ordinal()])
			return NonStandardKey.NUMPAD_5;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM6.ordinal()])
			return NonStandardKey.NUMPAD_6;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM7.ordinal()])
			return NonStandardKey.NUMPAD_1;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM8.ordinal()])
			return NonStandardKey.NUMPAD_2;

		if (__vc == vendorKeys[__VendorKeys__.KEYNUM9.ordinal()])
			return NonStandardKey.NUMPAD_3;

		if (__vc == vendorKeys[__VendorKeys__.KEYPOUND.ordinal()])
			return NonStandardKey.KEY_POUND;

		if (__vc == vendorKeys[__VendorKeys__.KEYRIGHT.ordinal()])
			return GenericDefaultKeys.ARROW_RIGHT;

		if (__vc == vendorKeys[__VendorKeys__.KEYSOFT1.ordinal()])
			return GenericDefaultKeys.MENU_ITEM_1;

		if (__vc == vendorKeys[__VendorKeys__.KEYSOFT2.ordinal()])
			return GenericDefaultKeys.MENU_ITEM_2;

		if (__vc == vendorKeys[__VendorKeys__.KEYSTAR.ordinal()])
			return NonStandardKey.KEY_STAR;

		if (__vc == vendorKeys[__VendorKeys__.KEYUP.ordinal()])
			return GenericDefaultKeys.ARROW_UP;

		return 0;
	}

	/**
	 * Returns the current global {@link GenericKeyCodeTranslator} instance,
	 * it will be initialized as needed.
	 *
	 * @return An initialized {@link GenericKeyCodeTranslator} instance,
	 * or {@code null} if .
	 * @throws IllegalArgumentException If the specified key mapping is
	 * malformed, not valid, or does not exist.
	 * @since 2026/05/22
	 */
	@Nullable
	@SquirrelJMEVendorApi
	public static GenericKeyCodeTranslator instance()
		throws IllegalArgumentException
	{
		String vendorKeycode = System.getProperty(
			GenericKeyCodeTranslator.KEY_VENDOR_PROPERTY);

		if (vendorKeycode == null || vendorKeycode.isEmpty())
			vendorKeycode = RuntimeShelf.systemEnv(
				GenericKeyCodeTranslator.KEY_VENDOR_ENV);

		// No vendor keycode? Return null, as the default mapping will be used.
		if (vendorKeycode == null || vendorKeycode.isEmpty())
			return null;

		// Parse the keys
		return GenericKeyCodeTranslator.load(null, vendorKeycode);
	}
	
	/**
	 * Loads a vendor key mapping from a resource.
	 *
	 * @param __pivot The library of the class to look within for the resource,
	 * if this is {@code null} then this will only load mappings which
	 * are defined within SquirrelJME.
	 * @param __rfqdn The vendor name and device map to load.
	 * @return The translator for the specific keys.
	 * @throws IllegalArgumentException If the specified key mapping is
	 * malformed, not valid, or does not exist.
	 * @throws NullPointerException
	 * @since 2026/06/27
	 */
	@SquirrelJMEVendorApi
	public static GenericKeyCodeTranslator load(Class<?> __pivot,
		String __rfqdn)
		throws IllegalArgumentException, NullPointerException
	{
		if (__rfqdn == null)
			throw new NullPointerException("NARG");
		
		// Parse the mapping
		return new GenericKeyCodeTranslator(
			GenericKeyCodeTranslator.__parse(__pivot, __rfqdn));
	}

	/**
	 * Parse a vendor's key layout, and populate the key map with it.
	 *
	 * Key disposition for vendors may be as follows, although key order does
	 * not matter as long as all key maps are present.
	 *
	 * If a vendor does not have a certain key, or if its keycode for a given
	 * key in the array does not deviate from MIDP's default, its key value 
	 * will be 0 as that will make EventTranslator revert to {@link Canvas}' 
	 * default codes.
	 *
	 * @param __pivot The library of the class to look within for the resource,
	 * if this is {@code null} then this will only load mappings which
	 * are defined within SquirrelJME.
	 * @param __rfqdn The vendor name whose key layout should be parsed.
	 * @return An integer mapping to {@link __VendorKeys__} ordinals containing
	 * the vendor key layout.
	 * @throws IllegalArgumentException If the mapping could not be parsed.
	 * @throws NullPointerException If {@code __rfqdn} is {@code null}.
	 * @since 2026/05/22
	 */
	@NotNull
	private static int[] __parse(Class<?> __pivot, @NotNull String __rfqdn)
		throws IllegalArgumentException, NullPointerException
	{
		if (__rfqdn == null)
			throw new NullPointerException("NARG");
		
		// Fallback pivot?
		if (__pivot == null)
			__pivot = KeyCodeTranslator.class;

		// Key disposition for vendors may be as follows:
		// [Up, Down, Left, Right, Center/Fire/Soft3, Soft1, Soft2,
		// Back/CLR, num0, num1, num2, num3, num4, num5, num6, num7, num8,
		// num9, *, #]
		//
		// Some vendors do have other keys (like some KDDI and SKT devices
		// having multiple COM keys, or Nokia's SEND key) but these
		// are not a concern at the moment. Also, numpad keys are only used
		// by vendor key layouts of keyboard devices that must set
		// themselves to numpad mode for app compatibility, like Nokia's
		// E-Series does, at the cost of not using Canvas' default numpad
		// codes.
		try (InputStream in = __pivot.getResourceAsStream(
			"keymaps/" + __rfqdn + ".csv"))
		{
			if (in == null)
				throw Debugging.oops("Keymap not found");
			
			// Parse values
			int[] keys = new int[__VendorKeys__.values().length];
			try (CsvReader<String[]> reader = new CsvReader<>(
				new KeymapCsvDeserializer(), new CsvReaderInputStream(in)))
			{
				// Process each code
				for (String[] __code : reader.readAll())
				{
					// TODO: Skip the 'haskeyboard' key for now.
					// TODO: Keyboard input still needs more work.
					if (__code[0].toLowerCase().equals("haskeyboard"))
					{
						Debugging.todoNote("Generic keyboard mappings " + 
							"are not implemented yet.");
						continue;
					}
					
					// Parse the code directly
					try
					{
						keys[__VendorKeys__.valueOf(
							__code[0].toUpperCase()).ordinal()] =
							Integer.parseInt(__code[1]);
					}
					
					// Badly formatted
					catch (NoSuchElementException|NumberFormatException __e)
					{
						throw new IllegalArgumentException("FRMT", __e);
					}
				}
			}

			// Use the resultant values
			return keys;
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException("IOIO", __e);
		}
	}
}
