// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Event modifier kind.
 *
 * @since 2021/03/13
 */
public enum JDWPEventModifierKind
	implements JDWPHasId
{
	/** Limit occurrences. */
	LIMIT_OCCURRENCES(1),
	
	/** Only in the given thread. */	
	THREAD_ONLY(3),
	
	/** Only in the given class. */
	CLASS_ONLY(4),
	
	/** Only in the given class, by pattern. */
	CLASS_MATCH_PATTERN(5),
	
	/** Not in the given class, by pattern. */
	CLASS_EXCLUDE_PATTERN(6),
	
	/** By location. */
	LOCATION_ONLY(7),
	
	/** By exception, caught/uncaught. */
	EXCEPTION_ONLY(8),
	
	/** By field. */
	FIELD_ONLY(9),
	
	/** Call stack and stepping limit. */
	CALL_STACK_STEPPING(10),
	
	/** This object. */
	THIS_INSTANCE_ONLY(11),
	
	/* End. */
	;
	
	/** Quick table. */
	private static final JDWPIdMap<JDWPEventModifierKind> _QUICK =
		new JDWPIdMap<>(JDWPEventModifierKind.values());
	
	/** The event ID. */
	public final int id;
	
	/**
	 * Initializes the constant.
	 * 
	 * @param __id The identifier.
	 * @since 2021/03/13
	 */
	JDWPEventModifierKind(int __id)
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
	public static JDWPEventModifierKind of(int __id)
	{
		return JDWPEventModifierKind._QUICK.get(__id);
	}
}
