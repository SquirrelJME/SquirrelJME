// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This class is the main SquirrelJME kernel and is used to initialize and
 * manage all of the various aspects of the JVM.
 *
 * @since 2019/04/20
 */
public final class Kernel
{
	/** Offset to size of memory partition. */
	public static final int OFF_MEMPART_SIZE =
		0;
	
	/** Bit to indicate memory partition is free. */
	public static final int MEMPART_FREE_BIT =
		0x80000000;
	
	/** Offset to next link in memory partition. */
	public static final int OFF_MEMPART_NEXT =
		4;
	
	/** The offset for the object's class type. */
	public static final int OBJECT_CLASS_OFFSET =
		0;
	
	/** The offset for the object's reference count. */
	public static final int OBJECT_COUNT_OFFSET =
		2;
	
	/** The offset for array length. */
	public static final int ARRAY_LENGTH_OFFSET =
		4;
	
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
	
	/** The current classpath, NUL split. */
	public byte[] classpath;
	
	/** The current system properties, key NUL value NUL. */
	public byte[] sysprops;
	
	/** The main class to execute. */
	public byte[] mainclass;
	
	/** Main entry arguments. */
	public byte[] mainargs;
	
	/** Static field space. */
	public int sfspace;
	
	/** Static field pointer. */
	public int sfptr;
	
	/** Allocation base address. */
	public int allocbase;
	
	/**
	 * Not used.
	 *
	 * @since 2019/04/20
	 */
	private Kernel()
	{
	}
	
