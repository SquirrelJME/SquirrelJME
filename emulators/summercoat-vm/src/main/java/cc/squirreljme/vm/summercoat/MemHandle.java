// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.jvm.summercoat.SummerCoatUtil;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This represents a basic memory handle.
 *
 * @since 2020/11/28
 */
public class MemHandle
{
	/** The identifier of the handle. */
	protected final int id;
	
	/** The kind of handle this is. */
	protected final int kind;
	
	/** The size of this handle. */
	protected final int size;
	
	/** The raw byte size of this handle. */
	protected final int rawSize;
	
	/** The handle count. */
	private final AtomicInteger _count =
		new AtomicInteger();
	
	/** The handle data. */
	private final byte[] _bytes;
	
	/**
	 * Initializes a new handle.
	 * 
	 * @param __id The identifier for this handle.
	 * @param __kind The kind of memory handle to allocate.
	 * @param __byteSize The number of bytes to allocate.
	 * @param __rawSize The raw size of the handle.
	 * @throws IllegalArgumentException If the kind is not valid or the
	 * requested size is negative.
	 * @since 2021/01/17
	 */
	public MemHandle(int __id, int __kind, int __byteSize, int __rawSize)
		throws IllegalArgumentException
	{
		if (__kind <= 0 || __kind >= MemHandleKind.NUM_KINDS)
			throw new IllegalArgumentException("Invalid kind: " + __kind);
		if (__byteSize < 0 || __rawSize < 0)
			throw new IllegalArgumentException("Negative allocation size.");
		if (!SummerCoatUtil.isArrayKind(__kind) && __byteSize != __rawSize) 
			throw new IllegalArgumentException("Byte/raw size mismatch");
		if (__byteSize < __rawSize)
			throw new IllegalArgumentException("Byte size smaller than raw.");
		
		this.id = __id;
		this.kind = __kind;
		this.size = __byteSize;
		this.rawSize = __rawSize;
		this._bytes = new byte[__rawSize];
	}
	
	/**
	 * Changes the count on the memory handle.
	 * 
	 * @param __up Count this handle up?
	 * @return The new count.
	 * @since 2020/11/28
	 */
	public final int count(boolean __up)
	{
		if (__up)
			return this._count.incrementAndGet();
		return this._count.decrementAndGet();
	}
	
	/**
	 * Sets the explicit memory handle count.
	 * 
	 * @param __count The count to set to.
	 * @since 2021/01/17
	 */
	public final void setCount(int __count)
	{
		this._count.set(__count);
	}
}
