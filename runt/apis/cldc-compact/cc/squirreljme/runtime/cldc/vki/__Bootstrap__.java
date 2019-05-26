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

/**
 * This is the bootstrap initializer for SquirrelJME.
 *
 * @since 2019/05/04
 */
final class __Bootstrap__
{
	/**
	 * Entry point for the bootstrap.
	 *
	 * @param __rambase The base RAM address.
	 * @param __ramsize The size of RAM.
	 * @param __bootsize Boot memory size.
	 * @param __classpath The class path.
	 * @param __sysprops System properties.
	 * @param __mainclass Main class.
	 * @param __mainargs Main arguments.
	 * @param __ismidlet Is this a MIDlet?
	 * @param __gd The current guest depth.
	 * @param __rombase Base address of the ROM (for offset calculation).
	 * @param __romtoc The address of the table of contents.
	 * @since 2019/05/04
	 */
	static final void __start(int __rambase, int __ramsize, int __bootsize,
		byte[][] __classpath, byte[][] __sysprops, byte[] __mainclass,
		byte[][] __mainargs, boolean __ismidlet, int __gd, int __rombase,
		int __romtoc)
	{
		// Allocation base is set to the base of RAM
		Allocator._allocbase = __rambase;
		
		// Round the bootsize to 4 bytes, just in case!
		__bootsize = (__bootsize + 3) & (~3);
		
		// Rework the initial block base
		int nextblock = __rambase + __bootsize;
		Assembly.memWriteInt(__rambase, Allocator.OFF_MEMPART_SIZE,
			__bootsize | Allocator.PROTECTION_BITS);
		Assembly.memWriteInt(__rambase, Allocator.OFF_MEMPART_NEXT,
			nextblock);
		
		// Setup the next RAM block following the base bootstrap RAM block
		Assembly.memWriteInt(nextblock, Allocator.OFF_MEMPART_SIZE,
			(__ramsize - __bootsize) | Allocator.MEMPART_FREE_BIT |
			Allocator.PROTECTION_BITS);
		Assembly.memWriteInt(nextblock, Allocator.OFF_MEMPART_NEXT,
			0);
		
		// Store ROM TOC and such
		KernelTask._rombase = __rombase;
		KernelTask._romtoc = __romtoc;
		
		// Create initial kernel task
		int tid = KernelTask.createTask(__classpath, __sysprops, __mainclass,
			__mainargs, __ismidlet, __gd);
		if (tid == 0)
		{
			Assembly.breakpoint();
			
			// {@squirreljme.error ZZ3x Could not start the main task.}
			throw new RuntimeException("ZZ3x");
		}
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

