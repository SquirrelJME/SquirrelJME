// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum EventKind
	implements JDWPId
{
	/** Single Step. */
	SINGLE_STEP(1),
	
	/** Breakpoint. */
	BREAKPOINT(2),
	
	/** Frame pop. */
	FRAME_POP(3),
	
	/** Exception. */
	EXCEPTION(4),
	
	/** User defined. */
	USER_DEFINED(5),
	
	/** Start of thread. */
	THREAD_START(6),
	
	/** End of thread. */
	THREAD_DEATH(7),
	
	/** Class being prepared. */
	CLASS_PREPARE(8),
	
	/** Class unloading. */
	CLASS_UNLOAD(9),
	
	/** Class loading. */
	CLASS_LOAD(10),
	
	/** Field access. */
	FIELD_ACCESS(20),
	
	/** Field modification. */
	FIELD_MODIFICATION(21),
	
	/** Exception catch. */
	EXCEPTION_CATCH(30),
	
	/** Method entry. */
	METHOD_ENTRY(40),
	
	/** Method exit. */
	METHOD_EXIT(41),
	
	/** Method exit with return value. */
	METHOD_EXIT_WITH_RETURN_VALUE(42),
	
	/** Contended monitor enter. */
	MONITOR_CONTENDED_ENTER(43),
	
	/** Contended monitor exit. */
	MONITOR_CONTENDED_EXIT(44),
	
	/** Monitor wait. */
	MONITOR_WAIT(45),
	
	/** Monitor waited. */
	MONITOR_WAITED(46),
	
	/** Virtual machine start. */
	VM_START(90),
	
	/** Virtual machine death. */
	VM_DEATH(99),
	
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
