// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * Table of contents for shown values.
 * 
 * "Why don't you eat the boulder?".
 *
 * @since 2024/01/23
 */
public class ShownTableOfContents
	extends JPanel
{
	/**
	 * Initializes the table of contents view.
	 *
	 * @since 2024/01/23
	 */
	public ShownTableOfContents()
	{
		// Readability
		this.setMinimumSize(new Dimension(100, 100));
	}
}
