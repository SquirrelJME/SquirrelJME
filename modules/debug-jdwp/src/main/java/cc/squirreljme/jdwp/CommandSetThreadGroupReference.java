// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPViewThreadGroup;

/**
 * Thread group reference commands.
 *
 * @since 2021/03/13
 */
public enum CommandSetThreadGroupReference
	implements JDWPCommand
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
			JDWPViewThreadGroup view = __controller.viewThreadGroup();
			
			// Is this valid?
			Object group = __packet.readThreadGroup(
				__controller, false);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write number of child threads
			Object[] threads = view.threads(group);
			rv.writeInt(threads.length);
			
			// Record all of their IDs
			for (Object thread : threads)
			{	
				rv.writeObject(__controller, thread);
				
				// Store for later referencing
				__controller.state.items.put(thread);
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
