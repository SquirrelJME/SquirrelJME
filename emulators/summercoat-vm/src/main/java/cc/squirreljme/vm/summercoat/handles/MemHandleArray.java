// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat.handles;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.summercoat.MemHandle;

/**
 * A memory handle that is an array.
 *
 * @since 2021/01/17
 */
public abstract class MemHandleArray
	extends MemHandle
{
	/** The length of the array. */
	public final int length;
	
	/** The size of each cell in the array. */
	protected final int cellSize;
	
	/**
	 * Initializes a new handle.
	 *
	 * @param __id The identifier for this handle.
	 * @param __kind The kind of memory handle to allocate.
	 * @param __base The array base
	 * @param __cellSize The size of each cell in the array.
	 * @param __len The array length.
	 * @throws IllegalArgumentException If the kind is not valid or the
	 * requested size is negative.
	 * @since 2021/01/17
	 */
	public MemHandleArray(int __id, int __kind, int __base, int __cellSize,
		int __len)
		throws IllegalArgumentException
	{
		super(__id, __kind, __base + (__cellSize * __len), __base);
		
		if (__len < 0)
			throw new IllegalArgumentException("Negative length: " + __len);
		
		this.length = __len;
		this.cellSize = __cellSize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	protected void specialWriteBytes(int __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Check if our cell is valid or not
		int relAddr = __addr - super.rawSize;
		int cellSize = this.cellSize;
		if ((relAddr % cellSize) != 0)
			throw new VMException(String.format("Invalid cell read %d @ %#x",
				cellSize, relAddr));
		
		// Are we not reading full cells?
		if ((__l % cellSize) != 0)
			throw new VMException(String.format("Invalid cell read %d / %d",
				cellSize, __l));
		
		// Determine the cell we are in
		int inCell = relAddr / cellSize;
		if (inCell < 0 || inCell >= this.length)
			throw new VMException("Out of bounds: " + inCell);
		
		// Depending on the cell size, forward accordingly
		int numCells = __l / cellSize;
		for (int cell = 0, addr = __addr, off = __o;
			cell < numCells; cell++, addr += cellSize, off += cellSize)
			switch (cellSize)
			{
				case 1:
					this.memWriteByte(addr,
						__b[off]);
					break;
					
				case 2:
					this.memWriteShort(addr,
						((__b[off] & 0xFF) << 8) |
						(__b[off + 1] & 0xFF));
					break;
					
				case 4:
					this.memWriteInt(addr,
						((__b[off] & 0xFF) << 24) |
						((__b[off + 1] & 0xFF) << 16) |
						((__b[off + 2] & 0xFF) << 8) |
						(__b[off + 3] & 0xFF));
					break;
					
				case 8:
					this.memWriteLong(addr,
						((__b[off] & 0xFFL) << 56) |
						((__b[off + 1] & 0xFFL) << 48) |
						((__b[off + 2] & 0xFFL) << 40) |
						((__b[off + 3] & 0xFFL) << 32) |
						((__b[off + 4] & 0xFFL) << 24) |
						((__b[off + 5] & 0xFF) << 16) |
						((__b[off + 6] & 0xFF) << 8) |
						(__b[off + 7] & 0xFF));
					break;
				
				default:
					throw Debugging.oops();
			}
	}
}
