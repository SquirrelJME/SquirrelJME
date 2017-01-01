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
 * This represents the standard set of program types.
 *
 * @since 2016/08/16
 */
public enum ELFStandardProgramType
	implements ELFProgramType
{
	/** The NULL segment. */
	NULL(0),
	
	/** Load the code. */
	LOAD(1),
	
	/** Dynamic. */
	DYNAMIC(2),
	
	/** The interpreter to use. */
	INTERP(3),
	
	/** Note. */
	NOTE(4),
	
	/** Shared library. */
	SHLIB(5),
	
	/** Program header. */
	PHDR(6),
	
	/** Thread-local storage template. */
	TLS(7),
	
	/** End. */
	;
	
	/** Returns the identifier to use. */
	protected final int id;
	
	/**
	 * Initializes the standard program type.
	 *
	 * @param __i The identifier.
	 * @since 2016/08/16
	 */
	private ELFStandardProgramType(int __i)
	{
		this.id = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/16
	 */
	@Override
	public int identifier()
	{
		return this.id;
	}
}

