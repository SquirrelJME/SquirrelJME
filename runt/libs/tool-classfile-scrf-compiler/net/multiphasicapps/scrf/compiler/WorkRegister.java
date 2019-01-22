// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

/**
 * This represents a single working register, which has a given index.
 *
 * @since 2019/01/21
 */
public final class WorkRegister
{
	/** The index of this work register. */
	protected final int index;
	
	/**
	 * Initializes the work register.
	 *
	 * @param __idx The index of this register.
	 * @since 2019/01/21
	 */
	public WorkRegister(int __idx)
	{
		this.index = __idx;
	}
}

