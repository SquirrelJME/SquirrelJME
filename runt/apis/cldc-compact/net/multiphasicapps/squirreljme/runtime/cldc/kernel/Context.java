// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.kernel;

/**
 * This represents the context in which represents the calling task performing
 * the kernel operation.
 *
 * The system context always has the zero ID
 *
 * @since 2017/12/08
 */
public final class Context
	implements Comparable<Context>
{
	/** The ID of the context. */
	protected final int id;
	
	/**
	 * Internally initialized.
	 *
	 * @param __id The context identifier.
	 * @since 2017/12/08
	 */
	Context(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public int compareTo(Context __o)
	{
		return this.id - __o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Context))
			return false;
		
		return this.id == ((Context)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * Returns the context identifier.
	 *
	 * @return The context identifier.
	 * @since 2017/12/08
	 */
	public int id()
	{
		return this.id;
	}
	
	/**
	 * Is this the system context?
	 *
	 * @return If this is the system context or not.
	 * @since 2017/12/08
	 */
	public boolean isSystem()
	{
		return this.id == 0;
	}
}

