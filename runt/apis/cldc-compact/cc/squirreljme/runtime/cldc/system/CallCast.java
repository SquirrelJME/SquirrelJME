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
 * This is given a bunch of input objects and is used cast input objects to
 * the appropriate types.
 *
 * @since 2018/03/03
 */
public final class CallCast
{
	/**
	 * Not used.
	 *
	 * @since 2018/03/03
	 */
	private CallCast()
	{
	}
	
	/**
	 * Casts to the given object type.
	 *
	 * @param <C> The class to cast to.
	 * @param __cl The class to cast to.
	 * @param __v The input object.
	 * @return The casted object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	public static final <C> C as(Class<C> __cl, Object __v)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(__v);
	}
	
	/**
	 * Casts to boolean.
	 *
	 * @param __v The input value.
	 * @return The boolean value.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/01
	 */
	public static final boolean asBoolean(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return (Boolean)__v;
	}
	
	/**
	 * Casts to byte array.
	 *
	 * @param __v The input value.
	 * @return The byte array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/01
	 */
	public static final ByteArray asByteArray(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		if (__v instanceof byte[])
			return new LocalByteArray((byte[])__v);
		return (ByteArray)__v;
	}
	
	/**
	 * Casts to the given class type.
	 *
	 * @param <T> The class to cast to.
	 * @param __cl The class to cast to.
	 * @param __v The input object.
	 * @return The casted object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	@SuppressWarnings({"unchecked"})
	public static final <T> Class<T> asClass(Class<T> __cl, Object __v)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return (Class<T>)((Object)Class.class.cast(__v));
	}
	
	/**
	 * Casts to the given enumeration.
	 *
	 * @param <E> The enum to cast to.
	 * @param __cl The class to cast to.
	 * @param __v The input object.
	 * @return The casted object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	public static final <E extends Enum<E>> E asEnum(Class<E> __cl, Object __v)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(__v);
	}
	
	/**
	 * Casts to integer.
	 *
	 * @param __v The input value.
	 * @return The integer value.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/01
	 */
	public static final int asInteger(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return (Integer)__v;
	}
}

