// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;

/**
 * This class stores anything that is within trace points and such.
 *
 * @since 2020/06/12
 */
public final class TracePointObject
	extends AbstractGhostObject
{
	/** The trace for the call. */
	protected final CallTraceElement trace;
	
	/**
	 * Initializes the trace point.
	 *
	 * @param __trace The trace to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/14
	 */
	public TracePointObject(CallTraceElement __trace)
		throws NullPointerException
	{
		if (__trace == null)
			throw new NullPointerException("NARG");
		
		this.trace = __trace;
	}
	
	/**
	 * Returns the trace.
	 *
	 * @return The trace.
	 * @since 2020/06/16
	 */
	public final CallTraceElement getTrace()
	{
		return this.trace;
	}
}
