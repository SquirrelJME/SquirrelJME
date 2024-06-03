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
import java.util.List;

/**
 * This represents a chain list, which has a starting point and vector codes.
 *
 * @since 2024/05/27
 */
public class VectorChain
	implements Iterable<ChainCode>
{
	/** Chain codes. */
	public final ChainList codes;
	
	/** The starting point for this chain. */
	public final VectorPoint point;
	
	/**
	 * Initializes the vector chain.
	 *
	 * @param __x The starting X position.
	 * @param __y The starting Y position.
	 * @param __hole Is this a hole?
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public VectorChain(int __x, int __y, boolean __hole, ChainList __codes)
		throws NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the vector chain.
	 *
	 * @param __point The chain point.
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/29
	 */
	public VectorChain(VectorPoint __point, ChainList __codes)
		throws NullPointerException
	{
		if (__point == null || __codes == null)
			throw new NullPointerException("NARG");
		
		this.point = __point;
		this.codes = __codes;
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
		else if ((!(__o instanceof VectorChain)))
			return false;
		
		VectorChain o = (VectorChain)__o;
		return this.point.equals(o.point) &&
			this.codes.equals(o.codes);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public int hashCode()
	{
		return this.point.hashCode() ^ this.codes.hashCode();
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
}
