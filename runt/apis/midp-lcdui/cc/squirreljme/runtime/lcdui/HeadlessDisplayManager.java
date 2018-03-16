// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.lcdui.DisplayHead;
import cc.squirreljme.runtime.lcdui.DisplayManager;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;

/**
 * This is a headless display manager which provides only a non-visible
 * display.
 *
 * @since 2017/08/21
 */
@Deprecated
public class HeadlessDisplayManager
	extends DisplayManager
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/21
	 */
	public DisplayHead[] heads()
	{
		return new DisplayHead[0];
	}
}

