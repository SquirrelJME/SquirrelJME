// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

/**
 * This is a reference to a Class's pool pointer.
 *
 * @since 2019/12/15
 */
public final class PoolClassPoolPointer
{
	/** The name of the class. */
	protected final String name;
	
	/**
	 * Initializes the entry.
	 *
	 * @param __n The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/15
	 */
	public PoolClassPoolPointer(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/15
	 */
	@Override
	public final String toString()
	{
		return this.name;
	}
}
