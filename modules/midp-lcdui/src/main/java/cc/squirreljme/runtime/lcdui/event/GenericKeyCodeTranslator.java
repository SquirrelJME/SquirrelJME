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
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
	private final EnumTypeMap<__VendorKeys__, Integer> _vendorKeys;

	/**
	 * This constructor should only be used internally.
	 *
	 * @param __keymap The vendor keymap that this translator will translate
	 * keys from/to.
	 * @throws NullPointerException If {@code __keymap} is {@code null}.
	 * @since 2026/05/22
	 */
	private GenericKeyCodeTranslator(
		@NotNull EnumTypeMap<__VendorKeys__, Integer> __keymap)
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
		EnumTypeMap<__VendorKeys__, Integer> vendorKeys = this._vendorKeys;

		switch (__ga)
		{
			case Canvas.DOWN:
				return vendorKeys.get(__VendorKeys__.KEYDOWN);

			case Canvas.FIRE:
				return vendorKeys.get(__VendorKeys__.KEYCENTER);

			case Canvas.GAME_A:
				return vendorKeys.get(__VendorKeys__.KEYNUM1);

			case Canvas.GAME_B:
				return vendorKeys.get(__VendorKeys__.KEYNUM3);

			case Canvas.GAME_C:
				return vendorKeys.get(__VendorKeys__.KEYNUM7);

			case Canvas.GAME_D:
				return vendorKeys.get(__VendorKeys__.KEYNUM9);

			case Canvas.LEFT:
				return vendorKeys.get(__VendorKeys__.KEYLEFT);

			case Canvas.RIGHT:
				return vendorKeys.get(__VendorKeys__.KEYRIGHT);

			case Canvas.UP:
				return vendorKeys.get(__VendorKeys__.KEYUP);
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
		EnumTypeMap<__VendorKeys__, Integer> vendorKeys = this._vendorKeys;

		switch (__kc)
		{
			case GenericDefaultKeys.ARROW_DOWN:
			case NonStandardKey.VGAME_DOWN:
				return vendorKeys.get(__VendorKeys__.KEYDOWN);

			case GenericDefaultKeys.ARROW_LEFT:
			case NonStandardKey.VGAME_LEFT:
				return vendorKeys.get(__VendorKeys__.KEYLEFT);

			case GenericDefaultKeys.ARROW_RIGHT:
			case NonStandardKey.VGAME_RIGHT:
				return vendorKeys.get(__VendorKeys__.KEYRIGHT);

			case GenericDefaultKeys.ARROW_UP:
			case NonStandardKey.VGAME_UP:
				return vendorKeys.get(__VendorKeys__.KEYUP);

				// TODO: We might need a VGAME key for this one
			case GenericDefaultKeys.MENU_BACK:
				return vendorKeys.get(__VendorKeys__.KEYBACK);

			case GenericDefaultKeys.MENU_ITEM_1:
			case NonStandardKey.VGAME_COMMAND_LEFT:
				return vendorKeys.get(__VendorKeys__.KEYSOFT1);

			case GenericDefaultKeys.MENU_ITEM_2:
			case NonStandardKey.VGAME_COMMAND_RIGHT:
				return vendorKeys.get(__VendorKeys__.KEYSOFT2);

			case GenericDefaultKeys.MENU_ITEM_3:
			case NonStandardKey.VGAME_COMMAND_CENTER:
			case ' ':
			case '\n':
				return vendorKeys.get(__VendorKeys__.KEYCENTER);

				// TODO: We also might need VGAME keys for all these, a few jars
				// don't use action keys for navigation at all, and that will be
				// an issue on jars tailored for keyboard phone layouts.
			case Canvas.KEY_NUM0:
			case NonStandardKey.NUMPAD_0:
				return vendorKeys.get(__VendorKeys__.KEYNUM0);

			case NonStandardKey.NUMPAD_1:
				return vendorKeys.get(__VendorKeys__.KEYNUM7);

			case NonStandardKey.NUMPAD_2:
				return vendorKeys.get(__VendorKeys__.KEYNUM8);

			case NonStandardKey.NUMPAD_3:
				return vendorKeys.get(__VendorKeys__.KEYNUM9);

			case NonStandardKey.NUMPAD_4:
				return vendorKeys.get(__VendorKeys__.KEYNUM4);

			case NonStandardKey.NUMPAD_5:
				return vendorKeys.get(__VendorKeys__.KEYNUM5);

			case NonStandardKey.NUMPAD_6:
				return vendorKeys.get(__VendorKeys__.KEYNUM6);

			case NonStandardKey.NUMPAD_7:
				return vendorKeys.get(__VendorKeys__.KEYNUM1);

			case NonStandardKey.NUMPAD_8:
				return vendorKeys.get(__VendorKeys__.KEYNUM2);

			case NonStandardKey.NUMPAD_9:
				return vendorKeys.get(__VendorKeys__.KEYNUM3);

			case NonStandardKey.KEY_POUND:
				return vendorKeys.get(__VendorKeys__.KEYPOUND);

			case NonStandardKey.KEY_STAR:
				return vendorKeys.get(__VendorKeys__.KEYSTAR);
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
		EnumTypeMap<__VendorKeys__, Integer> vendorKeys = this._vendorKeys;

		// Vendors expose these as physical Key IDs, so do the same here.
		if (__vc == vendorKeys.get(__VendorKeys__.KEYCENTER))
			return Canvas.FIRE;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYDOWN))
			return Canvas.DOWN;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYLEFT))
			return Canvas.LEFT;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM1))
			return Canvas.GAME_A;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM3))
			return Canvas.GAME_B;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM7))
			return Canvas.GAME_C;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM9))
			return Canvas.GAME_D;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYRIGHT))
			return Canvas.RIGHT;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYUP))
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
		EnumTypeMap<__VendorKeys__, Integer> vendorKeys = this._vendorKeys;

		// Vendors expose these as physical Key IDs, so do the same here.
		if (__vc == vendorKeys.get(__VendorKeys__.KEYCENTER))
			return GenericDefaultKeys.MENU_ITEM_3;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYBACK))
			return GenericDefaultKeys.MENU_BACK;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYDOWN))
			return GenericDefaultKeys.ARROW_DOWN;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYLEFT))
			return GenericDefaultKeys.ARROW_DOWN;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM0))
			return NonStandardKey.NUMPAD_0;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM1))
			return NonStandardKey.NUMPAD_7;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM2))
			return NonStandardKey.NUMPAD_8;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM3))
			return NonStandardKey.NUMPAD_9;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM4))
			return NonStandardKey.NUMPAD_4;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM5))
			return NonStandardKey.NUMPAD_5;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM6))
			return NonStandardKey.NUMPAD_6;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM7))
			return NonStandardKey.NUMPAD_1;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM8))
			return NonStandardKey.NUMPAD_2;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYNUM9))
			return NonStandardKey.NUMPAD_3;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYPOUND))
			return NonStandardKey.KEY_POUND;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYRIGHT))
			return GenericDefaultKeys.ARROW_RIGHT;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYSOFT1))
			return GenericDefaultKeys.MENU_ITEM_1;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYSOFT2))
			return GenericDefaultKeys.MENU_ITEM_2;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYSTAR))
			return NonStandardKey.KEY_STAR;

		if (__vc == vendorKeys.get(__VendorKeys__.KEYUP))
			return GenericDefaultKeys.ARROW_UP;

		return 0;
	}

	/**
	 * Instances a new {@link GenericKeyCodeTranslator} for use within canvases
	 * and other classes that handle input.
	 *
	 * @return A new {@link GenericKeyCodeTranslator}, or null if a translator
	 * @since 2026/05/22
	 */
	@Nullable
	@SquirrelJMEVendorApi
	public static GenericKeyCodeTranslator instance()
	{
		String vendorKeycode = System.getProperty(
			GenericKeyCodeTranslator.KEY_VENDOR_PROPERTY);

		if (vendorKeycode == null || vendorKeycode.isEmpty())
			vendorKeycode = RuntimeShelf.systemEnv(
				GenericKeyCodeTranslator.KEY_VENDOR_ENV);

		// No vendor keycode? Return null, as the default mapping will be used.
		if (vendorKeycode == null || vendorKeycode.isEmpty())
			return null;

		EnumTypeMap<__VendorKeys__, Integer> vendorKeys =
			GenericKeyCodeTranslator.__parse(vendorKeycode);

		// Use it!
		return new GenericKeyCodeTranslator(vendorKeys);
	}

	/**
	 * Parse a vendor's key layout, and populate the key map with it.
	 *
	 * Key disposition for vendors may be as follows, although key order does
	 * not matter as long as all key maps are present.
	 *
	 * If a vendor does not have a certain key, or if its keycode for a given
	 * key in the array does not deviate from MIDP's default, its key value will
	 * be 0 as that will make EventTranslator revert to Canvas' default input
	 * values.
	 *
	 * @param __vendorName The vendor name whose key layout should be parsed.
	 * @return A {@link EnumTypeMap} containing the vendor's key layout.
	 * @throws NullPointerException If {@code __vendorName} is {@code null}.
	 * @since 2026/05/22
	 */
	@KeepWhenCompacting
	@NotNull
	private static EnumTypeMap<__VendorKeys__, Integer> __parse(
		@NotNull String __vendorName)
		throws NullPointerException
	{
		if (__vendorName == null)
			throw new NullPointerException("NARG");

		// Key disposition for vendors may be as follows:
		// [Up, Down, Left, Right, Center/Fire/Soft3, Soft1, Soft2,
		// Back/CLR, num0, num1, num2, num3, num4, num5, num6, num7, num8,
		// num9, *, #]
		//
		// Some vendors do have other keys (like some KDDI and SKT devices
		// having multiple COM keys, or nokia's SEND key) but these
		// are not a concern at the moment. Also, numpad keys are only used
		// by vendor key layouts of keyboard devices that must set
		// themselves to numpad mode for app compatibility, like Nokia's
		// E-Series does, at the cost of not using Canvas' default numpad
		// codes.
		try (InputStream in = KeyCodeTranslator.class.getResourceAsStream(
			"keymaps/" + __vendorName + ".csv"))
		{
			if (in == null)
				throw Debugging.oops("Keymap not found");

			CsvReader<String[]> reader = new CsvReader<>(
				new KeymapCsvDeserializer(), new CsvReaderInputStream(in));
			EnumTypeMap<__VendorKeys__, Integer> keys =
				new EnumTypeMap<>(__VendorKeys__.class,
					__VendorKeys__.values());
			List<String[]> codes = reader.readAll();

			for (int i = 0, n = codes.size(); i < n; i++)
			{
				// TODO: Skip the 'haskeyboard' key for now.
				// Keyboard input still needs more work.
				if (codes.get(i)[0].toLowerCase().equals("haskeyboard"))
				{
					Debugging.todoNote("Generic keyboard mappings are not " +
						"implemented yet.");
					continue;
				}

				keys.put(__VendorKeys__.valueOf(codes.get(i)[0].
					toUpperCase()), Integer.parseInt(codes.get(i)[1]));
			}

			return keys;
		}

		// If the CSV is corrupted or somehow invalid and any exception
		// is caught when parsing, we must fail as that CSV should be fixed.
		catch (IOException|NoSuchElementException e)
		{
			throw Debugging.oops("Malformed or corrupted vendor keymap: " +
				e.getMessage());
		}
	}
}
