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

/**
 * This represents a stack frame which is used to indicate the current method
 * and the position within the method that is currently being executed.
 *
 * @since 2016/04/09
 */
public class JVMStackFrame
{
	/** The thread this frame exists in. */
	protected final JVMThread thread;
	
	/** The method the frame is executing. */
	protected final JVMMethod method;
	
	/** The current PC address. */
	private volatile int _pcaddr;
	
	/**
	 * Initializes the stack frame.
	 *
	 * @param __thr The current thread of execution.
	 * @param __in The method currently being executed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	public JVMStackFrame(JVMThread __thr, JVMMethod __in)
		throws NullPointerException
	{
		// Check
		if (__thr == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Set
		thread = __thr;
		method = __in;
	}
}

