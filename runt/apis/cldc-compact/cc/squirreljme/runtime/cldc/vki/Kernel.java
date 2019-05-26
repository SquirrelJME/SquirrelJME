// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.runtime.cldc.lang.ClassDataV2;

/**
 * This class is the main SquirrelJME kernel and is used to initialize and
 * manage all of the various aspects of the JVM.
 *
 * @since 2019/04/20
 */
public final class Kernel
{
	/** The offset for the object's class type. */
	public static final int OBJECT_CLASS_OFFSET =
		0;
	
	/** The offset for the object's reference count. */
	public static final int OBJECT_COUNT_OFFSET =
		4;
	
	/** Object monitor owner offset. */
	public static final int OBJECT_MONITOR_OFFSET =
		8;
	
	/** Base size for object types. */
	public static final int OBJECT_BASE_SIZE =
		12;
	
	/** The offset for array length. */
	public static final int ARRAY_LENGTH_OFFSET =
		12;
	
	/** The base size for arrays. */
	public static final int ARRAY_BASE_SIZE =
		16;
	
	/** Offset in static field space for the kernel. */
	public static final int SF_KERNEL_OFFSET =
		0;
	
	/** The address of the ROM file containing definitions and code. */
	public int romaddr;
	
	/** The address of the kernel mini class. */
	public int kernaddr;
	
	/** Kernel object base. */
	public int kobjbase;
	
	/** Static memory size. */
	public int staticmemsize;
	
	/** The starting address of free memory. */
	public int memaddr;
	
	/** The amount of memory that is available for the VM to use. */
	public int memsize;
	
	/** Is a MIDlet being ran? */
	public int ismidlet;
	
	/** The guest depth. */
	public int guestdepth;
	
	/** The current classpath, of byte arrays. */
	public byte[][] classpath;
	
	/** The current system properties, of byte arrays. */
	public byte[][] sysprops;
	
	/** The main class to execute. */
	public byte[] mainclass;
	
	/** Main entry arguments. */
	public byte[][] mainargs;
	
	/** Static field space. */
	public int sfspace;
	
	/** Static field pointer. */
	public int sfptr;
	
	/** Current static field size. */
	public int sfcursize;
	
	/** Class table size. */
	public int ctcount;
	
	/** Current class table size. */
	public int ctcurcount;
	
	/** Allocation base address. */
	public int allocbase;
	
	/** Did the kernel initialize? */
	private boolean _kdidinit;
	
	/**
	 * Not used.
	 *
	 * @since 2019/04/20
	 */
	private Kernel()
	{
	}
	
