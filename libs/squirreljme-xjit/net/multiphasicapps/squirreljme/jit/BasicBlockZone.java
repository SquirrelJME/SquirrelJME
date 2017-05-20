// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is a part of the program which contains {@link BasicBlock}s. The basic
 * block zones encompass a region of the program in {@link ProgramState} and
 * contains multiple basic blocks as needed for the correct input state.
 *
 * @see BasicBlock
 * @since 2017/05/13
 */
public class BasicBlockZone
{
	/** The byte code used. */
	protected final ByteCode code;
	
	/** The base address. */
	protected final int baseaddress;
	
	/** The end address. */
	protected final int endaddress;
	
	/**
	 * Initializes the basic block zone.
	 *
	 * @param __code The code the block is in.
	 * @param __base The base address of the zone.
	 * @param __end The end address of the zone.
	 * @throws NullPointerException On null arguments.
	 * @sine 2017/05/20
	 */
	BasicBlockZone(ByteCode __code, int __base, int __end)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.code = __code;
		this.baseaddress = __base;
		this.endaddress = __end;
	}
}

