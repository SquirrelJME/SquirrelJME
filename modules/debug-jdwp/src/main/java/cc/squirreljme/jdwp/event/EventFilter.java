// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

import cc.squirreljme.jdwp.JDWPLocation;

/**
 * This class is responsible for being a filter on any events that occur.
 *
 * @since 2021/04/17
 */
public final class EventFilter
{
	/** The call stack stepping. */
	protected final CallStackStepping callStackStepping;
	
	/** Which exceptions does this fire on? */
	protected final ExceptionOnly exception;
	
	/** Exclude the given class? */
	protected final ClassPatternMatcher excludeClass;
	
	/** Only on a specific field? */
	protected final FieldOnly fieldOnly;
	
	/** Include the given class? */
	protected final ClassPatternMatcher includeClass;
	
	/** The execution location. */
	protected final JDWPLocation location;
	
	/** The instance to check on, may be {@code null}. */
	protected final Object thisInstance;
	
	/** The thread to check on. */
	protected final Object thread;
	
	/** The type of class to check on. */
	protected final Object type;
	
	/**
	 * Initializes the event filter.
	 * 
	 * @param __thread The thread.
	 * @param __type The type.
	 * @param __includeClass The class to include.
	 * @param __excludeClass The class to exclude.
	 * @param __fieldOnly Only on this field.
	 * @param __location Only at this location.
	 * @param __thisInstance Only for the given instance.
	 * @param __exception Only for the given exception.
	 * @param __callStackStepping Call stepping.
	 * @since 2021/04/17
	 */
	public EventFilter(Object __thread, Object __type,
		ClassPatternMatcher __includeClass, ClassPatternMatcher __excludeClass,
		FieldOnly __fieldOnly, JDWPLocation __location, Object __thisInstance,
		ExceptionOnly __exception, CallStackStepping __callStackStepping)
	{
		this.thread = __thread;
		this.type = __type;
		this.includeClass = __includeClass;
		this.excludeClass = __excludeClass;
		this.fieldOnly = __fieldOnly;
		this.location = __location;
		this.thisInstance = __thisInstance;
		this.exception = __exception;
		this.callStackStepping = __callStackStepping;
	}
}
