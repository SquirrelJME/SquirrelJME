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
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
	
	/** The index of this instruction. */
	protected final int index;
	
	/** The current pointer. */
	protected final JButton pointer;
	
	/** Arguments to the instruction. */
	private final JButton[] _args;
	
	/**
	 * Initializes the instruction line.
	 *
	 * @param __state The optional state for tracking.
	 * @param __viewer The viewer to use.
	 * @param __context The optional thread debugging context.
	 * @param __index The index of this instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public ShownInstruction(DebuggerState __state, InstructionViewer __viewer,
		ContextThreadFrame __context, int __index)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		// Store for later usage
		this.state = __state;
		this.viewer = __viewer;
		this.context = __context;
		this.index = __index;
		
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
		
		// If there is a context we can show the current position
		if (__context == null)
			this.pointer = null;
		else
		{
			// Add current frame pointer button
			JButton pointer = new JButton();
			this.pointer = pointer;
			Utils.prettyTextButton(pointer);
			pointer.setIcon(Utils.tangoIcon("-"));
			this.add(pointer);
		}
		
		// Setup address
		JTextField address = new JTextField();
		Utils.prettyTextField(address);
		
		// Setup mnemonic
		JTextField mnemonic = new JTextField();
		Utils.prettyTextField(mnemonic);
		mnemonic.setFont(mnemonic.getFont().deriveFont(Font.BOLD));
		
		// Get arguments to the instruction
		Object[] rawArgs = __viewer.arguments();
		int argCount = rawArgs.length;
		JButton[] args = new JButton[argCount];
		for (int i = 0; i < argCount; i++)
		{
			JButton single = new JButton(
				Objects.toString(rawArgs[i]));
			Utils.prettyTextButton(single);
			args[i] = single;
		}
		
		// Add everything in
		this.add(address);
		this.add(mnemonic);
		for (JButton arg : args)
			this.add(arg);
		
		// Store for later
		this.address = address;
		this.description = mnemonic;
		this._args = args;
		
		// Request that everything gets updated
		Utils.swingInvoke(() -> this.shownUpdate());
	}
	
	/**
	 * Updates the shown instruction.
	 *
	 * @return If this is the instruction the pointer is at.
	 * @since 2024/01/21
	 */
	public boolean shownUpdate()
	{
		boolean isAt = false;
		InstructionViewer viewer = this.viewer;
		
		// What is our PC address?
		int pcAddr = viewer.address();
		int index = this.index;
		
		// Update the context-sensitive info
		ContextThreadFrame context = this.context;
		if (context != null)
		{
			JButton pointer = this.pointer;
			
			// How is the location interpreted?
			FrameLocationInterpret interpret =
				this.state._locationInterpret;
			
			// Is there a valid location
			FrameLocation location = context.getLocation();
			if (location != null)
			{
				if (interpret == FrameLocationInterpret.ADDRESS)
					isAt = (location.index == pcAddr);
				else
					isAt = (location.index == index);
			}
			
			// Is this the location we are at?
			if (isAt)
				pointer.setIcon(Utils.tangoIcon("go-next"));
			else
				pointer.setIcon(Utils.tangoIcon("-"));
			
			// Make sure the pointer is updated
			Utils.revalidate(pointer);
		}
		
		// Set address
		this.address.setText(String.format("@%d", pcAddr));
		
		// Set mnemonic
		this.description.setText(viewer.mnemonic());
		
		// Repaint
		Utils.revalidate(this.address);
		Utils.revalidate(this.description);
		Utils.revalidate(this);
		
		// Are we here?
		return isAt;
	}
}
