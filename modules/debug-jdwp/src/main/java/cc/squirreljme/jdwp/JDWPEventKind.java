// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.EventModContext;
import cc.squirreljme.jdwp.host.JDWPHostEventHandler;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.collections.EmptyList;
import net.multiphasicapps.collections.UnmodifiableIterable;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum JDWPEventKind
	implements __IdNumbered__, JDWPHostEventHandler
{
	/** Single Step. */
	SINGLE_STEP(1,
		Arrays.asList(EventModContext.CURRENT_THREAD,
			EventModContext.CURRENT_TYPE,
			EventModContext.CURRENT_INSTANCE),
		Arrays.asList(EventModContext.PARAMETER_STEPPING),
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.CALL_STACK_STEPPING,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __thread);
			__packet.writeLocation(__controller,
				__controller.locationOf(__thread));
		}
	},
	
	/** Breakpoint. */
	BREAKPOINT(2, Arrays.asList(EventModContext.CURRENT_THREAD,
			EventModContext.CURRENT_LOCATION,
			EventModContext.CURRENT_INSTANCE),
		null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __thread);
			__packet.writeLocation(__controller,
				__controller.locationOf(__thread));
		}
	},
	
	/** Frame pop. */
	FRAME_POP(3, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Exception. */
	EXCEPTION(4, null,
		Arrays.asList(
			EventModContext.ENSNARE_ARGUMENT,
			EventModContext.TOSSED_EXCEPTION),
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.EXCEPTION_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			Object tossing = __args[0];
			ExceptionHandler handler = (ExceptionHandler)__args[1];
			
			// Where are we?
			JDWPHostLocation tossLocation = __controller.locationOf(__thread);
			__packet.writeObject(__controller, __thread);
			__packet.writeLocation(__controller, tossLocation);
			
			// Object being tossed
			__packet.writeTaggedId(__controller, tossing);
			
			// Where is the exception handler, if there is one?
			if (handler == null)
				__packet.writeLocation(__controller, JDWPHostLocation.BLANK);
			else
				__packet.writeLocation(__controller,
					tossLocation.withCodeIndex(handler.handlerAddress()));
		}
	},
	
	/** User defined. */
	USER_DEFINED(5, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Start of thread. */
	THREAD_START(6, null, null, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __args[0]);
		}
	},
	
	/** End of thread. */
	THREAD_DEATH(7, null, null, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __args[0]);
		}
	},
	
	/** Class being prepared. */
	CLASS_PREPARE(8, null,
		Arrays.asList(EventModContext.PARAMETER_TYPE),
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			// Extract arguments
			Object cl = __args[0];
			JDWPClassStatus status = (JDWPClassStatus)__args[1];
			
			// Calling thread
			__packet.writeObject(__controller, __thread);
			
			// The Class ID
			__packet.writeTaggedId(__controller, cl);
			
			// The signature of the class
			__packet.writeString(__controller.viewType().signature(cl));
			
			// The state of this class
			__packet.writeInt(status.bits);
		}
	},
	
	/** Class unloading. */
	CLASS_UNLOAD(9, null, null,
		EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Class loading. */
	CLASS_LOAD(10, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Field access. */
	FIELD_ACCESS(20,
		Arrays.asList(EventModContext.CURRENT_THREAD,
			EventModContext.CURRENT_LOCATION,
			EventModContext.CURRENT_TYPE,
			EventModContext.CURRENT_INSTANCE),
		Arrays.asList(EventModContext.PARAMETER_TYPE_OR_FIELD,
			EventModContext.PARAMETER_FIELD),
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.FIELD_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			this.__field(false, __controller, __thread, __packet,
				__args);
		}
	},
	
	/** Field modification. */
	FIELD_MODIFICATION(21,
		Arrays.asList(EventModContext.CURRENT_THREAD,
			EventModContext.CURRENT_LOCATION,
			EventModContext.CURRENT_TYPE,
			EventModContext.CURRENT_INSTANCE),
		Arrays.asList(EventModContext.PARAMETER_TYPE_OR_FIELD,
			EventModContext.PARAMETER_FIELD),
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.LOCATION_ONLY,
		EventModKind.FIELD_ONLY, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			this.__field(true, __controller, __thread, __packet,
				__args);
		}
	},
	
	/** Exception catch. */
	EXCEPTION_CATCH(30, null,
		Arrays.asList(
			EventModContext.ENSNARE_ARGUMENT,
			EventModContext.TOSSED_EXCEPTION),
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.EXCEPTION_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			// Exceptions are always formatted the same regardless of
			// whether they were caught or not
			JDWPEventKind.EXCEPTION.write(__controller, __thread, __packet,
				__args);
		}
	},
	
	/** Method entry. */
	METHOD_ENTRY(40,
		Arrays.asList(EventModContext.CURRENT_LOCATION,
			EventModContext.CURRENT_INSTANCE,
			EventModContext.CURRENT_TYPE),
		null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __thread);
			__packet.writeLocation(__controller,
				__controller.locationOf(__thread));
		}
	},
	
	/** Method exit. */
	METHOD_EXIT(41,
		Arrays.asList(EventModContext.CURRENT_LOCATION,
			EventModContext.CURRENT_INSTANCE,
			EventModContext.CURRENT_TYPE),
		null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __thread);
			__packet.writeLocation(__controller,
				__controller.locationOf(__thread));
		}
	},
	
	/** Method exit with return value. */
	METHOD_EXIT_WITH_RETURN_VALUE(42, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeObject(__controller, __thread);
			__packet.writeLocation(__controller,
				__controller.locationOf(__thread));
			
			// Write down the value
			__packet.writeValue(__controller, __args[0],
				JDWPValueTag.guessTypeRaw(__controller, __args[0]),
				false);
		}
	},
	
	/** Contended monitor enter. */
	MONITOR_CONTENDED_ENTER(43, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Contended monitor exit. */
	MONITOR_CONTENDED_EXIT(44, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Monitor wait. */
	MONITOR_WAIT(45, null, null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Monitor waited. */
	MONITOR_WAITED(46, null, null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Virtual machine start. */
	VM_START(90, null, null, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			// Write the starting thread
			__packet.writeObject(__controller, __thread);
		}
	},
	
	/** Virtual machine death. */
	VM_DEATH(99, null, null, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Special alias to indicate an unconditional {@link #BREAKPOINT}. */
	UNCONDITIONAL_BREAKPOINT(-2, null, null)
	{
		@Override
		public void write(JDWPHostController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			// This should not truly be called
			throw Debugging.oops();
		}
	},
	
	/* End. */
	;
	
	/** Quick table. */
	private static final __QuickTable__<JDWPEventKind> _QUICK =
		new __QuickTable__<>(JDWPEventKind.values());
	
	/** The event ID. */
	public final int id;
	
	/** The modifier ordinal bits, for quicker lookup. */
	private final int _modifierBits;
	
	/** Non-argument context. */
	private final Iterable<EventModContext> _nonArg;
	
	/** Argument context. */
	private final List<EventModContext> _arg;
	
	/**
	 * Initializes the event kind.
	 * 
	 * @param __id The identifier.
	 * @param __nonArg Non-argument context.
	 * @param __arg Argument context.
	 * @param __modifiers The possible supported modifiers for this event.
	 * @since 2021/03/13
	 */
	JDWPEventKind(int __id, Iterable<EventModContext> __nonArg,
		List<EventModContext> __arg, EventModKind... __modifiers)
	{
		this.id = __id;
		
		// Contexts for modifiers
		this._nonArg = (__nonArg == null ? EmptyList.<EventModContext>empty() :
			UnmodifiableIterable.<EventModContext>of(__nonArg));
		this._arg = (__arg == null ? EmptyList.<EventModContext>empty() :
			UnmodifiableList.<EventModContext>of(__arg));
		
		// Determine the modifier bits to quickly get the items
		int modifierBits = 0;
		for (EventModKind mod : __modifiers)
			modifierBits |= (1 << mod.ordinal());
		this._modifierBits = modifierBits;
	}
	
	/**
	 * Returns the context for the given argument.
	 * 
	 * @param __i The argument index, the first value should always be
	 * {@code thread}.
	 * @return The context for the given argument, will be {@code null} if
	 * not valid for negative values.
	 * @since 2021/04/17
	 */
	public final EventModContext contextArgument(int __i)
	{
		List<EventModContext> arg = this._arg;
		if (__i < 0 || __i >= arg.size())
			return null;
		
		return arg.get(__i);
	}
	
	/**
	 * Returns the general context.
	 * 
	 * @return The general context.
	 * @since 2021/04/25
	 */
	public final Iterable<EventModContext> contextGeneral()
	{
		return this._nonArg;
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
	
	/**
	 * Checks if the given modifier is valid for this.
	 * 
	 * @param __mod The modifier to check.
	 * @return Is this a valid modifier?
	 * @since 2021/04/17
	 */
	public final boolean isValidModifier(EventModKind __mod)
	{
		// Is the ordinal set for this modifier?
		return (0 != (this._modifierBits & (1 << __mod.ordinal())));
	}
	
	/**
	 * Performs field packet writing for access and modification.
	 * 
	 * @param __write Writing field modification?
	 * @param __controller The controller used.
	 * @param __thread The current thread.
	 * @param __packet The outgoing packet.
	 * @param __args The arguments to the signal.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/04/30
	 */
	void __field(boolean __write, JDWPHostController __controller, Object __thread,
		JDWPPacket __packet, Object... __args)
		throws JDWPException
	{
		// Get the pass field values
		Object type = __args[0];
		int fieldDx = (int)__args[1];
		boolean write = (boolean)__args[2];
		Object instance = __args[3];
		JDWPValue newValue = (JDWPValue)__args[4];
		
		// Write current thread and location
		__packet.writeObject(__controller, __thread);
		JDWPHostLocation location = __controller.locationOf(__thread);
		__packet.writeLocation(__controller,
			__controller.locationOf(__thread));
		
		// Store the location items
		JDWPHostLinker<Object> items = __controller.getState().items;
		if (__thread != null)
			items.put(__thread);
		if (location.type != null)
			items.put(location.type);
		
		// Field reference type tag and type????
		// Documentation says "Kind of reference type. See JDWP.TypeTag" and
		// "Type of field" but this is then followed by the field ID so it
		// does not make sense if this is the field signature value because
		// how would we know which field we were even writing because the
		// information is not elsewhere at all??? So this is a big guess.
		// TODO: Was this guessed correctly???
		__packet.writeTaggedId(__controller, type);
		if (type != null)
			items.put(type);
		
		// The field ID
		__packet.writeId(fieldDx);
		
		// The object accessed, this is tagged oddly
		__packet.writeValue(__controller, instance,
			JDWPValueTag.guessTypeRaw(__controller, instance), false);
		if (instance != null)
			items.put(instance);
		
		// Write value if that is changing
		if (__write || write)
		{
			// Determine the field information
			String fieldSig = __controller.viewType()
				.fieldSignature(type, fieldDx);
			JDWPValueTag tag = JDWPValueTag.fromSignature(fieldSig);
			
			// Write the value
			__packet.writeValue(__controller, newValue, tag, false);
			
			// Make sure this is a known object
			Object itemVal = newValue.get();
			if (__controller.viewObject().isValid(itemVal))
				items.put(itemVal);
		}
		
		// Debug
		if (JDWPHostController._DEBUG)
			Debugging.debugNote("Field(%s#%d, %b, %s, %s) @ %s",
				type, fieldDx, write, instance, newValue, location);
	}
	
	/**
	 * Looks up the constant by the given Id.
	 * 
	 * @param __id The Id.
	 * @return The found constant.
	 * @since 2021/03/13
	 */
	public static JDWPEventKind of(int __id)
	{
		return JDWPEventKind._QUICK.get(__id);
	}
}
