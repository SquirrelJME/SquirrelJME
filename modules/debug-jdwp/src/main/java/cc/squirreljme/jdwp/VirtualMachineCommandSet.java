// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Virtual machine command set.
 *
 * @since 2021/03/12
 */
public enum VirtualMachineCommandSet
	implements JDWPCommand
{
	/** Version information. */
	VERSION(1)
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
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// VM Description
			rv.writeString(__controller.bind.vmDescription());
			
			// JDWP version (assuming Java 7?)
			rv.writeInt(1);
			rv.writeInt(7);
			
			// VM Information
			rv.writeString(__controller.bind.vmVersion());
			rv.writeString(__controller.bind.vmName());
			
			return rv;
		}
	},
	
	/** Classes by signature. */
	CLASSES_BY_SIGNATURE(2)
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
			// Write result
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			Debugging.todoNote("Implement CLASSES_BY_SIGNATURE");
			rv.writeInt(0);
			
			return rv;
		}
	},
	
	/** Top level thread groups. */
	TOP_LEVEL_THREAD_GROUPS(5)
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
			// Update the VM thread groups
			JDWPThreadGroups groupCache = __controller._threadGroups;
			__controller.bind.jdwpUpdateThreadGroups(groupCache);
			
			// Get current thread groups
			JDWPThreadGroup[] groups = groupCache.current();
			
			// Write result
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			int n = groups.length;
			rv.writeInt(n);
			for (JDWPThreadGroup __group : groups)
				rv.writeId(__group);
			
			return rv;
		}
	},
	
	/** Returns the size of variable data. */
	ID_SIZES(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// field, method, object, reference, frame
			for (int i = 0; i < 5; i++)
				rv.writeInt(JDWPConstants.ID_SIZE);
			
			return rv;
		}
	},
	
	/** Capabilities. */
	CAPABILITIES(12)
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
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// canWatchFieldModification
			rv.writeBoolean(false);
			
			// canWatchFieldAccess
			rv.writeBoolean(false);
			
			// canGetBytecodes
			rv.writeBoolean(false);
			
			// canGetSyntheticAttribute
			rv.writeBoolean(false);
			
			// canGetOwnedMonitorInfo
			rv.writeBoolean(false);
			
			// canGetCurrentContendedMonitor
			rv.writeBoolean(false);
			
			// canGetMonitorInfo
			rv.writeBoolean(false);
			
			return rv;
		}
	},
	
	/** Hold events, keep them queued and not transmit them. */
	HOLD_EVENTS(15)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Hold any events
			__controller._holdEvents = true;
			return null;
		}
	},
	
	/** Release any events. */
	RELEASE_EVENTS(16)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Resume events and let them flow
			__controller._holdEvents = false;
			return null;
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
	 * @since 2021/03/12
	 */
	VirtualMachineCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/12
	 */
	@Override
	public final int id()
	{
		return this.id;
	}
}
