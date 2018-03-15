// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.service;

import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.system.type.BooleanArray;
import cc.squirreljme.runtime.cldc.system.type.CharacterArray;
import cc.squirreljme.runtime.cldc.system.type.DoubleArray;
import cc.squirreljme.runtime.cldc.system.type.FloatArray;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LongArray;
import cc.squirreljme.runtime.cldc.system.type.ShortArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;

/**
 * This class allows simpler access to performing system calls into services
 * by using the standard system call interface.
 *
 * @since 2018/03/02
 */
public final class ServiceCaller
{
	/** The service index. */
	protected final Integer index;
	
	/**
	 * Initializes the service caller.
	 *
	 * @param __dx The system call index.
	 * @since 2018/03/02
	 */
	public ServiceCaller(int __dx)
		throws NullPointerException
	{
		this.index = __dx;
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final boolean booleanCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Boolean>serviceCall(Boolean.class, __func, __args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final BooleanArray booleanArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<BooleanArray>serviceCall(BooleanArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final char charCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Character>serviceCall(Character.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final CharacterArray charArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<CharacterArray>serviceCall(CharacterArray.class,
			__func, __args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final double doubleCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Double>serviceCall(Double.class, __func, __args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final DoubleArray doubleArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<DoubleArray>serviceCall(DoubleArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final float floatCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Float>serviceCall(Float.class, __func, __args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final FloatArray floatArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<FloatArray>serviceCall(FloatArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final int integerCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Integer>serviceCall(Integer.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final IntegerArray integerArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<IntegerArray>serviceCall(IntegerArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final long longCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Long>serviceCall(Long.class, __func, __args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final LongArray longArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<LongArray>serviceCall(LongArray.class, __func,
			__args);
	}
	
	/**
	 * Calls the service with the given arguments.
	 *
	 * @param <R> The return type of the call.
	 * @param __rv The class for the return type.
	 * @param
	 * @throws ClassCastException If the return type is not valid.
	 * @throws InvalidServiceCallException If the service call was not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/02
	 */
	public final <R> R serviceCall(Class<R> __rv, Enum<?> __func,
		Object... __args)
		throws ClassCastException, InvalidServiceCallException,
			NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		// Do not treat this as fatal
		if (__args == null)
			__args = new Object[0];
		
		// Build arguments to pass to the service
		int nargs = __args.length;
		Object[] xargs = new Object[nargs + 2];
		xargs[0] = this.index;
		xargs[1] = __func;
		for (int i = 0, o = 2; i < nargs; i++, o++)
			xargs[o] = __args[i];
		
		// Perform the call
		return SystemCall.<R>systemCall(__rv, SystemFunction.SERVICE_CALL,
			xargs);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final short shortCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<Short>serviceCall(Short.class, __func, __args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final ShortArray shortArrayCall(Enum<?> __func,
		Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		return this.<ShortArray>serviceCall(ShortArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified service call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @throws InvalidServiceCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/03/02
	 */
	public final void voidCall(Enum<?> __func, Object... __args)
		throws InvalidServiceCallException, NullPointerException
	{
		this.<VoidType>serviceCall(VoidType.class, __func, __args);
	}
}

