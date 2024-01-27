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
import java.awt.Window;
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
	protected final SequentialPanel panel;
	
	/** The state of the debugger. */
	protected final DebuggerState state;
	
	/** The owning window. */
	protected final Window owner;
	
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
	public Inspect(Window __owner, DebuggerState __state, I __what)
		throws NullPointerException
	{
		if (__owner == null || __state == null || __what == null)
			throw new NullPointerException("NARG");
		
		// Used for later
		this.owner = __owner;
		
		// What is being inspected?
		this._what = new WeakReference<>(__what);
		
		// Title
		this.setTitle(__what.toString());
		
		// Set window icon
		Utils.setIcon(this);
		
		// Store state for later updates
		this.state = __state;
		
		// This is hard to use when tiny
		this.setMinimumSize(new Dimension(320, 240));
		
		// BorderLayout keeps things simple
		this.setLayout(new BorderLayout());
		
		// Setup panel for viewing
		SequentialPanel panel = new SequentialPanel(true);
		this.panel = panel;
		this.add(panel.panel(), BorderLayout.CENTER);
		
		// Refresh button
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(this::__refreshButton);
		panel.add(refresh);
		
		// Add basic key/value panel
		panel.add(new KeyValuePanel(
			new JLabel("Key"),
			new JLabel("Value")));
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
		
		// Setup key
		JLabel key = new JLabel(__desc);
		key.setMinimumSize(new Dimension(100, 1));
		
		// Setup value
		InspectKnownValue value = new InspectKnownValue(this.owner,
			this.state, __value);
		
		// Add key/value
		this.panel.add(new KeyValuePanel(
			key, value));
		
		// Add known
		this._known.add(value);
		
		// Perform packing
		this.pack();
	}
	
	/**
	 * Adds a track on the given generic value.
	 *
	 * @param __desc The description of the item.
	 * @param __value The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected final void addTrack(String __desc, Object __value)
		throws NullPointerException
	{
		if (__desc == null || __value == null)
			throw new NullPointerException("NARG");
		
		// If a known value was passed, use that
		if (__value instanceof KnownValue)
			this.addTrack(__desc, (KnownValue<?>)__value);
		
		// Otherwise just wrap it in a known value
		else
			this.addTrack(__desc, new KnownValue(
				__value.getClass(), KnownValueUpdater.IGNORED));
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
		I what = this.what();
		if (what == null)
			this.setTitle("Garbage Collected");
		else
			this.setTitle(what.toString());
		
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
