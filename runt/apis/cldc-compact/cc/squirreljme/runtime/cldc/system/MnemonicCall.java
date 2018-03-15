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
 * This is a system call handler which handles any incoming system calls and
 * wraps them accordingly to safe methods for implementation.
 *
 * @since 2018/02/27
 */
public abstract class MnemonicCall
	implements SystemCallImplementation
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final Object systemCall(SystemFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		// Instance for the void call
		VoidType voidtype = VoidType.INSTANCE;
		
		// Forward calls
		switch (__func)
		{
			case CURRENT_TIME_MILLIS:
				throw new todo.TODO();
				
			case EXIT:
				this.exit(
					CallCast.asInteger(__args[0]));
				return voidtype;
			
			case GARBAGE_COLLECTION_HINT:
				this.garbageCollectionHint();
				return voidtype;
			
			case INITIALIZED:
				this.clientInitializationComplete();
				return voidtype;
			
			case NANOTIME:
				throw new todo.TODO();
				
			case PIPE_OUTPUT_ZI:
				this.pipeOutput(
					CallCast.asBoolean(__args[0]),
					CallCast.asInteger(__args[1]));
				return voidtype;

			case PIPE_OUTPUT_ZABII:
				this.pipeOutput(
					CallCast.asBoolean(__args[0]),
					CallCast.asByteArray(__args[1]),
					CallCast.asInteger(__args[2]),
					CallCast.asInteger(__args[3]));
				return voidtype;
			
			case SERVICE_CALL:
				return this.__serviceCall(__args);
			
			case SERVICE_COUNT:
				return this.serviceCount();
			
			case SERVICE_QUERY_CLASS:
				return this.serviceQueryClass(
					CallCast.asInteger(__args[0]));
			
			case SERVICE_QUERY_INDEX:
				return this.serviceQueryIndex(
					CallCast.<Object>asClass(Object.class, __args[0]));
			
			case SET_DAEMON_THREAD:
				this.setDaemonThread(
					CallCast.<Thread>as(Thread.class, __args[0]));
				return voidtype;
			
			case TASK_LIST:
				return this.taskList(
					CallCast.asBoolean(__args[0]));
			
				// {@squirreljme.error ZZ0f Unimplemented system call.
				// (The function being called)}
			default:
				throw new InvalidSystemCallException(
					String.format("ZZ0f %s", __func));
		}
	}
	
	/**
	 * Specifies that the client was successfully initialized.
	 *
	 * @since 2018/03/01
	 */
	public abstract void clientInitializationComplete();
	
	/**
	 * Returns the current time in milliseconds UTC since the epoch.
	 *
	 * @return The since since the epoch in UTC milliseconds.
	 * @since 2018/03/01
	 */
	public abstract long currentTimeMillis();
	
	/**
	 * Exit the virtual machine with the given exit code.
	 *
	 * This function does not return unless an exception is thrown.
	 *
	 * @param __e The exit code to use.
	 * @throws SecurityException If exit is not permitted.
	 * @since 2018/03/01
	 */
	public abstract void exit(int __e)
		throws SecurityException;
	
	/**
	 * Hint that garbage collection should be performed.
	 *
	 * @since 2018/03/01
	 */
	public abstract void garbageCollectionHint();
	
	/**
	 * Returns the current monotonic clock time.
	 *
	 * @return The current monotonic clock time.
	 * @since 2018/03/01
	 */
	public abstract long nanoTime();
	
	/**
	 * Pipe a single byte to standard output or standard error.
	 *
	 * @param __err To standard error?
	 * @param __b The value to pipe.
	 * @since 2018/03/01
	 */
	public abstract void pipeOutput(boolean __err, int __b);
	
	/**
	 * Pipes multiple bytes to standard output or standard error.
	 *
	 * @param __err To standard error?
	 * @param __b The values to pipe.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException
	 * @since 2018/03/01
	 */
	public abstract void pipeOutput(boolean __err, ByteArray __b, int __o,
		int __l)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Performs a call into a service.
	 *
	 * @param __dx The service index.
	 * @param __func The function in the service.
	 * @param __args The function arguments.
	 * @return The return value of the call.
	 * @since 2018/03/02
	 */
	public abstract Object serviceCall(int __dx, Enum<?> __func,
		Object... __args);
	
	/**
	 * Returns the number of available services.
	 *
	 * @return The number of available services.
	 * @since 2018/03/02
	 */
	public abstract int serviceCount();
	
	/**
	 * Queries which class the client should use for the given service index.
	 *
	 * @param __dx The index to get the client class for.
	 * @return The client class for the given index.
	 * @since 2018/03/02
	 */
	public abstract Class<? extends ServiceClientProvider> serviceQueryClass(
		int __dx);
	
	/**
	 * Queries the index of the service which implements the given class.
	 *
	 * @param __cl The class type to check the local service for.
	 * @return The index of the service which implements the given class.
	 * @since 2018/03/02
	 */
	public abstract int serviceQueryIndex(Class<?> __cl);
	
	/**
	 * Set thread as daemon thread.
	 *
	 * @param __t The thread to daemonize.
	 * @throws IllegalThreadStateException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/01
	 */
	public abstract void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException;
	
	/**
	 * Returns the list of tasks which are available on the system.
	 *
	 * @param __incsys Include system tasks?
	 * @return The array of tasks available to the system.
	 * @since 2018/03/02
	 */
	public abstract IntegerArray taskList(boolean __incsys);
	
	/**
	 * Pipes multiple bytes to standard output or standard error.
	 *
	 * @param __err To standard error?
	 * @param __b The values to pipe.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException
	 * @since 2018/03/01
	 */
	public final void pipeOutput(boolean __err, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		this.pipeOutput(__err, new LocalByteArray(__b), __o, __l);
	}
	
	/**
	 * Extracts and forwards calls made to services.
	 *
	 * @param __args Input arguments.
	 * @return The service return value.
	 * @sicne 2018/03/02
	 */
	private final Object __serviceCall(Object... __args)
	{
		// Extract arguments
		int nargs = __args.length,
			alen = nargs - 2;
		Object[] fargs = new Object[alen];
		System.arraycopy(__args, 2, fargs, 0, alen);
		
		// Perform the call
		return this.serviceCall(
			CallCast.asInteger(__args[0]),
			(Enum<?>)__args[1],
			fargs);
	}
}

