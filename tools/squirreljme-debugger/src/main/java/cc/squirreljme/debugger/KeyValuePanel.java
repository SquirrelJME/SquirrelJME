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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A basic panel that contains a key and value.
 *
 * @since 2024/01/22
 */
public class KeyValuePanel
	extends JPanel
{
	/** The key component. */
	protected final JComponent key;
	
	/** The value component. */
	protected final JComponent value;
	
	/**
	 * Initializes the key/value panel.
	 *
	 * @param __key The key to use.
	 * @param __value The value to use.
	 * @since 2024/01/22
	 */
	public KeyValuePanel(JComponent __key, JComponent __value)
		throws NullPointerException
	{
		if (__key == null || __value == null)
			throw new NullPointerException("NARG");
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		// Key item
		constraints.weightx = 0.25;
		constraints.gridx = 0;
		constraints.fill = GridBagConstraints.NONE;
		this.add(__key, constraints);
		
		// Value item
		constraints.weightx = 0.75;
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(__value, constraints);
		
		// Store for later
		this.key = __key;
		this.value = __value;
	}
}
