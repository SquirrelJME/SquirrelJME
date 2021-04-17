// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.util.Objects;

/**
 * Represents a location within the debugger.
 *
 * @since 2021/04/17
 */
public final class JDWPLocation
{
	/** The location within the method. */
	protected final long codeDx;
	
	/** The method index from the type. */
	protected final int methodDx;
	
	/** The type where this is located. */
	protected final Object type;
	
	/**
	 * Initializes the location.
	 * 
	 * @param __type The type used.
	 * @param __methodDx The method index.
	 * @param __codeDx The code index.
	 * @since 2021/04/17
	 */
	public JDWPLocation(Object __type, int __methodDx, long __codeDx)
	{
		this.type = __type;
		this.methodDx = __methodDx;
		this.codeDx = __codeDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JDWPLocation))
			return false;
		
		JDWPLocation o = (JDWPLocation)__o;
		return Objects.equals(this.type, o.type) &&
			this.methodDx == o.methodDx &&
			this.codeDx == o.codeDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public int hashCode()
	{
		long codeDx = this.codeDx;
		return System.identityHashCode(this.type) ^
			~this.methodDx ^ ((int)codeDx | (int)(codeDx >>> 32));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public String toString()
	{
		return String.format("Location[%s:%d @%d",
			this.type, this.methodDx, this.codeDx);
	}
}
