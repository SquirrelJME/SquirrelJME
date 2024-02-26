// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.event;

import cc.squirreljme.jdwp.JDWPClassPatternMatcher;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPEventModifierContext;
import cc.squirreljme.jdwp.host.JDWPHostController;
import cc.squirreljme.jdwp.host.JDWPHostLocation;
import cc.squirreljme.jdwp.host.JDWPHostStepTracker;
import cc.squirreljme.jdwp.host.JDWPHostUtils;
import cc.squirreljme.jdwp.host.JDWPHostValue;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class is responsible for being a filter on any events that occur.
 *
 * @since 2021/04/17
 */
public final class JDWPHostEventFilter
{
	/** Static field. */
	private static final byte _STATIC_FIELD =
		0x08;
	
	/** The execution location. */
	public final JDWPHostLocation location;
	
	/** The call stack stepping. */
	public final JDWPHostCallStackStepping callStackStepping;
	
	/** Which exceptions does this fire on? */
	protected final JDWPHostExceptionOnly exception;
	
	/** Exclude the given class? */
	protected final JDWPClassPatternMatcher excludeClass;
	
	/** Only on a specific field? */
	public final JDWPHostFieldOnly fieldOnly;
	
	/** Include the given class? */
	protected final JDWPClassPatternMatcher includeClass;
	
	/** The instance to check on, may be {@code null}. */
	protected final Object thisInstance;
	
	/** Is this instance set? */
	protected final boolean thisInstanceSet;
	
	/** The thread to check on. */
	public final Object thread;
	
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
	 * @param __thisInstanceSet Is this instance set?
	 * @param __thisInstance Only for the given instance.
	 * @param __exception Only for the given exception.
	 * @param __callStackStepping Call stepping.
	 * @since 2021/04/17
	 */
	public JDWPHostEventFilter(Object __thread, Object __type,
		JDWPClassPatternMatcher __includeClass, JDWPClassPatternMatcher __excludeClass,
		JDWPHostFieldOnly __fieldOnly, JDWPHostLocation __location,
		boolean __thisInstanceSet, Object __thisInstance,
		JDWPHostExceptionOnly __exception, JDWPHostCallStackStepping __callStackStepping)
	{
		this.thread = __thread;
		this.type = __type;
		this.includeClass = __includeClass;
		this.excludeClass = __excludeClass;
		this.fieldOnly = __fieldOnly;
		this.location = __location;
		this.thisInstance = (__thisInstanceSet ? __thisInstance : null);
		this.thisInstanceSet = __thisInstanceSet;
		this.exception = __exception;
		this.callStackStepping = __callStackStepping;
	}
	
