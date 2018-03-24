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

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * This is a panel which would directly be attached to a display head.
 *
 * @since 2018/03/24
 */
public class SwingDisplayHeadPanel
	extends JPanel
{
	/**
	 * Initializes the display head panel.
	 *
	 * @since 2018/03/24
	 */
	public SwingDisplayHeadPanel()
	{
		// Display heads which are very small are hard to use
		this.setMinimumSize(new Dimension(160, 160));
		this.setPreferredSize(new Dimension(640, 480));
	}
}

