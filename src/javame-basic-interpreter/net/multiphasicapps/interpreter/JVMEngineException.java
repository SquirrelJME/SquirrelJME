// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.PrintStream;

/**
 * This is the base class for exceptions which are thrown by the interoreter to
 * indicate that it has an incorrect state or there is a problem with it.
 *
 * @since 2016/03/05
 */
public class JVMEngineException
	extends RuntimeException
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Inbternal VM stack trace. */
	private volatile JVMExceptionTrace[] _vmtrace;
	
	/**
	 * Initializes the exception with no message.
	 *
	 * @param __f The execution frame.
	 * @since 2016/03/05
	 */
	public JVMEngineException(JVMFrameable __f)
	{
		super();
		
		// Record the internal VM trace
		if (__f != null)
			setVMStackTrace(__f);
	}
	
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __f The execution frame.
	 * @param __msg The exception message.
	 * @since 2016/03/02
	 */
	public JVMEngineException(JVMFrameable __f, String __msg)
	{
		super(__msg);
		
		// Record the internal VM trace
		if (__f != null)
			setVMStackTrace(__f);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __f The execution frame.
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/03/02
	 */
	public JVMEngineException(JVMFrameable __f, String __msg, Throwable __c)
	{
		super(__msg, __c);
		
		// Record the internal VM trace
		if (__f != null)
			setVMStackTrace(__f);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __f The execution frame.
	 * @param __c The cause of the exception.
	 * @since 2016/03/15
	 */
	public JVMEngineException(JVMFrameable __f, Throwable __c)
	{
		super(__c);
		
		// Record the internal VM trace
		if (__f != null)
			setVMStackTrace(__f);
	}
	
	/**
	 * Prints the virtual machine trace as it appears to the internal virtual
	 * machine.
	 *
	 * @param __ps The target stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/15
	 */
	public final void printVMStackTrace(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Get the trace
			JVMExceptionTrace[] trace = _vmtrace;
			
			// Missing?
			if (trace == null)
			{
				// {@squirreljme.error IN0p No stack trace available.}
				__ps.printf("IN0p");
				return;
			}
			
			// Go through the traces
			int n = trace.length;
			for (int i = n - 1; i >= 0; i--)
				__ps.printf("#%3d: %s%n", i, trace[i]);
		}
	}
	
	/**
	 * Records the internal virtual machine stack trace.
	 *
	 * @param __f The frame to record.
	 * @throws NullPointerException On null arguments.
	 * @since 3016/04/16
	 */
	public final void setVMStackTrace(JVMFrameable __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// If already traced, ignore
			if (_vmtrace != null)
				return;
			
			// Trace otherwise
			_vmtrace = __f.thread().getExceptionTrace();
		}
	}
}

