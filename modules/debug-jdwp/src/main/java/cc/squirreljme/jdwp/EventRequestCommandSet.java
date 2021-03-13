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
 * Event request command set.
 *
 * @since 2021/03/12
 */
public enum EventRequestCommandSet
	implements JDWPCommand
{
	/** Set event requests. */
	SET(1)
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
			// Which kind of event? If not supported, it is not valid
			EventKind eventKind = EventKind.of(__packet.readByte());
			if (eventKind == null)
				return __controller.__reply(__packet.id(),
					ErrorType.INVALID_EVENT_TYPE);
			
			// How does this suspend?
			SuspendPolicy suspendPolicy =
				SuspendPolicy.of(__packet.readByte());
			
			// Modifier kinds
			int numModifiers = __packet.readInt();
			for (int i = 0; i < numModifiers; i++)
			{
				// Check if the kind if supported or known about
				EventModKind modKind = EventModKind.of(__packet.readByte());
				if (modKind == null)
					return __controller.__reply(__packet.id(),
						ErrorType.NOT_IMPLEMENTED);
					
				// Depends on the kind
				switch (modKind)
				{
					case LIMIT_OCCURRENCES:
						throw Debugging.todo();
					
					case CONDITIONAL:
						throw Debugging.todo();
					
					case ONLY_IN_THREAD:
						throw Debugging.todo();
					
					case ONLY_IN_CLASS:
						throw Debugging.todo();
						
					case ONLY_IN_CLASS_PATTERN:
						throw Debugging.todo();
					
					case NOT_IN_CLASS_PATTERN:
						throw Debugging.todo();
					
					case LOCATION:
						throw Debugging.todo();
					
					case EXCEPTION:
						throw Debugging.todo();
					
					case FIELD:
						throw Debugging.todo();
					
					case CALL_STACK_STEPPING:
						throw Debugging.todo();
					
					case THIS_OBJECT:
						throw Debugging.todo();
					
					case SOURCE_FILENAME_PATTERN:
						throw Debugging.todo();
					
						// Report not-implemented
					default:
						return __controller.__reply(__packet.id(),
							ErrorType.NOT_IMPLEMENTED);
				}
			}
			
			// Register the event request
			EventRequest request = new EventRequest(
				__controller.__nextId(), eventKind, suspendPolicy);
			__controller.__addEventRequest(request);
			
			// Respond with the ID of this event
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(request.id());
			
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
	 * @since 2021/03/12
	 */
	EventRequestCommandSet(int __id)
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
