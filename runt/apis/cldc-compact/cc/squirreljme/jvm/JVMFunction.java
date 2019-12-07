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
	 * @return The pointer to the code to be invoked in the low-word and the
	 * pool of the target class in the high-word.
	 * @since 2019/04/30
	 */
	public static final long jvmInterfacePointer(int __p, int __icl, int __mdx)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
		// Assembly.longPack(hi, lo)
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
	public static final Class<?> jvmLoadClass(int __cldx)
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
	public static final String jvmLoadString(int __p)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Reads a long value from the given address
	 *
	 * @param __addr The address to access.
	 * @param __off The address offset.
	 * @return The read value.
	 * @since 2019/05/29
	 */
	public static final long jvmMemReadLong(int __addr, int __off)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Writes a long value to the given address
	 *
	 * @param __addr The address to access.
	 * @param __off The address offset.
	 * @param __hv The high value.
	 * @param __lv The low value.
	 * @since 2019/05/29
	 */
	public static final void jvmMemWriteLong(int __addr, int __off, int __hv,
		int __lv)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Writes a long value to the given address
	 *
	 * @param __addr The address to access.
	 * @param __off The address offset.
	 * @param __v The value
	 * @since 2019/05/29
	 */
	public static final void jvmMemWriteLong(int __addr, int __off, long __v)
	{
		JVMFunction.jvmMemWriteLong(__addr, __off,
			(int)(__v >>> 32), (int)__v);
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
	 * @throws OutOfMemoryError If there is not enough memory to allocate the
	 * class.
	 * @since 2019/05/24
	 */
	public static final int jvmNew(int __cl)
		throws OutOfMemoryError
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
	 * @throws NegativeArraySizeException If the array length is negative.
	 * @throws OutOfMemoryError If there is not enough memory to allocate the
	 * array.
	 * @since 2019/04/24
	 */
	public static final int jvmNewArray(int __at, int __len)
		throws NegativeArraySizeException, OutOfMemoryError
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * This handles an unpure system call which may modify the behavior of
	 * any system call which needs to be done. Some system calls might not be
	 * supported by the host machine or they might not make sense (such as
	 * garbage collection), so as such this allows their behavior to modified.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result.
	 * @since 2019/05/27
	 */
	public static final int jvmSystemCall(short __si, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		// Call pure form
		return Assembly.sysCallPV(__si, __a, __b, __c, __d, __e, __f, __g,
			__h);
	}
}

