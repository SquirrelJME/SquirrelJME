// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.CommandSetThreadReference;
import cc.squirreljme.jdwp.ErrorType;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPPacket;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

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
	
	/** Is this thread dead? */
	protected KnownValue<Boolean> isDead =
		new KnownValue<>(Boolean.class, false);
	
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
	public int compareTo(@NotNull Info __o)
	{
		if (!(__o instanceof InfoThread))
			return super.compareTo(__o);
		
		InfoThread other = (InfoThread)__o;
		
		// Compare by deadness
		boolean aDead = this.isDead.getOrDefault(false);
		boolean bDead = other.isDead.getOrDefault(false);
		if (aDead != bDead)
			return (aDead ? 1 : -1);
		
		// Compare name next
		String aName = this.threadName.getOrDefault(null);
		String bName = this.threadName.getOrDefault(null);
		if (aName != null && bName != null)
			return aName.compareTo(bName);
		
		// Fallback to normal sort
		return super.compareTo(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	protected String internalString()
	{
		String name = this.threadName.getOrDefault("Unknown?");
		if (this.isDead.getOrDefault(false))
			return "DEAD " + name;
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return
	 * @since 2024/01/20
	 */
	@Override
	public boolean internalUpdate(DebuggerState __state,
		Consumer<Info> __callback)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// Request name update
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			CommandSetThreadReference.NAME))
		{
			out.writeId(this.id);
			
			// Send it
			__state.send(out, (__ignored, __response) -> {
				// Thread no longer valid?
				if (__response.hasError(ErrorType.INVALID_THREAD))
				{
					this.dispose();
					return;
				}
				
				// Another error
				else if (__response.hasError())
					return;
				
				// Set name
				this.threadName.set(__response.readString());
				
				// Call info callback
				if (__callback != null)
					__callback.accept(this);
			});
		}
		
		// Consider as valid unless otherwise
		return true;
	}
}
