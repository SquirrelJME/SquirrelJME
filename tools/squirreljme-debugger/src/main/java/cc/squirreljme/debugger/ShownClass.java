// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import net.multiphasicapps.classfile.ClassName;

/**
 * Panel for showing class information.
 *
 * @since 2024/01/22
 */
public class ShownClass
	extends JPanel
{
	/** The viewer for classes. */
	protected final ClassViewer viewer;
	
	/** The label description what is being looked at. */
	protected final JLabel whatLabel;
	
	/** The sequential panel view. */
	protected final SequentialPanel seqPanel;
	
	/** The methods to show. */
	private volatile ShownMethod[] _shownMethods;
	
	/**
	 * Initializes the class viewer.
	 *
	 * @param __viewer The viewer for the class to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public ShownClass(ClassViewer __viewer)
		throws NullPointerException
	{
		this(__viewer, true);
	}
	
	/**
	 * Initializes the class viewer.
	 *
	 * @param __viewer The viewer for the class to show.
	 * @param __scroll Use a scrolling area for this?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public ShownClass(ClassViewer __viewer, boolean __scroll)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		this.viewer = __viewer;
		
		// Use border layout for this panel since it is cleaner
		this.setLayout(new BorderLayout());
		
		// Set up the label description what we are looking at
		JLabel whatLabel = new JLabel();
		whatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Set at the top
		this.add(whatLabel, BorderLayout.PAGE_START);
		this.whatLabel = whatLabel;
		
		// Setup sequential panel
		SequentialPanel seqPanel = new SequentialPanel(__scroll);
		this.add(seqPanel.panel(), BorderLayout.CENTER);
		this.seqPanel = seqPanel;
		
		// Initialize with instructions
		this.shownUpdate();
	}
	
	/**
	 * Updates the shown information.
	 *
	 * @since 2024/01/22
	 */
	public void shownUpdate()
	{
		// Do we need to get the methods to show?
		ClassViewer viewer = this.viewer;
		ShownMethod[] shownMethods = this._shownMethods;
		if (shownMethods == null)
		{
			// Get the methods to view
			MethodViewer[] methods = viewer.methods();
			
			// Do nothing yet if we do not know what instructions exist
			if (methods == null)
				return;
		
			// All the instructions are placed here
			SequentialPanel seqPanel = this.seqPanel;
			
			// Add everything to the grid view
			int count = methods.length;
			shownMethods = new ShownMethod[count];
			for (int i = 0; i < count; i++)
			{
				// Initialize a shower!
				ShownMethod shown = new ShownMethod(
					methods[i]);
				
				// Show it
				shownMethods[i] = shown;
				seqPanel.add(shown);
			}
			
			// Store for later updates
			this._shownMethods = shownMethods;
		}
		
		// Set the name of the class
		ClassName thisName = this.viewer.thisName();
		if (thisName != null)
			this.whatLabel.setText(thisName.toString());
		else
			this.whatLabel.setText("Unknown?");
		
		// Go through an update everything accordingly
		for (ShownMethod shown : shownMethods)
			shown.shownUpdate();
	}
}
