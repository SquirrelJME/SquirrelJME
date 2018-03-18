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

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.system.api.Call;
import cc.squirreljme.runtime.cldc.system.api.ClassEnumConstants;
import cc.squirreljme.runtime.cldc.system.api.ClassEnumValueOf;
import cc.squirreljme.runtime.cldc.system.api.SetDaemonThreadCall;
import cc.squirreljme.runtime.cldc.system.api.ThrowableGetStackCall;
import cc.squirreljme.runtime.cldc.system.api.ThrowableSetStackCall;
import cc.squirreljme.runtime.cldc.system.type.BooleanArray;
import cc.squirreljme.runtime.cldc.system.type.ByteArray;
import cc.squirreljme.runtime.cldc.system.type.CharacterArray;
import cc.squirreljme.runtime.cldc.system.type.ClassType;
import cc.squirreljme.runtime.cldc.system.type.DoubleArray;
import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.FloatArray;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalBooleanArray;
import cc.squirreljme.runtime.cldc.system.type.LocalByteArray;
import cc.squirreljme.runtime.cldc.system.type.LocalCharacterArray;
import cc.squirreljme.runtime.cldc.system.type.LocalDoubleArray;
import cc.squirreljme.runtime.cldc.system.type.LocalFloatArray;
import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalLongArray;
import cc.squirreljme.runtime.cldc.system.type.LocalShortArray;
import cc.squirreljme.runtime.cldc.system.type.LocalStringArray;
import cc.squirreljme.runtime.cldc.system.type.LongArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.ShortArray;
import cc.squirreljme.runtime.cldc.system.type.StringArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;

/**
 * This contains the static access to the system call interface used by
 * clients to interact with the kernel.
 *
 * @since 2018/02/21
 */
public final class SystemCall
{
	/** The easy call interface. */
	public static final EasyCall EASY =
		new EasyCall();	
	
	/** The implementation of the system call, this is specially set. */
	private static final Call[] _CALLS =
		SystemCall.__calls();
	
	/**
	 * Performs the given system call.
	 *
	 * @param <R> The return type of the system call.
	 * @param __func The function to call.
	 * @param __args The arguments to the system call.
	 * @return The result from the system call.
	 * @throws ClassCastException If the return type is of the wrong class.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/21
	 */
	public static <R> R systemCall(Class<R> __cl, SystemFunction __func,
		Object... __args)
		throws ClassCastException, InvalidSystemCallException,
			NullPointerException
	{
		if (__cl == null || __func == null)
			throw new NullPointerException("NARG");
		
		// Force arguments to always be valid, but null is an empty array
		if (__args == null)
			__args = new Object[0];
		
		// If this is intended to be a local system call then the arguments do
		// not need to be checked or wrapped for validity as long as they are
		// the write input and output types
		if (__func.isLocal())
			return __cl.cast(SystemCall.__callLocal(__func, __args));
		
		// Perform remote call where everything is checked and such
		return __cl.cast(SystemCall.__callRemote(__func, __args));
	}
	
