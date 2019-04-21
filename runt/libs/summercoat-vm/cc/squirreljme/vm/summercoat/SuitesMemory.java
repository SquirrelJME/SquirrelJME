// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;

/**
 * This class contains the memory information for every single suite which
 * exists within the VM.
 *
 * @since 2019/04/21
 */
public final class SuitesMemory
	implements ReadableMemory
{
	/** The suite chunk size. */
	public static final int SUITE_CHUNK_SIZE =
		4194304;
	
	/** The address where suite chunks start. */
	public static final int SUITE_CHUNK_START =
		0x8000;
	
	/** The suite manage to base from. */
	protected final VMSuiteManager suites;
	
	/** Offset. */
	protected final int offset;
	
	/** The size of this memory region. */
	protected final int size;
	
	/** The individual regions of suite memory. */
	private final SuiteMemory[] _suitemem;
	
	/**
	 * Initializes the suites memory.
	 *
	 * @param __off The offset of suite memory.
	 * @param __sm The suite manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public SuitesMemory(int __off, VMSuiteManager __sm)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		// Set suites
		this.suites = __sm;
		
		// All the libraries which are available for usage
		String[] libnames = __sm.listLibraryNames();
		
		// Setup suite memory area
		int n = libnames.length;
		SuiteMemory[] suitemem = new SuiteMemory[n];
		
		// Setup memory regions for the various suites
		int off = SUITE_CHUNK_START;
		for (int i = 0; i < n; i++, off += SUITE_CHUNK_SIZE)
		{
			// Set
			suitemem[i] = new SuiteMemory(off, __sm, libnames[i]);
			
			// Debug
			todo.DEBUG.note("MMap Suite %s -> %08x", libnames[i],
				__off + off);
		}
		
		// Store all the various suite memories
		this._suitemem = suitemem;
		
		// Store final memory parameters
		this.offset = __off;
		this.size = off;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memRegionOffset()
	{
		return this.offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionSize()
	{
		return 0x7FFFFFFF;
	}
}

