// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;

/**
 * Provides the shelf for types that exist within the JVM.
 *
 * @since 2020/05/30
 */
public final class TypeShelf
{
	/**
	 * Since types are virtual objects that do not have a true base off
	 * {@link Object}, this method is used to compare two types with each
	 * other.
	 *
	 * @param __a The first type.
	 * @param __b The second type.
	 * @return If these two types are the same.
	 * @since 2020/06/04
	 */
	public static native boolean equals(TypeBracket __a, TypeBracket __b);
	
	/**
	 * Finds a type by it's name.
	 *
	 * @param __name The name of the type.
	 * @return The type bracket for the type or {@code null} if none was
	 * found.
	 * @since 2020/06/02
	 */
	public static native TypeBracket findType(String __name);
	
	/**
	 * Returns the type of the given object.
	 *
	 * @param __o The object to get the type of.
	 * @return The type of the given object.
	 * @since 2020/06/02
	 */
	public static native TypeBracket objectType(Object __o);
	
	/**
	 * Returns the type holder for the {@code boolean} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfBoolean();
	
	/**
	 * Returns the type holder for the {@code byte} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfByte();
	
	/**
	 * Returns the type holder for the {@code short} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfShort();
	
	/**
	 * Returns the type holder for the {@code char} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfCharacter();
	
	/**
	 * Returns the type holder for the {@code int} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfInteger();
	
	/**
	 * Returns the type holder for the {@code long} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfLong();
	
	/**
	 * Returns the type holder for the {@code float} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfFloat();
	
	/**
	 * Returns the type holder for the {@code double} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static native TypeBracket typeOfDouble();
	
	/**
	 * Gets the class type of the given bracket.
	 *
	 * @param <T> Ignored.
	 * @param __type The type to get the class object of.
	 * @return The class type for the given type.
	 * @since 2020/05/30
	 */
	public static native <T> Class<T> typeToClass(TypeBracket __type);
}
