// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Represents a Swing Display.
 *
 * @since 2020/07/01
 */
public final class SwingDisplay
	implements UIDisplayBracket
{
	/** The screen instance. */
	@SuppressWarnings({"StaticVariableMayNotBeInitialized", 
		"NonConstantFieldWithUpperCaseName"})
	private static SwingDisplay _DISPLAY;
	
	/** The frame that makes up this display. */
	protected final JFrame frame;
	
	/**
	 * Initializes the display.
	 * 
	 * @since 2020/07/01
	 */
	public SwingDisplay()
	{
		JFrame frame = new JFrame();
		
		// Branding!
		frame.setTitle("SquirrelJME");
		
		// Use a basic layout for the form
		frame.setLayout(new BorderLayout());
		
		this.frame = frame;
	}
	
	/**
	 * Shows the given form on the display.
	 * 
	 * @param __form The form to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public void show(SwingForm __form)
		throws NullPointerException
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		JFrame frame = this.frame;
		
		// Remove all components from the frame and make our form the only one
		frame.setLayout(new BorderLayout());
		frame.add(__form.formPanel, BorderLayout.CENTER);
		
		// Minimize space used
		frame.pack();
		
		// If the frame was not already visible then just center it on screen 
		boolean alreadyVisible = frame.isVisible();
		if (!alreadyVisible)
			frame.setLocationRelativeTo(null);
		
		// Now show it
		frame.setVisible(true);
	}
	
	/**
	 * Returns the display instance.
	 * 
	 * @return The instance of the display.
	 * @since 2020/07/01
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static SwingDisplay getInstance()
	{
		// Already cached?
		SwingDisplay rv = SwingDisplay._DISPLAY;
		if (rv != null)
			return rv;
		
		// Create it?
		rv = new SwingDisplay();
		SwingDisplay._DISPLAY = rv;
		
		return rv;
	}
}
