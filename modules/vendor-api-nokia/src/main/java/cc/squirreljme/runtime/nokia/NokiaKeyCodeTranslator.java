// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nokia;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
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
	 *
	 * @since 2022/02/03
	 */
	@Override
	@SquirrelJMEVendorApi
	public int keyCodeToGameAction(int __kc)
	{
		switch (__kc)
		{
			// Nokia Soft Key 1
			case NonStandardKey.F1:
			case NonStandardKey.VGAME_COMMAND_LEFT:
			case FullCanvas.KEY_SOFTKEY1:
				return FullCanvas.KEY_SOFTKEY1;
			
			// Nokia Soft Key 2
			case NonStandardKey.F2:
			case NonStandardKey.VGAME_COMMAND_CENTER:
			case FullCanvas.KEY_SOFTKEY2:
				return FullCanvas.KEY_SOFTKEY2;
			
			// Nokia Soft Key 3
			case NonStandardKey.F3:
			case NonStandardKey.VGAME_COMMAND_RIGHT:
			case FullCanvas.KEY_SOFTKEY3:
				return FullCanvas.KEY_SOFTKEY3;
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/02/03
	 */
	@Override
	@SquirrelJMEVendorApi
	public int normalizeKeyCode(int __kc)
	{
		// Nokia exposes these as physical Key IDs, so do the same here
		// Since most software is made for Nokia we pretty much the
		// standard and as such have to support doing it this way.
		switch (__kc)
		{
			case NonStandardKey.F1:
			case NonStandardKey.VGAME_COMMAND_LEFT:
				return FullCanvas.KEY_SOFTKEY1;
			
			case NonStandardKey.F2:
			case NonStandardKey.VGAME_COMMAND_RIGHT:
				return FullCanvas.KEY_SOFTKEY2;
			
			case NonStandardKey.F3:
			case NonStandardKey.VGAME_COMMAND_CENTER:
				return FullCanvas.KEY_SOFTKEY3;
		}
		
		return 0;
	}
}