	/**
	 * Does this have type matching?
	 * 
	 * @return If this has type matching?
	 * @since 2021/04/25
	 */
	public boolean hasTypeMatch()
	{
		return this.type != null ||
			this.includeClass != null ||
			this.excludeClass != null;
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
	public boolean meets(JDWPHostController __controller, Object __thread,
		JDWPEventKind __kind, Object... __args)
		throws NullPointerException
	{
		if (__controller == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Check the general context for mis-matches
		for (JDWPEventModifierContext context : __kind.contextGeneral())
			if (!this.__context(__controller, __thread, context, null,
				null))
				return false;
		
		// Handle each argument and find mismatches
		Object[] ensnare = new Object[1];
		for (int i = 0, n = __args.length; i < n; i++)
		{
			// Check that there is context here 
			JDWPEventModifierContext context = __kind.contextArgument(i);
			if (context == null)
				continue;
			
			// Check the context if it is valid
			if (!this.__context(__controller, __thread, context, __args[i],
				ensnare))
				return false;
		}
		
		// If this was reached then would have all been matched
		return true;
	}
	
	/**
	 * Checks if this meets the given type.
	 * 
	 * @param __viewType The type viewer.
	 * @param __arg The argument.
	 * @return If this meets the given type.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/18
	 */
	public boolean meetsType(JDWPViewType __viewType, Object __arg)
		throws NullPointerException
	{
		if (__viewType == null)
			throw new NullPointerException("NARG");
		
		// Mismatched type?
		Object type = this.type;
		if (type != null && type != __arg)
			return false;
		
		// Not an included class?
		JDWPClassPatternMatcher includeClass = this.includeClass;
		JDWPClassPatternMatcher excludeClass = this.excludeClass;
		if (includeClass != null || excludeClass != null)
		{
			// Get the runtime name of the class
			String runtimeName = JDWPHostUtils.signatureToRuntime(
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
	 * Checks the context.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The current thread.
	 * @param __context The context to check for.
	 * @param __on The object to test on.
	 * @param __ensnare The ensnare storage.
	 * @return If this is mismatched.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/25
	 */
	private boolean __context(JDWPHostController __controller, Object __thread,
		JDWPEventModifierContext __context, Object __on, Object[] __ensnare)
		throws NullPointerException
	{
		if (__controller == null || __context == null)
			throw new NullPointerException("NARG");
		
		// Viewers
		JDWPViewObject viewObject = __controller.viewObject();
		JDWPViewType viewType = __controller.viewType();
		
		// Depends on the context
		switch (__context)
		{
				// Ensnare the argument, so it can be used for later
				// This is always valid.
			case ENSNARE_ARGUMENT:
				if (__ensnare != null && __ensnare.length > 0)
					__ensnare[0] = __on;
				break;
				
				// The current thread
			case CURRENT_THREAD:
				// Thread is only valid if it is set
				if (this.thread != null && __thread != this.thread)
					return false;
				break;
				
				// Current location in code
			case CURRENT_LOCATION:
				if (this.location != null)
				{
					JDWPHostLocation location = this.location;
					
					// With no current thread, we have no idea where we are
					// even located
					if (__thread == null)
						return false;
					
					// Is not the same location?
					if (!location.equals(__controller.locationOf(__thread)))
						return false;
				}
				break;
				
				// Current type being called in the class
			case CURRENT_TYPE:
				if (this.hasTypeMatch())
				{
					// If no thread is available, we have no idea where we are
					if (__thread == null)
						return false;
					
					// We are at the wrong location for this?
					Object type = __controller.locationOf(__thread).type;
					if (!viewType.isValid(type) ||
						!this.meetsType(viewType, type))
						return false;
				}
				break;
				
				// Current this type
			case CURRENT_INSTANCE:
				if (this.thisInstanceSet)
				{
					// If no thread is available, we have no idea where we are
					if (__thread == null)
						return false;
					
					// Is this method static? Determines if we match the
					// instance or not
					JDWPHostLocation location = __controller.locationOf(
						__thread);
					boolean isStatic = (__controller.viewType()
						.methodFlags(location.type, location.methodDx) &
							JDWPHostEventFilter._STATIC_FIELD) != 0;
						
					// Is this a static method?
					Object thisInstance = this.thisInstance;
					if (isStatic)
					{
						// If this instance is not-null then we can never match
						// a static method
						if (null != thisInstance)
							return false;
					}
					
					// Otherwise for instance methods we need to do much more
					// work to get the this object
					else
					{
						// No available frames?
						Object[] frames = __controller.viewThread().frames(
							__thread);
						if (frames == null || frames.length == 0)
							return false;
						
						// Get the this from the current frame
						Object frame = frames[0];
						try (JDWPHostValue val = __controller.value())
						{
							// Read in the value
							if (!__controller.viewFrame()
								.readValue(frame, 0, val))
								val.set(null);
							
							// Is this the wrong object?
							if (val.get() != thisInstance)
								return false;
						}
					}
				}
				break;
				
				// A field parameter
			case PARAMETER_FIELD:
				if (this.fieldOnly != null)
				{
					// Only works on number parameters
					if (!(__on instanceof Number))
						return false;
						
					// Is this the wrong field?
					JDWPHostFieldOnly fieldOnly = this.fieldOnly;
					if (fieldOnly.fieldDx != ((Number)__on).intValue())
						return false;
				}
				break;
				
				// Parameter based on a type or field
			case PARAMETER_TYPE_OR_FIELD:
				if (this.fieldOnly != null)
				{
					// Is this the wrong field?
					JDWPHostFieldOnly fieldOnly = this.fieldOnly;
					if (fieldOnly.type != __on)
						return false;
				}
				
				// Forward to type check and see if that is used
				if (!this.__context(__controller, __thread,
					JDWPEventModifierContext.PARAMETER_TYPE, __on, __ensnare))
					return false;
				break;
			
				// A parameter based type
			case PARAMETER_TYPE:
				if (this.hasTypeMatch())
				{
					if (!viewType.isValid(__on) ||
						!this.meetsType(viewType, __on))
						return false;
				}
				break;
				
				// Stepping for a given thread
			case PARAMETER_STEPPING:
				{
					// Not a tracker? Do nothing
					if (!(__on instanceof JDWPHostStepTracker))
						return false;
					
					// Get the tracker and the stepping
					JDWPHostStepTracker stepTracker = (JDWPHostStepTracker)__on;
					JDWPHostCallStackStepping stepping = this.callStackStepping;
					
					// No stepping here? Cannot be a match
					if (stepping == null)
						return false;
					
					// We can only reliably check on the thread and the depth
					// requested.
					if (__thread != stepping.thread ||
						stepping.depth != stepTracker.depth())
						return false;
				}
				break;
			
				// An exception has been tossed, is it caught or uncaught and
				// is it the one we want?
			case TOSSED_EXCEPTION:
				if (this.exception != null)
				{
					JDWPHostExceptionOnly exception = this.exception;
					
					// Caught/uncaught mismatch?
					if (__on == null && !exception.uncaught)
						return false;
					else if (__on != null && !exception.caught)
						return false;
					
					// Type mismatch?
					if (__ensnare != null && __ensnare[0] != null &&
						exception.optionalType != null)
					{
						Object object = __ensnare[0];
						
						// Do not know what this object is...
						if (viewObject.isValid(object))
							return false;
						
						// Do not know what type we are looking for...
						if (viewType.isValid(exception.optionalType))
							return false;
						
						// Cannot cast from this type, to the other type
						if (!viewType.canCastTo(exception.optionalType,
							viewObject.type(object)))
							return false;
					}
				}
				break;
			
			default:
				throw Debugging.oops(__context);
		}
		
		// Is okay
		return true;
	}
}
