// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSetThreadReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPId;
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
	protected final KnownValue<String> threadName;
	
	/** Is this thread dead? */
	protected KnownValue<Boolean> isDead =
		new KnownValue<>(Boolean.class, false);
	
	/** The current suspend count of the thread. */
	protected KnownValue<Integer> suspendCount;
	
	/**
	 * Initializes the base thread.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this thread.
	 * @since 2024/01/20
	 */
	public InfoThread(DebuggerState __state, JDWPId __id)
	{
		super(__state, __id, InfoKind.THREAD);
		
		this.threadName = new KnownValue<String>(String.class,
			this::__updateThreadName);
		this.suspendCount = new KnownValue<Integer>(Integer.class,
			this::__updateSuspendCount);
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
		String name = this.threadName.getOrUpdate(this.internalState());
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
	public boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		// Consider as valid unless otherwise
		return true;
	}
	
	/**
	 * Updates the suspend count of the current thread.
	 *
	 * @param __state The debugger state.
	 * @param __value The current value.
	 * @since 2024/01/25
	 */
	private void __updateSuspendCount(DebuggerState __state,
		KnownValue<Integer> __value)
	{
		// Request name update
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.SUSPEND_COUNT))
		{
			out.writeId(this.id);
			
			// Send it
			__state.sendThenWait(out, Utils.TIMEOUT,
				(__ignored, __reply) -> {
					__value.set(__reply.readInt());
				}, (__ignored, __reply) -> {
				});
		}
	}
	
	/**
	 * Updates the thread name.
	 *
	 * @param __state The debugger state.
	 * @param __value The current value.
	 * @since 2024/01/25
	 */
	private void __updateThreadName(DebuggerState __state,
		KnownValue<String> __value)
	{
		// Request name update
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.NAME))
		{
			out.writeId(this.id);
			
			// Send it
			__state.send(out, (__ignored, __response) -> {
				// Thread no longer valid?
				if (__response.hasError(JDWPErrorType.INVALID_THREAD))
				{
					this.dispose();
					return;
				}
				
				// Another error
				else if (__response.hasError())
					return;
				
				// Set name
				this.threadName.set(__response.readString());
			});
		}
	}
}
