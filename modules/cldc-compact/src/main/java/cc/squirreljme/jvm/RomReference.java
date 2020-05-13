// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents a single ROM reference which is used to wrap the standard
 * long identifier used to refer to ROMs.
 *
 * @since 2020/05/12
 */
public final class RomReference
{
	/** The reference ID. */
	protected final long reference;
	
	/**
	 * Initializes the reference.
	 *
	 * @param __ref The ROM reference ID.
	 * @since 2020/05/12
	 */
	public RomReference(long __ref)
	{
		this.reference = __ref;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/12
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/12
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
