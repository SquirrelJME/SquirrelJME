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
 * This is the bootstrap entry point for the supervisor.
 *
 * @since 2019/05/25
 */
public final class Bootstrap
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/25
	 */
	private Bootstrap()
	{
	}
	
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
	 * @since 2019/05/25
	 */
	static final void __start(int __rambase, int __ramsize, int __bootsize,
		byte[][] __classpath, byte[][] __sysprops, byte[] __mainclass,
		byte[][] __mainargs, boolean __ismidlet, int __gd, int __rombase)
	{
		// Initialize the RAM links to setup dirty bits and initialize the
		// last block of memory with anything that remains. This makes it so
		// the RAM is actually useable.
		Allocator.__initRamLinks(__rambase, __ramsize);
		
		// Debug
		todo.DEBUG.note("Hello adorable and cute squirrels!");
		todo.DEBUG.note("Basic formatting too: %s!", "Neat!");
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

