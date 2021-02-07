// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.lle;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * SummerCoat implementation of {@link TypeShelf}, provides the shelf for
 * types that exist within the JVM.
 *
 * @since 2020/05/30
 */
public final class LLETypeShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/06/30
	 */
	private LLETypeShelf()
	{
	}
	
	/**
	 * Returns the binary name of the given class.
	 *
	 * @param __type The type to get the binary name of.
	 * @return The binary name of this class.
	 * @since 2020/06/07
	 */
	public static String binaryName(TypeBracket __type)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the name of the package the class is within.
	 *
	 * @param __type The type to get the binary package of.
	 * @return The binary name of the package the class is in.
	 * @since 2020/06/07
	 */
	public static String binaryPackageName(TypeBracket __type)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type that is used within the given class.
	 *
	 * @param __cl The class to get the type of.
	 * @return The type of the given class.
	 * @since 2020/06/07
	 */
	public static TypeBracket classToType(Class<?> __cl)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the component type of the class.
	 *
	 * @param __type The type to get the component of.
	 * @return The component type of the class.
	 * @throws MLECallError If {@code __type} is {@code null} or is not an
	 * array type.
	 * @since 2020/06/07
	 */
	public static TypeBracket component(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the root component of this type, this is essentially calling
	 * {@link #component(TypeBracket)} over until the type is no longer an
	 * array type.
	 *
	 * @param __type The type to get the root component of.
	 * @return The root component of the type.
	 * @since 2020/06/07
	 */
	public static TypeBracket componentRoot(TypeBracket __type)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of dimensions this type has.
	 *
	 * @param __type The type to get the dimension count for.
	 * @return The number of dimensions for the type, this will be {@code 0}
	 * if this is not an array.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/07/06
	 */
	public static int dimensions(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Gets the enumeration values for the given type.
	 * 
	 * @param __type The type to get the enumerated values from.
	 * @return The enumeration values for the given type.
	 * @throws MLECallError If {@code __type} is {@code null} or is not an
	 * enumeration.
	 * @since 2020/06/28
	 */
	@SuppressWarnings("rawtypes")
	public static Enum[] enumValues(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Since types are virtual objects that do not have a true base off
	 * {@link Object}, this method is used to compare two types with each
	 * other.
	 *
	 * @param __a The first type.
	 * @param __b The second type.
	 * @return If these two types are the same.
	 * @throws MLECallError If either are {@code null}.
	 * @since 2020/06/04
	 */
	public static boolean equals(TypeBracket __a, TypeBracket __b)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Finds a type by it's name.
	 *
	 * @param __name The name of the type.
	 * @return The type bracket for the type or {@code null} if none was
	 * found.
	 * @since 2020/06/02
	 */
	public static TypeBracket findType(String __name)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the JAR that the type is within.
	 *
	 * @param __type The type to get the JAR of.
	 * @return The JAR that the type is within or {@code null} if this type
	 * is not within any JAR.
	 * @since 2020/06/07
	 */
	public static JarPackageBracket inJar(TypeBracket __type)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the interfaces of the type.
	 *
	 * @param __type The type to get the interfaces of.
	 * @return The interfaces of the class.
	 * @throws MLECallError If {@code __type} is null. 
	 * @since 2020/06/07
	 */
	public static TypeBracket[] interfaces(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns true if this type is an array.
	 *
	 * @param __type The type to check.
	 * @return If this is an array.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/06/07
	 */
	public static boolean isArray(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Performs the same logic as {@link Class#isAssignableFrom(Class)}, 
	 * checks if the given class can be assigned to this one. The check is
	 * in the same order as {@code instanceof Object} that is
	 * {@code a.getClass().isAssignableFrom(b.getClass()) == (a instanceof b)}
	 * and {@code (Class<B>)a} does not throw {@link ClassCastException}.
	 * 
	 * @param __source The basis class
	 * @param __target The target class which is checked for assignment.
	 * @return If the given 
	 * @throws MLECallError On null arguments.
	 * @since 2021/02/07
	 */
	public static boolean isAssignableFrom(TypeBracket __source,
		TypeBracket __target)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
		
		/*
		TypeBracket self = this._type;
		TypeBracket other = TypeShelf.classToType(__cl);
		
		// Scan through the current and super classes of this class
		for (TypeBracket rover = self; rover != null;
			rover = TypeShelf.superClass(rover))
		{
			// Is this the same type?
			if (TypeShelf.equals(self, rover))
				return true;
			
			// Go through interfaces
			for (TypeBracket iFace : TypeShelf.interfaces(rover))
				if (TypeShelf.equals(self, iFace))
					return true;
		}
		
		// If this is an array and the other type is an array with the same
		// number of dimensions, then compare the base type so that say
		// Number[] is assignable from Integer[].
		if (TypeShelf.isArray(self) && TypeShelf.isArray(other))
		{
			int thisDims = TypeShelf.dimensions(self);
			int otherDims = TypeShelf.dimensions(other);
			
			if (thisDims > 0 && thisDims == otherDims)
				return TypeShelf.typeToClass(TypeShelf.componentRoot(self))
					.isAssignableFrom(TypeShelf.typeToClass(
						TypeShelf.componentRoot(other)));
		}
		
		// Not assignable
		return false;
		 */
	}
	
	/**
	 * Checks if this is an enumeration.
	 * 
	 * @param __type The type to check.
	 * @return If this is an enumeration.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/06/28
	 */
	public static boolean isEnum(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns true if this type is an interface.
	 *
	 * @param __type The type to check.
	 * @return If this is an interface.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/06/07
	 */
	public static boolean isInterface(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns true if this is a primitive type.
	 *
	 * @param __type The type to check.
	 * @return If this is a primitive.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/06/07
	 */
	public static boolean isPrimitive(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type of the given object.
	 *
	 * @param __o The object to get the type of.
	 * @return The type of the given object.
	 * @since 2020/06/02
	 */
	public static TypeBracket objectType(Object __o)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the run-time name of a type as it would be if
	 * {@link Class#getName()} were called.
	 *
	 * @param __type The type to get the name of.
	 * @return The runtime name of the class.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/06/07
	 */
	public static String runtimeName(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the super class of this class.
	 *
	 * @param __type The super class of this type.
	 * @return Return the super class or {@code null} on null arguments.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2020/06/07
	 */
	public static TypeBracket superClass(TypeBracket __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code boolean} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfBoolean()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code byte} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfByte()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code short} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfShort()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code char} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfCharacter()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code int} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfInteger()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code long} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfLong()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code float} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfFloat()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the type holder for the {@code double} primitive type.
	 *
	 * @return The type for the primitive type.
	 * @since 2020/05/30
	 */
	public static TypeBracket typeOfDouble()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Gets the class type of the given bracket.
	 *
	 * @param <T> Ignored.
	 * @param __type The type to get the class object of.
	 * @return The class type for the given type.
	 * @since 2020/05/30
	 */
	public static <T> Class<T> typeToClass(TypeBracket __type)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
