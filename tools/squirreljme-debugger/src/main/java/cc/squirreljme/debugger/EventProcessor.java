// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
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
	SINGLE_STEP(JDWPEventKind.SINGLE_STEP)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	BREAKPOINT(JDWPEventKind.BREAKPOINT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	FRAME_POP(JDWPEventKind.FRAME_POP)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	EXCEPTION(JDWPEventKind.EXCEPTION)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	USER_DEFINED(JDWPEventKind.USER_DEFINED)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	THREAD_START(JDWPEventKind.THREAD_START)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			StoredInfo<InfoThread> threadStore =
				__state.storedInfo.getThreads();
			
			// Read in packet details
			JDWPId threadId = __packet.readId(JDWPIdKind.THREAD_ID);
			
			// Mark as started
			InfoThread thread = threadStore.get(__state, threadId);
			if (thread != null)
				thread.isStarted.set(true);
		}
	},
	
	/** Event. */
	THREAD_DEATH(JDWPEventKind.THREAD_DEATH)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			StoredInfo<InfoThread> threadStore =
				__state.storedInfo.getThreads();
			
			// Read in packet details
			JDWPId threadId = __packet.readId(JDWPIdKind.THREAD_ID);
			
			// Set dead state
			InfoThread thread = threadStore.get(__state, threadId);
			if (thread != null)
				thread.isDead.set(true);
		}
	},
	
	/** Event. */
	CLASS_PREPARE(JDWPEventKind.CLASS_PREPARE)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	CLASS_UNLOAD(JDWPEventKind.CLASS_UNLOAD)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	CLASS_LOAD(JDWPEventKind.CLASS_LOAD)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	FIELD_ACCESS(JDWPEventKind.FIELD_ACCESS)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	FIELD_MODIFICATION(JDWPEventKind.FIELD_MODIFICATION)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	EXCEPTION_CATCH(JDWPEventKind.EXCEPTION_CATCH)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	METHOD_ENTRY(JDWPEventKind.METHOD_ENTRY)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	METHOD_EXIT(JDWPEventKind.METHOD_EXIT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	METHOD_EXIT_WITH_RETURN_VALUE(JDWPEventKind.METHOD_EXIT_WITH_RETURN_VALUE)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_CONTENDED_ENTER(JDWPEventKind.MONITOR_CONTENDED_ENTER)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_CONTENDED_EXIT(JDWPEventKind.MONITOR_CONTENDED_EXIT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_WAIT(JDWPEventKind.MONITOR_WAIT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	MONITOR_WAITED(JDWPEventKind.MONITOR_WAITED)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	VM_START(JDWPEventKind.VM_START)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			StoredInfo<InfoThread> threadStore =
				__state.storedInfo.getThreads();
			
			// Read in packet details
			JDWPId threadId = __packet.readId(JDWPIdKind.OBJECT_ID);
			InfoThread thread = threadStore.get(__state, threadId);
			
			// Mark as started
			if (thread != null)
				thread.isStarted.set(true);
			
			// Set the VM as started
			__state.setStarted();
			
			// If the VM was started in the suspend state then this would be
			// known accordingly... so we need to resume the VM since we are
			// connected to it
			if (__suspend == JDWPSuspendPolicy.EVENT_THREAD)
				__state.threadResume(thread);
			else if (__suspend == JDWPSuspendPolicy.ALL)
				__state.threadResumeAll();
		}
	},
	
	/** Event. */
	VM_DEATH(JDWPEventKind.VM_DEATH)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},
	
	/** Event. */
	UNCONDITIONAL_BREAKPOINT(JDWPEventKind.UNCONDITIONAL_BREAKPOINT)
	{
		/**
		 * {@inheritDoc}
		 *
		 * @since 2024/01/19
		 */
		@Override
		protected void process(DebuggerState __state, JDWPPacket __packet,
			JDWPSuspendPolicy __suspend, EventHandler __handler)
		{
			throw Debugging.todo();
		}
	},

	/* End. */
	;
	
	/** The mapping of event handlers. */
	private static final Map<JDWPEventKind, EventProcessor> _EVENT_MAP;
	
	/** The kind of event this is. */
	public final JDWPEventKind kind;
	
	static
	{
		Map<JDWPEventKind, EventProcessor> map = new EnumTypeMap<>(
			JDWPEventKind.class, JDWPEventKind.values());
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
	EventProcessor(JDWPEventKind __kind)
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
		JDWPPacket __packet, JDWPSuspendPolicy __suspend,
		EventHandler __handler);
	
	/**
	 * Returns the processor for the given event.
	 *
	 * @param __kind The kind of event this is.
	 * @return The resultant processor.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public static EventProcessor of(JDWPEventKind __kind)
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
		JDWPSuspendPolicy suspend = JDWPSuspendPolicy.of(__packet.readByte());
		
		// Process all events
		int numEvents = __packet.readInt();
		for (int seq = 0; seq < numEvents; seq++)
		{
			// Is this event known?
			int rawKind = __packet.readByte();
			JDWPEventKind kind = JDWPEventKind.of(rawKind);
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
				"Process event #%d %d of type %s (suspend=%s)...",
				seq, requestId, processor, suspend);
			
			// Process it
			processor.process(__state, __packet, suspend,
				handler);
		}
	}
}
