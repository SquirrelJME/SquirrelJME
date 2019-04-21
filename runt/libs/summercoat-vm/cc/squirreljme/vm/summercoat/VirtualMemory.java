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
	extends AbstractWritableMemory
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
		
		// Some other region
		super.memReadBytes(__addr, __b, __o, __l);
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
		
		// Debug
		todo.DEBUG.note("Read %08x", __addr);
		
		// Reading from suite memory?
		SuitesMemory suites = this.suites;
		int sbas = suites.offset,
			soff = __addr - sbas;
		if (soff >= 0 && soff < suites.size)
			return suites.memReadInt(soff);
		
		throw new todo.TODO();
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

