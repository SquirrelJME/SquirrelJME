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
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.jdwp.JDWPValueTag;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
		new JDWPValueTag[]
		{
			JDWPValueTag.INTEGER,
			JDWPValueTag.OBJECT,
			JDWPValueTag.LONG,
			JDWPValueTag.SHORT,
			JDWPValueTag.CHARACTER,
			JDWPValueTag.BYTE,
			JDWPValueTag.BOOLEAN,
			JDWPValueTag.FLOAT,
			JDWPValueTag.DOUBLE,
		};
	
	/** Item success. */
	private static final __Success__ _SUCCESS =
		new __Success__();
	
	/** Item failed. */
	private static final NoSuchElementException _FAILED =
		new NoSuchElementException();
	
	/** The thread this frame is in. */
	protected final InfoThread inThread;
	
	/** The location of the frame. */
	protected final FrameLocation location;
	
	/** Frame variables. */
	protected final KnownValue<JDWPValue[]> variables;
	
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
		this.variables = new KnownValue<JDWPValue[]>(JDWPValue[].class,
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
		return this.location.toString();
	}
	
	/**
	 * Updates variables.
	 *
	 * @param __state The current state.
	 * @param __value The current value.
	 * @since 2024/01/26
	 */
	private void __updateVariables(DebuggerState __state,
		KnownValue<JDWPValue[]> __value)
	{
		// Native/abstract methods cannot have variables
		InfoMethod inMethod = this.location.inMethod;
		if (inMethod.flags.isNative() || inMethod.flags.isAbstract())
		{
			__value.set(new JDWPValue[0]);
			return;
		}
		
		JDWPId threadId = this.inThread.id;
		JDWPId frameId = this.id;
		
		// For JDWP there is no real way to get an accurate set of variables
		// directly, like it does not tell us the actual type that is there, so
		// we have to do some major probing to try to get that information...
		List<JDWPValue> result = new ArrayList<>();
		JDWPValue[] object = new JDWPValue[1];
		for (int i = 0; i < 255; i++)
			try
			{
				result.add(this.__variableAttempt(
					__state, threadId, frameId, i, object));
			}
			catch (NoSuchElementException ignored)
			{
				break;
			}
		
		// Set value
		__value.set(result.toArray(new JDWPValue[result.size()]));
	}
	
	/**
	 * Attempts to read a local variable.
	 *
	 * @param __state The current state.
	 * @param __threadId The thread ID.
	 * @param __frameId The frame ID.
	 * @param __varIndex The variable index.
	 * @param __object The resultant object.
	 * @return The value at the given location.
	 * @throws NoSuchElementException If no such item is here.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	private JDWPValue __variableAttempt(DebuggerState __state,
		JDWPId __threadId, JDWPId __frameId, int __varIndex,
		JDWPValue[] __object)
		throws NoSuchElementException, NullPointerException
	{
		if (__state == null || __threadId == null || __frameId == null ||
			__object == null)
			throw new NullPointerException("NARG");
		
		// Try to read with all tags
		JDWPValueTag[] tags = InfoFrame._TAGS;
		for (JDWPValueTag tag : tags)
			try
			{
				return this.__variableAttempt(__state, __threadId, __frameId,
					__varIndex, tag, __object);
			}
			catch (__Success__ __ignored)
			{
				synchronized (__object)
				{
					return __object[0];
				}
			}
			catch (NoSuchElementException __ignored)
			{
			}
		
		// Failed
		throw InfoFrame._FAILED;
	}
	
	/**
	 * Attempts to read a local variable.
	 *
	 * @param __state The debugger state.
	 * @param __threadId The thread ID.
	 * @param __frameId The frame ID.
	 * @param __varIndex The variable index.
	 * @param __object The resultant object.
	 * @return The value at the given location.
	 * @throws NoSuchElementException If no such item is here.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	private JDWPValue __variableAttempt(DebuggerState __state,
		JDWPId __threadId, JDWPId __frameId, int __varIndex,
		JDWPValueTag __tag, JDWPValue[] __object)
		throws NoSuchElementException, NullPointerException
	{
		if (__state == null || __threadId == null || __frameId == null ||
			__tag == null || __object == null)
			throw new NullPointerException("NARG");
	
		// Request variables
		try (JDWPPacket out = __state.request(JDWPCommandSet.STACK_FRAMES,
			JDWPCommandSetStackFrame.GET_VALUES))
		{
			// Current frame and thread
			out.writeId(__threadId);
			out.writeId(__frameId);
			
			// Request a single slot with the tag
			out.writeInt(1);
			out.writeInt(__varIndex);
			out.writeByte(__tag.tag);
			
			// Send it
			__state.sendThenWait(out, Utils.SHORT_TIMEOUT,
				(__ignored, __reply) -> {
					// The number of read values
					int numValues = __reply.readInt();
					JDWPValue value = __reply.readValue();
					
					// Set value
					synchronized (__object)
					{
						__object[0] = value;
					}
					
					// Success
					throw InfoFrame._SUCCESS;
				}, (__ignored, __reply) -> {
					throw InfoFrame._FAILED;
				});
		}
		
		// Failed
		throw InfoFrame._FAILED;
	}
}
