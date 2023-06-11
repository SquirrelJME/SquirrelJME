// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.constants.VerboseDebugFlag;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Not Described.
 *
 * @since 2023/02/10
 */
public enum VerboseDebugFlagName
{
	/** All verbosity settings. */
	ALL(VerboseDebugFlag.ALL),
	
	/** Be verbose on the called instructions. */
	INSTRUCTIONS(VerboseDebugFlag.INSTRUCTIONS),
	
	/** Be verbose on the entered methods. */
	METHOD_ENTRY(VerboseDebugFlag.METHOD_ENTRY),
	
	/** Be verbose on exited methods. */
	METHOD_EXIT(VerboseDebugFlag.METHOD_EXIT),
	
	/** Be verbose on MLE calls. */
	MLE_CALL(VerboseDebugFlag.MLE_CALL),
	
	/** Be verbose on static invocations. */
	INVOKE_STATIC(VerboseDebugFlag.INVOKE_STATIC),
	
	/** Be verbose on allocations. */
	ALLOCATION(VerboseDebugFlag.ALLOCATION),
	
	/** Be verbose on class initializations. */
	CLASS_INITIALIZE(VerboseDebugFlag.CLASS_INITIALIZE),
	
	/** Virtual machine exceptions. */
	VM_EXCEPTION(VerboseDebugFlag.VM_EXCEPTION),
	
	/** Class lookup failures. */
	MISSING_CLASS(VerboseDebugFlag.MISSING_CLASS),
	
	/** Monitor entry. */
	MONITOR_ENTER(VerboseDebugFlag.MONITOR_ENTER),
	
	/** Monitor exit. */
	MONITOR_EXIT(VerboseDebugFlag.MONITOR_EXIT),
	
	/** Wait on monitor. */
	MONITOR_WAIT(VerboseDebugFlag.MONITOR_WAIT),
	
	/** Notify on a monitor. */
	MONITOR_NOTIFY(VerboseDebugFlag.MONITOR_NOTIFY),
	
	/** Inherit the current verbose checks to another thread. */
	INHERIT_VERBOSE_FLAGS(VerboseDebugFlag.INHERIT_VERBOSE_FLAGS),
	
	/** New thread is created. */
	THREAD_NEW(VerboseDebugFlag.THREAD_NEW),
	
	/* End. */
	;
	
	/** The names for this. */
	public final Set<String> names;
	
	/** The bits to set. */
	public final int bits;
	
	/**
	 * Initializes the debug flag bits.
	 * 
	 * @param __bits The bits used.
	 * @since 2023/02/10
	 */
	VerboseDebugFlagName(int __bits)
	{
		this.bits = __bits;
		
		// Figure out the names to use, ignore case
		Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		this.names = Collections.unmodifiableSet(names);
		
		// Use base name
		String name = this.name();
		names.add(name);
		
		// Underscores to dashes
		names.add(name.replace('_', '-'));
		
		// Removed underscores
		names.add(name.replaceAll(Pattern.quote("_"), ""));
	}
}
