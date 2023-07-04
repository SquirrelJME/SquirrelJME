// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import cc.squirreljme.c.CExpression;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Objects;

/**
 * Represents a container for linkage.
 *
 * @param <L> The contained linkage type.
 * @since 2023/06/03
 */
public final class Container<L extends Linkage>
{
	/** The container index. */
	protected final int index;
	
	/** The linkage this contains. */
	protected final L linkage;
	
	/**
	 * Initializes the container.
	 * 
	 * @param __index The index used.
	 * @param __linkage The linkage this contains.
	 * @throws IllegalArgumentException If the index is zero or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/03
	 */
	public Container(int __index, L __linkage)
		throws IllegalArgumentException, NullPointerException
	{
		if (__index <= 0)
			throw new IllegalArgumentException("INEG");
		if (__linkage == null)
			throw new NullPointerException("NARG");
		
		this.index = __index;
		this.linkage = __linkage;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/03
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof Container))
			return false;
		
		Container<?> o = (Container<?>)__o;
		return this.index == o.index &&
			Objects.equals(this.linkage, o.linkage);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/03
	 */
	@Override
	public int hashCode()
	{
		return this.index + this.linkage.hashCode();
	}
	
	/**
	 * Returns the index of the class linkage.
	 * 
	 * @return The linkage index.
	 * @since 2023/06/03
	 */
	public int index()
	{
		return this.index;
	}
}
