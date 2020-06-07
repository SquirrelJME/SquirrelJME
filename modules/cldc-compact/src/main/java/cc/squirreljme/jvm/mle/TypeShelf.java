// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;

/**
 * Provides the shelf for types that exist within the JVM.
 *
 * @since 2020/05/30
 */
public final class TypeShelf
{
	/**
	 * Returns the binary name of the given class.
	 *
	 * @param __type The type to get the binary name of.
	 * @return The binary name of this class.
	 * @since 2020/06/07
	 */
	public static native String binaryName(TypeBracket __type);
	
	/**
	 * Returns the name of the package the class is within.
	 *
	 * @param __type The type to get the binary package of.
	 * @return The binary name of the package the class is in.
	 * @since 2020/06/07
	 */
	public static native String binaryPackageName(TypeBracket __type);
	
	/**
	 * Returns the type that is used within the given class.
	 *
	 * @param __cl The class to get the type of.
	 * @return The type of the given class.
	 * @since 2020/06/07
	 */
	public static native TypeBracket classToType(Class<?> __cl);
	
	/**
	 * Returns the component type of the class.
	 *
	 * @param __type The type to get the component of.
	 * @return The component type of the class.
	 * @since 2020/06/07
	 */
	public static native TypeBracket component(TypeBracket __type);
	
	/**
	 * Returns the root component of this type, this is essentially calling
	 * {@link #component(TypeBracket)} over until the type is no longer an
	 * array type.
	 *
	 * @param __type The type to get the root component of.
	 * @return The root component of the type.
	 * @since 2020/06/07
	 */
	public static native TypeBracket componentRoot(TypeBracket __type);
	
	/**
	 * Returns the number of dimensions this type has.
	 *
	 * @param __type The type to get the dimension count for.
	 * @return The number of dimensions for the type, this will be {@code 0}
	 * if this is not an array.
	 * @since 2020/07/06
	 */
	public static native int dimensions(TypeBracket __type);
	
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
	 * Returns the JAR that the type is within.
	 *
	 * @param __type The type to get the JAR of.
	 * @return The JAR that the type is within or {@code null} if this type
	 * is not within any JAR.
	 * @since 2020/06/07
	 */
	public static native JarPackageBracket inJar(TypeBracket __type);
	
	/**
	 * Returns the interfaces of the type.
	 *
	 * @param __type The type to get the interfaces of.
	 * @return The interfaces of the class.
	 * @since 2020/06/07
	 */
	public static native TypeBracket[] interfaces(TypeBracket __type);
	
	/**
	 * Returns true if this type is an array.
	 *
	 * @param __type The type to check.
	 * @return If this is an array.
	 * @since 2020/06/07
	 */
	public static native boolean isArray(TypeBracket __type);
	
	/**
	 * Returns true if this type is an interface.
	 *
	 * @param __type The type to check.
	 * @return If this is an interface.
	 * @since 2020/06/07
	 */
	public static native boolean isInterface(TypeBracket __type);
	
	/**
	 * Returns true if this is a primitive type.
	 *
	 * @param __type The type to check.
	 * @return If this is a primitive.
	 * @since 2020/06/07
	 */
	public static native boolean isPrimitive(TypeBracket __type);
	
	/**
	 * Returns the type of the given object.
	 *
	 * @param __o The object to get the type of.
	 * @return The type of the given object.
	 * @since 2020/06/02
	 */
	public static native TypeBracket objectType(Object __o);
	
	/**
	 * Returns the run-time name of a type as it would be if
	 * {@link Class#getName()} were called.
	 *
	 * @param __type The type to get the name of.
	 * @return The runtime name of the class.
	 * @since 2020/06/07
	 */
	public static native String runtimeName(TypeBracket __type);
	
	/**
	 * Returns the super class of this class.
	 *
	 * @param __type The super class of this type.
	 * @return Return the super class or {@code null} on null arguments.
	 * @since 2020/06/07
	 */
	public static native TypeBracket superClass(TypeBracket __type);
	
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
