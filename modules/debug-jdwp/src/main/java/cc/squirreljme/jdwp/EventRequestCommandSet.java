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
import java.util.ArrayList;
import java.util.List;

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
			
			// Occurrence limit
			int occurrenceLimit = -1;
			
			// Modifier kinds
			List<EventModifier> modifiers = new ArrayList<>();
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
						occurrenceLimit = __packet.readInt();
						break;
					
					case CONDITIONAL:
						throw Debugging.todo();
					
					case ONLY_IN_THREAD:
						modifiers.add(new ThreadModifier(
							__controller.state.oldThreads.get(__packet.id())));
						break;
					
					case ONLY_IN_CLASS:
						throw Debugging.todo();
						
					case ONLY_IN_CLASS_PATTERN:
						modifiers.add(new OnlyInClassPatternModifier(
							__packet.readString()));
						break;
					
					case NOT_IN_CLASS_PATTERN:
						throw Debugging.todo();
					
						// A specific location in a class
					case LOCATION:
						{
							// Ignore the type tag, need not know the
							// difference between interfaces and classes
							__packet.readByte();
							
							// Read the location
							JDWPClass inClass = __controller.state.oldClasses
								.get(__packet.readId());
							JDWPMethod inMethod = __controller.state.oldMethods
								.get(__packet.readId()); 
							long index = __packet.readLong();
							
							// Not a valid location?
							if (inClass == null || inMethod == null)
								return __controller.__reply(__packet.id(),
									ErrorType.INVALID_LOCATION);
							
							// Build location modifier
							modifiers.add(new LocationModifier(inClass,
								inMethod, index));
						}
						break;
					
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
				__controller.__nextId(), eventKind, suspendPolicy,
				occurrenceLimit,
				modifiers.toArray(new EventModifier[modifiers.size()]));
			__controller.eventManager.addEventRequest(request);
			
			// Respond with the ID of this event
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(request.debuggerId());
			
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
	public final int debuggerId()
	{
		return this.id;
	}
}
