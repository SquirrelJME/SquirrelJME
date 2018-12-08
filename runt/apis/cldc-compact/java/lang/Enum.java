// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import cc.squirreljme.runtime.cldc.asm.StaticMethod;
import cc.squirreljme.runtime.cldc.lang.ClassData;

/**
 * This is the base class for enum types.
 *
 * @param <E> The enum type.
 * @since 2018/09/24
 */
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
	protected Enum(String __s, int __o)
		throws IllegalArgumentException, NullPointerException
	{
		// {@squirreljme.error ZZ0n Enum has no string.}
		if (__s == null)
			throw new NullPointerException("ZZ0n");
		
		// {@squirreljme.error ZZ0o Enum has negative ordinal.}
		if (__o < 0)
			throw new IllegalArgumentException("ZZ0o");
		
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
		// {@squirreljme.error ZZ0p Enums cannot be cloned.}
		throw new CloneNotSupportedException("ZZ0p");
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
		
		// {@squirreljme.error ZZ3h Cannot compare enums of a different
		// type.}
		if (this.getDeclaringClass() != __o.getDeclaringClass())
			throw new ClassCastException("ZZ3h");
		
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
	public static <T extends Enum<T>> T valueOf(Class<T> __cl, String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Get the data for this class because the enum information will be
		// in here somewhere
		ClassData data = ObjectAccess.classData(__cl);
		
		// {@squirreljme.error ZZ3i Cannot get the value of a non-enumeration
		// type or it has no implicit {@code values()} method.}
		StaticMethod getvalues = data.enumValues();
		if (getvalues == null)
			throw new ClassCastException("ZZ3i");
		
		// Go through and check all the names
		for (Enum e : (Enum[])ObjectAccess.invokeStatic(getvalues))
			if (__s.equals(e.name()))
				return __cl.cast(e);
		
		// {@squirreljme.error ZZ3j Not an enumeration value. (The value)}
		throw new IllegalArgumentException(String.format("ZZ3j %s", __s));
	}
}

