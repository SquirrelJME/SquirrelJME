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
 * Virtual machine command set.
 *
 * @since 2021/03/12
 */
public enum JDWPCommandSetVirtualMachine
	implements JDWPCommand
{
	/** Version information. */
	VERSION(1),
	
	/** Class search by signature. */
	CLASSES_BY_SIGNATURE(2),
	
	/** All Classes. */
	ALL_CLASSES(3),
	
	/** All Threads. */
	ALL_THREADS(4),
	
	/** Top level thread groups. */
	TOP_LEVEL_THREAD_GROUPS(5),
	
	/** Dispose of the debugging connection. */
	DISPOSE(6),
	
	/** Returns the size of variable data. */
	ID_SIZES(7),
	
	/** Suspend all threads. */
	SUSPEND(8),
	
	/** Resume all threads. */
	RESUME(9),
	
	/** Force exit the virtual machine. */
	EXIT(10),
	
	/** Capabilities. */
	CAPABILITIES(12),
	
	/** Class paths. */
	CLASS_PATHS(13),
	
	/** Hold events, keep them queued and not transmit them. */
	HOLD_EVENTS(15),
	
	/** Release any events. */
	RELEASE_EVENTS(16),
	
	/** New Capabilities. */
	CAPABILITIES_NEW(17),
	
	/** All loaded classes with generic signature included. */ 
	ALL_CLASSES_WITH_GENERIC_SIGNATURE(20),
		
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/12
	 */
	JDWPCommandSetVirtualMachine(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/12
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
