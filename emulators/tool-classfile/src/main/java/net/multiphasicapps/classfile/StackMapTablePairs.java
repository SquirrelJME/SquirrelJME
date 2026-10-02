// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.util.IntegerArrays;
import cc.squirreljme.runtime.cldc.util.IntegerIntegerArray;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * Stack map table pairs.
 *
 * @since 2023/07/03
 */
public final class StackMapTablePairs
	implements Contexual
{
	/** Stack map pairs. */
	private final Map<Integer, StackMapTablePair> _pairs;
	
	/**
	 * Initializes the stack map table pairs.
	 * 
	 * @param __inputs The inputs.
	 * @param __outputs The outputs.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	StackMapTablePairs(Map<Integer, StackMapTableState> __inputs,
		Map<Integer, StackMapTableState> __outputs)
		throws NullPointerException
	{
		if (__inputs == null || __outputs == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC01 Stack map inputs and outputs do not match.} */
		if (!__inputs.keySet().equals(__outputs.keySet()))
			throw new IllegalArgumentException("JC01");
		
		// Reference everything in
		Map<Integer, StackMapTablePair> result = new SortedTreeMap<>();
		for (Integer addr : __inputs.keySet())
		{
			result.put(addr, new StackMapTablePair(
				__inputs.get(addr), __outputs.get(addr)));
		}
		
		this._pairs = UnmodifiableMap.of(result);
	}
	
	/**
	 * Returns all the available addresses.
	 *
	 * @return The resultant addresses within the stack map.
	 * @since 2023/08/09
	 */
	public int[] addresses()
	{
		return IntegerArrays.toIntArray(this._pairs.keySet());
	}
	
	/**
	 * Gets the pair at the given address.
	 * 
	 * @param __address The address to get.
	 * @return The pair for the stack map.
	 * @throws IllegalArgumentException If the address is not valid.
	 * @since 2023/07/03
	 */
	public StackMapTablePair get(int __address)
		throws IllegalArgumentException
	{
		StackMapTablePair rv = this._pairs.get(__address);
		
		if (rv == null)
			throw new IllegalArgumentException("IOOB");
		return rv;
	}
	
	/**
	 * Returns the number of pairs.
	 *
	 * @return The number of pairs.
	 * @since 2023/08/09
	 */
	public int size()
	{
		return this._pairs.size();
	}
}
