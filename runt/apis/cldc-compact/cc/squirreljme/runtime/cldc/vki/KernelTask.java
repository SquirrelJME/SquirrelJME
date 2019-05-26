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
 * This class manages the tasks for the kernel and such.
 *
 * @since 2019/05/04
 */
public final class KernelTask
{
	/** Offset of suite offset. */
	public static final int TOC_OFFSET_SUITE_OFFSET =
		0;
	
	/** Offset of string offset. */
	public static final int TOC_OFFSET_STRING_OFFSET =
		4;
	
	/** Size of table of contents entry. */
	public static final int TOC_ENTRY_SIZE =
		8;
	
	/** Base address of the ROM. */
	static int _rombase;
	
	/** Table of contents address. */
	static int _romtoc;
	
	/**
	 * Creates a new task.
	 *
	 * @param __classpath The class path.
	 * @param __sysprops System properties.
	 * @param __mainclass Main class.
	 * @param __mainargs Main arguments.
	 * @param __ismidlet Is this a MIDlet?
	 * @param __gd The current guest depth.
	 * @return The task ID, a pointer to the task identifier. {@code 0} will
	 * be returned if the task was not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	public static final int createTask(byte[][] __classpath,
		byte[][] __sysprops, byte[] __mainclass, byte[][] __mainargs,
		boolean __ismidlet, int __gd)
		throws NullPointerException
	{
		if (__classpath == null || __mainclass == null)
			throw new NullPointerException("NARG");
		
		// Set to default, if missing
		if (__sysprops == null)
			__sysprops = new byte[0][];
		if (__mainargs == null)
			__mainargs = new byte[0][];
		
		// Number of libraries in the classpath
		int numlib = __classpath.length;
		
		// Go through and find all the libraries
		int[] libps = new int[numlib];
		for (int i = 0; i < numlib; i++)
		{
			// Locate library
			int libp = KernelTask.findLibrary(__classpath[i]);
			
			// {@squirreljme.error ZZ3y Could not locate library.}
			if (libp == 0)
				throw new RuntimeException("ZZ3y");
			
			// Set
			libps[i] = libp;
		}
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Finds the given library.
	 *
	 * @param __lib The library to find.
	 * @return The pointer to the suite JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final int findLibrary(byte[] __lib)
		throws NullPointerException
	{
		if (__lib == null)
			throw new NullPointerException("NARG");
		
		// Needed to locate the ROM
		int rombase = KernelTask._rombase,
			romtoc = KernelTask._romtoc;
		
		// Scan through ROM
		for (int i = 0;; i += TOC_ENTRY_SIZE)
		{
			// Read Offsets
			int liboff = Assembly.memReadInt(romtoc,
					i + TOC_OFFSET_SUITE_OFFSET),
				stroff = Assembly.memReadInt(romtoc,
					i + TOC_OFFSET_STRING_OFFSET);
			
			// End of list
			if (liboff == -1 || stroff == -1)
				break;
			
			// If this string matches then return it
			if (C.utfcasecmp(rombase + stroff, __lib) == 0)
				return rombase + liboff;
		}
		
		// Not found
		return 0;
	}
}

