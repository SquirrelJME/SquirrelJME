// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

import cc.squirreljme.runtime.cldc.service.ServiceClientProvider;

/**
 * This is the mnemonic call which just uses the system's actual system call
 * interface rather than implementing it.
 *
 * @since 2018/03/01
 */
public class __SystemMnemonicCall__
	extends MnemonicCall
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final long currentTimeMillis()
	{
		return SystemCall.longCall(
			SystemFunction.CURRENT_TIME_MILLIS);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final void exit(int __e)
		throws SecurityException
	{
		SystemCall.voidCall(
			SystemFunction.EXIT, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final void garbageCollectionHint()
	{
		SystemCall.voidCall(
			SystemFunction.GARBAGE_COLLECTION_HINT);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final void initialized()
	{
		SystemCall.voidCall(
			SystemFunction.INITIALIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final long nanoTime()
	{
		return SystemCall.longCall(
			SystemFunction.NANOTIME);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final void pipeOutput(boolean __err, int __b)
	{
		SystemCall.voidCall(
			SystemFunction.PIPE_OUTPUT_ZI, __err, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final void pipeOutput(boolean __err, ByteArray __b, int __o,
		int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		SystemCall.voidCall(
			SystemFunction.PIPE_OUTPUT_ZABII, __err, __b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final Object serviceCall(int __dx, Enum<?> __func,
		Object... __args)
		throws NullPointerException
	{
		// Force to exist
		if (__args == null)
			__args = new Object[0];
		
		// Expand to full array
		int nargs = __args.length;
		Object[] xargs = new Object[nargs + 2];
		xargs[0] = __dx;
		xargs[1] = __func;
		for (int i = 0, o = 2; i < nargs; i++, o++)
			xargs[o] = __args[i];
		
		// Forward call
		return SystemCall.<Object>systemCall(Object.class,
			SystemFunction.SERVICE_CALL, xargs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/02
	 */
	@Override
	public final int serviceCount()
	{
		return SystemCall.integerCall(
			SystemFunction.SERVICE_COUNT);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public final Class<? extends ServiceClientProvider> serviceQueryClass(
		int __dx)
	{
		return (Class<? extends ServiceClientProvider>)SystemCall.<Class>
			systemCall(Class.class, SystemFunction.SERVICE_QUERY_CLASS, __dx);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final int serviceQueryIndex(Class<?> __cl)
	{
		return SystemCall.integerCall(
			SystemFunction.SERVICE_QUERY_INDEX, __cl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		SystemCall.voidCall(
			SystemFunction.SET_DAEMON_THREAD, __t);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/02
	 */
	@Override
	public final IntegerArray taskList(boolean __incsys)
	{
		return SystemCall.integerArrayCall(
			SystemFunction.TASK_LIST, __incsys);
	}
}

