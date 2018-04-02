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
import cc.squirreljme.runtime.cldc.system.api.ClassEnumConstants;
import cc.squirreljme.runtime.cldc.system.api.ClassEnumCount;
import cc.squirreljme.runtime.cldc.system.api.ClassEnumIndexOf;
import cc.squirreljme.runtime.cldc.system.api.ClassEnumValueOf;
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
	implements ClassEnumConstants,
		ClassEnumCount,
		ClassEnumIndexOf,
		ClassEnumValueOf,
		SetDaemonThreadCall,
		ThrowableGetStackCall,
		ThrowableSetStackCall
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	public final <E extends Enum<E>> E[] classEnumConstants(Class<E> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.getEnumConstants();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final int classEnumCount(Class<? extends Enum> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.getEnumConstants().length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final <E extends Enum<E>> E classEnumIndexOf(Class<E> __cl, int __i)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AF07 The enumeration value index is out of
		// bounds. (The enumeration; The index)}
		E[] values = this.<E>classEnumConstants(__cl);
		if (__i < 0 || __i >= values.length)
			throw new IllegalArgumentException(
				String.format("AF07 %s %i", __cl, __i));
		return values[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	public final <E extends Enum<E>> E classEnumValueOf(Class<E> __cl,
		String __n)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null || __n == null)
			throw new NullPointerException("NARG");
		
		for (E e : this.<E>classEnumConstants(__cl))
			if (__n.equals(((Enum<?>)e).name()))
				return e;
		
		// {@squirreljme.error AF01 Could not find the enumeration constant
		// by the specified name in the given class. (The class name; The
		// enum type)}
		throw new IllegalArgumentException(
			String.format("AF01 %s %s", __cl, __n));
	}
	
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
			__e.file(), __e.line());
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
			null, -1, __e.getFileName(), __e.getLineNumber());
	}
}

