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
 * Command set for stack frames.
 *
 * @since 2021/03/15
 */
public enum StackFrameCommandSet
	implements JDWPCommand
{
	/** Get stack frame values. */
	GET_VALUES(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
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
			
			// Frame is missing or otherwise invalid?
			JDWPThreadFrame frame = __controller.state.frames.get(
				__packet.readId());
			if (frame == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_FRAME_ID);
			
			// Read in the slot table
			int numSlots = __packet.readInt();
			int[] wantSlot = new int[numSlots];
			for (int i = 0; i < numSlots; i++)
			{
				wantSlot[i] = __packet.readInt();
				
				// Ignore the type that was requested
				__packet.readByte();
			}
			
			// Always reply with the same number of slots
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(numSlots);
			for (int i = 0; i < numSlots; i++)
			{
				// If we ever get any object values record them, otherwise
				// the debugger will be incapable of figuring this out
				Object value = frame.debuggerRegisterGetValue(false,
					wantSlot[i]);
				if (value instanceof JDWPObject)
					__controller.state.objects.put((JDWPObject)value);
				
				rv.writeValue(value);
			}
			
			return rv;
		}
	},
	
	/** The current object of the current frame. */
	THIS_OBJECT(3)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
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
			
			// Frame is missing or otherwise invalid?
			JDWPThreadFrame frame = __controller.state.frames.get(
				__packet.readId());
			if (frame == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_FRAME_ID);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Static and native methods always return null
			int mFlags = frame.debuggerAtMethod().debuggerMemberFlags();
			if (0 != (mFlags & (StackFrameCommandSet._FLAG_STATIC |
				StackFrameCommandSet._FLAG_NATIVE)))
				rv.writeId(null);
			
			else
			{
				// This may be a boxed value like integer
				Object val = frame.debuggerRegisterGetValue(
					false, 0);
				
				// Register this if it is an object because we want the
				// debugger able to grab information about this
				if (val instanceof JDWPObject)
					__controller.state.objects.put((JDWPObject)val);
				
				// Write the ID of the object, if it is one
				if (val instanceof JDWPObjectLike)
					rv.writeId((JDWPObjectLike)val);
				else
					rv.writeId(null);
			}
			
			return rv;
		}
	},
		
	/* End. */
	;
	
	/** Static method. */
	static final int _FLAG_STATIC =
		0x0008;
	
	/** Native method. */
	static final int _FLAG_NATIVE =
		0x0100;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/15
	 */
	StackFrameCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/15
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
