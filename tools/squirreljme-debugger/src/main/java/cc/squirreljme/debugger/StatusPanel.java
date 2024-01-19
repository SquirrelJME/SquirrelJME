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
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * Status panel tracker, as Swing does not have one!
 *
 * @since 2024/01/19
 */
public class StatusPanel
	extends JPanel
	implements TallyListener
{
	/** The state of the debugger. */
	protected final DebuggerState debuggerState;
	
	/** Received Label. */
	protected final JLabel receivedLabel;
	
	/**
	 * Initializes the panel.
	 *
	 * @param __debuggerState The state of the debugger.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public StatusPanel(DebuggerState __debuggerState)
		throws NullPointerException
	{
		if (__debuggerState == null)
			throw new NullPointerException("NARG");
		
		// Store for tracking
		this.debuggerState = __debuggerState;
		
		// Get text size
		
		// Make sure it is always visible
		this.setMinimumSize(new Dimension(320, 16));
		this.setPreferredSize(new Dimension(320, 16));
		
		// Beveled border, which is generally natural
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		// Flow layouts are clean
		this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		// Received packets label
		JLabel receivedLabel = new JLabel();
		this.receivedLabel = receivedLabel;
		
		// Add labels
		this.add(receivedLabel);
		
		// Add listeners to tallies to update stats
		__debuggerState.receiveTally.addListener(this);
		__debuggerState.disconnectedTally.addListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void updateTally(TallyTracker __which, int __old, int __new)
	{
		DebuggerState debuggerState = this.debuggerState;
		
		// Received a packet?
		if (__which == debuggerState.receiveTally)
			this.receivedLabel.setText(
				String.format("Received: %d", __new));
		
		// Disconnected?
		else if (__which == debuggerState.disconnectedTally)
			this.receivedLabel.setText(
				String.format("DISCONNECTED: %d",
					debuggerState.receiveTally.get()));
	}
}
