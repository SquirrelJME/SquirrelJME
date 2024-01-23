// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Quick ID Table for lookups and otherwise.
 *
 * @param <I> ID Type.
 * @since 2021/03/13
 */
public final class JDWPIdMap<I extends JDWPHasId>
	extends AbstractMap<Integer, I>  
{
	/** The minimum Id. */
	protected final int minId;
	
	/** Quick table. */
	private final I[] _table;
	
	/**
	 * Generates the quick table.
	 * 
	 * @param __items The items to map.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public JDWPIdMap(I[] __items)
		throws NullPointerException
	{
		if (__items == null)
			throw new NullPointerException("NARG");
		
		// Determine the minimum ID, for value shifting
		int minId = Integer.MAX_VALUE;
		for (I i : __items)
			minId = Math.min(minId, i.debuggerId());
		
		List<I> quick = new ArrayList<>();
		for (I i : __items)
		{
			// Determine where it fits in the table
			int spot = i.debuggerId() - minId;
			
			// Make the table grow to fit the entry
			while (spot >= quick.size())
				quick.add(null);
			
			// Set position
			quick.set(spot, i);
		}
		
		// Set in
		this.minId = minId;
		this._table = quick.<I>toArray(Arrays.copyOf(__items, 0));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public boolean containsKey(Object __key)
	{
		return this.get(__key) != null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public Set<Entry<Integer, I>> entrySet()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 * @param __key
	 */
	@Override
	public I get(Object __key)
	{
		if (!(__key instanceof Integer))
			return null;
		
		// Check if the item is in bounds
		int key = (int)__key;
		int minId = this.minId;
		I[] table = this._table;
		if (key < minId || key >= minId + table.length)
			return null;
		
		return table[key - minId];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public int size()
	{
		return this._table.length;
	}
}
