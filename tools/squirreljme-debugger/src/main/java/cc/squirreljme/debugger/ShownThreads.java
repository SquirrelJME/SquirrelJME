// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import javax.swing.JPanel;

/**
 * Shows all the running threads within the virtual machine and allows
 * them to be selected accordingly.
 *
 * @since 2024/01/24
 */
public class ShownThreads
	extends JPanel
{
	/** The debugger state. */
	protected final DebuggerState state;
	
	/**
	 * Initializes the thread shower.
	 *
	 * @param __state The debugger state.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public ShownThreads(DebuggerState __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
}
