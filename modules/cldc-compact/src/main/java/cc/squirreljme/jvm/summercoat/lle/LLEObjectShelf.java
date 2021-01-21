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
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.constants.MonitorResultType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This shelf supports object anything that has to do with objects.
 *
 * @since 2020/06/09
 */
public final class LLEObjectShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/09
	 */
	private LLEObjectShelf()
	{
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(boolean[] __src, int __srcOff,
		boolean[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(byte[] __src, int __srcOff,
		byte[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(short[] __src, int __srcOff,
		short[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(char[] __src, int __srcOff,
		char[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(int[] __src, int __srcOff,
		int[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(long[] __src, int __srcOff,
		long[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(float[] __src, int __srcOff,
		float[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	public static void arrayCopy(double[] __src, int __srcOff,
		double[] __dest, int __destOff, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the length of the array if this object is an array.
	 *
	 * @param __object The object to get the length of.
	 * @return The length of the array or a negative value if this is not an
	 * array.
	 * @since 2020/06/09
	 */
	public static int arrayLength(Object __object)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Allocates a new array.
	 *
	 * @param <T> The resultant type of the array.
	 * @param __type The type to allocate the array for.
	 * @param __len The length of the array.
	 * @return The newly allocated array as the given object.
	 * @since 2020/06/09
	 */
	public static <T> T arrayNew(TypeBracket __type, int __len)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Checks if the given thread holds the lock on the given method.
	 *
	 * @param __javaThread The Java thread to check if it holds the lock.
	 * @param __o The object to check.
	 * @return If the given thread holds the lock.
	 * @since 2020/06/17
	 */
	public static boolean holdsLock(Thread __javaThread, Object __o)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the identity hashcode of the object.
	 *
	 * @param __o The object to get the hashcode of.
	 * @return The identity hashcode of the object.
	 * @since 2020/06/18
	 */
	public static int identityHashCode(Object __o)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Creates a new instance of the given type.
	 *
	 * @param __type The type to instantiate.
	 * @return The newly created object or {@code null} if there was no
	 * memory left.
	 * @since 2020/06/17
	 */
	public static Object newInstance(TypeBracket __type)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Notifies the monitors holding onto this object.
	 * 
	 * @param __object The object to signal.
	 * @param __all Notify all threads?
	 * @return The {@link MonitorResultType}.
	 * @since 2020/06/22
	 */
	public static int notify(Object __object, boolean __all)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Waits on the given monitor.
	 * 
	 * @param __object The object to wait on.
	 * @param __ms The milliseconds to wait.
	 * @param __ns The nanoseconds to wait.
	 * @return The {@link MonitorResultType}.
	 * @since 2020/06/22
	 */
	public static int wait(Object __object, long __ms, int __ns)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
