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
 * This is used to build the register based code which is for later execution.
 *
 * @since 2019/01/22
 */
public final class RegisterCodeBuilder
{
	/** The VTable for this class. */
	protected final VTableBuilder vtable;
	
	/**
	 * Initializes the code builder.
	 *
	 * @param __vt The VTable.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/22
	 */
	public RegisterCodeBuilder(VTableBuilder __vt)
		throws NullPointerException
	{
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		this.vtable = __vt;
	}
}

