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
public abstract class Magic
{
	/**
	 * Not initialized or its initialization is faked.
	 *
	 * @since 2016/03/02
	 */
	private Magic()
	{
	}
	
	/**
	 * Returns the package which called the method calling this.
	 *
	 * @return The package which called the method calling this.
	 * @since 2016/03/02
	 */
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
	public static Class<?> componentType(Class<?> __ct)
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns a raw output stream which is attached to standard error.
	 *
	 * @return The stream attached to standard error.
	 * @since 2016/03/17
	 */
	public static OutputStream stdErr()
	{
		throw new ForbiddenMagicError();
	}
	
	/**
	 * Returns a raw output stream which is attached to standard output.
	 *
	 * @return The stream attached to standard output.
	 * @since 2016/03/17
	 */
	public static OutputStream stdOut()
	{
		throw new ForbiddenMagicError();
	}
}

