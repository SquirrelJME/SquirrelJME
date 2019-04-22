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
		Assembly.breakpoint();
		
		return 0;
	}
	
	/**
	 * This is the booting point for the SquirrelJME kernel, it will initialize
	 * some classes and then prepare the virtual machine for proper execution.
	 *
	 * @since 2019/04/20
	 */
	private final void __start()
	{
		Assembly.memReadInt(this.memaddr, 0);
		
		Assembly.memReadInt(Assembly.objectToPointer(this), 17);
		Assembly.breakpoint();
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
		int pcl = Assembly.memReadShort(__p, 0);
		if (pcl == __cldx)
			return 1;
		
		throw new todo.TODO();
	}
}

