// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPCommandSetThreadReference;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
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
	protected final KnownValue<Boolean> isStarted;
	
	/** The name of the thread. */
	protected final KnownValue<String> threadName;
	
	/** Is this thread dead? */
	protected final KnownValue<Boolean> isDead;
	
	/** The current suspend count of the thread. */
	protected final KnownValue<Integer> suspendCount;
	
	/** Current stack frames. */
	protected final KnownValue<InfoFrame[]> frames;
	
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
		
		this.isStarted = new KnownValue<Boolean>(Boolean.class,
			false, KnownValueUpdater.IGNORED);
		this.isDead = new KnownValue<Boolean>(Boolean.class,
			false, KnownValueUpdater.IGNORED);
		
		this.threadName = new KnownValue<String>(String.class,
			this::__updateThreadName);
		this.suspendCount = new KnownValue<Integer>(Integer.class,
			this::__updateSuspendCount);
		this.frames = new KnownValue<InfoFrame[]>(InfoFrame[].class,
			this::__updateFrames);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public int compareTo(@NotNull Info __o)
	{
		if (__o == this || !(__o instanceof InfoThread))
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
		{
			int rv = aName.compareTo(bName);
			if (rv != 0)
				return rv;
		}
		
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
		String name = this.threadName.getOrUpdateSync(this.internalState());
		String result = String.format("%s (%d)",
			(name != null ? name : "Unknown?"), this.id.id);
		
		if (this.isDead.getOrDefault(false))
			return "DEAD " + result;
		return result;
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
	 * Updates the stack frames of this thread.
	 *
	 * @param __state The current debugger state.
	 * @param __value The current value.
	 * @param __sync The callback to execute.
	 * @since 2024/01/25
	 */
	private void __updateFrames(DebuggerState __state,
		KnownValue<InfoFrame[]> __value,
		KnownValueCallback<InfoFrame[]> __sync)
	{
		// Request frames
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.FRAMES))
		{
			// Request all frames
			out.writeId(this.id);
			out.writeInt(0);
			out.writeInt(-1);
			
			// Send it
			__state.sendKnown(out, __value, __sync,
				(__ignored, __reply) -> {
					// Read in frame count
					int numFrames = __reply.readInt();
					
					// Load in all frames
					InfoFrame[] result = new InfoFrame[numFrames];
					for (int i = 0; i < numFrames; i++)
					{
						JDWPId frameId = __reply.readId(JDWPIdKind.FRAME_ID);
						FrameLocation location = __state.readLocation(
							this, __reply);
						
						// Build frame
						result[i] = new InfoFrame(__state, frameId,
							this, location);
					}
					
					// Store frames
					__value.set(result);
				}, ReplyHandler.IGNORED);
		}
	}
	
	/**
	 * Updates the suspend count of the current thread.
	 *
	 * @param __state The debugger state.
	 * @param __value The current value.
	 * @param __sync The callback to execute.
	 * @since 2024/01/25
	 */
	private void __updateSuspendCount(DebuggerState __state,
		KnownValue<Integer> __value, KnownValueCallback<Integer> __sync)
	{
		// Request name update
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.SUSPEND_COUNT))
		{
			out.writeId(this.id);
			
			// Send it
			__state.sendKnown(out, __value, __sync,
				(__ignored, __reply) -> {
					__value.set(__reply.readInt());
				}, ReplyHandler.IGNORED);
		}
	}
	
	/**
	 * Updates the thread name.
	 *
	 * @param __state The debugger state.
	 * @param __value The current value.
	 * @param __sync The callback to execute.
	 * @since 2024/01/25
	 */
	private void __updateThreadName(DebuggerState __state,
		KnownValue<String> __value, KnownValueCallback<String> __sync)
	{
		// Request name update
		try (JDWPPacket out = __state.request(JDWPCommandSet.THREAD_REFERENCE,
			JDWPCommandSetThreadReference.NAME))
		{
			out.writeId(this.id);
			
			// Send it
			__state.sendKnown(out, __value, __sync,
				(__ignored, __response) -> {
					// Set name
					__value.set(__response.readString());
				}, (__ignored, __response) -> {
					this.dispose();
				});
		}
	}
}
