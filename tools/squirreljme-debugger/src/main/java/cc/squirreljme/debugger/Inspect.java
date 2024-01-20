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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Base inspector dialog.
 *
 * @param <I> The item to inspect.
 * @since 2024/01/20
 */
public abstract class Inspect<I>
	extends JDialog
{
	/** What item is being inspected? */
	protected final I what;
	
	/** Known items being inspected. */
	private final List<InspectKnownValue> _known =
		new ArrayList<>();
	
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
		this.what = __what;
		
		// This is hard to use when tiny
		this.setMinimumSize(new Dimension(320, 240));
		
		// Setup grid view for items
		GridLayout layout = new GridLayout(0, 2);
		this.setLayout(layout);
		
		// Refresh button
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(this::__refreshButton);
		
		this.add(new JLabel(""));
		this.add(refresh);
		
		// Add initial top items
		this.add(new JLabel("Key"));
		this.add(new JLabel("Value"));
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
		
		// Add both
		this.add(key);
		this.add(value);
		
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
		// Call internal logic
		this.updateInternal();
		
		// Update title of what we are looking at
		this.setTitle(this.what.toString());
		
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
}
