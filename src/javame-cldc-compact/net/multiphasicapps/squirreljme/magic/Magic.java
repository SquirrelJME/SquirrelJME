// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.magic;

import java.io.OutputStream;

/**
 * This class contains methods which are internal to the virtual machine and
 * is used by the interpreter and the native compiler to perform work that Java
 * has no abstractions for without using native methods.
 *
 * THIS CLASS IS FOR INTERNAL USE ONLY.
 *
 * @since 2016/03/02
 */
@Deprecated
public abstract class Magic
{
	/** Standard output stream. */
	@Deprecated
	private static final __StdOutOutputStream__ _stdout =
		new __StdOutOutputStream__();
	
	/** Standard error stream. */
	@Deprecated
	private static final __StdErrOutputStream__ _stderr =
		new __StdErrOutputStream__();
	
	/**
	 * Not initialized or its initialization is faked.
	 *
	 * @since 2016/03/02
	 */
	@Deprecated
	private Magic()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Returns the package which called the method calling this.
	 *
	 * @return The package which called the method calling this.
	 * @since 2016/03/02
	 */
	@Deprecated
	public static String callingPackage()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the component type of the given array if it is one, otherwise
	 * the input will be returned.
	 *
	 * @param __ct If the class is an array, the component type will be
	 * obtained.
	 * @return The component type if an array, or {@code __ct} if it is not
	 * one.
	 * @since 2016/03/02
	 */
	@Deprecated
	public static Class<?> componentType(Class<?> __ct)
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Matches the specified string with the internal string array (if one
	 * exists) so that {@link String@intern()}ed strings match pre-defined
	 * and constant strings.
	 *
	 * To implementors of this magic, the best result would be to have the
	 * internal strings sorted so that {@code compareTo()} works correctly.
	 * Thus, since the internal strings are always of fixed size, they can
	 * easily be searched for without probing the entire array (it would be
	 * O(log(n)) instead of O(n)).
	 *
	 * @param __s The string to match.
	 * @return The string which matches the given string or {@code null} if
	 * no internal string was found.
	 * @since 2016/04/02
	 */
	@Deprecated
	public static String matchInternalString(String __s)
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code boolean}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Boolean> primitiveBooleanClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code byte}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Byte> primitiveByteClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code char}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Character> primitiveCharacterClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code double}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Double> primitiveDoubleClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code float}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Float> primitiveFloatClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code int}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Integer> primitiveIntegerClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code long}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Long> primitiveLongClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns the class type for the primitive type {@code short}.
	 *
	 * @return The class for it.
	 * @since 2016/03/31
	 */
	@Deprecated
	public static Class<Short> primitiveShortClass()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns a raw output stream which is attached to standard error.
	 *
	 * @return The stream attached to standard error.
	 * @since 2016/03/17
	 */
	@Deprecated
	public static OutputStream stdErr()
	{
		return _stderr;
	}
	
	/**
	 * Returns a raw output stream which is attached to standard output.
	 *
	 * @return The stream attached to standard output.
	 * @since 2016/03/17
	 */
	@Deprecated
	public static OutputStream stdOut()
	{
		return _stdout;
	}
}

