// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class contains the entirety of virtual memory, this includes access
 * to the various on-demand minification of suites and classes.
 *
 * @since 2019/04/21
 */
public final class VirtualMemory
	extends AbstractWritableMemory
{
	private final Map<Region, ReadableMemory> regions =
		this.SortedTreeMap
	
	/** The suites memory space. */
	protected final SuitesMemory suites;
	
	/** RAM. */
	protected final RawMemory ram;
	
	/** The size of RAM. */
	protected final int ramsize;
	
	/**
	 * Initializes the virtual memory space.
	 *
	 * @param __sm The suite memory.
	 * @param __ram The amount of RAM to be made available.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public VirtualMemory(SuitesMemory __sm, int __ram)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		
		// Initialize RAM
		int ramsize = (__ram < 1048576 ? DEFAULT_RAM_SIZE : __ram);
		this.ramsize = ramsize;
		this.ram = new RawMemory(RAM_START_ADDRESS, ramsize);
	}
	
	/**
	 * Maps the given region of memory.
	 *
	 * @param __mem The region to map.
	 * @param __off The region offset.
	 * @param __len Th
	 * @since 2019/04/21
	 */
	public final void mapRegion(ReadableMemory __mem, int __off, int __len)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memReadBytes(int __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Reading from entirely suite memory?
		SuitesMemory suites = this.suites;
		int sbas = suites.offset,
			soff = __addr - sbas;
		if (soff >= 0 && soff + __l < suites.size)
		{
			suites.memReadBytes(soff, __b, __o, __l);
			return;
		}
		
		// From RAM
		this.ram.memReadBytes(__addr - RAM_START_ADDRESS, __b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(int __addr)
	{
		// {@squirreljme.error AE0r Unaligned memory access.}
		if ((__addr & 3) != 0)
			throw new VMRuntimeException("AE0r");
		
		// Reading from suite memory?
		SuitesMemory suites = this.suites;
		int sbas = suites.offset,
			soff = __addr - sbas;
		if (soff >= 0 && soff < suites.size)
			return suites.memReadInt(soff);
		
		// Considered from the rest of RAM
		return this.ram.memReadInt(__addr - RAM_START_ADDRESS);
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
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void memWriteInt(int __addr, int __v)
	{
		// {@squirreljme.error AE0z Unaligned memory access.}
		if ((__addr & 3) != 0)
			throw new VMRuntimeException("AE0z");
		
		// To RAM
		this.ram.memWriteInt(__addr - RAM_START_ADDRESS, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void memWriteShort(int __addr, int __v)
	{
		// {@squirreljme.error AE14 Unaligned memory access.}
		if ((__addr & 1) != 0)
			throw new VMRuntimeException("AE14");
		
		// To RAM
		if (__addr >= RAM_START_ADDRESS && __addr < 0x80000000)
		{
			this.ram.memWriteShort(__addr - RAM_START_ADDRESS, __v);
			return;
		}
		
		// Use standard method
		super.memWriteShort(__addr, __v);
	}
	
	/**
	 * Represents a region within memory.
	 *
	 * @since 2019/04/21
	 */
	public static final class Region
		implements Comparable
	{
		/** The start address. */
		public final int start;
		
		/**
		 * Initializes the region.
		 *
		 * @param __s The start address.
		 * @since 2019/04/21
		 */
		public Region(int __s)
		{
			this.start = __s;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/04/21
		 */
		@Override
		public final int compareTo(Region __b)
		{
			int a = this.start,
				b = __b.start;
			
			if (a < 0)
				if (b < 0)
					return a - b;
				else
					return 1;
			else if (b < 0)
				return -1;
			else
				return a - b;
		}
	}
}

