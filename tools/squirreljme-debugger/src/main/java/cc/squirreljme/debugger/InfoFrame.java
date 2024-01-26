// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Tracks information on a single frame within a thread.
 *
 * @since 2024/01/25
 */
public class InfoFrame
	extends Info
{
	/** The thread this frame is in. */
	protected final InfoThread inThread;
	
	/** The location of the frame. */
	protected final FrameLocation location;
	
	/**
	 * Initializes the frame information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @param __thread The thread this is in.
	 * @param __location The location of this frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public InfoFrame(DebuggerState __state, JDWPId __id, InfoThread __thread,
		FrameLocation __location)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.FRAME);
		
		if (__thread == null || __location == null)
			throw new NullPointerException("NARG");
		
		this.inThread = __thread;
		this.location = __location;
	}
	
	/**
	 * Returns the current method.
	 *
	 * @return The current method this is in.
	 * @since 2024/01/25
	 */
	public InfoMethod inMethod()
	{
		return this.location.inMethod;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	protected String internalString()
	{
		return this.location.toString();
	}
}
