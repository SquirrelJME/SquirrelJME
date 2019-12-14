// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import java.util.Objects;

/**
 * This represents a class name within the constant pool.
 *
 * @since 2019/11/25
 */
public final class PoolClassName
{
	/** The class name. */
	protected final String name;
	
	/** The component type. */
	protected final PoolClassName componenttype;
	
	/**
	 * Initializes the pool class name.
	 *
	 * @param __n The name of the class.
	 * @param __cp The component type, this is optional.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/25
	 */
	public PoolClassName(String __n, PoolClassName __cp)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.componenttype = __cp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/14
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (!(__o instanceof PoolClassName))
			return false;
		
		PoolClassName o = (PoolClassName)__o;
		return this.name.equals(o.name) &&
			Objects.equals(this.componenttype, o.componenttype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/14
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^
			Objects.hashCode(this.componenttype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/25
	 */
	@Override
	public final String toString()
	{
		return this.name;
	}
}

