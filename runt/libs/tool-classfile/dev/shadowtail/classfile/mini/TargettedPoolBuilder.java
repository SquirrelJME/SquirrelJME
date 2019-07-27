// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;

/**
 * This is used to to build either an aliased pool or a standard pool depending
 * on which setting is used.
 *
 * @since 2019/07/27
 */
public final class TargettedPoolBuilder
{
	/** Is this working in aliased mode? */
	protected final boolean isaliaspool;
	
	/** The target dual pool. */
	protected final DualClassRuntimePoolBuilder dualpool;
	
	/**
	 * Initializes the targetted pool builder.
	 *
	 * @param __ap The alias pool.
	 * @param __dp The dual-pool.
	 * @since 2019/07/27
	 */
	public TargettedPoolBuilder(boolean __ap, DualClassRuntimePoolBuilder __dp)
		throws NullPointerException
	{
		if (__dp == null)
			throw new NullPointerException("NARG");
		
		this.isaliaspool = __ap;
		this.dualpool = __dp;
	}
}

