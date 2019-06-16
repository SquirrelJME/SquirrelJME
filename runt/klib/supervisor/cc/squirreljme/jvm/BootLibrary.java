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
 * This represents a single boot library.
 *
 * @since 2019/06/14
 */
public final class BootLibrary
{
	/** The offset to the jar count. */
	public static final int ROM_NUMJARS_OFFSET =
		4;
	
	/** Offset to the table of contents offset. */
	public static final int ROM_TOCOFFSET_OFFSET =
		28;
	
	/**
	 * Returns all of the libraries which are available to the bootstrap.
	 *
	 * @param __rombase The ROM base.
	 * @return The available bootstrap libraries.
	 * @since 2019/06/14
	 */
	public static final BootLibrary[] bootLibraries(int __rombase)
	{
		// Number of JARs in the ROM
		int numjars = Assembly.memReadJavaInt(__rombase, ROM_NUMJARS_OFFSET);
		
		// Offset to table of contents
		int tocoff = Assembly.memReadJavaInt(__rombase, ROM_TOCOFFSET_OFFSET);
		
		// Debug
		todo.DEBUG.code('n', 'J', numjars);
		todo.DEBUG.code('t', 'O', tocoff);
		todo.DEBUG.note("NumJars: %d, TocOff: %d", numjars, tocoff);
		
		if (true)
			throw new RuntimeException();
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

