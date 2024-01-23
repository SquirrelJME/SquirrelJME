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
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.jdwp.host.views.JDWPViewThreadGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread group reference commands.
 *
 * @since 2021/03/13
 */
public enum CommandSetThreadGroupReference
	implements JDWPCommandHandler
{
	/** The name of the group. */
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
			JDWPViewThreadGroup view = __controller.viewThreadGroup();
			
			// Is this valid?
			Object group = __packet.readThreadGroup(
				__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Matches the string representation of the group
			rv.writeString(view.name(group));
			
			return rv;
		}
	},
	
	/** The parent thread group. */
	PARENT(2)
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
			// Is this valid?
			__packet.readThreadGroup(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// There are never any parent thread groups
			rv.writeId(0);
			
			return rv;
		}
	},
	
	/** The children thread groups and threads. */
	CHILDREN(3)
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
			JDWPViewThreadGroup groupView = __controller.viewThreadGroup();
			JDWPViewThread threadView = __controller.viewThread();
			
			// Is this valid?
			Object group = __packet.readThreadGroup(
				__controller, false);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Filter out terminated, frameless, and debug threads (callbacks?)
			List<Object> threads = new ArrayList<>();
			for (Object thread : groupView.threads(group))
				if (JDWPUtils.isVisibleThread(threadView, thread))
					threads.add(thread);
			
			// Write number of child threads
			rv.writeInt(threads.size());
			
			// Record all of their IDs
			for (Object thread : threads)
			{
				Object threadInstance = threadView.instance(thread);
				rv.writeObject(__controller, threadInstance);
				
				// Store for later referencing
				__controller.state.items.put(thread);
				__controller.state.items.put(threadInstance);
			}
			
			// There are never any child thread groups
			rv.writeInt(0);
			
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
	CommandSetThreadGroupReference(int __id)
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
