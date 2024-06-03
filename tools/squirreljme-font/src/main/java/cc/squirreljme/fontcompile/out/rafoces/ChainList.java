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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This is a list of chain codes which can be used in a table to repeat
 * duplicated shapes in glyphs.
 *
 * @since 2024/05/27
 */
public final class ChainList
	implements Iterable<ChainCode>
{
	/** Chain codes. */
	private final ChainCode[] _codes;
	
	/** The hashcode. */
	private volatile int _hash;
	
	/**
	 * Initializes the chain list.
	 *
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public ChainList(ChainCode... __codes)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the chain list.
	 *
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public ChainList(List<ChainCode> __codes)
		throws NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		// Copy in
		int n = __codes.size();
		ChainCode[] codes = new ChainCode[n];
		for (int i = 0; i < n; i++)
		{
			ChainCode code = __codes.get(i);
			if (code == null)
				throw new NullPointerException("NARG");
			
			codes[i] = code;
		}
		
		// Set
		this._codes = codes;
	}
	
	/**
	 * The number of flat bits needed to represent this chain.
	 *
	 * @return Flattened bits needed to represent chain.
	 * @since 2024/06/03
	 */
	public int bitsFlat()
	{
		return this._codes.length * 2;
	}
	
	/**
	 * Bits needed using simple huffman compression.
	 *
	 * @return The number of bits needed for simple huffman compression.
	 * @since 2024/06/03
	 */
	public int bitsSimpleHuffman()
	{
		int total = 0;
		
		for (ChainCode code : this._codes)
			if (code == ChainCode.STRAIGHT)
				total += 1;
			else
				total += 2;
		
		return total;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if ((!(__o instanceof ChainList)))
			return false;
		else if (this.hashCode() != __o.hashCode())
			return false;
		
		ChainList o = (ChainList)__o;
		return Arrays.equals(this._codes, o._codes);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public int hashCode()
	{
		int result = this._hash;
		if (result != 0)
			return result;
		
		result = Arrays.asList(this._codes).hashCode();
		this._hash = result;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public Iterator<ChainCode> iterator()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of codes in the list.
	 *
	 * @return The code list size.
	 * @since 2024/06/03
	 */
	public int size()
	{
		return this._codes.length;
	}
}
