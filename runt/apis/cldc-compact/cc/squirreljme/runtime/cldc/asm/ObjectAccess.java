// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.ref.PrimitiveReference;
import cc.squirreljme.runtime.cldc.ref.PrimitiveWeakReference;

/**
 * This contains accessors for object information.
 *
 * @since 2018/09/22
 */
public final class ObjectAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/09/22
	 */
	private ObjectAccess()
	{
	}
	
	/**
	 * Returns the class object for the specified class by its binary name.
	 *
	 * @param __s The class to lookup, the binary name is used.
	 * @return The class for the given binary name, or {@code null} if it
	 * does not exist.
	 * @since 2018/09/23 
	 */
	public static final native Class<?> classByName(String __s);
	
	/**
	 * Returns the class object for the given object.
	 *
	 * @param __v The object to get the class of.
	 * @return The class of the given object, or {@code null} if it has no
	 * class.
	 * @since 2018/09/22
	 */
	public static final native Class<?> classOf(Object __v);
	
	/**
	 * Creates a new primitive weak reference. Note that it is not valid to
	 * operate on this object as a normal object, it is a special
	 * representation.
	 *
	 * @return The primitive weak reference.
	 * @since 2018/09/23
	 */
	public static final native PrimitiveReference newWeakReference();
	
	/**
	 * Gets the given reference.
	 *
	 * @param __r The reference to read from.
	 * @return The reference value, may be {@code null} if the input reference
	 * is not valid, it was garbage collected, or it was never set.
	 * @since 2018/09/23
	 */
	public static final native Object referenceGet(PrimitiveReference __r);
	
	/**
	 * Sets the given reference to the given value.
	 *
	 * @param __r The reference to set.
	 * @param __v The value to set.
	 * @since 2018/09/23
	 */
	public static final native void referenceSet(PrimitiveReference __r,
		Object __v);
		
	
	/**
	 * Returns the class object for the specified class by its binary name.
	 *
	 * @param <C> The class to type this as.
	 * @param __s The class to lookup, the binary name is used.
	 * @return The class for the given binary name, or {@code null} if it
	 * does not exist.
	 * @since 2018/09/23 
	 */
	@SuppressWarnings({"unchecked"})
	public static final <C> Class<C> classByNameType(String __s)
	{
		return (Class<C>)((Object)ObjectAccess.classByName(__s));
	}
}

