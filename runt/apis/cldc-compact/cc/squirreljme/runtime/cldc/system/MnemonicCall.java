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

