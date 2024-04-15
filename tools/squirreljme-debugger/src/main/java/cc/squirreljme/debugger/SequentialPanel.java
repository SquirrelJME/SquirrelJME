// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

/**
 * Sequential viewing pane.
 *
 * @since 2024/01/22
 */
public class SequentialPanel
{
	/** The panel to use when returning. */
	protected final JComponent viewPanel;
	
	/** The panel for this pane. */
	protected final JPanel addPanel;
	
	/** Blank filler. */
	protected final JLabel filler =
		new JLabel();
	
	/** Does this have items? */
	private volatile boolean _hasItems;
	
	/**
	 * Initializes the panel.
	 *
	 * @since 2024/01/22
	 */
	public SequentialPanel()
	{
		this(true);
	}
	
	/**
	 * Initializes the panel.
	 *
	 * @param __scroll Use a scrolling area for the panel?
	 * @since 2024/01/22
	 */
	public SequentialPanel(boolean __scroll)
	{
		JPanel panel = new JPanel();
		this.addPanel = panel;
		
		// Use grid bag layout for this
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		
		// Use scrolling area
		if (__scroll)
		{
			// Setup shown area for all the instructions in the method
			JScrollPane scroll = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			
			// Scrolling is so horrendously slow...
			scroll.getVerticalScrollBar().setUnitIncrement(
				scroll.getVerticalScrollBar().getUnitIncrement() * 8);
			scroll.setWheelScrollingEnabled(true);
			
			// We want to view the scroll
			this.viewPanel = scroll;
		}
		
		// Do not use a scrolling area
		else
			this.viewPanel = panel;
		
		// Initialize with filler
		panel.add(this.filler);
	}
	
	/**
	 * Adds a component to the panel.
	 *
	 * @param __component The component to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public void add(JComponent __component)
		throws NullPointerException
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Force to the left and take no vertical space
		constraints.weightx = 20.0;
		constraints.weighty = 0.0;
		
		// Remove the filler
		this.addPanel.remove(this.filler);
		
		// Add to the panel
		this.addPanel.add(__component, constraints);
		
		// Add the filler back with an extreme weight
		constraints.weighty = 20.0;
		this.addPanel.add(this.filler, constraints);
		
		// Has items now
		this._hasItems = true;
		
		// Repaint
		Utils.revalidate(this.addPanel);
		Utils.revalidate(this.viewPanel);
	}
	
	/**
	 * Does this have any items?
	 *
	 * @return If this has items or not.
	 * @since 2024/01/27
	 */
	public boolean hasItems()
	{
		return this._hasItems;
	}
	
	/**
	 * Look at the given component.
	 *
	 * @param __component The component to look at.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/27
	 */
	public void lookAt(JComponent __component)
		throws NullPointerException
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		// If we are using the scroll pane, we need to focus on this
		JComponent viewPanel = this.viewPanel;
		if (viewPanel instanceof JScrollPane)
		{
			JScrollPane scroll = (JScrollPane)viewPanel;
			JViewport viewport = scroll.getViewport();
			
			// Where is the current view?
			Rectangle viewRect = viewport.getViewRect();
			
			// Scroll here
			Rectangle bounds = __component.getBounds();
			viewport.setViewPosition(new Point(bounds.x,
				bounds.y - (viewRect.height / 4)));
			//viewport.scrollRectToVisible(bounds);
			
			// Make sure the view is updated
			Utils.revalidate(scroll);
		}
	}
	
	/**
	 * Returns the used panel for this pane.
	 *
	 * @return The used panel.
	 * @since 2024/01/22
	 */
	public JComponent panel()
	{
		return this.viewPanel;
	}
	
	/**
	 * Removes all items.
	 *
	 * @since 2024/01/25
	 */
	public void removeAll()
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Clear everything
		this.addPanel.removeAll();
		
		// Add the filler back with an extreme weight
		constraints.weighty = 20.0;
		this.addPanel.add(this.filler, constraints);
		
		// No longer has items
		this._hasItems = false;
		
		// Repaint
		Utils.revalidate(this.addPanel);
		Utils.revalidate(this.viewPanel);
	}
}
