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
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Shows a thread's stack trace.
 *
 * @since 2024/01/24
 */
public class ShownThread
	extends JPanel
{
	/** The thread being viewed. */
	protected final InfoThread thread;
	
	/** The state of the debugger. */
	protected final DebuggerState state;
	
	/** Current suspension state? */
	protected final JLabel suspendLabel;
	
	/**
	 * Initializes the shown thread.
	 *
	 * @param __state The debugger state.
	 * @param __thread The thread to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public ShownThread(DebuggerState __state, InfoThread __thread)
		throws NullPointerException
	{
		if (__state == null || __thread == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.thread = __thread;
		
		// Border layout is clean
		this.setLayout(new BorderLayout());
		
		// Add suspension state label
		JLabel suspendLabel = new JLabel();
		this.suspendLabel = suspendLabel;
		this.add(suspendLabel, BorderLayout.PAGE_START);
	}
	
	/**
	 * Updates the shown information.
	 *
	 * @since 2024/01/25
	 */
	public void update()
	{
		InfoThread thread = this.thread;
		JLabel suspendLabel = this.suspendLabel;
		DebuggerState state = this.state;
		
		// Inform of the current thread suspension state
		int suspendCount = thread.suspendCount.update(state, 0);
		if (suspendCount > 0)
			suspendLabel.setText(String.format("Suspended %d times.",
				suspendCount));
		else
			suspendLabel.setText("Running");
	}
}
