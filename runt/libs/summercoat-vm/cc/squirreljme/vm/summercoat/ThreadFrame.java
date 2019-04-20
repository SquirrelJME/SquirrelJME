// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents a single thread frame which stores registers and the PC
 * address.
 *
 * @since 2019/04/19
 */
public final class ThreadFrame
{
	/** Maximum number of used registers. */
	public static final int MAX_REGISTERS =
		64;
	
	/** The constant pool of the method. */
	public final RuntimeConstantPool pool;
	
	/** The frame index. */
	public final int index;
	
	/** Registers. */
	public final int[] registers =
		new int[MAX_REGISTERS];
	
	/** Reference queue. */
	public final int[] refq =
		new int[MAX_REGISTERS];
	
	/** The method code. */
	public final byte[] code;
	
	/** The PC address. */
	public volatile int pc;
	
	/** Reference pointer. */
	public volatile int refp;
	
	/**
	 * Initializes the thread frame.
	 *
	 * @param __cp The constant pool to use.
	 * @param __cd The method code.
	 * @param __fdx The frame index.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/19
	 */
	public ThreadFrame(RuntimeConstantPool __cp, byte[] __cd, int __fdx)
		throws NullPointerException
	{
		if (__cp == null || __cd == null)
			throw new NullPointerException("NARG");
		
		this.pool = __cp;
		this.code = __cd;
		this.index = __fdx;
	}
}

