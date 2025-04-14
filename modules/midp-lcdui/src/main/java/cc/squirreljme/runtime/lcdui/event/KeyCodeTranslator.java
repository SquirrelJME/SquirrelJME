// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.ServiceLoader;

/**
 * This is used with {@link ServiceLoader} to implement API specific event
 * translators.
 * 
 * @since 2022/02/23
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@SquirrelJMEVendorApi
public interface KeyCodeTranslator
{
	/**
	 * Converts the key code to a game action.
	 *
	 * @param __kc The key code.
	 * @return The game action or {@code 0} if it is not valid.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	int keyCodeToGameAction(int __kc);
	
	/**
	 * Normalizes the given key code.
	 * 
	 * @param __kc The key code.
	 * @return The normalized key code or {@code 0} if it is not normalizable.
	 * @since 2022/02/03
	 */
	@SquirrelJMEVendorApi
	int normalizeKeyCode(int __kc);
}
