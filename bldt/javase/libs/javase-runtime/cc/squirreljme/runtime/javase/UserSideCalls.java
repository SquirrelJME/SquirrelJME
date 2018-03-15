// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase;

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.system.api.SetDaemonThreadCall;
import cc.squirreljme.runtime.cldc.system.api.ThrowableGetStackCall;
import cc.squirreljme.runtime.cldc.system.api.ThrowableSetStackCall;
import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Simple user side calls for Java SE systems.
 *
 * @since 2018/03/15
 */
public final class UserSideCalls
	implements SetDaemonThreadCall,
		ThrowableGetStackCall,
		ThrowableSetStackCall
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		__t.setDaemon(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final CallTraceElement[] throwableGetStack(Throwable __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
			
		StackTraceElement[] from = __t.getStackTrace();
		int n = from.length;
		
		// Convert
		CallTraceElement[] rv = new CallTraceElement[n];
		for (int i = 0; i < n; i++)
			rv[i] = UserSideCalls.stackJavaToDebug(from[i]);
		
		return rv;
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final void throwableSetStack(Throwable __t,
		CallTraceElement[] __e)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		if (__e == null)
			__e = new CallTraceElement[0];
		
		int n = __e.length;
		StackTraceElement[] to = new StackTraceElement[n];
		
		// Convert
		for (int i = 0; i < n; i++)
			to[i] = UserSideCalls.stackDebugToJava(__e[i]);
		
		__t.setStackTrace(to);
	}
	
	/**
	 * Converts a debug call trace element to a stack trace element.
	 *
	 * @param __e The input element.
	 * @return The converted element.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public static StackTraceElement stackDebugToJava(CallTraceElement __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		return new StackTraceElement(__e.className(), __e.methodName(),
			__e.methodDescriptor(), (int)__e.address());
	}
	
	/**
	 * Converts a stack trace element to a debug call trace element.
	 *
	 * @param __e The input element.
	 * @return The converted element.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public static CallTraceElement stackJavaToDebug(StackTraceElement __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		return new CallTraceElement(__e.getClassName(), __e.getMethodName(),
			__e.getFileName(), __e.getLineNumber());
	}
}

