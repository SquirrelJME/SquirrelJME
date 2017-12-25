// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This represents a single program which exists within the kernel and maps
 * with suites within the Java ME environment. Each program is identified by
 * an identifier which represents the program index. The index remains constant
 * for the same program (unless that program has been changed). The index is
 * used to refer to the program slot.
 *
 * @since 2017/12/11
 */
public final class KernelProgram
{
	/** The native program reference. */
	protected final NativeProgram program;
	
	/** The index of the program. */
	protected final int index;
	
	/**
	 * Initializes the base program.
	 *
	 * @param __np The reference to the native program.
	 * @param __dx The index of the program, the slot it is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/25
	 */
	KernelProgram(NativeProgram __np, int __dx)
		throws NullPointerException
	{
		if (__np == null)
			throw new NullPointerException("NARG");
		
		this.program = __np;
		this.index = __dx;
	}
	
	/**
	 * Returns the program index.
	 *
	 * @return The program index.
	 * @since 2017/12/25
	 */
	public final int index()
	{
		return this.index;
	}
}

