// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

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
	 * @throws InvalidClassFormatException If the component type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public static final String componentType(String __cl)
		throws IllegalArgumentException, InvalidClassFormatException,
			NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error SV0o Cannot get component type of empty
		// class name.}
		int len = __cl.length();
		if (len <= 0)
			throw new IllegalArgumentException("SV0o");
		
		// {@squirreljme.error SV0p The class name is not an array.}
		if (__cl.charAt(0) != '[')
			throw new IllegalArgumentException("SV0p");
		
		switch (__cl.charAt(1))
		{
				// Primitive types
			case 'Z':	return "boolean";
			case 'B':	return "byte";
			case 'S':	return "short";
			case 'C':	return "char";
			case 'I':	return "int";
			case 'J':	return "long";
			case 'F':	return "float";
			case 'D':	return "double";
			
				// Another array
			case '[':
				return __cl.substring(1);
				
				// Class
			case 'L':
				// {@squirreljme.error SV0r Expected class name of array type
				// to end in semi-colon.}
				if (__cl.charAt(len - 1) != ';')
					throw new IllegalArgumentException("SV0r");
				return __cl.substring(2, len - 1);
			
				// {@squirreljme.error SV0q Unknown component type.}
			default:
				throw new InvalidClassFormatException("SV0q");
		}
	}
	
	/**
	 * Returns the number of dimensions the class name has.
	 *
	 * @param __cl The class name to check.
	 * @return The dimension count.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public static final int dimensions(String __cl)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Non-array types have no dimensions ever
		if (!ClassNameUtils.isArray(__cl))
			return 0;
		
		// Count up to the last one
		for (int i = 0, n = __cl.length(); i < n; i++)
			if (__cl.charAt(i) != '[')
				return i - 1;
		
		// {@squirreljme.error SV0y Malformed array class name.}
		throw new InvalidClassFormatException("SV0y");
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
	 * Is this the class info class?
	 *
	 * @param __cl The class name to check.
	 * @return If this is for class info.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/17
	 */
	public static final boolean isClassInfo(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return "cc/squirreljme/jvm/ClassInfo".equals(__cl);
	}
	
	/**
	 * Checks whether the specified class is object.
	 *
	 * @param __cl The class name to check.
	 * @return If this is the object class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/29
	 */
	public static final boolean isObject(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return "java/lang/Object".equals(__cl);
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

