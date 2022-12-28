// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is an attachment which bases itself on top of {@link UIFormBracket}
 * via the {@link UIFormShelf}, it implements the simplest means of a
 * framebuffer by initializing just a {@link UIItemType#CANVAS} as its
 * display.
 *
 * @since 2022/07/20
 */
public class UIFormAttachment
	extends FBUIBackend
{
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	protected FBDisplay[] queryDisplays()
	{
		UIDisplayBracket[] uiDisplays = UIFormShelf.displays();
		
		// Wrap around existing displays
		int n = uiDisplays.length;
		UIFormFBDisplay[] result = new UIFormFBDisplay[n];
		for (int i = 0; i < n; i++)
			result[i] = new UIFormFBDisplay(uiDisplays[i]);
		
		return result;
	}
}
