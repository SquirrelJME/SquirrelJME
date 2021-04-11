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
public enum CommandSetStackFrame
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
			JDWPThread thread = __controller.state.oldThreads.get(
				__packet.readId());
			if (thread == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_THREAD);
			
			// Frame is missing or otherwise invalid?
			JDWPThreadFrame frame = __controller.state.oldFrames.get(
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
				try (JDWPValue value = __controller.__value())
				{
					// If we ever get any object values record them, otherwise
					// the debugger will be incapable of figuring this out
					if (!frame.debuggerRegisterGetValue(false,
						wantSlot[i], value))
						rv.writeVoid();
					else
					{
						rv.writeValue(value, (String)null, false);
						
						// Store object for later use
						Object rawVal = value.get();
						if (rawVal instanceof JDWPObject)
							__controller.state.oldObjects.put((JDWPObject)rawVal);
					}
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
			JDWPThread thread = __controller.state.oldThreads.get(
				__packet.readId());
			if (thread == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_THREAD);
			
			// Frame is missing or otherwise invalid?
			JDWPThreadFrame frame = __controller.state.oldFrames.get(
				__packet.readId());
			if (frame == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_FRAME_ID);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Static and native methods always return null
			int mFlags = frame.debuggerAtMethod().debuggerMemberFlags();
			if (0 != (mFlags & (CommandSetStackFrame._FLAG_STATIC |
				CommandSetStackFrame._FLAG_NATIVE)))
				rv.writeId(null);
			
			// Write self value
			else
				try (JDWPValue value = __controller.__value())
				{
					// If this value is an object we need to register it for
					// future grabbing
					if (!frame.debuggerRegisterGetValue(
					false, 0, value))
						rv.writeVoid();
					else
					{
						rv.writeValue(value, (String)null, false);
						
						// Store object for later use
						Object rawVal = value.get();
						if (rawVal instanceof JDWPObject)
							__controller.state.oldObjects.put((JDWPObject)rawVal);
					}
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
	CommandSetStackFrame(int __id)
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
