// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

/**
 * This represents a jump replacement.
 *
 * @since 2019/05/29
 */
final class __Jump__
{
	/** Instruction index. */
	public final int cdx;
	
	/** Data position size. */
	public final int dss;
	
	/** The jump target. */
	public final int jt;
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __cdx The instruction index.
	 * @param __dss The DSS.
	 * @param __jt The jump target.
	 * @since 2019/05/29
	 */
	__Jump__(int __cdx, int __dss, int __jt)
	{
		this.cdx = __cdx;
		this.dss = __dss;
		this.jt = __jt;
	}
}

