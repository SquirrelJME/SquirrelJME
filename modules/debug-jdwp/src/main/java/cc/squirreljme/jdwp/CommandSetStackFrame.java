// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.host.JDWPCommandHandler;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;

/**
 * Command set for stack frames.
 *
 * @since 2021/03/15
 */
public enum CommandSetStackFrame
	implements JDWPCommandHandler
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
			// Ignore the thread but check it, then read the frame
			__packet.readThread(__controller);
			Object frame = __packet.readFrame(__controller, false);
			
			// Read in the slot table
			int numSlots = __packet.readInt();
			int[] wantSlot = new int[numSlots];
			byte[] wantTag = new byte[numSlots];
			for (int i = 0; i < numSlots; i++)
			{
				wantSlot[i] = __packet.readInt();
				wantTag[i] = __packet.readByte();
			}
			
			// Always reply with the same number of slots
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(numSlots);
			
			JDWPViewFrame viewFrame = __controller.viewFrame();
			for (int i = 0; i < numSlots; i++)
				try (JDWPValue value = __controller.value())
				{
					// If this value is an object we need to register it for
					// future grabbing
					if (!viewFrame.readValue(frame, wantSlot[i], value))
						value.set(null);
						
					// Try to guess the used value
					JDWPValueTag tag = JDWPValueTag.guessType(
						__controller, value);
					rv.writeValue(__controller, value, tag, false);
					
					// Store object for later use
					if (value.get() != null && tag.isObject)
						__controller.state.items.put(value.get());
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
			// Ignore the thread but check it, then read the frame
			__packet.readThread(__controller);
			Object frame = __packet.readFrame(__controller, false);
			
			// Where is this frame located?
			JDWPViewFrame viewFrame = __controller.viewFrame();
			Object type = viewFrame.atClass(frame);
			int methodDx = viewFrame.atMethodIndex(frame);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Static and native methods always return null
			int mFlags = __controller.viewType().methodFlags(type, methodDx);
			if (0 != (mFlags & (CommandSetStackFrame._FLAG_STATIC |
				CommandSetStackFrame._FLAG_NATIVE)))
			{
				rv.writeId(0);
			}
			
			// Write self value
			else
				try (JDWPValue value = __controller.value())
				{
					// If this value is an object we need to register it for
					// future grabbing
					if (!viewFrame.readValue(frame, 0, value))
						value.set(null);
						
					// Try to guess the used value
					JDWPValueTag tag = JDWPValueTag.guessType(
						__controller, value);
					rv.writeValue(__controller, value, tag, false);
					
					// Store object for later use
					if (value.get() != null && tag.isObject)
						__controller.state.items.put(value.get());
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
