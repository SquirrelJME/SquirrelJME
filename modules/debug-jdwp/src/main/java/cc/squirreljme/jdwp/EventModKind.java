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
 * Event modifier kind.
 *
 * @since 2021/03/13
 */
public enum EventModKind
	implements JDWPId
{
	/** Limit occurrences. */
	LIMIT_OCCURRENCES(1),
	
	/** Conditional expression. */
	CONDITIONAL(2),
	
	/** Only in the given thread. */	
	ONLY_IN_THREAD(3),
	
	/** Only in the given class. */
	ONLY_IN_CLASS(4),
	
	/** Only in the given class, by pattern. */
	ONLY_IN_CLASS_PATTERN(5),
	
	/** Not in the given class, by pattern. */
	NOT_IN_CLASS_PATTERN(6),
	
	/** By location. */
	LOCATION(7),
	
	/** By exception, caught/uncaught. */
	EXCEPTION(8),
	
	/** By field. */
	FIELD(9),
	
	/** Call stack and stepping limit. */
	CALL_STACK_STEPPING(10),
	
	/** This object. */
	THIS_OBJECT(11),
	
	/** The source file name. */
	SOURCE_FILENAME_PATTERN(12),
	
	/* End. */
	;
	
	/** Quick table. */
	private static final __QuickTable__<EventModKind> _QUICK =
		new __QuickTable__<>(EventModKind.values());
	
	/** The event ID. */
	public final int id;
	
	/**
	 * Initializes the constant.
	 * 
	 * @param __id The identifier.
	 * @since 2021/03/13
	 */
	EventModKind(int __id)
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
	public static EventModKind of(int __id)
	{
		return EventModKind._QUICK.get(__id);
	}
}
