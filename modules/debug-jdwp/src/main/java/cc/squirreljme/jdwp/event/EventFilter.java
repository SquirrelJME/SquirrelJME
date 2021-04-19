// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

import cc.squirreljme.jdwp.EventKind;
import cc.squirreljme.jdwp.JDWPController;
import cc.squirreljme.jdwp.JDWPLocation;
import cc.squirreljme.jdwp.JDWPUtils;
import cc.squirreljme.jdwp.views.JDWPViewType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

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
	
	/**
	 * Checks if this filter meets the given criteria for the event.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The thread where this is coming from.
	 * @param __kind The kind of event.
	 * @param __args The arguments to the event.
	 * @return If this meets or not.
	 * @since 2021/04/18
	 */
	public boolean meets(JDWPController __controller, Object __thread,
		EventKind __kind, Object... __args)
		throws NullPointerException
	{
		if (__controller == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Match against the current thread?
		if (this.thread != null && __thread != this.thread)
			return false;
		
		// Any needed viewer
		JDWPViewType viewType = __controller.viewType();
		
		// Handle each argument and find mismatches
		for (int i = 0, n = __args.length; i < n; i++)
		{
			// Check that there is context here 
			EventModContext context = __kind.modifierContext(i);
			if (context == null)
				continue;
			
			// Depends on the context
			Object arg = __args[i];
			switch (context)
			{
					// A parameter based type
				case PARAMETER_TYPE:
					if (!viewType.isValid(arg) ||
						this.__meetsType(viewType, arg))
						return false;
					break;
				
				default:
					throw Debugging.oops(context);
			}
		}
		
		// If this was reached then would have all been matched
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/18
	 */
	@Override
	public String toString()
	{
		return String.format(
			"EventFilter{callStackStepping=%s, " +
			"exception=%s, excludeClass=%s, " +
			"fieldOnly=%s, " +
			"includeClass=%s, " +
			"location=%s, " +
			"thisInstance=%s, " +
			"thread=%s, " +
			"type=%s}", this.callStackStepping, this.exception,
			this.excludeClass, this.fieldOnly, this.includeClass,
			this.location, this.thisInstance, this.thread, this.type);
	}
	
	/**
	 * Checks if this meets the given type.
	 * 
	 * @param __viewType The type viewer.
	 * @param __arg The argument.
	 * @return If this meets the given type.
	 * @since 2021/04/18
	 */
	private boolean __meetsType(JDWPViewType __viewType, Object __arg)
	{
		// Mismatched type?
		Object type = this.type;
		if (type != null && type != __arg)
			return false;
		
		// Not an included class?
		ClassPatternMatcher includeClass = this.includeClass;
		ClassPatternMatcher excludeClass = this.excludeClass;
		if (includeClass != null || excludeClass != null)
		{
			// Get the runtime name of the class
			String runtimeName = JDWPUtils.signatureToRuntime(
				__viewType.signature(__arg));
			
			// Is not an included class?
			if (includeClass != null && !includeClass.meets(runtimeName))
				return false;
			
			// Is an excluded class?
			return excludeClass == null || !excludeClass.meets(runtimeName);
		}
		
		// If not failed, this meets!
		return true;
	}
}
