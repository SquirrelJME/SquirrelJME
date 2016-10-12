// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains static methods which are used by the standard class
 * library code to perform special system and virtual machine related
 * functions.
 *
 * @since 2016/08/07
 */
public final class SquirrelJME
{
	/**
	 * Not used.
	 *
	 * @since 2016/08/07
	 */
	private SquirrelJME()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Returns the class object for the class that uses the specified name.
	 *
	 * @param __n The name of the class to get the class object for.
	 * @return The class object for the given class or {@code null} if it was
	 * not found.
	 * @since 2016/09/30
	 */
	public static Class<?> classForName(String __n)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Returns the class type of the specified object.
	 *
	 * @param __o The object to get the class of.
	 * @return The class type of the given object.
	 * @since 2016/08/07
	 */
	public static Class<?> classOf(Object __o)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Returns the number of milliseconds that have passed in the UTC
	 * timezone since the Java epoch.
	 *
	 * @return The amount of milliseconds that have passed.
	 * @since 2016/08/07
	 */
	public static long currentTimeMillis()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * This exits the virtual machine using the specifed exit code.
	 *
	 * @param __e The exit code to use.
	 * @since 2016/08/07
	 */
	public static void exit(int __e)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * This suggests that the garbage collector should be ran.
	 *
	 * @since 2016/08/07
	 */
	public static void gc()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Returns the identity hash code of the specified object.
	 *
	 * Note that for memory reduction, the hash code is only 16-bits wide.
	 *
	 * @return The object's identity hashcode.
	 * @since 2016/08/07
	 */
	public static short identityHashCode(Object __o)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Returns the amount of time which has passed on an unspecified
	 * monotonic clock.
	 *
	 * @return The number of monotonic nanoseconds which have passed.
	 * @since 2016/08/07
	 */
	public static long nanoTime()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Writes a single byte to standard error.
	 *
	 * @param __b The byte to write.
	 * @since 2016/08/07
	 */
	public static void stdErr(int __b)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Writes multiple bytes to standard error.
	 *
	 * @param __b The bytes to write.
	 * @param __o The starting offset.
	 * @param __l The number of bytes to write.
	 * @since 2016/08/07
	 */
	public static void stdErr(byte[] __b, int __o, int __l)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Writes a single byte to standard output.
	 *
	 * @param __b The byte to write.
	 * @since 2016/08/07
	 */
	public static void stdOut(int __b)
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Writes multiple bytes to standard output.
	 *
	 * @param __b The bytes to write.
	 * @param __o The starting offset.
	 * @param __l The number of bytes to write.
	 * @since 2016/08/07
	 */
	public static void stdOut(byte[] __b, int __o, int __l)
	{
		throw new RuntimeException("OOPS");
	}
}

