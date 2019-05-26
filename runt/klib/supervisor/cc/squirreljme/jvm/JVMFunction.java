// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This class contains the functions of the virtual machine.
 *
 * @since 2019/05/25
 */
public final class JVMFunction
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/25
	 */
	private JVMFunction()
	{
	}
	
	/**
	 * Checks if the given object can be stored in the array.
	 *
	 * @param __p The array pointer.
	 * @param __v The value to check.
	 * @return If this object can be stored in the array then {@code 1} will
	 * be returned, otherwise {@code 0} will.
	 * @since 2019/04/27
	 */
	public static final int jvmCanArrayStore(int __p, int __v)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the component type of the array
	 *
	 * @param __clid The class ID
	 * @return The component type of the array or {@code 0} if it is not an
	 * array.
	 * @since 2019/04/27
	 */
	public static final int jvmComponentType(int __clid)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Performs aggressive garbage collection of the JVM heap to free as much
	 * memory as possible.
	 *
	 * @since 2019/04/25
	 */
	public static final void jvmGarbageCollect()
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Garbage collects a single object.
	 *
	 * @param __p The object to garbage collect.
	 * @since 2019/04/25
	 */
	public static final void jvmGarbageCollectObject(int __p)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Searches the interface vtables for the class of object {@code __p}
	 * and searches for an interface implementation of class {@code __icl} and
	 * if one is found then the pointer for index {@code __mdx} is returned.
	 *
	 * @param __p The object to do a interface lookup on.
	 * @param __icl The interface class to find.
	 * @param __mdx The method index to relate to, of the interface class.
	 * @return The pointer to the code to be invoked.
	 * @since 2019/04/30
	 */
	public static final int jvmInterfacePointer(int __p, int __icl, int __mdx)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Checks whether the given pointer is an array.
	 *
	 * @param __p The pointer to check.
	 * @return Either {@code 1} if it is an array or {@code 0} if it is not.
	 * @since 2019/04/27
	 */
	public static final int jvmIsArray(int __p)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Checks whether the given pointer is an instance of the given class.
	 *
	 * @param __p The pointer to check.
	 * @param __cldx The class index.
	 * @return Either {@code 1} if the class is an instance or {@code 0} if
	 * it is not.
	 * @since 2019/04/22
	 */
	public static final int jvmIsInstance(int __p, int __cldx)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the {@link Class} instance for the given class info pointer,
	 * if none has been created yet then it will be created as needed.
	 *
	 * @param __cldx The class index pointer.
	 * @since 2019/05/26
	 */
	public static final int jvmLoadClass(int __cldx)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads a string from memory and returns an `intern()` string value.
	 *
	 * @param __p The pointer to load the string bytes from.
	 * @since 2019/05/26
	 */
	public static final int jvmLoadString(int __p)
	{
		// Ignore null pointers
		if (__p == 0)
			return 0;
		
		// Read length of the raw bytes
		int rawlen = Assembly.memReadShort(__p, 0) & 0xFFFF;
		
		// Load the string data into bytes
		byte[] bytes = new byte[rawlen];
		for (int i = 0, base = __p + 2; i < rawlen; i++)
			bytes[i] = (byte)Assembly.memReadByte(base, i);
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Enters the monitor for the given object.
	 *
	 * @param __p The object to enter.
	 * @since 2019/04/26
	 */
	public static final void jvmMonitorEnter(int __p)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Exits the monitor for the given object.
	 *
	 * @param __p The object to exit.
	 * @since 2019/04/26
	 */
	public static final void jvmMonitorExit(int __p)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __cl The class type.
	 * @return The resulting class pointer.
	 * @since 2019/05/24
	 */
	public static final int jvmNew(int __cl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Allocates a new array.
	 *
	 * @param __at The array type.
	 * @param __len The length of the array.
	 * @return The resulting array pointer.
	 * @since 2019/04/24
	 */
	public static final int jvmNewArray(int __at, int __len)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

