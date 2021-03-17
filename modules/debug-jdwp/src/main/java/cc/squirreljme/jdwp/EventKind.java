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
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum EventKind
	implements JDWPId
{
	/** Single Step. */
	SINGLE_STEP(1)
	{
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
	BREAKPOINT(2)
	{
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
	FRAME_POP(3)
	{
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
	EXCEPTION(4)
	{
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
	USER_DEFINED(5)
	{
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
	THREAD_START(6)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeId((JDWPThread)__args[0]);
		}
	},
	
	/** End of thread. */
	THREAD_DEATH(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeId((JDWPThread)__args[0]);
		}
	},
	
	/** Class being prepared. */
	CLASS_PREPARE(8)
	{
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
	
	/** Class unloading. */
	CLASS_UNLOAD(9)
	{
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
	CLASS_LOAD(10)
	{
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
	FIELD_ACCESS(20)
	{
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
	FIELD_MODIFICATION(21)
	{
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
	EXCEPTION_CATCH(30)
	{
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
	METHOD_ENTRY(40)
	{
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
	METHOD_EXIT(41)
	{
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
	METHOD_EXIT_WITH_RETURN_VALUE(42)
	{
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
	MONITOR_CONTENDED_ENTER(43)
	{
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
	MONITOR_CONTENDED_EXIT(44)
	{
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
	MONITOR_WAIT(45)
	{
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
	MONITOR_WAITED(46)
	{
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
	VM_START(90)
	{
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
	VM_DEATH(99)
	{
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
	
	/**
	 * Initializes the event kind.
	 * 
	 * @param __id The identifier.
	 * @since 2021/03/13
	 */
	EventKind(int __id)
	{
		this.id = __id;
	}
	
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
