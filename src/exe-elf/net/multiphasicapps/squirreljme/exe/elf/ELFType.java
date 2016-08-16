// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

/**
 * Represents the type of ELF that this is.
 *
 * @since 2016/08/15
 */
public enum ELFType
{
	/** Relocatable executable. */
	RELOCATABLE(1),
	
	/** Executable. */
	EXECUTABLE(2),
	
	/** Shared library. */
	SHARED(3),
	
	/** Core (dump). */
	CORE(4),
	
	/** End. */
	;
	
	/** The identifier. */
	protected final int id;
	
	/**
	 * Initializes the type identifier.
	 *
	 * @param __id The identifier to use.
	 * @since 2016/08/15
	 */
	private ELFType(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * Returns the identifier for this type.
	 *
	 * @return The type identifier.
	 * @since 2016/08/15
	 */
	public int identifier()
	{
		return this.id;
	}
}

