// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is the base class for enum types.
 *
 * @param <E> The enum type.
 * @since 2018/09/24
 */
@Api
public abstract class Enum<E extends Enum<E>>
	implements Comparable<E>
{
	/** The name of the enum. */
	private final String _name;
	
	/** The ordinal of the enumeration. */
	private final int _ordinal;
	
	/**
	 * Initializes the enum properties.
	 *
	 * @param __s The enum name.
	 * @param __o The enum ordinal.
	 * @throws IllegalArgumentException If the ordinal is negative.
	 * @throws NullPointerException If no name was specified.
	 * @since 2018/09/24
	 */
	@Api
	protected Enum(String __s, int __o)
		throws IllegalArgumentException, NullPointerException
	{
		/* {@squirreljme.error ZZ10 Enum has no string.} */
		if (__s == null)
			throw new NullPointerException("ZZ10");
		
		/* {@squirreljme.error ZZ11 Enum has negative ordinal.} */
		if (__o < 0)
			throw new IllegalArgumentException("ZZ11");
		
		this._name = __s;
		this._ordinal = __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/24
	 */
	@Override
	protected final Object clone()
		throws CloneNotSupportedException
	{
		/* {@squirreljme.error ZZ12 Enums cannot be cloned.} */
		throw new CloneNotSupportedException("ZZ12");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/24
	 */
	@Override
	public final int compareTo(E __o)
		throws ClassCastException, NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ13 Cannot compare enums of a different
		type.} */
		if (this.getDeclaringClass() != __o.getDeclaringClass())
			throw new ClassCastException("ZZ13");
		
		// Just ordinal subtraction
		return this.ordinal() - __o.ordinal();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * Returns the class that declares this enum, this may be different from
	 * {@link Object#getClass()}.
	 *
	 * @return The declaring class of this enum.
	 * @since 2018/09/24
	 */
	@SuppressWarnings({"unchecked"})
	@Api
	public final Class<E> getDeclaringClass()
	{
		// Enums are either directly extending or extending a base class which
		// then extends this class, so we just need to look a few places up
		// the tree
		Class<?> me = this.getClass(),
			ext = me.getSuperclass();
		if (ext == Enum.class)
			return (Class<E>)((Object)me);
		return (Class<E>)((Object)ext);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/24
	 */
	@Override
	public final int hashCode()
	{
		return super.hashCode();
	}
	
	/**
	 * Returns the name of the constant.
	 *
	 * @return The constant name.
	 * @since 2018/09/24
	 */
	@Api
	public final String name()
	{
		return this._name;
	}
	
	/**
	 * Returns the ordinal of the constant.
	 *
	 * @return The ordinal constant.
	 * @since 2018/09/24
	 */
	@Api
	public final int ordinal()
	{
		return this._ordinal;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/24
	 */
	@Override
	public String toString()
	{
		return this._name;
	}
	
	/**
	 * From the given enumeration, find a value which matches the given name.
	 *
	 * @param <T> The enumeration type to search in.
	 * @param __cl The class to lookup.
	 * @param __s The string to search for.
	 * @return The enumeration value.
	 * @throws IllegalArgumentException If the value was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	@SuppressWarnings({"rawtypes"})
	@Api
	public static <T extends Enum<T>> T valueOf(Class<T> __cl, String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null || __s == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ3x Type is not an enumeration. (The type)} */
		TypeBracket type = TypeShelf.classToType(__cl);
		if (!TypeShelf.isEnum(type))
			throw new ClassCastException("ZZ3x " + __cl);
		
		// Enumerations will be found by their key name
		for (Enum value : TypeShelf.enumValues(type))
			if (__s.equals(value.name()))
				return __cl.cast(value);
		
		/* {@squirreljme.error ZZ15 Not an enumeration value. (The value)} */
		throw new IllegalArgumentException(String.format("ZZ15 %s", __s));
	}
}

