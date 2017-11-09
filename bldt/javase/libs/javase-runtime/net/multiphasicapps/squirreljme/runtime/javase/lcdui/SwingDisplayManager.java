// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase.lcdui;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;
import net.multiphasicapps.squirreljme.lcdui.DisplayHead;
import net.multiphasicapps.squirreljme.lcdui.DisplayManager;

/**
 * This provides access to Swing display heads for displaying graphics.
 *
 * @since 2017/08/19
 */
public class SwingDisplayManager
	extends DisplayManager
{
	/** The single display head instance to use always. */
	private static final SwingDisplayHead _HEAD =
		new SwingDisplayHead();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/19
	 */
	@Override
	public DisplayHead[] heads()
	{
		return new DisplayHead[]{_HEAD};
	}
}

