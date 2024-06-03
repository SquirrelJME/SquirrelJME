// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Huffman splice item.
 *
 * @since 2024/06/03
 */
public final class HuffSpliceItem
{
	/** The used list. */
	public final ChainList list;
	
	/** The number of times this appears. */
	public final int count;
	
	/**
	 * Initializes the splice item.
	 *
	 * @param __list The list to use.
	 * @param __count The count for the item.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public HuffSpliceItem(ChainList __list, int __count)
		throws NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		this.list = __list;
		this.count = __count;
	}
	
	/**
	 * Returns the chain list.
	 *
	 * @return The chain list.
	 * @since 2024/06/03
	 */
	public ChainList getList()
	{
		return this.list;
	}
	
	/**
	 * Returns the count.
	 *
	 * @return The count.
	 * @since 2024/06/03
	 */
	public int count()
	{
		return this.count;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		return this.list + "=" + this.count;
	}
}
