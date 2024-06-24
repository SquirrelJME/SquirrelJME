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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Not Described.
 *
 * @since 2024/06/07
 */
final class __HuffBitsIterator__
	implements Iterator<Boolean>
{
	/** The bits to iterate over. */
	protected final HuffBits bits;
	
	/** The bit we are at. */
	private int _at;
	
	/**
	 * Initializes the iterator.
	 *
	 * @param __bits The bits to iterate over.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/07
	 */
	__HuffBitsIterator__(HuffBits __bits)
		throws NullPointerException
	{
		if (__bits == null)
			throw new NullPointerException("NARG");
		
		this.bits = __bits;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public boolean hasNext()
	{
		return this._at < this.bits.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public Boolean next()
		throws NoSuchElementException
	{
		HuffBits bits = this.bits;
		int at = this._at;
		
		if (at >= bits.length())
			throw new NoSuchElementException("NSEE");
		
		this._at = at + 1;
		return bits.get(at);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
}
