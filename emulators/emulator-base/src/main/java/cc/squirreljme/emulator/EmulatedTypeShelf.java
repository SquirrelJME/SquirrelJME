// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;

/**
 * Emulation for {@link TypeShelf}.
 *
 * @since 2022/09/05
 */
@SuppressWarnings({"unused", "UtilityClassWithoutPrivateConstructor", 
	"PublicConstructor"})
public class EmulatedTypeShelf
{
	/**
	 * Returns the binary name of the given class.
	 *
	 * @param __type The type to get the binary name of.
	 * @return The binary name of this class.
	 * @throws MLECallError If the type is not valid.
	 * @since 2023/07/19
	 */
	public static String binaryName(Class<?> __type)
		throws MLECallError
	{
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		if (__type.isArray())
			return __type.getName();
		return __type.getName().replace('.', '/');
	}
	
	/**
	 * Returns the type that is used within the given class.
	 *
	 * @param __cl The class to get the type of.
	 * @return The type of the given class.
	 * @since 2020/06/07
	 */
	public static Class<?> classToType(Class<?> __cl)
	{
		if (__cl == null)
			throw new MLECallError("Null arguments.");
		
		return __cl;
	}
	
	/**
	 * Returns the root component of this type, this is essentially calling
	 * {@code #component(Class<?>)} over until the type is no longer an
	 * array type.
	 *
	 * @param __type The type to get the root component of.
	 * @return The root component of the type.
	 * @since 2023/07/19
	 */
	@SquirrelJMEVendorApi
	public static Class<?> componentRoot(
		@NotNull Class<?> __type)
	{
		if (__type == null)
			throw new MLECallError("No type specified.");
		
		Class<?> javaClass = __type;
		while (javaClass.isArray())
			javaClass = javaClass.getComponentType();
		
		return javaClass;
	}
	
	/**
	 * Finds a type by its name.
	 *
	 * @param __name The name of the type.
	 * @return The type bracket for the type or {@code null} if none was
	 * found.
	 * @since 2020/06/02
	 */
	public static Class<?> findType(String __name)
		throws MLECallError
	{
		if (__name == null)
			throw new MLECallError("No name specified.");
		
		try
		{
			return Class.forName(__name.replace('/', '.'));
		}
		catch (ClassNotFoundException __e)
		{
			throw new MLECallError("Class not found: " + __name, __e);
		}
	}
	
	/**
	 * Returns the JAR that the type is within.
	 *
	 * @param __type The type to get the JAR of.
	 * @return The JAR that the type is within or {@code null} if this type
	 * is not within any JAR.
	 * @since 2023/07/19
	 */
	@SquirrelJMEVendorApi
	public static JarPackageBracket inJar(Class<?> __type)
	{
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		Class<?> javaClass = EmulatedTypeShelf.typeToClass(__type);
		
		// Arrays and primitives are in no jar
		if (javaClass.isArray() || javaClass.isPrimitive())
			return null;
		
		// Get the file name of the class
		String fileName = javaClass.getName()
			.replace('.', '/') + ".class";
		
		// This is a bit of a cheat, but we go through all of our JARs and
		// try to find the class this belongs to... the emulation layer fakes
		// the libraries, but they should still technically be valid in finding
		// which Jar a class is in... at least via resource
		for (JarPackageBracket library : EmulatedJarPackageShelf.libraries())
		{
			try (InputStream in = JarPackageShelf.openResource(
				library, fileName))
			{
				// If the resource was opened, then we have this
				if (in != null)
					return library;
			}
			catch (IOException ignored)
			{
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Returns the interfaces of the type.
	 *
	 * @param __type The type to get the interfaces of.
	 * @return The interfaces of the class.
	 * @throws MLECallError If {@code __type} is null. 
	 * @since 2020/06/07
	 */
	public static Class<?>[] interfaces(Class<?> __type)
		throws MLECallError
	{
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		// Get all the interfaces
		Class<?> javaClass = EmulatedTypeShelf.typeToClass(__type);
		Class<?>[] interfaces = javaClass.getInterfaces();
		int count = interfaces.length;
		
		// Build result
		Class<?>[] result = new Class<?>[count];
		for (int i = 0; i < count; i++)
			result[i] = EmulatedTypeShelf.classToType(interfaces[i]);
		
		return result;
	}
	
	/**
	 * Returns true if this type is an array.
	 *
	 * @param __type The type to check.
	 * @return If this is an array.
	 * @throws MLECallError If {@code __type} is {@code null}.
	 * @since 2023/07/19
	 */
	@SquirrelJMEVendorApi
	public static boolean isArray(Class<?> __type)
		throws MLECallError
	{
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		return __type.isArray();
	}
	
	/**
	 * Gets the class type of the given bracket.
	 *
	 * @param <T> Ignored.
	 * @param __type The type to get the class object of.
	 * @return The class type for the given type.
	 * @since 2020/05/30
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> typeToClass(Class<?> __type)
	{
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		try
		{
			return (Class<T>)__type;
		}
		catch (ClassCastException e)
		{
			throw new MLECallError("Wrong type.");
		}
	}
}