	/**
	 * Validates a single argument for a system call.
	 *
	 * @param __arg The input argument.
	 * @return The resulting argument.
	 * @throws InvalidSystemCallException If the argument is not valid.
	 * @since 2018/03/18
	 */
	public static Object validateArgument(Object __arg)
	{
		// Nulls are always valid
		if (__arg == null)
			return null;
		
		// Primitive array types needs to be translated
		if (__arg.getClass().isArray())
		{
			boolean isarray = true;
			if (__arg instanceof boolean[])
				__arg = new LocalBooleanArray((boolean[])__arg);
			else if (__arg instanceof byte[])
				__arg = new LocalByteArray((byte[])__arg);
			else if (__arg instanceof short[])
				__arg = new LocalShortArray((short[])__arg);
			else if (__arg instanceof char[])
				__arg = new LocalCharacterArray((char[])__arg);
			else if (__arg instanceof int[])
				__arg = new LocalIntegerArray((int[])__arg);
			else if (__arg instanceof long[])
				__arg = new LocalLongArray((long[])__arg);
			else if (__arg instanceof float[])
				__arg = new LocalFloatArray((float[])__arg);
			else if (__arg instanceof double[])
				__arg = new LocalDoubleArray((double[])__arg);
			else if (__arg instanceof String[])
				__arg = new LocalStringArray((String[])__arg);
			
			// {@squirreljme.error ZZ0k Cannot pass the specified array
			// type as a system call. (The class type)}
			else
				throw new InvalidSystemCallException(
					String.format("ZZ0k %s", __arg.getClass()));
			
			// Reset
			return __arg;
		}
		
		// Wrap enumerated values
		else if (__arg instanceof Enum)
		{
			Enum e = (Enum)__arg;
			return (__arg = new EnumType(e.getClass().getName(),
				e.ordinal(), e.name()));
		}
		
		// Wrap class types
		else if (__arg instanceof Class)
			return (__arg = new ClassType(((Class)__arg).getName()));
		
		// {@squirreljme.error ZZ0j Cannot utilize the given class as
		// an argument to a system call. (The class type)}
		else if (!(__arg instanceof Boolean ||
			__arg instanceof Byte ||
			__arg instanceof Short ||
			__arg instanceof Character ||
			__arg instanceof Integer ||
			__arg instanceof Long ||
			__arg instanceof Float ||
			__arg instanceof Double ||
			__arg instanceof String ||
			__arg instanceof EnumType ||
			__arg instanceof ClassType ||
			__arg instanceof BooleanArray ||
			__arg instanceof ByteArray ||
			__arg instanceof ShortArray ||
			__arg instanceof CharacterArray ||
			__arg instanceof IntegerArray ||
			__arg instanceof LongArray ||
			__arg instanceof FloatArray ||
			__arg instanceof DoubleArray ||
			__arg instanceof StringArray ||
			__arg instanceof RemoteMethod ||
			__arg instanceof VoidType))
			throw new InvalidSystemCallException(String.format("ZZ0j %s",
				__arg.getClass()));
		
		// Is okay!
		return __arg;
	}
	
	/**
	 * Validates the system call arguments to ensure they are valid.
	 *
	 * @param __args The arguments to call.
	 * @return The input arguments.
	 * @throws InvalidSystemCallException If the arguments are not valid.
	 * @since 2018/03/18
	 */
	public static Object[] validateArguments(Object... __args)
		throws InvalidSystemCallException
	{
		// Defensive copy always
		__args = (__args == null ? new Object[0] : __args.clone());
		
		// Check argument inputs
		for (int i = 0, n = __args.length; i < n; i++)
		{
			Object v = __args[i],
				w = SystemCall.validateArgument(v);
			
			// Set if it has changed
			if (v != w)
				__args[i] = w;
		}
		
		// Return the copy
		return __args;
	}
	
