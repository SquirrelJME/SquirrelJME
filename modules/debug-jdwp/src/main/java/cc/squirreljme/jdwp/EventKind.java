// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.EventFilter;
import cc.squirreljme.jdwp.event.EventModContext;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum EventKind
	implements __IdNumbered__
{
	/** Single Step. */
	SINGLE_STEP(1, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.CALL_STACK_STEPPING,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Breakpoint. */
	BREAKPOINT(2, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Frame pop. */
	FRAME_POP(3, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Exception. */
	EXCEPTION(4, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.EXCEPTION_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** User defined. */
	USER_DEFINED(5, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Start of thread. */
	THREAD_START(6, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeId(System.identityHashCode(__args[0]));
		}
	},
	
	/** End of thread. */
	THREAD_DEATH(7, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeId(System.identityHashCode(__args[0]));
		}
	},
	
	/** Class being prepared. */
	CLASS_PREPARE(8, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			Object thread = __args[0];
			Object cl = __args[1];
			JDWPClassStatus status = (JDWPClassStatus)__args[2];
			
			// Write out the packet
			__packet.writeId(thread);
			
			// The Class ID
			__packet.writeByte(cl.debuggerClassType().id);
			__packet.writeId(cl);
			
			// The signature of the class
			__packet.writeString(cl.debuggerFieldDescriptor());
			
			// The state of this class
			__packet.writeInt(status.bits);
		}
	},
	
	/** Class unloading. */
	CLASS_UNLOAD(9, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Class loading. */
	CLASS_LOAD(10, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Field access. */
	FIELD_ACCESS(20, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.FIELD_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		protected void inject(JDWPController __controller,
			EventRequest __request, EventFilter __subject)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Field modification. */
	FIELD_MODIFICATION(21, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.LOCATION_ONLY,
		EventModKind.FIELD_ONLY, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		protected void inject(JDWPController __controller,
			EventRequest __request, EventFilter __subject)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Exception catch. */
	EXCEPTION_CATCH(30, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.EXCEPTION_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Method entry. */
	METHOD_ENTRY(40, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Method exit. */
	METHOD_EXIT(41, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Method exit with return value. */
	METHOD_EXIT_WITH_RETURN_VALUE(42, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Contended monitor enter. */
	MONITOR_CONTENDED_ENTER(43, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Contended monitor exit. */
	MONITOR_CONTENDED_EXIT(44, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Monitor wait. */
	MONITOR_WAIT(45, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Monitor waited. */
	MONITOR_WAITED(46, EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Virtual machine start. */
	VM_START(90, EventModKind.THREAD_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Virtual machine death. */
	VM_DEATH(99, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/17
		 */
		@Override
		public EventModContext modifierContext(EventModKind __modifier)
		{
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/* End. */
	;
	
	/** Quick table. */
	private static final __QuickTable__<EventKind> _QUICK =
		new __QuickTable__<>(EventKind.values());
	
	/** The event ID. */
	public final int id;
	
	/** The event modifiers that are possible. */
	private final EventModKind[] _modifiers;
	
	/** The modifier ordinal bits, for quicker lookup. */
	private final int _modifierBits;
	
	/**
	 * Initializes the event kind.
	 * 
	 * @param __id The identifier.
	 * @param __modifiers The possible supported modifiers for this event.
	 * @since 2021/03/13
	 */
	EventKind(int __id, EventModKind... __modifiers)
	{
		this.id = __id;
		this._modifiers = __modifiers;
		
		// Determine the modifier bits to quickly get the items
		int modifierBits = 0;
		for (EventModKind mod : __modifiers)
			modifierBits |= (1 << mod.ordinal());
		this._modifierBits = modifierBits;
	}
	
	/**
	 * Injects the given event for trips to occur at later point.
	 * 
	 * @param __controller The controller used. 
	 * @param __request The request being injected.
	 * @param __subject The subject of this event.
	 * @since 2021/04/17
	 */
	protected void inject(JDWPController __controller,
		EventRequest __request, EventFilter __subject)
	{
		// By default this does nothing.
	}
	
	/**
	 * Returns the context for the given modifier.
	 * 
	 * @param __modifier The modifier used.
	 * @return The context for the given modifier.
	 * @since 2021/04/17
	 */
	public abstract EventModContext modifierContext(EventModKind __modifier);
	
	/**
	 * Writes the packet event data.
	 * 
	 * @param __packet The packet to write to.
	 * @param __args The arguments to the packet.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/16
	 */
	public abstract void write(JDWPPacket __packet, Object... __args)
		throws JDWPException;
	
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
	 * Looks up the constant by the given Id.
	 * 
	 * @param __id The Id.
	 * @return The found constant.
	 * @since 2021/03/13
	 */
	public static EventKind of(int __id)
	{
		return EventKind._QUICK.get(__id);
	}
}
