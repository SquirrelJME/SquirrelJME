// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This is the holder for kernel traces so that all traces are mapped to a
 * single process which forwards the trace to a currently assigned tracer (if
 * one exists).
 *
 * @since 2016/05/21
 */
final class __KernelTraceHolder__
	extends KernelTrace
{
	/** The trace to forward into. */
	private volatile KernelTrace _trace;
	
	/**
	 * Initializes the base holder.
	 *
	 * @since 2016/05/21
	 */
	__KernelTraceHolder__()
	{
	}
	
	/**
	 * Sets the trace to forward all traces to.
	 *
	 * @param __kt The kernel trace to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	final void __setTrace(KernelTrace __kt)
		throws NullPointerException
	{
		// Check
		if (__kt == null)
			throw new NullPointerException("NARG");
		
		// Set
		_trace = __kt;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public void createdProcess(KernelProcess __kp)
	{
		KernelTrace trace = this._trace;
		if (trace != null)
			trace.createdProcess(__kp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public void noMoreProcesses()
	{
		KernelTrace trace = this._trace;
		if (trace != null)
			trace.noMoreProcesses();
	}
}

