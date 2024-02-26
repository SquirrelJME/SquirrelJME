// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.trips;

/**
 * A global trip event that is completely non-local.
 *
 * @since 2021/04/11
 */
public enum JDWPGlobalTrip
{
	/** Class preparation, loading, and unloading. */
	CLASS_STATUS(JDWPTripClassStatus.class),
	
	/** Fields. */
	FIELD(JDWPTripField.class),
	
	/** Virtual machine state. */
	VM_STATE(JDWPTripVmState.class),
	
	/** Trip on thread alive/death and states. */
	THREAD(JDWPTripThread.class),
	
	/* End. */
	;
	
	/** The trip class. */
	public final Class<? extends JDWPTrip> tripClass;
	
	/**
	 * The kind of global trip to get.
	 * 
	 * @param __tripClass The tripping class.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	JDWPGlobalTrip(Class<? extends JDWPTrip> __tripClass)
		throws NullPointerException
	{
		this.tripClass = __tripClass;
	}
}
