// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

/**
 * This represents a flag within a program header.
 *
 * @since 2016/08/16
 */
public enum ELFProgramFlag
{
	/** Can be executed. */
	EXECUTE(1),
	
	/** Can be written to. */
	WRITE(2),
	
	/** Can be read from. */
	READ(4),
	
	/** End. */
	;
	
	/** The mask to use. */
	protected final int mask;
	
	/**
	 * Initializes the program flag information.
	 *
	 * @param __mask The mask to use.
	 * @since 2016/08/16
	 */
	private ELFProgramFlag(int __mask)
	{
		this.mask = __mask;
	}
	
	/**
	 * Returns the flag mask.
	 *
	 * @return The flag mask.
	 * @since 2016/08/16
	 */
	public int mask()
	{
		return this.mask;
	}
}

