// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class provides native virtual machine access to objects.
 *
 * @since 2017/11/10
 */
public abstract class ObjectFunctions
{
	/**
	 * Internal implementation of {@link #identityHashCode(Object)}.
	 *
	 * @param __o The object to get the hashcode for.
	 * @return The identity hashcode for the given object.
	 * @since 2017/11/10
	 */
	protected abstract int protectedIdentityHashCode(Object __o);
	
	/**
	 * Returns the class object for the class that uses the specified name.
	 *
	 * @param __n The name of the class to get the class object for.
	 * @return The class object for the given class or {@code null} if it was
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public final Class<?> classForName(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the identity hash code for the given object.
	 *
	 * @param __o The object to get the identity hash code for.
	 * @return The identity hash code of the given object.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/10
	 */
	public final int identityHashCode(Object __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return protectedIdentityHashCode(__o);
	}
	
	/**
	 * Package access for {@link #protectedIdentityHashCode(Object)}.
	 *
	 * @param __o The object to get the hashcode for.
	 * @return The identity hashcode for the given object.
	 * @since 2017/11/10
	 */
	final int __identityHashCode(Object __o)
	{
		return protectedIdentityHashCode(__o);
	}
}

