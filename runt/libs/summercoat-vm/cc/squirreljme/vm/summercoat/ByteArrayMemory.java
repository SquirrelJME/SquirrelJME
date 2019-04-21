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
 * This is a region of memory which uses a read-only byte array.
 *
 * @since 2019/04/21
 */
public final class ByteArrayMemory
	implements ReadableMemory
{
	/** The offset to this address. */
	protected final int offset;
	
	/** The size of the byte array. */
	protected final int size;
	
	/** Offset into the byte array. */
	protected final int boff;
	
	/** The backing byte array. */
	private final byte[] _bytes;
	
	/**
	 * Initializes the byte array memory.
	 *
	 * @param __mo The memory offset.
	 * @param __b The memory bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public ByteArrayMemory(int __mo, byte[] __b)
		throws NullPointerException
	{
		this(__mo, __b, 0, __b.length);
	}
	
	/**
	 * Initializes the byte array memory.
	 *
	 * @param __mo The memory offset.
	 * @param __b The memory bytes.
	 * @param __o The array offset.
	 * @param __l The number of bytes to access.
	 * @throws IndexOutOfBoundsException If the byte array offset and/or
	 * length exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public ByteArrayMemory(int __mo, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this.offset = __mo;
		this.size = __l;
		this.boff = __o;
		this._bytes = __b;
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
		return this.size;
	}
}

