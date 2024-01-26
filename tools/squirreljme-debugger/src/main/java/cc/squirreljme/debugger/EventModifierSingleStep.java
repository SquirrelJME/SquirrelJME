// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPEventModifierKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPStepDepth;
import cc.squirreljme.jdwp.JDWPStepSize;

/**
 * Single step event modifier.
 *
 * @since 2024/01/26
 */
public class EventModifierSingleStep
	implements EventModifier
{
	/** The step depth. */
	protected final JDWPStepDepth depth;
	
	/** The step size. */
	protected final JDWPStepSize size;
	
	/** The thread to step in. */
	protected final InfoThread thread;
	
	/**
	 * Initializes the single step.
	 *
	 * @param __thread The thread to step in.
	 * @param __depth The step depth.
	 * @param __size The step size.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public EventModifierSingleStep(InfoThread __thread, JDWPStepDepth __depth,
		JDWPStepSize __size)
		throws NullPointerException
	{
		if (__thread == null || __depth == null || __size == null)
			throw new NullPointerException("NARG");
		
		this.thread = __thread;
		this.depth = __depth;
		this.size = __size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public void write(DebuggerState __debuggerState, JDWPPacket __packet)
	{
		// Single step in this thread
		__packet.writeByte(JDWPEventModifierKind.CALL_STACK_STEPPING
			.debuggerId());
		__packet.writeId(this.thread.id);
		__packet.writeInt(this.size.debuggerId());
		__packet.writeInt(this.depth.debuggerId());
	}
}
