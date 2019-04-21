// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the entirety of virtual memory, this includes access
 * to the various on-demand minification of suites and classes.
 *
 * @since 2019/04/21
 */
public final class VirtualMemory
	extends AbstractWritableMemory
{
	/** Memory map. */
	private final List<Memory> _memories =
		new ArrayList<>();
	
	/** Active cache map. */
	private volatile Memory[] _cache =
		new Memory[0];
	
	/**
	 * Maps the given region of memory.
	 *
	 * @param __mem The region to map.
	 * @param __off The region offset.
	 * @param __len Th
	 * @since 2019/04/21
	 */
	public final void mapRegion(Memory __mem)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		// Add memory
		List<Memory> memories = this._memories;
		synchronized (memories)
		{
			memories.add(__mem);
			
			// Redo cache
			this._cache = memories.<Memory>toArray(
				new Memory[memories.size()]);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memReadByte(int __addr)
	{
		// Find memory to read from
		Memory[] cache = this._cache;
		for (Memory c : cache)
		{
			int cbase = c.memRegionOffset(),
				csize = c.memRegionSize(),
				vaddr = __addr - cbase;
			
			if (vaddr >= 0 && vaddr < csize)
				return ((ReadableMemory)c).memReadByte(vaddr);
		}
		
		// Missed Read
		return 0xF0 | (__addr & 0x0F);
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
		return 0x7FFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void memWriteByte(int __addr, int __v)
	{
		// Find memory to write to
		Memory[] cache = this._cache;
		for (Memory c : cache)
		{
			int cbase = c.memRegionOffset(),
				csize = c.memRegionSize(),
				vaddr = __addr - cbase;
			
			if (vaddr >= 0 && vaddr < csize)
				((WritableMemory)c).memWriteByte(vaddr, __v);
		}
	}
}

