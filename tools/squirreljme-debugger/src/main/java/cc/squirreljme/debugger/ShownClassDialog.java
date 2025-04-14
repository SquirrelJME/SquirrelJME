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
import java.awt.Window;
import javax.swing.JDialog;

/**
 * Dialog for showing classes.
 *
 * @since 2024/01/22
 */
public class ShownClassDialog
	extends JDialog
{
	/**
	 * Initializes the class viewing dialog.
	 *
	 * @param __owner The owning frame.
	 * @param __state The optional debugger state.
	 * @param __viewer The viewer to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public ShownClassDialog(Window __owner, DebuggerState __state,
		ClassViewer __viewer)
		throws NullPointerException
	{
		if (__owner == null || __viewer == null)
			throw new NullPointerException("NARG");
		
		// Title
		this.setTitle("Showing " + __viewer.thisName());
		
		// Set window icon
		Utils.setIcon(this);
		
		// Minimum size for readability
		this.setMinimumSize(new Dimension(640, 480));
		
		// Border layout is cleaner
		this.setLayout(new BorderLayout());
		
		// Table of contents view
		ShownTableOfContents toc = new ShownTableOfContents();
		
		// Add class viewer
		ShownClass shownClass = new ShownClass(__viewer, toc, __state);
		this.add(shownClass, BorderLayout.CENTER);
		
		// Table of contents
		this.add(toc, BorderLayout.LINE_START);
		
		// Pack
		this.pack();
	}
}
