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
	public final void clientInitializationComplete()
	{
		SystemCall.voidCall(
			SystemFunction.CLIENT_INITIALIZATION_COMPLETE);
	}
	
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
	public final void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		SystemCall.voidCall(
			SystemFunction.SET_DAEMON_THREAD, __t);
	}
}

