// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.CommLink;
import cc.squirreljme.jdwp.CommandSetThreadReference;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.function.Consumer;

/**
 * Thread storage information.
 *
 * @since 2024/01/20
 */
public class InfoThread
	extends Info
{
	/** Is this thread started? */
	protected final KnownValue<Boolean> isStarted =
		new KnownValue<>(Boolean.class);
	
	/** The name of the thread. */
	protected final KnownValue<String> threadName =
		new KnownValue<>(String.class);
	
	/**
	 * Initializes the base thread.
	 *
	 * @param __id The ID number of this thread.
	 * @since 2024/01/20
	 */
	public InfoThread(int __id)
	{
		super(__id, InfoKind.THREAD);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public void update(DebuggerState __state, Consumer<Info> __callback)
		throws NullPointerException
	{
		// Request name update
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			CommandSetThreadReference.NAME))
		{
			out.writeId(this.id);
			
			// Send it
			__state.send(out, (__ignored, __response) -> {
				// Set name
				this.threadName.set(__response.readString());
				
				// Call info callback
				if (__callback != null)
					__callback.accept(this);
			});
		}
	}
}
