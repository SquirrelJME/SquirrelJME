// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Shows information on a single instruction.
 *
 * @since 2024/01/21
 */
public class ShownInstruction
	extends JPanel
{
	/** The instruction viewer. */
	protected final InstructionViewer viewer;
	
	/** Breakpoint button. */
	protected final JButton breakpoint;
	
	/** The address of this instruction. */
	protected final JTextField address;
	
	/** The label which describes this. */
	protected final JTextField description;
	
	/** The optional debugger state. */
	protected final DebuggerState state;
	
	/** The context for this instruction. */
	protected final ContextThreadFrame context;
	
	/**
	 * Initializes the instruction line.
	 *
	 * @param __state The optional state for tracking.
	 * @param __viewer The viewer to use.
	 * @param __context The optional thread debugging context.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public ShownInstruction(DebuggerState __state, InstructionViewer __viewer,
		ContextThreadFrame __context)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		// Store for later usage
		this.state = __state;
		this.viewer = __viewer;
		this.context = __context;
		
		// Reduce border size
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Use flow layout for items
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setHgap(4);
		layout.setVgap(0);
		this.setLayout(layout);
		
		// If there is no state we cannot show the breakpoint button
		if (__state == null)
			this.breakpoint = null;
		
		// Show the breakpoint button
		else
		{
			// Setup button
			JButton breakpoint = new JButton();
			this.breakpoint = breakpoint;
			
			// Remove everything that makes it look like a button
			Utils.prettyTextButton(breakpoint);
			breakpoint.setIcon(Utils.tangoIcon("-"));
			
			// Add in the button
			this.add(breakpoint);
		}
		
		// Setup address
		JTextField address = new JTextField();
		Utils.prettyTextField(address);
		
		// Setup mnemonic
		JTextField mnemonic = new JTextField();
		Utils.prettyTextField(mnemonic);
		mnemonic.setFont(mnemonic.getFont().deriveFont(Font.BOLD));
		
		// Add everything in
		this.add(address);
		this.add(mnemonic);
		
		// Store for later
		this.address = address;
		this.description = mnemonic;
	}
	
	/**
	 * Updates the shown instruction.
	 *
	 * @since 2024/01/21
	 */
	public void shownUpdate()
	{
		InstructionViewer viewer = this.viewer;
		
		// Set address
		this.address.setText(String.format("@%d", viewer.address()));
		
		// Set mnemonic
		this.description.setText(viewer.mnemonic());
	}
}
