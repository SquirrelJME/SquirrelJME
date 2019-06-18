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
	/** Last error state. */
	static volatile int _LAST_ERROR;
	
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
		if (__p == Constants.BAD_MAGIC || __v == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Can always store null values
		if (__v == 0)
			return 1;
		
		// Load the component for this array
		ClassInfo pci = Assembly.pointerToClassInfo(
			Assembly.memReadInt(__p, Constants.OBJECT_CLASS_OFFSET)).
			componentclass;
		
		// Load value class type
		int vcl = Assembly.memReadInt(__v, Constants.OBJECT_CLASS_OFFSET);
		
		// Quick exact class match?
		int pcisp = pci.selfptr;
		if (pcisp == vcl)
			return 1;
		
		// Check if the value we are putting in is an instance of the given
		// class component type
		return JVMFunction.jvmIsInstance(vcl, pcisp);
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
			Assembly.breakpoint();
		
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
		if (__p == 0 || __p == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Attempt to garbage collect object with no class or is invalid
		int pcl = Assembly.memReadInt(__p, Constants.OBJECT_CLASS_OFFSET);
		if (pcl == 0 || pcl == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Debug
		/*todo.DEBUG.code('G', 'C', __p);*/
		
		// Get class info for this type
		ClassInfo pinfo = Assembly.pointerToClassInfo(pcl);
		
		// If this is an array, elements have to be uncounted
		// Instance fields can be skipped for non-object arrays
		if ((pinfo.flags & Constants.CIF_IS_ARRAY) != 0)
		{
			/*todo.DEBUG.code('G', 'a', 1);*/
			
			// This only needs to be done for objects
			if ((pinfo.flags & Constants.CIF_IS_ARRAY_OF_OBJECTS) != 0)
			{
				/*todo.DEBUG.code('G', 'o', 1);*/
				
				// Go through all elements and uncount them
				for (int i = 0, n = Assembly.memReadInt(__p,
					Constants.ARRAY_LENGTH_OFFSET),
					bp = __p + Constants.ARRAY_BASE_SIZE,
					xp = 0; i < n; i++, xp += 4)
					Assembly.refUncount(Assembly.memReadInt(bp, xp));
			}
		}
		
		// Otherwise uncount the instance field information
		else
		{
			/*todo.DEBUG.code('G', 'o', 1);*/
			
			// Go through all classes in the class chain
			for (ClassInfo ro = pinfo; ro != null; ro = ro.superclass)
			{
				// Actual base position for the objects in the instances
				int rbase = __p + ro.base;
				
				// Clear all offsets in the object
				int rnobj = ro.numobjects;
				for (int i = 0, px = 0; i < rnobj; i++, px += 4)
					Assembly.refUncount(Assembly.memReadInt(rbase, px));
			}
		}
		
		// Free this memory
		Allocator.free(__p);
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
			Assembly.breakpoint();
		
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
			Assembly.breakpoint();
		
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
			Assembly.breakpoint();
		
		// Not instance of null class
		if (__p == 0)
			return 0;
		
		// If the object's class type is a direct match then quickly return
		int pcl = Assembly.memReadInt(__p, Constants.OBJECT_CLASS_OFFSET);
		if (pcl == __cldx)
			return 1;
		
		// Corrupted object?
		if (pcl == 0 || pcl == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Scan through super classes and check
		ClassInfo mine = Assembly.pointerToClassInfo(pcl);
		for (ClassInfo seek = mine; seek != null; seek = seek.superclass)
		{
			// Get self pointer
			int selfptr = seek.selfptr;
			
			// Make sure we are not reading bad memory
			if (selfptr == Constants.BAD_MAGIC)
			{
				Assembly.breakpoint();
				throw new VirtualMachineError();
			}
			
			// Same as this one?
			if (selfptr == __cldx)
				return 1;
			
			// See if our class or any of our super class implemented
			// interfaces matches the target class
			for (ClassInfo xface : mine.interfaceclasses)
			{
				int ifaceptr = xface.selfptr;
				
				// Make sure we are not reading bad memory
				if (ifaceptr == Constants.BAD_MAGIC)
				{
					Assembly.breakpoint();
					throw new VirtualMachineError();
				}
				
				// Is a match?
				if (ifaceptr == __cldx)
					return 1;
			}
		}
		
		// Not a match
		return 0;
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
			Assembly.breakpoint();
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads a string from memory and returns an `intern()` string value.
	 *
	 * @param __p The pointer to load the string bytes from.
	 * @return The resulting and interned string.
	 * @since 2019/05/26
	 */
	public static final String jvmLoadString(int __p)
	{
		// If a null pointer was just requested, then treat as null
		if (__p == 0)
			return null;
		
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Read length of the raw bytes
		int rawlen = Assembly.memReadJavaShort(__p, 0) & 0xFFFF;
		
		// Load the string data into bytes
		byte[] bytes = new byte[rawlen];
		for (int i = 0, base = __p + 2; i < rawlen; i++)
			bytes[i] = (byte)Assembly.memReadByte(base, i);
		
		// Initialize and intern string
		return new String(bytes).intern();
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
		// Access of invalid object?
		if (__p == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
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
			Assembly.breakpoint();
		
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
			Assembly.breakpoint();
		
		// Cannot allocate a null class
		if (__cl == 0)
			Assembly.breakpoint();
		
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
			Assembly.breakpoint();
		
		// Do not initialize null class
		if (__at == 0)
			Assembly.breakpoint();
		
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
		// Override the behavior of system calls?
		switch (__si)
		{
				// Query index, this allows system calls to be replaced
				// and implemented if the native system lacks it or there is
				// a deprecation/enhancement or otherwise
			case SystemCallIndex.QUERY_INDEX:
				switch (__a)
				{
					case SystemCallIndex.GARBAGE_COLLECT:
					case SystemCallIndex.LOAD_STRING:
						return 1;
					
						// Otherwise, check if the native system supports
					default:
						return Assembly.sysCallPV(__si, __a);
				}
				
				// Built-in system calls may set an error state
			case SystemCallIndex.ERROR_GET:
				switch (__a)
				{
						// Use last error state
					case SystemCallIndex.LOAD_STRING:
						return JVMFunction._LAST_ERROR;
					
						// Forward otherwise
					default:
						return Assembly.sysCallPV(__si, __a);
				}
			
				// Perform garbage collection
			case SystemCallIndex.GARBAGE_COLLECT:
				JVMFunction.jvmGarbageCollect();
				return 0;
				
				// Load string
			case SystemCallIndex.LOAD_STRING:
				JVMFunction._LAST_ERROR = 0;
				return Assembly.objectToPointer(
					JVMFunction.jvmLoadString(__a));
				
				// Use native handler
			default:
				return Assembly.sysCallPV(__si, __a, __b, __c, __d, __e, __f,
					__g, __h);
		}
	}
}

