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
import java.util.ServiceLoader;
import javax.microedition.lcdui.Canvas;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;

/**
 * This is used with {@link ServiceLoader} to implement API specific key
 * event translation.
 * 
 * The order of translation is always between SquirrelJME/ScritchUI key codes
 * and Vendor Specific Keycodes; and then Vendor Specific KeyCodes and
 * Game Actions.
 * 
 * Game actions are mapped through {@link Canvas#getGameAction(int)} and
 * {@link Canvas#getKeyCode(int)}.
 * 
 * Note that for vendor key conversion it is highly recommended to handle
 * cases where the input is a {@code NonStandardKey.VGAME_...} key as this
 * generally indicates that a non-keyboard controller device is being used
 * as the input mechanism for ScritchUI. Note that {@code VGAME_...} has
 * a specific layout.
 *
 * Game actions are mapped to physical keys such as left/right/up/down
 * and select. Also since some phones only have a dial pad this means
 * that game actions take up actual digits on the phone itself.
 * 
 * <pre>
 * [1 2 3] > [A ^ B]
 * [4 5 6] > [< F >]
 * [7 8 9] > [C v D]
 * [* 0 #] > [* 0 #]
 * </pre>
 * 
 * @since 2022/02/23
 */
@SquirrelJMEVendorApi
public interface KeyCodeTranslator
{
	/**
	 * If this is returned by any method, the conversion processor should just
	 * flat out fail and treat the key as non-existent.
	 */
	@SquirrelJMEVendorApi
	int IMMEDIATE_FAIL =
		Integer.MIN_VALUE;
	
	/**
	 * Does this translator handle the given identifier?
	 *
	 * This takes a reverse fully qualified domain name as input and should
	 * always start with the root vendor being the most generic device. For
	 * example {@code cc.squirreljme} would be a generic SquirrelJME device
	 * while {@code cc.squirreljme.phone.a} would be a SquirrelJME Phone
	 * Model A, hypothetically.
	 *
	 * This method is generally used if {@link ServiceLoader} is used to
	 * access a translator and one is defined.
	 *
	 * @param __identifier An identifier in the form of a reverse fully
	 * qualified domain name.
	 * @param __exact Require an exact match with no wildcard handling.
	 * @return If the translator handles the given vendor identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/05/13
	 */
	@SquirrelJMEVendorApi
	boolean accepts(@Language("rfqdn") String __identifier, boolean __exact)
		throws NullPointerException;
	
	/**
	 * Converts the game action to a vendor key code.
	 *
	 * @param __ga The game action, this may derive values
	 * from {@link Canvas} however it may also have vendor specific value.
	 * @param __last If {@code true} this is a last case translation.
	 * @return The game action or {@code 0} if it is not valid. Returning
	 * a value of {@link #IMMEDIATE_FAIL} will stop all processing.
	 * @since 2026/05/12
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = NonStandardKey.class)
	int gameActionToVendor(int __ga, boolean __last);
	
	/**
	 * Normalizes the given key code from SquirrelJME to a vendor specific
	 * code.
	 *
	 * @param __kc The key code, this is a SquirrelJME key.
	 * @return The normalized key code or {@code 0} if it is not
	 * normalizable. Returning
	 * a value of {@link #IMMEDIATE_FAIL} will stop all processing.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	int keyCodeToVendor(
		@MagicConstant(valuesFromClass = NonStandardKey.class) int __kc);
	
	/**
	 * Converts a vendor specific key code to a vendor specific game action.
	 *
	 * @param __vc The vendor specific key code.
	 * @param __last If {@code true} this is a last case translation.
	 * @return The game action or {@code 0} if it is not valid. This may
	 * derive values from {@link Canvas} however it may also have vendor
	 * specific value. Returning
	 * a value of {@link #IMMEDIATE_FAIL} will stop all processing.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	int vendorToGameAction(int __vc, boolean __last);
	
	/**
	 * Converts a vendor specific key to a SquirrelJME key
	 *
	 * @param __vc The vendor specific key code.
	 * @return A SquirrelJME key {@code 0} if it is not valid. Returning
	 * a value of {@link #IMMEDIATE_FAIL} will stop all processing.
	 * @since 2026/05/12
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = NonStandardKey.class)
	int vendorToKeyCode(int __vc);
}
