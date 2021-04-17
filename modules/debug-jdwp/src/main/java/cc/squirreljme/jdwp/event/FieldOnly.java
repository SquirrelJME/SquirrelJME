// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Only valid in a given field.
 *
 * @since 2021/04/17
 */
public final class FieldOnly
{
	/** The type the field is in. */
	protected final Object type;
	
	/** The field index. */
	protected final int fieldDx;
	
	/**
	 * Represents a field only match.
	 * 
	 * @param __type The type containing the field.
	 * @param __fieldDx The field index.
	 * @since 2021/04/17
	 */
	public FieldOnly(Object __type, int __fieldDx)
	{
		this.type = __type;
		this.fieldDx = __fieldDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
