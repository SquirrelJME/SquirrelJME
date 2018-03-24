// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.server.LcdDefinition;
import javax.swing.SwingUtilities;

/**
 * This contains the implementation of the LCDUI server which utilizes Swing
 * to display graphics to the user.
 *
 * @since 2018/03/15
 */
public class SwingDefinition
	extends LcdDefinition
{
	/**
	 * Initializes the Swing LCDUI server.
	 *
	 * @since 2018/03/17
	 */
	public SwingDefinition()
	{
		super(new SwingDisplays());
	}
}

