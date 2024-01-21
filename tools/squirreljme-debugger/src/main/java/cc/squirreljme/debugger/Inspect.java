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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Base inspector dialog.
 *
 * @param <I> The item to inspect.
 * @since 2024/01/20
 */
public abstract class Inspect<I extends Info>
	extends JDialog
{
	/** Known items being inspected. */
	private final List<InspectKnownValue> _known =
		new ArrayList<>();
	
	/** The root panel. */
	protected final JPanel panel;
	
	/** The state of the debugger. */
	protected final DebuggerState state;
	
	/** What item is being inspected? */
	private final WeakReference<I> _what;
	
	/**
	 * Initializes the base inspector.
	 *
	 * @param __owner The owning frame.
	 * @param __state The debugging state.
	 * @param __what What is being inspected.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public Inspect(PrimaryFrame __owner, DebuggerState __state, I __what)
		throws NullPointerException
	{
		if (__owner == null || __state == null || __what == null)
			throw new NullPointerException("NARG");
		
		// What is being inspected?
		this._what = new WeakReference<>(__what);
		
		// Store state for later updates
		this.state = __state;
		
		// This is hard to use when tiny
		this.setMinimumSize(new Dimension(320, 240));
		
		// Setup stretching panel
		JPanel panel = new JPanel();
		this.panel = panel;
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		
		// Setup grid view for items
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		panel.setLayout(layout);
		
		// Default settings
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START; 
		
		// Refresh button
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(this::__refreshButton);
		
		// Add to form
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(refresh, constraints);
		
		// Revert
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		
		// Add initial top items
		constraints.gridy = 1;
		constraints.gridx = 0;
		panel.add(new JLabel("Key"), constraints);
		constraints.gridx = 2;
		panel.add(new JLabel("Value"), constraints);
	}
	
	/**
	 * Internal update logic.
	 *
	 * @since 2024/01/20
	 */
	protected abstract void updateInternal();
	
	/**
	 * Adds a track on the given known value.
	 *
	 * @param __desc The description of the item.
	 * @param __value The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	protected final void addTrack(String __desc, KnownValue<?> __value)
		throws NullPointerException
	{
		if (__desc == null || __value == null)
			throw new NullPointerException("NARG");
		
		// Setup key and value
		JLabel key = new JLabel(__desc);
		InspectKnownValue value = new InspectKnownValue(__value);
		
		// Setup constraints
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		
		// The Y position is depending on which entry this is
		synchronized (this)
		{
			constraints.gridy = 2 + this._known.size();
		}
		
		// Add both
		constraints.gridx = 0;
		this.panel.add(key, constraints);
		constraints.gridx = 1;
		this.panel.add(value, constraints);
		
		// Add known
		this._known.add(value);
		
		// Perform packing
		this.pack();
	}
	
	/**
	 * Updates the inspection.
	 *
	 * @since 2024/01/20
	 */
	public final void update()
	{
		I what = this.what();
		
		// Update base information, if it still exists
		if (what != null)
			what.update(this.state, this::__update);
	}
	
	/**
	 * Returns the stored item.
	 *
	 * @return The stored item or {@code null} if it was disposed of.
	 * @since 2024/01/21
	 */
	protected final I what()
	{
		return this._what.get();
	}
	
	/**
	 * Performs the update.
	 *
	 * @param __event Not used.
	 * @since 2024/01/20
	 */
	private void __refreshButton(ActionEvent __event)
	{
		this.update();
	}
	
	/**
	 * Called when the information has been updated.
	 *
	 * @param __info The information that was updated.
	 * @since 2024/01/20
	 */
	private void __update(Info __info)
	{
		// Call internal logic
		this.updateInternal();
		
		// Update title of what we are looking at
		this.setTitle(this._what.toString());
		
		// Update all knowns
		InspectKnownValue[] update;
		synchronized (this)
		{
			List<InspectKnownValue> known = this._known;
			update = known.toArray(new InspectKnownValue[known.size()]);
		}
		
		// Update everything
		for (InspectKnownValue known : update)
			known.update();
		
		// Perform packing
		this.pack();
	}
}
