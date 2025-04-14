// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog for showing the disk probe.
 *
 * @since 2024/01/28
 */
public class VendorCommandProbe
	extends JDialog
	implements Runnable
{
	/** The start of the command set. */
	public static final int COMMAND_SET_START =
		128;
	
	/** The end of the command set. */
	public static final int COMMAND_SET_END =
		255;
	
	/** The number of command sets. */
	public static final int COMMAND_SET_COUNT =
		(VendorCommandProbe.COMMAND_SET_END -
			VendorCommandProbe.COMMAND_SET_START) + 1;
	
	/** The start of the command. */
	public static final int COMMAND_START =
		0;
	
	/** The end of the command. */
	public static final int COMMAND_END =
		255;
	
	/** The command count. */
	public static final int COMMAND_COUNT =
		(VendorCommandProbe.COMMAND_END -
			VendorCommandProbe.COMMAND_START) + 1;
	
	/** The total number of all items. */
	public static final int TOTAL_COUNT =
		VendorCommandProbe.COMMAND_SET_COUNT *
			VendorCommandProbe.COMMAND_COUNT;
	
	/** The panel where all the commands go. */
	protected final SequentialPanel sequence;
	
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** Labels for all results. */
	private final JLabel[] labels;
	
	/**
	 * Initializes the command probe.
	 *
	 * @param __owner The owning frame.
	 * @param __state The state used.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/28
	 */
	public VendorCommandProbe(PrimaryFrame __owner, DebuggerState __state)
		throws NullPointerException
	{
		super(__owner);
		
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.state = __state;
		
		// Set title and icon
		this.setTitle("VM Command Probe");
		Utils.setIcon(this);
		
		// Readability
		this.setMinimumSize(new Dimension(300, 300));
		
		// Set up a ton of labels
		JLabel[] labels = new JLabel[VendorCommandProbe.TOTAL_COUNT];
		for (int i = 0; i < VendorCommandProbe.TOTAL_COUNT; i++)
			labels[i] = new JLabel("Unknown");
		this.labels = labels;
		
		// Store all of these into the sequence
		SequentialPanel sequence = new SequentialPanel(true);
		this.sequence = sequence;
		for (int i = 0; i < VendorCommandProbe.TOTAL_COUNT; i++)
			sequence.add(new KeyValuePanel(
				new JLabel(String.format("%d.%d",
					(i / VendorCommandProbe.COMMAND_COUNT) +
						VendorCommandProbe.COMMAND_SET_START,
					(i % VendorCommandProbe.COMMAND_COUNT))),
				labels[i]));
		
		// Add everything
		this.add(sequence.panel());
		
		// Run the probe task
		new Thread(this, "JDWPCommandProbe").start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/28
	 */
	@Override
	public void run()
	{
		DebuggerState state = this.state;
		
		// Do the very long probe
		for (int commandSet = VendorCommandProbe.COMMAND_SET_START;
			 commandSet <= VendorCommandProbe.COMMAND_SET_END; commandSet++)
			for (int command = VendorCommandProbe.COMMAND_START;
				 command <= VendorCommandProbe.COMMAND_END; command++)
				try (JDWPPacket out = state.request(commandSet, command))
				{
					int copyCommandSet = commandSet;
					int copyCommand = command;
					state.send(out,
						(__state, __reply) -> {
							this.__result(copyCommandSet, copyCommand,
								JDWPErrorType.NO_ERROR);
						}, (__state, __reply) -> {
							this.__result(copyCommandSet, copyCommand,
								__reply.error());
						});
				}
	}
	
	/**
	 * Updates this item
	 *
	 * @since 2024/01/28
	 */
	public void update()
	{
		Utils.revalidate(this.sequence.panel());
	}
	
	/**
	 * Sets the result of the command.
	 *
	 * @param __commandSet The command set used.
	 * @param __command The command sued.
	 * @param __error The error that was given.
	 * @since 2024/01/28
	 */
	private void __result(int __commandSet, int __command,
		JDWPErrorType __error)
	{
		// What is the index?
		int index = ((__commandSet - VendorCommandProbe.COMMAND_SET_START) *
			VendorCommandProbe.COMMAND_COUNT) + __command;
		
		// Need to update within the Swing thread
		Utils.swingInvoke(() -> {
			JLabel label = this.labels[index];
			label.setText(Objects.toString(__error, "null"));
			
			// Needs to be revalidated
			Utils.revalidate(label);
			
			// Update UI
			this.update();
		});
	}
}
