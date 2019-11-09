// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

/**
 * Utilities for working with class names.
 *
 * @since 2019/11/04
 */
public final class ClassNameUtils
{
	/**
	 * Not used.
	 *
	 * @since 2019/11/04
	 */
	private ClassNameUtils()
	{
	}
	
	/**
	 * Returns the component type of the given array class.
	 *
	 * @param __cl The class to check.
	 * @return The component type of the class.
	 * @throws IllegalArgumentException If this is not an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public static final String componentType(String __cl)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Checks whether the given class name is an array.
	 *
	 * @param __cl The class name to check.
	 * @return If it is an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public static final boolean isArray(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return (__cl.length() >= 1 && __cl.charAt(0) == '[');
	}
	
	/**
	 * Checks whether the given class name is a primitive type.
	 *
	 * @param __cl The class name to check.
	 * @return If it is a primitive type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public static final boolean isPrimitiveType(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		switch (__cl)
		{
			case "boolean":
			case "byte":
			case "short":
			case "char":
			case "int":
			case "long":
			case "float":
			case "double":
				return true;
		}
		
		return false;
	}
	
	/**
	 * Is this an array or primitive type?
	 *
	 * @param __cl The class name to check.
	 * @return If it is an array or primitive type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public static final boolean isSpecial(String __cl)
		throws NullPointerException
	{
		return ClassNameUtils.isArray(__cl) ||
			ClassNameUtils.isPrimitiveType(__cl);
	}
}

