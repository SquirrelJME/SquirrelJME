// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This class contains the entirety of virtual memory, this includes access
 * to the various on-demand minification of suites and classes.
 *
 * @since 2019/04/21
 */
public final class VirtualMemory
	implements ReadableMemory
{
	/** The suites memory space. */
	protected final SuitesMemory suites;
	
	/**
	 * Initializes the virtual memory space.
	 *
	 * @param __sm The suite memory.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public VirtualMemory(SuitesMemory __sm)
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
		return 0x00000000;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionSize()
	{
		return 0xFFFFFFFF;
	}
}

