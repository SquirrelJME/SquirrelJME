// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nokia;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.lcdui.event.GenericDefaultKeys;
import cc.squirreljme.runtime.lcdui.event.KeyCodeTranslator;
import com.nokia.mid.ui.FullCanvas;

/**
 * Translator for Nokia events.
 *
 * @since 2022/02/03
 */
@SquirrelJMEVendorApi
public class NokiaKeyCodeTranslator
	implements KeyCodeTranslator
{
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public boolean accepts(String __identifier, boolean __exact)
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		// Handle all Nokia devices in a generic manner, unless exactly
		// generic
		return __identifier.equals("com.nokia") ||
			(!__exact && __identifier.startsWith("com.nokia."));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/12
	 */
	@Override
	public int gameActionToVendor(int __ga, boolean __last)
	{
		// TODO: Use MIDP generic mapping???
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/03
	 */
	@Override
	@SquirrelJMEVendorApi
	public int keyCodeToVendor(int __kc)
	{
		// Nokia exposes these as physical Key IDs, so do the same here
		// Since most software is made for Nokia we pretty much the
		// standard and as such have to support doing it this way.
		switch (__kc)
		{
			case GenericDefaultKeys.MENU_ITEM_1:
			case NonStandardKey.VGAME_COMMAND_LEFT:
				return FullCanvas.KEY_SOFTKEY1;
			
			case GenericDefaultKeys.MENU_ITEM_2:
			case NonStandardKey.VGAME_COMMAND_RIGHT:
				return FullCanvas.KEY_SOFTKEY2;
			
			case GenericDefaultKeys.MENU_ITEM_3:
			case NonStandardKey.VGAME_COMMAND_CENTER:
				return FullCanvas.KEY_SOFTKEY3;
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/03
	 */
	@Override
	@SquirrelJMEVendorApi
	public int vendorToGameAction(int __vc, boolean __last)
	{
		// TODO: Use MIDP generic mapping???
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public int vendorToKeyCode(int __vc)
	{
		// Nokia exposes these as physical Key IDs, so do the same here
		// Since most software is made for Nokia we pretty much the
		// standard and as such have to support doing it this way.
		switch (__vc)
		{
			case FullCanvas.KEY_SOFTKEY1:
				return GenericDefaultKeys.MENU_ITEM_1;
			
			case FullCanvas.KEY_SOFTKEY2:
				return GenericDefaultKeys.MENU_ITEM_2;
			
			case FullCanvas.KEY_SOFTKEY3:
				return GenericDefaultKeys.MENU_ITEM_3;
		}
		
		return 0;
	}
}