	/**
	 * Creates a new task.
	 *
	 * @param __ismid Is it a midlet?
	 * @param __gd The guest depth.
	 * @param __cp The classpath.
	 * @param __sprops System properties.
	 * @param __maincl Main class.
	 * @param __mainargs Main arguments.
	 * @throws NullPointerException On null arguments.
	 * @return The newly created task.
	 * @since 2019/04/27
	 */
	public final int taskNew(boolean __ismid, int __gd, byte[][] __cp,
		byte[][] __sprops, byte[] __maincl, byte[][] __mainargs)
		throws NullPointerException
	{
		if (__cp == null || __sprops == null || __maincl == null ||
			__mainargs == null)
			throw new NullPointerException("NARG");
		
		// The number of libraries that will be active
		int libcount = __cp.length;
		
		// Need to search through the ROM suites for the suites to use
		int romaddr = this.romaddr;
		int[] cpsubroms = new int[libcount];
		for (int i = 0; i < libcount; i++)
		{
			// Which suite do we want?
			byte[] wantlib = __cp[i];
			int wln = wantlib.length;
			
			// Scan through all suites
			int userom = -1;
			for (int r = 0;; r++)
			{
				// ROM relative address
				int rreladdr = Assembly.memReadInt(romaddr, r * 4);
				
				// No more ROMs to read
				if (rreladdr == 0xFFFFFFFF)
					break;
				
				// Absolute address of the ROM
				int rabsaddr = romaddr + rreladdr;
				
				// Read address of the relative library name offset
				int rellibname = Assembly.memReadInt(rabsaddr, 0),
					abslibname = rabsaddr + rellibname;
				
				// Perform a name match to see if this is the one
				int q;
				for (q = 0; q < wln; q++)
					if (wantlib[q] != Assembly.memReadByte(abslibname, q))
						break;
				
				// Is this a match???
				if (q == wln)
				{
					userom = rabsaddr;
					Assembly.breakpoint();
					break;
				}
			}
			
			// {@squirreljme.error ZZ3w Could not locate suite in ROM.}
			if (userom < 0)
				throw new VirtualMachineError("ZZ3w");
			
			// Use this
			cpsubroms[i] = userom;
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * This is the booting point for the SquirrelJME kernel, it will initialize
	 * some classes and then prepare the virtual machine for proper execution.
	 *
	 * @since 2019/04/20
	 */
	private final void __start()
	{
		// Currently all of the memory exists as a bunch of bytes of nothing
		// with no structure. So this will initialize the region of memory into
		// a single gigantic partition.
		int memaddr = this.memaddr,
			staticmemsize = this.staticmemsize,
			allocbase = (memaddr + staticmemsize + 3) & (~3);
		this.allocbase = allocbase;
		
		// The actual size of memory that can be used, cut off from the static
		// memory size which just contains the config properties and the
		// kernel object itself. Make sure it is always rounded down to
		// 4 bytes because that would really mess with things in the
		// allocator
		int allocmemsize = (this.memsize - staticmemsize) & (~3);
		
		// Write memory size at this position, the highest bit indicates
		// that it is free memory
		Assembly.memWriteInt(allocbase, Allocator.OFF_MEMPART_SIZE,
			allocmemsize | Allocator.MEMPART_FREE_BIT);
		
		// This is the next chunk in memory, zero means that there is no
		// remaining chunk (at end of memory)
		Assembly.memWriteInt(allocbase, Allocator.OFF_MEMPART_NEXT, 0);
		
		// Now that we have some kind of memory, the static field space can
		// be initialized. Make sure it is a minimum size
		int sfspace = this.sfspace;
		if (sfspace <= 0)
			sfspace = DefaultConfiguration.DEFAULT_STATIC_FIELD_SIZE;
		else if (sfspace < DefaultConfiguration.MINIMUM_STATIC_FIELD_SIZE)
			sfspace = DefaultConfiguration.MINIMUM_STATIC_FIELD_SIZE;
		this.sfspace = sfspace;
		int sfptr = Allocator.allocate(sfspace);
		
		// If this is zero then allocation has failed
		if (sfptr == 0)
		{
			Assembly.breakpoint();
			return;
		}
		
		// Store static field pointer
		this.sfptr = sfptr;
		
		// Set static field pointer since everything using static fields will
		// now use this information
		Assembly.specialSetStaticFieldRegister(sfptr);
		
		// Write the kernel object so we can call back into it whenever it is
		// needed, by any system call or otherwise
		Assembly.memWriteInt(sfptr, SF_KERNEL_OFFSET,
			Assembly.objectToPointer(this));
		
		// Space remaining in the static field area
		this.sfcursize = sfspace - 4;
		
		// Setup main task now, which does class initialization and such per
		// task because it is different for every running thing
		int maintask = this.taskNew((this.ismidlet == 0 ? false : true),
			this.guestdepth, this.classpath, this.sysprops, this.mainclass,
			this.mainargs);
		
		// Could not create main task
		if (maintask == 0)
		{
			Assembly.breakpoint();
			return;
		}
		
		// Break
		Assembly.breakpoint();
		throw new todo.TODO();
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
		// Cannot store into null array
		if (__p == 0)
			return 0;
		
		// Null is always valid
		if (__v == 0)
			return 1;
			
		Assembly.breakpoint();
		throw new todo.TODO();
		/*
		// Anything being stored in an object array is valid
		int pcl = Assembly.memReadInt(__p, OBJECT_CLASS_OFFSET);
		if (pcl == FixedClassIDs.OBJECT_ARRAY)
			return 1;
			
		// Get the component types of both
		int ccl = Kernel.jvmComponentType(pcl),
			vcl = Assembly.memReadInt(__v, OBJECT_CLASS_OFFSET);
		
		// Same class type for storage?
		if (ccl == vcl)
			return 1;
			
		// Need to go through and check a bunch of things
		Assembly.breakpoint();
		throw new todo.TODO();
		*/
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
		// Null object has no component type
		if (__clid == 0)
			return 0;
		
		Assembly.breakpoint();
		throw new todo.TODO();
		/*
		// Fixed array types?
		switch (__clid)
		{
			case FixedClassIDs.PRIMITIVE_BOOLEAN_ARRAY:
				return FixedClassIDs.PRIMITIVE_BOOLEAN;
			case FixedClassIDs.PRIMITIVE_BYTE_ARRAY:
				return FixedClassIDs.PRIMITIVE_BYTE;
			case FixedClassIDs.PRIMITIVE_BYTE_ARRAY_ARRAY:
				return FixedClassIDs.PRIMITIVE_BYTE_ARRAY;
			case FixedClassIDs.PRIMITIVE_SHORT_ARRAY:
				return FixedClassIDs.PRIMITIVE_SHORT;
			case FixedClassIDs.PRIMITIVE_CHARACTER_ARRAY:
				return FixedClassIDs.PRIMITIVE_CHARACTER;
			case FixedClassIDs.PRIMITIVE_INTEGER_ARRAY:
				return FixedClassIDs.PRIMITIVE_INTEGER;
			case FixedClassIDs.PRIMITIVE_LONG_ARRAY:
				return FixedClassIDs.PRIMITIVE_LONG;
			case FixedClassIDs.PRIMITIVE_FLOAT_ARRAY:
				return FixedClassIDs.PRIMITIVE_FLOAT;
			case FixedClassIDs.PRIMITIVE_DOUBLE_ARRAY:
				return FixedClassIDs.PRIMITIVE_DOUBLE;
			case FixedClassIDs.OBJECT_ARRAY:
				return FixedClassIDs.OBJECT;
			case FixedClassIDs.STRING_ARRAY:
				return FixedClassIDs.STRING;
		}
			
		// Need to go through and check a bunch of things
		Assembly.breakpoint();
		throw new todo.TODO();
		*/
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
		// {@squirreljme.error ZZ40 Cannot garbage collect a null pointer.}
		if (__p == 0)
			throw new VirtualMachineError("ZZ40");
		
		// Get the ClassDataV2 for this object
		ClassDataV2 cd = Assembly.pointerToObjectClassDataV2(
			Assembly.memReadInt(__p, OBJECT_CLASS_OFFSET));
		
		// {@squirreljme.error ZZ4e Attempt to garbage collect an object
		// which has been corrupted.}
		if (cd.magic != ClassDataV2.MAGIC_NUMBER)
			throw new VirtualMachineError("ZZ4e");
		
		// {@squirreljme.error ZZ41 Cannot garbage collect an object which
		// is locked by a thread.}
		int lock = Assembly.memReadInt(__p, OBJECT_MONITOR_OFFSET);
		if (lock != 0)
			throw new VirtualMachineError("ZZ41");
		
		// Go through the object and recursively uncount any used references
		if (false)
		{
			Assembly.breakpoint();
			throw new todo.TODO();
		}
		
		// Free object in the chain
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
		if (__p == 0)
			return 0;
		
		// Get the ClassDataV2 for this object
		ClassDataV2 cd = Assembly.pointerToObjectClassDataV2(
			Assembly.memReadInt(__p, OBJECT_CLASS_OFFSET));
		
		// {@squirreljme.error ZZ4a Corrupt object when checking if it is an
		// array.}
		if (cd.magic != ClassDataV2.MAGIC_NUMBER)
			throw new VirtualMachineError("ZZ4a");
		
		// Is considered an array if it has more than zero dimensions
		return (cd.dimensions > 0 ? 1 : 0);
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
		if (__p == 0)
			return 0;
		
		// Recursively go up the class chain
		int at = Assembly.memReadInt(__p, OBJECT_CLASS_OFFSET);
		while (at != 0)
		{
			// Is a match?
			if (at == __cldx)
				return 1;
			
			// Need the data
			ClassDataV2 d = Assembly.pointerToObjectClassDataV2(at);
			
			// {@squirreljme.error ZZ4b Corrupt object when checking if it is
			// an instance of a given object.}
			if (d.magic != ClassDataV2.MAGIC_NUMBER)
				throw new VirtualMachineError("ZZ4b");
			
			// Go up to super class
			at = d.superclass;
		}
		
		// Not an instance
		return 0;
	}
	
	/**
	 * Enters the monitor for the given object.
	 *
	 * @param __p The object to enter.
	 * @since 2019/04/26
	 */
	public static final void jvmMonitorEnter(int __p)
	{
		// Do nothing if the current thread is not set
		int curthread = Assembly.specialGetThreadRegister();
		if (curthread == 0)
			return;
		
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
		// Do nothing if the current thread is not set
		int curthread = Assembly.specialGetThreadRegister();
		if (curthread == 0)
			return;
		
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
		// {@squirreljme.error ZZ3u Cannot allocate an array which is of a
		// negative length.}
		if (__len < 0)
			throw new NegativeArraySizeException("ZZ3u");
		
		// {@squirreljme.error ZZ4c Attempt to allocate an array from a
		// corrupted class type.}
		ClassDataV2 data = Assembly.pointerToObjectClassDataV2(__at);
		if (data.magic != ClassDataV2.MAGIC_NUMBER)
			throw new VirtualMachineError("ZZ4c");
		
		// {@squirreljme.error ZZ4d Attempt to allocate an array of a type
		// which is not an array.}
		if (data.dimensions <= 0)
			throw new VirtualMachineError("ZZ4d");
		
		// Determine allocation size
		int allocsize = data.size + (__len * data.cellsize);
		
		// Allocate memory
		int rv = Allocator.allocate(allocsize);
		if (rv == 0)
		{
			// Perform garbage collection
			Kernel.jvmGarbageCollect();
			
			// Try to allocate again, fail completely if this fails
			rv = Allocator.allocate(allocsize);
			if (rv == 0)
				throw new OutOfMemoryError();
		}
		
		// Class type, initial count, and length
		Assembly.memWriteInt(rv, OBJECT_CLASS_OFFSET, __at);
		Assembly.memWriteInt(rv, OBJECT_COUNT_OFFSET, 1);
		Assembly.memWriteInt(rv, ARRAY_LENGTH_OFFSET, __len);
		
		// Use this array
		return rv;
	}
}

