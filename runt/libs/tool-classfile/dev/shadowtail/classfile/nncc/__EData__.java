// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

/**
 * Exception storage data.
 *
 * @since 2019/04/11
 */
final class __EData__
{	
	/** The address. */
	public final int addr;
	
	/** The line. */
	public final int line;
	
	/** The used label. */
	public final NativeCodeLabel label;
	
	/**
	 * Initializes the data.
	 *
	 * @param __a The address.
	 * @param __ln The line.
	 * @param __lab The label to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	__EData__(int __a, int __ln, NativeCodeLabel __lab)
		throws NullPointerException
	{
		if (__lab == null)
			throw new NullPointerException("NARG");
		
		this.addr = __a;
		this.line = __ln;
		this.label = __lab;
	}
}

