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
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

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
	
	/** State label. */
	protected final JLabel stateLabel;
	
	/** Received Label. */
	protected final JLabel receivedLabel;
	
	/** The number of packets that were sent. */
	protected final JLabel sentLabel;
	
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
		
		// Make sure it is always visible
		Font baseLabelFont = new JLabel().getFont();
		this.setMinimumSize(
			new Dimension(320, baseLabelFont.getSize()));
		/*this.setPreferredSize(new Dimension(320, 16));*/
		
		// Beveled border, which is generally natural
		/*this.setBorder(new BevelBorder(BevelBorder.LOWERED));*/
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Flow layouts are clean
		FlowLayout layout = new FlowLayout(FlowLayout.LEADING, 0, 0);
		layout.setAlignOnBaseline(true);
		this.setLayout(layout);
		
		// State label
		JLabel stateLabel = new JLabel("CONNECTED");
		this.__pretty(stateLabel);
		this.stateLabel = stateLabel;
		
		// Received packets label
		JLabel receivedLabel = new JLabel();
		this.__pretty(receivedLabel);
		this.receivedLabel = receivedLabel;
		
		// Sent packets
		JLabel sentLabel = new JLabel();
		this.__pretty(sentLabel);
		this.sentLabel = sentLabel;
		
		// Add labels
		this.add(stateLabel);
		this.add(receivedLabel);
		this.add(sentLabel);
		
		// Add listeners to tallies to update stats
		__debuggerState.receiveTally.addListener(this);
		__debuggerState.sentTally.addListener(this);
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
		
		// Received a packet? Or disconnected?
		if (__which == debuggerState.receiveTally)
			this.receivedLabel.setText(
				String.format("Received: %d", __new));
		
		// Sent a packet?
		else if (__which == debuggerState.sentTally)
			this.sentLabel.setText(
				String.format("Sent: %d", __new));
		
		// Disconnected?
		else if (__which == debuggerState.disconnectedTally)
			this.stateLabel.setText("DISCONNECTED");
	}
	
	/**
	 * Makes the border pretty.
	 *
	 * @param __label The label to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private void __pretty(JLabel __label)
		throws NullPointerException
	{
		if (__label == null)
			throw new NullPointerException("NARG");
		
		__label.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
}
