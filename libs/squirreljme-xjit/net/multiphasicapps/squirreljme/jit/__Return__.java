// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This class contains the return state which is clear and values placed for
 * each operation which is executed.
 *
 * @since 2017/05/09
 */
final class __Return__
{
	/** How execution continues when an instruction finishes. */
	private volatile __ExecutionFlow__ _flow;
	
	/**
	 * Initializes the return state.
	 *
	 * @since 2017/05/09
	 */
	__Return__()
	{
		reset();
	}
	
	/**
	 * Returns the flow which execution follows when the instruction finishes.
	 *
	 * @return The flow used.
	 * @throws IllegalStateException If no flow was specified.
	 * @since 2017/05/10
	 */
	public __ExecutionFlow__ flow()
		throws JITException
	{
		// {@squirreljme.error AQ29 No instruction flow has been specified.}
		__ExecutionFlow__ rv = this._flow;
		if (rv == null)
			throw new IllegalStateException("AQ29");
		
		return rv;
	}
	
	/**
	 * Clears the state and resets everything to their default values.
	 *
	 * @since 2017/05/09
	 */
	public void reset()
	{
		this._flow = null;
	}
	
	/**
	 * Sets the flow that occurs when the instruction terminates.
	 *
	 * @param __f The flow to set.
	 * @throws IllegalStateException If a flow was already set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/11
	 */
	public void setFlow(__ExecutionFlow__ __f)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ2a A flow was already specified.}
		if (this._flow != null)
			throw new IllegalStateException("AQ2a");
		
		// Set
		this._flow = __f;
	}
}

