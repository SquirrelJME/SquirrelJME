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
import cc.squirreljme.jdwp.JDWPCommandSetEventRequest;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPEventModifierKind;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.jdwp.host.event.JDWPHostCallStackStepping;
import cc.squirreljme.jdwp.JDWPClassPatternMatcher;
import cc.squirreljme.jdwp.host.event.JDWPHostExceptionOnly;
import cc.squirreljme.jdwp.host.event.JDWPHostFieldOnly;
import cc.squirreljme.jdwp.host.event.JDWPHostEventFilter;
import cc.squirreljme.jdwp.JDWPStepDepth;
import cc.squirreljme.jdwp.JDWPStepSize;

/**
 * Event request command set.
 *
 * @since 2021/03/12
 */
public enum JDWPHostCommandSetEventRequest
	implements JDWPCommandHandler
{
	/** Set event requests. */
	SET(JDWPCommandSetEventRequest.SET)
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
			JDWPClassPatternMatcher includeClass = null;
			JDWPClassPatternMatcher excludeClass = null;
			JDWPHostFieldOnly fieldOnly = null;
			JDWPHostLocation location = null;
			Object thisInstance = null;
			boolean thisInstanceSet = false;
			JDWPHostExceptionOnly exception = null;
			JDWPHostCallStackStepping callStackStepping = null;
			
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
						thread = __controller.readThread(__packet);
						
						if (thread != null)
							__controller.getState().items.put(thread);
						break;
					
					case CLASS_ONLY:
						type = __controller.readType(__packet, false);
						
						if (type != null)
							__controller.getState().items.put(type);
						break;
						
					case CLASS_MATCH_PATTERN:
						includeClass = new JDWPClassPatternMatcher(
							__packet.readString());
						break;
					
					case CLASS_EXCLUDE_PATTERN:
						excludeClass = new JDWPClassPatternMatcher(
							__packet.readString());
						break;
					
						// A specific location in a class
					case LOCATION_ONLY:
						location = __controller.readLocation(__packet);
						break;
					
					case EXCEPTION_ONLY:
						exception = new JDWPHostExceptionOnly(
							__controller.readType(__packet, true),
							__packet.readBoolean(),
							__packet.readBoolean());
						break;
					
					case FIELD_ONLY:
						{
							// Make sure this is a valid field
							Object atType = __controller.readType(__packet,
								false);
							int fieldDx = __packet.readId();
							if (!__controller.viewType().isValidField(atType,
									fieldDx))
								JDWPErrorType.INVALID_FIELD_ID.toss(atType,
									fieldDx, null); 
							
							fieldOnly = new JDWPHostFieldOnly(atType, fieldDx);
						}
						break;
					
					case CALL_STACK_STEPPING:
						callStackStepping = new JDWPHostCallStackStepping(
							__controller.readThread(__packet),
							JDWPStepSize.of(__packet.readInt()),
							JDWPStepDepth.of(__packet.readInt()));
						break;
					
					case THIS_INSTANCE_ONLY:
						thisInstance = __controller.readObject(__packet, true);
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
	CLEAR(JDWPCommandSetEventRequest.CLEAR)
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
	CLEAR_ALL_BREAKPOINTS(JDWPCommandSetEventRequest.CLEAR_ALL_BREAKPOINTS)
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
	
	/** The base command. */
	public final JDWPCommand command;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/12
	 */
	JDWPHostCommandSetEventRequest(JDWPCommand __id)
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
	 * @since 2021/03/12
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
