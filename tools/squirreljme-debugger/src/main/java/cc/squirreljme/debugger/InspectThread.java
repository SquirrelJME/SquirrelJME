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
import java.awt.Window;
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
	 * @param __info What is being inspected.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public InspectThread(Window __owner, DebuggerState __state,
		InfoThread __info)
		throws NullPointerException
	{
		super(__owner, __state, __info);
		
		// Track these
		this.addTrack("Started?", __info.isStarted);
		this.addTrack("Dead?", __info.isDead);
		this.addTrack("Name", __info.threadName);
		this.addTrack("Suspend Count", __info.suspendCount);
		
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
