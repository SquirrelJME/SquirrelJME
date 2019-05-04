// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

/**
 * Represents a memory operation to do.
 *
 * @since 2019/04/30
 */
public final class Operation
{
	/** The value modifier. */
	public final Modifier mod;
	
	/** Operation size. */
	public final int size;
	
	/** The address. */
	public final int addr;
	
	/**
	 * Initializes the operation.
	 *
	 * @param __m The modifier.
	 * @param __s The operation size.
	 * @param __a The address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	public Operation(Modifier __m, int __s, int __a)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.mod = __m;
		this.size = __s;
		this.addr = __a;
	}
}
