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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
	
	/** The address of this instruction. */
	protected final JTextField address;
	
	/** The label which describes this. */
	protected final JTextField description;
	
	/**
	 * Initializes the instruction line.
	 *
	 * @param __viewer The viewer to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public ShownInstruction(InstructionViewer __viewer)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		// Store for later usage
		this.viewer = __viewer;
		
		// Reduce border size
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Use flow layout for items
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setHgap(4);
		layout.setVgap(0);
		this.setLayout(layout);
		
		// Setup address
		JTextField address = new JTextField();
		ShownInstruction.__pretty(address);
		
		// Setup mnemonic
		JTextField mnemonic = new JTextField();
		ShownInstruction.__pretty(mnemonic);
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
	
	/**
	 * Makes the label look better.
	 *
	 * @param __label The label to edit.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private static void __pretty(JTextField __label)
		throws NullPointerException
	{
		if (__label == null)
			throw new NullPointerException("NARG");
		
		// Reduce padding
		__label.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Align to the left
		__label.setHorizontalAlignment(SwingConstants.LEFT);
		
		// Allow it to be copied
		__label.setEditable(false);
		__label.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Use a better font for the label
		Font descFont = __label.getFont();
		__label.setFont(new Font("monospaced",
			descFont.getStyle(), descFont.getSize()));
	}
}
