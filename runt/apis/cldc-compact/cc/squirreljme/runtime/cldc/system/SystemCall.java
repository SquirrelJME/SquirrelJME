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
 * This contains the static access to the system call interface used by
 * clients to interact with the kernel.
 *
 * @since 2018/02/21
 */
public final class SystemCall
{
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static boolean booleanCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Boolean>call(Boolean.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static BooleanArray booleanArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<BooleanArray>call(BooleanArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the given system call.
	 *
	 * @param <R> The return type of the system call.
	 * @param __func The function to call.
	 * @param __args The arguments to the system call.
	 * @return The result from the system call.
	 * @throws ClassCastException If the return type is of the wrong class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/21
	 */
	public static <R> R call(Class<R> __cl, SystemFunction __func,
		Object... __args)
		throws ClassCastException, InvalidSystemCallException,
			NullPointerException
	{
		if (__cl == null || __func == null)
			throw new NullPointerException("NARG");
		
		// Force arguments to always be valid, but null is an empty array
		if (__args == null)
			__args = new Object[0];
		
		throw new todo.TODO();
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static char charCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Character>call(Character.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static CharacterArray charArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<CharacterArray>call(CharacterArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static double doubleCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Double>call(Double.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static DoubleArray doubleArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<DoubleArray>call(DoubleArray.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static float floatCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Float>call(Float.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static FloatArray floatArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<FloatArray>call(FloatArray.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static int integerCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Integer>call(Integer.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static IntegerArray integerArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<IntegerArray>call(IntegerArray.class, __func,
			__args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static long longCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Long>call(Long.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static LongArray longArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<LongArray>call(LongArray.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static short shortCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Short>call(Short.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @return The return value of the system call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static ShortArray shortArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<ShortArray>call(ShortArray.class, __func, __args);
	}
	
	/**
	 * Performs the specified system call.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the call.
	 * @throws InvalidSystemCallException If the system call is not valid.
	 * @throws NullPointerException If no function was specified.
	 * @since 2018/02/21
	 */
	public static void voidCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		SystemCall.<VoidType>call(VoidType.class, __func, __args);
	}
}

