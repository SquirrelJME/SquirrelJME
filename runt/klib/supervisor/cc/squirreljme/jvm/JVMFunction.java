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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid class?
		if (__clid == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
		// Not instance of null class
		if (__p == 0)
			return 0;
		
		// If the object's class type is a direct match then quickly return
		int pcl = Assembly.memReadInt(__p, Constants.OBJECT_CLASS_OFFSET);
		if (pcl == __cldx)
			return 1;
		
		// Corrupted object?
		if (pcl == 0 || pcl == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid class?
		if (__cldx == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
		// Cannot load from a null string
		if (__p == 0)
			throw new VirtualMachineError();
		
		// Read length of the raw bytes
		int rawlen = Assembly.memReadShort(__p, 0) & 0xFFFF;
		
		// Load the string data into bytes
		byte[] bytes = new byte[rawlen];
		for (int i = 0, base = __p + 2; i < rawlen; i++)
			bytes[i] = (byte)Assembly.memReadByte(base, i);
		
		// Initialize and intern string
		return new String(bytes).intern();
	}
	
	/**
	 * Enters the monitor for the given object.
	 *
	 * @param __p The object to enter.
	 * @since 2019/04/26
	 */
	public static final void jvmMonitorEnter(int __p)
	{
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
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
		// Access of invalid class?
		if (__cl == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
		// Cannot allocate a null class
		if (__cl == 0)
			throw new VirtualMachineError();
		
		// Get the class information for the object to allocate
		ClassInfo info = Assembly.pointerToClassInfo(__cl);
		
		// Allocate the memory
		int rv = Allocator.allocate(info.size);
		if (rv == 0)
			throw new OutOfMemoryError();
		
		// Write class information with an initial count of one
		Assembly.memWriteInt(rv, Constants.OBJECT_CLASS_OFFSET,
			__cl);
		Assembly.memWriteInt(rv, Constants.OBJECT_COUNT_OFFSET,
			1);
		
		// Use this pointer
		return rv;
	}
	
	/**
	 * Allocates a new array.
	 *
	 * @param __at The array type.
	 * @param __len The length of the array.
	 * @return The resulting array pointer.
	 * @throws NegativeArraySizeException If an attempt is made to allocate
	 * an array of a negative size.
	 * @throws OutOfMemoryError If there is not enough memory left.
	 * @since 2019/04/24
	 */
	public static final int jvmNewArray(int __at, int __len)
		throws NegativeArraySizeException, OutOfMemoryError
	{
		// Access of invalid class?
		if (__at == Constants.BAD_MAGIC)
			throw new VirtualMachineError();
		
		// Do not initialize null class
		if (__at == 0)
			throw new VirtualMachineError();
		
		// Cannot allocate negative length
		if (__len < 0)
			throw new NegativeArraySizeException();
		
		// Get the class information for the array we want to allocate
		ClassInfo info = Assembly.pointerToClassInfo(__at);
		
		// Determine the actual size of allocation
		int allocsize = info.size + (info.cellsize * __len);
		
		// Allocate the memory
		int rv = Allocator.allocate(allocsize);
		if (rv == 0)
			throw new OutOfMemoryError();
		
		// Write class information with an initial count of one
		Assembly.memWriteInt(rv, Constants.OBJECT_CLASS_OFFSET,
			__at);
		Assembly.memWriteInt(rv, Constants.OBJECT_COUNT_OFFSET,
			1);
		
		// Write length of array
		Assembly.memWriteInt(rv, Constants.ARRAY_LENGTH_OFFSET,
			__len);
		
		// Use this pointer
		return rv;
	}
	
	/**
	 * If this is an invoked then it just throws an exception to indicate that
	 * an illegal abstract method was called.
	 *
	 * @since 2019/05/26
	 */
	public static final void jvmPureVirtualCall()
	{
		Assembly.breakpoint();
		throw new VirtualMachineError();
	}
}

