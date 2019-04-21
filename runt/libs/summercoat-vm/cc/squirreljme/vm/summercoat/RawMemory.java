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
 * DESCRIBE THIS.
 *
 * @since 2019/04/21
 */
public final class RawMemory
	implements ReadableMemory
{
	/** The memory offset. */
	protected final int offset;
	
	/** The memory size. */
	protected final int size;
	
	/** The memory data. */
	private final byte[] _data;
	
	/**
	 * Raw memory space.
	 *
	 * @param __off The offset.
	 * @param __sz The size of this region.
	 * @since 2019/04/21
	 */
	public RawMemory(int __off, int __sz)
	{
		this.offset = __off;
		this.size = __sz;
		this._data = new byte[__sz];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memReadInt(int __addr)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionOffset()
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
		return this.size;
	}
}

