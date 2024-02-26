// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCapability;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

/**
 * Capability inspection dialog.
 *
 * @since 2024/01/19
 */
public class InspectCapabilities
	extends JDialog
{
	/**
	 * Initializes the dialog.
	 *
	 * @param __owner The owning frame.
	 * @param __capabilities The capabilities to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public InspectCapabilities(PrimaryFrame __owner,
		CapabilityStatus __capabilities)
		throws NullPointerException
	{
		super(__owner);
		
		if (__capabilities == null)
			throw new NullPointerException("NARG");
		
		// Set info
		this.setTitle("VM Capabilities");
		
		// Minimum size for readability
		this.setMinimumSize(new Dimension(300, 300));
		
		// Layout for the dialog
		this.setLayout(new BorderLayout());
		
		// Scrolling for readability
		JPanel scrollPanel = new JPanel();
		JScrollPane scroll = new JScrollPane(scrollPanel,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Readability
		scroll.setMinimumSize(new Dimension(300, 300));
		scrollPanel.setMinimumSize(new Dimension(300, 300));
		
		// Use a vertical flowing of items
		GridLayout layout = new GridLayout(0, 1);
		scrollPanel.setLayout(layout);
		
		// Add each capability
		for (JDWPCapability cap : JDWPCapability.values())
		{
			// Setup check box
			JCheckBox check = new JCheckBox(cap.toString(),
				__capabilities.has(cap));
			
			// Make read only
			check.setEnabled(false);
			
			// Add it to the frame
			scrollPanel.add(check);
		}
		
		// Add the scroll
		this.add(scroll, BorderLayout.CENTER);
		
		// Pack the frame
		this.pack();
	}
}
