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
import cc.squirreljme.jdwp.JDWPCommandSetStackFrame;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPValueTag;

/**
 * Tracks information on a single frame within a thread.
 *
 * @since 2024/01/25
 */
public class InfoFrame
	extends Info
{
	/** Possible tags used. */
	private static final JDWPValueTag[] _TAGS =
		JDWPValueTag.values();
	
	/** The thread this frame is in. */
	protected final InfoThread inThread;
	
	/** The location of the frame. */
	protected final FrameLocation location;
	
	/** Frame variables. */
	protected final KnownValue<InfoFrameLocals> variables;
	
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
		this.variables = new KnownValue<InfoFrameLocals>(InfoFrameLocals.class,
			this::__updateVariables);
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
		DebuggerState state = this.internalState();
		InfoMethod inMethod = this.location.inMethod;
		return String.format("%s:%s @ %d",
			(inMethod == null ? "<opaque>" :
				inMethod.name.getOrUpdateSync(state)),
			(inMethod == null ? "<opaque>" :
				inMethod.type.getOrUpdateSync(state)),
			this.location.index);
	}
	
	/**
	 * Attempts variable update state and chains accordingly on failure.
	 *
	 * @param __state The state to update within.
	 * @param __inThread The thread this is in.
	 * @param __inFrame The frame this is in.
	 * @param __value The resultant values.
	 * @param __locals The local variable.
	 * @param __varIndex The local variable index.
	 * @param __tagType The tag type to request.
	 * @param __varLimit The variable limit.
	 * @param __sync The callback called when the variable has been updated.
	 * @since 2024/01/26
	 */
	private void __updateChain(DebuggerState __state, JDWPId __inThread,
		JDWPId __inFrame, KnownValue<InfoFrameLocals> __value,
		InfoFrameLocals __locals, int __varIndex, int __tagType,
		int __varLimit, KnownValueCallback<InfoFrameLocals> __sync)
	{
		// Determine which tag we are on
		JDWPValueTag[] tags = InfoFrame._TAGS;
		if (__tagType >= tags.length)
			return;
		JDWPValueTag tag = tags[__tagType];
		
		// Request variables
		try (JDWPPacket out = __state.request(JDWPCommandSet.STACK_FRAMES,
			JDWPCommandSetStackFrame.GET_VALUES))
		{
			// Current frame and thread
			out.writeId(__inThread);
			out.writeId(__inFrame);
			
			// Request a single slot with the tag
			out.writeInt(1);
			out.writeInt(__varIndex);
			out.writeByte(tag.tag);
			
			// Send it
			__state.sendKnown(out, __value, __sync,
				(__ignored, __reply) -> {
					// Only read and set the first value found
					int numValues = __reply.readInt();
					if (numValues > 0)
						__locals.set(__varIndex, __reply.readValue());
					
					// We got the value here, so move onto the next local
					// variable index
					if (__varIndex < InfoFrameLocals.MAX_LOCALS - 1)
						this.__updateChain(__state, __inThread, __inFrame,
							__value, __locals, __varIndex + 1,
							0,
							Math.min(InfoFrameLocals.MAX_LOCALS,
								__varIndex + InfoFrameLocals._LOCAL_BUMP),
							__sync);
				}, (__ignored, __reply) -> {
					// If the slot is invalid, do not go down the chain
					// to any more variables
					if (__reply.hasError(JDWPErrorType.INVALID_SLOT))
					{
						// We do not want to get stuck on a single slot so
						// try to skip it
						if (__varIndex < __varLimit)
							this.__updateChain(__state, __inThread, __inFrame,
								__value, __locals, __varIndex + 1,
								0, __varLimit, __sync);
						return;
					}
					
					// When updating the chain keep the current variable limit
					this.__updateChain(__state, __inThread, __inFrame,
						__value, __locals, __varIndex, __tagType + 1,
						__varLimit, __sync);
				});
		}
	}
	
	/**
	 * Updates variables.
	 *
	 * @param __state The current state.
	 * @param __value The current value.
	 * @param __sync The callback to execute.
	 * @since 2024/01/26
	 */
	private void __updateVariables(DebuggerState __state,
		KnownValue<InfoFrameLocals> __value,
		KnownValueCallback<InfoFrameLocals> __sync)
	{
		// Get the locals to update
		InfoFrameLocals locals = __value.get();
		if (locals == null)
		{
			locals = new InfoFrameLocals();
			__value.set(locals);
		}
		
		// Get current thread and frame IDs
		JDWPId inThread = this.inThread.id;
		JDWPId inFrame = this.id;
		
		// We need to ask the virtual machine for a ton of information to get
		// the proper local variables... This is a very expensive operation
		// so to reduce load this sequentially obtains the variables up
		// until it cannot any further
		this.__updateChain(__state, inThread, inFrame, __value,
			locals, 0, 0, InfoFrameLocals._LOCAL_BUMP,
			__sync);
	}
}
