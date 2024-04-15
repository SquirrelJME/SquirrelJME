// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.EmptyList;
import net.multiphasicapps.collections.UnmodifiableIterable;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum JDWPEventKind
	implements JDWPHasId
{
	/** Single Step. */
	SINGLE_STEP(1,
		Arrays.asList(JDWPEventModifierContext.CURRENT_THREAD,
			JDWPEventModifierContext.CURRENT_TYPE,
			JDWPEventModifierContext.CURRENT_INSTANCE),
		Arrays.asList(JDWPEventModifierContext.PARAMETER_STEPPING),
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.LOCATION_ONLY, 
		JDWPEventModifierKind.CALL_STACK_STEPPING,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Breakpoint. */
	BREAKPOINT(2, Arrays.asList(JDWPEventModifierContext.CURRENT_THREAD,
			JDWPEventModifierContext.CURRENT_LOCATION,
			JDWPEventModifierContext.CURRENT_INSTANCE),
		null,
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.LOCATION_ONLY, 
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Frame pop. */
	FRAME_POP(3, null, null,
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Exception. */
	EXCEPTION(4, null,
		Arrays.asList(
			JDWPEventModifierContext.ENSNARE_ARGUMENT,
			JDWPEventModifierContext.TOSSED_EXCEPTION),
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.LOCATION_ONLY, 
		JDWPEventModifierKind.EXCEPTION_ONLY,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** User defined. */
	USER_DEFINED(5, null, null,
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Start of thread. */
	THREAD_START(6, null, null, 
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** End of thread. */
	THREAD_DEATH(7, null, null, 
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Class being prepared. */
	CLASS_PREPARE(8, null,
		Arrays.asList(JDWPEventModifierContext.PARAMETER_TYPE),
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Class unloading. */
	CLASS_UNLOAD(9, null, null,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN,
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Class loading. */
	CLASS_LOAD(10, null, null,
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Field access. */
	FIELD_ACCESS(20,
		Arrays.asList(JDWPEventModifierContext.CURRENT_THREAD,
			JDWPEventModifierContext.CURRENT_LOCATION,
			JDWPEventModifierContext.CURRENT_TYPE,
			JDWPEventModifierContext.CURRENT_INSTANCE),
		Arrays.asList(JDWPEventModifierContext.PARAMETER_TYPE_OR_FIELD,
			JDWPEventModifierContext.PARAMETER_FIELD),
		JDWPEventModifierKind.THREAD_ONLY, JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN,
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.LOCATION_ONLY, 
		JDWPEventModifierKind.FIELD_ONLY,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Field modification. */
	FIELD_MODIFICATION(21,
		Arrays.asList(JDWPEventModifierContext.CURRENT_THREAD,
			JDWPEventModifierContext.CURRENT_LOCATION,
			JDWPEventModifierContext.CURRENT_TYPE,
			JDWPEventModifierContext.CURRENT_INSTANCE),
		Arrays.asList(JDWPEventModifierContext.PARAMETER_TYPE_OR_FIELD,
			JDWPEventModifierContext.PARAMETER_FIELD),
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY, 
		JDWPEventModifierKind.CLASS_MATCH_PATTERN,
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN, 
		JDWPEventModifierKind.LOCATION_ONLY,
		JDWPEventModifierKind.FIELD_ONLY, 
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Exception catch. */
	EXCEPTION_CATCH(30, null,
		Arrays.asList(
			JDWPEventModifierContext.ENSNARE_ARGUMENT,
			JDWPEventModifierContext.TOSSED_EXCEPTION),
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.LOCATION_ONLY, 
		JDWPEventModifierKind.EXCEPTION_ONLY,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Method entry. */
	METHOD_ENTRY(40,
		Arrays.asList(JDWPEventModifierContext.CURRENT_LOCATION,
			JDWPEventModifierContext.CURRENT_INSTANCE,
			JDWPEventModifierContext.CURRENT_TYPE),
		null,
		JDWPEventModifierKind.THREAD_ONLY, 
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Method exit. */
	METHOD_EXIT(41,
		Arrays.asList(JDWPEventModifierContext.CURRENT_LOCATION,
			JDWPEventModifierContext.CURRENT_INSTANCE,
			JDWPEventModifierContext.CURRENT_TYPE),
		null,
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Method exit with return value. */
	METHOD_EXIT_WITH_RETURN_VALUE(42, null, null,
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY, 
		JDWPEventModifierKind.CLASS_MATCH_PATTERN,
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Contended monitor enter. */
	MONITOR_CONTENDED_ENTER(43, null, null,
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY, 
		JDWPEventModifierKind.CLASS_MATCH_PATTERN,
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN, 
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Contended monitor exit. */
	MONITOR_CONTENDED_EXIT(44, null, null,
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN,
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Monitor wait. */
	MONITOR_WAIT(45, null, null, 
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Monitor waited. */
	MONITOR_WAITED(46, null, null, 
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.CLASS_ONLY,
		JDWPEventModifierKind.CLASS_MATCH_PATTERN, 
		JDWPEventModifierKind.CLASS_EXCLUDE_PATTERN,
		JDWPEventModifierKind.THIS_INSTANCE_ONLY, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Virtual machine start. */
	VM_START(90, null, null, 
		JDWPEventModifierKind.THREAD_ONLY,
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Virtual machine death. */
	VM_DEATH(99, null, null, 
		JDWPEventModifierKind.LIMIT_OCCURRENCES),
	
	/** Special alias to indicate an unconditional {@link #BREAKPOINT}. */
	UNCONDITIONAL_BREAKPOINT(-2, null, null),
	
	/* End. */
	;
	
	/** Quick table. */
	private static final JDWPIdMap<JDWPEventKind> _QUICK =
		new JDWPIdMap<>(JDWPEventKind.values());
	
	/** The event ID. */
	public final int id;
	
	/** The modifier ordinal bits, for quicker lookup. */
	private final int _modifierBits;
	
	/** Non-argument context. */
	private final Iterable<JDWPEventModifierContext> _nonArg;
	
	/** Argument context. */
	private final List<JDWPEventModifierContext> _arg;
	
	/**
	 * Initializes the event kind.
	 * 
	 * @param __id The identifier.
	 * @param __nonArg Non-argument context.
	 * @param __arg Argument context.
	 * @param __modifiers The possible supported modifiers for this event.
	 * @since 2021/03/13
	 */
	JDWPEventKind(int __id, Iterable<JDWPEventModifierContext> __nonArg,
		List<JDWPEventModifierContext> __arg, 
		JDWPEventModifierKind... __modifiers)
	{
		this.id = __id;
		
		// Contexts for modifiers
		this._nonArg = (__nonArg == null ? 
			EmptyList.<JDWPEventModifierContext>empty() :
			UnmodifiableIterable.<JDWPEventModifierContext>of(__nonArg));
		this._arg = (__arg == null ? 
			EmptyList.<JDWPEventModifierContext>empty() :
			UnmodifiableList.<JDWPEventModifierContext>of(__arg));
		
		// Determine the modifier bits to quickly get the items
		int modifierBits = 0;
		for (JDWPEventModifierKind mod : __modifiers)
			modifierBits |= (1 << mod.ordinal());
		this._modifierBits = modifierBits;
	}
	
	/**
	 * Returns the context for the given argument.
	 * 
	 * @param __i The argument index, the first value should always be
	 * {@code thread}.
	 * @return The context for the given argument, will be {@code null} if
	 * not valid for negative values.
	 * @since 2021/04/17
	 */
	public final JDWPEventModifierContext contextArgument(int __i)
	{
		List<JDWPEventModifierContext> arg = this._arg;
		if (__i < 0 || __i >= arg.size())
			return null;
		
		return arg.get(__i);
	}
	
	/**
	 * Returns the general context.
	 * 
	 * @return The general context.
	 * @since 2021/04/25
	 */
	public final Iterable<JDWPEventModifierContext> contextGeneral()
	{
		return this._nonArg;
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
	 * Checks if the given modifier is valid for this.
	 * 
	 * @param __mod The modifier to check.
	 * @return Is this a valid modifier?
	 * @since 2021/04/17
	 */
	public final boolean isValidModifier(JDWPEventModifierKind __mod)
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
	public static JDWPEventKind of(int __id)
	{
		return JDWPEventKind._QUICK.get(__id);
	}
}
