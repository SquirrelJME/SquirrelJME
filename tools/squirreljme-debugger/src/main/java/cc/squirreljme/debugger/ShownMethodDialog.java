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
 * Dialog for showing a method.
 *
 * @since 2024/01/24
 */
public class ShownMethodDialog
	extends JDialog
{
	/**
	 * Initializes the method viewing dialog.
	 *
	 * @param __owner The owning frame.
	 * @param __state The optional debug state.
	 * @param __viewer The viewer to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public ShownMethodDialog(Window __owner, DebuggerState __state,
		MethodViewer __viewer)
		throws NullPointerException
	{
		if (__owner == null || __viewer == null)
			throw new NullPointerException("NARG");
		
		// Title
		this.setTitle("Showing " + __viewer.methodNameAndType());
		
		// Set window icon
		Utils.setIcon(this);
		
		// Minimum size for readability
		this.setMinimumSize(new Dimension(640, 480));
		
		// Border layout is cleaner
		this.setLayout(new BorderLayout());
		
		// Add class viewer
		ShownMethod shownMethod = new ShownMethod(__state, __viewer);
		this.add(shownMethod, BorderLayout.CENTER);
		
		// Pack
		this.pack();
	}
}
