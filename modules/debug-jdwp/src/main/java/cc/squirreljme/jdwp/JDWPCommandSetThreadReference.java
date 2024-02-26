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
 * Command set for thread support.
 *
 * @since 2021/03/13
 */
public enum JDWPCommandSetThreadReference
	implements JDWPCommand
{
	/** Thread name. */
	NAME(1),
	
	/** Suspend thread. */
	SUSPEND(2),
	
	/** Resume thread. */
	RESUME(3),
	
	/** Status of the thread. */
	STATUS(4),
	
	/** Thread group of a thread. */
	THREAD_GROUP(5),
	
	/** Frames. */
	FRAMES(6),
	
	/** Frame count. */
	FRAME_COUNT(7),
	
	/** Stops a thread, not supported in Java ME. */
	STOP(10),
	
	/** Interrupt the thread. */
	INTERRUPT(11),
	
	/** Suspension count for each thread. */
	SUSPEND_COUNT(12),
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	JDWPCommandSetThreadReference(int __id)
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
}
