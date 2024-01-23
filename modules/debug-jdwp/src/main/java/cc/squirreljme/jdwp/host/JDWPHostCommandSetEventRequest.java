// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPEventModifierKind;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPHostController;
import cc.squirreljme.jdwp.JDWPHostEventRequest;
import cc.squirreljme.jdwp.JDWPHostLocation;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.jdwp.event.CallStackStepping;
import cc.squirreljme.jdwp.event.ClassPatternMatcher;
import cc.squirreljme.jdwp.event.ExceptionOnly;
import cc.squirreljme.jdwp.event.FieldOnly;
import cc.squirreljme.jdwp.event.JDWPHostEventFilter;
import cc.squirreljme.jdwp.event.StepDepth;
import cc.squirreljme.jdwp.event.StepSize;

/**
 * Event request command set.
 *
 * @since 2021/03/12
 */
public enum JDWPHostCommandSetEventRequest
	implements JDWPCommandHandler
{
	/** Set event requests. */
	SET(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which kind of event? If not supported, it is not valid
			JDWPEventKind eventKind = JDWPEventKind.of(__packet.readByte());
			if (eventKind == null)
				return __controller.reply(__packet.id(),
					JDWPErrorType.INVALID_EVENT_TYPE);
			
			// How does this suspend?
			JDWPSuspendPolicy suspendPolicy =
				JDWPSuspendPolicy.of(__packet.readByte());
			
			// Is there at least one filter?
			boolean hasFilter = false;
			
			// Modifier properties
			int occurrenceLimit = -1;
			Object thread = null;
			Object type = null;
			ClassPatternMatcher includeClass = null;
			ClassPatternMatcher excludeClass = null;
			FieldOnly fieldOnly = null;
			JDWPHostLocation location = null;
			Object thisInstance = null;
			boolean thisInstanceSet = false;
			ExceptionOnly exception = null;
			CallStackStepping callStackStepping = null;
			
			// Modifier kinds
			int numModifiers = __packet.readInt();
			for (int i = 0; i < numModifiers; i++)
			{
				// Check if the kind if supported or known about
				JDWPEventModifierKind modKind = JDWPEventModifierKind.of(__packet.readByte());
				if (modKind == null)
					return __controller.reply(__packet.id(),
						JDWPErrorType.NOT_IMPLEMENTED);
				
				// If this is not a valid modifier, ignore this
				if (!eventKind.isValidModifier(modKind))
					throw JDWPErrorType.ILLEGAL_ARGUMENT.toss(
						null, modKind.ordinal(), null);
				
				// Everything except occurrences has a filter!
				if (modKind != JDWPEventModifierKind.LIMIT_OCCURRENCES)
					hasFilter = true;
					
				// Depends on the kind
				switch (modKind)
				{
					case LIMIT_OCCURRENCES:
						occurrenceLimit = __packet.readInt();
						break;
					
					case THREAD_ONLY:
						thread = __packet.readThread(
							__controller);
						
						if (thread != null)
							__controller.getState().items.put(thread);
						break;
					
					case CLASS_ONLY:
						type = __packet.readType(__controller, false);
						
						if (type != null)
							__controller.getState().items.put(type);
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
								JDWPErrorType.INVALID_FIELD_ID.toss(atType,
									fieldDx, null); 
							
							fieldOnly = new FieldOnly(atType, fieldDx);
						}
						break;
					
					case CALL_STACK_STEPPING:
						callStackStepping = new CallStackStepping(
							__packet.readThread(__controller),
							StepSize.of(__packet.readInt()),
							StepDepth.of(__packet.readInt()));
						break;
					
					case THIS_INSTANCE_ONLY:
						thisInstance = __packet.readObject(__controller,
							true);
						thisInstanceSet = true;
						break;
					
						// Report not-implemented
					default:
						return __controller.reply(__packet.id(),
							JDWPErrorType.NOT_IMPLEMENTED);
				}
			}
			
			// Initialize the event filter with all the modifier parameters
			JDWPHostEventFilter
				eventFilter = (hasFilter ? new JDWPHostEventFilter(thread,
				type, includeClass, excludeClass, fieldOnly, location,
				thisInstanceSet, thisInstance, exception,
				callStackStepping) : null);
			
			// Register the event request
			JDWPHostEventRequest request = new JDWPHostEventRequest(
				__controller.getCommLink().nextId(), eventKind, suspendPolicy,
				occurrenceLimit, eventFilter);
			__controller.getEventManager().addEventRequest(request);
			
			// Perform injection for the event so whatever we are using for
			// the call can trip events 
			__controller.tripRequest(request);
			
			// Respond with the ID of this event
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
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
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which kind of event? If not supported, it is not valid
			JDWPEventKind eventKind = JDWPEventKind.of(__packet.readByte());
			if (eventKind == null)
				return __controller.reply(__packet.id(),
					JDWPErrorType.INVALID_EVENT_TYPE);
			
			// Delete the event, if it is known... the kind is ignored since
			// we always use unique IDs regardless of type
			__controller.getEventManager().delete(__packet.readInt());
			
			// Always successful, even if the ID is not valid
			return null;
		}
	},
	
	/** Clear all breakpoints. */
	CLEAR_ALL_BREAKPOINTS(3)
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
			__controller.getEventManager().clear(JDWPEventKind.BREAKPOINT);
			
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
	JDWPHostCommandSetEventRequest(int __id)
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
