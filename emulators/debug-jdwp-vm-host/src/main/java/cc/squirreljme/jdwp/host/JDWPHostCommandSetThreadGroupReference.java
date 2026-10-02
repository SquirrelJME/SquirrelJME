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
import cc.squirreljme.jdwp.JDWPCommandSetThreadGroupReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.jdwp.host.views.JDWPViewThreadGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread group reference commands.
 *
 * @since 2021/03/13
 */
public enum JDWPHostCommandSetThreadGroupReference
	implements JDWPCommandHandler
{
	/** The name of the group. */
	NAME(JDWPCommandSetThreadGroupReference.NAME)
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
			JDWPViewThreadGroup view = __controller.viewThreadGroup();
			
			// Is this valid?
			Object group = __controller.readThreadGroup(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Matches the string representation of the group
			rv.writeString(view.name(group));
			
			return rv;
		}
	},
	
	/** The parent thread group. */
	PARENT(JDWPCommandSetThreadGroupReference.PARENT)
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
			// Is this valid?
			__controller.readThreadGroup(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// There are never any parent thread groups
			rv.writeId(0);
			
			return rv;
		}
	},
	
	/** The children thread groups and threads. */
	CHILDREN(JDWPCommandSetThreadGroupReference.CHILDREN)
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
			JDWPViewThreadGroup groupView = __controller.viewThreadGroup();
			JDWPViewThread threadView = __controller.viewThread();
			
			// Is this valid?
			Object group = __controller.readThreadGroup(__packet, false);
				
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Filter out terminated, frameless, and debug threads (callbacks?)
			List<Object> threads = new ArrayList<>();
			for (Object thread : groupView.threads(group))
				if (JDWPHostUtils.isVisibleThread(threadView, thread))
					threads.add(thread);
			
			// Write number of child threads
			rv.writeInt(threads.size());
			
			// Record all of their IDs
			for (Object thread : threads)
			{
				Object threadInstance = threadView.instance(thread);
				__controller.writeObject(rv, threadInstance);
				
				// Store for later referencing
				__controller.getState().items.put(thread);
				__controller.getState().items.put(threadInstance);
			}
			
			// There are never any child thread groups
			rv.writeInt(0);
			
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
	JDWPHostCommandSetThreadGroupReference(JDWPCommand __id)
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
