// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * This represents the null pool entry.
 *
 * @since 2019/09/22
 */
public final class NullPoolEntry
{
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		return (__o instanceof NullPoolEntry);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final int hashCode()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final String toString()
	{
		return "null";
	}
}

