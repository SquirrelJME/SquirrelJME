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
 * This represents the single virtual memory space for suite memory.
 *
 * @since 2019/04/21
 */
public final class SuiteMemory
	implements ReadableMemory
{
	/** The suite manager. */
	protected final VMSuiteManager suites;
	
	/** The library this uses. */
	protected final String libname;
	
	/** The offset to this memory region. */
	protected final int offset;
	
	/**
	 * Initializes the suite memory.
	 *
	 * @param __off The memory offset for this suite.
	 * @param __sm The suite manager.
	 * @param __ln The library name.
	 * @throws NullPointerException On null arguments.
	 */
	public SuiteMemory(int __off, VMSuiteManager __sm, String __ln)
		throws NullPointerException
	{
		if (__sm == null || __ln == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		this.libname = __ln;
		this.offset = __off;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(int __addr)
	{
		throw new todo.TODO();
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
		return SuitesMemory.SUITE_CHUNK_SIZE;
	}
}

