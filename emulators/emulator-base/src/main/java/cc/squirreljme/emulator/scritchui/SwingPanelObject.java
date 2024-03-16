// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import javax.swing.JPanel;

/**
 * Object for Swing panels.
 *
 * @since 2024/03/16
 */
public class SwingPanelObject
	extends SwingComponentObject
	implements ScritchPanelBracket
{
	/** The backing panel. */
	protected final JPanel panel =
		new JPanel();
}
