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
 * This is an accessor which allows mapping of memory via .
 *
 * @since 2016/07/04
 */
public class MappedMemoryAccessor
	extends MemoryAccessor
{
	/** The normal accessor to memory. */
	protected final MemoryAccessor accessor;
	
	public MappedMemoryAccessor(MemoryAccessor __ma)
		throws NullPointerException
	{
		// Check
		if (__ma == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.accessor = __ma;
	}
}

