// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.EventKind;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.SuspendPolicy;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.util.Map;

/**
 * Processes events sent from the virtual machine.
 *
 * @since 2024/01/19
 */
public enum EventProcessor
{
	/** Event. */
	SINGLE_STEP(EventKind.SINGLE_STEP)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	BREAKPOINT(EventKind.BREAKPOINT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	FRAME_POP(EventKind.FRAME_POP)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	EXCEPTION(EventKind.EXCEPTION)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	USER_DEFINED(EventKind.USER_DEFINED)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	THREAD_START(EventKind.THREAD_START)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			StoredInfo<InfoThread> threadStore =
				__state.storedInfo.getThread();
			
			// Read in packet details
			JDWPId threadId = __packet.readId(JDWPIdKind.THREAD_ID);
			
			// Mark as started
			InfoThread thread = threadStore.get(__state, threadId);
			if (thread != null)
				thread.isStarted.set(true);
		}
	},
	
	/** Event. */
	THREAD_DEATH(EventKind.THREAD_DEATH)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			StoredInfo<InfoThread> threadStore =
				__state.storedInfo.getThread();
			
			// Read in packet details
			JDWPId threadId = __packet.readId(JDWPIdKind.THREAD_ID);
			
			// Set dead state
			InfoThread thread = threadStore.get(__state, threadId);
			if (thread != null)
				thread.isDead.set(true);
		}
	},
	
	/** Event. */
	CLASS_PREPARE(EventKind.CLASS_PREPARE)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	CLASS_UNLOAD(EventKind.CLASS_UNLOAD)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	CLASS_LOAD(EventKind.CLASS_LOAD)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	FIELD_ACCESS(EventKind.FIELD_ACCESS)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	FIELD_MODIFICATION(EventKind.FIELD_MODIFICATION)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	EXCEPTION_CATCH(EventKind.EXCEPTION_CATCH)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	METHOD_ENTRY(EventKind.METHOD_ENTRY)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	METHOD_EXIT(EventKind.METHOD_EXIT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	METHOD_EXIT_WITH_RETURN_VALUE(EventKind.METHOD_EXIT_WITH_RETURN_VALUE)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_CONTENDED_ENTER(EventKind.MONITOR_CONTENDED_ENTER)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_CONTENDED_EXIT(EventKind.MONITOR_CONTENDED_EXIT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_WAIT(EventKind.MONITOR_WAIT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_WAITED(EventKind.MONITOR_WAITED)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	VM_START(EventKind.VM_START)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			StoredInfo<InfoThread> threadStore =
				__state.storedInfo.getThread();
			
			// Read in packet details
			JDWPId threadId = __packet.readId(JDWPIdKind.OBJECT_ID);
			InfoThread thread = threadStore.get(__state, threadId);
			
			// Alias of thread start since this gives the first initial thread
			EventProcessor.THREAD_START.process(__state, __packet, __suspend,
				__handler);
			
			// Set the VM as started
			__state.setStarted();
			
			// If the VM was started in the suspend state then this would be
			// known accordingly... so we need to resume the VM since we are
			// connected to it
			if (__suspend == SuspendPolicy.EVENT_THREAD)
				__state.threadResume(thread);
			else if (__suspend == SuspendPolicy.ALL)
				__state.threadResumeAll();
		}
	},
	
	/** Event. */
	VM_DEATH(EventKind.VM_DEATH)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	UNCONDITIONAL_BREAKPOINT(EventKind.UNCONDITIONAL_BREAKPOINT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			SuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},

	/* End. */
	;
	
	/** The mapping of event handlers. */
	private static final Map<EventKind, EventProcessor> _EVENT_MAP;
	
	/** The kind of event this is. */
	public final EventKind kind;
	
	static
	{
		Map<EventKind, EventProcessor> map = new EnumTypeMap<>(
			EventKind.class, EventKind.values());
		for (EventProcessor processor : EventProcessor.values())
			map.put(processor.kind, processor);
			
		_EVENT_MAP = map;
	}
	
	/**
	 * Initializes the event processor.
	 *
	 * @param __kind The kind of event this handles.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	EventProcessor(EventKind __kind)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		this.kind = __kind;
	}
	
	/**
	 * Processes the given event.
	 *
	 * @param __state The debugger state.
	 * @param __packet The packet to read from.
	 * @param __suspend The suspension policy that is used.
	 * @param __handler The handler for this specific event.
	 * @since 2024/01/19
	 */
	protected abstract void process(DebuggerState __state,
		JDWPPacket __packet, SuspendPolicy __suspend, EventHandler __handler);
	
	/**
	 * Returns the processor for the given event.
	 *
	 * @param __kind The kind of event this is.
	 * @return The resultant processor.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public static EventProcessor of(EventKind __kind)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		return EventProcessor._EVENT_MAP.get(__kind);
	}
	
	/**
	 * Handles the specified event.
	 *
	 * @param __state The input state.
	 * @param __packet The packet containing the event data.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public static void handle(DebuggerState __state, JDWPPacket __packet)
		throws NullPointerException
	{
		if (__state == null || __packet == null)
			throw new NullPointerException("NARG");
		
		// If the packet is blank, ignore it
		if (__packet.length() == 0)
			return;
		
		// Read the suspension policy
		SuspendPolicy suspend = SuspendPolicy.of(__packet.readByte());
		
		// Process all events
		int numEvents = __packet.readInt();
		for (int seq = 0; seq < numEvents; seq++)
		{
			// Is this event known?
			int rawKind = __packet.readByte();
			EventKind kind = EventKind.of(rawKind);
			if (kind == null)
			{
				Debugging.debugNote("Unknown event kind: %d", rawKind);
				return;
			}
			
			// The event request, find the optional handler for it
			int requestId = __packet.readInt();
			EventHandler handler = __state.eventHandler(requestId);
			
			// Find the processor to use
			EventProcessor processor = EventProcessor.of(kind);
			
			// Debug
			Debugging.debugNote(
				"Process event %d of type %s (suspend=%s)...",
				requestId, processor, suspend);
			
			// Process it
			processor.process(__state, __packet, suspend,
				handler);
		}
	}
}
