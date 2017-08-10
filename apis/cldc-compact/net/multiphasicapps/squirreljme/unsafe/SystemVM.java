// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains static methods which are used by the standard class
 * library code to perform special system and virtual machine related
 * functions. These methods are specific to SquirrelJME and using them will
 * cause your code to not be compatible with other virtual machines.
 *
 * @see __Ext_SystemVM__
 * @since 2017/08/10
 */
public final class SystemVM
{
	/** Private access. */
	public static final int ACCESS_PRIVATE =
		0;
	
	/** Package private access. */
	public static final int ACCESS_PACKAGE_PRIVATE =
		1;
	
	/** Protected access. */
	public static final int ACCESS_PROTECTED =
		2;
	
	/** Public access. */
	public static final int ACCESS_PUBLIC =
		3;
	
	/**
	 * Not used.
	 *
	 * @since 2017/08/10
	 */
	private SystemVM()
	{
	}
	
	/**
	 * Creates a new instance of the given class.
	 *
	 * @param <C> The class to allocate.
	 * @param __cl The class to allocate.
	 * @return The allocated class.
	 * @throws IllegalAccessException If the class cannot be accessed from the
	 * calling class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/30
	 */
	public static <C> C allocateInstance(Class<C> __cl)
		throws IllegalAccessException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * This throws an index out of bounds exception.
	 *
	 * @param __i The out of bounds index.
	 * @param __l The length of the array.
	 * @throws ArrayIndexOutOfBoundsException Always.
	 * @since 2017/05/13
	 */
	public static void arrayIndexOutOfBounds(int __i, int __l)
		throws ArrayIndexOutOfBoundsException
	{
		throw new ArrayIndexOutOfBoundsException("ZZ0a " + __i + " " + __l);
	}
	
	/**
	 * Returns the class which called the method which called this method.
	 *
	 * With the following stack trace:
	 * {@code
	 * SquirrelJME.callingClass()
	 * ClassB.methodB()
	 * ClassA.methodA()
	 * } this returns {@code ClassA}.
	 *
	 * @return The class which called the method which called this method.
	 * @since 2017/03/24
	 */
	public static Class<?> callingClass()
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * Returns a stream to the resource associated with the given class.
	 *
	 * @param __cl The class to get a resource from.
	 * @param __rc The resource name of the class.
	 * @return The input stream of the resource or {@code null} if it does not
	 * exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public static InputStream classResource(Class<?> __cl, String __rc)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the access level of the classe's default constructor.
	 *
	 * @param __cl The class to get the default constructor for.
	 * @return The access level, if there is no default constructor then a
	 * negative value is returned.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public static int defaultConstructorAccess(Class<?> __cl)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * This suggests that the garbage collector should be ran.
	 *
	 * @since 2016/08/07
	 */
	public static void gc()
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * Checks if the specified class is visible from the from class.
	 *
	 * @param __from The source class to check visibility for.
	 * @param __cl The target class to check if it is visible.
	 * @return {@code true} if the specified class is visible.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public static boolean isClassVisibleFrom(Class<?> __from, Class<?> __cl)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks whether the specified object is an instance of the given class.
	 *
	 * @param __cl The class to check.
	 * @param __o The object to check if it is an instance.
	 * @return {@code true} if the object is an instance of the specified
	 * class.
	 * @throws NullPointerException If no class was specified.
	 * @since 2017/03/24
	 */
	public static boolean isInstance(Class<?> __cl, Object __o)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks if the two classes are in the same package.
	 *
	 * @param __a The first class.
	 * @param __b The second class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public static boolean isSamePackage(Class<?> __a, Class<?> __b)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * This is used to detect whether the environment truly is running on
	 * SquirrelJME.
	 *
	 * @return {@code true} if running on SquirrelJME, otherwise {@code false}.
	 * @since 2016/10/11
	 */
	public static boolean isSquirrelJME()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new instance of the given class.
	 *
	 * @param <C> The class to create.
	 * @param __cl The class to create.
	 * @return The created class.
	 * @throws IllegalAccessException If the class cannot be accessed from the
	 * calling class.
	 * @throws InstantiationException If the class could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public static <C> C newInstance(Class<C> __cl)
		throws IllegalAccessException, InstantiationException,
			NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * This obtains a class which implements a system specific service.
	 * All services that exist are singletons, calling this same method twice
	 * must return the same object for the given class.
	 *
	 * @param <C> The class to get the singleton service for.
	 * @param __cl The class to get the singleton service for.
	 * @return The singleton instance of the given class or {@code null} if
	 * no service for that class exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public static <C> C systemService(Class<C> __cl)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Throws the specified throwable, on successful throws this method does
	 * not return. This method also causes the stack to unwind.
	 *
	 * @param __t The throwable to throw
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/31
	 */
	public static void throwThrowable(Throwable __t)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

