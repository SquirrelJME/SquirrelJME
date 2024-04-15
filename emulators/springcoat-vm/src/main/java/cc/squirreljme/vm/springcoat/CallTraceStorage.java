// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;

/**
 * Storage for call traces.
 *
 * @since 2020/07/06
 */
public final class CallTraceStorage
{
	/** The message. */
	public final String message;
	
	/** The trace. */
	private final CallTraceElement[] _trace;
	
	/**
	 * Initializes the call trace storage.
	 * 
	 * @param __message The message.
	 * @param __trace The trace.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/06
	 */
	public CallTraceStorage(String __message, CallTraceElement... __trace)
		throws NullPointerException
	{
		if (__message == null || __trace == null)
			throw new NullPointerException("NARG");
		
		this.message = __message;
		this._trace = __trace.clone();
	}
	
	/**
	 * Returns the stored call trace.
	 * 
	 * @return The traces.
	 * @since 2020/07/08
	 */
	public final CallTraceElement[] trace()
	{
		return this._trace.clone();
	}
}
