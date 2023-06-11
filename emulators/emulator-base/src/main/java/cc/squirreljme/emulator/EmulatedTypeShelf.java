// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Emulation for {@link TypeShelf}.
 *
 * @since 2022/09/05
 */
public class EmulatedTypeShelf
{
	/**
	 * Returns the type that is used within the given class.
	 *
	 * @param __cl The class to get the type of.
	 * @return The type of the given class.
	 * @since 2020/06/07
	 */
	public static TypeBracket classToType(Class<?> __cl)
	{
		if (__cl == null)
			throw new MLECallError("Null arguments.");
		
		return new EmulatedTypeBracket(__cl);
	}
	
	/**
	 * Finds a type by its name.
	 *
	 * @param __name The name of the type.
	 * @return The type bracket for the type or {@code null} if none was
	 * found.
	 * @since 2020/06/02
	 */
	public static TypeBracket findType(String __name)
		throws MLECallError
	{
		if (__name == null)
			throw new MLECallError("No name specified.");
		
		try
		{
			return new EmulatedTypeBracket(
				Class.forName(__name.replace('/', '.')));
		}
		catch (ClassNotFoundException __e)
		{
			throw new MLECallError("Class not found: " + __name, __e);
		}
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
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		// Get all the interfaces
		Class<?> javaClass = EmulatedTypeShelf.typeToClass(__type);
		Class<?>[] interfaces = javaClass.getInterfaces();
		int count = interfaces.length;
		
		// Build result
		TypeBracket[] result = new TypeBracket[count];
		for (int i = 0; i < count; i++)
			result[i] = EmulatedTypeShelf.classToType(interfaces[i]);
		
		return result;
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
	public static <T> Class<T> typeToClass(TypeBracket __type)
	{
		if (__type == null)
			throw new MLECallError("Null arguments.");
		
		try
		{
			return (Class<T>)((EmulatedTypeBracket)__type).javaClass;
		}
		catch (ClassCastException e)
		{
			throw new MLECallError("Wrong type.");
		}
	}
}
