// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.summercoat.register;

import dev.shadowtail.classfile.nncc.NativeCode;

/**
 * Reference to the run-time pool pointer.
 *
 * @since 2020/11/24
 */
public final class RuntimePoolPointer
	extends Register
{
	/** The current pool pointer. */
	public static final RuntimePoolPointer CURRENT =
		new RuntimePoolPointer(NativeCode.POOL_REGISTER);
	
	/** The next pool pointer. */
	public static final RuntimePoolPointer NEXT =
		new RuntimePoolPointer(NativeCode.NEXT_POOL_REGISTER);
	
	/**
	 * Initializes the basic register.
	 *
	 * @param __register The register to get.
	 * @since 2020/11/24
	 */
	public RuntimePoolPointer(int __register)
	{
		super(__register);
	}
}
