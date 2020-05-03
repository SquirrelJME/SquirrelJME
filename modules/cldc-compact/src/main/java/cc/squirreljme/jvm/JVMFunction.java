// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.jvm.boot.SystemBoot;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

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
	public static int jvmCanArrayStore(int __p, int __v)
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
	public static int jvmComponentType(int __clid)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Enters the static main method of the given class.
	 *
	 * @param __ci The class info to enter,
	 * @param __args The arguments to the main entry call.
	 * @since 2020/03/21
	 */
	public static void jvmEnterMain(ClassInfo __ci, String[] __args)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Performs aggressive garbage collection of the JVM heap to free as much
	 * memory as possible.
	 *
	 * @see Reference
	 * @since 2019/04/25
	 */
	public static void jvmGarbageCollect()
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
	public static void jvmGarbageCollectObject(int __p)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the given class.
	 *
	 * @param __ns The noted string.
	 * @return The loaded class info.
	 * @since 2019/12/15
	 */
	public static ClassInfo jvmInitClass(int __ns)
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
	public static long jvmInterfacePointer(int __p, int __icl, int __mdx)
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
	public static int jvmIsArray(int __p)
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
	public static int jvmIsInstance(int __p, int __cldx)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the {@link Class} instance for the given class info pointer,
	 * if none has been created yet then it will be created as needed.
	 *
	 * @param <T> The class type.
	 * @param __cldx The class index pointer.
	 * @return The resulting class.
	 * @since 2019/05/26
	 */
	public static <T> Class<T> jvmLoadClass(ClassInfo __cldx)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads a string from memory and returns an `intern()` string value.
	 *
	 * @param __p The pointer to load the string bytes from.
	 * @return The resulting string.
	 * @since 2019/05/26
	 */
	@Deprecated
	public static String jvmLoadString(int __p)
	{
		// Zero will be the null pointer
		if (__p == 0)
			return null;
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Enters the monitor for the given object.
	 *
	 * @param __p The object to enter.
	 * @since 2019/04/26
	 */
	public static void jvmMonitorEnter(int __p)
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
	public static void jvmMonitorExit(int __p)
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
	public static int jvmNew(int __cl)
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
	public static int jvmNewArray(int __at, int __len)
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
	public static long jvmSystemCall(short __si, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		// Depends on the system call being performed
		switch (__si)
		{
				// Query for support?
			case SystemCallIndex.QUERY_INDEX:
				switch (__a)
				{
					default:
						break;
				}
				break;
		}
		
		// If this is the supervisor then some calls should just be forwarded
		// to the task handler which could do things on behalf of the
		// supervisor if not already handled above. This will ensure that
		// the supervisor and tasks will have the same handling of system
		// calls in general cases.
		if (SystemCall.currentTaskId() == 0)
			return JVMFunction.jvmSystemCallByTask(0, __si,
				__a, __b, __c, __d, __e, __f, __g, __h);
		
		// Call pure form as nothing is handled by this handler.
		return Assembly.sysCallPVL(__si,
			__a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * This handles system calls which were made in behalf of a task, this
	 * executes from within the supervisor.
	 *
	 * @param __taskId The task that executed the system call.
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
	 * @since 2020/05/03
	 */
	public static long jvmSystemCallByTask(int __taskId, short __si,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g, int __h)
	{
		// Depends on the system call type
		switch (__si)
		{
				// Query supported system calls
			case SystemCallIndex.QUERY_INDEX:
				switch (__a)
				{
					case SystemCallIndex.CONFIG_GET_VALUE:
					case SystemCallIndex.CONFIG_GET_TYPE:
						return 1;
					
					default:
						break;
				}
				break;
				
				// Get configuration key value
			case SystemCallIndex.CONFIG_GET_VALUE:
				return SystemBoot.config().rawValue(__a);
			
				// Get configuration key type
			case SystemCallIndex.CONFIG_GET_TYPE:
				return SystemBoot.config().type(__a);
		}
		
		// Call pure form as the supervisor has no way to handle this system
		// call either.
		return Assembly.sysCallPVL(__si,
			__a, __b, __c, __d, __e, __f, __g, __h);
	}
}

