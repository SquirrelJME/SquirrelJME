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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * A panel which shows a single Java method.
 *
 * @since 2024/01/21
 */
public class ShownMethod
	extends JPanel
{
	/** The viewer for the method being shown. */
	protected final MethodViewer viewer;
	
	/** The label which says what method this is. */
	protected final JLabel whatLabel;
	
	/** The grid panel view. */
	protected final JPanel gridPanel;
	
	/** All the current instruction showers. */
	private volatile ShownInstruction[] _shownInstructions;
	
	/**
	 * Initializes the method viewer.
	 *
	 * @param __viewer The method to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public ShownMethod(MethodViewer __viewer)
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
		whatLabel.setText(String.format("%s%s%s::%s",
			(__viewer.isNative() ? "native " : ""),
			(__viewer.isAbstract() ? "abstract " : ""),
			__viewer.inClass(), __viewer.methodNameAndType()));
		
		// Set at the top
		this.add(whatLabel, BorderLayout.PAGE_START);
		this.whatLabel = whatLabel;
		
		// Primary grid view for instructions
		JPanel flowPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		flowPanel.setLayout(flowLayout);
		
		JPanel gridPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 1);
		gridLayout.setHgap(0);
		gridLayout.setVgap(0);
		gridPanel.setLayout(gridLayout);
		
		flowPanel.add(gridPanel);
		this.gridPanel = gridPanel;
		
		// Setup shown area for all the instructions in the method
		JScrollPane scroll = new JScrollPane(flowPanel,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Show the scroll pane
		this.add(scroll, BorderLayout.CENTER);
		
		// Initialize with instructions
		this.shownUpdate();
	}
	
	/**
	 * Updates the given view.
	 * 
	 * @since 2024/01/21
	 */
	public void shownUpdate()
	{
		// Cannot show any byte code if this is abstract or native
		MethodViewer viewer = this.viewer;
		if (viewer.isNative() || viewer.isAbstract())
			return;
		
		// Do we need to get the instructions to show?
		ShownInstruction[] shownInstructions = this._shownInstructions;
		if (shownInstructions == null)
		{
			// Get the instructions to view
			InstructionViewer[] instructions = viewer.instructions();
			
			// Do nothing yet if we do not know what instructions exist
			if (instructions == null)
				return;
		
			// All the instructions are placed here
			JPanel gridPanel = this.gridPanel;
			
			// Add everything to the grid view
			int count = instructions.length;
			shownInstructions = new ShownInstruction[count];
			for (int i = 0; i < count; i++)
			{
				// Initialize a shower!
				ShownInstruction shown = new ShownInstruction(
					instructions[i]);
				
				// Show it
				shownInstructions[i] = shown;
				gridPanel.add(shown);
			}
			
			// Store for later updates
			this._shownInstructions = shownInstructions;
		}
		
		// Go through an update everything accordingly
		for (ShownInstruction shown : shownInstructions)
			shown.shownUpdate();
	}
}
