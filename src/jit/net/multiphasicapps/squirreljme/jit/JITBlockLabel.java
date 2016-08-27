// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is a label which is attached to a grouping of basic blocks within the
 * method program. This is used for jumping purposes, to other regions of the
 * byte code.
 *
 * @since 2016/08/27
 */
@Deprecated
public final class JITBlockLabel
{
	/** The label's ID. */
	protected final int id;
	
	/**
	 * Initializes the basic block level.
	 *
	 * @param __id The identification of the block.
	 * @since 2016/08/27
	 */
	public JITBlockLabel(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Must be this
		if (!(__o instanceof JITBlockLabel))
			return false;
		
		return this.id == ((JITBlockLabel)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public final int hashCode()
	{
		return this.id;
	}
	
	/**
	 * Returns the block label ID.
	 *
	 * @return The ID used.
	 * @since 2016/08/27
	 */
	public final int id()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	public String toString()
	{
		return Integer.toString(this.id);
	}
}

