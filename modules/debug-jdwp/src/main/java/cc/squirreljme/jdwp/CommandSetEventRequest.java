// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.CallStackStepping;
import cc.squirreljme.jdwp.event.ClassPatternMatcher;
import cc.squirreljme.jdwp.event.EventFilter;
import cc.squirreljme.jdwp.event.ExceptionOnly;
import cc.squirreljme.jdwp.event.FieldOnly;

/**
 * Event request command set.
 *
 * @since 2021/03/12
 */
public enum CommandSetEventRequest
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
			
			// Is there at least one filter?
			boolean hasFilter = false;
			
			// Modifier properties
			int occurrenceLimit = -1;
			Object thread = null;
			Object type = null;
			ClassPatternMatcher includeClass = null;
			ClassPatternMatcher excludeClass = null;
			FieldOnly fieldOnly = null;
			JDWPLocation location = null;
			Object thisInstance = null;
			boolean thisInstanceSet = false;
			ExceptionOnly exception = null;
			CallStackStepping callStackStepping = null;
			
			// Modifier kinds
			int numModifiers = __packet.readInt();
			for (int i = 0; i < numModifiers; i++)
			{
				// Check if the kind if supported or known about
				EventModKind modKind = EventModKind.of(__packet.readByte());
				if (modKind == null)
					return __controller.__reply(__packet.id(),
						ErrorType.NOT_IMPLEMENTED);
				
				// If this is not a valid modifier, ignore this
				if (!eventKind.isValidModifier(modKind))
					throw ErrorType.ILLEGAL_ARGUMENT.toss(
						null, modKind.ordinal(), null);
				
				// Everything except occurrences has a filter!
				if (modKind != EventModKind.LIMIT_OCCURRENCES)
					hasFilter = true;
					
				// Depends on the kind
				switch (modKind)
				{
					case LIMIT_OCCURRENCES:
						occurrenceLimit = __packet.readInt();
						break;
					
					case THREAD_ONLY:
						thread = __packet.readThread(
							__controller, false);
						
						__controller.state.items.put(type);
						break;
					
					case CLASS_ONLY:
						type = __packet.readType(__controller, false);
						
						__controller.state.items.put(type);
						break;
						
					case CLASS_MATCH_PATTERN:
						includeClass = new ClassPatternMatcher(
							__packet.readString());
						break;
					
					case CLASS_EXCLUDE_PATTERN:
						excludeClass = new ClassPatternMatcher(
							__packet.readString());
						break;
					
						// A specific location in a class
					case LOCATION_ONLY:
						location = __packet.readLocation(__controller);
						break;
					
					case EXCEPTION_ONLY:
						exception = new ExceptionOnly(
							__packet.readType(__controller, true),
							__packet.readBoolean(),
							__packet.readBoolean());
						break;
					
					case FIELD_ONLY:
						{
							// Make sure this is a valid field
							Object atType = __packet.readType(__controller,
								false);
							int fieldDx = __packet.readId();
							if (!__controller.viewType().isValidField(atType,
									fieldDx))
								ErrorType.INVALID_FIELD_ID.toss(atType,
									fieldDx, null); 
							
							fieldOnly = new FieldOnly(atType, fieldDx);
						}
						break;
					
					case CALL_STACK_STEPPING:
						callStackStepping = new CallStackStepping(
							__packet.readThread(__controller, false),
							__packet.readInt(),
							__packet.readInt());
						break;
					
					case THIS_INSTANCE_ONLY:
						thisInstance = __packet.readObject(__controller,
							true);
						thisInstanceSet = true;
						break;
					
						// Report not-implemented
					default:
						return __controller.__reply(__packet.id(),
							ErrorType.NOT_IMPLEMENTED);
				}
			}
			
			// Initialize the event filter with all the modifier parameters
			EventFilter eventFilter = (hasFilter ? new EventFilter(thread,
				type, includeClass, excludeClass, fieldOnly, location,
				thisInstanceSet, thisInstance, exception,
				callStackStepping) : null);
			
			// Register the event request
			EventRequest request = new EventRequest(
				__controller.__nextId(), eventKind, suspendPolicy,
				occurrenceLimit, eventFilter);
			__controller.eventManager.addEventRequest(request);
			
			// Perform injection for the event so whatever we are using for
			// the call can trip events 
			__controller.tripRequest(request);
			
			// Respond with the ID of this event
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeInt(request.id);
			
			return rv;
		}
	},
	
	/** Clear event. */
	CLEAR(2)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/18
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
			
			// Delete the event, if it is known... the kind is ignored since
			// we always use unique IDs regardless of type
			__controller.eventManager.delete(__packet.readInt());
			
			// Always successful, even if the ID is not valid
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
	CommandSetEventRequest(int __id)
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
