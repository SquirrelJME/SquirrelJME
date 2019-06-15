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
	 * @param __rombase Base address of the ROM (for offset calculation).
	 * @param __romsize The size of ROM.
	 * @param __confbase The configuration memory base.
	 * @param __confsize The configuration memory size.
	 * @since 2019/05/25
	 */
	static final void __start(int __rambase, int __ramsize,
		int __rombase, int __romsize, int __confbase, int __confsize)
	{
		// Initialize the RAM links to setup dirty bits and initialize the
		// last block of memory with anything that remains. This makes it so
		// the RAM is actually useable.
		Allocator.__initRamLinks(__rambase, __ramsize);
		
		String qx = "Hello?";
		String qy = "Love you!";
		String qz = "Is this working?";
		todo.DEBUG.note("%s %s %s", qx, qy, qz);
		Assembly.breakpoint();
		
		// Load boot libraries that are available
		BootLibrary[] bootlibs = BootLibrary.bootLibraries(__rombase);
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

