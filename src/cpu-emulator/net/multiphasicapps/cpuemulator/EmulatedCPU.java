// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cpuemulator;

/**
 * This is the base class for any emulated CPU.
 *
 * @since 2016/07/04
 */
public abstract class EmulatedCPU
{
	/** The class which handles accessing of memory. */
	protected final MemoryAccessor memoryaccessor;
	
	/**
	 * Initializes the base emulated CPU.
	 *
	 * @param __ma The accessor used to access memory for read/write.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public EmulatedCPU(MemoryAccessor __ma)
		throws NullPointerException
	{
		// Check
		if (__ma == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.memoryaccessor = __ma;
	}
}

