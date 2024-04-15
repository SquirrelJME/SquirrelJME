// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Emulation for {@link ReflectionShelf}.
 *
 * @since 2022/09/07
 */
public class EmulatedReflectionShelf
{
	/**
	 * Invokes the main method of a give type.
	 *
	 * @param __type The type to look for the main method in.
	 * @param __args The arguments to the call.
	 * @throws MLECallError If any argument is {@code null}.
	 * @throws Throwable Any exception thrown by the target.
	 * @since 2022/09/07
	 */
	public static void invokeMain(TypeBracket __type,
		String... __args)
		throws MLECallError, Throwable
	{
		if (__type == null || __args == null)
			throw new MLECallError("Null arguments.");
		
		// Go into the class
		Method mainMethod = null;
		try
		{
			// Find class
			Class<?> mainClass = ((EmulatedTypeBracket)__type).javaClass;
			
			// Find main method
			Method[] methods = mainClass.getMethods();
			for (int i = methods.length - 1; i >= 0; i--)
			{
				Method maybe = methods[i];
				int flags = maybe.getModifiers();
				
				// Needs to match everything
				if ((flags & (Modifier.PUBLIC | Modifier.STATIC)) ==
					(Modifier.PUBLIC | Modifier.STATIC) &&
					"main".equals(maybe.getName()) &&
					maybe.getReturnType() == Void.TYPE &&
					maybe.getParameterCount() == 1 &&
					String[].class == maybe.getParameterTypes()[0])
				{
					mainMethod = maybe;
					break;
				}
			}
			
			// Not found?
			if (mainMethod == null)
				throw new MLECallError("No public static void " +
					"main(String[]) in " + mainClass);
			
			// Invoke call
			mainMethod.setAccessible(true);
			mainMethod.invoke(null, (Object)__args);
		}
		catch (InvocationTargetException __e)
		{
			// Just throw what we wrapped around
			throw __e.getTargetException();
		}
		catch (IllegalAccessException __e)
		{
			throw new MLECallError("Cannot access main.", __e);
		}
	}
}
