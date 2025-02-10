// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

/**
 * This class contains methods for averting some safety checks done by the
 * compiler. It is better to use this than SuppressWarnings because this can
 * easily be found and only casts a single object at a time.
 *
 * @since 2014/09/05
 */
public final class Unchecked
{
	/**
	 * Not instantiated.
	 *
	 * @since 2014/09/05
	 */
	private Unchecked()
	{
	}
	
	/**
	 * Unchecked cast from any type to type T.
	 *
	 * @param <T> To type.
	 * @param __from The current value.
	 * @since 2014/09/05
	 */
	@SuppressWarnings({"unchecked"})
	public static final <T> T anyCast(Object __from)
	{
		return (T)__from;
	}
	
	/**
	 * Unchecked cast from type F to type T.
	 *
	 * @param <F> From type.
	 * @param <T> To type.
	 * @param __from The current value.
	 * @since 2014/09/05
	 */
	@SuppressWarnings({"unchecked"})
	public static final <F,T> T cast(F __from)
	{
		return (T)__from;
	}
	
	/**
	 * Casts to the generic form of the specified class.
	 *
	 * @param <T> Returning class type.
	 * @param __anon Normal class object to modify.
	 * @since 2014/09/19
	 */
	@SuppressWarnings({"unchecked"})
	public static final <T> Class<T> classOf(Class<?> __anon)
	{
		return (Class<T>)__anon;
	}

	/**
	 * Unchecked array cast from component type F to component type T.
	 *
	 * @param <F> From component type.
	 * @param <T> To component type.
	 * @param __from The current array.
	 * @since 2014/09/05
	 */
	@SuppressWarnings({"unchecked"})
	public static final <F,T> T[] arrayCast(F[] __from)
	{
		return (T[])__from;
	}
	
	/**
	 * Instance of check for generic types.
	 *
	 * @param __cl Class type.
	 * @param __o Object.
	 * @return {@code true} If __o is an instance of the class __cl.
	 * @since 2014/09/05
	 */
	public static final boolean iof(Class<?> __cl, Object __o)
	{
		return __cl.isInstance(__o);
	}
}

