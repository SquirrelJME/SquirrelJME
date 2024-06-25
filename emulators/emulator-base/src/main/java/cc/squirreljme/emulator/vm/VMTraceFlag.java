// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.jvm.mle.constants.VerboseDebugFlag;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableSet;

/**
 * Represents a trace flag.
 *
 * @since 2024/01/14
 */
public enum VMTraceFlag
{
	/* Enable all. */
	ALL("all", VerboseDebugFlag.ALL),

	/* Instructions being executed. */
	INSTRUCTIONS("instructions", VerboseDebugFlag.INSTRUCTIONS),

	/* Entry of methods. */
	METHOD_ENTRY("methodEntry", VerboseDebugFlag.METHOD_ENTRY),

	/* Exit of methods. */
	METHOD_EXIT("methodExit", VerboseDebugFlag.METHOD_EXIT),

	/* SquirrelJME MLE Native Calls. */
	MLE_CALL("mleCall", VerboseDebugFlag.MLE_CALL),

	/* Static invocations. */
	INVOKE_STATIC("invokeStatic", VerboseDebugFlag.INVOKE_STATIC),

	/* Allocations such as `new`. */
	ALLOCATION("allocation", VerboseDebugFlag.ALLOCATION),

	/* Class is initialized. */
	CLASS_INITIALIZE("classInitialize",
		VerboseDebugFlag.CLASS_INITIALIZE),

	/* Virtual machine exceptions. */
	VM_EXCEPTION("vmException", VerboseDebugFlag.VM_EXCEPTION),

	/* Class does not exist. */
	MISSING_CLASS("missingClass", VerboseDebugFlag.MISSING_CLASS),

	/* Monitor is entered. */
	MONITOR_ENTER("monitorEnter", VerboseDebugFlag.MONITOR_ENTER),

	/* Exiting a monitor. */
	MONITOR_EXIT("monitorExit", VerboseDebugFlag.MONITOR_EXIT),

	/* Monitor is waited on. */
	MONITOR_WAIT("monitorWait", VerboseDebugFlag.MONITOR_WAIT),

	/* Monitor is notified. */
	MONITOR_NOTIFY("monitorNotify", VerboseDebugFlag.MONITOR_NOTIFY),

	/* New thread is created. */
	THREAD_NEW("threadNew", VerboseDebugFlag.THREAD_NEW),
	
	/** Method cycles. */
	METHOD_CYCLES("methodCycles", VerboseDebugFlag.METHOD_CYCLES),
	
	/** Ignored exceptions. */
	IGNORED_EXCEPTIONS("ignoredExceptions",
		VerboseDebugFlag.IGNORED_EXCEPTIONS),

	/* End. */
	;
	
	/** The name of the flag. */
	public final String name;
	
	/** The set of possible names. */
	public final Set<String> names; 
	
	/** The bit flag in {@link VerboseDebugFlag}. */
	public final int bits;
	
	/**
	 * Initializes the constant.
	 *
	 * @param __name The name of the flag.
	 * @param __bits The MLE flag.
	 * @since 2024/01/14
	 */
	VMTraceFlag(String __name, int __bits)
	{
		this.name = __name;
		this.bits = __bits;
		
		// Figure out the names to use, ignore case
		Set<String> names = new SortedTreeSet<>(String.CASE_INSENSITIVE_ORDER);
		this.names = UnmodifiableSet.of(names);
		
		// Use both the standard name and the enum base name
		names.add(this.name());
		names.add(__name);
	}
}