	/**
	 * Recursively wraps the remote exception so that all call traces are of
	 * the remote side.
	 *
	 * @param <T> The type of exception to return.
	 * @param __t The throwable to wrap.
	 * @return The wrapped exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	@SuppressWarnings({"unchecked"})
	public static final <T extends Throwable> T wrapException(
		Throwable __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Does not need wrapping
		if (__t instanceof SystemCallException ||
			__t instanceof SystemCallError)
			return (T)__t;
		
		// Is there a cause which needs to be wrapped?
		Throwable cause = __t.getCause();
		if (cause != null)
			cause = SystemCall.<Throwable>wrapException(cause);
		
		// Set base exception for returning
		Class<?> cl = __t.getClass();
		ClassType ct = new ClassType(cl.getName());
		String m = ct.name() + ": " + __t.getMessage();
		Throwable rv = (__t instanceof Error ?
			new SystemCallError(ct, m, cause) :
			new SystemCallException(ct, m, cause));
		
		// Wrap any exceptions which have been suppressed
		Throwable[] sups = __t.getSuppressed();
		for (int i = 0, n = sups.length; i < n; i++)
		{
			// This should not happen, but it might
			Throwable sup = sups[i];
			if (sup == null)
				continue;
			
			// Record it
			rv.addSuppressed(SystemCall.<Throwable>wrapException(sup));
		}
		
		// Before returning make sure the wrapped exception share the same
		// stack trace as the remote one
		SystemCall.<VoidType>systemCall(VoidType.class,
			SystemFunction.THROWABLE_SET_STACK, rv,
			SystemCall.<CallTraceElement[]>systemCall(CallTraceElement[].class,
				SystemFunction.THROWABLE_GET_STACK, __t));
		
		// Unsafe cast for returning
		return (T)((Object)rv);
	}
	
	/**
	 * Performs a local system call which does not go to the kernel.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The result of the call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	@SuppressWarnings({"unchecked"})
	private static final Object __callLocal(SystemFunction __func,
		Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		// Force to be valid
		if (__args == null)
			__args = new Object[0];
		
		// {@squirreljme.error ZZ0m Unimplemented local call. (The function)}
		Call[] calls = SystemCall._CALLS;
		Call call = calls[__func.ordinal()];
		if (call == null)
			throw new InvalidSystemCallException(String.format("ZZ0n %s",
				__func));
		
		// Depends on the function
		switch (__func)
		{
			case CLASS_ENUM_CONSTANTS:
				return ((ClassEnumConstants)call).<Object>classEnumConstants(
					(Class<Object>)__args[0]);
			
			case CLASS_ENUM_VALUEOF:
				return ((ClassEnumValueOf)call).<Object>classEnumValueOf(
					(Class<Object>)__args[0], (String)__args[1]);
			
			case SET_DAEMON_THREAD:
				((SetDaemonThreadCall)call).setDaemonThread(
					(Thread)__args[0]);
				return VoidType.INSTANCE;
				
			case THROWABLE_GET_STACK:
				return ((ThrowableGetStackCall)call).throwableGetStack(
					(Throwable)__args[0]);
				
			case THROWABLE_SET_STACK:
				((ThrowableSetStackCall)call).throwableSetStack(
					(Throwable)__args[0], (CallTraceElement[])__args[1]);
				return VoidType.INSTANCE;
			
			// {@squirreljme.error ZZ0m Unknown local call. (The function)}
			default:
				throw new InvalidSystemCallException(String.format("ZZ0m %s",
					__func));
		}
	}
	
	/**
	 * Performs a remote system call which is dispatched to a handler for
	 * remote execution.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The result of the call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	private static final Object __callRemote(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		// Force to be valid
		if (__args == null)
			__args = new Object[0];
		
		// {@squirreljme.error ZZ0o Unimplemented remote call. (The function)}
		Call[] calls = SystemCall._CALLS;
		Call call = calls[__func.ordinal()];
		if (!(call instanceof SystemCallDispatch))
			throw new InvalidSystemCallException(String.format("ZZ0o %s",
				__func));
		
		// Validate all the arguments
		__args = SystemCall.validateArguments(__args);
		
		// Perform the call but wrap any exceptions that may have been
		// thrown by the remote end
		try
		{
			return SystemCall.validateArgument(
				((SystemCallDispatch)call).dispatch(__func, __args));
		}
		
		// Wrap exceptions so that local interfaces are consistent
		catch (RuntimeException|Error t)
		{
			// Already excpetions of the desired type
			if (t instanceof SystemCallException ||
				t instanceof SystemCallError)
				throw t;
			
			// Recursively initialize new exceptions accordingly
			if (t instanceof Error)
				throw SystemCall.<Error>wrapException(t);
			throw SystemCall.<RuntimeException>wrapException(t);
		}
	}
	
	/**
	 * This may potentially be intercepted to initialize the system call
	 * implementation class.
	 *
	 * @return The system call implementation class.
	 * @since 2018/02/21
	 */
	private static final Call[] __calls()
	{
		return SystemCall._CALLS;
	}
}

