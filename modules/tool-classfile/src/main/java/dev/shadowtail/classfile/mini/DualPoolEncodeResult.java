// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

/**
 * This contains the result of an encoded dual-pool.
 *
 * @since 2019/07/17
 */
public final class DualPoolEncodeResult
{
	/** Static constant pool offset. */
	public final int staticpooloff;
	
	/** Static constant pool size. */
	public final int staticpoolsize;
	
	/** Runtime constant pool offset. */
	public final int runtimepooloff;
	
	/** Runtime constant pool size. */
	public final int runtimepoolsize;
	
	/**
	 * Initializes the result of the dual-encoder.
	 *
	 * @param __spo The static pool offset.
	 * @param __spz The static pool size.
	 * @param __rpo The run-time pool offset.
	 * @param __rpz The run-time pool size.
	 * @since 2019/07/17
	 */
	public DualPoolEncodeResult(int __spo, int __spz, int __rpo, int __rpz)
	{
		this.staticpooloff = __spo;
		this.staticpoolsize = __spz;
		this.runtimepooloff = __rpo;
		this.runtimepoolsize = __rpz;
	}
}

