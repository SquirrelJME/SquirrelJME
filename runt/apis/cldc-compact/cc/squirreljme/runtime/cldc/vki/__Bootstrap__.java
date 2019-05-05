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
	 * @since 2019/05/04
	 */
	static final void __start(int __rambase, int __ramsize, int __bootsize,
		byte[][] __classpath, byte[][] __sysprops, byte[] __mainclass,
		byte[][] __mainargs, boolean __ismidlet, int __gd)
	{
		// Allocation base is set to the base of RAM
		Allocator._allocbase = __rambase;
		
		// Setup the next RAM block following the base bootstrap RAM block
		int nextblock = __rambase + __bootsize;
		Assembly.memWriteInt(nextblock, Allocator.OFF_MEMPART_SIZE,
			(__ramsize - __bootsize) | Allocator.MEMPART_FREE_BIT);
		Assembly.memWriteInt(nextblock, Allocator.OFF_MEMPART_NEXT,
			0);
		
		// Create initial kernel task
		int tid = KernelTask.createTask(__classpath, __sysprops, __mainclass,
			__mainargs, __ismidlet, __gd);
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

