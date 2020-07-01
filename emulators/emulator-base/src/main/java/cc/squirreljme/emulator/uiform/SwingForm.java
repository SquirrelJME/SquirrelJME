// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * Represents a single Swing form, which is built-on top of a panel that
 * is shown in a window.
 *
 * @since 2020/07/01
 */
public final class SwingForm
	implements UIFormBracket
{
	/** The panel which makes up the form. */
	protected final JPanel panel;
	
	/**
	 * Initializes the form.
	 * 
	 * @since 2020/07/01
	 */
	public SwingForm()
	{
		JPanel panel = new JPanel();
		
		// Make sure the panel is not so tiny
		Dimension minDim = new Dimension(SwingHardMetrics.DISPLAY_MIN_WIDTH,
			SwingHardMetrics.DISPLAY_MIN_HEIGHT);
		panel.setMinimumSize(minDim);
		panel.setPreferredSize(minDim);
		panel.setSize(minDim);
		
		// Use a border layout because we can set tops and bottoms
		// accordingly
		panel.setLayout(new BorderLayout());
		
		this.panel = panel;
	}
	
	/**
	 * Deletes the form.
	 * 
	 * @since 2020/07/01
	 */
	public void delete()
	{
		Debugging.todoNote("Form deletion?");
	}
}
