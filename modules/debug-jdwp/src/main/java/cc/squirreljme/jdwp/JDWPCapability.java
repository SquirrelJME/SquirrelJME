// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents a single capability of the virtual machine.
 *
 * @since 2024/01/19
 */
public enum JDWPCapability
{
	/** canWatchFieldModification. */
	CAN_WATCH_FIELD_MODIFICATION("canWatchFieldModification"),
	
	/** canWatchFieldAccess. */
	CAN_WATCH_FIELD_ACCESS("canWatchFieldAccess"),
	
	/** canGetBytecodes. */
	CAN_GET_BYTECODES("canGetBytecodes"),
	
	/** canGetSyntheticAttribute. */
	CAN_GET_SYNTHETIC_ATTRIBUTE("canGetSyntheticAttribute"),
	
	/** canGetOwnedMonitorInfo. */
	CAN_GET_OWNED_MONITOR_INFO("canGetOwnedMonitorInfo"),
	
	/** canGetCurrentContendedMonitor. */
	CAN_GET_CURRENT_CONTENDED_MONITOR("canGetCurrentContendedMonitor"),
	
	/** canGetMonitorInfo. */
	CAN_GET_MONITOR_INFO("canGetMonitorInfo"),
	
	/** canRedefineClasses. */
	CAN_REDEFINE_CLASSES("canRedefineClasses"),
	
	/** canAddMethod. */
	CAN_ADD_METHOD("canAddMethod"),
	
	/** canUnrestrictedlyRedefineClasses. */
	CAN_UNRESTRICTEDLY_REDEFINE_CLASSES(
		"canUnrestrictedlyRedefineClasses"),
	
	/** canPopFrames. */
	CAN_POP_FRAMES("canPopFrames"),
	
	/** canUseInstanceFilters. */
	CAN_USE_INSTANCE_FILTERS("canUseInstanceFilters"),
	
	/** canGetSourceDebugExtension. */
	CAN_GET_SOURCE_DEBUG_EXTENSION("canGetSourceDebugExtension"),
	
	/** canRequestVMDeathEvent. */
	CAN_REQUEST_VMDEATH_EVENT("canRequestVmdeathEvent"),
	
	/** canSetDefaultStratum. */
	CAN_SET_DEFAULT_STRATUM("canSetDefaultStratum"),
	
	/** canGetInstanceInfo. */
	CAN_GET_INSTANCE_INFO("canGetInstanceInfo"),
	
	/** canRequestMonitorEvents. */
	CAN_REQUEST_MONITOR_EVENTS("canRequestMonitorEvents"),
	
	/** canGetMonitorFrameInfo. */
	CAN_GET_MONITOR_FRAME_INFO("canGetMonitorFrameInfo"),
	
	/** canUseSourceNameFilters. */
	CAN_USE_SOURCE_NAME_FILTERS("canUseSourceNameFilters"),
	
	/** canGetConstantPool. */
	CAN_GET_CONSTANT_POOL("canGetConstantPool"),
	
	/** canForceEarlyReturn. */
	CAN_FORCE_EARLY_RETURN("canForceEarlyReturn"),
	
	/** reserved22. */
	RESERVED_22("reserved22"),
	
	/** reserved23. */
	RESERVED_23("reserved23"),
	
	/** reserved24. */
	RESERVED_24("reserved24"),
	
	/** reserved25. */
	RESERVED_25("reserved25"),
	
	/** reserved26. */
	RESERVED_26("reserved26"),
	
	/** reserved27. */
	RESERVED_27("reserved27"),
	
	/** reserved28. */
	RESERVED_28("reserved28"),
	
	/** reserved29. */
	RESERVED_29("reserved29"),
	
	/** reserved30. */
	RESERVED_30("reserved30"),
	
	/** reserved31. */
	RESERVED_31("reserved31"),
	
	/** reserved32. */
	RESERVED_32("reserved32"),
	
	/* End. */
	;
	
	/** Capability values. */
	private static final JDWPCapability[] _VALUES =
		JDWPCapability.values();
	
	/** The capability name. */
	protected final String name;
	
	/**
	 * Initializes the capability.
	 *
	 * @param __name The name of the capability.
	 * @since 2024/01/19
	 */
	JDWPCapability(String __name)
	{
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Finds the capability for the given ID.
	 *
	 * @param __id The capability ID.
	 * @return The capability or {@code null} if not valid.
	 * @since 2024/01/19
	 */
	public static JDWPCapability of(int __id)
	{
		JDWPCapability[] values = JDWPCapability._VALUES;
		if (__id < 0 || __id >= values.length)
			return null;
		
		return values[__id];
	}
}
