// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandException;
import cc.squirreljme.jdwp.JDWPCommandSetThreadReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.jdwp.host.views.JDWPViewThreadGroup;
import cc.squirreljme.jvm.mle.constants.ThreadStatusType;

/**
 * Command set for thread support.
 *
 * @since 2021/03/13
 */
public enum JDWPHostCommandSetThreadReference
	implements JDWPCommandHandler
{
	/** Thread name. */
	NAME(JDWPCommandSetThreadReference.NAME)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// This just uses the object name of the thread, whatever that
			// may be for simplicity and mapping
			rv.writeString(__controller.viewThread().name(thread));
			
			return rv;
		}
	},
	
	/** Suspend thread. */
	SUSPEND(JDWPCommandSetThreadReference.SUSPEND)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
			
			// Suspend the thread
			view.suspension(thread).suspend();
			
			return null;
		}
	},
	
	/** Resume thread. */
	RESUME(JDWPCommandSetThreadReference.RESUME)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
			
			// Suspend the thread
			view.suspension(thread).resume();
			
			return null;
		}
	},
	
	/** Status of the thread. */
	STATUS(JDWPCommandSetThreadReference.STATUS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want? Do not use filtering here
			Object thread = __controller.readThread(__packet);
			
			// If the thread is not valid, then just stop
			if (thread == null)
			{
				JDWPPacket rv = __controller.reply(
					__packet.id(), JDWPErrorType.NO_ERROR);
				
				// Terminated and not suspended
				rv.writeInt(0);
				rv.writeInt(0);
				
				return rv;
			}
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// If this thread has terminated it becomes a zombie
			boolean terminated = view.isTerminated(thread);
			if (terminated)
				rv.writeInt(0);
			
			// Which state is this thread in?
			else
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
			// Terminated threads will never be seen as suspended
			rv.writeInt(!terminated && view.suspension(thread).query() > 0 ?
				1 : 0);
			
			return rv;
		}
	},
	
	/** Thread group of a thread. */
	THREAD_GROUP(JDWPCommandSetThreadReference.THREAD_GROUP)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread viewThread = __controller.viewThread();
			JDWPViewThreadGroup viewThreadGroup =
				__controller.viewThreadGroup();
			
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
				
			// Get the parent
			Object parent = viewThread.parentGroup(thread);
			Object parentInstance = viewThreadGroup.instance(parent);
			
			// Make sure both parent representations are stored
			__controller.getState().items.put(parent);
			__controller.getState().items.put(parentInstance);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Write the thread group
			__controller.writeObject(rv, parentInstance);
			
			return rv;
		}
	},
	
	/** Frames. */
	FRAMES(JDWPCommandSetThreadReference.FRAMES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
			
			// Not suspended?
			JDWPViewThread viewThread = __controller.viewThread();
			if (viewThread.suspension(thread).query() <= 0)
				throw new JDWPCommandException(
					JDWPErrorType.THREAD_NOT_SUSPENDED);
			
			// Input for the packet
			int startFrame = __packet.readInt();
			int count = __packet.readInt();
			
			// Correct the frame count, to make sure it is always within
			// bounds of the call
			Object[] frames = viewThread.frames(thread);
			count = (count == -1 ? Math.max(0, frames.length - startFrame) :
				Math.min(count, frames.length - startFrame));
			
			// Start by writing the frame count
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			rv.writeInt(count);
			
			// Write each individual frame
			JDWPViewFrame viewFrame = __controller.viewFrame();
			for (int i = startFrame, j = 0; j < count; i++, j++)
			{
				// Register this frame so it can be grabbed later
				Object frame = frames[i];
				__controller.getState().items.put(frame);
				
				// Write frame ID
				__controller.writeObject(rv, frame);
				
				// We need to store and cache the class for later reference
				Object classy = viewFrame.atClass(frame);
				if (classy != null)
					__controller.getState().items.put(classy);
				
				// Write the frame location
				int atMethodIndex = viewFrame.atMethodIndex(frame);
				__controller.writeLocation(rv, classy, atMethodIndex,
					viewFrame.atCodeIndex(frame));
			} 
			
			return rv;
		}
	},
	
	/** Frame count. */
	FRAME_COUNT(JDWPCommandSetThreadReference.FRAME_COUNT)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
			
			JDWPViewThread viewThread = __controller.viewThread();
			if (viewThread.suspension(thread).query() <= 0)
				throw new JDWPCommandException(
					JDWPErrorType.THREAD_NOT_SUSPENDED);
				
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Return the frame count
			rv.writeInt(viewThread.frames(thread).length);
			
			return rv;
		}
	},
	
	/** Stops a thread, not supported in Java ME. */
	STOP(JDWPCommandSetThreadReference.STOP)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Read the thread to check if valid, but otherwise do nothing
			Object thread = __controller.readThread(__packet);
			
			// Always fail because this does not do anything
			throw JDWPErrorType.ILLEGAL_ARGUMENT.toss(thread,
				System.identityHashCode(thread), null);
		}
	},
	
	/** Interrupt the thread. */
	INTERRUPT(JDWPCommandSetThreadReference.INTERRUPT)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which thread do we want?
			JDWPViewThread view = __controller.viewThread();
			Object thread = __controller.readThread(__packet);
			
			// Interrupt the thread
			view.interrupt(thread);
			
			return null;
		}
	},
	
	/** Suspension count for each thread. */
	SUSPEND_COUNT(JDWPCommandSetThreadReference.SUSPEND_COUNT)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewThread view = __controller.viewThread();
			
			// Which thread do we want?
			Object thread = __controller.readThread(__packet);
				
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			rv.writeInt(view.suspension(thread).query());
			
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The base command. */
	public final JDWPCommand command;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	JDWPHostCommandSetThreadReference(JDWPCommand __id)
	{
		this.command = __id;
		this.id = __id.debuggerId();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public final JDWPCommand command()
	{
		return this.command;
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
