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
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Iterator;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableMap;
import org.jetbrains.annotations.NotNull;

/**
 * Huffman table used for compression.
 *
 * @since 2024/06/03
 */
public final class HuffTable
	implements Iterable<Map.Entry<ChainList, HuffBits>>
{
	/** The huffman table. */
	protected final Map<ChainList, HuffBits> table;
	
	/**
	 * Initializes the huffman table.
	 *
	 * @param __table The table to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public HuffTable(Map<ChainList, HuffBits> __table)
		throws NullPointerException
	{
		if (__table == null)
			throw new NullPointerException("NARG");
		
		// Copy the raw table
		__table = new SortedTreeMap<>(__table);
		this.table = UnmodifiableMap.of(__table);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if (!(__o instanceof HuffTable))
			return false;
		
		HuffTable o = (HuffTable)__o;
		return this.table.equals(o.table);
	}
	
	/**
	 * Finds the given sequence.
	 *
	 * @param __seq The sequence to find.
	 * @return The resultant bits in the table, or {@code null} if it was
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public HuffBits find(ChainList __seq)
		throws NullPointerException
	{
		if (__seq == null)
			throw new NullPointerException("NARG");
		
		return this.table.get(__seq);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public int hashCode()
	{
		return this.table.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public Iterator<Map.Entry<ChainList, HuffBits>> iterator()
	{
		return this.table.entrySet().iterator();
	}
	
	/**
	 * Returns the table size.
	 *
	 * @return The table size.
	 * @since 2024/06/03
	 */
	public int size()
	{
		return this.table.size();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		return this.table.toString();
	}
}
