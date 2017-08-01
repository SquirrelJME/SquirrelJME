// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is the stack map table which is used to verify the types of local and
 * stack variables along with the stack length are valid for certain
 * positions within the instruction.
 *
 * This just contains the type and potential initialization information for
 * instructions which are targets of jumps.
 *
 * @since 2017/07/15
 */
public final class StackMapTable
{
	/** The instruction address lookup. */
	private final int[] _address;
	
	/** Stack map tables for each address. */
	private final StackMapTableState[] _tables;
	
	/** String representation of this table. */
	private volatile Reference<String> _string;
	
	/**
	 * Constructs the stack map table.
	 *
	 * @param __t States for instruction addresses.
	 * @throws NullPointerException On null arguments or null keys/values.
	 * @since 2017/07/28
	 */
	public StackMapTable(Map<Integer, StackMapTableState> __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Setup target lists
		int n = __t.size();
		List<Integer> address = new ArrayList<>(n);
		List<StackMapTableState> tables = new ArrayList<>(n);
		
		// Go through elements and insert into the table
		for (Map.Entry<Integer, StackMapTableState> e : __t.entrySet())
		{
			Integer k = e.getKey();
			StackMapTableState v = e.getValue();
			
			// Cannot be null
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error JI1w The stack map table contains a table
			// for an address which already has a table. (The addres)}
			int dx = Collections.binarySearch(address, k);
			if (dx >= 0)
				throw new JITException(String.format("JI1w %d", dx));
			
			// Get the real insertion point
			dx = -dx - 1;
			
			// Insert
			address.add(dx, k);
			tables.add(dx, v);
		}
		
		// Fixate
		n = address.size();
		int[] iaddr = new int[n];
		for (int i = 0; i < n; i++)
			iaddr[i] = address.get(i);
		this._address = iaddr;
		this._tables = tables.<StackMapTableState>toArray(
			new StackMapTableState[tables.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			int[] address = this._address;
			StackMapTableState[] tables = this._tables;
			
			// Build string
			StringBuilder sb = new StringBuilder("{");
			for (int i = 0, n = address.length; i < n; i++)
			{
				if (i > 0)
					sb.append(", ");
				
				sb.append(address[i]);
				sb.append('=');
				sb.append(tables[i]);
			}
			sb.append("}");
			
			// Finish
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

