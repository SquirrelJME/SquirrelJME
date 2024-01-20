// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.JDialog;

/**
 * Thread inspector.
 *
 * @since 2024/01/20
 */
public class InspectThread
	extends Inspect<InfoThread>
{
	/**
	 * Initializes the thread inspector.
	 *
	 * @param __owner The owning frame.
	 * @param __state The debugging state.
	 * @param __what What is being inspected.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public InspectThread(PrimaryFrame __owner, DebuggerState __state,
		InfoThread __what)
		throws NullPointerException
	{
		super(__owner, __state, __what);
		
		// Track these
		this.addTrack("Started?", __what.isStarted);
		
		// Update inspection
		this.update();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	protected void updateInternal()
	{
	}
}
