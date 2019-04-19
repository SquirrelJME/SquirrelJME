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
	
	/** Registers. */
	public final int[] r =
		new int[MAX_REGISTERS];
	
	/** The PC address. */
	public int pc;
	
	/**
	 * Initializes the thread frame.
	 *
	 * @param __cp The constant pool to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/19
	 */
	public ThreadFrame(RuntimeConstantPool __cp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		this.pool = __cp;
	}
}

