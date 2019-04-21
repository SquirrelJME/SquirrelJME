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
	/** The suite manage to base from. */
	protected final VMSuiteManager suites;
	
	/**
	 * Initializes the suites memory.
	 *
	 * @param __sm The suite manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public SuitesMemory(VMSuiteManager __sm)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memRegionOffset()
	{
		return 0x80000000;
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