	/**
	 * Allocates a space within memory of the given size and then returns
	 * it.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The allocated object or {@code 0} if allocation has failed.
	 * @since 2019/04/22
	 */
	public final int kernelNew(int __sz)
	{
		// Cannot allocate zero bytes
		if (__sz == 0)
			return 0;
		
		// This is the seeker which scans through the memory links to find
		// free space somewhere
		int seeker = this.allocbase;
		
		// Round allocations to nearest 4 bytes since the VM expects this
		// alignment be used
		__sz = (__sz + 3) & (~3);
		
		// We will be going through every chain
		for (;;)
		{
			// If the seeker ever ends up at the null pointer then we just
			// ran off the end of the chain
			if (seeker == 0)
				return 0;
			
			// Read size and next address
			int size = Assembly.memReadInt(seeker, OFF_MEMPART_SIZE),
				next = Assembly.memReadInt(seeker, OFF_MEMPART_NEXT);
				
			// This region of memory is free for use
			if ((size & MEMPART_FREE_BIT) != 0)
			{
				// Determine the actual size available by clipping the bit
				// of and then. The block size is the size of this region
				// with the partition info
				int blocksize = (size ^ MEMPART_FREE_BIT),
					actsize = blocksize - 8;
				
				// There is enough space to use this partition
				if (__sz <= actsize)
				{
					// The return pointer is the region start address
					int rv = seeker + 8;
					
					// This is the new block size, if it does not match the
					// current block size then we are not using an entire
					// block (if it does match then we just claimed all the
					// free space here).
					int newblocksize = (__sz + 8);
					if (blocksize != newblocksize)
					{
						// This is the address of the next block
						int nextseeker = seeker + newblocksize;
						
						// The size of this block is free and it has the
						// remaining size of the current block's old size
						// minute the new block size
						Assembly.memWriteInt(nextseeker, OFF_MEMPART_SIZE,
							(blocksize - newblocksize) | MEMPART_FREE_BIT);
						
						// The next link of the next block because our
						// current link (since it is a linked list)
						Assembly.memWriteInt(nextseeker, OFF_MEMPART_NEXT,
							next);
						
						// The next link of our current block (the one we
						// are claiming)
						Assembly.memWriteInt(seeker, OFF_MEMPART_NEXT,
							nextseeker);
					}
					
					// Clear the memory here since it is expected that
					// everything in Java has been initialized to zero, this
					// is also much safer than C's malloc().
					for (int i = 0; i < __sz; i += 4)
						Assembly.memWriteInt(rv, i, 0);
					
					// Return pointer
					return rv;
				}
			}
			
			// If this point was reached, we need to try the next link
			seeker = next;
		}
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
		int allocbase = (this.memaddr + this.staticmemsize + 3) & (~3);
		this.allocbase = allocbase;
		
		// The actual size of memory that can be used, cut off from the static
		// memory size which just contains the config properties and the
		// kernel object itself. Make sure it is always rounded down to
		// 4 bytes because that would really mess with things in the
		// allocator
		int memsize = (this.memsize - this.staticmemsize) & (~3);
		
		// Write memory size at this position, the highest bit indicates
		// that it is free memory
		Assembly.memWriteInt(allocbase, OFF_MEMPART_SIZE,
			memsize | MEMPART_FREE_BIT);
		
		// This is the next chunk in memory, zero means that there is no
		// remaining chunk (at end of memory)
		Assembly.memWriteInt(allocbase, OFF_MEMPART_NEXT, 0);
		
		// Now that we have some kind of memory, the static field space can
		// be initialized. Make sure it is a minimum size
		int sfspace = this.sfspace;
		if (sfspace < DefaultConfiguration.MINIMUM_STATIC_FIELD_SIZE)
			sfspace = DefaultConfiguration.MINIMUM_STATIC_FIELD_SIZE;
		int sfptr = this.kernelNew(sfspace);
		
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
		
		// Current write address in the field space and the space remaining
		Assembly.memWriteInt(sfptr, 0, sfptr + 16);
		Assembly.memWriteInt(sfptr, 4, sfspace - 16);
		
		// Write the kernel object so we can call back into it whenever it is
		// needed, by any system call or otherwise
		Assembly.memWriteInt(sfptr, 8, Assembly.objectToPointer(this));
		
		// Break
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
		if (__p == 0)
			return 0;
		
		// If the class exactly matches the given type then no further
		// checking is needed
		int pcl = Assembly.memReadShort(__p, OBJECT_CLASS_OFFSET);
		if (pcl == __cldx)
			return 1;
		
		// Need to go through and check a bunch of things
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
		
		// Determine the allocation size according to the type
		int cellsize;
		switch (__at)
		{
				// Boolean, Byte
			case FixedClassIDs.PRIMITIVE_BOOLEAN_ARRAY:
			case FixedClassIDs.PRIMITIVE_BYTE_ARRAY:
				cellsize = 1;
				break;
				
				// Short, Character
			case FixedClassIDs.PRIMITIVE_SHORT_ARRAY:
			case FixedClassIDs.PRIMITIVE_CHARACTER_ARRAY:
				cellsize = 2;
				break;
				
				// Integer, Float
			case FixedClassIDs.PRIMITIVE_INTEGER_ARRAY:
			case FixedClassIDs.PRIMITIVE_FLOAT_ARRAY:
				cellsize = 4;
				break;
				
				// Long, Double
			case FixedClassIDs.PRIMITIVE_LONG_ARRAY:
			case FixedClassIDs.PRIMITIVE_DOUBLE_ARRAY:
				cellsize = 8;
				break;
				
				// Non-default type
			default:
				if (true)
				{
					Assembly.breakpoint();
					throw new todo.TODO();
				}
				break;
		}
		
		// Determine the allocation size
		int allocsize = 8 + (cellsize * __len);
		
		// {@squirreljme.error ZZ3v Not enough memory to allocate array.}
		int rv = ((Kernel)Assembly.pointerToObject(
			Assembly.memReadInt(Assembly.specialGetStaticFieldRegister(), 0))).
			kernelNew(allocsize);
		if (rv == 0)
			throw new OutOfMemoryError("ZZ3v");
		
		// Class type, initial count, and length
		Assembly.memWriteShort(__at, OBJECT_CLASS_OFFSET, __at);
		Assembly.memWriteShort(__at, OBJECT_COUNT_OFFSET, 1);
		Assembly.memWriteInt(__at, ARRAY_LENGTH_OFFSET, __len);
		
		// Return the array
		return rv;
	}
}

