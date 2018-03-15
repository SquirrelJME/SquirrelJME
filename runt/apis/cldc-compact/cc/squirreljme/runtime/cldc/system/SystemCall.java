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
	/** System call mnemonics. */
	public static final MnemonicCall MNEMONIC =
		new __SystemMnemonicCall__();
	
	/** The implementation of the system call, this is specially set. */
	private static final SystemCallImplementation[] _IMPLEMENTATIONS =
		SystemCall.__implementations();
	
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
		return SystemCall.<Boolean>systemCall(Boolean.class, __func, __args);
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
		return SystemCall.<BooleanArray>systemCall(BooleanArray.class, __func,
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
	public static char charCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Character>systemCall(Character.class, __func,
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
	public static CharacterArray charArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<CharacterArray>systemCall(CharacterArray.class,
			__func, __args);
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
		return SystemCall.<Double>systemCall(Double.class, __func, __args);
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
		return SystemCall.<DoubleArray>systemCall(DoubleArray.class, __func,
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
	public static float floatCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Float>systemCall(Float.class, __func, __args);
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
		return SystemCall.<FloatArray>systemCall(FloatArray.class, __func,
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
	public static int integerCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Integer>systemCall(Integer.class, __func,
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
	public static IntegerArray integerArrayCall(SystemFunction __func,
		Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<IntegerArray>systemCall(IntegerArray.class, __func,
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
		return SystemCall.<Long>systemCall(Long.class, __func, __args);
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
		return SystemCall.<LongArray>systemCall(LongArray.class, __func,
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
	public static short shortCall(SystemFunction __func, Object... __args)
		throws InvalidSystemCallException, NullPointerException
	{
		return SystemCall.<Short>systemCall(Short.class, __func, __args);
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
		return SystemCall.<ShortArray>systemCall(ShortArray.class, __func,
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
		
		// {@squirreljme.error ZZ0d Unimplemented system call. (The system
		// call)}.
		SystemCallImplementation impl = SystemCall._IMPLEMENTATIONS[
			__func.ordinal()];
		if (impl == null)
			throw new InvalidSystemCallException(String.format("ZZ0d %s",
				__func));
		
		// If this is intended to be a local system call then the arguments do
		// not need to be checked or wrapped for validity as long as they are
		// the write input and output types
		if (__func.isLocal())
			return __cl.cast(impl.systemCall(__func, __args));
		
		// Check argument inputs
		for (int i = 0, n = __args.length; i < n; i++)
		{
			Object v = __args[i];
			
			// Nulls are always valid
			if (v == null)
				continue;
			
			// Primitive array types needs to be translated
			if (v.getClass().isArray())
			{
				boolean isarray = true;
				if (v instanceof boolean[])
					v = new LocalBooleanArray((boolean[])v);
				else if (v instanceof byte[])
					v = new LocalByteArray((byte[])v);
				else if (v instanceof short[])
					v = new LocalShortArray((short[])v);
				else if (v instanceof char[])
					v = new LocalCharacterArray((char[])v);
				else if (v instanceof int[])
					v = new LocalIntegerArray((int[])v);
				else if (v instanceof long[])
					v = new LocalLongArray((long[])v);
				else if (v instanceof float[])
					v = new LocalFloatArray((float[])v);
				else if (v instanceof double[])
					v = new LocalDoubleArray((double[])v);
				else if (v instanceof String[])
					v = new LocalStringArray((String[])v);
				
				// {@squirreljme.error ZZ0k Cannot pass the specified array
				// type as a system call. (The class type)}
				else
					throw new InvalidSystemCallException(
						String.format("ZZ0k %s", v.getClass()));
				
				// Reset
				__args[i] = v;
			}
			
			// Wrap enumerated values
			else if (v instanceof Enum)
			{
				Enum e = (Enum)v;
				__args[i] = (v = new EnumType(e.getClass().getName(),
					e.ordinal(), e.name()));
			}
			
			// Wrap class types
			else if (v instanceof Class)
				__args[i] = (v = new ClassType(((Class)v).getName()));
			
			// {@squirreljme.error ZZ0j Cannot utilize the given class as
			// an argument to a system call. (The class type)}
			else if (!(v instanceof Boolean ||
				v instanceof Byte ||
				v instanceof Short ||
				v instanceof Character ||
				v instanceof Integer ||
				v instanceof Long ||
				v instanceof Float ||
				v instanceof Double ||
				v instanceof String ||
				v instanceof EnumType ||
				v instanceof ClassType ||
				v instanceof LocalBooleanArray ||
				v instanceof LocalByteArray ||
				v instanceof LocalShortArray ||
				v instanceof LocalCharacterArray ||
				v instanceof LocalIntegerArray ||
				v instanceof LocalLongArray ||
				v instanceof LocalFloatArray ||
				v instanceof LocalDoubleArray ||
				v instanceof LocalStringArray))
				throw new InvalidSystemCallException(String.format("ZZ0j %s",
					v.getClass()));
		}
		
		// Perform the call but wrap any exceptions that may have been
		// thrown by the remote end
		Object rv;
		try
		{
			rv = impl.systemCall(__func, __args);
		}
		
		// Wrap exceptions so that local interfaces are consistent
		catch (RuntimeException|Error t)
		{
			// Already excpetions of the desired type
			if (t instanceof SystemCallException ||
				t instanceof SystemCallError)
				throw t;
			
			throw new todo.TODO();
		}
		
		// Make sure the return value is correct
		return __cl.cast(rv);
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
		SystemCall.<VoidType>systemCall(VoidType.class, __func, __args);
	}
	
	/**
	 * This may potentially be intercepted to initialize the system call
	 * implementation class.
	 *
	 * @return The system call implementation class.
	 * @since 2018/02/21
	 */
	private static final SystemCallImplementation[] __implementations()
	{
		return SystemCall._IMPLEMENTATIONS;
	}
}

