// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Command set for thread support.
 *
 * @since 2021/03/13
 */
public enum ThreadReferenceCommandSet
	implements JDWPCommand
{
	/** Thread name. */
	NAME(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPThread thread = __controller.state.threads.get(
				__packet.readId());
			
			// Thread is missing or otherwise invalid?
			if (thread == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_THREAD);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// This just uses the object name of the thread, whatever that
			// may be for simplicity and mapping
			rv.writeString(thread.toString());
			
			return rv;
		}
	},
	
	/** Status of the thread. */
	STATUS(4)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Thread is missing or otherwise invalid?
			JDWPThread thread = __controller.state.threads.get(
				__packet.readId());
			if (thread == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_THREAD);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Consider all threads as running
			rv.writeInt(1);
			
			// If the thread is suspended, then it will be flagged as such
			rv.writeInt(thread.debuggerSuspend().query() > 0 ? 1 : 0);
			
			return rv;
		}
	},
	
	/** Frames. */
	FRAMES(6)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Thread is missing or otherwise invalid?
			JDWPThread thread = __controller.state.threads.get(
				__packet.readId());
			if (thread == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_THREAD);
			
			// Input for the packet
			int startFrame = __packet.readInt();
			int count = __packet.readInt();
			
			// Correct the frame count
			JDWPThreadFrame[] frames = thread.debuggerFrames();
			count = (count == -1 ? Math.max(0, frames.length - startFrame) :
				Math.min(count, frames.length - startFrame));
			
			// Start by writing the frame count
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(count);
			
			// Write each individual frame
			for (int i = startFrame, j = 0; j < count; i++, j++)
			{
				// Register this frame so it can be grabbed later
				JDWPThreadFrame frame = frames[i];
				__controller.state.frames.put(frame);
				
				// Write frame ID
				rv.writeId(frame);
				
				// Write the frame location
				JDWPClass classy = frame.debuggerAtClass();
				rv.writeByte(classy.debuggerClassType().id);
				rv.writeId(classy);
				
				// Write the method ID and the special index (address)
				JDWPMethod method = frame.debuggerAtMethod();
				rv.writeId(method);
				rv.writeLong(frame.debuggerAtIndex());
				
				// Make sure the class and methods are registered for later
				// retrieval
				__controller.state.classes.put(classy);
				__controller.state.methods.put(method);
			} 
			
			return rv;
		}
	},
	
	/** Frame count. */
	FRAME_COUNT(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPThread thread = __controller.state.threads.get(
				__packet.readId());
			
			// Thread is missing or otherwise invalid?
			if (thread == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_THREAD);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Return the frame count
			rv.writeInt(thread.debuggerFrames().length);
			
			return rv;
		}
	},
	
	/** Suspension count for each thread. */
	SUSPEND_COUNT(12)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPThread thread = __controller.state.threads.get(
				__packet.readId());
			
			// Thread is missing or otherwise invalid?
			if (thread == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_THREAD);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// This just uses the object name of the thread, whatever that
			// may be for simplicity and mapping
			rv.writeInt(thread.debuggerSuspend().query());
			
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	ThreadReferenceCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
