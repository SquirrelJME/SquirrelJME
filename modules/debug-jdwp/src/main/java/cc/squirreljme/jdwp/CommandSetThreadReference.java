// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPViewFrame;
import cc.squirreljme.jdwp.views.JDWPViewThread;
import cc.squirreljme.jvm.mle.constants.ThreadStatusType;

/**
 * Command set for thread support.
 *
 * @since 2021/03/13
 */
public enum CommandSetThreadReference
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
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// This just uses the object name of the thread, whatever that
			// may be for simplicity and mapping
			rv.writeString(__controller.viewThread().name(thread));
			
			return rv;
		}
	},
	
	/** Suspend thread. */
	SUSPEND(2)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
			
			// Suspend the thread
			view.suspension(thread).suspend();
			
			return null;
		}
	},
	
	/** Resume thread. */
	RESUME(3)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
			
			// Suspend the thread
			view.suspension(thread).resume();
			
			return null;
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
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Which state is this thread in?
			switch (view.status(thread))
			{
					// Sleeping
				case ThreadStatusType.SLEEPING:
					rv.writeInt(2);
					break;
					
					// Waiting on a monitor?
				case ThreadStatusType.MONITOR_WAIT:
					rv.writeInt(3);
					break;
				
					// Running state, assuming anything else is running
				case ThreadStatusType.RUNNING:
				default:
					rv.writeInt(1);
					break;
			}
			
			// If the thread is suspended, then it will be flagged as such
			rv.writeInt(view.suspension(thread).query() > 0 ? 1 : 0);
			
			return rv;
		}
	},
	
	/** Thread group of a thread. */
	THREAD_GROUP(5)
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
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
				
			// Get the parent
			Object parent = view.parentGroup(thread);
			__controller.state.items.put(parent);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write the thread group
			rv.writeId(System.identityHashCode(parent));
			
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
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
			
			// Input for the packet
			int startFrame = __packet.readInt();
			int count = __packet.readInt();
			
			// Correct the frame count, to make sure it is always within
			// bounds of the call
			Object[] frames = __controller.viewThread().frames(thread);
			count = (count == -1 ? Math.max(0, frames.length - startFrame) :
				Math.min(count, frames.length - startFrame));
			
			// Start by writing the frame count
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(count);
			
			// Write each individual frame
			JDWPViewFrame viewFrame = __controller.viewFrame();
			for (int i = startFrame, j = 0; j < count; i++, j++)
			{
				// Register this frame so it can be grabbed later
				Object frame = frames[i];
				__controller.state.items.put(frame);
				
				// Write frame ID
				rv.writeId(System.identityHashCode(frame));
				
				// We need to store and cache the class for later reference
				Object classy = viewFrame.atClass(frame);
				if (classy != null)
					__controller.state.items.put(classy);
				
				// Write the frame location
				rv.writeLocation(__controller, classy,
					viewFrame.atMethodIndex(frame),
					viewFrame.atCodeIndex(frame));
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
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Return the frame count
			rv.writeInt(__controller.viewThread().frames(thread).length);
			
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
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __packet.readThread(__controller, false);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeInt(view.suspension(thread).query());
			
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
	CommandSetThreadReference(int __id)
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
